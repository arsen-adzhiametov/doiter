package com.lutshe.doiter.data.rest.clients;

import com.lutshe.doiter.data.model.Message;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Arturro on 23.08.13.
 */
public interface MessagesService {
    @GET("/goals/{goalId}/messages")
    Message[] getAllMessagesForGoal(Long goalId);

    @GET("/goals/{goalId}/messages/{firstMessageNum}/{numberOfMessages}")
    Message[] getMessagesForGoal(@Path("goalId") Long goalId, @Path("firstMessageNum") Long firstMessageNum, @Path("numberOfMessages") Long numberOfMessages);
}
