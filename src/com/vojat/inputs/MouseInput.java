package com.vojat.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;

import com.vojat.Enums.ErrorList;
import com.vojat.garden.Flower;
import com.vojat.garden.Game;
import com.vojat.garden.Player;

public class MouseInput implements MouseListener {
    private Player dad;
    private short controlVariableX;
    private short controlVariableY;
    private static int assignNumberToPlant = Game.flowers.size();
    private Flower flower;

    public MouseInput(Player player) {
        this.dad = player;
    }

    public static void setAssignPlantNum(int number) {
        assignNumberToPlant = number;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gardenerX(e);
        gardenerY(e);
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                if (dad.selectedItem > 0 && controlVariableY != 7) {
                    if (Game.map[controlVariableY][controlVariableX] >= 2) {                            // Checks if the desired area is occupied or not
                        System.err.println(ErrorList.ERR_CANTPLANT.message);
                    } else {                                                                            // If not, creates another Flower object to place here
                        flower = new Flower(Game.textures[dad.selectedItem], dad.inventory[dad.selectedItem], controlVariableX, controlVariableY, "Alive", assignNumberToPlant);
                        dad.plant(flower);
                        Game.wirteIntoMap(controlVariableY, controlVariableX, 2);                      // Writes it's value into map
                        assignNumberToPlant++;                                                          // Assigns the plant index
                    }
                } else if(dad.selectedItem == 0) {       // Stop the watering if water isn't selected or if the water is empty
                    if (Game.map[controlVariableY][controlVariableX] == 0) {   // Game.textures[0].equals("res/Pics/WaterDrop0.png")
                        System.err.println(ErrorList.ERR_NOPLANT.message);
                    } else if (Game.textures[0].equals("res/Pics/WaterDrop0.png")) {
                        System.err.println(ErrorList.ERR_WATER.message);
                    } else {
                        dad.water(flower, controlVariableX, controlVariableY);
                        Game.textures[0] = "res/Pics/WaterDrop" + (Integer.parseInt(Game.textures[0].substring(18, 19))-1) + ".png";
                        dad.gamePanel.inventoryPanel.repaintItem(dad);
                    }
                }
                System.gc();
                break;
            
            case MouseEvent.BUTTON2:
                Game.getMapData("print");

                try {
                    Game.saveGame("src/com/vojat/Data/Saves/Save3.json");
                } catch (FileNotFoundException f) {
                    System.err.println(ErrorList.ERR_404.message);
                }
                break;

            case MouseEvent.BUTTON3:
                Game.textures[0] = "res/Pics/WaterDrop9.png";
                dad.gamePanel.inventoryPanel.repaintItem(dad);
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

    private void gardenerX(MouseEvent e) {                                                               // Center the click into a grid place for X
        if (e.getX() <= 128) {
            controlVariableX = 0;
        } else if (e.getX() <= 128*2) {
            controlVariableX = 1;
        } else if (e.getX() <= 128*3) {
            controlVariableX = 2;
        } else if (e.getX() <= 128*4) {
            controlVariableX = 3;
        } else if (e.getX() <= 128*5) {
            controlVariableX = 4;
        } else if (e.getX() <= 128*6) {
            controlVariableX = 5;
        } else if (e.getX() <= 128*7) {
            controlVariableX = 6;
        } else if (e.getX() <= 128*8) {
            controlVariableX = 7;
        } else if (e.getX() <= 128*9) {
            controlVariableX = 8;
        } else if (e.getX() <= 128*10) {
            controlVariableX = 9;
        } else if (e.getX() <= 128*11) {
            controlVariableX = 10;
        } else if (e.getX() <= 128*12) {
            controlVariableX = 11;
        } else if (e.getX() <= 128*13) {
            controlVariableX = 12;
        } else if (e.getX() <= 128*14) {
            controlVariableX = 13;
        } else {
            controlVariableX = 14;
        }
    }

    private void gardenerY(MouseEvent e) {                                                                 // Center the click into a grid place for Y
        if (e.getY() <= 128) {
            controlVariableY = 0;
        } else if (e.getY() <= 128*2) {
            controlVariableY = 1;
        } else if (e.getY() <= 128*3) {
            controlVariableY = 2;
        } else if (e.getY() <= 128*4) {
            controlVariableY = 3;
        } else if (e.getY() <= 128*5) {
            controlVariableY = 4;
        } else if (e.getY() <= 128*6) {
            controlVariableY = 5;
        } else if (e.getY() <= 128*7) {
            controlVariableY = 6;
        } else {
            controlVariableY = 7;
        }
    }
}
