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
        Flower flower = new Flower("res/Red_Tulip.png", "fialka", gardenerX(e), gardenerY(e), "Alive");
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

    private int gardenerX(MouseEvent e) {
        if (e.getX() <= 128) {
            return 0;
        } else if (e.getX() <= 128*2) {
            return 128;
        } else if (e.getX() <= 128*3) {
            return 128*2;
        } else if (e.getX() <= 128*4) {
            return 128*3;
        } else if (e.getX() <= 128*5) {
            return 128*4;
        } else if (e.getX() <= 128*6) {
            return 128*5;
        } else if (e.getX() <= 128*7) {
            return 128*6;
        } else if (e.getX() <= 128*8) {
            return 128*7;
        } else if (e.getX() <= 128*9) {
            return 128*8;
        } else if (e.getX() <= 128*10) {
            return 128*9;
        } else if (e.getX() <= 128*11) {
            return 128*10;
        } else if (e.getX() <= 128*12) {
            return 128*11;
        } else if (e.getX() <= 128*13) {
            return 128*12;
        } else if (e.getX() <= 128*14) {
            return 128*13;
        } else {
            return 128*14;
        }
    }

    private int gardenerY(MouseEvent e) {
        if (e.getY() <= 128) {
            return 0;
        } else if (e.getY() <= 128*2) {
            return 128;
        } else if (e.getY() <= 128*3) {
            return 128*2;
        } else if (e.getY() <= 128*4) {
            return 128*3;
        } else if (e.getY() <= 128*5) {
            return 128*4;
        } else if (e.getY() <= 128*6) {
            return 128*5;
        } else if (e.getY() <= 128*7) {
            return 128*6;
        } else {
            return 128*7;
        }
    }
}
