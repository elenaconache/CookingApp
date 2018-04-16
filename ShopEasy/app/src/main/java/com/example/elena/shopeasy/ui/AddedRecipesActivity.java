package com.example.elena.shopeasy.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.adapters.AddedRecipesRecyclerViewAdapter;
import com.example.elena.shopeasy.fragments.FooterFragment;
import com.example.elena.shopeasy.realm.RecipeInput;
import com.example.elena.shopeasy.realm.RealmController;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import io.realm.RealmResults;

public class AddedRecipesActivity extends AppCompatActivity
        implements FooterFragment.FragmentFooterListener{

    static RecyclerView mRecyclerView;

    static TextView mEmptyView;

    private static AdView mAdViewBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_recipes);

        mRecyclerView = findViewById(R.id.recyclerview_added_recipes);
        mEmptyView = findViewById(R.id.text_empty_view_added_recipes);
        mAdViewBanner= findViewById(R.id.banner_AdView);

        //Load BannerAd
        showBannerAd();

        mEmptyView.setVisibility(View.GONE);

        setupRecyclerView();

    }

    private void setupRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        RealmResults<RecipeInput> results = RealmController.with(getApplication())
                .getRecipeInputs();
        AddedRecipesRecyclerViewAdapter adapter =
                new AddedRecipesRecyclerViewAdapter(results, this);
        mRecyclerView.setAdapter(adapter);

        if (results==null || results.size()==0){
            showEmptyView();
        }else{
            showRecyclerView();
        }
    }

    public static void showEmptyView(){
        mEmptyView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mAdViewBanner.setVisibility(View.GONE);
    }

    public static void showRecyclerView(){
        mEmptyView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);

        mAdViewBanner.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRecyclerView();//when navigating back to this activity you might need to query again
        //in case that the user edited the photo or number of servings
    }

    private void showBannerAd() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("11FC98BB86472EE536487E740D4FDF25")
                .build();
        mAdViewBanner.loadAd(adRequest);
    }

    @Override
    public void onHome() {//for footer fragment
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               // Intent openAccountActivity =  new Intent(AddedRecipesActivity.this,MainActivity.class);
              //  startActivity(openAccountActivity);
                finish();
            }
        },480);//300ms = time of anim/pulse.xml
    }

    @Override
    public void onAccount() {//for footer fragment
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openAccountActivity =  new Intent(AddedRecipesActivity.this, AccountActivity.class);
                startActivity(openAccountActivity);
            }
        },480);//300ms = time of anim/pulse.xml
    }

    @Override
    public void onBook() {//for footer fragment

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
