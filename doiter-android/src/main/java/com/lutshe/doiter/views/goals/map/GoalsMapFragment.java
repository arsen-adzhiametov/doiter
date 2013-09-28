package com.lutshe.doiter.views.goals.map;

import android.app.Fragment;
import android.widget.FrameLayout;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.data.provider.ImagesProviderImpl;
import com.lutshe.doiter.views.util.FragmentsSwitcher;

/**
 * Created by Arturro on 15.09.13.
 */
@EFragment(R.layout.goals_map_fragment)
public class GoalsMapFragment extends Fragment {

    @Bean(GoalsDao.class)
    GoalsDao goalsDao;

    @Bean
    FragmentsSwitcher fragmentsSwitcher;

    @Bean(ImagesProviderImpl.class)
    ImagesProvider imagesProvider;

    @AfterViews
    public void init() {
        GoalsMapView view = new GoalsMapView(fragmentsSwitcher, imagesProvider, getActivity().getApplicationContext(), goalsDao.getAllGoals());
        ((FrameLayout)getView()).addView(view);
    }
}