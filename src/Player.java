/**
 * The {@code Player} Class, is used by the main character on the map
 * It extends the token class
 * The position of the player is stored via Cartesian coordinates
 */
public class Player extends Token {	
	private boolean onGoal = false;
	
	/**
	*
	* This constructor creates a {@code Player} at the specified
	* Cartesian coordinates. The coordinates are integers.
	*/
	public Player(int x, int y) {
		super(x, y, 'P');
	}
	
	/**
	 * 
	 * @return a boolean if the player is on a goal
	 */
	public boolean isOnGoal(){
		return onGoal;
	}
	
	/**
	 * set player status to be on the goal
	 */
	public void setOnGoal(boolean onGoal) {
		this.onGoal = onGoal;
	}

	/**
	* Moves the player up in the 2d array map if the move is valid
	* (must check if there is a wall or if there is a non-pushable box)
	*/
	public boolean moveUp(GameMap map) {
		int row = this.getRow();
		int col = this.getColumn();
		
		System.out.println();
		
		System.out.println("Player current coordinates: ("+ this.getRow() +", "+ this.getColumn() +")");

		boolean moveValid = moveValidRow(map, row, col,row-1, row-2);
		
		if (moveValid == true){
			this.setRow(row-1);
			System.out.println("Move Valid. Moving up...");
		} else {
			System.out.println("Move not valid. Not moving.");
		}

		
		System.out.println("Player result coordinates: ("+ this.getRow() +", "+ this.getColumn() +")");
		
		return moveValid;
	}
	

	/**
	* Moves the player down in the 2d array map if the move is valid 
	* (must check if there is a wall or if there is a non-pushable box)
	*/
	public boolean moveDown(GameMap map) {
		int row = this.getRow();
		int col = this.getColumn();
		
		System.out.println();
		
		System.out.println("Player current coordinates: ("+ this.getRow() +", "+ this.getColumn() +")");

		boolean moveValid = moveValidRow(map, row, col,row+1, row+2);
		
		if (moveValid == true){
			this.setRow(row+1);
			System.out.println("Move Valid. Moving down...");
		} else {
			System.out.println("Move not valid. Not moving.");
		}

		
		System.out.println("Player result coordinates: ("+ this.getRow() +", "+ this.getColumn() +")");
		
		return moveValid;
	}


	/**
	*
	* Moves the player left in the 2d array map if the move is valid 
	* (must check if there is a wall or if there is a non-pushable box)
	*/
	public boolean moveLeft(GameMap map) {
		int row = this.getRow();
		int col = this.getColumn();
		
		System.out.println();
		System.out.println("Player current coordinates: ("+ this.getRow() +", "+ this.getColumn() +")");

		boolean moveValid = moveValidCol(map, row, col,col-1, col-2);
		
		if (moveValid == true){
			this.setColumn(col-1);
			System.out.println("Move Valid. Moving left...");
		} else {
			System.out.println("Move not valid. Not moving.");
		}

		
		System.out.println("Player result coordinates: ("+ this.getRow() +", "+ this.getColumn() +")");
		
		return moveValid;
	}


	/**
	*
	* Moves the player right in the 2d array map if the move is valid 
	* (must check if there is a wall or if there is a non-pushable box)
	*/
	public boolean moveRight(GameMap map) {
		int row = this.getRow();
		int col = this.getColumn();
		
		System.out.println();
		System.out.println("Player current coordinates: ("+ this.getRow() +", "+ this.getColumn() +")");

		boolean moveValid = moveValidCol(map, row, col,col+1, col+2);
		
		if (moveValid == true){
			this.setColumn(col+1);
			System.out.println("Move Valid. Moving right...");
		} else {
			System.out.println("Move not valid. Not moving.");
		}

		
		System.out.println("Player result coordinates: ("+ this.getRow() +", "+ this.getColumn() +")");
		
		return moveValid;
	}
	
	// check validity for left and right movements
	public boolean moveValidCol(GameMap map, int row, int col, int next, int further){
		boolean isValid = true;
		boolean hasBox = false;
		Token[][] mapLayout = map.getMap();
		
		System.out.println("row, col, next, further: " + row + " "+ col + " " +next + " " + further);

		// check if there's a wall next to player
		if (mapLayout[row][next] instanceof Wall){
			System.out.println("Player has wall next to it, invalid.");
			isValid = false;
		}
		
		// check if there's a box next to player
		// and check if there's another box or a wall next to that box
		if (mapLayout[row][next] instanceof Box){
			hasBox = true;
			
			if (mapLayout[row][further] instanceof Box || mapLayout[row][further] instanceof Wall){
				isValid = false;
			}
		}
		
		
		// if move is valid and there is no box next to the player
		if (isValid == true && hasBox == false){
			Player player = (Player) mapLayout[row][col];
			
			// if there is a goal in front of player
			if (mapLayout[row][next] instanceof Goal){
			
				// if  player is on a goal
				if (player.isOnGoal() == true){
					Goal newgoal = new Goal(row,col);		
					mapLayout[row][next] = mapLayout[row][col];
					mapLayout[row][col] = newgoal;
				// if player is not on a goal	
				} else {
					player.setOnGoal(true);					
					mapLayout[row][next] = player;			
					mapLayout[row][col] = null;					
				}
				
			// if there is no goal in front of player	
			} else {
				
				// if  player is on a goal
				if (player.isOnGoal() == true){
					player.setOnGoal(false);
					Goal newgoal = new Goal(row,col);		
					mapLayout[row][next] = mapLayout[row][col];
					mapLayout[row][col] = newgoal;
					
				// if player is not on a goal	
				} else {
					mapLayout[row][next] = player;			
					mapLayout[row][col] = null;					
				}
			}
			
		// if move is valid and there is a box next to the player	
		} else if (isValid == true && hasBox == true) {
			Box box = (Box) mapLayout[row][next];
			Player player = (Player) mapLayout[row][col];
			Goal newgoal = new Goal(row,col);
			
			
			// if there is a goal next to that box
			if (mapLayout[row][further] instanceof Goal){
				
				// if player is on a goal
				if (player.isOnGoal() == true){
				
					// if box is on a goal
					if (box.isOnGoal() == true){
						mapLayout[row][further] = mapLayout[row][next];
						mapLayout[row][next] = mapLayout[row][col];
						mapLayout[row][col] = newgoal;
					
					// if box is not on a goal
					} else {
						box.setOnGoal(true);
						player.setOnGoal(false);
						mapLayout[row][further] = mapLayout[row][next];
						mapLayout[row][next] = mapLayout[row][col];
						mapLayout[row][col] = newgoal;	
					}
					
				// if player is not on a goal	
				} else {
		
					// if box is on a goal
					if (box.isOnGoal() == true){
						player.setOnGoal(true);
						
					// if box is not on a goal
					} else {
						box.setOnGoal(true);						
					}
					
					// move box and player forward
					mapLayout[row][further] = mapLayout[row][next];
					mapLayout[row][next] = mapLayout[row][col];
					mapLayout[row][col] = null;
				}
				
				
			// if there is no goal next to that box	
			} else {
				
				// if player is on a goal
				if (player.isOnGoal() == true){
				
					// if box is on a goal
					if (box.isOnGoal() == true){
						box.setOnGoal(false);	
					// if box is not on a goal
					} else {
						player.setOnGoal(false);
					}
					
					mapLayout[row][further] = mapLayout[row][next];
					mapLayout[row][next] = mapLayout[row][col];
					mapLayout[row][col] = newgoal;					
					
					
				// if player is not on a goal	
				} else {
					
					// if box is on a goal
					if (box.isOnGoal() == true){
						box.setOnGoal(false);
						player.setOnGoal(true);
					}
					
					mapLayout[row][further] = mapLayout[row][next];
					mapLayout[row][next] = mapLayout[row][col];
					mapLayout[row][col] = null;
				}
				
		}
		
		// if move is invalid
		} else {
			System.out.println("Hey, move is invalid.");
		}

		return isValid;
	}
	
	// check validity for up and down movements
	public boolean moveValidRow(GameMap map, int row, int col, int next, int further){
		boolean isValid = true;
		boolean hasBox = false;
		Token[][] mapLayout = map.getMap();
		
		System.out.println("row, col, next, further: " + row + " "+ col + " " +next + " " + further);

		
		// check if there's a wall next to player
		System.out.println("Checking if there's a wall next to player..");
		if (mapLayout[next][col] instanceof Wall){
			System.out.println("Player has wall next to it, invalid.");
			isValid = false;
		} else {
			System.out.println("No wall.");
		}
		
		// check if there's a box next to player
		// and check if there's another box or a wall next to that box
		System.out.println("Checking if player has a box next to it");
		if (mapLayout[next][col] instanceof Box){
			hasBox = true;
			System.out.println("Player has box next to it.");
			System.out.println("Checking if there's another box or wall near this box");
			if (mapLayout[further][col] instanceof Box || mapLayout[further][col] instanceof Wall){
				System.out.println("There is a wall/box next to this box, invalid.");
				isValid = false;
			}
		} else {
			System.out.println("No box.");
		}
		
		
		// if move is valid and there is no box next to the player
		if (isValid == true && hasBox == false){
			System.out.println("Move is valid but no box next to player");
			Player player = (Player) mapLayout[row][col];
			
			// if there is a goal in front of player
			if (mapLayout[next][col] instanceof Goal){
				System.out.println("Got goal in front of player");
			
				// if  player is on a goal
				if (player.isOnGoal() == true){
					System.out.println("Player is on a goal");
					Goal newgoal = new Goal(row,col);		
					mapLayout[next][col] = mapLayout[row][col];
					mapLayout[row][col] = newgoal;
				// if player is not on a goal	
				} else {
					System.out.println("Player not on goal");
					player.setOnGoal(true);					
					mapLayout[next][col] = player;			
					mapLayout[row][col] = null;					
				}
				
			// if there is no goal in front of player	
			} else {
				System.out.println("No goal in front of player");
				// if  player is on a goal
				System.out.println("Checking if player is on goal or not");
				if (player.isOnGoal() == true){
					System.out.println("Player is on a goal");
					player.setOnGoal(false);
					Goal newgoal = new Goal(row,col);		
					mapLayout[next][col] = mapLayout[row][col];
					mapLayout[row][col] = newgoal;
					
				// if player is not on a goal	
				} else {
					System.out.println("Player not on goal");
					mapLayout[next][col] = mapLayout[row][col];			
					mapLayout[row][col] = null;					
				}
			}
			
		// if move is valid and there is a box next to the player	
		} else if (isValid == true && hasBox == true) {
			System.out.println("Move valid but there's a box next to player");
			Box box = (Box) mapLayout[next][col];
			Player player = (Player) mapLayout[row][col];
			Goal newgoal = new Goal(row,col);
			
			
			// if there is a goal next to that box
			if (mapLayout[further][col] instanceof Goal){
				System.out.println("There's a goal next to that box");
				
				// if player is on a goal
				if (player.isOnGoal() == true){
					System.out.println("Player is on goal");
					// if box is on a goal
					if (box.isOnGoal() == true){
						System.out.println("box is on goal");
						mapLayout[further][col] = mapLayout[next][col];
						mapLayout[next][col] = mapLayout[row][col];
						mapLayout[row][col] = newgoal;
					
					// if box is not on a goal
					} else {
						System.out.println("Box not on goal");
						box.setOnGoal(true);
						player.setOnGoal(false);
						mapLayout[further][col] = mapLayout[next][col];
						mapLayout[next][col] = mapLayout[row][col];
						mapLayout[row][col] = newgoal;	
					}
					
				// if player is not on a goal	
				} else {
					System.out.println("Player not on goal");
					// if box is on a goal
					if (box.isOnGoal() == true){
						System.out.println("box not on goal");
						player.setOnGoal(true);
						
					// if box is not on a goal
					} else {
						System.out.println("Box on goal");
						box.setOnGoal(true);						
					}
					
					// move box and player forward
					mapLayout[further][col] = mapLayout[next][col];
					mapLayout[next][col] = mapLayout[row][col];
					mapLayout[row][col] = null;
				}
				
				
			// if there is no goal next to that box	
			} else {
				System.out.println("No goal next to that box");
				// if player is on a goal
				if (player.isOnGoal() == true){
					System.out.println("Player on goal");
					// if box is on a goal
					if (box.isOnGoal() == true){
						System.out.println("box on on goal");
						box.setOnGoal(false);	
					// if box is not on a goal
					} else {
						System.out.println("box not on goal");
						player.setOnGoal(false);
					}
					
					mapLayout[further][col] = mapLayout[next][col];
					mapLayout[next][col] = mapLayout[row][col];
					mapLayout[row][col] = newgoal;					
					
					
				// if player is not on a goal	
				} else {
					System.out.println("Player not on goal");
					// if box is on a goal
					if (box.isOnGoal() == true){
						System.out.println("box on goal");
						box.setOnGoal(false);
						player.setOnGoal(true);
					}
					
					mapLayout[further][col] = mapLayout[next][col];
					mapLayout[next][col] = mapLayout[row][col];
					mapLayout[row][col] = null;
				}
				
		}
		
		// if move is invalid
		} else {
			System.out.println("Hey, move is invalid.");
		}

		return isValid;
	}
	
}