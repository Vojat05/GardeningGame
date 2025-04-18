package com.vojat.Data;

import java.util.ArrayList;

import com.vojat.Rendering.Render;

public class Map {
    private char[][] map;

    public Map(char[][] map) { this.map = map; }

    /**
     * Clears a 2D character map.
     * @param map to be cleared.
     * 
     */
    public static void clear(char[][] map) {

        for (int i=0; i<map.length; i++) {

            for (int j=0; j<map[0].length; j++) {

                map[i][j] = '0';

            }
        }

    }

    /**
     * Cleares a 2D int map.
     * @param map to be cleared.
     * 
     */
    public static void clear(int[][] map) {

        for (int i=0; i<map.length; i++) {

            for (int j=0; j<map[0].length; j++) {

                map[i][j] = 0;

            }
        }

    }

    /**
     * Gets the map.
     * @return This map.
     * 
     */
    public char[][] getMap() { return this.map; }

    /**
     * Writes data into map.
     * @param x int.
     * @param y int.
     * @param value int that will be translated to a char by (char) + 48.
     * @return The char writen into the map.
     * 
     */
    public char write(int x, int y, int value) { return this.map[y][x] = (char) (48 + value); }

    /**
     * Writes data into map.
     * @param x int.
     * @param y int.
     * @param value char data to be writen.
     * @return The char writen into the map.
     * 
     */
    public char write(int x, int y, char value) { return this.map[y][x] = value; }

    /**
     * Reads the data stored in this 2D array.
     * @param x int.
     * @param y int.
     * @return The value stored at the coordinates.
     * 
     */
    public char read(int x, int y) { return this.map[y][x]; }

    /**
     * Gets the date from the map as a string.
     * @param type string "print" or none.
     * @return If type == "print" the map data will be formated and displayed into console and an empty string will be returned.
     * 
     */
    public String getData(String type) {

        if (type.equals("print")) {

            for (int i=0; i<map.length; i++) {

                for (int j=0; j<map[0].length; j++) System.out.print(" | " + map[i][j] + " | ");
                System.out.println("");

            }
            return "";
        } else {

            String value = "";

            for (int i=0; i<map.length; i++) {

                for (int j=0; j<map[0].length; j++) value += map[i][j];
                value += "!";

            }
            return value;
        }
    }

    /**
     * Compiles a list of every different character that is present in the map.
     * @return An <code>ArrayList</code> of different characters present in the map.
     * 
     */
    public ArrayList<Character> getValues() {

        ArrayList<Character> values = new ArrayList<Character>();

        for (int i=0; i<map.length; i++) {

            for (int j=0; j<map[0].length; j++) {

                if (!values.contains(map[i][j])) { values.add(map[i][j]); }
            }
        }
        return values;
    }

    /**
     * Gets the number of columns that is located in the Map. The number of positions in the X direction.
     * @return Number of columns in the Map.
     */
    public int getColumns() { return map[0].length; }


    /**
     * Gets the number of rows that is located in the Map. The number of positions in the Y direction.
     * @return Number of rows in the Map.
     */
    public int getRows() { return map.length; }

    /**
     * Gets the theoretical X location in the map
     * @param posX
     * @return Integer value of a horizontal position translated into the map
     * 
     */
    public static int translateX(double posX) { return (int) (posX * Math.pow(Render.blockWidth, -1)); /* PosX / block width */ }

    /**
     * Gets the theoretical Y location in the map
     * @param posY
     * @return Integer value of a vertical position translated into the map
     * 
     */
    public static int translateY(double posY) { return (int) (posY * Math.pow(Render.blockWidth, -1)); /* PosY / block width */ }

    /**
     * Gets the object in located in the map at the specific location
     * @param x position
     * @param y position
     * @param map to search in
     * @return The value stored in a specified location in the given map
     * 
     */
    public static int translate(int x, int y, Map map) { return (int) map.read(x, y) - 48; }
}
