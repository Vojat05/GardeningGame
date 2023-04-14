package com.vojat.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;

import com.vojat.Main;
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
                    if (Game.map[controlVariableY][controlVariableX] != 2) {   // Game.textures[0].equals("res/Pics/WaterDrop0.png")
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

                if (Main.debug) {
                    try {
                        Game.saveGame("src/com/vojat/Data/Saves/Save3.json");
                    } catch (FileNotFoundException f) {
                        System.err.println(ErrorList.ERR_404.message);
                    }
                }
                break;

            case MouseEvent.BUTTON3:
                if (gardenerX(dad.LOCATION_X+64) == 1 && gardenerY(dad.LOCATION_Y+64) == 5) dad.waterRefill(); else System.err.println(ErrorList.ERR_WELL.message);
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

    private void gardenerX(MouseEvent e) {                                                               // Center the click into a grid place for X
        controlVariableX = Short.parseShort(Integer.toString(gardenerX(e.getX())));
    }

    public static int gardenerX(int positionX) {
        if (positionX <= 128) {
            return 0;
        } else if (positionX <= 128*2) {
            return 1;
        } else if (positionX <= 128*3) {
            return 2;
        } else if (positionX <= 128*4) {
            return 3;
        } else if (positionX <= 128*5) {
            return 4;
        } else if (positionX <= 128*6) {
            return 5;
        } else if (positionX <= 128*7) {
            return 6;
        } else if (positionX <= 128*8) {
            return 7;
        } else if (positionX <= 128*9) {
            return 8;
        } else if (positionX <= 128*10) {
            return 9;
        } else if (positionX <= 128*11) {
            return 10;
        } else if (positionX <= 128*12) {
            return 11;
        } else if (positionX <= 128*13) {
            return 12;
        } else if (positionX <= 128*14) {
            return 13;
        } else {
            return 14;
        }
    }

    private void gardenerY(MouseEvent e) {                                                                 // Center the click into a grid place for Y
        controlVariableY = Short.parseShort(Integer.toString(gardenerY(e.getY())));
    }

    public static int gardenerY(int positionY) {
        if (positionY <= 128) {
            return 0;
        } else if (positionY <= 128*2) {
            return 1;
        } else if (positionY <= 128*3) {
            return 2;
        } else if (positionY <= 128*4) {
            return 3;
        } else if (positionY <= 128*5) {
            return 4;
        } else if (positionY <= 128*6) {
            return 5;
        } else if (positionY <= 128*7) {
            return 6;
        } else {
            return 7;
        }
    }
}
