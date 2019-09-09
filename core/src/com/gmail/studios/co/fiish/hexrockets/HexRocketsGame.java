package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class HexRocketsGame extends Game {
	private TextureAtlas mAtlas;
	private AssetManager mAssetManager;

	private FiishCoScreen mFiishCoScreen;

	@Override
	public void create() {
		mAtlas = new TextureAtlas(Gdx.files.internal("spritesheet.atlas"));

		mAssetManager = new AssetManager();

		mAssetManager.load("correct.wav", Sound.class);
		mAssetManager.load("death.wav", Sound.class);
		mAssetManager.load("highScore.wav", Sound.class);
		mAssetManager.load("start.wav", Sound.class);
		mFiishCoScreen = new FiishCoScreen();

		setScreen(mFiishCoScreen);
	}

	@Override
	public void render() {
		if (getScreen().equals(mFiishCoScreen) && mFiishCoScreen.mElapsedTime > 2.5f && mAssetManager.update()) { //Fiish Co logo screen remains active for 2.5 seconds
			setScreen(new GameScreen(mAtlas, mAssetManager));
		}
		super.render();
	}

	@Override
	public void dispose() {
		mAtlas.dispose();
		mAssetManager.dispose();
		mFiishCoScreen.dispose();
	}

}
