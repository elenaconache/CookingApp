package com.example.elena.shopeasy.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

/**
 * Created by Absolute on 2/5/2018.
 */

public class MyAnimationUtils {
    public static void temporaryOpaqueView(final View v){
        ValueAnimator animator = ValueAnimator.ofFloat(0.4f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        animator.setDuration(300);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(1);
        animator.start();
    }

    public static void temporaryColorImage(final ImageView v){
        ValueAnimator animatorSaturation = ValueAnimator.ofFloat(0f, 1f);
        animatorSaturation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation((Float)animation.getAnimatedValue());
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                v.clearColorFilter();
                v.setColorFilter(filter);
            }
        });
        animatorSaturation.setDuration(300);
        animatorSaturation.setRepeatMode(ValueAnimator.REVERSE);
        animatorSaturation.setRepeatCount(1);
        animatorSaturation.start();
    }

}
