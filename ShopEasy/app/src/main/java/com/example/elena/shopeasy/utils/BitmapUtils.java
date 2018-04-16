package com.example.elena.shopeasy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.widget.ImageView;

import com.example.elena.shopeasy.ui.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Absolute on 2/5/2018.
 */

public class BitmapUtils {

    private static Bitmap mBitmapProfile;

    public static void blackWhiteImage(ImageView v){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(filter);
        v.setAlpha(0.4f);
    }

    public static void getBitmapFromURL(String src) {

        createObservable(src)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("BitmapUtils", "onSubscribe: " + d);
                    }

                    @Override
                    public void onNext(String value) {
                        Log.d("BitmapUtils", "onNext: " + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("BitmapUtils", "onError: ", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d("BitmapUtils", "onComplete: ");
                        MainActivity.setupImageProfile(mBitmapProfile);
                    }
                });


    }

    private static Observable<String> createObservable(final String src) {
        //Could use fromCallable
        return Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                return Observable.just(run(src));
            }
        });
    }

    private static String run(String src) throws IOException {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            mBitmapProfile =  BitmapFactory.decodeStream(input);
            return "";
        } catch (IOException e) {
            // Log exception
            return "";
        }
    }


}
