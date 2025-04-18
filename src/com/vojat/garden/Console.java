package com.vojat.garden;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.vojat.Main;
import com.vojat.Enums.ErrorList;

public class Console {
    
    /*
     * --------------------------------------------------------------------------------
     * Console variables
     * --------------------------------------------------------------------------------
     */
    
    public static String commandPrompt = "";                                                                            // The command that is being entered to the command prompt
    public static char cursor = '|';                                                                                    // Cursor shown at the end of the command prompt
    private static boolean show = false;                                                                                // Should the console JPanel be drawn?
    private static String[] outputStack = new String[12];                                                               // The history of entered commands and their output
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

    // Gets the output stack
    public static String[] getOutput() { return outputStack; }

    // Sets the console show parametr to a specified value
    public static boolean setShow(boolean visible) { return show = visible; }

    // Switches console show parametr between the 2 possible states
    public static boolean toggleShow() { return show = show ? false : true; }

    // Is the command promt displayed
    public static boolean isVisible() { return show; }

    // Cleares the whole output stack
    public static void clearOutput() { for (int i=0; i<outputStack.length; i++) outputStack[i] = null; }

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
        boolean writeCommand = true;
        String argument = "";

        // Parsing the command into the command itself and it's arguments
        for (int i = 0; i < command.length(); i++) {

            if (command.charAt(i) == ' ') {

                if (!writeCommand) commandArgs.add(argument);
                argument = "";
                writeCommand = false;
                continue;
                
            }

            if (writeCommand) parsedCommand += command.charAt(i);
            else argument += command.charAt(i);
            
        }

        // Add the last argument and return
        commandArgs.add(argument);
        return parsedCommand;
    }

    public static void execChain() {
        if (Console.getCommandList().size() > 0) {

            ArrayList<String> cArgs = Console.getArgsAsList();

            for (int i = 0; i < Console.getCommandList().size(); i++) {
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
                        Game.render.dad.LOCATION_X = Render.blockWidth * Integer.parseInt(cArgs.get(0));
                        Game.render.dad.LOCATION_Y = Render.blockWidth * Integer.parseInt(cArgs.get(1));
                        break;
                    
                    case "TP":
                        Game.render.dad.LOCATION_X = Integer.parseInt(cArgs.get(0));
                        Game.render.dad.LOCATION_Y = Integer.parseInt(cArgs.get(1));
                        break;
                    
                        case "KILL", "SUICIDE", "DIE":
                        Game.render.dad.kill();
                        break;
                    
                    case "SETSKIN":
                        if (cArgs.size() == 0) break;
                        if (cArgs.size() > 1) break;
                        if (cArgs.get(0).charAt(0) < 48 && cArgs.get(0).charAt(0) > 51) break;
                        Game.render.dad.setTextureModifier(cArgs.get(0).charAt(0));
                        break;
                    
                    case "REVIVE":
                        Game.render.dad.setHealth((byte) 100);
                        Game.render.dad.tire((byte) -100);
                        Game.render.dad.setTexture("Player/Dad_Texture_F" + Game.render.dad.getTextureModifier() + ".png");
                        Game.warning = false;
                        Game.alert = false;
                        if (Game.render.dad.outOfStamina) {
                            
                            Game.render.dad.outOfStamina = false;
                            Game.render.dad.setTexture("Player/Dad_Texture_" + (Game.render.dad.VECTORX > 0 ? "R" : "L") + Game.render.dad.getTextureModifier() + ".png");
                            Game.render.dad.setMove(true);
                            Game.render.getKeyboardInput().resetMovement();
                            Game.render.dad.VECTORX = 0;
                            Game.render.dad.VECTORY = 0;
                        
                        }
                        break;
                    
                    case "QUIT", "EXIT":
                        Main.exit(0);
                        break;
                    
                    case "SETHP":
                        Game.render.dad.setHealth(Byte.parseByte(cArgs.get(0)));
                        break;
                    
                    case "SETSTAMINA":
                        Game.render.dad.setStamina(Byte.parseByte(cArgs.get(0)));
                        Game.render.inventoryPanel.SColor = new Color(0xfadc05);
                        break;
                    
                    case "SAVE":
                        try {
                        
                            Game.saveGame("../com/vojat/Data/Saves/Save" + cArgs.get(0) + ".json", Game.render.dad, Byte.parseByte(cArgs.get(0)));
                        
                        } catch (IOException ioe) {
                            System.err.println(ErrorList.ERR_IO.message);
                        
                        }
                        break;
                    
                    case "SUMMON":
                        if (cArgs.size() == 2) for (int j = 0; j < Integer.parseInt(cArgs.get(1)); j++) Game.summon(cArgs.get(0).toUpperCase());
                        break;
                    
                    case "ENTITY":
                        if (cArgs.size() == 0) break;
                        if (cArgs.get(0).toUpperCase().equals("CLEAR")) Game.entities.clear();
                        break;

                    case "CLEAR", "CLS":
                        clearOutput();
                        continue;
                    
                    case "SETSPEED":
                        Game.render.dad.dSpeed = Float.parseFloat(cArgs.get(0));
                        break;
                    
                    default: break;
                }
                println(command);
            }
        }
    }

    public static void println(String line) {

        // Shift all command output by 1 row
        String[] helper = new String[outputStack.length - 1];
        for (int i = 0; i < helper.length; i++) helper[i] = outputStack[i];

        // output Write through
        outputStack[0] = line;
        for (int i = 1; i < outputStack.length; i++) outputStack[i] = helper[i - 1];
    }

    // Gets the argument array list
    public static ArrayList<String> getArgsAsList() { return commandArgs; }
}
