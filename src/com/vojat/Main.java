package com.vojat;

import com.vojat.menu.MenuPanel;
import com.vojat.menu.Window;

public class Main{
    public static void main(String[] args) {
        Window window = new Window();
        new MenuPanel(1920, 1080, window);
    }
}
