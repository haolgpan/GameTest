package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Bullet extends Actor {
    private Rectangle bounds;
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;

    public Bullet(Texture texture) {
        this.texture = texture;
        position = new Vector2();
        velocity = new Vector2();
        active = false;
        bounds = new Rectangle();
        setSize(50,50);

    }

    public void update(float delta) {
        position.add(velocity.x * delta, velocity.y * delta);
        bounds.set(getX(), getY(), getWidth(), getHeight());
        if (position.x > Gdx.graphics.getWidth()) {
            active = false;
        }
    }

    public void render(Batch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void shoot(float x, float y) {
        position.set(x, y + 25);
        velocity.set(500, 0);
        active = true;
    }

    public boolean isActive() {
        return active;
    }

    public Vector2 getPosition() {
        return position;
    }
    public Rectangle getBounds() {
        return bounds;
    }
}

