import java.awt.Font;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;


/**
 * The Class GameWindow.
 */
public class GameWindow extends WindowAdapter{
    
    /** The ifw. */
    private final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW; //Allows an easy way to reference components in window
    
    /** The if. */
    private final int IF = JComponent.WHEN_FOCUSED; //Allows an easy way to reference the focused component
    
    /** The btnwidth. */
    private final int BTNWIDTH = 100;
    
    /** The btnheight. */
    private final int BTNHEIGHT = 37;
    
    /** The lblwidth. */
    private final int LBLWIDTH = 450;
    
    /** The lblheight. */
    private final int LBLHEIGHT = 50;
    
    /** The wdwwidth. */
    private final int WDWWIDTH = 500;
    
    /** The wdwheight. */
    private final int WDWHEIGHT = 500;
    
    /** The Constant GO. */
    private static final String
        HOVER = "HOVER",
        SELECT = "SELECT",
        START = "START",
        QUIT = "QUIT",
        GO = "GO";
    
    /** The frame. */
    private JFrame frame;
    
    /** The cl. */
    private CardLayout cl;
    
    /** The panel cards. */
    private JPanel panelCards;
    
    /** The menu. */
    private JPanel menu;
    
    /** The level selection. */
    private JPanel levelSelection;
    
    /** The level. */
    protected JPanel level;
    
    /**
     * This creates an empty game window, that the game will present in.
     * It defaults to being placed in the centre of the screen.
     * It is initalised as an empty window, and needs to be populated!
     * It is not visible when created, and must be turned on later!
     */
    public GameWindow() {
        frame = new JFrame();
        //frame.setLayout(null); //Means we set coordinates ourselves i.e. no auto layout => more control
        frame.setSize(WDWWIDTH, WDWHEIGHT); //set frame size to specified width and height
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(this); 
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        panelCards = new JPanel(null);
        panelCards.setBounds(0, 0, WDWWIDTH, WDWHEIGHT);
        menu = new JPanel(null);
        levelSelection = new JPanel(null);
        level = new JPanel(null);
        cl = new CardLayout();
        
        panelCards.setLayout(cl);
        panelCards.add(menu, "1");
        panelCards.add(levelSelection, "2");
        panelCards.add(level, "3");
        cl.show(panelCards, "1");
        frame.getContentPane().add(panelCards);
        initialiseMenu();
        initialiseLevelSelection();
        frame.setVisible(true);
    }
    
    /**
     * This is just a container for an action, to obtain the focus of a button.
     */
    private static class getFocus extends AbstractAction {
        
        /** The Constant serialVersionUID. */
        //this is needed for nested classes to stop warnings, it's a unique id.
        private static final long serialVersionUID = 1L; 
        
        /** The temp. */
        final JButton temp;
        
        /**
         * Instantiates a new gets the focus.
         *
         * @param j the j
         */
        public getFocus(JButton j) {
            temp = j;
        }
        
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            temp.requestFocusInWindow();
        }
    }
    
    /**
     * This determines the action to perform after a button has been clicked.
     * If it is the quit button, the frame is cleared, and program terminates.
     * Otherwise something else...
     */
    private class clickAction extends AbstractAction {
        
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 2L;
        
        /** The temp. */
        final JButton temp;
        
        /**
         * Instantiates a new click action.
         *
         * @param j the j
         */
        public clickAction(JButton j) {
            temp = j;
        }
        
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (temp.getName().equals(QUIT)) {
                windowClosingQuit(e);
            } else if (temp.getName().equals(START)){
                frame.setSize(WDWWIDTH,WDWHEIGHT);
                frame.setLocationRelativeTo(null);
                cl.show(panelCards, "2");
            } else if (temp.getName().equals(GO)){
                frame.setSize(WDWWIDTH,WDWHEIGHT);
                frame.setLocationRelativeTo(null);
                cl.show(panelCards, "3");
            }
        }
    }
    
    /**
     * This initialises the window with a menu before the game begins.
     * It creates a start and quit button, and sets the frame to be visible.
     */
    private void initialiseMenu() {
        menu.setSize(WDWWIDTH, WDWHEIGHT);
        JButton start = initMenuButton(START, 130, 390, "S", "ENTER");
        menu.add(start);
        
        JButton quit = initMenuButton(QUIT, 260, 390, "Q", "ENTER");
        menu.add(quit);
        
        JLabel label = new JLabel("Warehouse Bros!");
        label.setFont(new Font("Copperplate", Font.PLAIN, 40));
        label.setBounds(70, 50, LBLWIDTH, LBLHEIGHT);
        menu.add(label);
        
        ImageIcon icon = new ImageIcon("mario.jpg"); 
        JLabel thumb = new JLabel();
        thumb.setIcon(icon);
        thumb.setBounds(0,0,500,500);
        menu.add(thumb);
    }
    
    /**
     * This initialises level selection.
     */
    private void initialiseLevelSelection() {
        levelSelection.setSize(WDWWIDTH, WDWHEIGHT);
        
        JLabel lblLevel = new JLabel("Select Difficulty");
        lblLevel.setFont(new Font("Copperplate", Font.PLAIN, 40));
        lblLevel.setForeground(Color.WHITE);
        lblLevel.setBounds(70, 25, LBLWIDTH, LBLHEIGHT);
        levelSelection.add(lblLevel);
        levelSelection.setBackground(Color.DARK_GRAY);
        
        JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Normal", "Heroic", "Legendary"}));
        comboBox.setBounds(200, 150, BTNWIDTH, BTNHEIGHT);
        levelSelection.add(comboBox);
        
        JButton btnGo = initMenuButton(GO, 200, 280, "G", "ENTER");
        levelSelection.add(btnGo);
    }
    
    /**
     * Method to initialise a button with specified name and coordinates
     * This method uses keybinding and input and action maps to map
     * the key the user entered to actions which perform hover and click.
     * i.e. the buttons are set up to work with a keyboard, alleviating the
     * need for a mouse!
     *
     * @param name the name
     * @param x the x
     * @param y the y
     * @param hover the hover
     * @param select the select
     * @return the j button
     */
    private JButton initMenuButton(String name, int x, int y, String hover, String select){
        JButton btn = new JButton(name); // the argument sets the name of the button in UI
        btn.setForeground(Color.DARK_GRAY);
        btn.setName(name); //the argument sets the name of the button object, i.e. if you call getName()
        btn.setBounds(x, y, BTNWIDTH, BTNHEIGHT); //sets position of button (x, y ,xlen, ylen)
        
        //Key binding buttons to respond to keyboard!
        btn.getInputMap(IFW).put(KeyStroke.getKeyStroke(hover), HOVER);
        btn.getInputMap(IF).put(KeyStroke.getKeyStroke(select), SELECT);
        btn.getActionMap().put(HOVER, new getFocus(btn));
        btn.getActionMap().put(SELECT, new clickAction(btn));
        
        //Setting buttons to work by mouse click too!
        btn.setAction(new clickAction(btn));
        btn.setHideActionText(false);
        btn.setText(btn.getName()); //this sets the name, didn't we already do this? Yes. 
                                    //But action sets it to null, so we have to tell it again!
        return btn;
    }
    
    /**
     * method that ask user to confirm exit via quit button.
     *
     * @param e the e
     */
    private void windowClosingQuit(ActionEvent e) {  
        int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", 
                "Quit", JOptionPane.YES_NO_OPTION);
        if(option == JOptionPane.YES_OPTION){  
            System.exit(0);
        }  
    }
    
    /**
     * method that creates option pane prompting user to confirm exit.
     *
     * @param e the e
     */
    @Override
    public void windowClosing(WindowEvent e) {  
        int option=JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", 
                "Quit", JOptionPane.YES_NO_OPTION);
        if(option==JOptionPane.YES_OPTION){  
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        }  
    } 
    
    /**
     * This method takes in a GameMap, and draws it on the screen.
     *
     * @param g the GameMap which contains the map data
     * @param gw the GameWindow on which the map is to be rendered
     */
    public void drawMap (Token[][] map, int dimension) {
        level.removeAll();
        initTitle();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (map[i][j] == null)
                    continue;
                if (map[i][j].getType()=='B') {
                    initToken(j, i, dimension,"box.png");
                } else if (map[i][j].getType()=='W') {
                    initToken(j, i, dimension,"wall.png");
                } else if (map[i][j].getType()=='P') {
                    initToken(j, i, dimension,"player.png");
                } else if (map[i][j].getType()=='G') {
                    initToken(j, i, dimension,"goal.png");
                }
            }
        }
        level.revalidate();
        level.repaint();
    }
    
    /**
     * Initialises a token on the map.
     *
     * @param gw the GameWindow on which the token is to be drawn
     * @param x the x coordinate where the token is to be placed
     * @param y the y coordinate where the token is to be placed
     * @param dimension  the size of a map
     * @param fileName the file name
     */
    public void initToken(int x, int y, int dimension, String fileName) {
        int sizeOfIcon = 29; //size of images
        int midPosition = this.WDWWIDTH/2;	//mid position of our game window 
        int startPosition = midPosition - ((dimension/2)+1)*sizeOfIcon;  //position to start drawing 
    	JLabel box = new JLabel();
        box.setIcon(new ImageIcon(fileName));
        box.setBounds((x+1)*29+startPosition,(y+1)*29+startPosition, 29,29);
        level.add(box);
    }
    
    /**
     * Initialises the title on the level panel.
     *
     * @param gw the GameWindow on which the title is to be drawn
     */
    public void initTitle() {
        JLabel lblLevel = new JLabel("Level One");
        lblLevel.setFont(new Font("Copperplate", Font.PLAIN, 40));
        lblLevel.setForeground(Color.WHITE);
        lblLevel.setBounds(150, 25, LBLWIDTH, LBLHEIGHT);
        level.add(lblLevel);
    }
    

}