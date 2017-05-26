import java.awt.Font;
import java.applet.AudioClip;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

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
    
    private GameMap gmInGW = null;
    
    /** The Constant GO. */
    private static final String
        HOVER = "HOVER",
        SELECT = "SELECT",
        START = "START",
        QUIT = "QUIT",
        NEXT = "NEXT",
        GO = "GO",
        UP = "UP",
        DOWN = "DOWN",
        LEFT = "LEFT",
        RIGHT = "RIGHT",
        RESTART = "RESTART",
        END = "END";
    
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
    
    /** Character Select. */
    protected JPanel characterSelect;
    
    /** Win screen. */
    protected JPanel winScreen;
    
    /** The background Music. */
    private AudioClip bgm; 
    
    private ArrayList<ImageIcon> images;
    private final int BOX = 0;
    private final int GOAL = 1;
    private final int PLAYERUP = 5;
    private final int PLAYERLEFT = 3;
    private final int PLAYERRIGHT = 4;
    private final int PLAYERDOWN = 2;
    private final int WALL = 6;
    private final int GBOX = 7;

    
    /**
     * This creates an empty game window, that the game will present in.
     * It defaults to being placed in the centre of the screen.
     * It is initalised as an empty window, and needs to be populated!
     * It is not visible when created, and must be turned on later!
     */
    public GameWindow(GameMap gm) {
        gmInGW = gm;
        loadAssets();
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
        characterSelect = new JPanel(null);
        winScreen = new JPanel(null);
        cl = new CardLayout();
        
        panelCards.setLayout(cl);
        panelCards.add(menu, "1");
        panelCards.add(levelSelection, "2");
        panelCards.add(level, "3");
        panelCards.add(winScreen, "5");
        panelCards.add(characterSelect, "6");
        cl.show(panelCards, "1");
        frame.getContentPane().add(panelCards);
        initialiseMenu();
        initialiseLevelSelection();
        initialiseCharScreen();
        frame.setVisible(true);
    }
    
    public void loadAssets() {
        images = new ArrayList<>();
        images.add(new ImageIcon("box.png"));//0
        images.add(new ImageIcon("goal.png"));//1
        images.add(new ImageIcon("playerdown.png"));//2
        images.add(new ImageIcon("playerleft.png"));//3
        images.add(new ImageIcon("playerright.png"));//4
        images.add(new ImageIcon("playerup.png"));//5
        images.add(new ImageIcon("wall.png"));//6
        images.add(new ImageIcon("gbox.png"));//7
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
            } else if (temp.getName().equals(NEXT)){
                frame.setSize(WDWWIDTH,WDWHEIGHT);
                frame.setLocationRelativeTo(null);
                cl.show(panelCards, "6");
            } else if (temp.getName().equals(GO)){
                frame.setSize(WDWWIDTH,WDWHEIGHT);
                frame.setLocationRelativeTo(null);
                cl.show(panelCards, "3");
            } else if (temp.getName().equals(RESTART)){
                try {
                    String file = "map" + Integer.toString(gmInGW.getCurrLevel()+1) +".txt";
                    gmInGW.readMap(file);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                drawMap(gmInGW.getMap(), gmInGW.getCurrLevel(), gmInGW.getDimension(), 1);
            } else if (temp.getName().equals(END)) {
                System.exit(0);
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
        levelSelection.setBackground(Color.DARK_GRAY);
        
        JLabel title = LabelForSelection("Choice selection",100,25,40);
        levelSelection.add(title);
        
        
        JLabel level = LabelForSelection("Difficulty:",80,95,20);
        levelSelection.add(level);

        
        JLabel musicSwitch = LabelForSelection("Music on/off:",80,150,20);
        levelSelection.add(musicSwitch);
        
        JLabel musicSelect = LabelForSelection("Select Music:",80,210,20);
        levelSelection.add(musicSelect);
        
        JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Normal", "Heroic", "Legendary"}));
        comboBox.setBounds(250, 100, BTNWIDTH+20, BTNHEIGHT);
        levelSelection.add(comboBox);
        
        JButton btnNext = initMenuButton(NEXT, 250, 280, "N", "ENTER");
        levelSelection.add(btnNext);
        
      //making the radio Button and the confirm button
        ButtonGroup  c = new ButtonGroup ();
        JRadioButton c1 = new JRadioButton("on");
        JRadioButton c2 = new JRadioButton("off");
        
        c.add(c1);
        c.add(c2);
        c1.setName("on");
        c2.setName("off");   
        c1.setBounds(250, 150, BTNWIDTH, BTNHEIGHT);
        c2.setBounds(250, 180, BTNWIDTH, BTNHEIGHT);
        c1.setBackground(Color.DARK_GRAY);
        c1.setForeground(Color.WHITE);
        c2.setBackground(Color.DARK_GRAY);
        c2.setForeground(Color.WHITE);
        c2.setSelected(true);
        levelSelection.add(c1);
        levelSelection.add(c2);
        
        //making a ComboBox to select the songs
        JComboBox<String> musicComboBox = new JComboBox<String>();
        musicComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Mario", "Sonic"}));
        musicComboBox.setBounds(250, 220,  BTNWIDTH, BTNHEIGHT);
        levelSelection.add(musicComboBox);    
        bgm = loadBGM ("bgm1Mario.wav");
        musicComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                if(musicComboBox.getSelectedItem() == "Mario"){
                    bgm = loadBGM ("bgm1Mario.wav");
                    System.out.println("You selected song1");
                }else if(musicComboBox.getSelectedItem() == "Sonic"){
                    bgm = loadBGM ("bgm2Sonic.wav");
                    System.out.println("You selected song2");
                }
            }
        });
       
        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(c1.isSelected()){
                    System.out.println("music on");
                    bgm.loop();
                }else if(c2.isSelected()){
                    System.out.println("music off");
                    bgm.stop();
                }
            }
        });
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
        public move(GameMap gm, GameWindow gw, String s) {
            key = s;
            this.gm = gm;
            this.gw = gw;
        }
        
        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean changed = false;
            int face = 0;   //1: face up, 2: face down, 3: face left, 4: face right
            switch (key) {
            case UP: 
                if (gm.getPlayer().moveUp(gm)) {
                    changed = true;
                    face = 1;
                }
                break;
            case DOWN: 
                if (gm.getPlayer().moveDown(gm)) {
                    changed = true;
                    face = 2;
                }
                break;
            case LEFT: 
                if (gm.getPlayer().moveLeft(gm)){
                    changed = true;
                    face = 3;
                } 
                break;
            case RIGHT: 
                if (gm.getPlayer().moveRight(gm)) {
                    changed = true;
                    face = 4;
                }
                break;
            }
            if (changed && face != 0) gw.drawMap(gm.getMap(), gm.getCurrLevel(), gm.getDimension(),face);
            if (gm.getNumGoals()==0){
                gw.level.removeAll();
                gw.initialiseLevelOne(gm);
                if (gm.loadNextLevel()) {
                    gw.drawMap(gm.getMap(), gm.getCurrLevel(), gm.getDimension(),face);
                } else {
                    gw.initialiseWinScreen();
                    gw.cl.show(gw.panelCards, "5");
                }
                
            }
        }
    }
    
    /**
     * Initialise level one.
     *
     * @param gw the GameWindow on which the level is to be drawn
     */
    public void initialiseLevelOne(GameMap gm) {
        level.setSize(WDWWIDTH, WDWHEIGHT);
        initTitle(gm.getCurrLevel());
        level.setBackground(Color.DARK_GRAY);
        
        level.getInputMap(IFW).put(KeyStroke.getKeyStroke(UP), UP);
        level.getInputMap(IFW).put(KeyStroke.getKeyStroke(DOWN), DOWN);
        level.getInputMap(IFW).put(KeyStroke.getKeyStroke(LEFT), LEFT);        
        level.getInputMap(IFW).put(KeyStroke.getKeyStroke(RIGHT), RIGHT);
        level.getActionMap().put(UP, new move(gm, this, UP));
        level.getActionMap().put(DOWN, new move(gm, this, DOWN));
        level.getActionMap().put(LEFT, new move(gm, this, LEFT));
        level.getActionMap().put(RIGHT, new move(gm, this, RIGHT));
    }
    
    /**
     *
     */
    public void initialiseCharScreen() {
        characterSelect.setSize(WDWWIDTH, WDWHEIGHT);
        JLabel lblLevel = new JLabel("Choose Character!");
        lblLevel.setFont(new Font("Copperplate", Font.PLAIN, 40));
        lblLevel.setForeground(Color.WHITE);
        lblLevel.setBounds(60, 70, LBLWIDTH, LBLHEIGHT);
        characterSelect.add(lblLevel);
        characterSelect.setBackground(Color.DARK_GRAY);
        JButton btnGo = initMenuButton(GO, 195, 380, "E", "ENTER");
        characterSelect.add(btnGo);
    }
    
    /**
     * 
     */
    public void initialiseWinScreen() {
        winScreen.setSize(WDWWIDTH, WDWHEIGHT);
        JLabel lblLevel = new JLabel("You Won!");
        lblLevel.setFont(new Font("Copperplate", Font.PLAIN, 60));
        lblLevel.setForeground(Color.WHITE);
        lblLevel.setBounds(110, 200, LBLWIDTH, LBLHEIGHT);
        winScreen.add(lblLevel);
        winScreen.setBackground(Color.DARK_GRAY);
        JButton btnGo = initMenuButton(END, 195, 380, "E", "ENTER");
        winScreen.add(btnGo);
    }
    
    /**
     * Label for initialiseLevelSelection window
     */
    public JLabel LabelForSelection(String LabelName,int x,int y,int size){
         JLabel newLabel = new JLabel(LabelName);
         newLabel.setFont(new Font("Copperplate", Font.PLAIN, size));
         newLabel.setForeground(Color.WHITE);
         newLabel.setBounds(x, y, LBLWIDTH, LBLHEIGHT);
         
         return newLabel;
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
    public void drawMap (Token[][] map, int currLevel, int dimension, int face) {
        level.removeAll();
        initTitle(currLevel);
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (map[i][j] == null)
                    continue;
                if (map[i][j].getType()=='B') {
                    Box b = (Box) map[i][j];
                    if (b.isOnGoal()) {
                        initToken(j, i, dimension, GBOX);
                    } else {
                        initToken(j, i, dimension, BOX);
                    }
                } else if (map[i][j].getType()=='W') {
                    initToken(j, i, dimension, WALL);
                } else if (map[i][j].getType()=='P' && face == 1) {
                    initToken(j, i, dimension, PLAYERUP);
                } else if (map[i][j].getType()=='P' && face == 2) {
                    initToken(j, i, dimension, PLAYERDOWN);
                } else if (map[i][j].getType()=='P' && face == 3) {
                    initToken(j, i, dimension, PLAYERLEFT);
                } else if (map[i][j].getType()=='P' && face == 4) {
                    initToken(j, i, dimension, PLAYERRIGHT);
                } 
                else if (map[i][j].getType()=='G') {
                    initToken(j, i, dimension, GOAL);
                }
            }
        }
        JButton restart = initMenuButton(RESTART, 10, 10, "R", "ENTER");
        level.add(restart);
        level.revalidate();
        level.repaint();
    }
    
    /**
     * Initialises a token on the map.
     *
     * @param gw the GameWindow on which the token is to be drawn
     * @param x the x coordinate where the token is to be placed
     * @param y the y coordinate where the token is to be placed
     * @param fileName the file name
     */
    public void initToken(int x, int y, int dimension, int fileName) {
        int sizeOfIcon = 29; //size of images
        int midPosition = this.WDWWIDTH/2;  //mid position of our game window 
        int startPosition = midPosition - ((dimension/2)+1)*sizeOfIcon;  //position to start drawing 
        JLabel box = new JLabel();
        box.setIcon(images.get(fileName));
        box.setBounds((x+1)*29+startPosition,(y+1)*29+startPosition, 29,29);
        level.add(box);
    }
    
    /**
     * Initialises the title on the level panel.
     *
     * @param gw the GameWindow on which the title is to be drawn
     */
    public void initTitle(int currLevel) {
        JLabel lblLevel = new JLabel("Level " + (currLevel+1));
        lblLevel.setFont(new Font("Copperplate", Font.PLAIN, 40));
        lblLevel.setForeground(Color.WHITE);
        lblLevel.setBounds(170, 25, LBLWIDTH, LBLHEIGHT);
        level.add(lblLevel);
    }
    
    /**
     *  Play the background Music
     */
    public AudioClip loadBGM (String filename) {
        URL url = null;
        try
        {
            url = new URL ("file:" + filename);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return JApplet.newAudioClip (url);
    }

}