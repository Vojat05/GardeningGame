package com.vojat.inputs;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import com.vojat.garden.GamePanel;


/*
 * Class implementing KeyListener interface
 * Interface must have its every method overriden
 * When extending an abstract class, you don't have to override every method
 */
public class KeyboardInput implements KeyListener{

    private GamePanel gamePanel;

    public KeyboardInput (GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                gamePanel.moveY(-6);
                break;
            
            case KeyEvent.VK_S:
                gamePanel.moveY(6);
                break;
            
            case KeyEvent.VK_A:
                gamePanel.moveX(-6);
                break;

            case KeyEvent.VK_D:
                gamePanel.moveX(6);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {;}

    @Override
    public void keyTyped(KeyEvent e) {;}
}
