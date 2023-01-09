package com.vojat.garden;

public class Game {
    private Window gameWindow;
    private GamePanel gamePanel;

    public Game(int panelWidth, int panelHeight) {
        gamePanel = new GamePanel(panelWidth, panelHeight);
        gameWindow = new Window(gamePanel, panelWidth, panelHeight);
    }
}
