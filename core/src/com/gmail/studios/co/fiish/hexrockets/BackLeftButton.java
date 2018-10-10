package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BackLeftButton extends BasicButton {

    public BackLeftButton(Viewport viewport, TextureAtlas atlas) {
        mViewport = viewport;
        mRegion = atlas.findRegion("backleft");
    }

    @Override
    public void init() {
        mWidthDef = mViewport.getScreenHeight() * 0.0625f;
        mHeightDef = mViewport.getScreenHeight() * 0.0625f;

        mXDef = mViewport.getScreenWidth() / 9f * 0.3f;
        mYDef = mViewport.getScreenHeight() / 16f * 0.3f;

        mTouchableDef = Touchable.disabled;

        mAlphaDef = 0;
        super.init();
    }

    @Override
    public void press() {

    }
}
