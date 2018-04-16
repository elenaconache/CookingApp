package com.example.elena.shopeasy.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.model.Ingredient;
import com.example.elena.shopeasy.model.Recipe;
import com.example.elena.shopeasy.ui.MainActivity;
import com.example.elena.shopeasy.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Absolute on 2/2/2018.
 */

public class SuggestionsListAdapter extends BaseAdapter {

    // Declare Variables
    private LayoutInflater inflater;
    private List<Recipe> recipesList = new ArrayList<>();
    private List<Recipe> filteredRecipes = new ArrayList<>();
    private List<String> filteredIngredients = new ArrayList<>();

    private Activity mActivity;

    public SuggestionsListAdapter(Context context, List<Recipe> list, Activity activity) {
        inflater = LayoutInflater.from(context);

        if (list!=null){
            recipesList.addAll(list);
            filteredRecipes.addAll(list);

            filteredIngredients=new ArrayList<>();
            for (Recipe recipe: filteredRecipes){
                String ingredientString="";
                for (int i=0; i<recipe.getIngredients().size(); i++){
                    if (i!=0) ingredientString+=", ";
                    Ingredient ingredient = recipe.getIngredients().get(i);
                    ingredientString+=ingredient.getIngredient()+" ";
                }
                filteredIngredients.add(ingredientString);
            }
        }

        mActivity = activity;

        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView name;
        TextView ingredients;//, ingredientsLabel;
    }

    @Override
    public int getCount() {
        return filteredRecipes.size();
    }

    @Override
    public Recipe getItem(int position) {
        return filteredRecipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.suggestion_item, null);
            holder.name =  view.findViewById(R.id.name);

            holder.ingredients = view.findViewById(R.id.ingredients);
          //  holder.ingredientsLabel = view.findViewById(R.id.ingredientsLabel);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(filteredRecipes.get(position).getName());

        if (new PrefUtils(mActivity).getSearchOption().contains("ingredient")){
           // holder.ingredientsLabel.setVisibility(View.VISIBLE);
            holder.ingredients.setVisibility(View.VISIBLE);
            if (filteredIngredients.get(position).length()>0){
                holder.ingredients.setText(filteredIngredients.get(position));
            }else holder.ingredients.setVisibility(View.GONE);

        }else{
         //   holder.ingredientsLabel.setVisibility(View.GONE);
            holder.ingredients.setVisibility(View.GONE);
        }

        return view;
    }

    public void filter(String charText) {
        switch (new PrefUtils(mActivity).getSearchOption()){
            case "By recipe name":
                charText = charText.toLowerCase();
                filteredRecipes.clear();
                if (charText.length() != 0 ){
                    for (Recipe rec : recipesList) {
                        if (rec.getName().toLowerCase().contains(charText.toLowerCase())) {
                            filteredRecipes.add(rec);
                       //     break;
                        }
                    }
                }
                break;
            case "By ingredient":
                charText = charText.toLowerCase();
                filteredRecipes.clear();
                if (charText.length() != 0 ){
                    for (Recipe rec : recipesList) {
                       // Log.wtf("byingredient",rec.getIngredients().toString()+" list");
                        for (Ingredient ingredient: rec.getIngredients()){
                            if (ingredient.getIngredient().toLowerCase().contains(charText.toLowerCase())){
                                filteredRecipes.add(rec);
                                break;
                            }
                        }
                    }
                    //--ingredients
                    filteredIngredients.clear();
                    for (Recipe recipe: filteredRecipes){
                        String ingredientString="";
                        int count = 0;
                        for (int i=0; i<recipe.getIngredients().size(); i++){
                            Ingredient ingredient = recipe.getIngredients().get(i);
                            if (ingredient.getIngredient().toLowerCase().contains(charText.toLowerCase())){
                                if (count!=0) ingredientString+=", ";

                                ingredientString+=ingredient.getIngredient()+" ";
                                count++;
                            }

                        }
                        filteredIngredients.add(ingredientString);
                    }
                }
                break;
            case "By both recipe name and ingredients":
                charText = charText.toLowerCase();
                filteredRecipes.clear();
              //  boolean added = false;
                if (charText.length() != 0 ){
                    for (Recipe rec : recipesList) {
                        if (rec.getName().toLowerCase().contains(charText.toLowerCase())) {
                            filteredRecipes.add(rec);
                            //added = true;
                           // break;
                        }
                    }
                   // if(!added)
                        for (Recipe rec : recipesList) {
                            //Log.wtf("byingredient",rec.getIngredients().toString()+" list");
                            for (Ingredient ingredient: rec.getIngredients()){
                                if (ingredient.getIngredient().toLowerCase().contains(charText.toLowerCase())
                                        && !filteredRecipes.contains(rec)){
                                    filteredRecipes.add(rec);
                                    break;
                                }
                            }
                        }
                    //ingredients
                    filteredIngredients.clear();
                    for (Recipe recipe: filteredRecipes){
                        String ingredientString="";
                        int count = 0;
                        for (int i=0; i<recipe.getIngredients().size(); i++){
                            Ingredient ingredient = recipe.getIngredients().get(i);
                            if (ingredient.getIngredient().toLowerCase().contains(charText.toLowerCase())){
                                if (count!=0) ingredientString+=", ";

                                ingredientString+=ingredient.getIngredient()+" ";
                                count++;
                            }

                        }
                        filteredIngredients.add(ingredientString);
                    }
                }
                break;
        }

        notifyDataSetChanged();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public List<Recipe> getFilteredRecipes() {
        return filteredRecipes;
    }
}
