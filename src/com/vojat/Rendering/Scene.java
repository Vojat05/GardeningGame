package com.vojat.Rendering;

import java.util.ArrayList;

public class Scene {
    public static ArrayList<String> textures = new ArrayList<String>();

    public Scene(int scene) {
        switch (scene) {
            case 0:
                textures.add("Game_Logo");
                break;
        
            default: break;
        }
    }
}
