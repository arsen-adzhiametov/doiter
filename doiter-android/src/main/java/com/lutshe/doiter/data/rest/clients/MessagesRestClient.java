package com.lutshe.doiter.data.rest.clients;

import android.content.Context;
import com.lutshe.doiter.R;
import com.lutshe.doiter.dto.MessageDTO;
import org.androidannotations.annotations.EBean;

/**
 * Created by Arturro on 26.08.13.
 */
@EBean
public class MessagesRestClient extends AbstractRestClient<MessagesService> implements MessagesService {

    protected MessagesRestClient(Context context) {
        super(MessagesService.class, context.getResources().getString(R.string.serverUrl));
    }

    @Override
    public MessageDTO[] getAllMessagesForGoal(Long id) {
        return service.getAllMessagesForGoal(id);
    }

    @Override
    public MessageDTO[] getMessagesForGoal(Long id, Long firstMessageNum, Long numberOfMessages) {
        return service.getMessagesForGoal(id, firstMessageNum, numberOfMessages);
    }
}
