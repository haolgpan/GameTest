package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends Game {
	AssetManager manager;
	SpriteBatch batch;
	BitmapFont smallFont, bigFont;
	int topScore;
	int lastScore;
	public void create() {
		//Load assets
		manager = new AssetManager();
		manager.load("playerShip2_orange.png", Texture.class);
		manager.load("shipBeige_manned.png", Texture.class);
		manager.load("purple.png", Texture.class);
		manager.load("space.jpg", Texture.class);
		manager.load("laserBlue02.png", Texture.class);
		manager.load("laserBlue12.png", Texture.class);
		manager.finishLoading();
		//
		batch = new SpriteBatch();
		// Create bitmap fonts from TrueType font
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("kenvector_future.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.size = 22;
		params.borderWidth = 2;
		params.borderColor = Color.BLACK;
		params.color = Color.WHITE;
		smallFont = generator.generateFont(params);
		// font size 22 pixels
		params.size = 50;
		params.borderWidth = 5;
		bigFont = generator.generateFont(params); // font size 50 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!
		topScore = 0;
		lastScore = 0;
		this.setScreen(new MainMenuScreen(this));

	}
	public void render() {
		super.render(); // important!
	}
	public void dispose() {
	}
}