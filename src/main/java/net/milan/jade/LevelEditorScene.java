package net.milan.jade;


import net.milan.jade.renderer.Shader;
import net.milan.jade.util.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{

    private final float[] vertexArray = {
            // Positions         // Colors
             100.5f, 0.5f,   0.0f,   1.0f, 0.0f, 0.0f, 1.0f, // Bottom Right 0
             0.5f,   100.5f, 0.0f,   0.0f, 1.0f, 0.0f, 1.0f, // Top Left     1
             100.5f, 100.5f, 0.0f,   0.0f, 0.0f, 1.0f, 1.0f, // Top Right    2
             0.5f,   0.5f,   0.0f,   1.0f, 1.0f, 0.0f, 1.0f  // Bottom Left  3
    };

    // Clock wise order
    private final int[] elementArray = {
            2, 1, 0, // Top right triangle
            0, 1, 3  // Bottom left triangle
    };

    Shader defaultShader;

    public LevelEditorScene(){
        defaultShader = new Shader("assets/shaders/default.glsl");
    }

    private int vaoId, vboId, eboId;

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        // Compile the shaders
        defaultShader.compile();

        // Generate VAO, VBO, EBO buffers and send them to the GPU
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create float buffer for vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO and upload vertex buffer data
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create indices buffer for elements
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attributes pointers
        int positionSize = 3; // x, y, z
        int colorSize = 4; // r, g, b, a
        int floatSizeInBytes = 4;
        int vertexByteSize = (positionSize + colorSize) * floatSizeInBytes;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexByteSize, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexByteSize, positionSize * floatSizeInBytes);
        glEnableVertexAttribArray(1);

    }

    @Override
    public void update(float dt) {
        camera.position.x -= dt * 50.f;

        // Bind shader program
        defaultShader.use();

        // Upload the view and projection matrices to the shader
        defaultShader.uploadMat4f("uProjectionMatrix", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uViewMatrix", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        // Bind the VAO that we are using
        glBindVertexArray(vaoId);

        // Enable vertex attributes pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        defaultShader.detach();
    }
}
