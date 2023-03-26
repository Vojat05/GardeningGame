package com.vojat.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.vojat.Enums.ErrorList;

public class JSONEditor {
    private File file;
    public ArrayList<JSONObject> JSONObjects = new ArrayList<JSONObject>();
    private String jsonData = "";

    public JSONEditor(String filePath) throws FileNotFoundException {
        setFile(filePath);
    }

    public void setFile(String path) {
        this.file = new File(path);
    }

    public File getFile() {
        return this.file;
    }

    private int keyNumber() {
        int sum = 0;
        for (int i=0; i<jsonData.length(); i++) {
            if (jsonData.charAt(i) == ':') {
                sum++;
            }
        } return sum;
    }

    public void readFile() throws FileNotFoundException {
        jsonData = "";
        JSONObjects.clear();
        FileReader reader = new FileReader(file);
        try {
            int data = reader.read();
            while(data != -1) {             // Saves the entire JSON file as a String variable
                jsonData += (char) data;
                data = reader.read();
            }
            reader.close();
            makeObject(jsonData);              // Processes the Sting variable to make JSON Objects with name and content
        } catch (IOException e) {
            System.err.println(ErrorList.ERR_IO.message);
        }
    }

    private void makeObject(String data) {      // Goes through the entire file and creates the objects, which are then saved into memory for further usage
        int objectIndex = 0;
        String name = "";
        boolean isWritingIntoObject = false;
        boolean write = false;
        for (int i=0; i<data.length(); i++) {
            switch (data.charAt(i)) {
                case '}':       // Stops writing into a JSON object
                    ((JSONObject) JSONObjects.get(objectIndex-1)).addValue('}');
                    isWritingIntoObject = false;
                    break;

                case '{':       // Starts writing into a JSON object
                    if (i == 0) {
                        break;
                    }
                    isWritingIntoObject = true;
                    JSONObjects.add(new JSONObject(name));
                    name = "";
                    objectIndex++;
                    break;
                
                case '"':       // if isn't writing into an object it changes the write permission
                    if (!isWritingIntoObject) {
                        write = write ? false : true;
                    }
                    break;

                default:        // If it has write permission, it writes the name of the object
                    if (write) {
                        name += data.charAt(i);
                    }
                    break;
            }
            if (isWritingIntoObject) {      // Writes adds the data to the JSON object
                ((JSONObject) JSONObjects.get(objectIndex-1)).addValue(data.charAt(i));
            }
        }
    }

    public String readData(JSONObject object, String request) {         // Goes through the JSON object and stores every key and value into a HashMap for an easier access
        String key = "";
        String data = "";
        boolean writeData = false;
        boolean write = false;
        HashMap<String, String> map = new HashMap<>();

        for (int i=0; i<object.VALUE.length(); i++) {
            switch (object.VALUE.charAt(i)) {
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
                    if (write && !writeData) {
                        key += object.VALUE.charAt(i);
                    } else if (writeData) {
                        data += object.VALUE.charAt(i);
                    }
                    break;
            }
        }
        if (map.containsKey(request)) {
            return map.get(request);
        } else {
            return ErrorList.ERR_404.message;
        }
    }

    public String[][] read2DArr() {
        try {
            {
                jsonData = "";
                JSONObjects.clear();
                FileReader reader = new FileReader(file);
                int data = reader.read();
                while(data != -1) {             // Saves the entire JSON file as a String variable
                    jsonData += (char) data;
                    data = reader.read();
                }
                reader.close();
            }
            String[][] map = new String[keyNumber()][2];
            for (int i=0; i<map.length; i++) {
                for (int j=0; j<map[0].length; j++) {
                    map[i][j] = "";
                }
            }
            boolean write = false;
            int num = 0;
            for (int i=0; i<jsonData.length(); i++) {
                switch (jsonData.charAt(i)) {
                    case '{', '}', '"':
                        break;

                    case ':', ',':
                        if (write) {
                            num++;
                        }
                        write = write ? false : true;
                        break;

                    default:
                        map[num][write ? 1 : 0] += jsonData.charAt(i);
                }
            }
            return map;
        } catch (IOException fe) {
            System.err.println(ErrorList.ERR_404.message);
            return null;
        }
    }

    // Writes a whole new content to a JSON file
    public void write(String data) {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write("{" + data + "}");
            fw.close();
        } catch (IOException e) {
            System.err.println(ErrorList.ERR_404.message);
        }
    }

    // Writes a passed data under a certian key, the key has to be present
    public void change(String key, String data) {
        String name = "";
        String value = "";
        boolean writeName = false;
        boolean writeValue = false;
        HashMap<String, String> map = new HashMap<>();
        ArrayList<String> mapKeys = new ArrayList<>();

        for (int i=0; i<jsonData.length(); i++) {
            switch (jsonData.charAt(i)) {
                case '"':
                    writeName = writeName ? false : true;
                    break;
                
                case '{':
                    name = "";
                    writeValue = false;
                    break;

                case ':':
                    writeValue = writeValue ? false : true;
                    break;
                
                case ',', '}':
                    if (name != "" || value != "") {
                        map.put(name, value);
                        mapKeys.add(name);
                    }
                    writeValue = false;
                    name = "";
                    value = "";
                    break;
                
                default:
                    if (writeValue && writeName) {
                        value += jsonData.charAt(i);
                    } else if (writeName) {
                        name += jsonData.charAt(i);
                    }
                    break;
            }
        }
        map.put(key, data);
        value = "{\"game\":{\"" + mapKeys.get(0) + "\":\"" + map.get(mapKeys.get(0)) + "\"},\"movement\":{\"" + mapKeys.get(1) + "\":\"" + map.get(mapKeys.get(1)) + "\",\"" + mapKeys.get(2) + "\":\"" + map.get(mapKeys.get(2)) + "\",\"" + mapKeys.get(3) + "\":\"" + map.get(mapKeys.get(3)) + "\",\"" + mapKeys.get(4) + "\":\"" + map.get(mapKeys.get(4)) + "\"},\"inventory\":{\"" + mapKeys.get(5) + "\":\"" + map.get(mapKeys.get(5)) + "\",\"" + mapKeys.get(6) + "\":\"" + map.get(mapKeys.get(6)) + "\",\"" + mapKeys.get(7) + "\":\"" + map.get(mapKeys.get(7)) + "\"}}";
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            fw.write(value);
            fw.close();
            readFile();
        } catch (IOException e) {
            System.err.println(ErrorList.ERR_IO.message);
        }
    }

    public static File createFile(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
        return file;
    }
}
