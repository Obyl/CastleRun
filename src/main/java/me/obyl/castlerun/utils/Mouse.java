package me.obyl.castlerun.utils;

import me.obyl.castlerun.Game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener{

    public static final int LEFT = 1;
    public static final int MIDDLE = 2;
    public static final int RIGHT = 3;

    private static boolean[] buttons = new boolean[4];
    public static int x, y;
    public static int scroll;

    public static boolean isButtonPressed(int button){
        return buttons[button];
    }

    public static void tick(){
        scroll = 0;
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        buttons[e.getButton()] = true;

    }

    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton()] = false;
    }

    public void mouseEntered(MouseEvent e) {
        Mouse.x = e.getX() / Game.SCALE;
        Mouse.y = e.getY() / Game.SCALE;
    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {
        Mouse.x = e.getX() / Game.SCALE;
        Mouse.y = e.getY() / Game.SCALE;
    }

    public void mouseMoved(MouseEvent e) {
        Mouse.x = e.getX() / Game.SCALE;
        Mouse.y = e.getY() / Game.SCALE;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        scroll = e.getWheelRotation();
    }
}