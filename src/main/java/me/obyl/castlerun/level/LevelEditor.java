package me.obyl.castlerun.level;

/*
Have a program to create and edit level files

On startup:
Create brand new level file of set size
Display a side bar:
    - At top, have four buttons for four possible layers
    - Depending on selected layer, display something below:
    - Image Layer 1 & 2:
        - Vertical list (2 wide?) of all usable tiles
        - Click on a tile to select it
    - Collision Layer:
        - Brush or eraser tool
    - Interact layer:
        - Click on tile in world and then edit properties on sidebar
Left click on the screen to place a tile (on a grid)
Right click to delete
Keyboard shortcuts:
    - Ctrl N: New Level
    - Ctrl S: Save Level
    - Ctrl O: Open Level
 */

import com.google.gson.Gson;
import me.obyl.castlerun.level.Level;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LevelEditor extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{

    private static final long serialVersionUID = 1L;

    private static final int IMAGE_1 = 0;
    private static final int IMAGE_2 = 1;
    private static final int COLLISION = 2;
    private static final int INTERACT = 3;

    private static final Color selectedColor = new Color(100, 100, 100, 100);
    private static final Color overlayColor = new Color(200, 200, 200, 100);
    private static final Color collisionColor = new Color(50, 50, 50, 200);
    private static final Gson gson = new Gson();

    private Level level;
    private String filePath;
    private BufferedImage[] imageCache;
    private int mode = IMAGE_1;
    private int sidebarScroll = 0;
    private int selectedTile = 0;
    private int xScroll = 0;
    private int yScroll = 0;

    private int oldX = 0;
    private int oldY = 0;
    private int pressedButton = -1;
    private boolean ctrlHeld = false;

    public LevelEditor(){
        imageCache = new BufferedImage[Level.tileset.length];
        for(int i = 0; i < imageCache.length; i++){
            int width = Level.tileset[i].width;
            int height = Level.tileset[i].height;

            int[] source = Level.tileset[i].pixels;
            for(int p = 0; p < source.length; p++)
                if(source[p] != 0xff00ff)
                    source[p] = 0xff000000 | source[p];

            imageCache[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            imageCache[i].setRGB(0, 0, width, height, source, 0, width);
        }

        level = new Level("default", 10, 10);

        setDoubleBuffered(true);
        setSize(960, 700);
        setLayout(null);
        setFocusable(true);
        requestFocus();

        JFrame frame = new JFrame("Castle Run Level Editor");
        frame.setSize(960, 700);
        frame.add(this);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        JButton image1Button = new JButton("Image 1");
        image1Button.setBounds(820, 4, 132, 40);
        image1Button.setFocusable(false);
        image1Button.setEnabled(false);
        add(image1Button);
        JButton image2Button = new JButton("Image 2");
        image2Button.setBounds(820, 48, 132, 40);
        image2Button.setFocusable(false);
        add(image2Button);
        JButton collisionButton = new JButton("Collision");
        collisionButton.setBounds(820, 92, 132, 40);
        collisionButton.setFocusable(false);
        add(collisionButton);
        JButton interactButton = new JButton("Interact");
        interactButton.setBounds(820, 136, 132, 40);
        interactButton.setFocusable(false);
        add(interactButton);

        image1Button.addActionListener((e) -> {
            image1Button.setEnabled(false);
            image2Button.setEnabled(true);
            collisionButton.setEnabled(true);
            interactButton.setEnabled(true);

            mode = IMAGE_1;
        });
        image2Button.addActionListener((e) -> {
            image1Button.setEnabled(true);
            image2Button.setEnabled(false);
            collisionButton.setEnabled(true);
            interactButton.setEnabled(true);

            mode = IMAGE_2;
        });
        collisionButton.addActionListener((e) -> {
            image1Button.setEnabled(true);
            image2Button.setEnabled(true);
            collisionButton.setEnabled(false);
            interactButton.setEnabled(true);

            mode = COLLISION;
        });
        interactButton.addActionListener((e) -> {
            image1Button.setEnabled(true);
            image2Button.setEnabled(true);
            collisionButton.setEnabled(true);
            interactButton.setEnabled(false);

            mode = INTERACT;
        });
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 816, 700);

        for(int y = 0; y < level.height; y++){
            for(int x = 0; x < level.width; x++){
                int xPos = (x * 48) + xScroll;
                int yPos = (y * 48) + yScroll;
                if(xPos <= -48 || yPos <= -48 || xPos > (48 * 16) || yPos > (48 * 14))
                    continue;

                int targetIndex = x + y * level.width;
                int sprite1 = level.sprites1[targetIndex];
                int sprite2 = level.sprites2[targetIndex];
                boolean collision = level.collision[targetIndex];

                g.drawImage(imageCache[sprite1], xPos, yPos, 48, 48, null);

                if(mode == IMAGE_1)
                    continue;

                if(sprite1 != 0){
                    g.setColor(overlayColor);
                    g.fillRect(xPos, yPos, 48, 48);
                }

                g.drawImage(imageCache[sprite2], xPos, yPos, 48, 48, null);

                if(mode == IMAGE_2)
                    continue;

                if(sprite2 != 0){
                    g.setColor(overlayColor);
                    g.fillRect(xPos, yPos, 48, 48);
                }

                if(collision){
                    g.setColor(collisionColor);
                    g.fillRect(xPos, yPos, 48, 48);
                }
            }
        }

        g.setColor(Color.DARK_GRAY);
        g.fillRect(769, 0, 48, 700);

        //Sidebar:
        if(mode == IMAGE_1 || mode == IMAGE_2){
            for(int i = 0; i < 14; i++)
                g.drawImage(imageCache[i + (sidebarScroll * 2)], 820 + (i % 2 != 0 ? 66 : 0), 180 + ((i / 2) * 70), 64, 64, null);

            int selectedX = selectedTile % 2;
            int selectedY = selectedTile / 2;
            if(selectedY - sidebarScroll >= 0 && selectedY - sidebarScroll < 7){
                g.setColor(selectedColor);
                g.fillRect(820 + (selectedX * 66), 180 + ((selectedY - sidebarScroll) * 70), 64, 64);
            }
        }

        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if(e.getButton() == 1){
            if(x > 819 && y > 179 && (mode == IMAGE_1 || mode == IMAGE_2)){
                int targetX = (x - 820) / 66;
                int targetY = (y - 180) / 70;
                selectedTile = targetX + (targetY + sidebarScroll) * 2;
            }else if(x <= 768){
                int targetX = (x - xScroll) / 48;
                int targetY = (y - yScroll) / 48;

                if(mode == IMAGE_1)
                    level.sprites1[targetX + targetY * level.width] = selectedTile;
                else if(mode == IMAGE_2)
                    level.sprites2[targetX + targetY * level.width] = selectedTile;
                else if(mode == COLLISION)
                    level.collision[targetX + targetY * level.width] = true;
            }
        }else if(e.getButton() == 3 && x <= 768){
            int targetX = (x - xScroll) / 48;
            int targetY = (y - yScroll) / 48;

            if(mode == IMAGE_1)
                level.sprites1[targetX + targetY * level.width] = 0;
            else if(mode == IMAGE_2)
                level.sprites2[targetX + targetY * level.width] = 0;
            else if(mode == COLLISION)
                level.collision[targetX + targetY * level.width] = false;
        }

        oldX = x;
        oldY = y;
        pressedButton = e.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        pressedButton = -1;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if(pressedButton == 2 && x < 820){
            xScroll += x - oldX;
            yScroll += y - oldY;

            oldX = x;
            oldY = y;
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(e.getX() > 820){
            sidebarScroll += e.getWheelRotation();
            if(sidebarScroll < 0)
                sidebarScroll = 0;
            else if(sidebarScroll > 505)
                sidebarScroll = 505;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if(keyCode == KeyEvent.VK_CONTROL){
            ctrlHeld = true;
            return;
        }

        if(ctrlHeld){
            switch (keyCode){
                case KeyEvent.VK_N:
                    JFrame newFileWindow = new JFrame("Create New Level");
                    newFileWindow.setSize(300, 150);
                    newFileWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    newFileWindow.setLayout(new GridLayout(4, 1, 0, 0));
                    newFileWindow.setLocationRelativeTo(null);
                    newFileWindow.setResizable(false);
                    newFileWindow.setVisible(true);

                    JTextField nameField = new JTextField();
                    nameField.setText("name");
                    newFileWindow.add(nameField);

                    JTextField widthField = new JTextField();
                    widthField.setText("width");
                    newFileWindow.add(widthField);

                    JTextField heightField = new JTextField();
                    heightField.setText("height");
                    newFileWindow.add(heightField);

                    JButton newFileButton = new JButton("Create New Level");
                    newFileWindow.add(newFileButton);

                    newFileButton.addActionListener((event) -> {
                        level = new Level(nameField.getText(), Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));
                        newFileWindow.dispose();
                        xScroll = yScroll = 0;
                    });

                    break;
                case KeyEvent.VK_O:
                    JFileChooser open = new JFileChooser();
                    open.setFileFilter(new FileNameExtensionFilter("JSON File", "json"));

                    int openOption = open.showOpenDialog(this);
                    if(openOption == JFileChooser.APPROVE_OPTION){
                        filePath = open.getCurrentDirectory().getAbsolutePath() + "\\" + open.getSelectedFile().getName();
                    }

                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(filePath));
                        level =  gson.fromJson(reader.readLine(), Level.class);
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    break;
                case KeyEvent.VK_S:
                    if(filePath == null){
                        File file = new File("assets/data/levels/" + level.name + ".json");
                        filePath = file.getAbsolutePath();
                    }

                    try {
                        FileWriter writer = new FileWriter(filePath);

                        writer.write(gson.toJson(level));

                        writer.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_CONTROL)
            ctrlHeld = false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseMoved(MouseEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}