package com.vojat.inputs;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import com.vojat.garden.Player;


/*
 * Class implementing KeyListener interface
 * Interface must have its every method overriden
 * When extending an abstract class, you don't have to override every method
 */
public class KeyboardInput implements KeyListener{

    private Player dad;
    private boolean up = true, down = true, left = true, right = true;

    public KeyboardInput (Player dad) {
        this.dad = dad;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (up) {
                    dad.setTexture("res/Dad_Texture_B.png");
                    up = false;
                }
                dad.moveUP(-6);
                break;
            
            case KeyEvent.VK_DOWN:
                if (down) {
                    dad.setTexture("res/Dad_Texture_F.png");
                    down = false;
                }
                dad.moveUP(6);
                break;
            
            case KeyEvent.VK_LEFT:
                if (left) {
                    dad.setTexture("res/Dad_Texture_L.png");
                    left = false;
                }
                dad.moveSIDE(-6);
                break;

            case KeyEvent.VK_RIGHT:
                if (right) {
                    dad.setTexture("res/Dad_Texture_R.png");
                    right = false;
                }
                dad.moveSIDE(6);
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
