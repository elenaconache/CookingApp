package com.example.elena.shopeasy.modelview;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.elena.shopeasy.fragments.CategoriesFragment;
import com.example.elena.shopeasy.model.Ingredient;
import com.example.elena.shopeasy.model.Recipe;
import com.example.elena.shopeasy.ui.MainActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Retrofit;

/**
 * Created by User on 31/12/2017.
 */

public class RecipeViewModel extends ViewModel {
    private MediatorLiveData<List<Recipe>> mRecipeData;
    private RecipeInt mRecipeInt;

    // No argument constructor
    public RecipeViewModel() {//passed from activity where it's injected
        mRecipeData = new MediatorLiveData<>();
        mRecipeInt = new RecipeIntImpl();

    }

    public void setRetrofit(Retrofit retrofit){
        //set retrofit here.. for categint and call the method in mainactivity after instantiating
        mRecipeInt = new RecipeIntImpl(retrofit);
    }

    @NonNull
    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeData;
    }

    public void loadRecipes(final Context context) {
        mRecipeData.addSource(
                mRecipeInt.getRecipes(context), new Observer<List<Recipe>>() {
                    @Override
                    public void onChanged(@Nullable List<Recipe> recipes) {
                        if (recipes == null){
                            Toast.makeText(context, "The recipes list is null.",
                                    Toast.LENGTH_SHORT).show();
                            Log.v("recipes","null");

                        }else if (recipes.size()==0) Toast.makeText(context,
                                "The recipes list is empty.", Toast.LENGTH_SHORT).show();
                        if (recipes != null && recipes.size()>0){
                            mRecipeData.setValue(recipes);
                            handleResponse(recipes, context, new MainActivity());
                        }else{
                            handleError(context,recipes,null);
                        }
                    }
                }
        );
    }

    static void handleResponse(final List<Recipe> data, final Context context, Activity activity) {
        Set<String> ingredients= new HashSet<>();
        for (Recipe recipe: data){
            List<Ingredient> ingredientData = recipe.getIngredients();
            for (Ingredient ingredient: ingredientData){
                ingredients.add(ingredient.getIngredient());
            }
        }
        Ingredient.setmUsedIngredients(ingredients);
        CategoriesFragment.setupData(context);
        MainActivity.setupSuggestions(data);
    }



    private static void handleError(final Context context, List<Recipe> recipes, Throwable t) {
   //     MainActivity.stopLoading();
        if (recipes == null){
            Toast.makeText(context, "The recipes list is null.", Toast.LENGTH_SHORT).show();
            Log.v("recipes","null");

        }else if (recipes.size()==0) Toast.makeText(context, "The recipes list is empty.", Toast.LENGTH_SHORT).show();
        else Toast.makeText(context, "An error occured.", Toast.LENGTH_SHORT).show();
        // Log.v("recipe","error "+t+" recipes");
    }

    public RecipeInt getmCategInt() {
        return mRecipeInt;
    }
}
