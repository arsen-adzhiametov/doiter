package com.lutshe.doiter.views.usergoals.details.messages;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import com.lutshe.doiter.views.common.DeviceScalingProperties;
import com.lutshe.doiter.views.goals.MessageViewTemplateLayout;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class MessageViewClickListener implements View.OnTouchListener {

    private MessageViewTemplateLayout lastSelectedView;

    @Bean DeviceScalingProperties scalingProperties;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                sendClick(findParent(MessageViewTemplateLayout.class, view));
                break;
            default:
                break;
        }

        return true;
    }

    public void sendClick(MessageViewTemplateLayout messageView) {
        messageView.wrapAllContent();
        messageView.setSelected(true);

        final ListView listView = findParent(ListView.class, (View) messageView.getParent());
        final int scrollTo = listView.getHeight() / 4;

        final MessageItemView itemView = findParent(MessageItemView.class, (View) messageView.getParent());

        if (lastSelectedView != null && lastSelectedView != messageView) {
            final MessageItemView lastItem = findParent(MessageItemView.class, (View) lastSelectedView.getParent());
            lastSelectedView.crop(lastItem.getCroppedHeight());
            lastSelectedView.setSelected(false);
        }
        lastSelectedView = messageView;

        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.smoothScrollToPositionFromTop(itemView.getPositionInList(), scrollTo);
            }
        });
    }

    private <T> T findParent(Class<T> parentType, View view) {
        if (parentType.isAssignableFrom(view.getClass()) || view == null) {
            return (T) view;
        } else {
            return findParent(parentType, (View) view.getParent());
        }
    }
}
