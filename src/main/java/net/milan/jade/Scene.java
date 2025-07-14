package net.milan.jade;

import net.milan.jade.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;
    protected Renderer renderer = new Renderer();
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene(){

    }

    public void start(){
        for (GameObject object : gameObjects) {
            object.start();
            this.renderer.add(object);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject object){
        if (!isRunning){
            gameObjects.add(object);
        }
        else{
            gameObjects.add(object);
            object.start();
            this.renderer.add(object);
        }
    }

    public Camera getCamera(){
        return this.camera;
    }

    public abstract void update(float dt);
    public abstract void init();
}
