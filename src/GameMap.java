import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
	 * coordinates on the map */
	private Token[][] map = null;
	
	/** The dimension of the map. i.e. an 8x8 grid has dimension 8
	 * Only one dimension is specified since all maps are square*/
	private int dimension;
	
	/** The number of boxes on the map*/
	private int numBoxes;
	
	/** A pointer to the player object for easy referencing */
	private Player player;
	
	/**
	 * Instantiates a new game map.
	 * @throws IOException 
	 */
	public GameMap(String fileName) throws IOException{
	    readMap(fileName);
	}
	
	/**
	 * Fets the dimension of the map.
	 *
	 * @return the dimension
	 */
	public int getDimension(){
		return dimension;
	}
	
	/**
	 * Fets the number of goals on the map.
	 *
	 * @return the num goals
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
	 * gets the number of boxes of the map.
	 *
	 * @return the num boxes
	 */
	public int getNumBoxes(){
		return numBoxes;
	}
	
	/**
	 * Gets the player.
	 *
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Gets the map of the game in a 2d array of objects.
	 *
	 * @return the map
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
    private void readMap(String fileName) throws IOException {
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
}
