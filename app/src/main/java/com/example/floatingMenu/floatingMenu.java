package com.example.floatingMenu;


import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

public class floatingMenu extends RelativeLayout implements View.OnTouchListener {

    private int _xDelta;
    private int _yDelta;

    public floatingMenu(Context context) {
        super(context);
    }

    public floatingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public floatingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("test", "click");
            }
        });
        setOnTouchListener(this);
    }

    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                _xDelta = X - lp.leftMargin;
                _yDelta = Y - lp.topMargin;
                Log.e("test", "ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("test", "ACTION_UP");
                fixPosition();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.e("test", "ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.e("test", "ACTION_POINTER_UP");
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = new LayoutParams(getLayoutParams().width, getLayoutParams().height);
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                setLayoutParams(layoutParams);
                break;
        }
        invalidate();
        return true;
    }

    private void fixPosition() {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
        _xDelta = lp.leftMargin;
        _yDelta = lp.topMargin;
        getScreenW();
        getScreenH();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                RelativeLayout.LayoutParams layoutParams = new LayoutParams(getLayoutParams().width, getLayoutParams().height);
                if (_yDelta < getScreenH() / 8) {

                    layoutParams.leftMargin = _xDelta;
                    layoutParams.topMargin = ((int) (_yDelta * (1 - (float) valueAnimator.getAnimatedValue())));

                } else {
                    if (_yDelta > (getScreenH() - getScreenH() / 4)) {
                        layoutParams.leftMargin = _xDelta;
                        int edame = getScreenH() - getLayoutParams().height- getstatusbarH() - _yDelta;
                        layoutParams.topMargin = getScreenH() - getLayoutParams().height -getstatusbarH()- ((int) (edame * (1 - (float) valueAnimator.getAnimatedValue())));
                    } else {
                        if (_xDelta < getScreenW() / 2) {
                            layoutParams.leftMargin = ((int) (_xDelta * (1 - (float) valueAnimator.getAnimatedValue())));
                            layoutParams.topMargin = _yDelta;
                        } else {
                            int edame = getScreenW() - getLayoutParams().width - _xDelta;
                            layoutParams.leftMargin = getScreenW() - getLayoutParams().width - ((int) (edame * (1 - (float) valueAnimator.getAnimatedValue())));
                            layoutParams.topMargin = _yDelta;
                        }
                    }

                }
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                setLayoutParams(layoutParams);
            }
        });
        valueAnimator.start();
    }

    private int getstatusbarH() {
            int result = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
            return result;
    }

    private int getScreenW() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private int getScreenH() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }
}