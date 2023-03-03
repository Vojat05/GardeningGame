package com.vojat.Data;

public class JSONObject {
    public String NAME;
    public String value;

    public JSONObject(String name) {
        this.NAME = name;
    }

    public void add(char letter) {
        this.value += letter;
    }
}
