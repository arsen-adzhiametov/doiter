package com.lutshe.doiter.data.rest.clients;

import retrofit.RestAdapter;

/**
 * Created by Arturro on 26.08.13.
 */
public abstract class AbstractRestClient<T> {
    protected T service;

    protected AbstractRestClient(Class<T> c, String serviceUrl) {
        RestAdapter adapter = new RestAdapter.Builder().setServer(serviceUrl + "/api/json/v1").setDebug(true).build();
        service = adapter.create(c);
    }
}
