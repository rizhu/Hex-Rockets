package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
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

    private Stage mTitleUI, mCreditsUI, mHelpUI, mMainStage, mGameOverUI;

    private HexRocketsLogo mLogo;
    private NormalButton mNormalButton;
    private HardButton mHardButton;
    private CreditsButton mCreditsButton;
    private SoundButton mSoundButton;
    private HelpButton mHelpButton;

    private CreditsBG mCreditsBG;
    private BackLeftButton mBackLeftButton;

    private Array<BasicHelpPrompt> mHelpPrompts;
    private LeftButton mLeftButton;
    private RightButton mRightButton;
    private BackRightButton mBackRightButton;

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

    private Sound mCorrect, mDeath, mHighScore, mStart;

    private int mScore = -1;
    private boolean mCheckAlienPositions = true;
    private boolean mHardMode = false;

    public GameScreen(TextureAtlas atlas, AssetManager manager) {
        mViewport = new ScreenViewport();
        mData = Gdx.app.getPreferences("Data");

        mGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        mProblemParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mAnswerParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mScoreParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mLayout = new GlyphLayout();

        mTitleUI = new Stage(mViewport);
        mCreditsUI = new Stage(mViewport);
        mHelpUI = new Stage(mViewport);
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
        mCreditsButton = new CreditsButton(mViewport, atlas) {
            @Override
            public void press() {
                creditsButtonPress();
            }
        };
        mSoundButton = new SoundButton(mViewport, atlas, mData.getBoolean("soundOn", true)) {
            @Override
            public void press() {
                soundButtonPress();
            }
        };
        mHelpButton = new HelpButton(mViewport, atlas) {
            @Override
            public void press() {
                helpButtonPress();
            }
        };

        mCreditsBG = new CreditsBG(mViewport, atlas);
        mBackLeftButton = new BackLeftButton(mViewport, atlas) {
            @Override
            public void press() {
                backLeftButtonPress();
            }
        };

        mHelpPrompts = new Array<BasicHelpPrompt>(6);
        for (int i = 0; i < 6; ++i) {
            mHelpPrompts.add(new BasicHelpPrompt(atlas.findRegion("helpPrompt", i + 1), mViewport));
        }
        mLeftButton = new LeftButton(mViewport, atlas) {
            @Override
            public void press() {
                leftButtonPress();
            }
        };
        mRightButton = new RightButton(mViewport, atlas) {
            @Override
            public void press() {
                rightButtonPress();
            }
        };
        mBackRightButton = new BackRightButton(mViewport, atlas) {
            @Override
            public void press() {
                backRightButtonPress();
            }
        };

        mRockets = new Array<Rocket>(4);
        mAliens = new Array<Alien>(4);
        mStars = new Array<Star>();

        for (int i = 0; i < MathUtils.random(8, 15); ++i) {     //Generates 8-15 stars in random positions in the background
            mStars.add(new Star(mViewport, atlas));
        }

        for (int i = 0; i < 4; ++i) {
            mRockets.add(new Rocket(mViewport, atlas, i) {
                @Override
                public void newProblem() {
                    ++mScore;
                    mCorrect.play(mData.getFloat("volume", 1.0f));
                    if (mHardMode) {
                        setUpProblem(255);  //Hard Mode generates two terms for the problem between 0 and FF
                        for (int i = 0; i < 4; i++) {
                            resetAlienPosition(30f / (mScore + 3f) + 5);    //The time limit for the 1st question in Hard Mode is 15 seconds and it steadily drops off, approaching 5 seconds
                        }
                    } else {
                        setUpProblem(15);   //Normal Mode generates two terms for the problem between 0 and F
                        for (int i = 0; i < 4; i++) {
                            resetAlienPosition(15f / (mScore + 1.714f) + 1.25f);    //The time limit for the 1st question in Normal Mode is 10 seconds and it steadily drops off, approaching 1.25 seconds
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

        mCorrect = manager.get("correct.wav", Sound.class);
        mDeath = manager.get("death.wav", Sound.class);
        mHighScore = manager.get("highScore.wav", Sound.class);
        mStart = manager.get("start.wav", Sound.class);

        FreeTypeFontGenerator.setMaxTextureSize(4096);  //Allows for larger font size
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
    }   //Clears screen to black every frame

    @Override
    public void resize(int width, int height) {
        mViewport.update(width, height, true);

        mLogo.init();
        mNormalButton.init();
        mHardButton.init();
        mCreditsButton.init();
        mSoundButton.init();
        mHelpButton.init();

        mTitleUI.addActor(mLogo);
        mTitleUI.addActor(mNormalButton);
        mTitleUI.addActor(mHardButton);
        mTitleUI.addActor(mCreditsButton);
        mTitleUI.addActor(mSoundButton);
        mTitleUI.addActor(mHelpButton);

        mCreditsBG.init();
        mBackLeftButton.init();

        mCreditsUI.addActor(mCreditsBG);
        mCreditsUI.addActor(mBackLeftButton);

        for (int i = 0; i < 6; ++i) {
            mHelpPrompts.get(i).init();
            mHelpUI.addActor(mHelpPrompts.get(i));
        }
        mLeftButton.init();
        mRightButton.init();
        mBackRightButton.init();

        mHelpUI.addActor(mLeftButton);
        mHelpUI.addActor(mRightButton);
        mHelpUI.addActor(mBackRightButton);

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

        mProblemParam.size = (int) (0.075f * mViewport.getScreenHeight());  //Sets up font for problem
        mProblemParam.color = Color.WHITE;
        mProblemFont = mGenerator.generateFont(mProblemParam);

        mAnswerParam.size = (int) (0.06f * mViewport.getScreenHeight());    //Sets up font for answer choices
        mAnswerParam.color = Color.WHITE;
        mAnswerFont = mGenerator.generateFont(mAnswerParam);

        mScoreParam.size = (int) (0.04f * mViewport.getScreenHeight());     //Sets up font for score
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

        if (Gdx.input.getInputProcessor().equals(mCreditsUI)) {
            mCreditsUI.act(delta);
            mCreditsUI.draw();
        }

        if (Gdx.input.getInputProcessor().equals(mHelpUI)) {
            mHelpUI.act(delta);
            mHelpUI.draw();
        }
    }

    @Override
    public void dispose() {
        mTitleUI.dispose();
        mCreditsUI.dispose();
        mHelpUI.dispose();
        mMainStage.dispose();
        mGameOverUI.dispose();
        mScoreBG.dispose();
        mBatch.dispose();
        mProblemFont.dispose();
        mGenerator.dispose();

        mCorrect.dispose();
        mDeath.dispose();
        mHighScore.dispose();
        mStart.dispose();
    }

    private void backLeftButtonPress() {    //Actions to be executed when backLeftButton is pressed
        mBackLeftButton.addAction(sequence(run(new Runnable() {
                    @Override
                    public void run() {
                        mBackLeftButton.setTouchable(Touchable.disabled);   //Stops spam of button
                    }
                }),
                moveBy(0, -10, 0.1f),   //Animation of button moving down and up
                moveBy(0, 10, 0.1f),
                run(new Runnable() {
                    @Override
                    public void run() {     //Closes Credits UI and sets up Title UI
                        mBackLeftButton.addAction(fadeOut(0.35f));
                        mCreditsBG.addAction(fadeOut(0.35f));
                        Gdx.input.setInputProcessor(mTitleUI);
                        mTitleUI.addAction(fadeIn(0.35f));
                        mNormalButton.setTouchable(Touchable.enabled);
                        mHardButton.setTouchable(Touchable.enabled);
                        mCreditsButton.setTouchable(Touchable.enabled);
                        mSoundButton.setTouchable(Touchable.enabled);
                        mHelpButton.setTouchable(Touchable.enabled);
                    }
                })));
    }

    private void backRightButtonPress() {   //Actions to be executed when backRightButton is pressed
        mBackRightButton.addAction(sequence(run(new Runnable() {
                    @Override
                    public void run() {     //Stops spam of buttons on Help UI
                        mLeftButton.setTouchable(Touchable.disabled);
                        mRightButton.setTouchable(Touchable.enabled);
                        mBackRightButton.setTouchable(Touchable.disabled);
                    }
                }),
                moveBy(0, -10, 0.1f),   //Animation of button moving down and up
                moveBy(0, 10, 0.1f),
                run(new Runnable() {
                    @Override
                    public void run() {     //Closes Help UI and sets up Title UI
                        mBackRightButton.addAction(fadeOut(0.35f));
                        mLeftButton.addAction(fadeOut(0.35f));
                        mRightButton.addAction(fadeOut(0.35f));
                        for (int i = 0; i < 6; ++i) {
                            mHelpPrompts.get(i).addAction(fadeOut(0.35f));
                            mHelpPrompts.get(i).reset();
                        }
                        Gdx.input.setInputProcessor(mTitleUI);
                        mTitleUI.addAction(fadeIn(0.35f));
                        mNormalButton.setTouchable(Touchable.enabled);
                        mHardButton.setTouchable(Touchable.enabled);
                        mCreditsButton.setTouchable(Touchable.enabled);
                        mSoundButton.setTouchable(Touchable.enabled);
                        mHelpButton.setTouchable(Touchable.enabled);
                    }
                })));
    }

    private void checkAlienPosition() {     //Checks position of Aliens in order to check if the Aliens have reached the Rockets
        if (mAliens.get(0).getY() >= mRockets.get(0).getY() + mRockets.get(0).getHeight() * (20f / 52f) - mAliens.get(0).getHeight()) {
            mCheckAlienPositions = false;
            gameOverSequence();
        }
    }

    private void creditsButtonPress() {     //Actions to be executed when creditsButton is pressed
        mCreditsButton.addAction(sequence(run(new Runnable() {
                    @Override
                    public void run() {     //Stops spam of other buttons on Title UI
                        mNormalButton.setTouchable(Touchable.disabled);
                        mHardButton.setTouchable(Touchable.disabled);
                        mCreditsButton.setTouchable(Touchable.disabled);
                        mSoundButton.setTouchable(Touchable.disabled);
                        mHelpButton.setTouchable(Touchable.disabled);
                    }
                }),
                moveBy(0, -10, 0.1f),   //Animation of button moving up and down
                moveBy(0, 10, 0.1f)
        ));
        mTitleUI.addAction(sequence(fadeOut(0.35f),
                run(new Runnable() {
                    @Override
                    public void run() {     //Sets up Credits UI
                        Gdx.input.setInputProcessor(mCreditsUI);
                        mCreditsBG.addAction(fadeIn(0.35f));
                        mBackLeftButton.addAction(fadeIn(0.35f));
                        mBackLeftButton.setTouchable(Touchable.enabled);
                    }
                })));
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

    private void gameOverSequence() {   //Actions to be executed when player loses
        mDeath.play(mData.getFloat("volume", 1.0f));
        if (mHardMode) {     //Saves high score for Hard Mode
            if (mScore > mData.getInteger("hardHigh", 0)) {
                mData.putInteger("hardHigh", mScore);
                mData.flush();
                mHighScore.play(mData.getFloat("volume", 1.0f));
            }
            mScoreBG.sendData(mScore, mData.getInteger("hardHigh", 0), mHardMode);
        } else {     //Saves high score for Normal Mode
            if (mScore > mData.getInteger("normalHigh", 0)) {
                mData.putInteger("normalHigh", mScore);
                mData.flush();
                mHighScore.play(mData.getFloat("volume", 1.0f));
            }
            mScoreBG.sendData(mScore, mData.getInteger("normalHigh", 0), mHardMode);
        }

        for (int i = 0; i < 4; ++i) {   //Removes Rockets and Aliens
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
            public void run() {     //Game Over Logo setup
                mGameOverLogo.addAction(moveTo(mViewport.getScreenWidth() / 2 - mGameOverLogo.getWidth() / 2, mViewport.getScreenHeight() * 0.4f + 1.03f * mScoreBG.getHeight(), 0.35f));
                mGameOverLogo.addAction(fadeIn(0.35f));
            }
        })));

        mScoreBG.addAction(delay(0.3f, moveTo(mScoreBG.getX(), mViewport.getScreenHeight() * 0.4f, 0.35f)));    //Score menu setup

        mHomeButton.addAction(delay(0.3f, run(new Runnable() {
            @Override
            public void run() {     //Home Button setup
                mHomeButton.setTouchable(Touchable.enabled);
                mHomeButton.addAction(moveTo(mViewport.getScreenWidth() / 8f, mViewport.getScreenHeight() * 0.4f - 1.1f * mHomeButton.getHeight(), 0.35f));
                mHomeButton.addAction(fadeIn(0.35f));
            }
        })));

        mReplayButton.addAction(delay(0.3f, run(new Runnable() {
            @Override
                public void run() {     //Replay Button setup
                    mReplayButton.setTouchable(Touchable.enabled);
                    mReplayButton.addAction(moveTo(mViewport.getScreenWidth() / 8f * 7f - mReplayButton.getWidth(), mViewport.getScreenHeight() * 0.4f - 1.1f * mReplayButton.getHeight(), 0.35f));
                    mReplayButton.addAction(fadeIn(0.35f));
                }
        })));
    }

    private void hardButtonPress() {    //Actions executed when hardButton is pressed
        mHardButton.addAction(sequence(run(new Runnable() {
                    @Override
                    public void run() {     //Spam control
                        mNormalButton.setTouchable(Touchable.disabled);
                        mHardButton.setTouchable(Touchable.disabled);
                        mCreditsButton.setTouchable(Touchable.disabled);
                        mSoundButton.setTouchable(Touchable.disabled);
                        mHelpButton.setTouchable(Touchable.disabled);
                    }
                }),
                moveBy(0, -10, 0.1f),   //Press animation
                moveBy(0, 10, 0.1f),
                run(new Runnable() {
                    @Override
                    public void run() {
                        startGameSequence();
                    }
                })));
        mStart.play(mData.getFloat("volume", 1.0f));
    }

    private void helpButtonPress() {    //Actions executed when helpButton is pressed
        mHelpButton.addAction(sequence(run(new Runnable() {
                    @Override
                    public void run() {     //Spam control
                        mNormalButton.setTouchable(Touchable.disabled);
                        mHardButton.setTouchable(Touchable.disabled);
                        mCreditsButton.setTouchable(Touchable.disabled);
                        mSoundButton.setTouchable(Touchable.disabled);
                        mHelpButton.setTouchable(Touchable.disabled);
                    }
                }),
                moveBy(0, -10, 0.1f),   //Spam control
                moveBy(0, 10, 0.1f)
                ));
        mTitleUI.addAction(sequence(fadeOut(0.35f),
                run(new Runnable() {
                    @Override
                    public void run() {     //Help UI setup
                        Gdx.input.setInputProcessor(mHelpUI);
                        mHelpPrompts.get(0).enter();
                        mLeftButton.addAction(fadeIn(0.35f));
                        mRightButton.addAction(fadeIn(0.35f));
                        mBackRightButton.addAction(fadeIn(0.35f));
                        mLeftButton.setTouchable(Touchable.enabled);
                        mRightButton.setTouchable(Touchable.enabled);
                        mBackRightButton.setTouchable(Touchable.enabled);
                    }
                })));
    }

    private void homeButtonPress() {    //Actions executed when homeButton is pressed
        mHomeButton.addAction(sequence(run(new Runnable() {
                    @Override
                    public void run() {     //Spam control
                        mHomeButton.setTouchable(Touchable.disabled);
                        mReplayButton.setTouchable(Touchable.disabled);
                    }
                }),
                moveBy(0, -10, 0.1f),   //Press animation
                moveBy(0, 10, 0.1f),
                run(new Runnable() {
                    @Override
                    public void run() {     //Title UI setup
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

    private void leftButtonPress() {    //Actions executed when leftButton is pressed
        mLeftButton.addAction(sequence(
                moveBy(0, -10, 0.1f),   //Press animation
                moveBy(0, 10, 0.1f),
                run(new Runnable() {
                    @Override
                    public void run() {     //Help menu goes to previous slide
                        for (int i = 0; i < 6; ++i) {
                            if (mHelpPrompts.get(i).isActive() && i - 1 > -1 && mHelpPrompts.get(i - 1) != null) {
                                mHelpPrompts.get(i).exitToRight();
                                mHelpPrompts.get(i - 1).enter();
                                break;
                            }
                        }
                    }
                })));
    }

    private void normalButtonPress() {      //Actions executed when normalButton is pressed
        mNormalButton.addAction(sequence(run(new Runnable() {
                    @Override
                    public void run() {     //Spam control
                        mNormalButton.setTouchable(Touchable.disabled);
                        mHardButton.setTouchable(Touchable.disabled);
                        mCreditsButton.setTouchable(Touchable.disabled);
                        mSoundButton.setTouchable(Touchable.disabled);
                        mHelpButton.setTouchable(Touchable.disabled);
                    }
                }),
                moveBy(0, -10, 0.1f),   //Press animation
                moveBy(0, 10, 0.1f),
                run(new Runnable() {
                    @Override
                    public void run() {
                        startGameSequence();
                    }
                })));
        mStart.play(mData.getFloat("volume", 1.0f));
    }

    private void resetAlienPosition(float nextDuration) {   //Resets the position of the Aliens and sets the time limit of the next problem
        for (int i = 0; i < 4; i++) {
            mAliens.get(i).clearActions();
            mAliens.get(i).addAction(fadeIn(0.25f));
            mAliens.get(i).addAction(sequence(moveTo(mAliens.get(i).mInitialX, 0, 0.15f),
                    moveTo(mAliens.get(i).getX(),
                            mRockets.get(i).getY() + mRockets.get(i).getHeight() * (20f / 52f) - mAliens.get(i).getHeight(), nextDuration)));
        }
    }

    private void replayButtonPress() {      //Actions executed when replayButton is pressed
        mReplayButton.addAction(sequence(run(new Runnable() {
                    @Override
                    public void run() {     //Spam control
                        mHomeButton.setTouchable(Touchable.disabled);
                        mReplayButton.setTouchable(Touchable.disabled);
                    }
                }),
                moveBy(0, -10, 0.1f),   //Press animation
                moveBy(0, 10, 0.1f),
                run(new Runnable() {
                    @Override
                    public void run() {     //New game set up
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

    private void resetGameSequence() {      //Resets game
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
                    resetAlienPosition(10);
                }
                mCheckAlienPositions = true;
            }
        }));
    }

    private void rightButtonPress() {   //Actions executed when rightButton is pressed
        mRightButton.addAction(sequence(
                moveBy(0, -10, 0.1f),   //Press animation
                moveBy(0, 10, 0.1f),
                run(new Runnable() {
                    @Override
                    public void run() {     //Advances Help menu slide
                        for (int i = 0; i < 6; ++i) {
                            if (mHelpPrompts.get(i).isActive() && i + 1 < 6 && mHelpPrompts.get(i + 1) != null) {
                                mHelpPrompts.get(i).exitToLeft();
                                mHelpPrompts.get(i + 1).enter();
                                break;
                            }
                        }
                    }
                })));
    }

    private void soundButtonPress() {   //Actions executed when soundButton is pressed
        mSoundButton.addAction(sequence(
                moveBy(0, -10, 0.1f),   //Press animation
                moveBy(0, 10, 0.1f),
                run(new Runnable() {
                    @Override
                    public void run() {
                        if (mData.getBoolean("soundOn", true)) {
                            mSoundButton.setOff();
                            mData.putBoolean("soundOn", false);
                            mData.putFloat("volume", 0f);
                        } else {
                            mSoundButton.setOn();
                            mData.putBoolean("soundOn", true);
                            mData.putFloat("volume", 1f);
                        }
                        mData.flush();
                    }
                })));
    }

    private void startGameSequence() {      //Starts game
        mLogo.addAction(fadeOut(0.35f));    //Title UI removal
        mHardButton.addAction(fadeOut(0.35f));
        mCreditsButton.addAction(fadeOut(0.35f));
        mSoundButton.addAction(fadeOut(0.35f));
        mHelpButton.addAction(fadeOut(0.35f));
        mNormalButton.addAction(sequence(fadeOut(0.35f), run(new Runnable() {
            @Override
            public void run() {     //Game setup
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
                    sendAliensForward(10);
                }
            }
        })));
    }

    private void startHomeSequence() {      //Actions to be executed for Home menu setup
        mScore = 0;
        Gdx.input.setInputProcessor(mTitleUI);
        mLogo.reset();
        mNormalButton.reset();
        mHardButton.reset();
        mCreditsButton.reset();
        mSoundButton.reset();
        mHelpButton.reset();
        for (int i = 0; i < 4; ++i) {
            mRockets.get(i).reset();
            mAliens.get(i).reset();
        }
        mCheckAlienPositions = true;
    }

    private void sendAliensForward(float duration) {    //Sends Aliens forward for first problem in a game sequence
        for (int i = 0; i < 4; i++) {
            mAliens.get(i).addAction(moveTo(mAliens.get(i).getX(),
                    mRockets.get(i).getY() + mRockets.get(i).getHeight() * (20f / 52f) - mAliens.get(i).getHeight(), duration));
        }
    }

    private void setUpProblem(int bound) {      //Sets up problem
        mProblem.generateProblem(bound);

        mAnswerSet.generateAnswers(mProblem.getSolution(), bound, 32);

        for (int i = 0; i < 4; ++i) {
            if (i == mAnswerSet.getAnswerIndex()) {
                mRockets.get(i).setAnswerState(AnswerState.Correct);
            } else {
                mRockets.get(i).setAnswerState(AnswerState.Incorrect);
            }
        }
    }
}
