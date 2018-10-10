package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CreditsBG extends Actor {
    private Viewport mViewport;
    private TextureRegion mRegion;

    private Animation<TextureRegion> mRichard, mJames;

    private float mElapsedTime = -1;

    public CreditsBG(Viewport viewport, TextureAtlas atlas) {
        mViewport = viewport;
        mRegion = atlas.findRegion("creditsbg");
        mRichard = new Animation(0.1f, atlas.findRegions("richard"));
        mJames = new Animation(0.1f, atlas.findRegions("james"));

        setTouchable(Touchable.disabled);
    }

    public void init() {
        this.clearActions();

        this.setWidth(mViewport.getScreenWidth() * 0.85f);
        this.setHeight(getWidth() / mRegion.getRegionWidth() * mRegion.getRegionHeight());

        this.setX(mViewport.getScreenWidth() / 2f - getWidth() / 2f);
        this.setY(mViewport.getScreenHeight() / 2f - getHeight() / 2f);

        this.setColor(getColor().r, getColor().g, getColor().b, 0);

        mElapsedTime = 0;
    }

    public void reset() {
        this.clearActions();
        this.setColor(getColor().r, getColor().g, getColor().b, 0);

        mElapsedTime = 0;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        mElapsedTime += delta;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mRegion, getX(), getY(), getWidth(), getHeight());
        batch.draw(mRichard.getKeyFrame(mElapsedTime, true),
                getX() + getWidth() * 0.035f, getY() + getHeight() * 0.45f, 30f / 192f * getWidth(), 30f / 105f * (9f/11f) * getHeight());
        batch.draw(mJames.getKeyFrame(mElapsedTime, true),
                getX() + getWidth() * 0.0125f, getY() + getHeight() * 0.085f, 35f / 192f * getWidth(), 35f / 105f * getHeight());
    }
}
