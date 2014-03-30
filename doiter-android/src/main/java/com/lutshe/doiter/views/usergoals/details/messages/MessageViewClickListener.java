package com.lutshe.doiter.views.usergoals.details.messages;

import android.view.MotionEvent;
import android.view.View;
import com.lutshe.doiter.views.goals.MessageViewTemplateLayout;

public class MessageViewClickListener implements View.OnTouchListener {
    private MessageViewTemplateLayout messageView;

    public MessageViewClickListener(MessageViewTemplateLayout messageView) {
        this.messageView = messageView;
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                sendClick();
                break;
            default:
                break;
        }

        return true;
    }

    public void sendClick() {
        messageView.wrapAllContent();
        messageView.setSelected(true);
    }
}
