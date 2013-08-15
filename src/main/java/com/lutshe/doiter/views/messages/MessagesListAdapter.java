package com.lutshe.doiter.views.messages;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.googlecode.androidannotations.annotations.*;
import com.lutshe.doiter.data.model.Message;
import com.lutshe.doiter.data.provider.MessagesProvider;
import com.lutshe.doiter.data.provider.stub.MessagesProviderStub;

import java.util.List;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */

@EBean
public class MessagesListAdapter extends BaseAdapter {

    private List<Message> messages;

    @Bean(MessagesProviderStub.class)
    MessagesProvider messagesProvider;

    @RootContext
    Context context;

    public void initAdapter(Long goalId){
        messages = messagesProvider.getMessages(goalId);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageItemView messageItemView = (MessageItemView) convertView;

        if (messageItemView == null) {
            messageItemView = MessageItemView_.build(context);
        }
        Message message = getItem(position);
        return messageItemView.bind(message);
    }
}
