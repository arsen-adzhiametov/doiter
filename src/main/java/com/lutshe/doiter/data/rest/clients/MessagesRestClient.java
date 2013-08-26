package com.lutshe.doiter.data.rest.clients;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.lutshe.doiter.data.model.Message;

import retrofit.RestAdapter;

/**
 * Created by Arturro on 26.08.13.
 */
@EBean
public class MessagesRestClient implements  MessagesService {
    private static MessagesService messagesService;

    private static synchronized MessagesService service() {
        if (messagesService == null) {
            RestAdapter adapter = new RestAdapter.Builder().setServer("http://192.168.1.17:8844/api/json/v1").setDebug(true).build();
            messagesService = adapter.create(MessagesService.class);
        }
        return messagesService;
    }

    @Override
    public Message[] getAllMessagesForGoal(Long id) {
        return service().getAllMessagesForGoal(id);
    }

    @Override
    public Message[] getMessagesForGoal(Long id, Long firstMessageNum, Long numberOfMessages) {
        return service().getMessagesForGoal(id, firstMessageNum, numberOfMessages);
    }
}
