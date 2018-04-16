package com.example.elena.shopeasy.modelview;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.example.elena.shopeasy.model.Recipe;

import java.util.List;

/**
 * Created by User on 31/12/2017.
 */

public interface RecipeInt {
    MutableLiveData<List<Recipe>> getRecipes(Context context);
}
