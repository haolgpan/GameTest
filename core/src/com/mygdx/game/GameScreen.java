package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen implements Screen {
    final MyGdxGame game;
    OrthographicCamera camera;
    Stage stage;
    Player player;
    boolean dead;
    Array<Monsters> obstacles;
    Array<Bullet> bullets;

    long lastObstacleTime;
    float score;
    float touchY;
    Music bgMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
    float elapsedTime;

    public GameScreen(final MyGdxGame gam) {
        this.game = gam;
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        player = new Player(game.manager);
        bullets = player.bullets;
        stage = new Stage();
        stage.getViewport().setCamera(camera);
        stage.addActor(player);
        // create the obstacles array and spawn the first obstacle
        obstacles = new Array<Monsters>();
        spawnObstacle();
        score = 0;
        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touchPos = new Vector3();
                touchPos.set(screenX, screenY, 0);
                camera.unproject(touchPos);
                // Ahora touchPos contiene las coordenadas trasnformadas
                player.touch(touchPos.y);

                return true;
            }
        });
        bgMusic.play();
    }

    @Override
    public void render(float delta) {
        // clear the screen with a color
        ScreenUtils.clear(0.3f, 0.8f, 0.8f, 1);
        // tell the camera to update its matrices.
        camera.update();
        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);
        // begin a new batch
        game.batch.begin();
        game.batch.draw(game.manager.get("space.jpg", Texture.class), 0, 0);
        game.smallFont.draw(game.batch, "Score: " + (int)score, 10, 470);
        game.batch.end();
        // Stage batch: Actors
        stage.getBatch().setProjectionMatrix(camera.combined);
        stage.draw();
        // process user input
        /*if (Gdx.input.justTouched()) {
            touchY = Gdx.graphics.getHeight() - Gdx.input.getY(); // flip y-axis
            player.touch(touchY);
        }*/
        stage.act();
        boolean dead = false;
        // Comprova que el jugador no es surt de la pantalla.
        // Si surt per la part inferior, game over
        if (player.getBounds().y > 480 - 45)
            player.setY(480 - 45);
        if (player.getBounds().y < 0 - 45) {
            dead = true;
        }
        //La puntuació augmenta amb el temps de joc
        //score += Gdx.graphics.getDeltaTime();
        // Comprova si cal generar un obstacle nou
        if (TimeUtils.nanoTime() - lastObstacleTime > 1500000000)
            spawnObstacle();
        // Comprova si els monstres colisionen amb el jugador
        Iterator<Monsters> iter = obstacles.iterator();
        while (iter.hasNext()) {
            Monsters monsters = iter.next();
            if (monsters.getBounds().overlaps(player.getBounds())) {
                dead = true;
            }
            Iterator<Bullet> bulletIter = bullets.iterator();
            while (bulletIter.hasNext()) {
                Bullet bullet = bulletIter.next();
                if (monsters.getBounds().overlaps(bullet.getBounds())) {
                    iter.remove(); // Elimina el monstre de l'array d'obstacles
                    game.manager.get("explosion.wav", Sound.class).play();
                    monsters.remove();
                    game.batch.begin();
                    elapsedTime += delta*2;
                    if (elapsedTime < 10f) {
                        game.batch.draw(game.manager.get("laserYellow_burst.png", Texture.class), monsters.getX(), monsters.getY(), 50, 50);
                    }
                    game.batch.end();
                    score += 1;
                    //bulletIter.remove(); // Esborra la bala del jugador també
                    break;
                }
            }
        }
        // Treure de l'array les tuberies que estan fora de pantalla
        iter = obstacles.iterator();
        while (iter.hasNext()) {
            Monsters monsters = iter.next();
            //Puntua per pipes
            /*if(player.getX() > monsters.getX() && monsters.scoreAdded == false){
                score += 1;
                monsters.scoreAdded = true;
            }*/
            //
            if (monsters.getX() < -64) {
                obstacles.removeValue(monsters, true);
            }
        }
        if (dead) {
            game.manager.get("sfx_lose.wav", Sound.class).play();
            game.lastScore = (int)score;
            if(game.lastScore > game.topScore)
                game.topScore = game.lastScore;
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

    private void spawnObstacle() {
        // Calcula la alçada de l'obstacle aleatòriament
        float holey = MathUtils.random(50, 230);
        // Crea dos obstacles: Una tubería superior i una inferior
        Monsters monsters1 = new Monsters();
        monsters1.setX(800);
        monsters1.setY(holey - 200);
        monsters1.setManager(game.manager);
        obstacles.add(monsters1);
        stage.addActor(monsters1);
        Monsters monsters2 = new Monsters();
        monsters2.setX(800);
        monsters2.setY(holey + 200);
        monsters2.setManager(game.manager);
        obstacles.add(monsters2);
        stage.addActor(monsters2);
        lastObstacleTime = TimeUtils.nanoTime();
    }
}
