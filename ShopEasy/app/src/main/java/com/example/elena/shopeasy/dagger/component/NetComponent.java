package com.example.elena.shopeasy.dagger.component;

import com.example.elena.shopeasy.dagger.module.AppModule;
import com.example.elena.shopeasy.dagger.module.NetModule;
import com.example.elena.shopeasy.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Absolute on 1/10/2018.
 */

@Singleton //a single instance is needed from this class
@Component(modules = {AppModule.class,NetModule.class})//specify all module classes of this project
public interface NetComponent {
        void injectMain(MainActivity activity);//pass all activities/fragments that are using this
        // module[using retrofit]
    // send retrofit as parameter for mvvm int impl classes
}
