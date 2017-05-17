import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * The {@code GameEngine Class} is the controller, in the MVC scheme.
 * It runs the WareHouse Bros game, a puzzle game where the player must
 * move boxes to the goal.
 */
public class GameEngine extends WindowAdapter{
    
    /** Allows an easy way to reference components in window */
    private final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    
    /** The width of all windows in the game. */
    private int WDWWIDTH = 500;
    
    /** The height of all windows in the game. */
    private int WDWHEIGHT = 500;
    
    /** The GameMap reprents the Model, in the MVC Scheme.
     * It stores all the map data, such as size, and position of all elements */
    private final GameMap gm;
    
    /** The GameWindow represents the View, in the MVC Scheme. 
     * It is provided with map data from the GameEngine (Controller), 
     * */
    private final GameWindow gw;
    
    /** The Strings below are used for convenient naming of the four
     * possible directions a token can move, as well as the arrow key input. */
    private static final String
            UP = "UP",
            DOWN = "DOWN",
            LEFT = "LEFT",
            RIGHT = "RIGHT";

    /**
     * This laods the game, by initialising a GameEngine (Controller), 
     * GameMap (Model), and GameWindow (View) 
     * @param args the arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                GameEngine ge = new GameEngine("map1.txt"); //running the game
                ge.initialiseLevelOne(ge.gw);
                ge.gw.drawMap(ge.gm.getMap(), ge.gm.getDimension());
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Instantiates a new GameEngine, GameMap, and GameWindow
     * @throws IOException 
     */
    public GameEngine(String fileName) throws IOException {
    	gm = new GameMap(fileName); //stores map information
    	gw = new GameWindow();
    }
    
    /**
     * The action is required by actionmap for the level panel.
     * This class is used to bind keyboard input to functions which
     * move the player, and redraw the map. 
     */
    private static class move extends AbstractAction {
        
        /** The Constant serialVersionUID. */
        //this is needed for nested classes to stop warnings, it's a unique id.
        private static final long serialVersionUID = 3L; 
        
        /** The key that was pressed. */
        final String key;
        
        /** The GameEngine within which the move is to be performed*/
        final GameMap gm;
        final GameWindow gw;
        
        /**
         * Instantiates a new move.
         *
         * @param j the jpanel within which the contents will be updated
         * @param s the key that was pressed
         * @param engine the GameEngine
         */
        public move(JPanel j, String s, GameEngine ge) {
            key = s;
            gm = ge.gm;
            gw = ge.gw;
        }
        
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean changed = false;
	    	switch (key) {
            case UP: 
            	if (gm.getPlayer().moveUp(gm)) changed = true;
                break;
            case DOWN: 
            	if (gm.getPlayer().moveDown(gm)) changed = true;
                break;
            case LEFT: 
            	if (gm.getPlayer().moveLeft(gm)) changed = true;
                break;
            case RIGHT: 
            	if (gm.getPlayer().moveRight(gm)) changed = true;
                break;
            }
	    	if (changed) gw.drawMap(gm.getMap(), gm.getDimension());
	    	if (gm.getNumGoals()==0){
	    	    System.out.println("YOU WON!");
	    	    System.exit(0);
	    	}
        }
    }
    
    /**
     * Initialise level one.
     *
     * @param gw the GameWindow on which the level is to be drawn
     */
    private void initialiseLevelOne(GameWindow gw) {
        gw.level.setSize(WDWWIDTH, WDWHEIGHT);
        gw.initTitle();
        gw.level.setBackground(Color.DARK_GRAY);
        
        gw.level.getInputMap(IFW).put(KeyStroke.getKeyStroke(UP), UP);
        gw.level.getInputMap(IFW).put(KeyStroke.getKeyStroke(DOWN), DOWN);
        gw.level.getInputMap(IFW).put(KeyStroke.getKeyStroke(LEFT), LEFT);        
        gw.level.getInputMap(IFW).put(KeyStroke.getKeyStroke(RIGHT), RIGHT);
        gw.level.getActionMap().put(UP, new move(gw.level, UP, this));
        gw.level.getActionMap().put(DOWN, new move(gw.level, DOWN, this));
        gw.level.getActionMap().put(LEFT, new move(gw.level, LEFT, this));
        gw.level.getActionMap().put(RIGHT, new move(gw.level, RIGHT, this));
    }
    
}
    