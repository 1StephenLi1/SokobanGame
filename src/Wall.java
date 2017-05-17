/**
 * The Class Wall.
 */
public class Wall extends Token {

	/** The x. */
	private int x; // holds the x coordinate of the wall
	
	/** The y. */
	private int y; // holds the y coordinate of the wall
	
	/**
	 * Instantiates a new wall.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public Wall(int x, int y){
		super(x, y, 'W');
	}

	/**
	 * Gets the x coordinate of the wall.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x coordinate of the wall.
	 *
	 * @param x the new x
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Gets the y coordinate of the wall.
	 *
	 * @return the coordinate of the wall
	 */
	public int getY() {
		return y;
	}

	/**
	 * sets the y coordinate of the wall.
	 *
	 * @param y the new y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	
}
