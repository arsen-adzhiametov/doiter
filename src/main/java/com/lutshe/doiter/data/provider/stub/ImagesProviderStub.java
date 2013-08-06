package com.lutshe.doiter.data.provider.stub;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.provider.ImagesProvider;

import static android.graphics.BitmapFactory.decodeResource;

/**
 * Created by Arturro on 28.07.13.
 */
@EBean
public class ImagesProviderStub implements ImagesProvider {

    @RootContext
    Context context;

    private Resources resources;

    @AfterInject
    public void initResources() {
        resources = context.getResources();
    }

    @Override
    public Bitmap getImage(Long id) {
        switch (id.intValue()) {
            case 0:
                return decodeResource(resources, R.drawable.sample_donkey);
            case 1:
                return decodeResource(resources, R.drawable.sample_rate);
            case 2:
                return decodeResource(resources, R.drawable.sample_dating);
            case 3:
                return decodeResource(resources, R.drawable.sample_car);
            case 4:
                return decodeResource(resources, R.drawable.sample_power);
            case 5:
                return decodeResource(resources, R.drawable.sample_norris);
            case 6:
                return decodeResource(resources, R.drawable.sample_aids);
            case 7:
                return decodeResource(resources, R.drawable.sample_guitar);
            case 8:
                return decodeResource(resources, R.drawable.sample_languages);
            case 9:
                return decodeResource(resources, R.drawable.sample_running);
        }
        return decodeResource(resources, R.drawable.sample_donkey);
    }
}
