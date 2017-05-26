import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.io.PrintWriter;
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
    	for (int counter = 13; counter <=20 ; counter ++){
			MapGenerator newmap = new MapGenerator();
			Token[][] map = newmap.createMap(8);
			if (newmap.getStatus() == false){
				counter--;
				break;
			}
			char[][] charMap = new char[8][8];
			charMap = newmap.convertObjectArrayToCharArray(map, charMap);
			
			if (map == null) {
			    counter--;
			    break;
			}
			
			try
			{	
				String filename = "map" + Integer.toString(counter) +".txt";
			    PrintWriter pr = new PrintWriter(filename);    
			    pr.println("r8");
			    pr.println("c8");
			    for (int i=0; i<charMap.length ; i++)
			    {
			        pr.println(charMap[i]);
			    }
			    pr.close();
			}
			catch (Exception e) {
			    e.printStackTrace();
			    System.out.println("No such file exists.");
			}
		}
    	
    	SwingUtilities.invokeLater(() -> {
            try {
                ArrayList<String> maps = new ArrayList<>();
                maps.add("map1.txt");
                maps.add("map2.txt");
                maps.add("map3.txt");
                maps.add("map4.txt");
                maps.add("map5.txt");
                maps.add("map6.txt");

                GameEngine ge = new GameEngine(maps); //running the game
                ge.gw.initialiseLevelOne(ge.gm);
                ge.gw.drawMap(ge.gm.getMap(), ge.gm.getCurrLevel(), ge.gm.getDimension()); 
                
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
    