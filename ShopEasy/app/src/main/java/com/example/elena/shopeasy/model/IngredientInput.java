package com.example.elena.shopeasy.model;

/**
 * Created by Absolute on 3/12/2018.
 */

public class IngredientInput {

    private String mName;
    private int mFrequency;

    public IngredientInput() {
    }

    public IngredientInput(String mName, int mFrequency) {
        this.mName = mName;
        this.mFrequency = mFrequency;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmFrequency() {
        return mFrequency;
    }

    public void setmFrequency(int mFrequency) {
        this.mFrequency = mFrequency;
    }
}
