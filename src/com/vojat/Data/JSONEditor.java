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
            int index = 0;
            int objectIndex = 0;
            boolean isWritingIntoObject = false;
            String name = "";
            int data = reader.read();
            boolean write = false;
            while(data != -1) {
                switch((char) data) {
                    case '}':
                        isWritingIntoObject = false;
                        break;

                    case '{':
                        if (index == 0) {
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
                            name += (char) data;
                        }
                        break;
                }
                if (isWritingIntoObject) {
                    ((JSONObject) JSONObjects.get(objectIndex-1)).add((char) data);
                }
                index++;
                data = reader.read();
            }
            reader.close();
            System.out.print("\033[H\033[2J");
            System.out.println("Object 0: " + ((JSONObject) JSONObjects.get(0)).NAME);
            System.out.println("Object 1: " + ((JSONObject) JSONObjects.get(1)).NAME);
        } catch (IOException e) {
            System.err.println(ErrorList.ERR_IO.message);
            e.printStackTrace();
        }
    }

    // public void put() {}

    // public String get() {}
}
