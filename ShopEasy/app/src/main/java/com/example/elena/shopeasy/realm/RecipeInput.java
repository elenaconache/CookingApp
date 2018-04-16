package com.example.elena.shopeasy.realm;

import com.example.elena.shopeasy.model.Ingredient;
import com.example.elena.shopeasy.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Absolute on 3/9/2018.
 */

public class RecipeInput extends RealmObject {
    @PrimaryKey
    private int mId;

    private String mName;
    private String mIngredients;

    private RealmList<Ingredient> mIngredientsList;

    private String mServings;
    private String mPhotoUri;

    public RecipeInput() {
    }

    public RecipeInput(int mId, String mName, String mIngredients, List<Ingredient> mIngredientsList,String mServings
                      , String mPhotoUri
    ) {
        this.mId = mId;
        this.mName = mName;
        this.mIngredients = mIngredients;

        this.mIngredientsList.addAll(mIngredientsList);

        this.mServings = mServings;
        this.mPhotoUri = mPhotoUri;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmIngredients() {
        return mIngredients;
    }

    public void setmIngredients(String mIngredients) {
        this.mIngredients = mIngredients;
    }

    public List<Ingredient> getmIngredientsList() {
        return mIngredientsList;
    }

    public void setmIngredientsList(List<Ingredient> mIngredientsList) {
        if (this.mIngredientsList==null) this.mIngredientsList = new RealmList<>();
        this.mIngredientsList.addAll(mIngredientsList);
    }

    public void setmIngredientsListFromString(String ingredients) {
        this.mIngredientsList = new RealmList<>();
        String[] ingredientsInput = ingredients.split("\n");
        for (String i: ingredientsInput){
            mIngredientsList.add(new Ingredient(0,"",i));
        }

    }

    public Recipe toRecipe(){
        Recipe recipe = new Recipe();
        recipe.setId(mId);
        recipe.setName(mName);
        List<Ingredient> ingredients = new ArrayList<>();
        String[] ingredientsInput = mIngredients.split("\n");
        for (String i: ingredientsInput){
            ingredients.add(new Ingredient(0,"",i));
        }
        recipe.setIngredients(ingredients);

        recipe.setPhoto(mPhotoUri);

        if (mServings == null) recipe.setServings(0);
        else{
            String servings = mServings.replace(" ","");
            if (servings==null||servings.length()==0){
                recipe.setServings(0);
            }else {
                try {
                    recipe.setServings(Integer.valueOf(servings));
                }catch (Exception e){
                    recipe.setServings(0);
                    e.printStackTrace();
                }
            }
        }

        return recipe;
    }

    public void setmPhotoUri(String mPhotoUri) {
        this.mPhotoUri = mPhotoUri;
    }

    public void setmServings(String mServings) {
        this.mServings = mServings;
    }

    public String getmServings() {
        return mServings;
    }

    public String getmPhotoUri() {
        return mPhotoUri;
    }

    public void fill(final RecipeInput recipeInput) {
        setmServings(recipeInput.getmServings());
        setmId(recipeInput.getmId());
        setmIngredients(recipeInput.getmIngredients());
        setmName(recipeInput.getmName());

        setmPhotoUri(recipeInput.getmPhotoUri());

        setmIngredientsList(mIngredientsList);
    }
}
