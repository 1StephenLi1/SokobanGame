import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

/**
 * The {@code GameEngine Class} is the controller, in the MVC scheme.
 * It runs the WareHouse Bros game, a puzzle game where the player must
 * move boxes to the goal.
 */
public class GameEngine extends WindowAdapter{
    
    /** The GameMap reprents the Model, in the MVC Scheme.
     * It stores all the map data, such as size, and position of all elements */
    private final GameMap gm;
    
    /** The GameWindow represents the View, in the MVC Scheme. 
     * It is provided with map data from the GameEngine (Controller), 
     * */
    private final GameWindow gw;
    
    /**
     * This laods the game, by initialising a GameEngine (Controller), 
     * GameMap (Model), and GameWindow (View) 
     * @param args the arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ArrayList<String> maps = new ArrayList<>();
                maps.add("map1.txt");
                maps.add("map2.txt");
                maps.add("map3.txt");
                
                GameEngine ge = new GameEngine(maps); //running the game
                ge.gw.initialiseLevelOne(ge.gm);
                ge.gw.drawMap(ge.gm.getMap(), ge.gm.getCurrLevel(), ge.gm.getDimension(), 1);  //int parameter sets the starting face of our player
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * Instantiates a new GameEngine, GameMap, and GameWindow
     * @throws IOException 
     */
    public GameEngine(ArrayList<String> maps) throws IOException {
    	gm = new GameMap(maps); //stores map information
    	gw = new GameWindow(gm);
    }
    
}
    