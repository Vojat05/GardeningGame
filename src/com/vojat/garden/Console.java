package com.vojat.garden;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.vojat.Enums.ErrorList;

public class Console {
    
    /*
     * --------------------------------------------------------------------------------
     * Console variables
     * --------------------------------------------------------------------------------
     */
    
    public static String commandPrompt = "";                                                                            // The command that is being entered to the command prompt
    private static boolean show = false;                                                                                // Should the console JPanel be drawn?
    private static ArrayList<String> commandList = new ArrayList<>();                                                   // The comand queue
    private static ArrayList<String> commandArgs = new ArrayList<>();                                                   // The command arguments for the command that's to be executed
    private static HashMap<String, Integer> commandDecoder = new HashMap<String, Integer>();                            // Look-up table for decoding the instructions

    /*
     * --------------------------------------------------------------------------------
     * Console methods
     * --------------------------------------------------------------------------------
     */

    public Console() {

        // Builds the command decoder
        commandDecoder.put("tp", 0);
    }

    // Gets the command array list
    public static ArrayList<String> getCommandList() { return commandList; }

    // Sets the console show parametr to a specified value
    public static boolean setShow(boolean visible) { return show = visible; }

    // Switches console show parametr between the 2 possible states
    public static boolean toggleShow() { return show = show ? false : true; }

    public static boolean isVisible() { return show; }

    // Pops the firt command in the array list, working like a queue
    public static String commandPop() {

        // Returns if the command list doesn't contain any command to pop
        if (commandList.size() == 0) return "";

        String command = commandList.get(0);
        commandList.remove(0);
        return command;

    }

    // Adds a command to the end of the queue
    public static String addCommand(String command) {
        
        commandList.add(command);
        commandPrompt = "";
        return command;

    }

    // Translates the command and creates it's arguments
    public static String getCommand(String command) {
        
        commandArgs.clear();
        String parsedCommand = "";
        int i = 0;
        boolean writeCommand = true;
        String argument = "";

        while (true) {

            // Parsing the command into the command itself and it's arguments
            if (command.charAt(i) == ';') {
                commandArgs.add(argument);
                argument = "";
                break;
            }

            if (command.charAt(i) == ' ') {

                if (!writeCommand) commandArgs.add(argument);
                argument = "";
                writeCommand = false;
                i++;
                continue;
                
            }

            if (writeCommand) parsedCommand += command.charAt(i++);
            else argument += command.charAt(i++);
            
        }
        return parsedCommand;
    }

    public static void execChain() {
        if (Console.getCommandList().size() > 0) {

                ArrayList<String> cArgs = Console.getArgsAsList();
    
                for (int i=0; i<Console.getCommandList().size(); i++) {

                    String command = Console.commandPop();
                    switch (Console.getCommand(command).toUpperCase()) {
    
                        // Teleport
                        case "TPA":
                            if (Integer.parseInt(cArgs.get(0)) > Game.map.getColumns()) {

                                System.err.println(Game.ANSI_RED + ErrorList.ERR_X_TOO_FAR.message + Game.ANSI_RESET);
                                break;

                            } else if (Integer.parseInt(cArgs.get(1)) > Game.map.getRows()) {

                                System.err.println(Game.ANSI_RED + ErrorList.ERR_Y_TOO_FAR.message + Game.ANSI_RESET);
                                break;

                            }
                            Game.gamePanel.dad.LOCATION_X = GamePanel.blockWidth * Integer.parseInt(cArgs.get(0));
                            Game.gamePanel.dad.LOCATION_Y = GamePanel.blockWidth * Integer.parseInt(cArgs.get(1));
                            break;

                        case "TP":
                            Game.gamePanel.dad.LOCATION_X = Integer.parseInt(cArgs.get(0));
                            Game.gamePanel.dad.LOCATION_Y = Integer.parseInt(cArgs.get(1));
                            break;

                        case "KILL", "SUICIDE", "DIE":
                            Game.gamePanel.dad.kill();
                            break;
                        
                        case "SETSKIN":
                            if (cArgs.size() == 0) break;
                            if (cArgs.size() > 1) break;
                            if (cArgs.get(0).charAt(0) < 48 && cArgs.get(0).charAt(0) > 51) break;
                            Game.gamePanel.dad.setTextureModifier(cArgs.get(0).charAt(0));
                            break;

                        case "REVIVE":
                            Game.gamePanel.dad.setHealth(100);
                            Game.gamePanel.dad.tire(-100);
                            Game.gamePanel.dad.setTexture("Player/Dad_Texture_F" + Game.gamePanel.dad.getTextureModifier() + ".png");
                            Game.warning = false;
                            Game.alert = false;
                            if (Game.gamePanel.dad.outOfStamina) {
                                
                                Game.gamePanel.dad.outOfStamina = false;
                                Game.gamePanel.dad.setTexture("Player/Dad_Texture_" + (Game.gamePanel.dad.VECTORX > 0 ? "R" : "L") + Game.gamePanel.dad.getTextureModifier() + ".png");
                                Game.gamePanel.dad.setMove(true);
                                Game.gamePanel.getKeyboardInput().resetMovement();
                                Game.gamePanel.dad.VECTORX = 0;
                                Game.gamePanel.dad.VECTORY = 0;
                            
                            }
                            break;

                        case "QUIT":
                            System.exit(0);
                            break;
                        
                        case "SETHP":
                            Game.gamePanel.dad.setHealth(Integer.parseInt(cArgs.get(0)));
                            break;
                        
                        case "SETSTAMINA":
                            Game.gamePanel.dad.setStamina(Integer.parseInt(cArgs.get(0)));
                            break;

                        case "SAVE":
                            try {
                            
                                Game.saveGame("../com/vojat/Data/Saves/Save" + cArgs.get(0) + ".json", Game.gamePanel.dad, Byte.parseByte(cArgs.get(0)));
                            
                            } catch (IOException ioe) {

                                System.err.println(ErrorList.ERR_IO.message);
                            
                            }
                            break;

                        case "SUMMON":
                            if (cArgs.get(0).toUpperCase().equals("BIRD")) {

                                if (cArgs.size() == 2) for (int j=0; j<Integer.parseInt(cArgs.get(1)); j++) Game.spawnBird();
                                else Game.spawnBird();

                            }
                            break;

                        case "ENTITY":
                            if (cArgs.size() == 0) break;
                            if (cArgs.get(0).toUpperCase().equals("CLEAR")) Game.birdList.clear();
                            break;
                            
                        default:
                            break;
                    }
                }
            }
    }

    // Gets the argument array list
    public static ArrayList<String> getArgsAsList() { return commandArgs; }
}
