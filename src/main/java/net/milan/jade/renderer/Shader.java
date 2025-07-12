package net.milan.jade.renderer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {

    private int shaderProgramID;

    private boolean beingUsed = false;
    private String vertexSource;
    private String fragmentSource;
    private final String filepath;

    public Shader(String filepath){
        this.filepath = filepath;
        try{
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+[a-zA-Z]+");

            // Find the first pattern after "#type"
            int index = source.indexOf("#type") + 6; // 6 is the length of "#type"
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            // Find the second pattern after "#type"
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")){
                vertexSource = splitString[1];
            }
            else if (firstPattern.equals("fragment")){
                fragmentSource = splitString[2];
            }
            else{
                throw new IOException("Unexpected token after #type: " + firstPattern);
            }


            if (secondPattern.equals("vertex")){
                vertexSource = splitString[2];
            }
            else if (secondPattern.equals("fragment")){
                fragmentSource = splitString[2];
            }
            else{
                throw new IOException("Unexpected token after #type: " + firstPattern);
            }
        }
        catch (IOException exception){
            exception.printStackTrace();
            assert false : "Could not read shader file: " + filepath;
        }
    }

    public void compile(){
        // First load and compile the vertex shader
        int vertexId = glCreateShader(GL_VERTEX_SHADER);
        // Pass the source code to the GPU
        glShaderSource(vertexId, vertexSource);
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
        int fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the source code to the GPU
        glShaderSource(fragmentId, fragmentSource);
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
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexId);
        glAttachShader(shaderProgramID, fragmentId);
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int length = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: '"+ filepath + "' \n linking failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID, length));
            assert false : "";
        }
    }

    public void use(){
        // Bind shader program
        if (!beingUsed) {
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }

    public void detach(){
        // Detach shader program
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMath3f (String variableName, Matrix3f value){
        int varLocation = glGetUniformLocation(shaderProgramID, variableName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        value.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }


    public void uploadMat4f (String variableName, Matrix4f value){
        int varLocation = glGetUniformLocation(shaderProgramID, variableName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        value.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadVec2f (String variableName, Vector4f value){
        int varLocation = glGetUniformLocation(shaderProgramID, variableName);
        use();
        glUniform2f(varLocation, value.x, value.y);
    }

    public void uploadVec3f(String variableName, Vector4f value){
        int varLocation = glGetUniformLocation(shaderProgramID, variableName);
        use();
        glUniform3f(varLocation, value.x, value.y, value.z);
    }

    public void uploadVec4f(String variableName, Vector4f value){
        int varLocation = glGetUniformLocation(shaderProgramID, variableName);
        use();
        glUniform4f(varLocation, value.x, value.y, value.z, value.w);
    }

    public void uploadFloat(String variableName, float value){
        int varLocation = glGetUniformLocation(shaderProgramID, variableName);
        use();
        glUniform1f(varLocation, value);
    }

    public void uploadInt(String variableName, int value){
        int varLocation = glGetUniformLocation(shaderProgramID, variableName);
        use();
        glUniform1i(varLocation, value);
    }
}
