/**
 * The {@code Box} Class, is used by the boxes the player pushes
 * It implements the token interface, and thus has methods to control movement
 * The position of the box is stored via Cartesian coordinates.
 */
public class Box extends Token{

	/** The x. */
	private int x; // x coordinate of box
	
	/** The y. */
	private int y; // y coordinate of box
	
	private boolean onGoal = false;
	/**
	 * This constructor creates a {@code Box} at the specified
	 * cartesian coordinates. The coordinates are integers.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public Box(int x, int y){
		super(x, y, 'B');
	}
	
	/**
	 * Gets the x coordinate of the box.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y coordinate of the box.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	
	public boolean isOnGoal (){
        return onGoal;
    }

    public void setOnGoal(boolean onGoal) {
        this.onGoal = onGoal;
    }
}
