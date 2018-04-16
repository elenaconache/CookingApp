package com.example.elena.shopeasy.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.realm.RealmController;
import com.example.elena.shopeasy.utils.PrefUtils;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmResults;

public class AccountInfoActivity extends AppCompatActivity {

    private TextView mRecipesTextView, mEmailTextView, mGenderTextView, mNameTextView;
    private CircleImageView mProfileImageView;

    private PrefUtils mPrefUtils;

    private TextView mRecipesLabelTextView, mEmailLabelTextView, mGenderLabelTextView;
    private LinearLayout mContainer;
    private CardView mCardFirst, mCardSecond;
    private ImageView mGenderImage, mMailImage, mRecipesImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        findViews();
        populateUI();

        setupColors();
    }

    private void findViews() {
        mRecipesTextView = findViewById(R.id.text_recipes);
        mEmailTextView= findViewById(R.id.text_mail);
        mGenderTextView = findViewById(R.id.text_gender);
        mNameTextView = findViewById(R.id.text_name);
        mProfileImageView = findViewById(R.id.image_profile);

        mRecipesLabelTextView = findViewById(R.id.text_recipes_label);
        mEmailLabelTextView = findViewById(R.id.text_mail_label);
        mGenderLabelTextView = findViewById(R.id.text_gender_label);
        mContainer = findViewById(R.id.container_account_info);
        mCardFirst = findViewById(R.id.card_picture);
        mCardSecond = findViewById(R.id.card_data);
        mGenderImage = findViewById(R.id.image_gender);
        mMailImage = findViewById(R.id.image_mail);
        mRecipesImage = findViewById(R.id.image_recipes);
    }

    private void populateUI(){
        int recipesCount = RealmController.with(this).getRecipeInputs().size();
        mRecipesTextView.setText(recipesCount+"");
        mPrefUtils = new PrefUtils(this);
        mEmailTextView.setText(mPrefUtils.getEmail());
        mGenderTextView.setText(mPrefUtils.getGender());
        mNameTextView.setText(mPrefUtils.getFacebookUsername());
        Glide.with(this).load(mPrefUtils.getFacebookImage()).into(mProfileImageView);
    }

    private void setupColors(){
        int fontColor = mPrefUtils.getFontColor();
        Log.wtf("fontcolor",fontColor+"");
        int backgroundColor = mPrefUtils.getBackgroundColor();
        if (fontColor!=0){
            mNameTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            mEmailLabelTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            mEmailTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            mGenderLabelTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            mGenderTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            mRecipesLabelTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            mRecipesTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            Drawable drawable = mGenderImage.getDrawable();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTint(Color.parseColor("#"+Integer.toHexString(fontColor)));
            }
            mGenderImage.setImageDrawable(drawable);

            drawable = mRecipesImage.getDrawable();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTint(Color.parseColor("#"+Integer.toHexString(fontColor)));
            }
            mRecipesImage.setImageDrawable(drawable);

            drawable = mMailImage.getDrawable();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTint(Color.parseColor("#"+Integer.toHexString(fontColor)));
            }
            mMailImage.setImageDrawable(drawable);
        }
        if (backgroundColor!=0){
            /*mCardFirst.setCardBackgroundColor(
                    Color.parseColor("#"+Integer.toHexString(backgroundColor)));*/
            /*mCardSecond.setBackgroundColor(
                    Color.parseColor("#"+Integer.toHexString(backgroundColor))
            );*/
            mContainer.setBackgroundColor(
                   Color.parseColor("#"+Integer.toHexString(backgroundColor)));
        }
    }
}
