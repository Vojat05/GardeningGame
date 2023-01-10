package com.vojat.inputs;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/*
 * Class implementing KeyListener interface
 * Interface must have its every method overriden
 * When extending an abstract class, you don't have to override every method
 */
public class KeyboardInput implements KeyListener{
    
    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(String.format("Key: %s has been pressed", e.getKeyChar()));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println(String.format("Key: %s has been released", e.getKeyChar()));
    }

    @Override
    public void keyTyped(KeyEvent e) {;}
}
