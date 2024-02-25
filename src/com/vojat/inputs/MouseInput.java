package com.vojat.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileNotFoundException;

import com.vojat.Main;
import com.vojat.Data.Map;
import com.vojat.Enums.ErrorList;
import com.vojat.garden.Flower;
import com.vojat.garden.Game;
import com.vojat.garden.GamePanel;
import com.vojat.menu.MenuPanel;
import com.vojat.menu.Settings;

public class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener {

    /*
     * --------------------------------------------------------------------------------
     * Mouse input variables
     * --------------------------------------------------------------------------------
     */

    private GamePanel gamePanel;                                                        // Game panel
    private Settings settings;                                                          // Settings panel
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

    public MouseInput(Settings settings) {

        this.settings = settings;

    }

    @Override
    public void mouseClicked(MouseEvent e) {;}

    @Override
    public void mousePressed(MouseEvent e) {

        if (gamePanel == null) return;
        
        controlVariableX = gardenerX(e);
        controlVariableY = gardenerY(e);

        // Alert button interaction
        if (Game.alert || gamePanel.saveMenuOpen || gamePanel.skinMenuOpen) {

            if (gamePanel.saveMenuOpen) {
                // Save menu interaction

                if ( !(e.getX() >= 772 && e.getX() <= 1132) ) return;

                for (int i=1; i<=6; i++) {
                    
                    if (e.getY() >= 185 + i * 60 && e.getY() <= 235 + i * 60) {
                        
                        gamePanel.setSaveNumber(i);
                        Game.playSound("../../res/" + Game.texturePack + "/Audio/Button.wav");
                        break;
                    
                    }

                }
            }

            if (gamePanel.skinMenuOpen) {

                // Selecting a skin in the menu
                if ((mouseX >= 655 && mouseX <= 808) && (mouseY >= 345 && mouseY <= 497)) { gamePanel.setSelectedSkinNumber(0); }
                else if ((mouseX >= 1106 && mouseX <= 1257) && (mouseY >= 345 && mouseY <= 497)) { gamePanel.setSelectedSkinNumber(1); }
                else if ((mouseX >= 655 && mouseX <= 808) && (mouseY >= 545 && mouseY <= 697)) { gamePanel.setSelectedSkinNumber(2); }
                else if ((mouseX >= 1106 && mouseX <= 1257) && (mouseY >= 545 && mouseY <= 697)) { gamePanel.setSelectedSkinNumber(3); }

                // Reaction buttons
                if ((e.getX() >= 702 && e.getX() <= 752) && (e.getY() >= 736 && e.getY() <= 786)) {
                    
                    // The accept button
                    gamePanel.dad.setTextureModifier((char) (gamePanel.selectedSkinSlot + 48));
                    gamePanel.skinMenuOpen = false;
                    Game.pauseGame();
                    Game.playSound("../../res/" + Game.texturePack + "/Audio/Button.wav");

                } else if ((e.getX() >= 1152 && e.getX() <= 1202) && (e.getY() >= 736 && e.getY() <= 786)) {

                    // The reject button
                    gamePanel.skinMenuOpen = false;
                    Game.pauseGame();
                    Game.playSound("../../res/" + Game.texturePack + "/Audio/Button.wav");

                }

                return;

            }

            // Alert box
            if ( (e.getX() >= 802 && e.getX() <= 852) && (e.getY() >= 636 && e.getY() <= 685) ) {
                
                // Accept button

                try {
                    
                    if (Game.save != -1 && Game.alertMessage.equals("Do you want to reload your last save?")) {
                        
                        new Game(1920, 1075, Main.window);
                        Game.loadGame("../com/vojat/Data/Saves/Save" + Game.save + ".json", Game.save);
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

                        gamePanel.dad.setTextureModifier(gamePanel.dad.getTextureModifier() == '3' ? '0' : (char) (gamePanel.dad.getTextureModifier() + 1));
                        Game.pauseGame();
                        Game.alert = false;
                        gamePanel.skinMenuOpen = false;

                    } else if (Game.alertMessage.equals("")) {

                        // The save box options
                        Game.saveGame("../com/vojat/Data/Saves/Save" + gamePanel.getSaveNumber() + ".json", gamePanel.dad, (byte) gamePanel.getSaveNumber());
                        gamePanel.hideSaveMenu();
                        gamePanel.dad.LOCATION_X = GamePanel.blockWidth * 2;
                        gamePanel.dad.LOCATION_Y = 120;
                        gamePanel.dad.setMove(true);
                        Game.alertMessage = "None";
                        Game.pauseGame();

                    } else {

                        Game.alertUpdate("Save not found, return to main menu.");

                    }

                    Game.playSound("../../res/" + Game.texturePack + "/Audio/Button.wav");

                } catch (FileNotFoundException fne) {
                    
                    System.err.println("File not found");

                }
            }
            else if((e.getX() >= 1052 && e.getX() <= 1101) && (e.getY() >= 636 && e.getY() <= 685)) {
                
                // Reject button
                
                if (gamePanel.dad.HP == 0) {

                    Main.window.setElements(new MenuPanel(Main.window));
                    Game.killGame();
                    Game.alert = false;
                    Game.alertMessage = "None";
                    Game.warning = false;
                    
                } else if (gamePanel.saveMenuOpen) {

                    gamePanel.hideSaveMenu();
                    gamePanel.dad.LOCATION_X = GamePanel.blockWidth * 2;
                    gamePanel.dad.LOCATION_Y = 120;
                    gamePanel.dad.setMove(true);
                    Game.alertMessage = "None";
                    Game.pauseGame();

                } else {
                    
                    Game.pauseGame();
                    Game.alert = false;
                    Game.alertMessage = "None";
                    
                }

                Game.playSound("../../res/" + Game.texturePack + "/Audio/Button.wav");

            }

            return;

        }

        if (Game.firstStart && gamePanel.dad.level == 1) {
            // Turns off the tutorial panel

            if ( (e.getX() >= Game.tutorial.getX() + 305 && e.getX() <= Game.tutorial.getX() + 355) && (e.getY() <= 923 && e.getY() >= 875) ) {

                Game.firstStart = false;
                Game.tutorial.setVisibility(false);
                Game.playSound("../../res/" + Game.texturePack + "/Audio/Button.wav");

            }
        }

        // Pause intercation protection
        if (Game.pause || gamePanel.dad.HP == 0 || gamePanel.dad.selectedItem > gamePanel.dad.inventory.size()-1) return;

        switch (e.getButton()) {

            // LMB
            case MouseEvent.BUTTON1:

                // Player level check  |  0 == outside & 1 == inside
                if (gamePanel.dad.level == 1) {

                    interact((int) Game.houseMap.read(controlVariableX, controlVariableY) - 48);

                } else {

                    // Stop the watering if water isn't selected or if the water is empty or is out of reach
                    if (Game.map.read(controlVariableX, controlVariableY) == '4') {

                        // Distance check
                        if (Math.abs(controlVariableX - Map.translateX(gamePanel.dad.LOCATION_X+64)) > gamePanel.dad.reach || Math.abs(controlVariableY - Map.translateY(gamePanel.dad.LOCATION_Y+64)) > gamePanel.dad.reach) {
                            
                            System.err.println(ErrorList.ERR_RANGE_FAR.message);
                            Game.error("Out of reach", 3);
                            return;

                        } else if (Math.abs(controlVariableX - Map.translateX(gamePanel.dad.LOCATION_X+64)) == 0 && Math.abs(controlVariableY - Map.translateY(gamePanel.dad.LOCATION_Y+64)) == 0) {
                        
                            System.err.println(ErrorList.ERR_RANGE_CLOSE.message);
                            Game.error("Too close", 3);
                            return;
                        
                        }

                        gamePanel.dad.waterRefill();
                        Game.playSound("../../res/" + Game.texturePack + "/Audio/WaterPour.wav");
                        
                    }

                    if (gamePanel.dad.selectedItem == 1) {

                        // The tile placement
                        // Distance checks
                        if (Math.abs(controlVariableX - Map.translateX(gamePanel.dad.LOCATION_X+64)) > gamePanel.dad.reach || Math.abs(controlVariableY - Map.translateY(gamePanel.dad.LOCATION_Y+64)) > gamePanel.dad.reach) {

                            System.err.println(ErrorList.ERR_RANGE_FAR.message);
                            Game.error("Out of reach", 3);
                            return;

                        } else if (Game.map.read(controlVariableX, controlVariableY) == '4') return;
                        else if (Game.map.read(controlVariableX, controlVariableY) >= '2') {
                            
                            // Checks if the desired area is occupied or not
                            System.err.println(ErrorList.ERR_CANTPLANT.message);
                            Game.error("Area occupied", 3);
                            return;

                        } else if (controlVariableX == 2 && controlVariableY == 2) return;

                        Game.map.write(controlVariableX, controlVariableY, '6');
                        Game.playSound("../../res/" + Game.texturePack + "/Audio/Brick.wav");

                    } else if (gamePanel.dad.selectedItem > gamePanel.dad.inventory.size() - Game.flowerTypes.length - 1 && gamePanel.dad.selectedItem <= Game.flowerTypes.length + gamePanel.dad.inventory.size() - Game.flowerTypes.length - 1 && controlVariableY != 7) {

                        // Distance checks
                        if (Math.abs(controlVariableX - Map.translateX(gamePanel.dad.LOCATION_X+64)) > gamePanel.dad.reach || Math.abs(controlVariableY - Map.translateY(gamePanel.dad.LOCATION_Y+64)) > gamePanel.dad.reach) {

                            System.err.println(ErrorList.ERR_RANGE_FAR.message);
                            Game.error("Out of reach", 3);
                            return;

                        } else if (Math.abs(controlVariableX - Map.translateX(gamePanel.dad.LOCATION_X+64)) == 0 && Math.abs(controlVariableY - Map.translateY(gamePanel.dad.LOCATION_Y+80)) == 0) {
                        
                            System.err.println(ErrorList.ERR_RANGE_CLOSE.message);
                            Game.error("Too close", 3);
                            return;
                        
                        } else if ((int) Game.map.read(controlVariableX, controlVariableY) == 52) return;
                        else if ((int) Game.map.read(controlVariableX, controlVariableY) >= 50) {
                            
                            // Checks if the desired area is occupied or not
                            System.err.println(ErrorList.ERR_CANTPLANT.message);
                            Game.error("Area occupied", 3);
                            return;

                        } else if (controlVariableX == 2 && controlVariableY == 2) return;

                        // Creates a flower object if the area isn't being occupied or out of reach
                        Game.playSound("../../res/" + Game.texturePack + "/Audio/Plant.wav");
                        flower = new Flower(gamePanel.dad.inventory.get(gamePanel.dad.selectedItem), controlVariableX, controlVariableY, "Alive", Game.flowers.size());
                        gamePanel.dad.plant(flower);

                        // Writes it's value into map
                        Game.map.write(controlVariableX, controlVariableY, 2);

                    } else if(gamePanel.dad.selectedItem == 0) {

                        if (Integer.parseInt(gamePanel.dad.inventory.get(0).substring(5, 6))-1 >= 0) {

                            if (!(Game.map.read(controlVariableX, controlVariableY) == '2')) return;
                            // Distance check
                            if (Math.abs(controlVariableX - Map.translateX(gamePanel.dad.LOCATION_X+64)) > gamePanel.dad.reach || Math.abs(controlVariableY - Map.translateY(gamePanel.dad.LOCATION_Y+64)) > gamePanel.dad.reach) {

                                System.err.println(ErrorList.ERR_RANGE_FAR.message);
                                Game.error("Out of reach", 3);
                                return;

                            }

                            Game.playSound("../../res/" + Game.texturePack + "/Audio/WaterPlant.wav");

                            // Checks & selects the plant based on clicked location
                            for (Flower plant : Game.flowers) {

                                if (plant.LOCATION_X == controlVariableX && plant.LOCATION_Y == controlVariableY) {

                                    flower = plant;
                                    break;

                                }
                            }

                            gamePanel.dad.water(flower);
                            gamePanel.dad.inventory.set(0, "water" + (Integer.parseInt(gamePanel.dad.inventory.get(0).substring(5, 6))-1));

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

                    Game.map.getData("print");

                    if (Main.debug) {

                        try {

                            Game.saveGame("../com/vojat/Data/Saves/Save3.json", gamePanel.dad, (byte) 3);

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

        if (gamePanel != null) {

            if (gamePanel.skinMenuOpen) skinHoverEffect();
            else if (gamePanel.saveMenuOpen) saveHoverEffect();

            return;

        }

        if (mouseX >= 1055 && mouseX <= 1835 && mouseY >= 18 && mouseY <= 936) Settings.cursorOverControls = true;
        else Settings.cursorOverControls = false;

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        if (Settings.cursorOverControls) {

            if (e.getWheelRotation() > 0 && Settings.startIndexControlButtons < Settings.inputs.length - 6) Settings.startIndexControlButtons++;
            else if (e.getWheelRotation() < 0 && Settings.startIndexControlButtons > 0) Settings.startIndexControlButtons--;

            System.out.println("Settings index: " + Settings.startIndexControlButtons + "\nRotation: " + e.getWheelRotation());
            return;
        }

        if (gamePanel == null) return;

        if (e.getWheelRotation() > 0) {

            if (gamePanel.dad.selectedItem+1 < 10) gamePanel.dad.selectedItem++; 
            else gamePanel.dad.selectedItem = 0;

        } else {

            if (gamePanel.dad.selectedItem == 0) gamePanel.dad.selectedItem = 9;
            else gamePanel.dad.selectedItem--;

        }

        if (gamePanel.hasFocus()) gamePanel.inventoryPanel.repaint();

    }

    // Center the click location into a grid place for X
    private Short gardenerX(MouseEvent e) {

        return Short.parseShort(Integer.toString(Map.translateX(e.getX())));

    }

    // Center the click location into a grid place for Y
    private Short gardenerY(MouseEvent e) {

        return Short.parseShort(Integer.toString(Map.translateY(e.getY())));

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
                if (Math.abs(controlVariableX - Map.translateX(gamePanel.dad.LOCATION_X+64)) > gamePanel.dad.reach || Math.abs(controlVariableY - Map.translateY(gamePanel.dad.LOCATION_Y+64)) > gamePanel.dad.reach) {

                    System.err.println(ErrorList.ERR_RANGE_FAR.message);
                    Game.error("Out of reach", 3);
                    return;

                } else if (Math.abs(controlVariableX - Map.translateX(gamePanel.dad.LOCATION_X+64)) == 0 && Math.abs(controlVariableY - Map.translateY(gamePanel.dad.LOCATION_Y+64)) == 0) {

                    System.err.println(ErrorList.ERR_RANGE_CLOSE.message);
                    Game.error("Too close", 3);
                    return;

                }

                gamePanel.dad.LOCATION_X = 118;
                gamePanel.dad.LOCATION_Y = 120;
                gamePanel.dad.setMove(false);
                gamePanel.showSaveMenu();
                Game.alertMessage = "";
                Game.playSound("../../res/" + Game.texturePack + "/Audio/BedSqueak.wav");
                Game.pauseGame();
                break;
            
            case 5:

                // The closet interaction
                if (Math.abs(controlVariableX - Map.translateX(gamePanel.dad.LOCATION_X+64)) > gamePanel.dad.reach || Math.abs(controlVariableY - Map.translateY(gamePanel.dad.LOCATION_Y+64)) > gamePanel.dad.reach) {

                    System.err.println(ErrorList.ERR_RANGE_FAR.message);
                    Game.error("Out of reach", 3);
                    return;
                    
                } else if (Math.abs(controlVariableX - Map.translateX(gamePanel.dad.LOCATION_X+64)) == 0 && Math.abs(controlVariableY - Map.translateY(gamePanel.dad.LOCATION_Y+64)) == 0) {

                    System.err.println(ErrorList.ERR_RANGE_CLOSE.message);
                    Game.error("Too close", 3);
                    return;

                }

                // Game.alert("Do you want to change your clothes?");
                Game.pauseGame();
                gamePanel.skinMenuOpen = true;
                gamePanel.selectedSkinSlot = (int) gamePanel.dad.getTextureModifier() - 48;
                break;

            case 9:

                // The couch interaction
                if (Math.abs(controlVariableX - Map.translateX(gamePanel.dad.LOCATION_X+64)) > gamePanel.dad.reach || Math.abs(controlVariableY - Map.translateY(gamePanel.dad.LOCATION_Y+64)) > gamePanel.dad.reach) {

                    System.err.println(ErrorList.ERR_RANGE_FAR.message);
                    Game.error("Out of reach", 3);
                    return;
                    
                }

                Game.playSound("../../res/" + Game.texturePack + "/Audio/BedSqueak.wav");
                gamePanel.dad.isSitting = true;

                if (gamePanel.dad.LOCATION_X < 845) {

                    gamePanel.dad.LOCATION_X = 894;
                    gamePanel.dad.LOCATION_Y = 255;

                } else {

                    gamePanel.dad.LOCATION_X = 896;
                    gamePanel.dad.LOCATION_Y = 256;

                }

                gamePanel.dad.setMove(false);
                gamePanel.dad.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_Sitting.png");
                break;
            
            default:
                System.out.println("Block number |" + object + "|");
                break;
                
        }
    }

    // Highlites the save slot blocks on hover
    public void saveHoverEffect() {

        if ( !(mouseX >= 772 && mouseX <= 1132) ) { gamePanel.setHoverSaveNumber(0); return; }

        for (int i=1; i<=6; i++) {
            
            if (mouseY >= 185 + i * 60 && mouseY <= 235 + i * 60) gamePanel.setHoverSaveNumber(i);

        }

        if (mouseY < 245 || mouseY > 595) gamePanel.setHoverSaveNumber(0);

    }

    // Highlites the skin slot blocks on hover
    public void skinHoverEffect() {

        if ((mouseX >= 655 && mouseX <= 808) && (mouseY >= 345 && mouseY <= 497)) { gamePanel.setHoverSkinNumber(0); }
        else if ((mouseX >= 1106 && mouseX <= 1257) && (mouseY >= 345 && mouseY <= 497)) { gamePanel.setHoverSkinNumber(1); }
        else if ((mouseX >= 655 && mouseX <= 808) && (mouseY >= 545 && mouseY <= 697)) { gamePanel.setHoverSkinNumber(2); }
        else if ((mouseX >= 1106 && mouseX <= 1257) && (mouseY >= 545 && mouseY <= 697)) { gamePanel.setHoverSkinNumber(3); }

    }
}
