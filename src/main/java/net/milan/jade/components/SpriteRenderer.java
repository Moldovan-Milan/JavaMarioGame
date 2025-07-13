package net.milan.jade.components;

import net.milan.jade.Component;

public class SpriteRenderer extends Component {

    private boolean firstTime = true;

    @Override
    public void start(){
        System.out.println("I am starting ...");
    }

    @Override
    public void update(float dt) {
        if (firstTime) {
            System.out.println("I am updating ...");
            firstTime = false;
        }
    }
}
