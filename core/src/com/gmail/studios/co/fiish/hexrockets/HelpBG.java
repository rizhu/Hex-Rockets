package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HelpBG extends Actor {
    private Viewport mViewport;
    private TextureRegion mRegion;

    public HelpBG(Viewport viewport, TextureAtlas atlas) {
        mViewport = viewport;
        mRegion = atlas.findRegion("helpbg");

        setTouchable(Touchable.disabled);
    }

    public void init() {
        this.clearActions();

        this.setWidth(mViewport.getScreenWidth() * 0.85f);
        this.setHeight(getWidth() / mRegion.getRegionWidth() * mRegion.getRegionHeight());

        this.setX(mViewport.getScreenWidth() / 2f - getWidth() / 2f);
        this.setY(mViewport.getScreenHeight() / 2f - getHeight() / 2f);

        this.setColor(getColor().r, getColor().g, getColor().b, 0);
    }

    public void reset() {
        this.clearActions();
        this.setColor(getColor().r, getColor().g, getColor().b, 0);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mRegion, getX(), getY(), getWidth(), getHeight());
    }
}
