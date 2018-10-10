package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Rocket extends Actor { //Rocket fin starts at 21 pixels from bottom of sprite (21 / 52)
    private Viewport mViewport;
    private Animation<TextureRegion> mAnimation;
    private AnswerState mAnswerState = AnswerState.Null;

    private int mID = -1;   //Goes from 0 - 3

    private float mSpriteWidth = -1;
    private float mSpriteHeight = -1;

    private float mElapsedTime = -1;

    public Rocket(Viewport viewport, TextureAtlas atlas, int id) {
        mViewport = viewport;
        mAnimation = new Animation<TextureRegion>(1f/10, atlas.findRegions("rocket"));
        mID = id;

        mSpriteWidth = atlas.findRegion("rocket", 1).getRegionWidth();
        mSpriteHeight = atlas.findRegion("rocket", 1).getRegionHeight();

        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (mAnswerState == AnswerState.Correct) {
                    newProblem();
                    return true;
                }
                if (mAnswerState == AnswerState.Incorrect) {
                    gameOver();
                    return true;
                }
                return true;
            }
        });
    }

    public void init() {
        this.clearActions();

        this.setWidth(mViewport.getScreenWidth() / 4f * (18f / 25));
        this.setHeight(this.getWidth() / mSpriteWidth * mSpriteHeight);

        this.setX((2 * mID + 1) * (mViewport.getScreenWidth() / 8f) - getWidth() / 2f);
        this.setY((mViewport.getScreenHeight() - this.getHeight()) * 0.75f);

        mElapsedTime = 0f;

        this.setTouchable(Touchable.enabled);
        this.setColor(getColor().r, getColor().g, getColor().b, 0);
    }

    public void reset() {
        this.clearActions();

        this.setX((2 * mID + 1) * (mViewport.getScreenWidth() / 8f) - getWidth() / 2f);
        this.setY((mViewport.getScreenHeight() - this.getHeight()) * 0.75f);

        this.setTouchable(Touchable.enabled);
        this.setColor(getColor().r, getColor().g, getColor().b, 0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        mElapsedTime += delta;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mAnimation.getKeyFrame(mElapsedTime, true), this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    public void setAnswerState(AnswerState state) {
        mAnswerState = state;
    }

    public void newProblem() { }

    public void gameOver() { }

}
