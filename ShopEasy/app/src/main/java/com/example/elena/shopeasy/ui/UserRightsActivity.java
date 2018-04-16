package com.example.elena.shopeasy.ui;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.utils.PrefUtils;

public class UserRightsActivity extends AppCompatActivity {

    private TextView mLinkTextView;

    private TextView mContentTextView;
    private TextView mTitleTextView;
    private LinearLayout mContainer;

    private PrefUtils mPrefUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rights);

        findViews();
        mPrefUtils = new PrefUtils(this);
        setupColors();

        mLinkTextView.setText(getString(R.string.text_download_pdf)+
                " https://scholarlycommons.law.case.edu/cgi/viewcontent.cgi" +
                "?article=1687&context=caselrev");

        mContentTextView.setText("This is currently just a demo Android Application and should not " +
                        "be treated as a final product.\nThe user is allowed to explore and add " +
                        "recipes with custom ingredients.\nNote that there may appear a new version" +
                        " of the app.\nThe images are used just for a colorful experience, not" +
                        " intending to disrespect copyrights. It does not intend to make money out " +
                        "of it. It's just a demonstrative application which highlights some " +
                        "architectural and design principles.");
        mContentTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    private void findViews(){
        mLinkTextView = findViewById(R.id.text_download_pdf);
        mContainer = findViewById(R.id.container_user_rights);
        mContentTextView = findViewById(R.id.text_user_rights_content);
        mTitleTextView = findViewById(R.id.text_user_rights);
    }

    private void setupColors(){
        int fontColor = mPrefUtils.getFontColor();
        Log.wtf("fontcolor",fontColor+"");
        int backgroundColor = mPrefUtils.getBackgroundColor();
        if (fontColor!=0){
            mTitleTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            mContentTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            mLinkTextView
                    .setTextColor(Color.parseColor("#"+Integer.toHexString(fontColor)));
            mLinkTextView.setLinkTextColor(
                    Color.parseColor("#"+Integer.toHexString(fontColor))
            );

        }
        if (backgroundColor!=0){
            mContainer.setBackgroundColor(
                    Color.parseColor("#"+Integer.toHexString(backgroundColor)));
        }
    }
}
