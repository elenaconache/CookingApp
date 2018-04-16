package com.example.elena.shopeasy.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.Image;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.realm.RealmController;
import com.example.elena.shopeasy.utils.PrefUtils;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.w3c.dom.Text;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

public class SettingsActivity extends AppCompatActivity {

    private PrefUtils mPrefUtils;
    private TextView mFontColorTextView;
    private TextView mBackgroundColorTextView;

    private Switch mConfirmSwitch;

    private TextView mNotificationSummaryTextView;
    private RadioButton mDailyRadioButton, mWeeklyRadioButton, mDisabledRadioButton;
    private TextView mIngredientsTopSummaryTextView;

    private String mPrefNotification;

    private RadioButton mNameSearchRadioButton, mIngredientSearchRadioButton, mBothRadioButton;
    private TextView mSearchSummaryTextView;

    private ImageView mSelectedColorImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_setings);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mPrefUtils = new PrefUtils(this);

        findViews();

        mConfirmSwitch.setChecked(mPrefUtils.getConfirm());

        mConfirmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPrefUtils.saveConfirm(isChecked);
            }
        });

        setupColors();
        mNotificationSummaryTextView.setText(mPrefUtils.getNotify());
        mPrefNotification = mPrefUtils.getNotify();

        mIngredientsTopSummaryTextView.setText(mPrefUtils.getTopCount() + "");

        mSearchSummaryTextView.setText(mPrefUtils.getSearchOption());
    }

    public void onFontColorClick(View view) {
        showDialogColor("Choose font color", new int[]{
                Color.parseColor("#b71c1c"),
                Color.parseColor("#4a148c"),
                Color.parseColor("#1a237e"),
                Color.parseColor("#01579b"),
                Color.parseColor("#004d40"),
                Color.parseColor("#33691e"),
                Color.parseColor("#f57f17"),
                Color.parseColor("#e65100"),
                Color.parseColor("#3e2723"),
                Color.parseColor("#263238")
        });
    }

    public void onBackgroundColorClick(View view) {
        showDialogColor("Choose background color", new int[]{
                Color.parseColor("#ffcdd2"),
                Color.parseColor("#e1bee7"),
                Color.parseColor("#c5cae9"),
                Color.parseColor("#b3e5fc"),
                Color.parseColor("#b2dfdb"),
                Color.parseColor("#dcedc8"),
                Color.parseColor("#fff9c4"),
                Color.parseColor("#ffe0b2"),
                Color.parseColor("#d7ccc8"),
                Color.parseColor("#cfd8dc")
        });
    }

    private void setupColors() {
        int fontColor = mPrefUtils.getFontColor();
        Log.wtf("fontcolor", fontColor + "");
        int backgroundColor = mPrefUtils.getBackgroundColor();
        if (fontColor != 0) {
            Drawable drawable = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_color_lens_black_48dp, null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTint(Color.parseColor("#" + Integer.toHexString(fontColor)));
            }
            mFontColorTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    drawable,
                    null);

        }
        if (backgroundColor != 0) {
            Drawable drawable = getResources().getDrawable(R.drawable.ic_color_lens_black_48dp);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable.setTint(Color.parseColor("#" + Integer.toHexString(backgroundColor)));
            }
            mBackgroundColorTextView.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    drawable,
                    null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onNotificationTextClick(View view) {
        showDialogNotifications();
    }

    private void findViews() {
        mFontColorTextView = findViewById(R.id.text_font_color);
        mBackgroundColorTextView = findViewById(R.id.text_background_color);
        mConfirmSwitch = findViewById(R.id.switch_confirmation_dialogues);
        mNotificationSummaryTextView = findViewById(R.id.text_notifications_summary);

        mIngredientsTopSummaryTextView = findViewById(R.id.text_ingredients_top_summary);
        mSearchSummaryTextView = findViewById(R.id.text_search_summary);
    }

    private void showDialogNotifications() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView =
                factory.inflate(R.layout.dialog_pref_list_notifications, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);

        mDailyRadioButton = deleteDialogView.findViewById(R.id.radio_button_daily);
        mWeeklyRadioButton = deleteDialogView.findViewById(R.id.radio_button_weekly);
        mDisabledRadioButton = deleteDialogView.findViewById(R.id.radio_button_disabled);

        mPrefNotification = mPrefUtils.getNotify();
        switch (mPrefNotification) {
            case "Daily":
                mDailyRadioButton.setChecked(true);
                break;
            case "Weekly":
                mWeeklyRadioButton.setChecked(true);
                break;
            case "Disabled":
                mDisabledRadioButton.setChecked(true);
                break;

        }

        deleteDialogView.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                if (mDailyRadioButton.isChecked()) {
                    mPrefUtils.saveNotify(mDailyRadioButton.getText().toString());
                    mNotificationSummaryTextView.setText(mDailyRadioButton.getText().toString());
                } else if (mWeeklyRadioButton.isChecked()) {
                    mPrefUtils.saveNotify(mWeeklyRadioButton.getText().toString());
                    mNotificationSummaryTextView.setText(mWeeklyRadioButton.getText().toString());
                } else if (mDisabledRadioButton.isChecked()) {
                    mPrefUtils.saveNotify(mDisabledRadioButton.getText().toString());
                    mNotificationSummaryTextView.setText(mDisabledRadioButton.getText().toString());
                }
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

    private void showDialogTopCount() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView =
                factory.inflate(R.layout.dialog_ingredients_top_count, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this)
                .create();
        deleteDialog.setView(deleteDialogView);

        final MaterialNumberPicker materialNumberPicker;
        materialNumberPicker = deleteDialogView.findViewById(R.id.numberpicker);


        deleteDialogView.findViewById(R.id.text_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrefUtils.saveTopCount(materialNumberPicker.getValue());
                mIngredientsTopSummaryTextView.setText(materialNumberPicker.getValue() + "");
                deleteDialog.dismiss();
            }
        });
        deleteDialogView.findViewById(R.id.text_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();
    }

    public void onIngredientsTopCountClick(View view) {
        showDialogTopCount();
    }

    public void onSearchOptionClick(View view) {
        showDialogSearch();
    }

    private void showDialogSearch() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView =
                factory.inflate(R.layout.dialog_pref_search_options, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);

        mNameSearchRadioButton = deleteDialogView.findViewById(R.id.radio_button_name);
        mIngredientSearchRadioButton = deleteDialogView.findViewById(R.id.radio_button_ingredient);
        mBothRadioButton = deleteDialogView.findViewById(R.id.radio_button_both);

        switch (mPrefUtils.getSearchOption()) {
            case "By recipe name":
                mNameSearchRadioButton.setChecked(true);
                break;
            case "By ingredient":
                mIngredientSearchRadioButton.setChecked(true);
                break;
            case "By both recipe name and ingredients":
                mBothRadioButton.setChecked(true);
                break;

        }

        deleteDialogView.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                if (mNameSearchRadioButton.isChecked()) {
                    mPrefUtils.saveSearchOption(mNameSearchRadioButton.getText().toString());
                    mSearchSummaryTextView.setText(mNameSearchRadioButton.getText().toString());
                } else if (mIngredientSearchRadioButton.isChecked()) {
                    mPrefUtils.saveSearchOption(mIngredientSearchRadioButton.getText().toString());
                    mSearchSummaryTextView.setText(mIngredientSearchRadioButton.getText().toString());
                } else if (mBothRadioButton.isChecked()) {
                    mPrefUtils.saveSearchOption(mBothRadioButton.getText().toString());
                    mSearchSummaryTextView.setText(mBothRadioButton.getText().toString());
                }
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

    private void showDialogColor(final String title, final int[] colors) {

        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView =
                factory.inflate(R.layout.dialog_color_picker, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);

        TextView mTitleTextView = deleteDialogView.findViewById(R.id.text_title_picker);
        mTitleTextView.setText(title);

        ImageView mPreviewImage = deleteDialogView.findViewById(R.id.img_chosen_color);

        int savedColor = 0;

        if (title.toLowerCase().contains("font")) {
            savedColor = mPrefUtils.getFontColor();
        } else if (title.toLowerCase().contains("background")) {
            savedColor = mPrefUtils.getBackgroundColor();
        }

        if (savedColor!=0){
            GradientDrawable drawable = (GradientDrawable) mPreviewImage.getBackground();
            drawable.setColor(savedColor);
        }else{
            savedColor=colors[0];
            GradientDrawable drawable = (GradientDrawable) mPreviewImage.getBackground();
            drawable.setColor(savedColor);
        }

        for (int i = 0; i < 10; i++) {
            final ImageView img = deleteDialogView.findViewWithTag(i + "");

            GradientDrawable drawable = (GradientDrawable) img.getBackground();
            drawable.setColor(colors[i]); // set solid color
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onImageClick(view.getTag()+"", deleteDialogView);
                }
            });
        }


        for (int i = 0; i < 10; i++) {
            ImageView imageView = deleteDialogView.findViewWithTag(i + "");
            if (colors[i]==savedColor) mSelectedColorImageView = imageView;

        }
        if (mSelectedColorImageView!=null) onImageClick(mSelectedColorImageView.getTag()+"",
                deleteDialogView);

        deleteDialogView.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                if (mSelectedColorImageView != null) {
                    int position = Integer.valueOf(mSelectedColorImageView.getTag().toString());
                    if (title.toLowerCase().contains("font")) {
                        mPrefUtils.saveFontColor(colors[position]);
                    } else if (title.toLowerCase().contains("background")) {
                        mPrefUtils.saveBackgroundColor(colors[position]);
                    }
                }
                setupColors();
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

    private void onImageClick(String tag, View parent) {
        ImageView img = parent.findViewWithTag(tag);

        mSelectedColorImageView = img;

        Drawable drawableSelected = img.getBackground();


       ImageView preview = parent.findViewById(R.id.img_chosen_color);
        preview.setBackgroundDrawable(drawableSelected);
    }
}
