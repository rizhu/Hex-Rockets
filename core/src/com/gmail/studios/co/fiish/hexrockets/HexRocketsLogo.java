package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HexRocketsLogo extends Actor {
    private Viewport mViewport;
    private TextureRegion mRegion;

    public HexRocketsLogo(Viewport viewport, TextureAtlas atlas) {
        mViewport = viewport;
        mRegion = atlas.findRegion("hex-rockets");
    }

    public void init() {
        this.setWidth(mViewport.getScreenWidth() * 0.85f);
        this.setHeight(getWidth() / mRegion.getRegionWidth() * mRegion.getRegionHeight());

        this.setX(mViewport.getScreenWidth() / 2f - getWidth() / 2f);
        this.setY(mViewport.getScreenHeight() * 0.7f);

        this.setTouchable(Touchable.disabled);

        this.setColor(getColor().r, getColor().g, getColor().b, 1);
    }

    public void reset() {
        this.setX(mViewport.getScreenWidth() / 2f - getWidth() / 2f);
        this.setY(mViewport.getScreenHeight() * 0.7f);

        this.setTouchable(Touchable.disabled);

        this.setColor(getColor().r, getColor().g, getColor().b, 1);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mRegion, getX(), getY(), getWidth(), getHeight());
    }
}
