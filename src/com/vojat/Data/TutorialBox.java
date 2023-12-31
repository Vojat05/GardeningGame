package com.vojat.Data;

import java.util.ArrayList;

public class TutorialBox {
    private static int x, y;
    private static boolean visible;
    private static String rawData;
    private static ArrayList<String> lines = new ArrayList<String>();

    public TutorialBox(int x, int y, boolean visible) {
        TutorialBox.x = x;
        TutorialBox.y = y;
    }

    /**
     * Sets the tutorial box text data.
     * @param text text to be shown.
     * @return The newly set text.
     */
    public String setRawData(String text) { return rawData = text; }

    /**
     * Gets the data shown in the tutorial box as a <code>String</code>.
     * @return <code>String</code> data of the tutorial box.
     */
    public String getRawData() { return rawData; }

    /**
     * Gets the X position value of the tutorial box.
     * @return The X position value.
     */
    public int getX() { return x; }

    /**
     * Sets the X position value of the tutorial box.
     * @param x position value.
     * @return The newly set X position value.
     */
    public int setX(int x) { return TutorialBox.x = x; }

    /**
     * Gets the Y position value of the tutorial box.
     * @return The Y position value.
     */
    public int getY() { return y; }

    /**
     * Sets the Y position of the tutorial box.
     * @param y position value.
     * @return The newly set Y position value.
     */
    public int setY(int y) { return TutorialBox.y = y; }

    /**
     * Gets the visibility value of the tutorial box.
     * @return The visibility of the tutorial box.
     */
    public boolean isVisible() { return visible; }

    /**
     * Sets the visibility of the tutorial box.
     * @param value new visibility value.
     * @return The newly set visibility value.
     */
    public boolean setVisibility(boolean value) { return visible = value; }

    /**
     * Adds a line to the <code>ArrayList</code> of lines.
     * @param text <code>String</code> data of the line.
     * @return The <code>String</code> data of the last line.
     */
    public String addLine(String text) { lines.add(text); return text; }

    /**
     * Gets a line from the <code>ArrayList</code> of lines at a specified index position.
     * @param index position of the line.
     * @return A line at a specified index.
     */
    public String getLine(int index) { return lines.get(index); }

    /**
     * Gets the number of lines in the lines <code>ArrayList</code>.
     * @return Number of lines in the tutorial box.
     */
    public int getLines() { return lines.size(); }

    /**
     * Clears the tutorial box lines
     */
    public void clearLines() { lines.clear(); }
}
