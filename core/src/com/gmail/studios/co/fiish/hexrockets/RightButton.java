package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class RightButton extends BasicButton {

    public RightButton(Viewport viewport, TextureAtlas atlas) {
        mViewport = viewport;
        mRegion = atlas.findRegion("right");
    }

    @Override
    public void init() {
        mWidthDef = mViewport.getScreenWidth() * 0.2f;
        mHeightDef = mWidthDef / mRegion.getRegionWidth() * mRegion.getRegionHeight();

        mXDef = mViewport.getScreenWidth() / 2f + 20;
        mYDef = mViewport.getScreenHeight() / 2f - (mViewport.getScreenWidth() * 0.925f * 700 / 675 / 3) - mHeightDef - 25;

        mTouchableDef = Touchable.disabled;

        mAlphaDef = 0;
        super.init();
    }

    @Override
    public void press() {

    }
}
