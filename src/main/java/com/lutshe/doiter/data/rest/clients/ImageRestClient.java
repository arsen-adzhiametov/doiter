package com.lutshe.doiter.data.rest.clients;

import android.content.Context;

import com.googlecode.androidannotations.annotations.EBean;
import com.lutshe.doiter.R;

import retrofit.client.Response;

/**
 * Created by Arturro on 04.09.13.
 */
@EBean
public class ImageRestClient extends AbstractRestClient<ImagesService> implements ImagesService {

    protected ImageRestClient(Context context) {
        super(ImagesService.class, context.getResources().getString(R.string.serverUrl));
    }

    @Override
    public Response getImage(String name) {
        return service.getImage(name);
    }
}
