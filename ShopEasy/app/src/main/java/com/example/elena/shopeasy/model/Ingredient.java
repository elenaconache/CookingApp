package com.example.elena.shopeasy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Set;

import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by Absolute on 2/1/2018.
 */

public class Ingredient extends RealmObject implements Parcelable  {
    private double quantity;
    private String measure;
    private String ingredient;

    private static Set<String> mUsedIngredients;

    public Ingredient(){
    }

    public Ingredient(int quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    protected Ingredient(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public static Set<String> getmUsedIngredients() {
        return mUsedIngredients;
    }

    public static void setmUsedIngredients(Set<String> mUsedIngredientss) {
        mUsedIngredients = mUsedIngredientss;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }
}
