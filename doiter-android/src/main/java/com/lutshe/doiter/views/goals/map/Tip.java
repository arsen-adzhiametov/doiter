package com.lutshe.doiter.views.goals.map;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import com.lutshe.doiter.R;

public class Tip {
    private static final Tip[] tips = new Tip[] {
        new Tip(R.drawable.tip_blue_1, R.drawable.tip_blue_2),
        new Tip(R.drawable.tip_green_1, R.drawable.tip_green_2),
        new Tip(R.drawable.tip_orange_1, R.drawable.tip_orange_2),
        new Tip(R.drawable.tip_pink_1, R.drawable.tip_pink_2),
        new Tip(R.drawable.tip_purple_1, R.drawable.tip_purple_2),
        new Tip(R.drawable.tip_red_1, R.drawable.tip_red_2),
        new Tip(R.drawable.tip_yellow_1, R.drawable.tip_yellow_2)
    };

    private int left, right;

    private Tip (int left, int right) {
        this.left = left;
        this.right = right;
    }

    public static int getLeftTip(Long id) {
        id %= tips.length - 1;
        return tips[id.intValue()].left;
    }

    public static int getRightTip(Long id) {
        id %= tips.length - 1;
        return tips[id.intValue()].right;
    }
}
