package com.vojat.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;

import com.vojat.Main;
import com.vojat.Enums.ErrorList;
import com.vojat.garden.Flower;
import com.vojat.garden.Game;
import com.vojat.garden.GamePanel;
import com.vojat.menu.MenuPanel;

public class MouseInput implements MouseListener {

    /*
     * --------------------------------------------------------------------------------
     * Mouse input variables
     * --------------------------------------------------------------------------------
     */

    private GamePanel gamePanel;                                                        // Game panel
    private short controlVariableX;                                                     // Theoretical mouse X coordinate in the game map 
    private short controlVariableY;                                                     // Theoretical mouse Y coordiante in the game map
    private Flower flower;                                                              // Flower object that's set on each click on some flower

    /*
     * --------------------------------------------------------------------------------
     * In-game mouse click interactions
     * --------------------------------------------------------------------------------
     */

    public MouseInput(GamePanel gamePanel) {

        this.gamePanel = gamePanel;

    }

    @Override
    public void mouseClicked(MouseEvent e) {

        controlVariableX = gardenerX(e);
        controlVariableY = gardenerY(e);

        // Alert button interaction
        if (Game.alert) {

            if ((e.getX() >= 802 && e.getX() <= 852) && (e.getY() >= 636 && e.getY() <= 685)) {
                // Accept button

                try {
                    
                    if (Game.save != -1 && Game.alertMessage.equals("Do you want to reload your last save?")) {
                        
                        new Game(1920, 1075, Main.window);
                        Game.loadGame("src/com/vojat/Data/Saves/Save" + Game.save + ".json", Game.save);
                        Game.alert = false;
                        Game.warning = false;
                    
                    } else if (Game.alertMessage.equals("Save not found, return to main menu.")) {

                        Main.window.setElements(new MenuPanel(Main.window));
                        Game.alert = false;
                        Game.warning = false;

                    } else if (Game.alertMessage.equals("Are you sure you want to quit?")) {

                        Game.killGame();
                        Main.window.setElements(new MenuPanel(Main.window));
                        Game.alert = false;
                        Game.pause = false;

                    } else {

                        Game.alertUpdate("Save not found, return to main menu.", gamePanel);

                    }
                    Game.playSound("res/Audio/Button.wav");

                } catch (FileNotFoundException fne) {
                    
                    System.err.println("File not found");

                }

                return;
            
            }
            else if((e.getX() >= 1052 && e.getX() <= 1101) && (e.getY() >= 636 && e.getY() <= 685)) {
                // Reject button
                
                if (gamePanel.dad.HP == 0) {

                    Main.window.setElements(new MenuPanel(Main.window));
                    Game.alert = false;
                    Game.warning = false;
                    
                } else if (Game.alertMessage.equals("Are you sure you want to quit?")) {
                    
                    Game.pauseGame();
                    Game.alert = false;
                    
                }
                
                Game.playSound("res/Audio/Button.wav");
                return;
            
            }

            return;

        }

        // Pause intercation protection
        if (Game.pause || gamePanel.dad.HP == 0) return;

        switch (e.getButton()) {

            case MouseEvent.BUTTON1:

                // Too far and close checks
                if (Math.abs(controlVariableX - Game.intoMapX(gamePanel.dad.LOCATION_X+64)) > gamePanel.dad.reach || Math.abs(controlVariableY - Game.intoMapY(gamePanel.dad.LOCATION_Y+64)) > gamePanel.dad.reach) {

                    System.err.println(ErrorList.ERR_RANGE_FAR.message);
                    Game.error("Out of reach", 3);
                    return;

                } else if (Math.abs(controlVariableX - Game.intoMapX(gamePanel.dad.LOCATION_X+64)) == 0 && Math.abs(controlVariableY - Game.intoMapY(gamePanel.dad.LOCATION_Y+64)) == 0) {

                    System.err.println(ErrorList.ERR_RANGE_CLOSE.message);
                    Game.error("Too close", 3);
                    return;

                }

                // Player level check  |  0 == outside & 1 == inside
                if (gamePanel.dad.level == 1) {

                    interact((int) Game.houseMap[controlVariableY][controlVariableX] - 48);

                } else {

                    if ((gamePanel.dad.selectedItem > 0 && gamePanel.dad.selectedItem <= Game.flowerTypes.length) && controlVariableY != 7) {

                        // Checks if the desired area is occupied or not
                        if ((int) Game.map[controlVariableY][controlVariableX] >= 50) {

                            System.err.println(ErrorList.ERR_CANTPLANT.message);
                            Game.error("Area occupied", 3);

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
                        
                        if ((int) Game.map[controlVariableY][controlVariableX] == '4') {

                            gamePanel.dad.waterRefill();
                            Game.playSound("res/Audio/WaterPour.wav");
                            
                        } else if ((int) Game.map[controlVariableY][controlVariableX] != 50) {

                            System.err.println(ErrorList.ERR_NOPLANT.message);
                            Game.error("There isn't a plant", 3);

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

                        } else {

                            System.err.println(ErrorList.ERR_WATER.message);
                            Game.error("Out of Water", 3);

                        }
                    }
                }

                System.gc();
                break;
            
            // This is the mouse wheel button being pressed
            case MouseEvent.BUTTON2:
            
            gamePanel.dad.hurt(10);
            if (gamePanel.dad.level == 1) System.out.println("Interaction 2"); 
                else {

                    Game.getMapData("print");

                    if (Main.debug) {

                        try {

                            Game.saveGame("src/com/vojat/Data/Saves/Save3.json", gamePanel.dad, (byte) 3);

                        } catch (FileNotFoundException f) {

                            System.err.println(ErrorList.ERR_404.message);
                            Game.error("File not found", 3);

                        }
                    }
                }

                break;

            case MouseEvent.BUTTON3:

                if (gamePanel.dad.level == 1) System.out.println("Interaction 3");
                else {

                    if (Math.abs(1 - Game.intoMapX(gamePanel.dad.LOCATION_X+64)) <= gamePanel.dad.reach && Math.abs(5 - Game.intoMapY(gamePanel.dad.LOCATION_Y+64)) <= gamePanel.dad.reach) {

                        gamePanel.dad.waterRefill();
                        Game.playSound("res/Audio/WaterPour.wav");

                    } else {

                        System.err.println(ErrorList.ERR_WELL.message);
                        Game.error("The well is too far", 3);

                    }
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

    // Center the click location into a grid place for X
    private Short gardenerX(MouseEvent e) {

        return Short.parseShort(Integer.toString(Game.intoMapX(e.getX())));

    }

    // Center the click location into a grid place for Y
    private Short gardenerY(MouseEvent e) {

        return Short.parseShort(Integer.toString(Game.intoMapY(e.getY())));

    }

    /*
     * --------------------------------------------------------------------------------
     * Interaction method for the in-house objects
     * --------------------------------------------------------------------------------
     */

    private void interact(int object) {

        switch(object) {

            case 4:

                // The bed interaction
                Game.playSound("res/Audio/BedSqueak.wav");
                gamePanel.dad.LOCATION_X = 118;
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
