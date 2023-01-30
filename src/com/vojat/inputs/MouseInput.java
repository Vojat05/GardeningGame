package com.vojat.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.vojat.garden.Flower;
import com.vojat.garden.Player;

public class MouseInput implements MouseListener{
    private Player dad;

    public MouseInput(Player player) {
        this.dad = player;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Flower flower = new Flower("res/Red_Tulip.png", "fialka", e.getX() - 32, e.getY() - 32, "Alive");
        dad.plant(flower);
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
