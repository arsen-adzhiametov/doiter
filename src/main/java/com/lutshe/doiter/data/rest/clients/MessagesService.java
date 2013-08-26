package com.lutshe.doiter.data.rest.clients;

import com.googlecode.androidannotations.annotations.EBean;
import com.lutshe.doiter.data.model.Message;

import retrofit.http.GET;

/**
 * Created by Arturro on 23.08.13.
 */
public interface MessagesService {
    @GET("/goals/{goalId}/messages")
    Message[] getAllMessagesForGoal(Long id);

    @GET("/goals/{goalId}/messages/{firstMessageNum}/{numberOfMessages}")
    Message[] getMessagesForGoal(Long id, Long firstMessageNum, Long numberOfMessages);
}
