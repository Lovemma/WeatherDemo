package xyz.lovemma.weatherdemo.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by OO on 2017/6/17.
 */

public class CircularAnimUtil {

    public static final long PERFECT_MILLS = 618;


    @SuppressLint("NewApi")
    public static void startActivityForResult(
            final Activity thisActivity, final Intent intent, final Integer requestCode,
            final View triggerView, int colorOrImageRes) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            thisActivity.startActivity(intent);
            return;
        } else {
            int[] location = new int[2];
            triggerView.getLocationInWindow(location);
            final int cx = location[0] + triggerView.getWidth() / 2;
            final int cy = location[1] + triggerView.getHeight() / 2;
            final ImageView view = new ImageView(thisActivity);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setImageResource(colorOrImageRes);
            final ViewGroup decorView = (ViewGroup) thisActivity.getWindow().getDecorView();
            int w = decorView.getWidth();
            int h = decorView.getHeight();
            decorView.addView(view, w, h);
            final int finalRadius = (int) Math.sqrt(w * w + h * h) + 1;
            Animator
                    anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
            anim.setDuration(PERFECT_MILLS);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (requestCode == null) {
                        thisActivity.startActivity(intent);
                    } else {
                        thisActivity.startActivityForResult(intent, requestCode);
                    }
                    thisActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    triggerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Animator anim =
                                    ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                            anim.setDuration(PERFECT_MILLS);
                            anim.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    try {
                                        decorView.removeView(view);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            anim.start();

                        }
                    }, 1000);
                }
            });
            anim.start();
        }

    }
}
