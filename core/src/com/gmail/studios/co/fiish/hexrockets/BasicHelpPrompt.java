package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class BasicHelpPrompt extends Actor {
    protected boolean mActive = false;

    protected Viewport mViewport;
    protected TextureRegion mRegion;

    public BasicHelpPrompt(TextureRegion region, Viewport viewport) {
        mRegion = region;
        mViewport = viewport;
        mActive = false;
    }

    public void init() {
        this.clearActions();
        this.setWidth(mViewport.getScreenWidth() * 0.925f);
        this.setHeight(getWidth() * mRegion.getRegionHeight() / mRegion.getRegionWidth());

        this.setX(mViewport.getScreenWidth() + 10f);
        this.setY(mViewport.getScreenHeight() / 2f - getHeight() / 3f);

        this.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());

        this.setTouchable(Touchable.disabled);
        this.setColor(getColor().r, getColor().g, getColor().b, 0);
        mActive = false;
    }

    public void reset() {
        this.clearActions();
        this.setX(mViewport.getScreenWidth() + 10f);
        this.setY(mViewport.getScreenHeight() / 2f - getHeight() / 3f);

        this.setTouchable(Touchable.disabled);

        this.setColor(getColor().r, getColor().g, getColor().b, 0);
        mActive = false;
    }

    public void enter() {
        this.clearActions();
        this.addAction(run(new Runnable() {
            @Override
            public void run() {
                addAction(fadeIn(0.35f));
                addAction(moveTo(mViewport.getScreenWidth() / 2 - getWidth() / 2, getY(), 0.35f));
            }
        }));
        mActive = true;
    }

    public void exitToLeft() {
        this.clearActions();
        this.addAction(run(new Runnable() {
            @Override
            public void run() {
                addAction(fadeOut(0.35f));
                addAction(moveTo(0 - getWidth() - 10, getY(), 0.35f));
            }
        }));
        mActive = false;
    }

    public void exitToRight() {
        this.clearActions();
        this.addAction(run(new Runnable() {
            @Override
            public void run() {
                addAction(fadeOut(0.35f));
                addAction(moveTo(mViewport.getScreenWidth() + 10f, getY(), 0.35f));
            }
        }));
        mActive = false;
    }

    public boolean isActive() {
        return mActive;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mRegion, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
