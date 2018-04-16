package com.example.elena.shopeasy.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.adapters.RecipesGridAdapter;
import com.example.elena.shopeasy.model.Ingredient;
import com.example.elena.shopeasy.model.Recipe;
import com.example.elena.shopeasy.realm.RealmController;
import com.example.elena.shopeasy.realm.RecipeInput;
import com.example.elena.shopeasy.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class RecipesActivity extends AppCompatActivity {

    public static final String EXTRA_INGREDIENT="ingredient";
    private List<Recipe> mRecipes;
    private List<Recipe> mQuerriedRecipes;
    private RecipesGridAdapter mRecipesAdapter;
    private TextView mIngredientTextView;
    private GridView mRecipesGrid;
    public static final String EXTRA_RECIPE = "recipe";
    public static final String EXTRA_RECIPE_PARTIAL_NAME = "partial-recipe-name";

    private LinearLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        findViews();
        setupColors();

        mRecipes = MainActivity.getRecipes();
        mQuerriedRecipes = new ArrayList<>();

        String ingredient  = getIntent().getStringExtra(EXTRA_INGREDIENT);
        String partialName = getIntent().getStringExtra(EXTRA_RECIPE_PARTIAL_NAME);

        PrefUtils prefUtils = new PrefUtils(this);
        Log.wtf("search option",prefUtils.getSearchOption()+"option,"+partialName+mRecipes.size());


        if (ingredient!=null && ingredient.length()>0){
            mIngredientTextView.setText(Html.fromHtml("Recipes with: <b><i>"+ingredient+"</i></b>"));
            for (Recipe recipe: mRecipes){
                List<Ingredient> ingredients = recipe.getIngredients();
                for (Ingredient ingredient1: ingredients){
                    if (ingredient1.getIngredient().equals(ingredient)){
                        mQuerriedRecipes.add(recipe);
                        break;
                    }
                }
            }
        }else if (partialName!=null && partialName.length()>0){
            mIngredientTextView.setText(Html.fromHtml("Search results for: <b><i>"+partialName+"</i></b>"));

            if (prefUtils.getSearchOption().toLowerCase().equals("by recipe name")){
                for (Recipe recipe: mRecipes){
                    if (recipe.getName().toLowerCase().contains(partialName))
                        mQuerriedRecipes.add(recipe);
                   }/*else{
                        List<Ingredient> ingredients = recipe.getIngredients();
                        for (Ingredient ingredient1: ingredients){
                            if (ingredient1.getIngredient().toLowerCase().contains(partialName.toLowerCase())){
                                mQuerriedRecipes.add(recipe);
                                break;
                            }
                        }
                    }*/
            }
            else if (prefUtils.getSearchOption().toLowerCase().equals("by ingredient")){
                for (Recipe recipe: mRecipes){
                    /*if (recipe.getName().toLowerCase().contains(partialName)){
                        mQuerriedRecipes.add(recipe);
                    }else{*/
                    List<Ingredient> ingredients = recipe.getIngredients();
                    for (Ingredient ingredient1: ingredients){
                        if (ingredient1.getIngredient().toLowerCase().contains(partialName.toLowerCase())){
                            mQuerriedRecipes.add(recipe);
                            break;
                        }
                    }
                    // }
                }
            }else{
                //both
                for (Recipe recipe: mRecipes){
                    if (recipe.getName().toLowerCase().contains(partialName)){
                        mQuerriedRecipes.add(recipe);
                    }else{
                        List<Ingredient> ingredients = recipe.getIngredients();
                        for (Ingredient ingredient1: ingredients){
                            if (ingredient1.getIngredient().toLowerCase().contains(partialName.toLowerCase())){
                                mQuerriedRecipes.add(recipe);
                                break;
                            }
                        }
                    }
                }
            }
        }
           /* for (Recipe recipe: mRecipes){
                if (recipe.getName().toLowerCase().contains(partialName)){
                    mQuerriedRecipes.add(recipe);
                }else{
                    List<Ingredient> ingredients = recipe.getIngredients();
                    for (Ingredient ingredient1: ingredients){
                        if (ingredient1.getIngredient().toLowerCase().contains(partialName.toLowerCase())){
                            mQuerriedRecipes.add(recipe);
                            break;
                        }
                    }
                }
            }
        }*/

        for (Recipe recipe: mQuerriedRecipes){
            Log.wtf("recipesactivity", recipe.getName());
        }

        mRecipesAdapter = new RecipesGridAdapter(this, mQuerriedRecipes);
        mRecipesGrid.setAdapter(mRecipesAdapter);
        mRecipesGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.wtf("recipesactivity",i+" position");
                Intent intent = new Intent(mRecipesGrid.getContext(),RecipeDetailsActivity.class);
                intent.putExtra(EXTRA_RECIPE,mQuerriedRecipes.get(i));
                startActivity(intent);
            }
        });

    }

    private void findViews() {
        mIngredientTextView = findViewById(R.id.text_recipes_with);
        mRecipesGrid = findViewById(R.id.grid_recipes);
        mContainer = findViewById(R.id.container_recipes);
    }

    private void setupColors(){
        PrefUtils prefUtils = new PrefUtils(this);
        int fontColor = prefUtils.getFontColor();
        Log.wtf("fontcolor",fontColor+"");
       // int backgroundColor =prefUtils.getBackgroundColor();
        if (fontColor!=0){
            mIngredientTextView.setBackgroundColor(
                    Color.parseColor("#"+Integer.toHexString(fontColor)));
        }
        /*if (backgroundColor!=0){
            mContainer.setBackgroundColor(
                    Color.parseColor("#"+Integer.toHexString(backgroundColor)));
        }*/
    }
}
