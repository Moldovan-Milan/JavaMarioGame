package net.milan.jade.renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private String filepath;
    private final int textureId;


    public Texture(String filepath){
        this.filepath = filepath;

        // Generate texture on the GPU
        textureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Set texture parameters
        // Repeat the image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // When sctretching the image, pixalate it
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // When shrinking the image, pixalate it
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Load the image data
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if (image != null){
            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0),
                        height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            }
            else if (channels.get(0) == 4){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0),
                        height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            }
        }
        else{
            assert false : "Failed to load texture file: " + filepath;
        }

        stbi_image_free(image);
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
