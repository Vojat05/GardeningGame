package com.vojat.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.vojat.Enums.ErrorList;

public class JSONEditor {
    private File file;
    private ArrayList<JSONObject> JSONObjects = new ArrayList<JSONObject>();

    public JSONEditor(String filePath) throws FileNotFoundException{
        this.file = new File(filePath);

        FileReader reader = new FileReader(file);
        try {
            String value = "";
            int data = reader.read();
            while(data != -1) {             // Saves the entire JSON file as a String variable
                value += (char) data;
                data = reader.read();
            }
            reader.close();
            makeObject(value);              // Processes the Sting variable to make JSON Objects with name and content
            System.out.print("\033[H\033[2J");
            System.out.println("Object 0: " + ((JSONObject) JSONObjects.get(0)).NAME);
            System.out.println("Object 1: " + ((JSONObject) JSONObjects.get(1)).NAME);
            System.out.println("Object 0: " + ((JSONObject) JSONObjects.get(0)).getValue());
            System.out.println("Object 1: " + ((JSONObject) JSONObjects.get(1)).getValue());
        } catch (IOException e) {
            System.err.println(ErrorList.ERR_IO.message);
            e.printStackTrace();
        }
    }

    private void makeObject(String data) {
        int objectIndex = 0;
        boolean isWritingIntoObject = false;
        String name = "";
        boolean write = false;
        for(int i=0; i<data.length(); i++) {
            switch(data.charAt(i)) {
                case '}':
                    isWritingIntoObject = false;
                    break;

                case '{':
                    if (i == 0) {
                        break;
                    }
                    isWritingIntoObject = true;
                    JSONObjects.add(new JSONObject(name));
                    name = "";
                    objectIndex++;
                    break;
                
                case '"':
                    if (!isWritingIntoObject) {
                        write = write ? false : true;
                    }
                    break;

                default:
                    if (write) {
                        name += data.charAt(i);
                    }
                    break;
            }
            if (isWritingIntoObject) {
                ((JSONObject) JSONObjects.get(objectIndex-1)).add(data.charAt(i));
            }
        }
    }

    // public void write() {}

    // public String read() {}
}
