package com.lutshe.doiter.data.rest.clients;

import com.lutshe.doiter.dto.MessageDTO;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Arturro on 23.08.13.
 */
public interface MessagesService {
    @GET("/json.v1.goals/{goalId}/messages")
    MessageDTO[] getAllMessagesForGoal(Long goalId);

    @GET("/json.v1.goals/{goalId}/messages/{firstMessageNum}/{numberOfMessages}")
    MessageDTO[] getMessagesForGoal(@Path("goalId") Long goalId, @Path("firstMessageNum") Long firstMessageNum,
                                  @Path("numberOfMessages") Long numberOfMessages);
}
