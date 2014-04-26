package com.lutshe.doiter.views.common;

import android.content.Context;
import android.graphics.Typeface;
import com.lutshe.doiter.R;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.res.StringRes;

/**
 * Provides access to the custom font used by all our view.
 *
 * @Author: Art
 */
@EBean(scope = EBean.Scope.Singleton)
public class OurFont {

    @StringRes(R.string.our_font)
    String fontPath;

    @RootContext
    Context context;

    private Typeface font;

    public Typeface get() {
        if (font == null) {
            font = Typeface.createFromAsset(context.getAssets(), fontPath);
        }
        return font;
    }
}
