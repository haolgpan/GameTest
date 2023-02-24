package com.mygdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Monsters extends Actor {
    Rectangle bounds;
    AssetManager manager;
    boolean scoreAdded;
    Monsters()
    {
        setSize(30, 30);
        bounds = new Rectangle();
        setVisible(false);
        scoreAdded = false;
    }
    @Override
    public void act(float delta) {
        moveBy(-200 * delta, 0);
        bounds.set(getX(), getY(), getWidth(), getHeight());
        if(!isVisible())
            setVisible(true);
        if (getX() < -64)
            remove();
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw( manager.get( "shipBeige_manned.png", Texture.class), getX(), getY(), 30,30 );
    }
    public Rectangle getBounds() {
        return bounds;
    }
    public void setManager(AssetManager manager) {
        this.manager = manager;
    }
}