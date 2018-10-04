package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {

    private Viewport mViewport;
    private SpriteBatch mBatch;

    private Array<Rocket> mRockets;
    private Array<Alien> mAliens;
    private Stage mStage;

    private Problem mProblem;
    private AnswerSet mAnswerSet;

    private FreeTypeFontGenerator mGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter mProblemParam, mAnswerParam, mScoreParam;
    private BitmapFont mProblemFont, mAnswerFont, mScoreFont;
    private GlyphLayout mLayout;

    private int mScore = -1;

    public GameScreen(TextureAtlas atlas) {
        mViewport = new ScreenViewport();

        mStage = new Stage(mViewport);

        mRockets = new Array<Rocket>(4);
        mAliens = new Array<Alien>(4);

        for (int i = 0; i < 4; ++i) {
            mRockets.add(new Rocket(mViewport, atlas, i) {
                @Override
                public void newProblem() {
                    ++mScore;
                    for (int i = 0; i < 4; i++) {
                        setUpProblem(15);
                        resetAlienPosition(4);
                    }
                }

                @Override
                public void gameOver() {
                    resetGameSequence();
                }
            });

            mAliens.add(new Alien(mViewport, atlas, i));
        }

        mProblem = new Problem();
        mAnswerSet = new AnswerSet();

        mBatch = new SpriteBatch();
        FreeTypeFontGenerator.setMaxTextureSize(4096);
    }

    @Override
    public void show() {
        mGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        mProblemParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mAnswerParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mScoreParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mLayout = new GlyphLayout();

        Gdx.gl.glClearColor(0, 0, 0, 1);

        Gdx.input.setInputProcessor(mStage);
        setUpProblem(15);
    }

    @Override
    public void resize(int width, int height) {
        mViewport.update(width, height);

        for (int i = 0; i < 4; ++i) {
            mRockets.get(i).init();
            mAliens.get(i).init();
            mStage.addActor(mRockets.get(i));
            mStage.addActor(mAliens.get(i));
        }

        mProblemParam.size = (int) (0.075f * mViewport.getScreenHeight());
        mProblemParam.color = Color.WHITE;
        mProblemFont = mGenerator.generateFont(mProblemParam);

        mAnswerParam.size = (int) (0.06f * mViewport.getScreenHeight());
        mAnswerParam.color = Color.WHITE;
        mAnswerFont = mGenerator.generateFont(mAnswerParam);

        mScoreParam.size = (int) (0.04f * mViewport.getScreenHeight());
        mScoreParam.color = new Color(0.224f, 1f, .078f, 1);
        mScoreFont = mGenerator.generateFont(mScoreParam);

        sendAliensForward(4);
        mScore = 0;
    }

    @Override
    public void render(float delta) {
        mViewport.apply(true);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        mBatch.begin();
        mLayout.setText(mProblemFont, mProblem.toString());
        mProblemFont.draw(mBatch, mLayout, mViewport.getScreenWidth() / 2f - mLayout.width / 2f, mViewport.getScreenHeight() * 0.36f);

        for (int i = 0; i < 4; ++i) {
            mLayout.setText(mAnswerFont, Integer.toHexString(mAnswerSet.getAnswerSet().get(i)).toUpperCase());
            mAnswerFont.draw(mBatch, mLayout,
                    (2 * i + 1f) * mViewport.getScreenWidth() / 8f - mLayout.width / 2f,
                    mRockets.get(i).getY() + 1.075f * mRockets.get(i).getHeight() + mLayout.height);
        }

        mLayout.setText(mScoreFont, "Streak: " + mScore);
        mScoreFont.draw(mBatch, mLayout, mViewport.getScreenWidth() / 2f - mLayout.width / 2f, mViewport.getScreenHeight() * 0.26f);

        mBatch.end();
        mStage.act(delta);
        mStage.draw();
    }

    @Override
    public void dispose() {
        mBatch.dispose();
        mProblemFont.dispose();
        mGenerator.dispose();
    }

    private void checkAlienPos() {
        if (mAliens.get(0).getY() == mRockets.get(0).getY() + mRockets.get(0).getHeight() * (20f / 52f) - mAliens.get(0).getHeight()) {
            for (int i = 0; i < 4; ++i) {
                mAliens.get(i).clearActions();
                mRockets.get(i).addAction(fadeOut(1f));
            }
        }
    }

    private void setUpProblem(int bound) {
        mProblem.generateProblem(bound);

        Gdx.app.log("Problem", mProblem.toString());
        Gdx.app.log("Solution", "" + Integer.toHexString(mProblem.getSolution()));

        mAnswerSet.generateAnswers(mProblem.getSolution(), bound, 32);

        for (int i = 0; i < 4; ++i) {
            Gdx.app.log("" + i, "" + Integer.toHexString(mAnswerSet.getAnswerSet().get(i)));
            if (i == mAnswerSet.getAnswerIndex()) {
                mRockets.get(i).setAnswerState(AnswerState.Correct);
            } else {
                mRockets.get(i).setAnswerState(AnswerState.Incorrect);
            }
            Gdx.app.log("" + i, "" + mRockets.get(i).getAnswerState());
        }
    }

    private void resetAlienPosition(float nextDuration) {
        for (int i = 0; i < 4; i++) {
            mAliens.get(i).clearActions();
            mAliens.get(i).addAction(sequence(moveTo(mAliens.get(i).mInitialX, 0, 0.15f),
                    moveTo(mAliens.get(i).getX(),
                            mRockets.get(i).getY() + mRockets.get(i).getHeight() * (20f / 52f) - mAliens.get(i).getHeight(), nextDuration)));
        }
    }

    private void resetGameSequence() {
        mScore = 0;
        for (int i = 0; i < 4; ++i) {
            mRockets.get(i).setTouchable(Touchable.disabled);
            mAliens.get(i).clearActions();
            mRockets.get(i).addAction(fadeOut(0.25f));
        }
        mStage.addAction(delay(2.0f, run(new Runnable() {
            @Override
            public void run() {
                setUpProblem(15);
                for (int i = 0; i < 4; ++i) {
                    mRockets.get(i).addAction(fadeIn(0.25f));
                    mRockets.get(i).setTouchable(Touchable.enabled);
                }
                resetAlienPosition(4);
            }
        })));
    }

    private void moveAliensBack() {
        for (int i = 0; i < 4; i++) {
            mAliens.get(i).clearActions();
            mAliens.get(i).addAction(moveTo(mAliens.get(i).mInitialX, 0, 0.15f));
        }
    }

    private void sendAliensForward(float duration) {
        for (int i = 0; i < 4; i++) {
            mAliens.get(i).addAction(moveTo(mAliens.get(i).getX(),
                    mRockets.get(i).getY() + mRockets.get(i).getHeight() * (20f / 52f) - mAliens.get(i).getHeight(), duration));
        }
    }
}
