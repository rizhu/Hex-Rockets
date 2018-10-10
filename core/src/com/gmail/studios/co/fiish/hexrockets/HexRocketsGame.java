package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class HexRocketsGame extends Game {
	private TextureAtlas mAtlas;

	private FiishCoScreen mFiishCoScreen;
	private GameScreen mGameScreen;

	@Override
	public void create() {
		mAtlas = new TextureAtlas(Gdx.files.internal("spritesheet.atlas"));

		mGameScreen = new GameScreen(mAtlas);
		mFiishCoScreen = new FiishCoScreen();

		setScreen(mFiishCoScreen);
	}

	@Override
	public void render() {
		if (getScreen().equals(mFiishCoScreen) && mFiishCoScreen.mElapsedTime > 2.5f) { //Fiish Co logo screen remains active for 2.5 seconds
			setScreen(mGameScreen);
		}
		super.render();
	}

	@Override
	public void dispose() {
		mAtlas.dispose();
		mGameScreen.dispose();
		mFiishCoScreen.dispose();
	}

}
