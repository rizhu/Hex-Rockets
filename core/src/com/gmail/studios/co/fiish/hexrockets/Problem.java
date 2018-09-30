package com.gmail.studios.co.fiish.hexrockets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

public class Problem {
    private int mSolution, mTerm1, mTerm2;

    private ProblemType mProblemType;

    @Override
    public String toString() {
        if (mProblemType == ProblemType.Addition) {
            return Integer.toHexString(mTerm1).toUpperCase() + " + " + Integer.toHexString(mTerm2).toUpperCase();
        } else if (mProblemType == ProblemType.Subtraction) {
            return Integer.toHexString(mTerm1).toUpperCase() + " - " + Integer.toHexString(mTerm2).toUpperCase();
        } else {
            return "Error";
        }
    }

    public void generateProblem(int bound) {
        if (MathUtils.random() > 0.5) {
            mProblemType = ProblemType.Addition;
            mSolution = MathUtils.random(2, bound);

            mTerm1 = MathUtils.random(1, mSolution);
            mTerm2 = mSolution - mTerm1;
        } else {
            mProblemType = ProblemType.Subtraction;
            mSolution = MathUtils.random(1, bound - 1);

            mTerm1 = MathUtils.random(mSolution + 1, bound);
            mTerm2 = mTerm1 - mSolution;
        }
        Gdx.app.log("Problem Type", "" + mProblemType);
    }

    public int getSolution() {
        return mSolution;
    }
}
