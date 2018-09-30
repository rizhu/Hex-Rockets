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
    private FreeTypeFontGenerator.FreeTypeFontParameter mProblemParam, mAnswerParam;
    private BitmapFont mProblemFont, mAnswerFont;
    private GlyphLayout mLayout;

    public GameScreen(TextureAtlas atlas) {
        mViewport = new ScreenViewport();

        mRockets = new Array<Rocket>(4);
        mAliens = new Array<Alien>(4);
        for (int i = 0; i < 4; ++i) {
            mRockets.add(new Rocket(mViewport, atlas, i) {
                @Override
                public void newProblem() {
                    setUpProblem(255);
                }
            });

            mAliens.add(new Alien(mViewport, atlas, i));
        }

        mProblem = new Problem();
        mAnswerSet = new AnswerSet();

        mBatch = new SpriteBatch();
    }

    @Override
    public void show() {
        mStage = new Stage(mViewport);

        mGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        mProblemParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mAnswerParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mLayout = new GlyphLayout();

        Gdx.gl.glClearColor(0, 0, 0, 1);

        Gdx.input.setInputProcessor(mStage);
        setUpProblem(255);
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

        mProblemParam.size = (int) (0.065f * mViewport.getScreenHeight());
        mProblemParam.color = Color.WHITE;
        mProblemFont = mGenerator.generateFont(mProblemParam);

        mAnswerParam.size = (int) (0.05f * mViewport.getScreenHeight());
        mAnswerParam.color = Color.WHITE;
        mAnswerFont = mGenerator.generateFont(mAnswerParam);
    }

    @Override
    public void render(float delta) {
        mViewport.apply(true);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        mStage.act(delta);
        mStage.draw();


        mBatch.begin();
        mLayout.setText(mProblemFont, mProblem.toString());
        mProblemFont.draw(mBatch, mLayout, mViewport.getScreenWidth() / 2f - mLayout.width / 2f, mViewport.getScreenHeight() * 0.36f);

        for (int i = 0; i < 4; ++i) {
            mLayout.setText(mAnswerFont, Integer.toHexString(mAnswerSet.getAnswerSet().get(i)).toUpperCase());
            mAnswerFont.draw(mBatch, mLayout,
                    (2 * i + 1f) * mViewport.getScreenWidth() / 8f - mLayout.width / 2f,
                    mRockets.get(i).getY() + 1.05f * mRockets.get(i).getHeight() + mLayout.height);
        }

        mBatch.end();
    }

    @Override
    public void dispose() {
        mBatch.dispose();
        mProblemFont.dispose();
        mGenerator.dispose();
    }

    public void setUpProblem(int bound) {
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
}
