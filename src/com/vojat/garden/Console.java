package com.vojat.garden;

import java.util.ArrayList;
import java.util.HashMap;

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
    public static int commandTranslate(String command) {
        
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
        System.out.println("posX: " + commandArgs.get(0) + " | posY: " + commandArgs.get(1) + " | command: " + parsedCommand);
        return commandDecoder.get(parsedCommand);
    }

    // Gets the argument array list
    public static ArrayList<String> getArgsAsList() { return commandArgs; }
}
