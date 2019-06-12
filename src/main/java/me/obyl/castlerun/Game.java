package me.obyl.castlerun;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.obyl.castlerun.entity.Player;
import me.obyl.castlerun.graphics.Display;
import me.obyl.castlerun.graphics.Font;
import me.obyl.castlerun.graphics.Sprite;
import me.obyl.castlerun.utils.Keyboard;
import me.obyl.castlerun.utils.Mouse;
import me.obyl.castlerun.level.Level;
import me.obyl.castlerun.menu.LoadingMenu;
import me.obyl.castlerun.menu.MainMenu;
import me.obyl.castlerun.menu.SaveMenu;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Timer;
import java.util.TimerTask;

/*
DUE DATE: June 17, 2019
7 days from now.

How to finish the game:
    Implement the player.
    Make a list of all interact tiles the game will require.
    Implement those tiles into the level editor.
    Implement those tiles into the game.
    Implement the enemy/combat system.
    Implement enemies into the level editor. (?)
    Go through the game level by level and design them.
    Design the boss system.
    Implement the (three?) bosses.
    Implement audio engine.
    Implement options menu.
*/

public class Game extends Canvas {

    private static final long serialVersionUID = 1L;
    
    public static final int WIDTH = 256;
    public static final int HEIGHT = 224;
    public static final int SCALE = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / HEIGHT;

    private JFrame frame;
    private BufferedImage image;
    private BufferStrategy bs;
    public int[] pixels;

    public GameState state;
    public Display display;

    public LoadingMenu loadingMenu;
    public MainMenu mainMenu;
    public SaveMenu saveMenu;

    public Player player;

    public Game(){
        // Setup window:
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));

        frame = new JFrame("Castle Run");
        frame.setResizable(false);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        display = new Display(this);
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        this.createBufferStrategy(2);
        bs = this.getBufferStrategy();

        // Load game:
        load();
    }

    private void load(){
        Gson gson = new Gson();
        state = GameState.LOADING;

        //================================
        // BEFORE THE PLAYER SEES ANYTHING
        //================================
        
        loadingMenu = new LoadingMenu(this);

        //Start game loop:
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tick();
                render();
            }
        }, 0, 16);

        frame.setVisible(true);

        //================================
        // THE PLAYER NOW SEES STUFF
        //================================

        // Setup input:
        Mouse mouse = new Mouse();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addMouseWheelListener(mouse);
        loadingMenu.addToLoadCount();

        addKeyListener(new Keyboard());

        // Load font data:
        Font.load(gson);
        loadingMenu.addToLoadCount();

        // Load player's save data:
        player = new Player(this);
        loadingMenu.addToLoadCount();

        // Load menus:
        mainMenu = new MainMenu(this);
        saveMenu = new SaveMenu(this, gson);

        // Load tile textures:
        Sprite tileSpriteSheet = new Sprite("/textures/tiles.png");
        for(int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                Level.tileset[x + (y << 5)] = new Sprite(tileSpriteSheet, 16, 16, x, y);
                loadingMenu.addToLoadCount();
            }
        }

        // Load levels:
        Type parseType = new TypeToken<String[]>(){}.getType();
        String[] levelFiles = gson.fromJson(
            new BufferedReader(new InputStreamReader(Game.class.getResourceAsStream("/data/level_list.json"))), 
            parseType);
        for(int i = 0; i < levelFiles.length; i++){
            Level.loadLevelFromFile(gson, levelFiles[i]);
            //loadingMenu.addToLoadCount();
        }
    }

    private void tick(){
        switch (state){
            case LOADING:
                loadingMenu.tick();
                break;
            case MAIN_MENU:
                mainMenu.tick();
                break;
            case VIEW_SAVES:
                saveMenu.tick();
                break;
            case PLAYING:
                break;
            case INVENTORY:
                break;
            case PAUSED:
                break;
        }
    }

    private void render(){
        for(int i = 0; i < pixels.length; i++)
            pixels[i] = 0;

        switch (state){
            case LOADING:
                loadingMenu.render();
                break;
            case MAIN_MENU:
                mainMenu.render();
                break;
            case VIEW_SAVES:
                saveMenu.render();
                break;
            case PLAYING:
                Level.levels.get("area2").render(display);
                break;
            case INVENTORY:
                break;
            case PAUSED:
                break;
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g.dispose();
        bs.show();
    }
}