package com.lutshe.doiter.data.rest.clients;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Arturro on 04.09.13.
 */
public interface ImagesService {

    @GET("/image/{name}")
    Response getImage(@Path("name") String name);
}
