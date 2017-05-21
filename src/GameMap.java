import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The {@code GameMap Class} is the Model, in the MVC scheme.
 * It stores all the map data for the WareHouse Bros game, 
 * a puzzle game where the player must move boxes to the goal.
 * The {@code readMap} method is used to convert a txt file
 * into a token array for rendering.
 */
public class GameMap {
	
	/** A 2D token array, storing all object on the map
	 * The row and column index in the array correspond to
	 * coordinates on the map 
	 */
	private Token[][] map = null;
	
	/** The dimension of the map. i.e. an 8x8 grid has dimension 8
	 * Only one dimension is specified since all maps are square
	 */
	private int dimension;
	
	/** The number of boxes on the map*/
	private int numBoxes;
	
	/** An array list storing all the maps for a particular difficulty. */
	private ArrayList<String> maps;
	
	/**
	 * The current level being played, indexed from 0.
	 */
	private int currLevel;
	
	/** A pointer to the player object for easy referencing */
	private Player player;
	
	/**
	 * Instantiates a new game map.
	 * @throws IOException 
	 */
	public GameMap(ArrayList<String> maps) throws IOException{
	    this.maps = maps;
	    readMap(maps.get(0));
	    currLevel = 0;
	}
	
	/** 
	 * This function loads the next level, it is called when a level is finished.
	 */
	public void loadNextLevel() {
	    currLevel++;
	    if (currLevel < maps.size()) {
            try {
                readMap(maps.get(currLevel));
            } catch (IOException e) {
                e.printStackTrace();
            }
	    } else {
	        System.out.println("You Won!");
	        System.exit(0);
	    }
	}
	/**
	 * @return the dimension of the map
	 */
	public int getDimension(){
		return dimension;
	}
	
	/**
	 * @return the number of goals that have not been completed
	 */
	public int getNumGoals(){
	    int countGoal = 0;
        for(Token i[]: map) {
            for(Token j: i) {
                if (j instanceof Goal){
                    countGoal++;
                } else if (j instanceof Player){
                    if(((Player) j).isOnGoal()){
                        countGoal++;
                    }
                }
            }
        }
        return countGoal;
	}
	
	/**
	 * @return the number of boxes on the map
	 */
	public int getNumBoxes(){
		return numBoxes;
	}
	
	/**
	 * @return the player object on the map
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * @return A 2D token array storing the map information.
	 */
	public Token[][] getMap(){
		return map;
	}
	
	/**
	 * Reads a text file containing an ascii map
	 * and initialises a token array.
	 *
	 * @param fileName the file name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    public void readMap(String fileName) throws IOException {
    	FileReader f = new FileReader(fileName);
		int row = 0, column = 0;
		int tempRow =0, tempColumn = 0;
		Token[][] map = null;
		
		try(BufferedReader br = new BufferedReader(f)) {
		    String line = br.readLine();
		    boolean mapRead = false;
		    
		    // read files and create object array
		    while (line != null) {
		    	
				// match the string using split
				String[] command = line.split("");
				if (command[0].equals("r")){
					if(command.length == 3)	row = Integer.parseInt(command[1]+command[2]);
					else row = Integer.parseInt(command[1]);
					this.dimension = row;
				} else if (command[0].equals("c")) {
					if(command.length == 3)	column = Integer.parseInt(command[1]+command[2]);
					else column = Integer.parseInt(command[1]);
				} else {
					if(mapRead==false){
						map = new Token[row][column];
						mapRead = true;
					}
					for(tempColumn = 0; tempColumn<column; tempColumn++){
						if(command[tempColumn].equals("W")){
							map[tempRow][tempColumn] = new Wall(tempRow,tempColumn);
						} else if(command[tempColumn].equals(" ")){
						
						} else if(command[tempColumn].equals("B")){
							map[tempRow][tempColumn] = new Box(tempRow,tempColumn);
						} else if(command[tempColumn].equals("P")){
							player = new Player(tempRow,tempColumn);
							map[tempRow][tempColumn] = player;
						} else if(command[tempColumn].equals("G")){
							map[tempRow][tempColumn] = new Goal(tempRow,tempColumn);
						}
						
					}
				}
				if(mapRead == true){
					tempRow++;
					if (tempRow == row) break;
				}
		        line = br.readLine();
		    }
		}
		this.map = map;
	}
    
    /**
     * gets the current level of the map
     */
	public int getCurrLevel() {
		return currLevel;
	}
    
}
