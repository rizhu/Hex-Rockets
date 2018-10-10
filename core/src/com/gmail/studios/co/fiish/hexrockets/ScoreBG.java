package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
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
    private Animation<TextureRegion> mDiamondAnimation, mGoldAnimation, mSilverAnimation, mBronzeAnimation;

    private int mScore = -1;
    private int mHigh = -1;
    private boolean mHardMode = false;

    private FreeTypeFontGenerator mGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter mParam;
    private BitmapFont mFont;
    private GlyphLayout mLayout;

    private float mElapsedTime = -1;

    public ScoreBG(Viewport viewport, TextureAtlas atlas, FreeTypeFontGenerator generator) {
        mViewport = viewport;
        mRegion = atlas.findRegion("scorebg");
        mGenerator = generator;

        mDiamondAnimation = new Animation(1f/6f, atlas.findRegions("diamond"));
        mGoldAnimation = new Animation(1f/6f, atlas.findRegions("gold"));
        mSilverAnimation = new Animation(1f/6f, atlas.findRegions("silver"));
        mBronzeAnimation = new Animation(1f/6f, atlas.findRegions("bronze"));

        setTouchable(Touchable.disabled);
    }

    public void init() {
        this.clearActions();

        this.setWidth(mViewport.getScreenWidth() * 0.75f);
        this.setHeight(getWidth() / mRegion.getRegionWidth() * mRegion.getRegionHeight());

        this.setX(mViewport.getScreenWidth() / 2f - getWidth() / 2f);
        this.setY(mViewport.getScreenHeight());

        this.setColor(getColor().r, getColor().g, getColor().b, 1);
        this.setTouchable(Touchable.disabled);

        mParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mLayout = new GlyphLayout();
        mParam.size = (int) (mViewport.getScreenHeight() * 0.04f);
        mParam.color = Color.WHITE;
        mFont = mGenerator.generateFont(mParam);

        mElapsedTime = 0;
    }

    public void reset() {
        this.clearActions();

        this.setX(mViewport.getScreenWidth() / 2f - mRegion.getRegionWidth() / 2f);
        this.setY(mViewport.getScreenHeight() + 10);

        this.setColor(getColor().r, getColor().g, getColor().b, 1);

        mElapsedTime = 0;
    }

    public void sendData(int score, int high, boolean hardMode) {
        mScore = score;
        mHigh = high;
        mHardMode = hardMode;
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

        if (mHardMode) {
            if (mScore >= 20){
                batch.draw(mDiamondAnimation.getKeyFrame(mElapsedTime, true),
                        getX() + getWidth() * 0.082f, getY() + getHeight() * 0.135f, 63f / 192f * getWidth(), 61f / 128f * getHeight());
            } else if (mScore >= 15){
                batch.draw(mGoldAnimation.getKeyFrame(mElapsedTime, true),
                        getX() + getWidth() * 0.082f, getY() + getHeight() * 0.135f, 63f / 192f * getWidth(), 61f / 128f * getHeight());
            } else if (mScore >= 10){
                batch.draw(mSilverAnimation.getKeyFrame(mElapsedTime, true),
                        getX() + getWidth() * 0.082f, getY() + getHeight() * 0.135f, 63f / 192f * getWidth(), 61f / 128f * getHeight());
            } else if (mScore >= 5){
                batch.draw(mBronzeAnimation.getKeyFrame(mElapsedTime, true),
                        getX() + getWidth() * 0.082f, getY() + getHeight() * 0.135f, 63f / 192f * getWidth(), 61f / 128f * getHeight());
            }
        } else {
            if (mScore >= 40){
                batch.draw(mDiamondAnimation.getKeyFrame(mElapsedTime, true),
                        getX() + getWidth() * 0.082f, getY() + getHeight() * 0.135f, 63f / 192f * getWidth(), 61f / 128f * getHeight());
            } else if (mScore >= 30){
                batch.draw(mGoldAnimation.getKeyFrame(mElapsedTime, true),
                        getX() + getWidth() * 0.082f, getY() + getHeight() * 0.135f, 63f / 192f * getWidth(), 61f / 128f * getHeight());
            } else if (mScore >= 20){
                batch.draw(mSilverAnimation.getKeyFrame(mElapsedTime, true),
                        getX() + getWidth() * 0.082f, getY() + getHeight() * 0.135f, 63f / 192f * getWidth(), 61f / 128f * getHeight());
            } else if (mScore >= 10){
                batch.draw(mBronzeAnimation.getKeyFrame(mElapsedTime, true),
                        getX() + getWidth() * 0.082f, getY() + getHeight() * 0.135f, 63f / 192f * getWidth(), 61f / 128f * getHeight());
            }
        }

        mLayout.setText(mFont, "" + mScore);
        mFont.draw(batch, mLayout, getX() + getWidth() * 0.605f, getY() + getHeight() * 0.625f);

        mLayout.setText(mFont, "" + mHigh);
        mFont.draw(batch, mLayout, getX() + getWidth() * 0.605f, getY() + getHeight() * 0.245f);
    }

    public void dispose() {
        mGenerator.dispose();
        mFont.dispose();
    }
}
