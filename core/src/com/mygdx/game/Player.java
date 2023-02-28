package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.utils.Array;

public class Player extends Actor {
    Rectangle bounds;
    AssetManager manager;
    float speedy, gravity;
    float touchY;
    Array<Bullet> bullets;
    int bulletIndex;
    float bulletTime = 1.0f;

    Player(AssetManager manager) {
        this.manager = manager;
        setX(20);
        setY(280 / 2 - 64 / 2);
        setSize(50,50);
        bounds = new Rectangle();
        speedy = 0;
        touchY = getY();
        bullets = new Array<Bullet>(10);
        for (int i = 0; i < 10; i++) {
            bullets.add(new Bullet(manager.get("laserBlue02.png", Texture.class)));
        }
        bulletIndex = 0;
    }

    @Override
    public void act(float delta) {
        // Check if the screen is touched
        if(this.getY() < touchY) moveBy(0,140*delta);
        else if (this.getY() > touchY) moveBy(0,-140.f*delta);

        bounds.set(getX(), getY(), getWidth(), getHeight());
        if(bulletTime <= 0) {
            bullets.get(bulletIndex).shoot(getX() + getWidth(), getY());
            manager.get("sfx_laser1.wav", Sound.class).play();
            bulletIndex++;
            if (bulletIndex >= bullets.size) {
                bulletIndex = 0;
            }
            bulletTime = 1.0f;
        }
        else bulletTime -= delta;
        for (int i = 0; i < bullets.size; i++) {
            bullets.get(i).update(delta);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(manager.get("playerShip2_orange.png", Texture.class), getX(), getY(), 50, 50);
        for (int i = 0; i < bullets.size; i++) {
            if (bullets.get(i).isActive()) {
                bullets.get(i).render(batch);
            }
        }
    }

    public Rectangle getBounds() {
        return bounds;
    }

    void touch(float getY) {
        touchY = getY;
    }
}



