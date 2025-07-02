package net.milan.jade;

public class LevelScene extends Scene{

    public LevelScene(){
        System.out.println("Level Scene");
        Window.get().r = 1.0f;
        Window.get().g = 0.0f;
        Window.get().b = 0.0f;
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void init() {

    }
}
