package net.milan.jade;


import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{

    private String vertexShaderSource = "#version 330 core\n" +
            "layout(location = 0) in vec3 aPos;\n" +
            "layout(location = 1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0f);\n" +
            "}";

    private String fragmentShaderSource = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexId, fragmentId, shaderProgramId;

    private float[] vertexArray = {
            // Positions         // Colors
             0.5f, -0.5f, 0.0f,   1.0f, 0.0f, 0.0f, 1.0f, // Bottom Right 0
            -0.5f, 0.5f, 0.0f,    0.0f, 1.0f, 0.0f, 1.0f, // Top Left     1
             0.5f, 0.5f, 0.0f,    0.0f, 0.0f, 1.0f, 1.0f, // Top Right    2
            -0.5f, -0.5f, 0.0f,   1.0f, 1.0f, 0.0f, 1.0f  // Bottom Left  3
    };

    // Clock wise order
    private int[] elementArray = {
            2, 1, 0, // Top right triangle
            0, 1, 3  // Bottom left triangle
    };

    public LevelEditorScene(){

    }

    private int vaoId, vboId, eboId;

    @Override
    public void init() {
        // Compile shaders and link them

        // First load and compile the vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        // Pass the source code to the GPU
        glShaderSource(vertexId, vertexShaderSource);
        glCompileShader(vertexId);

        // Check for compilation errors
        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.out.println("Error: defaultShader.glsl \n compilation failed");
            System.out.println(glGetShaderInfoLog(vertexId, length));
            assert false : "";
        }

        // Second load and compile the fragment shader
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the source code to the GPU
        glShaderSource(fragmentId, fragmentShaderSource);
        glCompileShader(fragmentId);

        // Check for compilation errors
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.out.println("Error: defaultShader.glsl \n compilation failed");
            System.out.println(glGetShaderInfoLog(fragmentId, length));
            assert false : "";
        }

        // Create the shader program
        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexId);
        glAttachShader(shaderProgramId, fragmentId);
        glLinkProgram(shaderProgramId);

        // Check for linking errors
        success = glGetProgrami(shaderProgramId, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int length = glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH);
            System.out.println("Error: defaultShader.glsl \n linking failed");
            System.out.println(glGetProgramInfoLog(shaderProgramId, length));
            assert false : "";
        }

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
        // Bind shader program
        glUseProgram(shaderProgramId);
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
        glUseProgram(0);
    }
}
