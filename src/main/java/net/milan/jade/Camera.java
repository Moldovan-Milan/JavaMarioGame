package net.milan.jade;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    private Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;

    public Camera(Vector2f position){
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection(){
        projectionMatrix.identity();
        projectionMatrix.ortho(0.f, 32.f * 40.f, 0.f, 32.f * 21.f,
                0.f, 100.f);
    }

    public Matrix4f getViewMatrix(){
        Vector3f cameraFront = new Vector3f(0.f, 0.f, -1.f);
        Vector3f cameraUp = new Vector3f(0.f, 1.f, 0.f);
        viewMatrix.identity();
        viewMatrix = viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.f),
                cameraFront.add(position.x, position.y, 0.f),
                cameraUp);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
}
