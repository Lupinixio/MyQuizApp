package com.example.leonardocavalera.myquizapp;

/**
 * Created by Leonardo Cavalera on 18/03/2017.
 */

public class Quiz {

    private String mQuestionText;
    private String[] mAnswersText;
    private boolean[] mSolutionBoolean;
    private String[] mSolutionText;
    private String mType;

    Quiz(String q, String[] a, boolean[] s, String t) {
        mQuestionText = q;
        mAnswersText = a;
        mSolutionBoolean = s;
        mType = t;
    }

    Quiz(String q, String[] a, String[] s, String t) {
        mQuestionText = q;
        mAnswersText = a;
        mSolutionText = s;
        mType = t;
    }

    public String getQuestionText() {
        return mQuestionText;
    }

    public String[] getAnswersText() {
        return mAnswersText;
    }

    public int getSolutionLength() {
        switch (mType) {
            case "inputText":
                return mSolutionText.length;
            default:
                return mSolutionBoolean.length;
        }
    }

    public String getQuestionType() {
        return mType;
    }

    public boolean answerChecker(boolean[] b, String s) {

        boolean bool = true;
        switch (mType) {
            case "inputText":
                for (int n = 0; n < mSolutionText.length; n++) {
                    String control = mSolutionText[n];
                    if (s.toLowerCase().equals(control)) {
                        return true;
                    }
                }
                break;
            default:
                for (int i = 0; i < b.length; i++) {
                    if (b[i] != mSolutionBoolean[i]) {
                        bool = false;
                    }

                }
                break;
        }


        return bool;
    }

}
