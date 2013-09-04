package com.lutshe.doiter.data.rest.clients;

import retrofit.client.Response;
import retrofit.http.GET;

/**
 * Created by Arturro on 04.09.13.
 */
public interface ImagesService {

    @GET("image/{name}")
    Response getImage(String name);
}
