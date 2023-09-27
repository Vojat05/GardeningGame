package com.vojat.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;

import com.vojat.Main;
import com.vojat.Enums.ErrorList;
import com.vojat.garden.Flower;
import com.vojat.garden.Game;
import com.vojat.garden.GamePanel;
import com.vojat.menu.MenuPanel;

public class MouseInput implements MouseListener, MouseMotionListener {

    /*
     * --------------------------------------------------------------------------------
     * Mouse input variables
     * --------------------------------------------------------------------------------
     */

    private GamePanel gamePanel;                                                        // Game panel
    private short controlVariableX;                                                     // Theoretical mouse X coordinate in the game map 
    private short controlVariableY;                                                     // Theoretical mouse Y coordiante in the game map
    private Flower flower;                                                              // Flower object that's set on each click on some flower
    private int mouseX = 0;                                                             // X variable used for the hover effect in the save menu
    private int mouseY = 0;                                                             // Y variable used for the hover effect in the save menu

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
        if (Game.alert || gamePanel.saveMenuOpen) {

            if (gamePanel.saveMenuOpen) {

                if ( !(e.getX() >= 772 && e.getX() <= 1132) ) return;

                for (int i=1; i<=6; i++) {
                    
                    if (e.getY() >= 185 + i * 60 && e.getY() <= 235 + i * 60) {
                        
                        gamePanel.setSaveNumber(i);
                        Game.playSound("res/" + Game.texturePack + "/Audio/Button.wav");
                        break;
                    
                    }

                }
            }

            if ( (e.getX() >= 802 && e.getX() <= 852) && (e.getY() >= 636 && e.getY() <= 685) ) {
                
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

                    } else if (Game.alertMessage.equals("Do you want to change your clothes?")) {

                        gamePanel.dad.setTextureModifier(gamePanel.dad.getTextureModifier() == '0' ? '1' : '0');
                        Game.pauseGame();
                        Game.alert = false;

                    } else if (Game.alertMessage.equals("")) {

                        // The save box options
                        Game.saveGame("src/com/vojat/Data/Saves/Save" + gamePanel.getSaveNumber() + ".json", gamePanel.dad, (byte) gamePanel.getSaveNumber());
                        gamePanel.hideSaveMenu();
                        gamePanel.dad.LOCATION_X = 208;
                        gamePanel.dad.LOCATION_Y = 120;
                        gamePanel.dad.setMove(true);
                        Game.alertMessage = "None";
                        Game.pauseGame();

                    } else {

                        Game.alertUpdate("Save not found, return to main menu.", gamePanel);

                    }

                    Game.playSound("res/" + Game.texturePack + "/Audio/Button.wav");

                } catch (FileNotFoundException fne) {
                    
                    System.err.println("File not found");

                }
            }
            else if((e.getX() >= 1052 && e.getX() <= 1101) && (e.getY() >= 636 && e.getY() <= 685)) {
                
                // Reject button
                
                if (gamePanel.dad.HP == 0) {

                    Main.window.setElements(new MenuPanel(Main.window));
                    Game.alert = false;
                    Game.alertMessage = "None";
                    Game.warning = false;
                    
                } else if (gamePanel.saveMenuOpen) {

                    gamePanel.hideSaveMenu();
                    gamePanel.dad.LOCATION_X = 208;
                    gamePanel.dad.LOCATION_Y = 120;
                    gamePanel.dad.setMove(true);
                    Game.alertMessage = "None";
                    Game.pauseGame();

                } else {
                    
                    Game.pauseGame();
                    Game.alert = false;
                    Game.alertMessage = "None";
                    
                }

                Game.playSound("res/" + Game.texturePack + "/Audio/Button.wav");

            }

            return;

        }

        if (Game.firstStart) {
            // Should show the tutorial

            if ( (e.getX() >= 1550 && e.getX() <= 1600) && (e.getY() <=923 && e.getY() >= 875) ) {

                Game.firstStart = false;

            }
        }

        // Pause intercation protection
        if (Game.pause || gamePanel.dad.HP == 0) return;

        switch (e.getButton()) {

            // LMB
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
                            Game.playSound("res/" + Game.texturePack + "/Audio/Plant.wav");
                            flower = new Flower(gamePanel.dad.inventory.get(gamePanel.dad.selectedItem), controlVariableX, controlVariableY, "Alive", Game.flowers.size());
                            gamePanel.dad.plant(flower);

                            // Writes it's value into map
                            Game.wirteIntoMap(controlVariableY, controlVariableX, 2);

                        }

                    } else if(gamePanel.dad.selectedItem == 0) {

                        // Stop the watering if water isn't selected or if the water is empty or is out of reach
                        
                        if ((int) Game.map[controlVariableY][controlVariableX] == '4') {

                            gamePanel.dad.waterRefill();
                            Game.playSound("res/" + Game.texturePack + "/Audio/WaterPour.wav");
                            
                        } else if ((int) Game.map[controlVariableY][controlVariableX] != 50) {

                            System.err.println(ErrorList.ERR_NOPLANT.message);
                            Game.error("There isn't a plant", 3);

                        } else if (Integer.parseInt(gamePanel.dad.inventory.get(0).substring(5, 6))-1 >= 0) {

                            Game.playSound("res/" + Game.texturePack + "/Audio/WaterPlant.wav");

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

    @Override
    public void mouseDragged(MouseEvent e) {;}

    @Override
    public void mouseMoved(MouseEvent e) {

        mouseX = e.getX();
        mouseY = e.getY();

    }

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
                gamePanel.dad.LOCATION_X = 118;
                gamePanel.dad.LOCATION_Y = 120;
                gamePanel.dad.setMove(false);
                gamePanel.showSaveMenu();
                Game.alertMessage = "";
                Game.playSound("res/" + Game.texturePack + "/Audio/BedSqueak.wav");
                Game.pauseGame();
                break;
            
            case 5:

                // The closet interaction
                Game.alert("Do you want to change your clothes?", gamePanel);
                Game.pauseGame();
                break;

            case 9:

                // The couch interaction
                Game.playSound("res/" + Game.texturePack + "/Audio/BedSqueak.wav");
                System.out.println(controlVariableX*128 + " | " + controlVariableY*128);

                if (gamePanel.dad.LOCATION_X < 845) {

                    gamePanel.dad.LOCATION_X = 894;
                    gamePanel.dad.LOCATION_Y = 255;

                } else {

                    gamePanel.dad.LOCATION_X = 896;
                    gamePanel.dad.LOCATION_Y = 256;

                }

                gamePanel.dad.setMove(false);
                gamePanel.dad.currentTexture = Game.setTexture("res/" + Game.texturePack + "/Pics/Player/Dad_Texture_Sitting.png");
                break;
            
            default:
                System.out.println("Block number |" + object + "|");
                break;
                
        }
    }

    // Highlites the save slot blocks on hover
    public void hoverEffect() {

        if ( !(mouseX >= 772 && mouseX <= 1132) ) { gamePanel.setHoverSaveNumber(0); return; }

        for (int i=1; i<=6; i++) {
            
            if (mouseY >= 185 + i * 60 && mouseY <= 235 + i * 60) gamePanel.setHoverSaveNumber(i);

        }

        if (mouseY < 245 || mouseY > 595) gamePanel.setHoverSaveNumber(0);

    }
}
