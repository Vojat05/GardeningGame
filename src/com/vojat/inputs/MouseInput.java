package com.vojat.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.vojat.Enums.ErrorList;
import com.vojat.garden.Flower;
import com.vojat.garden.Game;
import com.vojat.garden.Player;

public class MouseInput implements MouseListener{
    private Player dad;
    private short controlVariableX;
    private short controlVariableY;
    private int assignNumberToPlant = 0;
    private Flower flower;

    public MouseInput(Player player) {
        this.dad = player;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gardenerX(e);
        gardenerY(e);
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                if (dad.selectedItem > 0) {
                    if (Game.map[controlVariableY][controlVariableX] >= 1) {                            // Checks if the desired area is occupied or not
                        System.err.println(ErrorList.ERR_CANTPLANT.message);
                    } else {                                                                            // If not, creates another Flower object to place here
                        flower = new Flower(Game.textures[dad.selectedItem], dad.inventory[dad.selectedItem], (short) gardenerX(e), (short) gardenerY(e), "Alive", assignNumberToPlant, controlVariableX, controlVariableY, dad.selectedItem);
                        dad.plant(flower);
                        wirteIntoMap(controlVariableY, controlVariableX, 1);                      // Writes it's value into map
                        assignNumberToPlant++;                                                          // Assigns the plant index
                    }
                } else if(dad.selectedItem == 0) {
                    if (Game.map[controlVariableY][controlVariableX] == 0) {
                        System.err.println(ErrorList.ERR_NOPLANT.message);
                    } else {
                        dad.water(flower, controlVariableX, controlVariableY);
                    }
                }
                break;
            
            case MouseEvent.BUTTON2:
                getMapData();
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {;}

    @Override
    public void mouseReleased(MouseEvent e) {;}

    @Override
    public void mouseEntered(MouseEvent e) {;}

    @Override
    public void mouseExited(MouseEvent e) {;}    

    private int gardenerX(MouseEvent e) {                                                               // Center the click into a grid place for X
        if (e.getX() <= 128) {
            controlVariableX = 0;
            return 0;
        } else if (e.getX() <= 128*2) {
            controlVariableX = 1;
            return 128;
        } else if (e.getX() <= 128*3) {
            controlVariableX = 2;
            return 128*2;
        } else if (e.getX() <= 128*4) {
            controlVariableX = 3;
            return 128*3;
        } else if (e.getX() <= 128*5) {
            controlVariableX = 4;
            return 128*4;
        } else if (e.getX() <= 128*6) {
            controlVariableX = 5;
            return 128*5;
        } else if (e.getX() <= 128*7) {
            controlVariableX = 6;
            return 128*6;
        } else if (e.getX() <= 128*8) {
            controlVariableX = 7;
            return 128*7;
        } else if (e.getX() <= 128*9) {
            controlVariableX = 8;
            return 128*8;
        } else if (e.getX() <= 128*10) {
            controlVariableX = 9;
            return 128*9;
        } else if (e.getX() <= 128*11) {
            controlVariableX = 10;
            return 128*10;
        } else if (e.getX() <= 128*12) {
            controlVariableX = 11;
            return 128*11;
        } else if (e.getX() <= 128*13) {
            controlVariableX = 12;
            return 128*12;
        } else if (e.getX() <= 128*14) {
            controlVariableX = 13;
            return 128*13;
        } else {
            controlVariableX = 14;
            return 128*14;
        }
    }

    private int gardenerY(MouseEvent e) {                                                                 // Center the click into a grid place for Y
        if (e.getY() <= 128) {
            controlVariableY = 0;
            return 0;
        } else if (e.getY() <= 128*2) {
            controlVariableY = 1;
            return 128;
        } else if (e.getY() <= 128*3) {
            controlVariableY = 2;
            return 128*2;
        } else if (e.getY() <= 128*4) {
            controlVariableY = 3;
            return 128*3;
        } else if (e.getY() <= 128*5) {
            controlVariableY = 4;
            return 128*4;
        } else if (e.getY() <= 128*6) {
            controlVariableY = 5;
            return 128*5;
        } else if (e.getY() <= 128*7) {
            controlVariableY = 6;
            return 128*6;
        } else {
            controlVariableY = 7;
            return 128*7;
        }
    }

    private void wirteIntoMap(int i, int j, int value) {                                                    // Writes data into map at specified location
        Game.map[i][j] = (byte) value;
    }

    private void getMapData() {                                                                             // Retrieves all data from map and prints it into console
        for (int i=0; i<Game.map.length; i++) {
            for (int j=0; j<Game.map[0].length; j++) {
                System.out.print(" | " + Game.map[i][j] + " | ");
            }
            System.out.println("");
        }
    }
}
