package com.lutshe.doiter.views.goals.preview;

import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;
import com.lutshe.doiter.R;

/**
 * Created by Arsen Adzhiametov on goal6/31/13.
 */
@EBean
public class ViewPaddingAdapter {

    static final int PADDING_MIN = 3;
    static final int PADDING_NORMAL = 7;
    static final int PADDING_MAX= 15;

    @ViewById(R.id.top_layout)
    RelativeLayout topLayout;
    @ViewById(R.id.bottom_layout)
    RelativeLayout bottomLayout;

    @SystemService
    WindowManager windowManager;

    @AfterViews
    void setPadding() {
        getDisplayRatio().applyPadding(topLayout, bottomLayout);
    }

    private DisplayRatio getDisplayRatio() {
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float ratio = (float) size.y/size.x;
        if (ratio <= 1.5) {
            return DisplayRatio.WIDE;
        } else if (ratio <= 1.7) {
            return DisplayRatio.NORMAL;
        } else {
            return DisplayRatio.TALL;
        }
    }

    enum DisplayRatio {

        WIDE(PADDING_MIN),
        NORMAL(PADDING_NORMAL),
        TALL(PADDING_MAX);

        private int padding;

        private DisplayRatio(int padding) {
            this.padding = padding;
        }

        void applyPadding(RelativeLayout ... layouts){
            for (RelativeLayout rl : layouts) {
                rl.setPadding(0, padding, 0, padding);
            }
        }

    }
}
