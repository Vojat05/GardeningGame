package com.vojat.Data;

public class JSONObject {
    public String NAME;
    public String VALUE = "";

    public JSONObject(String name) {
        this.NAME = name;
    }

    public void addValue(char letter) {
        this.VALUE += letter;
    }

    public String getValue() {
        return this.VALUE;
    }

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
