package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SoundButton extends BasicButton {
    private TextureRegion mSoundOn;
    private TextureRegion mSoundOff;

    public SoundButton(Viewport viewport, TextureAtlas atlas, boolean soundOn) {
        mViewport = viewport;
        if (soundOn) {
            mRegion = atlas.findRegion("soundon");
        } else {
            mRegion = atlas.findRegion("soundoff");
        }
        mSoundOn = atlas.findRegion("soundon");
        mSoundOff = atlas.findRegion("soundoff");
    }

    @Override
    public void init() {
        mWidthDef = mViewport.getScreenHeight() * 0.0625f;
        mHeightDef = mViewport.getScreenHeight() * 0.0625f;

        mXDef = mViewport.getScreenWidth() / 2f - mWidthDef / 2f;
        mYDef = mViewport.getScreenHeight() / 16f * 0.3f;

        mTouchableDef = Touchable.enabled;

        mAlphaDef = 1;
        super.init();
    }

    @Override
    public void press() {

    }

    public void setOn() {
        mRegion = mSoundOn;
    }

    public void setOff() {
        mRegion = mSoundOff;
    }
}
