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
                if (dad.level == 1) {
                    System.out.println("Interaction 1");
                } else {
                    if (dad.selectedItem > 0 && controlVariableY != 7) {
                        if (Math.abs(controlVariableX - Game.intoMapX(dad.LOCATION_X+64)) > dad.reach || Math.abs(controlVariableY - Game.intoMapY(dad.LOCATION_Y+64)) > dad.reach) {
                            System.err.println(ErrorList.ERR_RANGE_FAR.message);
                        } else if (Game.map[controlVariableY][controlVariableX] >= 2) {                            // Checks if the desired area is occupied or not
                            System.err.println(ErrorList.ERR_CANTPLANT.message);
                        } else if (Math.abs(controlVariableX - Game.intoMapX(dad.LOCATION_X+64)) == 0 && Math.abs(controlVariableY - Game.intoMapY(dad.LOCATION_Y+64)) == 0) {              // Checks if the desired area is the same as player position
                            System.err.println(ErrorList.ERR_RANGE_CLOSE.message);
                        } else {                                                                            // If not, creates another Flower object to place here
                            flower = new Flower(Game.textures[dad.selectedItem], dad.inventory[dad.selectedItem], controlVariableX, controlVariableY, "Alive", assignNumberToPlant);
                            dad.plant(flower);
                            Game.wirteIntoMap(controlVariableY, controlVariableX, 2);                      // Writes it's value into map
                            assignNumberToPlant++;                                                          // Assigns the plant index
                        }
                    } else if(dad.selectedItem == 0) {       // Stop the watering if water isn't selected or if the water is empty or is out of reach
                        if (Math.abs(controlVariableX - Game.intoMapX(dad.LOCATION_X+64)) > dad.reach || Math.abs(controlVariableY - Game.intoMapY(dad.LOCATION_Y+64)) > dad.reach) {
                            System.err.println(ErrorList.ERR_RANGE_FAR.message);
                        } else if (Game.map[controlVariableY][controlVariableX] != 2) {
                            System.err.println(ErrorList.ERR_NOPLANT.message);
                        } else if (Game.textures[0].equals("res/Pics/WaterDrop0.png")) {
                            System.err.println(ErrorList.ERR_WATER.message);
                        } else {
                            dad.water(flower, controlVariableX, controlVariableY);
                            Game.textures[0] = "res/Pics/WaterDrop" + (Integer.parseInt(Game.textures[0].substring(18, 19))-1) + ".png";
                            dad.gamePanel.inventoryPanel.repaintItem(dad);
                        }
                    }
                }
                System.gc();
                break;
            
            case MouseEvent.BUTTON2:
                if (dad.level == 1) {
                    System.out.println("Interaction 2");
                } else {
                    Game.getMapData("print");

                    if (Main.debug) {
                        try {
                            Game.saveGame("src/com/vojat/Data/Saves/Save3.json");
                        } catch (FileNotFoundException f) {
                            System.err.println(ErrorList.ERR_404.message);
                        }
                    }
                }
                break;

            case MouseEvent.BUTTON3:
                if (dad.level == 1) {
                    System.out.println("Interaction 3");
                } else {
                    if (Math.abs(1 - Game.intoMapX(dad.LOCATION_X+64)) <= dad.reach && Math.abs(5 - Game.intoMapY(dad.LOCATION_Y+64)) <= dad.reach) dad.waterRefill(); else System.err.println(ErrorList.ERR_WELL.message);
                }
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

    private void gardenerX(MouseEvent e) {                                                               // Center the click location into a grid place for X
        controlVariableX = Short.parseShort(Integer.toString(Game.intoMapX(e.getX())));
    }

    private void gardenerY(MouseEvent e) {                                                                 // Center the click location into a grid place for Y
        controlVariableY = Short.parseShort(Integer.toString(Game.intoMapY(e.getY())));
    }
}
