package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ScoreBG extends Actor {
    private Viewport mViewport;
    private TextureRegion mRegion;

    private int mScore = -1;

    private FreeTypeFontGenerator mGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter mParam;
    private BitmapFont mFont;
    private GlyphLayout mLayout;

    public ScoreBG(Viewport viewport, TextureAtlas atlas) {
        mViewport = viewport;
        mRegion = atlas.findRegion("scorebg");

        setTouchable(Touchable.disabled);
    }

    public void init() {
        this.clearActions();

        this.setWidth(mViewport.getScreenWidth() * 0.75f);
        this.setHeight(getWidth() / mRegion.getRegionWidth() * mRegion.getRegionHeight());

        this.setX(mViewport.getScreenWidth() / 2f - getWidth() / 2f);
        this.setY(mViewport.getScreenHeight() * 0.4f);

        this.setColor(getColor().r, getColor().g, getColor().b, 0);
        this.setTouchable(Touchable.disabled);
    }

    public void reset() {
        this.clearActions();

        this.setX(mViewport.getScreenWidth() / 2f - mRegion.getRegionWidth() / 2f);
        this.setY(mViewport.getScreenHeight() + 10);
        
        this.setColor(getColor().r, getColor().g, getColor().b, 0);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mRegion, getX(), getY(), getWidth(), getHeight());
    }
}
