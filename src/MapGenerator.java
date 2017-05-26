import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class MapGenerator {
	
	/**
	 * This function creates a map
	 * @param dimension is the dimension of the map. Currently it's always 8.
	 * @return
	 */
	public Token[][] createMap(int dimension){
		
		// creates a square map
		Token[][] map = new Token[dimension][dimension];
		
		// fills the edge of the map with walls
		for (int i = 0; i < dimension; i++){
			if (i == 0 || i == dimension - 1){
				for (int j = 0; j <dimension; j++){
					//System.out.println("Creating wall at index " + i + " " + j );
					map[i][j] = new Wall(i,j);
					//System.out.println("Wall created" );
				}
			} else {
				map[i][0] = new Wall(i,0);
				map[i][dimension - 1] = new Wall(i,dimension - 1);
			}
		}

		int numTemplates = 4;
		int block = 4;
		int blockRow = 1;
		int blockCol = 1;
		
		for (int i = 0; i < block/2; i++){
			int templateNum = randomise(numTemplates,0);
			Token[][] template = getTemplate(templateNum);
			System.out.println("template chosen is " + templateNum);
			addTemplate(map, template, blockRow, blockCol);
			blockCol = blockCol + 3;
		}
		
		blockCol = 4;
		
		for (int i = 0; i < block/2; i++){
			int templateNum = randomise(numTemplates,0);
			Token[][] template = getTemplate(templateNum);
			System.out.println("template chosen is " + templateNum);
			addTemplate(map, template, blockRow, blockCol);
			blockRow = blockRow + 3;
		}
		
		addPBG(map, dimension, 1);
		return map;	
	}
	
	/**
	 * Adds a player, box and goal token to the map
	 * @param map is a 2d array of token
	 * @param dimension is the size of map (8x8)
	 * @param numBG number of goals and boxes (currently is 1, havent tried it with more than 1)
	 */
	public void addPBG(Token[][] map, int dimension, int numBG){
		boolean pPlaced = false;
		boolean bPlaced = false;
		boolean gPlaced = false;
		
		Token[] boxes = new Token[numBG];
		//Token[] goals = new Token[numBG];

		Player thePlayer = new Player(0,0);
		
		while (pPlaced == false || bPlaced == false){
			
			// place the player onto the map
			if (pPlaced == false){
				int randomRow = randomise(dimension-1,1);
				int randomCol = randomise(dimension-1,1);
				
				if (map[randomRow][randomCol] == null){
					
					if (map[randomRow+1][randomCol] instanceof Wall == false 
							|| map[randomRow+1][randomCol] instanceof Wall == false
							|| map[randomRow+1][randomCol] instanceof Wall == false 
							|| map[randomRow+1][randomCol] instanceof Wall == false){
						Player player = new Player(randomRow, randomCol);
						map[randomRow][randomCol] = player;
						thePlayer = player;
						pPlaced = true;
						System.out.println("Player placed at " + randomRow + "," + randomCol);
					}
				}
			}
			
			// place the box(es) onto the map
			if (bPlaced == false){
				int count = 0;
				
				// placing all the boxes
				for (int i  = 0; i < numBG; i++){
					int randomRow = randomise(dimension-1,1);
					int randomCol = randomise(dimension-1,1);
					
					if (map[randomRow][randomCol] == null){	
						//System.out.println("found empty space. Checking if box is pushable at this spot.");
						// if the box is pushable, add the box to the specified spot
						if ((map[randomRow-1][randomCol] == null && map[randomRow+1][randomCol] == null)
								&& (map[randomRow][randomCol-1] == null && map[randomRow][randomCol+1] == null)){
							Box box = new Box(randomRow, randomCol);
							map[randomRow][randomCol] = box;
							System.out.println("1 boxed placed at " + randomRow + "," + randomCol);
							boxes[i] = box;
							count++;
						} else {
							//System.out.println("box not pushable");
						}
					}
				}
				
				//System.out.println("count: " + count + " | num Boxes: " + numBG);
				if (count == numBG){
					System.out.println("All boxes placed");
					bPlaced = true;
				} else {
					//System.out.println("Boxes not placed");
				}
			}
			
		}
		
		
		
		System.out.println("Placing goal(s) on map");
		while (gPlaced != true){
			int ctr = 0;
			for (int j = 0; j < numBG; j++){
				
				int randomRow = randomise(dimension-1,1);
				int randomCol = randomise(dimension-1,1);
				Goal theGoal = new Goal(randomRow, randomCol);
				System.out.println("Planning to add goal at " + randomRow + " " + randomCol);
				Box theBox = new Box(boxes[j].getRow(), boxes[j].getColumn());
				
				// if player and box can get to this goal, place it to the map
				if (pathToGoal(map, theGoal, theBox, thePlayer, dimension) == true){
					map[randomRow][randomCol] = theGoal;
					ctr++;
				}
			}
			
			
			if (ctr == numBG){
				gPlaced = true;
				System.out.println("Goal(s) placed");
			} 	
			
		}
		
	}
	
	/**
	 * Checks if there is a path from box and player to goal using bfs
	 * @param map
	 * @param goal
	 * @param box
	 * @param player
	 * @return
	 */
	public boolean pathToGoal(Token[][] map, Goal goal, Box box, Player player, int dimension){
		boolean hasPath = false;
		boolean pathPG = false;
		boolean pathBG = false;
		
		//System.out.println("Checking if there's a path from box to goal");
		
		// check if there is a path from box to goal
		pathBG = bfs(map, box, goal, dimension);
		
		//System.out.println("Checking if there's a path from player to goal");
		// checks if there is a path from player to goal
		pathPG = bfs(map, player, goal, dimension);
	
		
		if (pathBG == true && pathPG == true){
			hasPath = true;
			System.out.println("There's path from both box and player to goal");
		}
		
		return hasPath;	
	}   
    
	/**
	 * performs bfs search
	 * @param map is the puzzle layout
	 * @param start is the starting position
	 * @param dest is the goal
	 * @return
	 */
	public boolean bfs(Token[][] map, Token start, Token dest, int dimension){
		boolean foundPath = false;
		Queue <Token> queue = new LinkedList <Token>();
		ArrayList<Token> visited = new ArrayList<Token>();
		
		System.out.println();
		queue.add(start);
		//System.out.println("Starting token has been added. Starting token is at " + start.getRow() + "," + start.getColumn());
		//System.out.println("goal is at " + dest.getRow() + "," + dest.getColumn());
		
		while (!queue.isEmpty()){
			Token curr = queue.poll();
			//System.out.println("curr is at " + curr.getRow() + "," + curr.getColumn());
			// if we found the goal
			if (curr.getRow() == dest.getRow() && curr.getColumn() == dest.getColumn()){
				foundPath = true;
				//System.out.println("Found path!");
				break;
			}
			
			//System.out.println("curr is not the destination");
			if (checkVisited(visited, curr.getRow(), curr.getColumn()) == false){
				//System.out.println("this place has yet to be visited. Adding to visited list.");
				visited.add(curr);
				//System.out.println("Getting curr's neighbours.");
				List<Token> neighbours = getNeighbours(map, visited, curr,dimension);
				//System.out.println("adding neighbours to queue");
				queue.addAll(neighbours);
			} 
			
		}
		
		return foundPath;
	}
	
	/**
	 * gets the next available step (neighbour)
	 * @param map
	 * @param visited
	 * @param token
	 * @return
	 */
	public static List<Token> getNeighbours(Token[][] map, ArrayList<Token> visited, Token token, int dimension) {
        List<Token> neighbors = new ArrayList<Token>();
        
       // System.out.println("Checking if there is a neighbour above token");
        if(isValidNeighbour(map, visited, token.getRow()-1, token.getColumn(),dimension)) {
        	//System.out.println("has a valid neighbour above");
            neighbors.add(new Token(token.getRow() - 1, token.getColumn(),' '));
        }
        
        //System.out.println("Checking if there is a neighbour below token");
        if(isValidNeighbour(map, visited, token.getRow()+1, token.getColumn(),dimension)) {
        	//System.out.println("has a  valid neighbour below token");
            neighbors.add(new Token(token.getRow() + 1, token.getColumn(), ' '));
        }
        
        //System.out.println("Checking if there is a neighbour left of token");
        if(isValidNeighbour(map, visited, token.getRow(), token.getColumn()-1,dimension )) {
        	//System.out.println("got valid neighbour left of token");
            neighbors.add(new Token(token.getRow(), token.getColumn()-1, ' '));
        }
        
        //System.out.println("Checking if there is a neighbour right of token");
        if(isValidNeighbour(map, visited, token.getRow(), token.getColumn()+1,dimension)) {
        	//System.out.println("got valid neighbour right of token");
            neighbors.add(new Token(token.getRow(), token.getColumn(), ' '));
        }
        
        return neighbors;
    }
    
	/**
	 * checks if an index in the map is a neighbour
	 * @param map
	 * @param visited
	 * @param row
	 * @param col
	 * @return
	 */
    public static boolean isValidNeighbour(Token[][] map, ArrayList<Token> visited, int row, int col, int dimension) {
    	boolean isValid = false;
    	
    	//System.out.println("Coordinate " + row + "," + col);  	
    	if (map[row][col] == null && checkVisited(visited, row, col) == false){
    		isValid = true;
    		//System.out.println("Valid neighbour");
    	} else {
    		//System.out.println("Invalid neighbour");
    	}
    	
        return isValid;
    }
	
    /**
     * checks if an index in the map has been visited
     * @param visited
     * @param row of the token we are checking
     * @param col of the token we are checking
     * @return
     */
    public static boolean checkVisited(ArrayList<Token> visited, int row, int col){
    	boolean result = false;
    	
    	//System.out.println("size of visited " + visited.size());
    	for (int i = 0; i < visited.size(); i++){

    		if (visited.get(i).getRow() == row && visited.get(i).getColumn() == col){
    			result = true;
    		}
    	}
    	 
    	return result;
    }
    
    
	/**
	 * adds chosen template to the map
	 */
	public void addTemplate(Token[][] map, Token[][] template, int fromRow, int fromCol){
		
		int currRow = fromRow;
		int currCol = fromCol;
		
		for (int i = 0; i < 3; i++){
			currCol = fromCol;
			for(int j = 0; j < 3; j++){
				map[currRow][currCol] = template[i][j];
				currCol++;
			}
			currRow++;
		}
		
		
	}
	
	
	/**
	 * Prints the layout of the map
	 * @param array is a 2d array of characters of the map
	 */
	public void printCharArray(char[][] array){
		for(char i[]:array){
			for(char j:i){
				System.out.print(j);
			}
			System.out.println();
		}
	}
	
	
	/**
	 * gets the randomised template for the map
	 * @param num indicates which template to choose
	 * @return a token array
	 */
	public Token[][] getTemplate(int num){
			
		Token[][] template = new Token[][]{
			{null, null, null},
			{null, null, null},
			{null, null, null},
		};

		if (num == 1){
			System.out.println("Getting template 1");
			template[1][1] = new Wall(1,1);
		} else if (num == 2){
			System.out.println("Getting template 2");
			template[1][0] = new Wall(1,0);
			template[2][0] = new Wall(2,0);
			template[2][1] = new Wall(2,1);	
		} else if (num == 3){
			System.out.println("Getting template 3");
			template[0][1] = new Wall(0,1);
			template[2][1] = new Wall(2,1);
			template[2][2] = new Wall(2,2);
		} else if (num == 4){
			System.out.println("Getting template 4");
			template[2][1] = new Wall(2,1);
		} else {
			System.out.println("getting template 0");
		}
		
		return template;
	}
	
	
	/**
	 * iterate through the map to convert object array to char array
	 */
	public char[][] convertObjectArrayToCharArray(Token[][] t, char[][] c){
		int a=0;
		int b=0;
		for(Token i[]: t) {
			for(Token j: i) {
				if (j==null){
					c[a][b]='O';
				} else if(j instanceof Wall){
					c[a][b]='W';
				} else if(j instanceof Goal){
						c[a][b]='G';
				} else if(j instanceof Player){
					c[a][b]='P';
				} else if(j instanceof Box){
					c[a][b]='B';
				}
				b++;
			}
			a++;
			b=0;
		}
		return c;
	}
		
	
	/**
	 * number randomiser
	 * @param x is the total number of integers we have
	 * @return a randomised number between 0 to the total number of integers
	 */
	public int randomise(int max, int min){
		
		Random random =  new Random();
		
		int randomNum = random.nextInt((max - min) + 1)+ min;
		
	    return randomNum;
	}
	
	
	public static void main(String[] args){
		
		MapGenerator newmap = new MapGenerator();
		Token[][] map = newmap.createMap(8);
		System.out.println();
		char[][] charMap = new char[8][8];
		charMap = newmap.convertObjectArrayToCharArray(map,charMap);
		newmap.printCharArray(charMap);
		
		try
		{
		    PrintWriter pr = new PrintWriter("file.txt");    
		    pr.println("r8");
		    pr.println("c8");
		    for (int i=0; i<charMap.length ; i++)
		    {
		        pr.println(charMap[i]);
		    }
		    pr.close();
		}
		catch (Exception e)
		{
		    e.printStackTrace();
		    System.out.println("No such file exists.");
		}
		
		
		
	}
	
}
