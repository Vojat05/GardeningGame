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

    boolean up = true;
    boolean down = true;
    boolean left = true;
    boolean right = true;
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (up) {
                    gamePanel.setTextures("res/Dad_Texture_B.png");
                    up = false;
                }
                gamePanel.moveY(-6);
                break;
            
            case KeyEvent.VK_DOWN:
                if (down) {
                    gamePanel.setTextures("res/Dad_texture_F.png");
                    down = false;
                }
                gamePanel.moveY(6);
                break;
            
            case KeyEvent.VK_LEFT:
                if (left) {
                    gamePanel.setTextures("res/Dad_Texture_L.png");
                    left = false;
                }
                gamePanel.moveX(-6);
                break;

            case KeyEvent.VK_RIGHT:
                if (right) {
                    gamePanel.setTextures("res/Dad_Texture_R.png");
                    right = false;
                }
                gamePanel.moveX(6);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                up = true;
                break;
            
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            
            case KeyEvent.VK_LEFT:
                left = true;
                break;

            case KeyEvent.VK_RIGHT:
                right = true;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {;}
}
