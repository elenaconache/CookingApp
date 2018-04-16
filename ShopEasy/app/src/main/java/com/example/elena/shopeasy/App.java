package com.example.elena.shopeasy;

import android.app.Application;

import com.example.elena.shopeasy.dagger.component.DaggerNetComponent;
import com.example.elena.shopeasy.dagger.component.NetComponent;
import com.example.elena.shopeasy.dagger.module.AppModule;
import com.example.elena.shopeasy.dagger.module.NetModule;
import com.example.elena.shopeasy.realm.RealmMigrations;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Absolute on 1/10/2018.
 */

public class App extends Application {//pass it in the manifest android:name=".App"
    //it's used for initialization of global state before the first activity is displayed

    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        //initialization logic here
        mNetComponent = DaggerNetComponent.builder()//if it cant be resolved, try build project. the project needs to be compiled so that this file would be generated
                .appModule(new AppModule(this))//method not resolved if it's not defined as module in netcomponent
                .netModule(new NetModule())
                .build();

        // initialize Realm
      //  Realm.init(getApplicationContext());

        // create your Realm configuration
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("shopeasy.realm")
                .schemaVersion(5)//change this after every schema change
                .migration(new RealmMigrations())
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        // Realm.getInstance(realmConfig);

    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }

    @Override
    public void onTerminate() {
        Realm.getDefaultInstance().close();
        super.onTerminate();
    }
}