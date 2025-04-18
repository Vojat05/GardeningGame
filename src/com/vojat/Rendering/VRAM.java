package com.vojat.Rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL2.nvgDelete;

public class VRAM {

    /** Use only on PNG textures Loads texture to VRAM */
    public static void loadTexture(String path, String name) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            Render.glfw_textures.put(name, loadTextureFromIMG(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Used for PNG pictures only, loads PNG texture to VRAM */
    private static int loadTextureFromIMG(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
                buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green
                buffer.put((byte) (pixel & 0xFF));         // Blue
                buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
            }
        }

        buffer.flip();

        int texId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texId);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        return texId;
    }

    /** Frees texture from VRAM and removes it from Render texture hashmap */
    public static void freeTexture(String textureID) {
        // Free texture from VRAM
        glDeleteTextures(Render.glfw_textures.get(textureID));

        // Delete texture from Render scope
        Render.glfw_textures.remove(textureID);
    }

    /** Loads TTF font to VRAM */
    public static void loadFont(String name, String path) {
        int fontID = nvgCreateFont(NanoVGContext.vg, name, path);
        if (fontID == -1) throw new RuntimeException("Could not load font at " + path);
        Render.glfw_fonts.add(name);
    }

    /** Frees all fonts from VRAM and cleares the Render font arraylist */
    public static void freeAllFonts() {
        // Free font from VRAM
        nvgDelete(NanoVGContext.vg);

        // Delete font from Render scope
        Render.glfw_fonts.clear();
    }
}
