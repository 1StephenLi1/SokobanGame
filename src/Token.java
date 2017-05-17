/**
 * 
 * The {@code Token} class is parent class, is used by all items on the map
 * i.e. it is extended by boxes, walls and our player.
 * It contains methods to get the location of our items
 *
 */
public class Token {
	
	/** The row index of the token in the GameMap*/
	private int row;
	
	/** The column index of the token in the GameMap */
	private int column;
	
	/** The type of token e.g. Box, Wall, Goal, Player */
	private char type;

	/**
	 * Instantiates a new token.
	 *
	 * @param row the row index
	 * @param column the column index
	 * @param type the type of token
	 */
	public Token(int row, int column, char type) {
		this.row = row;
		this.column = column;
		this.type = type;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public char getType() {
		return type;
	}
	
	/**
	 * Gets the token x position.
	 *
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Gets the token y position.
	 *
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * sets the new row of the token.
	 *
	 * @param row the new row
	 */
	public void setRow(int row){
		this.row = row;
		
	}
	
	/**
	 * sets the new column of the token.
	 *
	 * @param col the new column
	 */
	public void setColumn(int col){
		this.column = col;
	}
	
	/**
	 * Gets the token location in the form of an array {x,y}.
	 *
	 * @return the location
	 */
	public int[] getLocation(){
		return new int[] {row, column};
	}
}

