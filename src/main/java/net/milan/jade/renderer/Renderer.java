package net.milan.jade.renderer;

import net.milan.jade.GameObject;
import net.milan.jade.components.SpriteRenderer;

import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> renderBatches;

    public Renderer(){
        this.renderBatches = new ArrayList<>();
    }

    public void add(GameObject object){
        SpriteRenderer spriteRenderer = object.getComponent(SpriteRenderer.class);
        if (spriteRenderer != null){
            add(spriteRenderer);
        }
    }

    private void add(SpriteRenderer spriteRenderer){
        boolean added = false;
        for (RenderBatch batch : renderBatches){
            if (batch.hasRoom()){
                batch.addSprite(spriteRenderer);
                added = true;
                break;
            }
        }

        if (!added){
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            renderBatches.add(newBatch);
            newBatch.addSprite(spriteRenderer);
        }
    }

    public void render(){
        for (RenderBatch batch : renderBatches){
            batch.render();
        }
    }

}