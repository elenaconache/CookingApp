package com.example.elena.shopeasy.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.fragments.FooterFragment;

public class BestIngredientsActivity extends AppCompatActivity implements
        FooterFragment.FragmentFooterListener{

    private WebView mWebView;
    private ProgressBar mProgressBar;

    private static final String URL_BEST_INGREDIENTS =
            "http://www.eatingwell.com/article/112072/6-healthy-ingredients-to-supercharge-your-smoothie/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_ingredients);

        mWebView = findViewById(R.id.webview_ingredients);
        mProgressBar = findViewById(R.id.pb_loading_webview_ingredients);
        mProgressBar.setVisibility(View.VISIBLE);

        setupWebView();
    }

    private void setupWebView(){


        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.wtf("bestingredientsactivity", "Processing webview url click");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.wtf("bestingredientsactivity", "Finished loading URL: " + url);
                mProgressBar.setVisibility(View.GONE);
            }

            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {
                Log.e("bestingredientsactivity", "Error: " + description);
                Toast.makeText(BestIngredientsActivity.this, "Oh no! " + description,
                        Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK",
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
            }
        });
        mWebView.loadUrl(URL_BEST_INGREDIENTS);
    }

    @Override
    protected void onPause() {
        mWebView.onPause();
        mWebView.pauseTimers();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.resumeTimers();
        mWebView.onResume();
    }

    @Override
    protected void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
    }

    @Override
    public void onHome() {//for footer fragment
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openActivity =  new Intent(BestIngredientsActivity.this, MainActivity.class);
                startActivity(openActivity);
            }
        },480);//300ms = time of anim/pulse.xml
    }

    @Override
    public void onAccount() {//for footer fragment
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openAccountActivity =  new Intent(BestIngredientsActivity.this, AccountActivity.class);
                startActivity(openAccountActivity);
            }
        },480);//300ms = time of anim/pulse.xml
    }

    @Override
    public void onBook() {//for footer fragment
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openAccountActivity =  new Intent(BestIngredientsActivity.this,AddedRecipesActivity.class);
                startActivity(openAccountActivity);


            }
        },480);//300ms = time of anim/pulse.xml
    }

    public void onHomeClick(View view) {
        pulseView(view);
        onHome();
    }

    public void onBookClick(View view) {
        pulseView(view);
        onBook();
    }

    public void onAccountClick(View view) {
        pulseView(view);
        onAccount();
    }

    private void pulseView(View view){
        Animation pulse = AnimationUtils.loadAnimation(this,R.anim.pulse);
        view.startAnimation(pulse);
    }
}
