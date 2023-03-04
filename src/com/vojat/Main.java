package com.vojat;

import com.vojat.garden.Window;
import com.vojat.menu.MenuPanel;

public class Main{
    public static void main(String[] args) {
        Window win = new Window();
        new MenuPanel(1920, 1080, win);
    }
}
