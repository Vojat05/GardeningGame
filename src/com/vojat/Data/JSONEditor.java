package com.vojat.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.vojat.Main;
import com.vojat.Enums.ErrorList;
import com.vojat.garden.Game;

public class JSONEditor {

    /*
     * ----------------------------------------------------------------
     * JSON Editor variables
     * ----------------------------------------------------------------
     */

    private File file;                                                                          // The JSON file which it writes to & reads from
    public ArrayList<JSONObject> JSONObjects = new ArrayList<JSONObject>();                     // ArrayList of all the JSON Objects in that JSON file
    private String jsonData = "";                                                               // The currently being edited contents of the JSON file

    /*
     * ----------------------------------------------------------------
     * JSON Editor methods & its constructor
     * ----------------------------------------------------------------
     */

    // Sets the initial JSON file
    public JSONEditor(String filePath) throws FileNotFoundException {
        setFile(filePath);
    }

    // Allows the reset of the JSON file
    public void setFile(String path) {
        this.file = new File(path);
    }

    // Returns the current JSON file
    public File getFile() {
        return this.file;
    }

    // Returns the number of elements in the JSON file
    private int keyNumber() {
        int sum = 0;
        for (int i=0; i<jsonData.length(); i++) {
            if (jsonData.charAt(i) == ':') {
                sum++;
            }
        } return sum;
    }

    // Reads the JSON file and saves it into JSON data for further editing
    public void readFile() throws FileNotFoundException {
        jsonData = "";
        JSONObjects.clear();
        FileReader reader = new FileReader(file);
        try {
            int data = reader.read();
            while(data != -1) {
                jsonData += (char) data;
                data = reader.read();
            }
            reader.close();

            // Processes the Sting variable to make JSON Objects with name and content
            makeObject(jsonData);
        } catch (IOException e) {
            System.err.println(ErrorList.ERR_IO.message);
            Game.error("Input / Output Error", 3);
        }
    }

    // Goes through the entire file and creates the JSON objects, which are then saved into memory for further usage
    private void makeObject(String data) {
        int objectIndex = 0;
        String name = "";
        boolean isWritingIntoObject = false;
        boolean write = false;
        for (int i=0; i<data.length(); i++) {
            switch (data.charAt(i)) {
                case '}':

                    // Stops writing into a JSON object
                    ((JSONObject) JSONObjects.get(objectIndex-1)).addValue('}');
                    isWritingIntoObject = false;
                    break;

                case '{':

                    // Starts writing into a JSON object
                    if (i == 0) {
                        break;
                    }
                    isWritingIntoObject = true;
                    JSONObjects.add(new JSONObject(name));
                    name = "";
                    objectIndex++;
                    break;
                
                case '"':

                    // if isn't writing into an object it changes the write permission
                    if (!isWritingIntoObject) {
                        write = write ? false : true;
                    }
                    break;

                default:

                    // If it has write permission, it writes the name of the object
                    if (write) {
                        name += data.charAt(i);
                    }
                    break;
            }

            // Writes adds the data to the JSON object
            if (isWritingIntoObject) {
                ((JSONObject) JSONObjects.get(objectIndex-1)).addValue(data.charAt(i));
            }
        }
    }


    // Goes through the JSON object and stores every key and value into a HashMap for an easier access and returns the value of the request
    public String readData(JSONObject object, String request) {
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

    // Returns the JSON file as a 2D String array
    public String[][] read2DArr() {
        try {
            {
                jsonData = "";
                JSONObjects.clear();
                FileReader reader = new FileReader(file);
                int data = reader.read();
                while(data != -1) {
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
            Game.error("File not found", 3);
            return null;
        }
    }

    // Rewrites the entire JSON file
    public void write(String data) {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write("{" + data + "}");
            fw.close();
        } catch (IOException e) {
            System.err.println(ErrorList.ERR_404.message);
            Game.error("File not found", 3);
        }
    }

    // Writes passed data under a certian key, the key has to be present
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
        if (Main.debug) {
            System.out.println(map.size() + " | " + mapKeys.size());
        }
        
        // This is for the controls
        value = "{\"game\":{\"" + mapKeys.get(0) + "\":\"" + map.get(mapKeys.get(0)) + "\", \"" + mapKeys.get(1) + "\":\"" + map.get(mapKeys.get(1)) + "\"},\"movement\":{\"" + mapKeys.get(2) + "\":\"" + map.get(mapKeys.get(2)) + "\",\"" + mapKeys.get(3) + "\":\"" + map.get(mapKeys.get(3)) + "\",\"" + mapKeys.get(4) + "\":\"" + map.get(mapKeys.get(4)) + "\",\"" + mapKeys.get(5) + "\":\"" + map.get(mapKeys.get(5)) + "\"},\"inventory\":{\"" + mapKeys.get(6) + "\":\"" + map.get(mapKeys.get(6)) + "\",\"" + mapKeys.get(7) + "\":\"" + map.get(mapKeys.get(7)) + "\",\"" + mapKeys.get(8) + "\":\"" + map.get(mapKeys.get(8)) + "\"}}";
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            fw.write(value);
            fw.close();
            readFile();
        } catch (IOException e) {
            System.err.println(ErrorList.ERR_IO.message);
            Game.error("Input / Output Error", 3);
        }
    }

    // Creates a new file
    public static File createFile(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
        return file;
    }
}
