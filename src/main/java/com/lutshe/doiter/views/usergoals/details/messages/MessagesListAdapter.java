package com.lutshe.doiter.views.usergoals.details.messages;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.model.Message;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
 */

@EBean
public class MessagesListAdapter extends BaseAdapter {

    private Message[] messages;

    @Bean
    MessagesDao messagesDao;

    @RootContext
    Context context;

    public void initAdapter(Long goalId){
        messages = messagesDao.getAllMessages(goalId);
    }

    @Override
    public int getCount() {
        return messages.length;
    }

    @Override
    public Message getItem(int position) {
        return messages[position];
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
