package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Alien extends Actor {
    public float mInitialX = -1f;

    private Viewport mViewport;
    private Animation<TextureRegion> mAnimation;

    private int mID = -1;   //Goes from 0 - 3

    private float mSpriteWidth = -1;
    private float mSpriteHeight = -1;

    private float mElapsedTime = -1;

    public Alien(Viewport viewport, TextureAtlas atlas, int id) {
        mViewport = viewport;
        mAnimation = new Animation<TextureRegion>(1f/4, atlas.findRegions("alienGreen"));

        mID = id;

        mSpriteWidth = atlas.findRegion("alienGreen", 1).getRegionWidth();
        mSpriteHeight = atlas.findRegion("alienGreen", 1).getRegionHeight();
    }

    public void init() {
        this.clearActions();

        this.setWidth(mViewport.getScreenWidth() / 4f * (24f / 25));
        this.setHeight(this.getWidth() / mSpriteWidth * mSpriteHeight);

        this.setX((2 * mID + 1) * mViewport.getScreenWidth() / 8f - getWidth() / 2f);
        this.setY(0);

        mInitialX = (2 * mID + 1) * mViewport.getScreenWidth() / 8f - getWidth() / 2f;

        mElapsedTime = 0f;
    }

    public void reset() {
        this.clearActions();

        this.addAction(Actions.delay(2, Actions.run(new Runnable() {
            @Override
            public void run() {
                setX((2 * mID + 1) * mViewport.getScreenWidth() / 8f - getWidth() / 2f);
                setY(0);
            }
        })));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        mElapsedTime += delta;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(mAnimation.getKeyFrame(mElapsedTime, true), this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}