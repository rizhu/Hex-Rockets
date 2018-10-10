package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BackRightButton extends BasicButton {

    public BackRightButton(Viewport viewport, TextureAtlas atlas) {
        mViewport = viewport;
        mRegion = atlas.findRegion("backright");
    }

    @Override
    public void init() {
        mWidthDef = mViewport.getScreenHeight() * 0.0625f;
        mHeightDef = mViewport.getScreenHeight() * 0.0625f;

        mXDef = mViewport.getScreenWidth() / 9f * 8f + mViewport.getScreenWidth() / 9f * 0.7f - getWidth();
        mYDef = mViewport.getScreenHeight() / 16f * 0.3f;

        mTouchableDef = Touchable.disabled;

        mAlphaDef = 0;
        super.init();
    }

    @Override
    public void press() {

    }
}
