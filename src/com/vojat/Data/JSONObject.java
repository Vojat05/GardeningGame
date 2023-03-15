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
}
