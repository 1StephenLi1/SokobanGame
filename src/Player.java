/**
 * The {@code Player} Class, is used by the main character on the map
 * It extends the token class
 * The position of the player is stored via Cartesian coordinates
 */
public class Player extends Token { 
    private boolean onGoal = false;
    private boolean second = false;
    private int face ;

    public int getFace() {
        return face;
    }

    public void setFace(int f) {
        face = f;
    }
    
    public boolean isSecond() {
        return second;
    }

    public void setSecond(boolean second) {
        this.second = second;
    }

    /**
    *
    * This constructor creates a {@code Player} at the specified
    * Cartesian coordinates. The coordinates are integers.
    */
    public Player(int x, int y) {
        super(x, y, 'P');
        face = 2;
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
        
        boolean moveValid = makeMove(map, row, col, row-1, col, row-2, col);
        if (moveValid == true){
            this.setRow(row-1);
            System.out.println("Move Valid. Moving up...");
        } else {
            System.out.println("Move not valid. Not moving.");
            return false;
        }
        
        System.out.println("Player current coordinates: ("+ row +", "+ col +")");
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
        
        boolean moveValid = makeMove(map, row, col, row+1, col, row+2, col);
        if (moveValid == true){
            this.setRow(row+1);
            System.out.println("Move Valid. Moving down...");
        } else {
            System.out.println("Move not valid. Not moving.");
            return false;
        }
        
        System.out.println("Player current coordinates: ("+ row +", "+ col +")");
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

        boolean moveValid = makeMove(map, row, col, row, col-1, row, col-2);
        if (moveValid == true){
            this.setColumn(col-1);
            System.out.println("Move Valid. Moving left...");
        } else {
            System.out.println("Move not valid. Not moving.");
            return false;
        }
        
        System.out.println("Player current coordinates: ("+ row +", "+ col +")");
        System.out.println("Player result coordinates: ("+ this.getRow() +", "+ this.getColumn() +")");
        
        return moveValid;
    }


    /**
    * Moves the player right in the 2d array map if the move is valid 
    * (must check if there is a wall or if there is a non-pushable box)
    */
    public boolean moveRight(GameMap map) {
        int row = this.getRow();
        int col = this.getColumn();
        System.out.println();

        boolean moveValid = makeMove(map, row, col, row, col+1, row, col+2);
        if (moveValid == true){
            this.setColumn(col+1);
            System.out.println("Move Valid. Moving right...");
        } else {
            System.out.println("Move not valid. Not moving.");
            return false;
        }
        
        System.out.println("Player current coordinates: ("+ row +", "+ col +")");
        System.out.println("Player result coordinates: ("+ this.getRow() +", "+ this.getColumn() +")");
        
        return moveValid;
    }

    /**
    * attempts to move the player to the requested index (rowNext, colNext).
    * rowNNext, and colNNext store the index of the cell adjacent to the players destination
    * this is needed to validate moves, as if there is a wall for example, although
    * players can push boxes, boxes cannot push walls, and so the move would be invalidated
    * and the function will return false.
    */
    public boolean makeMove(GameMap map, int row, int col, int rowNext, int colNext, int rowNNext, int colNNext){
        boolean isValid = true;
        boolean hasBox = false;
        Token[][] mapLayout = map.getMap();
        
        // check if there's a wall next to player
        if (mapLayout[rowNext][colNext] instanceof Wall || mapLayout[rowNext][colNext] instanceof Player){
            isValid = false;
        } 
        
        // check if there's a box next to player
        // and check if there's another box or a wall next to that box
        if (mapLayout[rowNext][colNext] instanceof Box){
            hasBox = true;
            if (mapLayout[rowNNext][colNNext] instanceof Box || mapLayout[rowNNext][colNNext] instanceof Wall){
                isValid = false;
            }
        } 
        

        // if move is valid and there is no box next to the player
        if (isValid == true && hasBox == false){
            Player player = (Player) mapLayout[row][col];
            
            // if there is a goal in front of player
            if (mapLayout[rowNext][colNext] instanceof Goal){
            
                // if  player is on a goal
                if (player.isOnGoal() == true){
                    Goal newgoal = new Goal(row,col);       
                    mapLayout[rowNext][colNext] = mapLayout[row][col];
                    mapLayout[row][col] = newgoal;
                // if player is not on a goal   
                } else {
                    player.setOnGoal(true);                 
                    mapLayout[rowNext][colNext] = player;          
                    mapLayout[row][col] = null;                 
                }
                
            // if there is no goal in front of player   
            } else {
                
                // if  player is on a goal
                if (player.isOnGoal() == true){
                    player.setOnGoal(false);
                    Goal newgoal = new Goal(row,col);       
                    mapLayout[rowNext][colNext] = mapLayout[row][col];
                    mapLayout[row][col] = newgoal;
                    
                // if player is not on a goal   
                } else {
                    mapLayout[rowNext][colNext] = mapLayout[row][col];         
                    mapLayout[row][col] = null;                 
                }
            }
            
        // if move is valid and there is a box next to the player   
        } else if (isValid == true && hasBox == true) {
            Box box = (Box) mapLayout[rowNext][colNext];
            Player player = (Player) mapLayout[row][col];
            Goal newgoal = new Goal(row,col);
            
            
            // if there is a goal next to that box
            if (mapLayout[rowNNext][colNNext] instanceof Goal){
                
                // if player is on a goal
                if (player.isOnGoal() == true){
                    
                    // if box is on a goal
                    if (box.isOnGoal() == true){
                        mapLayout[rowNNext][colNNext] = mapLayout[rowNext][colNext];
                        mapLayout[rowNext][colNext] = mapLayout[row][col];
                        mapLayout[row][col] = newgoal;
                    
                    // if box is not on a goal
                    } else {
                        box.setOnGoal(true);
                        player.setOnGoal(false);
                        mapLayout[rowNNext][colNNext] = mapLayout[rowNext][colNext];
                        mapLayout[rowNext][colNext] = mapLayout[row][col];
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
                    mapLayout[rowNNext][colNNext] = mapLayout[rowNext][colNext];
                    mapLayout[rowNext][colNext] = mapLayout[row][col];
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
                    
                    mapLayout[rowNNext][colNNext] = mapLayout[rowNext][colNext];
                    mapLayout[rowNext][colNext] = mapLayout[row][col];
                    mapLayout[row][col] = newgoal;                  
                    
                    
                // if player is not on a goal   
                } else {

                    // if box is on a goal
                    if (box.isOnGoal() == true){
                        box.setOnGoal(false);
                        player.setOnGoal(true);
                    }
                    
                    mapLayout[rowNNext][colNNext] = mapLayout[rowNext][colNext];
                    mapLayout[rowNext][colNext] = mapLayout[row][col];
                    mapLayout[row][col] = null;
                }
            }
        }
        return isValid;
    }
    
}
