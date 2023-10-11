package com.vojat.Data;

public class JSONObject {

    /*
     * ----------------------------------------------------------------
     * JSON Object data
     * ----------------------------------------------------------------
     */

    public String NAME;                         // The name of the JSON list group
    public String VALUE = "";                   // The JSON list value

    /*
     * ----------------------------------------------------------------
     * JSON Object methods
     * ----------------------------------------------------------------
     */

    public JSONObject(String name) {
        
        this.NAME = name;
        
    }

    // Adds to the current JSON objects value
    public void addValue(char letter) {
        
        this.VALUE += letter;
        
    }

    // Returns the current JSON object value
    public String getValue() {

        return this.VALUE;

    }

    // Returns the number of elements in the JSON object value list
    public int getNumberOfValues() {

        int number = 0;

        for (int i=0; i<VALUE.length(); i++) {

            if (VALUE.charAt(i) == ':') number++;

        }

        return number;

    }

    // Returns a 2D array of keys and values
    public String[][] getData() {

        String[][] data = new String[getNumberOfValues()+1][2];

        String rawData = getValue();
        String key = "";
        String value = "";
        boolean writeKey = true;
        boolean write = false;

        for (int i=0, x=0; i<getValue().length(); i++) {

            char letter = rawData.charAt(i);

            switch (letter) {

                case ':':
                    writeKey = write ? writeKey : false;
                    break;

                case '"':
                    write = write ? false : true;
                    break;

                case ',', '}':
                    data[x][0] = key;
                    data[x][1] = value;
                    x++;
                    key = "";
                    value = "";
                    writeKey = true;
                    break;

                default:
                    if (writeKey && write) key += letter;
                    else if (write) value += letter;
                    break;
            }
        }

        return data;

    }
}
