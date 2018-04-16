package com.example.elena.shopeasy.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.adapters.BrandsRecyclerViewAdapter;
import com.example.elena.shopeasy.fragments.FooterFragment;
import com.example.elena.shopeasy.utils.MyAnimationUtils;

public class BrandsActivity extends AppCompatActivity
        implements FooterFragment.FragmentFooterListener{

    private RecyclerView mBrandsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brands);

        mBrandsRecyclerView = findViewById(R.id.recyclerview_brands);
        setupRecyclerView();

    }

    private void setupRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mBrandsRecyclerView.setLayoutManager(layoutManager);
        mBrandsRecyclerView.setHasFixedSize(true);
        BrandsRecyclerViewAdapter adapter = new BrandsRecyclerViewAdapter();
        mBrandsRecyclerView.setAdapter(adapter);
    }


    @Override
    public void onHome() {//for footer fragment
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openActivity =  new Intent(BrandsActivity.this, MainActivity.class);
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
                Intent openAccountActivity =  new Intent(BrandsActivity.this, AccountActivity.class);
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
                Intent openAccountActivity =  new Intent(BrandsActivity.this,AddedRecipesActivity.class);
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