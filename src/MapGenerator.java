import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class MapGenerator {
	
	private Token[][] map = null;
	private boolean status;
	
	
	public MapGenerator(){
		
	}
	
	
	/**
	 * Gets the status of the map
	 * @return true if map is generated, and false if otherwise
	 */
	public boolean getStatus() {
		return status;
	}


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
					map[i][j] = new Wall(i,j);
				}
			} else {
				map[i][0] = new Wall(i,0);
				map[i][dimension - 1] = new Wall(i,dimension - 1);
			}
		}

		this.map = map;
		int numTemplates = 4;
		int block = 4;
		int blockRow = 1;
		int blockCol = 1;
		
		for (int i = 0; i < block/2; i++){
			int templateNum = randomise(numTemplates,0);
			Token[][] template = getTemplate(templateNum);
			addTemplate(map, template, blockRow, blockCol);
			blockCol = blockCol + 3;
		}
		
		blockCol = 4;
		
		for (int i = 0; i < block/2; i++){
			int templateNum = randomise(numTemplates,0);
			Token[][] template = getTemplate(templateNum);
			addTemplate(map, template, blockRow, blockCol);
			blockRow = blockRow + 3;
		}
		
		this.status = addPBG(map, dimension);
		
		this.map = map;
		return map;
		
	}
	
	/**
	 * Adds a player, box and goal token to the map
	 * @param map is a 2d array of token
	 * @param dimension is the size of map (8x8)
	 * @param numBG number of goals and boxes (currently is 1)
	 */
	public boolean addPBG(Token[][] map, int dimension){
		boolean pPlaced = false;
		boolean bPlaced = false;
		boolean gPlaced = false;
		boolean status = false;
		
		int ctr = 0;
		int limit = 150;

		Player thePlayer = new Player(0,0);
		Box theBox = new Box (0,0);
		
		while ((pPlaced == false || bPlaced == false) || ctr < limit){
			
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
					}
				}
			}
			
			// place the box onto the map
			if (bPlaced == false){
		
					int randomRow = randomise(dimension-1,1);
					int randomCol = randomise(dimension-1,1);
					
					if (map[randomRow][randomCol] == null){	
						// if the box is pushable, add the box to the specified spot
						if ((map[randomRow-1][randomCol] == null && map[randomRow+1][randomCol] == null)
								&& (map[randomRow][randomCol-1] == null && map[randomRow][randomCol+1] == null)){
							Box box = new Box(randomRow, randomCol);
							map[randomRow][randomCol] = box;
							theBox = box;
							bPlaced = true;
						}
					}
			}
			
			ctr++;
		}
		
		
	
		while (gPlaced != true || ctr < limit){
			
				
				int randomRow = randomise(dimension-1,1);
				int randomCol = randomise(dimension-1,1);
				Goal theGoal = new Goal(randomRow, randomCol);
				
				// if player and box can get to this goal, place it to the map
				if (pathToGoal(map, theGoal, theBox, thePlayer, dimension) == true){
					map[randomRow][randomCol] = theGoal;
					gPlaced = true;
				}
				
				ctr++;
			
		}
		
		if (pPlaced == true && gPlaced == true && bPlaced == true){
			status = true;
		}
		
		return status;
		
	}
	
	/**
	 * Checks if there is a path from box and player to goal using bfs
	 * @param map is the map of the puzzle
	 * @param goal is the goal in the map
	 * @param box is the box in the map
	 * @param player is the player in the map
	 * @return boolean true if player, box and goal has been placed in the map, false if otherwise
	 */
	public boolean pathToGoal(Token[][] map, Goal goal, Box box, Player player, int dimension){
		boolean hasPath = false;
		boolean pathPG = false;
		boolean pathBG = false;
				
		// check if there is a path from box to goal
		pathBG = bfs(map, box, goal, dimension);
		
		// checks if there is a path from player to goal
		pathPG = bfs(map, player, goal, dimension);
	
		
		if (pathBG == true && pathPG == true){
			hasPath = true;
		}
		
		return hasPath;	
	}   
    
	/**
	 * performs bfs search
	 * @param map is the puzzle layout
	 * @param start is the starting position
	 * @return
	 */
	public boolean bfs(Token[][] map, Token start, Token dest, int dimension){
		boolean foundPath = false;
		Queue <Token> queue = new LinkedList <Token>();
		ArrayList<Token> visited = new ArrayList<Token>();
		
		queue.add(start);

		while (!queue.isEmpty()){
			Token curr = queue.poll();
			
			// if we found the goal
			if (curr.getRow() == dest.getRow() && curr.getColumn() == dest.getColumn()){
				foundPath = true;
				break;
			}
			
			// add neighbours which are not visited to the queue
			if (checkVisited(visited, curr.getRow(), curr.getColumn()) == false){
				visited.add(curr);
				List<Token> neighbours = getNeighbours(map, visited, curr,dimension);
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
	public List<Token> getNeighbours(Token[][] map, ArrayList<Token> visited, Token token, int dimension) {
        List<Token> neighbors = new ArrayList<Token>();
        
        // checks if the index above is a valid neighbour
        if(isValidNeighbour(map, visited, token.getRow()-1, token.getColumn(),dimension)) {
            neighbors.add(new Token(token.getRow() - 1, token.getColumn(),' '));
        }
        
        // checks if the index below is a valid neighbour
        if(isValidNeighbour(map, visited, token.getRow()+1, token.getColumn(),dimension)) {
            neighbors.add(new Token(token.getRow() + 1, token.getColumn(), ' '));
        }
        
        // checks if the index on the left is a valid neighbour
        if(isValidNeighbour(map, visited, token.getRow(), token.getColumn()-1,dimension )) {
            neighbors.add(new Token(token.getRow(), token.getColumn()-1, ' '));
        }
        
        // checks if the index on the right is a valid neighbour
        if(isValidNeighbour(map, visited, token.getRow(), token.getColumn()+1,dimension)) {
            neighbors.add(new Token(token.getRow(), token.getColumn(), ' '));
        }
        
        return neighbors;
    }
    
	/**
	 * checks if an index in the map is a valid neighbour
	 * @param map is the 2d array of the puzzle map
	 * @param visited is a list of visited tokens
	 * @param row is the index of the row we are checking
	 * @param col is the index of the col we are checking
	 * @return a boolean (true if an index is a valid neighbour, false if otherwise)
	 */
    public boolean isValidNeighbour(Token[][] map, ArrayList<Token> visited, int row, int col, int dimension) {
    	boolean isValid = false;
    	
    	if (map[row][col] == null && checkVisited(visited, row, col) == false){
    		isValid = true;
    	} 
    	
        return isValid;
    }
	
    /**
     * checks if an index in the map has been visited
     * @param visited is an array of token
     * @param row is the row the token we are checking
     * @param col is the column of the token we are checking
     * @return a boolean (true if it has been visited, false if otherwise)
     */
    public boolean checkVisited(ArrayList<Token> visited, int row, int col){
    	boolean result = false;
    	
    	for (int i = 0; i < visited.size(); i++){
    		if (visited.get(i).getRow() == row && visited.get(i).getColumn() == col){
    			result = true;
    		}
    	}
    	 
    	return result;
    }
    
    
    /**
     * Adds a 3x3 token array (template) onto the existing map
     * @param map is the existing map of the puzzle
     * @param template is the chosen 3x3 template
     * @param fromRow is the starting row of the map that will be overwritten
     * @param fromCol is the starting column of the map that will be overwritten
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
	 * Prints the layout of the map in ASCII art
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
	 * @return a 2d token array of the template of size 3x3
	 */
	public Token[][] getTemplate(int num){
			
		Token[][] template = new Token[][]{
			{null, null, null},
			{null, null, null},
			{null, null, null},
		};

		if (num == 1){
			template[1][1] = new Wall(1,1);
		} else if (num == 2){
			template[1][0] = new Wall(1,0);
			template[2][0] = new Wall(2,0);
			template[2][1] = new Wall(2,1);	
		} else if (num == 3){
			template[0][1] = new Wall(0,1);
			template[2][1] = new Wall(2,1);
			template[2][2] = new Wall(2,2);
		} else if (num == 4){
			template[2][1] = new Wall(2,1);
		}
		
		return template;
	}
	
	/**
	 * gets the map of the game in 2d array of char
	 * @param dimension is the dimension of the map
	 * @return a 2d array of char 
	 */
	public char[][] getCharMap(int dimension){
		
		char[][] charMap = new char[dimension][dimension];
		charMap = convertObjectArrayToCharArray(map,charMap);
		return charMap;
		
	}
	
	
	/**
	 * Converts the 2d token array to a 2d array of characters for printing
	 * @param t is the token array
	 * @param c is the character array
	 * @return
	 */
	public char[][] convertObjectArrayToCharArray(Token[][] t, char[][] c){
		int a=0;
		int b=0;
		for(Token i[]: t) {
			for(Token j: i) {
				if (j==null){
					c[a][b]=' ';
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
}
