package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

    public ScoreBG(Viewport viewport, TextureAtlas atlas, FreeTypeFontGenerator generator) {
        mViewport = viewport;
        mRegion = atlas.findRegion("scorebg");
        mGenerator = generator;

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

        mParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mLayout = new GlyphLayout();
        mParam.size = (int) (mViewport.getScreenHeight() * 0.04f);
        mParam.color = Color.WHITE;
        mFont = mGenerator.generateFont(mParam);
    }

    public void reset() {
        this.clearActions();

        this.setX(mViewport.getScreenWidth() / 2f - mRegion.getRegionWidth() / 2f);
        this.setY(mViewport.getScreenHeight() + 10);

        this.setColor(getColor().r, getColor().g, getColor().b, 0);
    }

    public void setScore(int score) {
        mScore = score;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mRegion, getX(), getY(), getWidth(), getHeight());

        mLayout.setText(mFont, "" + mScore);
        mFont.draw(batch, mLayout, getX() + getWidth() * 0.605f, getY() + getHeight() * 0.61f);
    }

    public void dispose() {
        mGenerator.dispose();
        mFont.dispose();
    }
}
