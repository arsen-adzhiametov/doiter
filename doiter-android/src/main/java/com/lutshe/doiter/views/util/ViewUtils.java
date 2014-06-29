package com.lutshe.doiter.views.util;

import android.view.View;

/**
 * @Author: Art
 */
public final class ViewUtils {

    /**
     * Finds view's parent of a given type.
     * For example to find a ListView of one of the elements: findParent(ListView.class, element)
     */
    public static <T> T findParent(Class<T> parentType, View view) {
        if (view == null || parentType.isAssignableFrom(view.getClass())) {
            return (T) view;
        } else {
            return findParent(parentType, (View) view.getParent());
        }
    }
}
