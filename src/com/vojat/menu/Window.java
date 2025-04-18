package com.vojat.menu;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.lwjgl.glfw.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import com.vojat.Main;
import com.vojat.garden.Game;
import com.vojat.garden.MainPanel;
import com.vojat.garden.Player;

public class Window extends JFrame {

    /*
     * --------------------------------------------------------------------------------
     * Window variables
     * --------------------------------------------------------------------------------
     */

    public static int width, height;                                                        // The device screen width and height in pixels
    private static long handle;                                                             // OpenGL GLFW window handle
    public static String title = "Dad The Gardener";                                        // Window title
    private MainPanel mainPanel;                                                            // The main menu panel
    private Player dad;                                                                     // The player character
    private ArrayList<JComponent> components = new ArrayList<JComponent>();                 // Arraylist of all of it's components

    /*
     * --------------------------------------------------------------------------------
     * Window constructor to setup the window & window change methods
     * --------------------------------------------------------------------------------
     */

    public Window(int screenWidth, int screenHeight) {
        Window.width = screenWidth;
        Window.height = screenHeight;
        
        if (width == Main.sizeX && height == Main.sizeY && Main.fullscreen) {
            setUndecorated(true);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        setTitle("Dad The Gardener");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("../../res/" + Game.texturePack + "/Pics/Icons/tulip.png").getImage());

        pack();
        setSize(new Dimension(Main.width, Main.height));

        setResizable(false);
    }

    public static void glfw_init(int width, int height) {
        // Setup the error callback stream, every error will be printed using the System.err PrintStream
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) throw new IllegalStateException("Failed to initialize the GLFW window");

        // Configure GLFW window
        glfwDefaultWindowHints(); // Optional, the current is already default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // The window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // The window is resizable

        // Create a GLFW window
        handle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (handle == NULL) throw new RuntimeException("Failed to create a GLFW window");

        // Setup a key callback, it will be called every time a key is pressed, repeated or released
        glfwSetKeyCallback(handle, (handle, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(handle, true); // Detected in the rendering loop
        });

        // The stack frame is popped automatically
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get window size passed to glfwCreateWindow
            glfwGetWindowSize(handle, pWidth, pHeight);

            // Get the resolution of main monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(handle, (int) ((vidmode.width() - pWidth.get(0)) * .5), (int) ((vidmode.height() - pHeight.get(0)) * .5));
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(handle);

        // Enable V-Synch
        glfwSwapInterval(1);

        // GL flags
        //glEnable(GL_CULL_FACE);
        //glEnable(GL_BACK);

        // Make the window visible
        glfwShowWindow(handle);
    }

    public static void glfw_maximize() {
        glfwMaximizeWindow(handle);
    }

    public static void glfw_destroy() {
        glfwDestroyWindow(handle);
        glfwTerminate();
    }

    public static void update() {
        // Clear the frame buffer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Swap the color buffers
        glfwSwapBuffers(handle);

        // Poll for window events, the above key callback will only be invoked during this call
        glfwPollEvents();
    }

    public static boolean keyPressed(int keycode) {
        return glfwGetKey(handle, keycode) == GLFW_PRESS;
    }

    public static boolean windowShouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public static void glfwSetTitle(String title) {
        Window.title = title;
        glfwSetWindowTitle(handle, title);
    }


    /*
     * --------------------------------------------------------------------------------
     * JFrame methods
     * --------------------------------------------------------------------------------
     */
    public void setElements(JComponent arg) {
        for (JComponent component : components) {
            remove(component);
        }
        components.clear();

        add(arg);
        components.add(arg);
        setVisible(true);
    }

    // dad & mainPanel are always null !!
    @Override
    public void validate() {
        super.validate();
        if (dad != null & mainPanel != null) {
            mainPanel.setPanelSize(getSize().width, getSize().height);
            dad.setLimit(getSize().width, getSize().height);

            if (dad.LOCATION_X > Player.windowLimitX-120) {
                dad.LOCATION_X = Player.windowLimitX-130;
            }

            if (dad.LOCATION_Y > Player.windowLimitY-120) {
                dad.LOCATION_Y = Player.windowLimitY-150;
            }
        }
    }

    /**
     * Recursive function to calculate the screen resolution to match a specified ratio.
     * @param width The starting screen width input.
     * @param height The starting screen height input.
     * @param ratioX From <code>X : Y</code> the value of <code>X</code>.
     * @param ratioY From <code>X : Y</code> the value of <code>Y</code>.
     * @return An integer array containing <code>{ width, height }</code>.
     */
    public static int[] calculateResolution(int width, int height, int ratioX, int ratioY) {
        // Fail break condition
        if (width == 0 || height == 0) System.exit(1);

        // Second break condition
        if (width % ratioX == 0 && height % ratioY == 0) {
            int[] resolution = new int[2];
            resolution[0] = width;
            resolution[1] = height;
            return resolution;
        }

        return calculateResolution(width % ratioX == 0 ? width : (width - 1), height % ratioY == 0 ? height : (height - 1), ratioX, ratioY);
    }
}
