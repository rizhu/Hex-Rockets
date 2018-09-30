package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class AnswerSet {
    private Array<Integer> mSet;
    private Array<Integer> mAnswerList;

    private int mAnswerIndex = -1;
    private int mTempAnswer = -1;
    private int mLower, mUpper;

    private boolean mEligibleAnswer = false;

    public AnswerSet() {
        mSet = new Array<Integer>(4);
        mAnswerList = new Array<Integer>();
    }

    public void generateAnswers(int solution, int bound, int range) {
        mAnswerIndex = MathUtils.random(0, 3);
        mAnswerList.clear();
        mSet.clear();
        Gdx.app.log("Answer Index", "" + mAnswerIndex);

        if (solution - range < 0) {
            mLower = 0;
        } else {
            mLower = solution - range;
        }

        if (solution + range > bound) {
            mUpper = bound;
        } else {
            mUpper = solution + range;
        }

        for (; mLower <= mUpper; ++mLower) {
            mAnswerList.add(mLower);
        }
        mAnswerList.shuffle();

        for (int i = 0; i < 4; ++i) {
            if (i == mAnswerIndex) {
                mSet.add(solution);
            } else {
                if (mAnswerList.get(i) != solution) {
                    mSet.add(mAnswerList.get(i));
                } else {
                    mAnswerList.removeIndex(i);
                    mSet.add(mAnswerList.get(i));
                }
            }
        }
    }

    public int getAnswerIndex() {
        return mAnswerIndex;
    }

    public Array<Integer> getAnswerSet() {
        return mSet;
    }

}
