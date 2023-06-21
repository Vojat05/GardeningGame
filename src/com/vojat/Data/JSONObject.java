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
            if (VALUE.charAt(i) == ':') {
                number++;
            }
        }
        return number;
    }
}
