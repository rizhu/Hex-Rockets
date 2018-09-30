package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class HexRocketsGame extends Game {
	private TextureAtlas mAtlas;

	@Override
	public void create() {
		mAtlas = new TextureAtlas(Gdx.files.internal("spritesheet.atlas"));
		setScreen(new GameScreen(mAtlas));
	}

	@Override
	public void dispose() {
		mAtlas.dispose();
	}

}
