package com.vojat.inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;

import com.vojat.Main;
import com.vojat.Data.Map;
import com.vojat.Enums.ErrorList;
import com.vojat.garden.Flower;
import com.vojat.garden.Game;
import com.vojat.garden.Render;
import com.vojat.menu.MenuPanel;
import com.vojat.menu.Settings;

public class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener {

    /*
     * --------------------------------------------------------------------------------
     * Mouse input variables
     * --------------------------------------------------------------------------------
     */

    private Render render;                                                              // Render object
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

    public MouseInput(Render render) {

        this.render = render;

    }

    @Override
    public void mouseClicked(MouseEvent e) {;}

    @Override
    public void mousePressed(MouseEvent e) {

        if (render == null) return;
        
        controlVariableX = gardenerX(e);
        controlVariableY = gardenerY(e);

        // Alert button interaction
        if (Game.alert || render.saveMenuOpen || render.skinMenuOpen) {

            int middleX = (int) (render.getWidth() * 0.5);
            int middleY = (int) (render.getHeight() * 0.5);
            int y = (int) (middleY - middleY * 0.5);

            if (render.saveMenuOpen) {

                // Save menu interaction
                if (!(e.getX() >= middleX - 180 && e.getX() <= middleX + 180)) return;

                for (int i=1; i<=6; i++) {
                    
                    if (e.getY() >= y - 55 + 60 * i && e.getY() <= y - 5 + 60 * i) {
                        
                        render.setSaveNumber(i);
                        Game.playSound("../../res/" + Game.texturePack + "/Audio/Button.wav");
                        break;
                    
                    }
                }
            }

            if (render.skinMenuOpen) {

                for (int i = 0; i < 4; i++) {

                    int posX = (int) (middleX + (i % 2 == 0 ? 300 : 150) * Math.pow(-1, i + 1));
                    int posY = (int) (middleY - middleY * 0.5) + (i < 2 ? 100 : 300);

                    if (mouseX >= posX && mouseX <= posX + 160 && mouseY >= posY && mouseY <= posY + 160) render.setSelectedSkinNumber(i);
                }

                // Buttons
                if ((e.getX() >= middleX - 270 && e.getX() <= middleX - 220) && (e.getY() >= y + 495 && e.getY() <= y + 545)) {
                    
                    // The accept button
                    render.dad.setTextureModifier((char) (render.selectedSkinSlot + 48));
                    render.skinMenuOpen = false;
                    Game.togglePauseGame();
                    Game.playSound("../../res/" + Game.texturePack + "/Audio/Button.wav");

                } else if ((e.getX() >= middleX + 200 && e.getX() <= middleX + 250) && (e.getY() >= y + 495 && e.getY() <= y + 545)) {

                    // The reject button
                    render.skinMenuOpen = false;
                    Game.togglePauseGame();
                    Game.playSound("../../res/" + Game.texturePack + "/Audio/Button.wav");

                }
                return;
            }

            // Alert box
            if ( (e.getX() >= middleX - 150 && e.getX() <= middleX - 100) && (e.getY() >= y + 395 && e.getY() <= y + 445) ) {
                
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

                        render.dad.setTextureModifier(render.dad.getTextureModifier() == '3' ? '0' : (char) (render.dad.getTextureModifier() + 1));
                        Game.togglePauseGame();
                        Game.alert = false;
                        render.skinMenuOpen = false;

                    } else if (Game.alertMessage.equals("")) {

                        // The save box options
                        Game.saveGame("../com/vojat/Data/Saves/Save" + render.getSaveNumber() + ".json", render.dad, (byte) render.getSaveNumber());
                        render.hideSaveMenu();
                        render.dad.LOCATION_X = Render.blockWidth * 2 - 10;
                        render.dad.LOCATION_Y = Render.blockWidth;
                        render.dad.setMove(true);
                        Game.alertMessage = "None";
                        Game.togglePauseGame();

                    } else Game.alertUpdate("Save not found, return to main menu.");

                    Game.playSound("../../res/" + Game.texturePack + "/Audio/Button.wav");

                } catch (IOException fne) { System.err.println("File not found"); }
            } else if((e.getX() >= middleX + 100 && e.getX() <= middleX + 150) && (e.getY() >= y + 395 && e.getY() <= y + 445)) {
                
                // Reject button
                if (render.dad.HP == 0) {

                    Main.window.setElements(new MenuPanel(Main.window));
                    Game.killGame();
                    Game.alert = false;
                    Game.alertMessage = "None";
                    Game.warning = false;
                    
                } else if (render.saveMenuOpen) {

                    render.hideSaveMenu();
                    render.dad.LOCATION_X = Render.blockWidth * 2 - 10;
                    render.dad.LOCATION_Y = Render.blockWidth;
                    render.dad.setMove(true);
                    Game.alertMessage = "None";
                    Game.togglePauseGame();

                } else {
                    
                    Game.togglePauseGame();
                    Game.alert = false;
                    Game.alertMessage = "None";
                    
                }

                Game.playSound("../../res/" + Game.texturePack + "/Audio/Button.wav");

            }
            return;
        }

        if (Game.firstStart && (render.dad.reachLevel & 0xf) == 1) {
            
            // Turns off the tutorial panel
            if ( (e.getX() >= Game.tutorial.getX() + 305 && e.getX() <= Game.tutorial.getX() + 355) && (e.getY() <= Game.tutorial.getY() + 875 && e.getY() >= Game.tutorial.getY() + 825) ) {

                Game.firstStart = false;
                Game.tutorial.setVisibility(false);
                Game.playSound("../../res/" + Game.texturePack + "/Audio/Button.wav");

            }
        }

        // Pause intercation protection
        if (Game.pause || render.dad.HP == 0 || render.dad.selectedItem > render.dad.inventory.size()-1) return;

        switch (e.getButton()) {

            // LMB
            case MouseEvent.BUTTON1:

                // Player level check  |  0 == outside & 1 == inside
                if ((render.dad.reachLevel & 0xf) == 1) {

                    interact((int) Game.houseMap.read(controlVariableX, controlVariableY) - 48);

                } else {

                    // Stop the watering if water isn't selected or if the water is empty or is out of reach
                    if (Game.map.read(controlVariableX, controlVariableY) == '4') {

                        // Distance check
                        if (Math.abs(controlVariableX - Map.translateX(render.dad.LOCATION_X+64)) > ((render.dad.reachLevel & 0xf0) >> 4) || Math.abs(controlVariableY - Map.translateY(render.dad.LOCATION_Y+64)) > ((render.dad.reachLevel & 0xf0) >> 4)) {
                            
                            System.err.println(ErrorList.ERR_RANGE_FAR.message);
                            Game.error("Out of reach", 3);
                            return;

                        } else if (Math.abs(controlVariableX - Map.translateX(render.dad.LOCATION_X+64)) == 0 && Math.abs(controlVariableY - Map.translateY(render.dad.LOCATION_Y+64)) == 0) {
                        
                            System.err.println(ErrorList.ERR_RANGE_CLOSE.message);
                            Game.error("Too close", 3);
                            return;
                        
                        }

                        render.dad.waterRefill();
                        Game.playSound("../../res/" + Game.texturePack + "/Audio/WaterPour.wav");
                        
                    }

                    switch (render.dad.selectedItem) {
                        case 0:
                            if (Integer.parseInt(render.dad.inventory.get(0).substring(5, 6))-1 >= 0) {

                                if (!(Game.map.read(controlVariableX, controlVariableY) == '2')) return;
                                // Distance check
                                if (Math.abs(controlVariableX - Map.translateX(render.dad.LOCATION_X+64)) > ((render.dad.reachLevel & 0xf0) >> 4) || Math.abs(controlVariableY - Map.translateY(render.dad.LOCATION_Y+64)) > ((render.dad.reachLevel & 0xf0) >> 4)) {

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

                                render.dad.water(flower);
                                render.dad.inventory.set(0, "water" + (Integer.parseInt(render.dad.inventory.get(0).substring(5, 6))-1));

                            } else {

                                System.err.println(ErrorList.ERR_WATER.message);
                                Game.error("Out of Water", 3);

                            }
                            break;
                            
                        case 1:
                            // The tile placement
                            // Distance checks
                            if (Math.abs(controlVariableX - Map.translateX(render.dad.LOCATION_X+64)) > ((render.dad.reachLevel & 0xf0) >> 4) || Math.abs(controlVariableY - Map.translateY(render.dad.LOCATION_Y+64)) > ((render.dad.reachLevel & 0xf0) >> 4)) {

                                System.err.println(ErrorList.ERR_RANGE_FAR.message);
                                Game.error("Out of reach", 3);
                                return;

                            } else if (Game.map.read(controlVariableX, controlVariableY) == '4') return;
                            else if (Game.map.read(controlVariableX, controlVariableY) >= '2') {

                                // Checks if the desired area is occupied or not
                                System.err.println(ErrorList.ERR_CANTPLANT.message);
                                Game.error("Area occupied", 3);
                                return;

                            }

                            Game.map.write(controlVariableX, controlVariableY, '6');
                            Game.playSound("../../res/" + Game.texturePack + "/Audio/Brick.wav");
                            break;

                        case 3:
                            // Distance checks
                            if (Math.abs(controlVariableX - Map.translateX(render.dad.LOCATION_X+64)) > ((render.dad.reachLevel & 0xf0) >> 4) || Math.abs(controlVariableY - Map.translateY(render.dad.LOCATION_Y+64)) > ((render.dad.reachLevel & 0xf0) >> 4)) {

                                System.err.println(ErrorList.ERR_RANGE_FAR.message);
                                Game.error("Out of reach", 3);
                                return;

                            }

                            // Removing tiles
                            if (Game.map.read(controlVariableX, controlVariableY) == '6') {

                                Game.map.write(controlVariableX, controlVariableY, '0');
                                Game.playSound("../../res/" + Game.texturePack + "/Audio/Shovel.wav");
                                return;

                            }

                            // Select the flower to be removed
                            Flower flower = null;
                            for (int i=0; i<Game.flowers.size(); i++) {

                                Flower plant = Game.flowers.get(i);
                                if (plant.LOCATION_X == controlVariableX && plant.LOCATION_Y == controlVariableY) { flower = plant; break; }
                            
                            }

                            // Remove selected flower if it exists
                            if (flower == null) return;
                            Game.flowers.remove(flower);
                            Game.map.write(flower.LOCATION_X, flower.LOCATION_Y, '0');
                            Game.playSound("../../res/" + Game.texturePack + "/Audio/Shovel.wav");
                            break;

                        case 4:
                            if (Game.map.read(controlVariableX, controlVariableY) != '2') {

                                render.infoFlower = null;
                                break;

                            }
                            // The magnifying glass
                            for (int i = 0; i<Game.flowers.size(); i++) {

                                flower = Game.flowers.get(i);
                                if (flower.LOCATION_X == controlVariableX && flower.LOCATION_Y == controlVariableY) {

                                    render.infoFlower = flower;
                                    break;

                                }
                            }
                            break;

                        default:
                            break;
                    }
                    
                    if (render.dad.selectedItem > render.dad.inventory.size() - Game.flowerTypes.length - 1 && render.dad.selectedItem <= Game.flowerTypes.length + render.dad.inventory.size() - Game.flowerTypes.length - 1 && controlVariableY != 7) {

                        // Distance checks
                        if (Math.abs(controlVariableX - Map.translateX(render.dad.LOCATION_X+64)) > ((render.dad.reachLevel & 0xf0) >> 4) || Math.abs(controlVariableY - Map.translateY(render.dad.LOCATION_Y+64)) > ((render.dad.reachLevel & 0xf0) >> 4)) {

                            System.err.println(ErrorList.ERR_RANGE_FAR.message);
                            Game.error("Out of reach", 3);
                            return;

                        } else if (Math.abs(controlVariableX - Map.translateX(render.dad.LOCATION_X+64)) == 0 && Math.abs(controlVariableY - Map.translateY(render.dad.LOCATION_Y+80)) == 0) {
                        
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
                        flower = new Flower(render.dad.inventory.get(render.dad.selectedItem), controlVariableX, controlVariableY, "Alive", Game.flowers.size());
                        render.dad.plant(flower);

                        // Writes it's value into map
                        Game.map.write(controlVariableX, controlVariableY, '2');

                    }
                }

                System.gc();
                break;
            
            // This is the mouse wheel button being pressed
            case MouseEvent.BUTTON2:
            
                render.dad.hurt(10);
                if ((render.dad.reachLevel & 0xf) == 1) System.out.println("Interaction 2"); 
                else {

                    Game.map.getData("print");

                    if (Main.debug) {

                        try {

                            Game.saveGame("../com/vojat/Data/Saves/Save3.json", render.dad, (byte) 3);

                        } catch (IOException f) {

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

        if (render != null) {

            if (render.skinMenuOpen) skinHoverEffect();
            else if (render.saveMenuOpen) saveHoverEffect();

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

        if (render == null) return;

        if (e.getWheelRotation() > 0) {

            if (render.dad.selectedItem+1 < 10) render.dad.selectedItem++; 
            else render.dad.selectedItem = 0;

        } else {

            if (render.dad.selectedItem == 0) render.dad.selectedItem = 9;
            else render.dad.selectedItem--;

        }

        if (render.hasFocus()) render.inventoryPanel.repaint();

    }

    // Center the click location into a grid place for X
    private Short gardenerX(MouseEvent e) {

        return Short.parseShort("" + Map.translateX(e.getX()));

    }

    // Center the click location into a grid place for Y
    private Short gardenerY(MouseEvent e) {

        return Short.parseShort("" + Map.translateY(e.getY()));

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
                if (Math.abs(controlVariableX - Map.translateX(render.dad.LOCATION_X+64)) > ((render.dad.reachLevel & 0xf0) >> 4) || Math.abs(controlVariableY - Map.translateY(render.dad.LOCATION_Y+64)) > ((render.dad.reachLevel & 0xf0) >> 4)) {

                    System.err.println(ErrorList.ERR_RANGE_FAR.message);
                    Game.error("Out of reach", 3);
                    return;

                } else if (Math.abs(controlVariableX - Map.translateX(render.dad.LOCATION_X+64)) == 0 && Math.abs(controlVariableY - Map.translateY(render.dad.LOCATION_Y+64)) == 0) {

                    System.err.println(ErrorList.ERR_RANGE_CLOSE.message);
                    Game.error("Too close", 3);
                    return;

                }

                render.dad.LOCATION_X = 118;
                render.dad.LOCATION_Y = 120;
                render.dad.setMove(false);
                render.showSaveMenu();
                Game.alertMessage = "";
                Game.playSound("../../res/" + Game.texturePack + "/Audio/BedSqueak.wav");
                Game.togglePauseGame();
                break;
            
            case 5:

                // The closet interaction
                if (Math.abs(controlVariableX - Map.translateX(render.dad.LOCATION_X+64)) > ((render.dad.reachLevel & 0xf0) >> 4) || Math.abs(controlVariableY - Map.translateY(render.dad.LOCATION_Y+64)) > ((render.dad.reachLevel & 0xf0) >> 4)) {

                    System.err.println(ErrorList.ERR_RANGE_FAR.message);
                    Game.error("Out of reach", 3);
                    return;
                    
                } else if (Math.abs(controlVariableX - Map.translateX(render.dad.LOCATION_X+64)) == 0 && Math.abs(controlVariableY - Map.translateY(render.dad.LOCATION_Y+64)) == 0) {

                    System.err.println(ErrorList.ERR_RANGE_CLOSE.message);
                    Game.error("Too close", 3);
                    return;

                }

                // Game.alert("Do you want to change your clothes?");
                Game.togglePauseGame();
                render.skinMenuOpen = true;
                render.selectedSkinSlot = (int) render.dad.getTextureModifier() - 48;
                break;

            case 9:

                // The couch interaction
                if (Math.abs(controlVariableX - Map.translateX(render.dad.LOCATION_X+64)) > ((render.dad.reachLevel & 0xf0) >> 4) || Math.abs(controlVariableY - Map.translateY(render.dad.LOCATION_Y+64)) > ((render.dad.reachLevel & 0xf0) >> 4)) {

                    System.err.println(ErrorList.ERR_RANGE_FAR.message);
                    Game.error("Out of reach", 3);
                    return;
                    
                }

                Game.playSound("../../res/" + Game.texturePack + "/Audio/BedSqueak.wav");
                render.dad.isSitting = true;

                if (render.dad.LOCATION_X < 845) {

                    render.dad.LOCATION_X = 894;
                    render.dad.LOCATION_Y = 255;

                } else {

                    render.dad.LOCATION_X = 896;
                    render.dad.LOCATION_Y = 256;

                }

                render.dad.setMove(false);
                render.dad.currentTexture = Game.setTexture("../../res/" + Game.texturePack + "/Pics/Player/Dad_Texture_Sitting.png");
                break;
            
            default:
                System.out.println("Block number |" + object + "|");
                break;
                
        }
    }

    // Highlites the save slot blocks on hover
    public void saveHoverEffect() {

        int middleX = (int) (render.getWidth() * 0.5);
        int middleY = (int) (render.getHeight() * 0.5);
        int y = (int) (middleY - middleY * 0.5);

        if ( !(mouseX >= middleX - 180 && mouseX <= middleX + 180) ) { render.setHoverSaveNumber(0); return; }
        if (mouseY < y + 4 || mouseY > y + 355) { render.setHoverSaveNumber(0); return; }

        for (int i=1; i<=6; i++) if (mouseY >= y - 55 + 60 * i && mouseY <= y - 5 + i * 60) render.setHoverSaveNumber(i);
    }

    // Highlites the skin slot blocks on hover
    public void skinHoverEffect() {

        if ((mouseX >= 655 && mouseX <= 808) && (mouseY >= 345 && mouseY <= 497)) { render.setHoverSkinNumber(0); }
        else if ((mouseX >= 1106 && mouseX <= 1257) && (mouseY >= 345 && mouseY <= 497)) { render.setHoverSkinNumber(1); }
        else if ((mouseX >= 655 && mouseX <= 808) && (mouseY >= 545 && mouseY <= 697)) { render.setHoverSkinNumber(2); }
        else if ((mouseX >= 1106 && mouseX <= 1257) && (mouseY >= 545 && mouseY <= 697)) { render.setHoverSkinNumber(3); }

    }
}
