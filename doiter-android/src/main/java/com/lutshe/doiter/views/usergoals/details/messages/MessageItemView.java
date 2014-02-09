package com.lutshe.doiter.views.usergoals.details.messages;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import com.lutshe.doiter.R;
import com.lutshe.doiter.model.Message;

/**
 * Created by Arsen Adzhiametov on goal6/31/13.
 */

@EViewGroup(R.layout.message_list_item)
public class MessageItemView extends LinearLayout {

    @ViewById(R.id.messageText)
    TextView messageText;

    public MessageItemView(Context context) {
        super(context);
    }

    public MessageItemView bind(Message message) {
        messageText.setText(message.getText());
        return this;
    }
}
