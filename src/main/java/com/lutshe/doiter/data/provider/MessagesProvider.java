package com.lutshe.doiter.data.provider;

import com.lutshe.doiter.data.model.Message;

import java.util.List;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */

public interface MessagesProvider {

    List<Message> getMessages(Long goalId);

    List<Message> getMessages(Long goalId, Long lastMessageId);
}
