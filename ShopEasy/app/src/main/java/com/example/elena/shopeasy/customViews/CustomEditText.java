package com.example.elena.shopeasy.customViews;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Created by Absolute on 3/13/2018.
 */

public class CustomEditText extends android.support.v7.widget.AppCompatEditText {

    private Drawable drawableRight;
    int actionX, actionY;

    public CustomEditText(Context context, AttributeSet attrs,
                          int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context) {
        super(context);
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom) {
        if (right != null) {
            drawableRight = right;
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }
/*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Rect bounds;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            actionX = (int) event.getX();
            actionY = (int) event.getY();
            // this works for left since container shares 0,0 origin with bounds
            if (drawableRight != null) {
                bounds = null;
                bounds = drawableRight.getBounds();
                int x, y;
                //int extraTapArea = 13;
                int extraTapArea = 20;
                x = (int) (actionX + extraTapArea);
                y = (int) (actionY - extraTapArea);
                x = getWidth() - x;
                if (x <= 0) {
                    x += extraTapArea;
                }
                if (y <= 0)
                    y = actionY;
                if (bounds.contains(x, y)) {
                    setText("");
                    return false;
                }
                return super.onTouchEvent(event);
            }
        }
        return super.onTouchEvent(event);
    }*/
}