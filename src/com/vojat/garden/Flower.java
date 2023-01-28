package com.vojat.garden;

public class Flower {
    private String PICTURE_PATH;
    public String FLOWER_TYPE;
    public String STATUS;
    
    public Flower(String path, String type) {
        this.PICTURE_PATH = path;
        this.FLOWER_TYPE = type;
    }

    public Flower() {       // The base example
        this.FLOWER_TYPE = "fialka";
        this.STATUS = "alive";
        this.PICTURE_PATH = null;
    }
}
