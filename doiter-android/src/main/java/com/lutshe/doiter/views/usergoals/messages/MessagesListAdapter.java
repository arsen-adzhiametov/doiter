package com.lutshe.doiter.views.usergoals.messages;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.model.Message;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.Trace;

/**
 * Created by Arsen Adzhiametov on goal6/31/13.
 */

@EBean
public class MessagesListAdapter extends BaseAdapter {

    private Long goalId;
    private Message[] messages;

    @Bean
    MessagesDao messagesDao;

    @Bean
    MessageViewClickListener messageViewClickListener;

    @RootContext
    Context context;

    public void initAdapter(Long goalId, ListView listView){
        this.goalId = goalId;
        loadMessages(goalId);
        messageViewClickListener.prepare(listView);
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
    @Trace
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageItemView messageItemView = (MessageItemView) convertView;

        if (messageItemView == null) {
            messageItemView = MessageItemView_.build(context);
        }
        Message message = getItem(position);
        return messageItemView.bind(message, position);
    }

    @Override
    public void notifyDataSetChanged() {
        loadMessages(goalId);
        super.notifyDataSetChanged();
    }

    private void loadMessages(Long goalId) {
        messages = messagesDao.getAllMessages(goalId);
    }
}
