package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class NormalButton extends BasicButton {

    public NormalButton(Viewport viewport, TextureAtlas atlas) {
        mViewport = viewport;
        mRegion = atlas.findRegion("normal");
    }

    @Override
    public void init() {
        mWidthDef = mViewport.getScreenWidth() * 0.35f;
        mHeightDef = mWidthDef / mRegion.getRegionWidth() * mRegion.getRegionHeight();

        mXDef = mViewport.getScreenWidth() / 2f - getWidth() / 2f;
        mYDef = mViewport.getScreenHeight() * 0.45f;

        mTouchableDef = Touchable.enabled;

        mAlphaDef = 1;
        super.init();
    }

    @Override
    public void press() {

    }
}
