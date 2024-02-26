package com.vojat.garden;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

public class Console extends JPanel {
    
    /*
     * --------------------------------------------------------------------------------
     * Console variables
     * --------------------------------------------------------------------------------
     */
    
    private ArrayList<String> commandList = new ArrayList<>();
    private HashMap<String, Byte> commandDecoder = new HashMap<String, Byte>();

    /*
     * --------------------------------------------------------------------------------
     * Console methods
     * --------------------------------------------------------------------------------
     */

    public Console() {

        // Builds the command decoder
        commandDecoder.put("tp", (byte) 0);
    }

    // Gets the command array list
    public ArrayList<String> getCommandList() { return this.commandList; }

    // Pops the firt command in the array list, working like a queue
    public String commandPop() {

        // Returns if the command list doesn't contain any command to pop
        if (commandList.size() == 0) return "";

        String command = this.commandList.get(0);
        commandList.remove(0);
        return command;

    }

    // Adds a command to the end of the queue
    public String addCommand(String command) {
        
        commandList.add(command);
        return command;

    }

    public byte commandTranslate(String command) {
        
        String parsedCommand = "";
        int i = 0;

        while (true) {

            // Break condition if it runs into a space
            if (command.charAt(i) == 0x20) break;
            parsedCommand += command.charAt(i++);
            
        }
        return commandDecoder.get(parsedCommand);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

    }
}
