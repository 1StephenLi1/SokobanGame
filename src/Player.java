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
		boolean isValid = true;
		boolean hasBox = false;
		int row = this.getRow();
		int col = this.getColumn();
		Token[][] mapLayout = map.getMap();
		
		System.out.println();
		
		if (mapLayout[row-1][col] instanceof Wall){ // if there's a wall on top of the player
			System.out.println("Found wall, invalid move."); // move is invalid
			isValid = false;
		}

		if (mapLayout[row-1][col] instanceof Box){ // if there is a box next to the player
			hasBox = true;						   // let the system know that there exist a box next to player
			
			if (mapLayout[row-2][col] instanceof Box || mapLayout[row-2][col] instanceof Wall){	// move is invalid
					isValid = false;																
					System.out.println("Box has something next to it, cant be pushed.");			
			}	
		}
		
		
		if (isValid == true && hasBox == false){			// if moving is valid and we're not pushing the boxes.
			Player player = (Player) mapLayout[row][col];
			
			if (mapLayout[row-1][col] instanceof Goal){ 	// if the player is next to a goal
				System.out.println("Player is next to a goal");
				if (player.isOnGoal() == true){				// if the player is already standing on a goal
					Goal newgoal = new Goal(row,col);
					mapLayout[row-1][col] = mapLayout[row][col];
					mapLayout[row][col] = newgoal;
				} else {									// player is not standing on a goal
					player.setOnGoal(true);					// set 'onGoal' = true
					mapLayout[row-1][col] = player;			// player is now on the goal (Z)
					mapLayout[row][col] = null;				// the place player was is now null/empty space
				}				
				
			} else {										// if there is no goal in front of the player
				mapLayout[row-1][col] = mapLayout[row][col];
				if (player.isOnGoal() == true){
					Goal newgoal = new Goal(row,col);
					mapLayout[row][col] = newgoal;
					player.setOnGoal(false);
				} else {
					mapLayout[row][col] = null;
				}
				
			}
			
			this.setRow(row-1);
			System.out.println("Move valid. Moving up...");
		} 
		
		// if moving is valid and we r pushing the box
		else if (isValid == true && hasBox == true){
			if (mapLayout[row-2][col] instanceof Goal){ 	// if there's another goal in front of the box
				Box box = (Box) mapLayout[row-1][col];			
				Player player = (Player) mapLayout[row][col];
				Goal newgoal = new Goal(row,col);
				
				if (player.isOnGoal() == false){			// if player is not on a goal
					System.out.println("player not on goal");
					if (box.isOnGoal() == true){
						player.setOnGoal(true);
					} else {
						box.setOnGoal(true);
					}
					mapLayout[row-2][col] = mapLayout[row-1][col];
					mapLayout[row-1][col] = mapLayout[row][col];
					mapLayout[row][col] = null;
				} else {
					System.out.println("player is on goal");
					
					if (box.isOnGoal() == true){
						System.out.println("the box is on a goal");
						box.setOnGoal(true);
						player.setOnGoal(true);
						mapLayout[row-2][col] = mapLayout[row-1][col];
						mapLayout[row-1][col] = mapLayout[row][col];
						mapLayout[row][col] = newgoal;
					} else {
						System.out.println("box is not on goal");
						box.setOnGoal(true);
						player.setOnGoal(false);
						mapLayout[row-2][col] = mapLayout[row-1][col];
						mapLayout[row-1][col] = mapLayout[row][col];
						mapLayout[row][col] = newgoal;
					}	
				}

			// in other cases we r not pushing the box to the goal
			} else {
				Box box = (Box) mapLayout[row-1][col];
				Player player = (Player) mapLayout[row][col];
				if (player.isOnGoal() == true && box.isOnGoal() == true){
					box.setOnGoal(false);
					Goal newgoal = new Goal(row,col);
					mapLayout[row-2][col] = box;
					mapLayout[row-1][col] = player;
					mapLayout[row][col] = newgoal;
				} else if (box.isOnGoal() == true){
					box.setOnGoal(false);
					mapLayout[row-2][col] = box;
					mapLayout[row-1][col] = mapLayout[row][col];
					player.setOnGoal(true);
					mapLayout[row][col] = null;
					
				} else if (player.isOnGoal() == true){
					player.setOnGoal(false);
					mapLayout[row-2][col] = mapLayout[row-1][col];
					mapLayout[row-1][col] = mapLayout[row][col];
					Goal newgoal = new Goal(row,col);
					mapLayout[row][col] = newgoal;
				} else {
					mapLayout[row-2][col] = box;
					mapLayout[row-1][col] = player;
					mapLayout[row][col]=null;
				}
			
			}
			this.setRow(row-1);
			System.out.println("Move valid, pushing box up...");
		} else {
			System.out.println("Move not valid. Won't move.");
			
		}
		
		System.out.println("Player result coordinates: ("+this.getRow()+", "+this.getColumn()+")");
		return isValid;
	}
	

	/**
	* Moves the player down in the 2d array map if the move is valid 
	* (must check if there is a wall or if there is a non-pushable box)
	*/
	public boolean moveDown(GameMap map) {
		boolean isValid = true;
		boolean hasBox = false;
		int row = this.getRow();
		int col = this.getColumn();
		Token[][] mapLayout = map.getMap();
		
		System.out.println();
		
		// checks space under the player
		// if there is a wall under the player, move is not valid
		if (mapLayout[row+1][col] instanceof Wall){
			System.out.println("Found wall, invalid move.");
			isValid = false;
		}
		
		// if there is a box 
		// check if there's a wall or a box under it
		// if there is a wall, it is not a valid move
		// if there is no wall, then it is a valid move
		if (mapLayout[row+1][col] instanceof Box){
			hasBox = true;
			
			if (mapLayout[row+2][col] instanceof Box || mapLayout[row+2][col] instanceof Wall){
				isValid = false;
				System.out.println("Box has something next to it, can't be pushed.");
			}
		}
		
		// if moving is valid and we r not pushing the boxes. 
		if (isValid == true && hasBox == false){
			Player player = (Player) mapLayout[row][col];
			
			if (mapLayout[row+1][col] instanceof Goal){ 	// if the player is next to a goal
				
				if (player.isOnGoal() == true){				// if the player is already on a goal
					Goal newgoal = new Goal(row,col);		
					mapLayout[row+1][col] = player;
					mapLayout[row][col] = newgoal;
				} else {									// if the player is not on a goal
					player.setOnGoal(true);					// set 'onGoal' = true
					mapLayout[row+1][col] = player;			// player is now on the goal (Z)
					mapLayout[row][col] = null;				// the place player was is now null/empty space
				}
				
				
			} else if (player.isOnGoal()== true){
				Goal newgoal = new Goal(row,col);
				mapLayout[row+1][col] = player;
				mapLayout[row][col] = newgoal;
				player.setOnGoal(false);
			} else {
				mapLayout[row+1][col] = player;
				mapLayout[row][col] = null;
			}
			this.setRow(row+1);
			System.out.println("Move valid. Moving down...");
		} 
		
		// if moving is valid and we r pushing the box
		else if (isValid == true && hasBox == true){
			if (mapLayout[row+2][col] instanceof Goal){
				Box box = (Box) mapLayout[row+1][col];
				Player player = (Player) mapLayout[row][col];
				Goal newgoal = new Goal(row,col);

				if (player.isOnGoal() == false){			// if player is not on a goal
					System.out.println("player not on goal");
					if (box.isOnGoal() == true){
						player.setOnGoal(true);
					} else {
						box.setOnGoal(true);
					}
					mapLayout[row+2][col] = mapLayout[row+1][col];
					mapLayout[row+1][col] = mapLayout[row][col];
					mapLayout[row][col] = null;
				} 
				// player is on goal
				else {
					if (box.isOnGoal() == true){
						System.out.println("the box is on a goal");
						box.setOnGoal(true);
						player.setOnGoal(true);
						mapLayout[row+2][col] = mapLayout[row+1][col];
						mapLayout[row+1][col] = mapLayout[row][col];
						mapLayout[row][col] = newgoal;
					} else {
						System.out.println("box is not on goal");
						box.setOnGoal(true);
						player.setOnGoal(false);
						mapLayout[row+2][col] = mapLayout[row+1][col];
						mapLayout[row+1][col] = mapLayout[row][col];
						mapLayout[row][col] = newgoal;
					}	
				}
				
			// in other cases we r not pushing the box to the goal
			} else {
				Box box = (Box) mapLayout[row+1][col];
				Player player = (Player) mapLayout[row][col];
				if (player.isOnGoal() == true && box.isOnGoal() == true){
					box.setOnGoal(false);
					Goal newgoal = new Goal(row,col);
					mapLayout[row+2][col] = box;
					mapLayout[row+1][col] = player;
					mapLayout[row][col] = newgoal;
				} else if (box.isOnGoal() == true){
					box.setOnGoal(false);
					mapLayout[row+2][col] = box;
					mapLayout[row+1][col] = player;
					player.setOnGoal(true);
					mapLayout[row][col] = null;
				} else if (player.isOnGoal() == true){
					player.setOnGoal(false);
					mapLayout[row+2][col] = box;
					mapLayout[row+1][col] = player;
					Goal newgoal = new Goal(row,col);
					mapLayout[row][col] = newgoal;
				} else {
					mapLayout[row+2][col] = box;
					mapLayout[row+1][col] = player;
					mapLayout[row][col]=null;
				}
			
			}
			
			this.setRow(row+1);
			System.out.println("Move valid, pushing box down...");
		} else {
			System.out.println("Move not valid. Won't move.");
		}
		
		System.out.println("Player result coordinates: ("+this.getRow()+", "+this.getColumn()+")");
		return isValid;
	}


	/**
	*
	* Moves the player left in the 2d array map if the move is valid 
	* (must check if there is a wall or if there is a non-pushable box)
	*/
	public boolean moveLeft(GameMap map) {
		boolean isValid = true;
		boolean hasBox = false;
		int row = this.getRow();
		int col = this.getColumn();
		Token[][] mapLayout = map.getMap();
		
		System.out.println();
		
		// checks space next to player
		// if there is a wall next to the player, move is not valid
		if (mapLayout[row][col-1] instanceof Wall){
			System.out.println("Player has wall next to it, invalid.");
			isValid = false;
		}
		
		// if there is a box next to the player, and there's another box or wall next to the box
		// move is not valid
		if (mapLayout[row][col-1] instanceof Box){
			hasBox = true;
			
			if (mapLayout[row][col-2] instanceof Box || mapLayout[row][col-2] instanceof Wall){
				isValid = false;
			}
		}
		
		// if the move is valid but no box, swap the player with the empty space
		if (isValid == true && hasBox == false){
			Player player = (Player) mapLayout[row][col];
			
			if (mapLayout[row][col-1] instanceof Goal){ 	// if the player is next to a goal
				if (player.isOnGoal() == true){				// if the player is already on a goal
					Goal newgoal = new Goal(row,col);		
					mapLayout[row][col-1] = mapLayout[row][col];
					mapLayout[row][col] = newgoal;
				} else {									// if the player is not on a goal yet
					player.setOnGoal(true);					// set 'onGoal' = true
					mapLayout[row][col-1] = player;			// player is now on the goal (Z)
					mapLayout[row][col] = null;				// the place player was is now null/empty space
				}	
			} else 	if (player.isOnGoal() == true){			// if player is on a goal
				Goal newgoal = new Goal(row,col);
				mapLayout[row][col-1] = mapLayout[row][col];
				mapLayout[row][col] = newgoal;
				player.setOnGoal(false);
			} else {
				mapLayout[row][col-1] = player;
				mapLayout[row][col] = null;
			}
			
			this.setColumn(col-1);
			System.out.println("Move valid. Moving left...");
			
		// if it is a valid move and there is a box next to the player
		} else if (isValid == true && hasBox == true){
			
			// if there is a goal next to the box
			if (mapLayout[row][col-2] instanceof Goal){
				Box box = (Box) mapLayout[row][col-1];
				Player player = (Player) mapLayout[row][col];
				Goal newgoal = new Goal(row,col);

				// if player is not on a goal
				if (player.isOnGoal() == false){	
					System.out.println("player not on goal");
					if (box.isOnGoal() == true){
						player.setOnGoal(true);
					} else {
						box.setOnGoal(true);
					}
					mapLayout[row][col-2] = mapLayout[row][col-1];
					mapLayout[row][col-1] = mapLayout[row][col];
					mapLayout[row][col] = null;
					
				// if player is on a goal
				} else {							
					System.out.println("player is on goal");
					
					// if box is on a goal
					if (box.isOnGoal() == true){	
						System.out.println("the box is on a goal");
						box.setOnGoal(true);
						player.setOnGoal(true);
						mapLayout[row][col-2] = mapLayout[row][col-1];
						mapLayout[row][col-1] = mapLayout[row][col];
						mapLayout[row][col] = newgoal;
						
					// if box is not on a goal
					} else {						
						System.out.println("box is not on goal");
						box.setOnGoal(true);
						player.setOnGoal(false);
						mapLayout[row][col-2] = mapLayout[row][col-1];
						mapLayout[row][col-1] = mapLayout[row][col];
						mapLayout[row][col] = newgoal;
					}	
				}
			
			} else {
				Box box = (Box) mapLayout[row][col-1];
				Player player = (Player) mapLayout[row][col];
				if (player.isOnGoal() == true && box.isOnGoal() == true){
					box.setOnGoal(false);
					Goal newgoal = new Goal(row,col);
					mapLayout[row][col-2] = box;
					mapLayout[row][col-1] = player;
					mapLayout[row][col] = newgoal;
				} else if (box.isOnGoal() == true){
					box.setOnGoal(false);
					mapLayout[row][col-2] = box;
					mapLayout[row][col-1] = player;
					player.setOnGoal(true);
					mapLayout[row][col] = null;
				} else if (player.isOnGoal() == true){
					player.setOnGoal(false);
					mapLayout[row][col-2] = box;
					mapLayout[row][col-1] = player;
					Goal newgoal = new Goal(row,col);
					mapLayout[row][col] = newgoal;
				} else {
					mapLayout[row][col-2] = box;
					mapLayout[row][col-1] = player;
					mapLayout[row][col] = null;
				}
			}
			
			this.setColumn(col-1);
			System.out.println("Move valid, pushing box to the left...");		
		} else {
			System.out.println("Move not valid. Can't move.");
		}
		
		System.out.println("Player result coordinates: ("+this.getRow()+", "+this.getColumn()+")");
		return isValid;
	}


	/**
	*
	* Moves the player right in the 2d array map if the move is valid 
	* (must check if there is a wall or if there is a non-pushable box)
	*/
	public boolean moveRight(GameMap map) {
		boolean isValid = true;
		boolean hasBox = false;
		int row = this.getRow();
		int col = this.getColumn();
		Token[][] mapLayout = map.getMap();
		
		System.out.println();
		
		// checks space right to player
		// if there is a wall next to the player, move is not valid
		if (mapLayout[row][col+1] instanceof Wall){
			System.out.println("Player has wall next to it, invalid.");
			isValid = false;
		}
		
		// if there is a box to the right of the player,
		// check if there's a wall or a box to the right of the box
		// if there is a wall, it is not a valid move
		// if there is no wall, then it is a valid move
		if (mapLayout[row][col+1] instanceof Box){
			hasBox = true;
			
			if (mapLayout[row][col+2] instanceof Box || mapLayout[row][col+2] instanceof Wall){
				isValid = false;
			}

		}
		
		
		// if the move is valid but there is no box next to the player
		if (isValid == true && hasBox == false){
			Player player = (Player) mapLayout[row][col];

			if (mapLayout[row][col+1] instanceof Goal){ // if the player is next to a goal
				
				if (player.isOnGoal() == true){				// if the player is already on a goal
					Goal newgoal = new Goal(row,col);		
					mapLayout[row][col+1] = mapLayout[row][col];
					mapLayout[row][col] = newgoal;
				} else {
					player.setOnGoal(true);					// set 'onGoal' = true
					mapLayout[row][col+1] = player;			// player is now on the goal (Z)
					mapLayout[row][col] = null;				// the place player was is now null/empty space
				}

				
			} else if (player.isOnGoal() == true){		// if player is on the goal
				Goal newgoal = new Goal(row,col);		// create a new goal 
				mapLayout[row][col+1] = player;			// move player right
				mapLayout[row][col] = newgoal;			// put the new goal back to the goal spot
				player.setOnGoal(false);				// set player is now not on a goal
				
			} else {									// move player to the next spot
				mapLayout[row][col+1] = player;			// make player's previous spot null
				mapLayout[row][col] = null;
			}
			this.setColumn(col+1);
			System.out.println("Move valid. Moving right...");
			
		// if moving is valid and we r pushing the box
		} else if (isValid == true && hasBox == true){
			if (mapLayout[row][col+2] instanceof Goal){
				Box box = (Box) mapLayout[row][col+1];
				Player player = (Player) mapLayout[row][col];
				Goal newgoal = new Goal(row,col);

				if (player.isOnGoal() == false){			// if player is not on a goal
					System.out.println("player not on goal");
					if (box.isOnGoal() == true){
						player.setOnGoal(true);
					} else {
						box.setOnGoal(true);
					}
					mapLayout[row][col+2] = mapLayout[row][col+1];
					mapLayout[row][col+1] = mapLayout[row][col];
					mapLayout[row][col] = null;
				} else {
					System.out.println("player is on goal");
					
					if (box.isOnGoal() == true){
						System.out.println("the box is on a goal");
						box.setOnGoal(true);
						player.setOnGoal(true);
						mapLayout[row][col+2] = mapLayout[row][col+1];
						mapLayout[row][col+1] = mapLayout[row][col];
						mapLayout[row][col] = newgoal;
					} else {
						System.out.println("box is not on goal");
						box.setOnGoal(true);
						player.setOnGoal(false);
						mapLayout[row][col+2] = mapLayout[row][col+1];
						mapLayout[row][col+1] = mapLayout[row][col];
						mapLayout[row][col] = newgoal;
					}	
				}
				
				// in other cases we r not pushing the box to the goal
			} else {
				Box box = (Box) mapLayout[row][col+1];
				Player player = (Player) mapLayout[row][col];
				if (player.isOnGoal() == true && box.isOnGoal() == true){
					box.setOnGoal(false);
					Goal newgoal = new Goal(row,col);
					mapLayout[row][col+2] = box;
					mapLayout[row][col+1] = player;
					mapLayout[row][col] = newgoal;
				} if (box.isOnGoal() == true){
					box.setOnGoal(false);
					mapLayout[row][col+2] = box;
					mapLayout[row][col+1] = player;
					player.setOnGoal(true);
					mapLayout[row][col] = null;
					
				} else if (player.isOnGoal() == true){
					player.setOnGoal(false);
					mapLayout[row][col+2] = box;
					mapLayout[row][col+1] = player;
					Goal newgoal = new Goal(row,col);
					mapLayout[row][col] = newgoal;
				} else {
					mapLayout[row][col+2] = box;
					mapLayout[row][col+1] = player;
					mapLayout[row][col]=null;
				}
			
			}
			
			this.setColumn(col+1);
			System.out.println("Move valid, pushing box to the right...");		
		} else {
			System.out.println("Move not valid. Can't move.");
		
		}
		
		System.out.println("Player result coordinates: ("+this.getRow()+", "+this.getColumn()+")");
		return isValid;
	}

}