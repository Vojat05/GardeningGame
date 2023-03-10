package com.vojat.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.vojat.Enums.ErrorList;

public class JSONEditor {
    private File file;
    public ArrayList<JSONObject> JSONObjects = new ArrayList<JSONObject>();

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
        } catch (IOException e) {
            System.err.println(ErrorList.ERR_IO.message);
        }
    }

    private void makeObject(String data) {
        int objectIndex = 0;
        String name = "";
        boolean isWritingIntoObject = false;
        boolean write = false;
        for(int i=0; i<data.length(); i++) {
            switch(data.charAt(i)) {
                case '}':
                    ((JSONObject) JSONObjects.get(objectIndex-1)).add('}');
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

    public String read(JSONObject object, String request) {
        String key = "";
        String data = "";
        boolean writeData = false;
        boolean write = false;
        HashMap<String, String> map = new HashMap<>();

        for (int i=0; i<object.VALUE.length(); i++) {
            switch(object.VALUE.charAt(i)) {
                case '"':
                    write = write ? false : true;
                    break;

                case ':':
                    writeData = true;
                    data = "";
                    break;
                
                case ',', '}':
                    writeData = false;
                    map.put(key, data);
                    key = "";
                    write = false;
                    data = "";
                    break;

                default:
                    if (write & !writeData) {
                        key += object.VALUE.charAt(i);
                    } else if (writeData) {
                        data += object.VALUE.charAt(i);
                    }
                    break;
            }
        }
        return map.get(request);
    }

    // public String write() {}
}
