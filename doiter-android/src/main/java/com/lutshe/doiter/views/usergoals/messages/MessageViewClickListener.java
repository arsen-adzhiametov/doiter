package com.lutshe.doiter.views.usergoals.messages;

import android.view.MotionEvent;
import android.view.View;
import org.androidannotations.annotations.EBean;

import static com.lutshe.doiter.views.util.ViewUtils.findParent;

@EBean(scope = EBean.Scope.Singleton)
public class MessageViewClickListener implements View.OnTouchListener {

    private MessageViewTemplateLayout lastSelectedView;
    private Integer lastSelectedPosition;

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

        return false;
    }

    public synchronized void sendClick(MessageViewTemplateLayout messageView) {
        messageView.wrapAllContent();
        messageView.setSelected(true);

        final MessageItemView itemView = findParent(MessageItemView.class, (View) messageView.getParent());
        lastSelectedPosition = itemView.getPositionInList();

        boolean selectionChanged = lastSelectedView != messageView;
        if (selectionChanged) {
            final MessageItemView lastItem = findParent(MessageItemView.class, (View) lastSelectedView.getParent());
            lastSelectedView.crop(lastItem.getCroppedHeight());
            lastSelectedView.setSelected(false);
        }
        lastSelectedView = messageView;
    }

    public boolean hasSelection() {
        return lastSelectedPosition != null;
    }

    public synchronized void setDefaultSelection(MessageViewTemplateLayout messageView, int positionInList) {
        messageView.setSelected(true);
        messageView.resizeWhenReady();
        lastSelectedView = messageView;
        lastSelectedPosition = positionInList;
    }

    public void prepare() {
        lastSelectedPosition = null;
        lastSelectedView = null;
    }
}
