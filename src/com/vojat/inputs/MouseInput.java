package com.vojat.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;

import com.vojat.Main;
import com.vojat.Enums.ErrorList;
import com.vojat.garden.Flower;
import com.vojat.garden.Game;
import com.vojat.garden.GamePanel;

public class MouseInput implements MouseListener {

    /*
     * ----------------------------------------------------------------
     * Mouse input variables
     * ----------------------------------------------------------------
     */

    private GamePanel gamePanel;                                                        // Game panel
    private short controlVariableX;                                                     // Theoretical mouse X coordinate in the game map 
    private short controlVariableY;                                                     // Theoretical mouse Y coordiante in the game map
    private Flower flower;                                                              // Flower object that's set on each click on some flower

    /*
     * ----------------------------------------------------------------
     * In-game mouse click interactions
     * ----------------------------------------------------------------
     */

    public MouseInput(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (Game.pause) return;
        gardenerX(e);
        gardenerY(e);
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:

                // Too far and close checks
                if (Math.abs(controlVariableX - Game.intoMapX(gamePanel.dad.LOCATION_X+64)) > gamePanel.dad.reach || Math.abs(controlVariableY - Game.intoMapY(gamePanel.dad.LOCATION_Y+64)) > gamePanel.dad.reach) {
                    System.err.println(ErrorList.ERR_RANGE_FAR.message);
                    return;
                } else if (Math.abs(controlVariableX - Game.intoMapX(gamePanel.dad.LOCATION_X+64)) == 0 && Math.abs(controlVariableY - Game.intoMapY(gamePanel.dad.LOCATION_Y+64)) == 0) {
                    System.err.println(ErrorList.ERR_RANGE_CLOSE.message);
                    return;
                }

                // Player level check
                if (gamePanel.dad.level == 1) {
                    interact(Game.houseMap[controlVariableY][controlVariableX]);
                } else {
                    if (gamePanel.dad.selectedItem > 0 && controlVariableY != 7) {

                        // Checks if the desired area is occupied or not
                        if (Game.map[controlVariableY][controlVariableX] >= 2) {
                            System.err.println(ErrorList.ERR_CANTPLANT.message);
                        } else {

                            // Creates a flower object if the area isn't being occupied
                            Game.playSound("res/Audio/Plant.wav");
                            flower = new Flower("res/Pics/" + gamePanel.dad.inventory.get(gamePanel.dad.selectedItem) + ".png", gamePanel.dad.inventory.get(gamePanel.dad.selectedItem), controlVariableX, controlVariableY, "Alive", Game.flowers.size());
                            gamePanel.dad.plant(flower);

                            // Writes it's value into map
                            Game.wirteIntoMap(controlVariableY, controlVariableX, 2);
                        }
                    } else if(gamePanel.dad.selectedItem == 0) {

                        // Stop the watering if water isn't selected or if the water is empty or is out of reach
                        if (Game.map[controlVariableY][controlVariableX] != 2) {
                            System.err.println(ErrorList.ERR_NOPLANT.message);
                        } else if (Integer.parseInt(gamePanel.dad.inventory.get(0).substring(5, 6))-1 >= 0) {
                            Game.playSound("res/Audio/WaterPlant.wav");

                            // Checks selects the plant based on clicked location
                            for (Flower plant : Game.flowers) {
                                if (plant.LOCATION_X == controlVariableX && plant.LOCATION_Y == controlVariableY) {
                                    flower = plant;
                                    break;
                                }
                            }

                            gamePanel.dad.water(flower);
                            gamePanel.dad.inventory.set(0, "water" + (Integer.parseInt(gamePanel.dad.inventory.get(0).substring(5, 6))-1));
                            gamePanel.inventoryPanel.repaintItem(gamePanel.dad);
                        }
                    }
                }
                System.gc();
                break;
            
            // This is the mouse wheel button being pressed
            case MouseEvent.BUTTON2:
                if (gamePanel.dad.level == 1) {
                    System.out.println("Interaction 2");
                } else {
                    Game.getMapData("print");

                    if (Main.debug) {
                        try {
                            Game.saveGame("src/com/vojat/Data/Saves/Save3.json", gamePanel.dad);
                        } catch (FileNotFoundException f) {
                            System.err.println(ErrorList.ERR_404.message);
                        }
                    }
                }
                break;

            case MouseEvent.BUTTON3:
                if (gamePanel.dad.level == 1) {
                    System.out.println("Interaction 3");
                } else {
                    if (Math.abs(1 - Game.intoMapX(gamePanel.dad.LOCATION_X+64)) <= gamePanel.dad.reach && Math.abs(5 - Game.intoMapY(gamePanel.dad.LOCATION_Y+64)) <= gamePanel.dad.reach) {
                        gamePanel.dad.waterRefill();
                        Game.playSound("res/Audio/WaterPour.wav");
                    } else System.err.println(ErrorList.ERR_WELL.message);
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

    private void gardenerX(MouseEvent e) {                                                                 // Center the click location into a grid place for X
        controlVariableX = Short.parseShort(Integer.toString(Game.intoMapX(e.getX())));
    }

    private void gardenerY(MouseEvent e) {                                                                 // Center the click location into a grid place for Y
        controlVariableY = Short.parseShort(Integer.toString(Game.intoMapY(e.getY())));
    }

    /*
     * ----------------------------------------------------------------
     * Interaction method for the in-house objects
     * ----------------------------------------------------------------
     */

    private void interact(int object) {
        switch(object) {
            case 4:

                // The bed interaction
                Game.playSound("res/Audio/BedSqueak.wav");
                gamePanel.dad.LOCATION_X = -10;
                gamePanel.dad.LOCATION_Y = 120;
                gamePanel.changeVisibility(gamePanel.saveMenu);
                Game.pauseGame();
                break;
            
            default:
                System.out.println("Block number |" + object + "|");
                break;
        }
    }
}
