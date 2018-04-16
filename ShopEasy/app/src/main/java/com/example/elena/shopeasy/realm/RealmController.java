package com.example.elena.shopeasy.realm;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.example.elena.shopeasy.model.Ingredient;
import com.example.elena.shopeasy.model.Recipe;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Absolute on 3/9/2018.
 */

public class RealmController {
    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Book.class
   /* public void clearAll() {

        realm.beginTransaction();
        realm.clear(RecipeInput.class);
        realm.commitTransaction();
    }*/

    //find all objects in the Book.class
    public RealmResults<RecipeInput> getRecipeInputs() {

        return realm.where(RecipeInput.class).findAll();
    }

    //query a single item with the given id
    public RecipeInput getRecipeInput(int id) {

        return realm.where(RecipeInput.class).equalTo("mId", id).findFirst();
    }

    //check if Book.class is empty
  /*  public boolean hasRecipeInputs() {

        return !realm.allObjects(RecipeInput.class).isEmpty();
    }*/

    public void insertRecipeInput(String recipeName, String ingredients,String servings){
        // Persist your data easily

        Number currentIdNum = realm.where(RecipeInput.class).max(RecipeInputFields.ID);
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
        nextId = currentIdNum.intValue() + 1;
        }

        RecipeInput recipeInput = new RecipeInput();
        recipeInput.setmId(nextId);
        recipeInput.setmIngredients(ingredients);
        recipeInput.setmName(recipeName);
        recipeInput.setmServings(servings);
        recipeInput.setmPhotoUri(null);

        recipeInput.setmIngredientsListFromString(ingredients);
      //  recipeInput = new RecipeInput(nextId,recipeName,ingredients,)

        realm.beginTransaction();
        realm.copyToRealm(recipeInput);
        realm.commitTransaction();
    }

    //query example
  /*  public RealmResults<RecipeInput> queryedRecipeInputs() {

        return realm.where(RecipeInput.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }*/

  //RealmController.with(this).getBooks() -- usage

    public void deleteRecipeInput(final String name){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                RealmResults<RecipeInput> rows = realm.where(RecipeInput.class)
                        .equalTo(RecipeInputFields.NAME,name).findAll();
                rows.deleteAllFromRealm();
            }
        });
    }

    //todo footer everywhere: add footer in layouts: added recipes, recipe details, best ingredients, get inspired
    //todo book functionality from footer recipe details, best ingredients, get inspired + brands, account, home
    //todo home functionality from footer recipe details, best ingredients, get inspired + brands, account, home

    //todo widget to view recipes [mini youtube]
    //todo activity transitions
    //todo test overdraw

    public RecipeInput getRecipeInputForName(String name) {

        return realm.where(RecipeInput.class).equalTo(RecipeInputFields.NAME, name).findFirst();
    }

    public void updateRecipeInputServings(final Recipe recipe){
        final Realm myRealm;
        myRealm = Realm.getDefaultInstance();
        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                try {
                    // the listObject has contain updated data in the List
                    RecipeInput recipeInput = new RecipeInput();
                    recipeInput.setmId(recipe.getId());
                    recipeInput.setmName(recipe.getName());
                    StringBuilder ingredients= new StringBuilder();
                    for (Ingredient ingredient: recipe.getIngredients()){
                        ingredients.append(ingredient.getIngredient()).append("\n");
                    }
                    recipeInput.setmIngredients(ingredients.toString());

                    recipeInput.setmIngredientsListFromString(ingredients.toString());

                    recipeInput.setmServings(recipe.getServings()+"");
                    recipeInput.setmPhotoUri(recipe.getPhoto());
                    myRealm.copyToRealmOrUpdate(recipeInput);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void deleteRecipeInputs(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                RealmResults<RecipeInput> rows = realm.where(RecipeInput.class).findAll();
                rows.deleteAllFromRealm();
            }
        });
    }


}
