package com.vojat.inputs;

import com.vojat.garden.GamePanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseInput implements MouseListener{

    protected GamePanel gamePanel;

    public MouseInput(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("X: " + e.getX() + " | Y: " + e.getY());     // Test the clicking location
    }

    @Override
    public void mousePressed(MouseEvent e) {;}

    @Override
    public void mouseReleased(MouseEvent e) {;}

    @Override
    public void mouseEntered(MouseEvent e) {;}

    @Override
    public void mouseExited(MouseEvent e) {;}    
}
