package com.example.elena.shopeasy.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.customViews.CustomEditText;
import com.example.elena.shopeasy.realm.RealmController;
import com.example.elena.shopeasy.realm.RecipeInput;
import com.example.elena.shopeasy.ui.AddedRecipesActivity;
import com.example.elena.shopeasy.ui.MainActivity;
import com.example.elena.shopeasy.ui.RecipeDetailsActivity;
import com.example.elena.shopeasy.utils.PrefUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.realm.RealmResults;

import static com.example.elena.shopeasy.ui.RecipesActivity.EXTRA_RECIPE;

/**
 * Created by elena on 2/4/2018.
 */

public class AddedRecipesRecyclerViewAdapter extends
        RecyclerView.Adapter<AddedRecipesRecyclerViewAdapter.AddedRecipesViewHolder> {

    private RealmResults<RecipeInput> results;
    private Activity mActivity;

    public AddedRecipesRecyclerViewAdapter(RealmResults<RecipeInput> results, Activity activity){
        this.results = results;
        mActivity = activity;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddedRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.item_added_recipe;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);
        return new AddedRecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddedRecipesViewHolder holder, final int position) {
        holder.nameText.setText(String.format("Recipe: %s", results.get(position).getmName()));
        holder.ingredientsText.setText(String.format("%s", results.get(position)
                .getmIngredients()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (results.get(position)!=null){
                    Intent intent = new Intent(view.getContext(),RecipeDetailsActivity.class);
                    intent.putExtra(EXTRA_RECIPE,results.get(position).toRecipe());
                    view.getContext().startActivity(intent);
                }

            }
        });

        final PrefUtils prefUtils = new PrefUtils(mActivity);
        int fontColor = prefUtils.getFontColor();
        Log.wtf("fontcolor",fontColor+"");
        int backgroundColor = prefUtils.getBackgroundColor();
        if (fontColor!=0){
           // holder.nameText
        //            .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            holder.ingredientsText
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            holder.ingredientsLabelText
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            Drawable drawable =  holder.removeImageView.getDrawable();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTint(Color.parseColor("#"+Integer.toHexString(fontColor)));
            }
            holder.removeImageView.setImageDrawable(drawable);
        }
        if (backgroundColor!=0){
            //holder.container.setCardBackgroundColor(
             //       Color.parseColor("#"+Integer.toHexString(backgroundColor)));
            Drawable drawable =  holder.nameText.getBackground();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTint(Color.parseColor("#"+Integer.toHexString(fontColor)));
            }
            holder.nameText.setBackgroundDrawable(drawable);
        //    holder.nameText
             //           .setTextColor(Color.parseColor("#"+Integer.toHexString(backgroundColor)));
        }

        holder.removeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove from db
                if (prefUtils.getConfirm()){
                    showDialog(position);
                }else {
                    RealmController.with(mActivity).deleteRecipeInput(results.get(position).getmName());
                    notifyDataSetChanged();
                    if (results==null || results.size()==0) AddedRecipesActivity.showEmptyView();
                    else AddedRecipesActivity.showRecyclerView();
                }


                //  results.deleteFromRealm();
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    class AddedRecipesViewHolder extends RecyclerView.ViewHolder{

        private TextView nameText, ingredientsText;
        private ImageView removeImageView;
        private CardView container;

        private TextView ingredientsLabelText;

        AddedRecipesViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.recipe_name_text);
            ingredientsText = itemView.findViewById(R.id.text_ingredients);
            removeImageView = itemView.findViewById(R.id.image_remove_recipe);

            container = itemView.findViewById(R.id.container_added_recipe);
            ingredientsLabelText = itemView.findViewById(R.id.ingredients_label_text);
        }
    }

    private void showDialog(final int position){
        LayoutInflater factory = LayoutInflater.from(mActivity);
        final View deleteDialogView = factory.inflate(R.layout.dialog_confirm_deletion, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(mActivity).create();
        deleteDialog.setView(deleteDialogView);

       deleteDialogView.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                RealmController.with(mActivity).deleteRecipeInput(results.get(position).getmName());
                deleteFromFirebase(results.get(position).getmName());
                notifyDataSetChanged();
                if (results==null || results.size()==0) AddedRecipesActivity.showEmptyView();
                else AddedRecipesActivity.showRecyclerView();
                deleteDialog.dismiss();
            }
        });
        deleteDialogView.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });

        deleteDialog.show();
    }

    private void deleteFromFirebase(String name) {
        PrefUtils prefUtils = new PrefUtils(mActivity);
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("recipes")
                .child(prefUtils.getEmail().replace(".","")
                        .replace("#","")
                        .replace("$","")
                        .replace("[","")
                        .replace("]","")+" "+name);

        //removing artist
        dR.removeValue();
    }

}
