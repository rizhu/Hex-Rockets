package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
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
    private Preferences mData;

    private Stage mTitleUI, mMainStage, mGameOverUI;

    private HexRocketsLogo mLogo;
    private NormalButton mNormalButton;
    private HardButton mHardButton;

    private Array<Rocket> mRockets;
    private Array<Alien> mAliens;
    private Array<Star> mStars;

    private GameOverLogo mGameOverLogo;
    private ScoreBG mScoreBG;
    private HomeButton mHomeButton;
    private ReplayButton mReplayButton;

    private Problem mProblem;
    private AnswerSet mAnswerSet;

    private FreeTypeFontGenerator mGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter mProblemParam, mAnswerParam, mScoreParam;
    private BitmapFont mProblemFont, mAnswerFont, mScoreFont;
    private GlyphLayout mLayout;

    private int mScore = -1;
    private boolean mCheckAlienPositions = true;
    private boolean mHardMode = false;

    public GameScreen(TextureAtlas atlas) {
        mViewport = new ScreenViewport();
        mData = Gdx.app.getPreferences("Data");

        mGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        mProblemParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mAnswerParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mScoreParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mLayout = new GlyphLayout();

        mTitleUI = new Stage((mViewport));
        mMainStage = new Stage(mViewport);
        mGameOverUI = new Stage(mViewport);

        mLogo = new HexRocketsLogo(mViewport, atlas);
        mNormalButton = new NormalButton(mViewport, atlas) {
            @Override
            public void press() {
                mHardMode = false;
                normalButtonPress();
            }
        };
        mHardButton = new HardButton(mViewport, atlas) {
            @Override
            public void press() {
                mHardMode = true;
                hardButtonPress();
            }
        };

        mRockets = new Array<Rocket>(4);
        mAliens = new Array<Alien>(4);
        mStars = new Array<Star>();

        for (int i = 0; i < MathUtils.random(8, 15); ++i) {
            mStars.add(new Star(mViewport, atlas));
        }

        for (int i = 0; i < 4; ++i) {
            mRockets.add(new Rocket(mViewport, atlas, i) {
                @Override
                public void newProblem() {
                    ++mScore;
                    if (mHardMode) {
                        setUpProblem(255);
                        for (int i = 0; i < 4; i++) {
                            resetAlienPosition(30f / (mScore + 3f) + 5);
                        }
                    } else {
                        setUpProblem(15);
                        for (int i = 0; i < 4; i++) {
                            resetAlienPosition(11f / (mScore + 3.6666f) + 1);
                        }
                    }
                }

                @Override
                public void gameOver() {
                    gameOverSequence();
                }
            });

            mAliens.add(new Alien(mViewport, atlas, i));
        }

        mGameOverLogo = new GameOverLogo(mViewport, atlas);
        mScoreBG = new ScoreBG(mViewport, atlas, mGenerator);
        mReplayButton = new ReplayButton(mViewport, atlas) {
            @Override
            public void press() {
                replayButtonPress();
            }
        };
        mHomeButton = new HomeButton(mViewport, atlas) {
            @Override
            public void press() {
                homeButtonPress();
            }
        };

        mProblem = new Problem();
        mAnswerSet = new AnswerSet();

        mBatch = new SpriteBatch();
        FreeTypeFontGenerator.setMaxTextureSize(4096);
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void resize(int width, int height) {
        mViewport.update(width, height);

        Gdx.app.log("Star size", "" + mStars.size);

        mLogo.init();
        mNormalButton.init();
        mHardButton.init();

        mTitleUI.addActor(mLogo);
        mTitleUI.addActor(mNormalButton);
        mTitleUI.addActor(mHardButton);

        for (int i = 0; i < mStars.size; ++i) {
            mStars.get(i).init();
            mMainStage.addActor(mStars.get(i));
        }

        for (int i = 0; i < 4; ++i) {
            mRockets.get(i).init();
            mAliens.get(i).init();
            mMainStage.addActor(mRockets.get(i));
            mMainStage.addActor(mAliens.get(i));
        }

        mGameOverLogo.init();
        mScoreBG.init();
        mHomeButton.init();
        mReplayButton.init();

        mGameOverUI.addActor(mGameOverLogo);
        mGameOverUI.addActor(mScoreBG);
        mGameOverUI.addActor(mHomeButton);
        mGameOverUI.addActor(mReplayButton);

        mProblemParam.size = (int) (0.075f * mViewport.getScreenHeight());
        mProblemParam.color = Color.WHITE;
        mProblemFont = mGenerator.generateFont(mProblemParam);

        mAnswerParam.size = (int) (0.06f * mViewport.getScreenHeight());
        mAnswerParam.color = Color.WHITE;
        mAnswerFont = mGenerator.generateFont(mAnswerParam);

        mScoreParam.size = (int) (0.04f * mViewport.getScreenHeight());
        mScoreParam.color = new Color(0.224f, 1f, .078f, 1);
        mScoreFont = mGenerator.generateFont(mScoreParam);

        mScore = 0;

        Gdx.input.setInputProcessor(mTitleUI);
    }

    @Override
    public void render(float delta) {
        mViewport.apply(true);

        if (mCheckAlienPositions) {
            checkAlienPosition();
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (Gdx.input.getInputProcessor().equals(mMainStage)) {
            mBatch.begin();
            drawProblem();
            drawAnswers();
            mBatch.end();
        }


        mMainStage.act(delta);
        mMainStage.draw();

        if (Gdx.input.getInputProcessor().equals(mGameOverUI)) {
            mGameOverUI.act(delta);
            mGameOverUI.draw();
        }

        if (Gdx.input.getInputProcessor().equals(mTitleUI)) {
            mTitleUI.act(delta);
            mTitleUI.draw();
        }
    }

    @Override
    public void dispose() {
        mTitleUI.dispose();
        mMainStage.dispose();
        mGameOverUI.dispose();
        mBatch.dispose();
        mProblemFont.dispose();
        mGenerator.dispose();
    }

    private void checkAlienPosition() {
        if (mAliens.get(0).getY() >= mRockets.get(0).getY() + mRockets.get(0).getHeight() * (20f / 52f) - mAliens.get(0).getHeight()) {
            mCheckAlienPositions = false;
            gameOverSequence();
        }
    }

    private void drawProblem() {
        mLayout.setText(mProblemFont, mProblem.toString());
        mProblemFont.draw(mBatch, mLayout, mViewport.getScreenWidth() / 2f - mLayout.width / 2f, mViewport.getScreenHeight() * 0.36f);
    }

    private void drawAnswers() {
        for (int i = 0; i < 4; ++i) {
            mLayout.setText(mAnswerFont, Integer.toHexString(mAnswerSet.getAnswerSet().get(i)).toUpperCase());
            mAnswerFont.draw(mBatch, mLayout,
                    (2 * i + 1f) * mViewport.getScreenWidth() / 8f - mLayout.width / 2f,
                    mRockets.get(i).getY() + 1.075f * mRockets.get(i).getHeight() + mLayout.height);
        }

        mLayout.setText(mScoreFont, "" + mScore);
        mScoreFont.draw(mBatch, mLayout, mViewport.getScreenWidth() / 2f - mLayout.width / 2f, mViewport.getScreenHeight() * 0.26f);
    }

    private void gameOverSequence() {
        if (mHardMode) {
            if (mScore > mData.getInteger("hardHigh", 0)) {
                mData.putInteger("hardHigh", mScore);
                mData.flush();
            }
            mScoreBG.setScores(mScore, mData.getInteger("hardHigh", 0));
        } else {
            if (mScore > mData.getInteger("normalHigh", 0)) {
                mData.putInteger("normalHigh", mScore);
                mData.flush();
            }
            mScoreBG.setScores(mScore, mData.getInteger("normalHigh", 0));
        }

        for (int i = 0; i < 4; ++i) {
            mRockets.get(i).setTouchable(Touchable.disabled);
            mAliens.get(i).clearActions();
            mRockets.get(i).addAction(fadeOut(0.25f));
            mAliens.get(i).addAction(fadeOut(0.25f));
        }

        mRockets.get(0).addAction(delay(0.25f, run(new Runnable() {
            @Override
            public void run() {
                Gdx.input.setInputProcessor(mGameOverUI);
            }
        })));

        mGameOverLogo.addAction(delay(0.3f, run(new Runnable() {
            @Override
            public void run() {
                mGameOverLogo.addAction(moveTo(mViewport.getScreenWidth() / 2 - mGameOverLogo.getWidth() / 2, mViewport.getScreenHeight() * 0.4f + 1.03f * mScoreBG.getHeight(), 0.35f));
                mGameOverLogo.addAction(fadeIn(0.35f));
            }
        })));

        mScoreBG.addAction(delay(0.3f, moveTo(mScoreBG.getX(), mViewport.getScreenHeight() * 0.4f, 0.35f)));

        mHomeButton.addAction(delay(0.3f, run(new Runnable() {
            @Override
            public void run() {
                mHomeButton.setTouchable(Touchable.enabled);
                mHomeButton.addAction(moveTo(mViewport.getScreenWidth() / 8f, mViewport.getScreenHeight() * 0.4f - 1.1f * mHomeButton.getHeight(), 0.35f));
                mHomeButton.addAction(fadeIn(0.35f));
            }
        })));

        mReplayButton.addAction(delay(0.3f, run(new Runnable() {
            @Override
                public void run() {
                    mReplayButton.setTouchable(Touchable.enabled);
                    mReplayButton.addAction(moveTo(mViewport.getScreenWidth() / 8f * 7f - mReplayButton.getWidth(), mViewport.getScreenHeight() * 0.4f - 1.1f * mReplayButton.getHeight(), 0.35f));
                    mReplayButton.addAction(fadeIn(0.35f));
                }
        })));
    }

    private void hardButtonPress() {
        mHardButton.addAction(sequence(run(new Runnable() {
                    @Override
                    public void run() {
                        mNormalButton.setTouchable(Touchable.disabled);
                        mHardButton.setTouchable(Touchable.disabled);
                    }
                }),
                moveBy(0, -10, 0.1f),
                moveBy(0, 10, 0.1f),
                run(new Runnable() {
                    @Override
                    public void run() {
                        startGameSequence();
                    }
                })));
    }

    private void homeButtonPress() {
        mHomeButton.addAction(sequence(run(new Runnable() {
                    @Override
                    public void run() {
                        mHomeButton.setTouchable(Touchable.disabled);
                    }
                }),
                moveBy(0, -10, 0.1f),
                moveBy(0, 10, 0.1f),
                run(new Runnable() {
                    @Override
                    public void run() {
                        mGameOverLogo.addAction(moveTo(mViewport.getScreenWidth() / 2 - mGameOverLogo.getWidth() / 2, mViewport.getScreenHeight() * 0.75f, 0.35f));
                        mGameOverLogo.addAction(fadeOut(0.35f));

                        mScoreBG.addAction(moveTo(mScoreBG.getX(), mViewport.getScreenHeight(), 0.35f));

                        mHomeButton.addAction(moveTo(mViewport.getScreenWidth() / 8f, mViewport.getScreenHeight() * 0.25f, 0.35f));
                        mHomeButton.addAction(fadeOut(0.35f));

                        mReplayButton.addAction(moveTo(mViewport.getScreenWidth() / 8f * 7f - mReplayButton.getWidth(), mViewport.getScreenHeight() * 0.25f, 0.35f));
                        mReplayButton.addAction(fadeOut(0.35f));
                    }
                }),
                delay(0.35f, run(new Runnable() {
                    @Override
                    public void run() {
                        startHomeSequence();
                    }
                }))));
    }

    private void moveAliensBack() {
        for (int i = 0; i < 4; i++) {
            mAliens.get(i).clearActions();
            mAliens.get(i).addAction(moveTo(mAliens.get(i).mInitialX, 0, 0.15f));
        }
    }

    private void normalButtonPress() {
        mNormalButton.addAction(sequence(run(new Runnable() {
                    @Override
                    public void run() {
                        mNormalButton.setTouchable(Touchable.disabled);
                        mHardButton.setTouchable(Touchable.disabled);
                    }
                }),
                moveBy(0, -10, 0.1f),
                moveBy(0, 10, 0.1f),
                run(new Runnable() {
                    @Override
                    public void run() {
                        startGameSequence();
                    }
                })));
    }

    private void resetAlienPosition(float nextDuration) {
        for (int i = 0; i < 4; i++) {
            mAliens.get(i).clearActions();
            mAliens.get(i).addAction(fadeIn(0.25f));
            mAliens.get(i).addAction(sequence(moveTo(mAliens.get(i).mInitialX, 0, 0.15f),
                    moveTo(mAliens.get(i).getX(),
                            mRockets.get(i).getY() + mRockets.get(i).getHeight() * (20f / 52f) - mAliens.get(i).getHeight(), nextDuration)));
        }
    }

    private void replayButtonPress() {
        mReplayButton.addAction(sequence(run(new Runnable() {
                    @Override
                    public void run() {
                        mReplayButton.setTouchable(Touchable.disabled);
                    }
                }),
                moveBy(0, -10, 0.1f),
                moveBy(0, 10, 0.1f),
                run(new Runnable() {
                    @Override
                    public void run() {
                        mGameOverLogo.addAction(moveTo(mViewport.getScreenWidth() / 2 - mGameOverLogo.getWidth() / 2, mViewport.getScreenHeight() * 0.75f, 0.35f));
                        mGameOverLogo.addAction(fadeOut(0.35f));

                        mScoreBG.addAction(moveTo(mScoreBG.getX(), mViewport.getScreenHeight(), 0.35f));

                        mHomeButton.addAction(moveTo(mViewport.getScreenWidth() / 8f, mViewport.getScreenHeight() * 0.25f, 0.35f));
                        mHomeButton.addAction(fadeOut(0.35f));

                        mReplayButton.addAction(moveTo(mViewport.getScreenWidth() / 8f * 7f - mReplayButton.getWidth(), mViewport.getScreenHeight() * 0.25f, 0.35f));
                        mReplayButton.addAction(fadeOut(0.35f));
                    }
                }),
                delay(0.35f, run(new Runnable() {
                    @Override
                    public void run() {
                        Gdx.input.setInputProcessor(mMainStage);
                        resetGameSequence();
                    }
                }))));
    }

    private void resetGameSequence() {
        if (mHardMode) {
            setUpProblem(255);
        } else {
            setUpProblem(15);
        }
        mScore = 0;
        mMainStage.addAction(run(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 4; ++i) {
                    mRockets.get(i).addAction(fadeIn(0.25f));
                    mRockets.get(i).setTouchable(Touchable.enabled);
                }
                if (mHardMode) {
                    resetAlienPosition(15);
                } else {
                    resetAlienPosition(4);
                }
            }
        }));
        mCheckAlienPositions = true;
    }

    private void startGameSequence() {
        mLogo.addAction(fadeOut(0.35f));
        mHardButton.addAction(fadeOut(0.35f));
        mNormalButton.addAction(sequence(fadeOut(0.35f), run(new Runnable() {
            @Override
            public void run() {
                Gdx.input.setInputProcessor(mMainStage);
                for (int i = 0; i < 4; ++i) {
                    mRockets.get(i).addAction(fadeIn(0.35f));
                    mAliens.get(i).addAction(fadeIn(0.35f));
                }
                if (mHardMode) {
                    setUpProblem(255);
                    sendAliensForward(15);
                } else {
                    setUpProblem(15);
                    sendAliensForward(4);
                }
            }
        })));
    }

    private void startHomeSequence() {
        mScore = 0;
        Gdx.input.setInputProcessor(mTitleUI);
        mLogo.reset();
        mNormalButton.reset();
        mHardButton.reset();
        for (int i = 0; i < 4; ++i) {
            mRockets.get(i).reset();
            mAliens.get(i).reset();
        }
        mCheckAlienPositions = true;
    }

    private void sendAliensForward(float duration) {
        for (int i = 0; i < 4; i++) {
            mAliens.get(i).addAction(moveTo(mAliens.get(i).getX(),
                    mRockets.get(i).getY() + mRockets.get(i).getHeight() * (20f / 52f) - mAliens.get(i).getHeight(), duration));
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
}
