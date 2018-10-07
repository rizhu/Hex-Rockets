package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Star extends Actor {
    private Viewport mViewport;
    private Animation<TextureRegion> mAnimation;
    private float mAlphaDef = 1f;

    private float mElapsedTime = -1;

    public Star(Viewport viewport, TextureAtlas atlas) {
        mViewport = viewport;
        mAnimation = new Animation<TextureRegion>(1f/MathUtils.random(4, 7), atlas.findRegions("star"));
    }

    public void init() {
        this.clearActions();

        this.setWidth(MathUtils.random(mViewport.getScreenHeight() / 35f, mViewport.getScreenHeight() / 25f));
        this.setHeight(getWidth());

        this.setX(MathUtils.random(mViewport.getScreenWidth() / 16f, 15f * mViewport.getScreenWidth() / 16f));
        this.setY(MathUtils.random(mViewport.getScreenHeight() / 16f, 15f * mViewport.getScreenHeight() / 16f));

        mElapsedTime = 0f;

        this.setTouchable(Touchable.disabled);
        this.setColor(getColor().r, getColor().g, getColor().b, mAlphaDef);
    }

    public void reset() {
        this.clearActions();
        this.setColor(getColor().r, getColor().g, getColor().b, mAlphaDef);
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
}
