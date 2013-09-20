package com.lutshe.doiter.views.goals.map;

import android.app.Fragment;
import android.widget.LinearLayout;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.common.CanvasView;

/**
 * Created by Arturro on 15.09.13.
 */
@EFragment(R.layout.goals_map_fragment)
public class GoalsMapFragment extends Fragment {

    @AfterViews
    public void afterShown() {
        LinearLayout layout = (LinearLayout) getView();
        layout.addView(new CanvasView(getActivity()));
        layout.invalidate();
    }
}
