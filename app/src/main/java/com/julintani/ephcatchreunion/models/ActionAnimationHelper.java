package com.julintani.ephcatchreunion.models;

import android.view.View;

/**
 * Created by ell on 4/9/16.
 */
public class ActionAnimationHelper {

    public static float toYGivenToX(float toX, float radius){
        return (float)Math.sqrt(Math.pow(radius, 2) - Math.pow(toX, 2));
    }

    //quadratic formula used here
    public static float nextXGivenPreviousPoint(float oldX, float oldY, int radius, int margin){
        double constant = 32 * Math.pow(radius, 2) - Math.pow(2 * radius + margin, 2);
        double b = - 4 * oldX * constant;
        double a = 4 * Math.pow(4 * radius, 2);
        double c = Math.pow(constant, 2) - Math.pow(8 * oldY * radius, 2);
        double radicand = Math.pow(b, 2) - 4 * a * c; // b^2 - 4ac
        double numerator = -b - Math.sqrt(radicand); // -b - root(b^2 - 4ac)
        double denominator = 2 * a;
        return (float) (numerator / denominator);
    }

    public static void animateButtonBy(View button, float fromX, float toX, float fromY, float toY, int offset){
        int duration = 100;
        button.setX(fromX);
        button.setY(fromY);
        button.animate()
                .setDuration(duration)
                .translationXBy(toX)
                .translationYBy(toY)
                .setStartDelay(offset)
                .start();
    }
}
