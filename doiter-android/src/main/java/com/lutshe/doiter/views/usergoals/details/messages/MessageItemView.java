package com.lutshe.doiter.views.usergoals.details.messages;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import com.lutshe.doiter.R;
import com.lutshe.doiter.model.Message;
import com.lutshe.doiter.views.common.DeviceScalingProperties;
import com.lutshe.doiter.views.goals.MessageViewTemplateLayout;
import com.lutshe.doiter.views.util.HtmlCodePreparer;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import static android.widget.AbsListView.LayoutParams.MATCH_PARENT;

/**
 * Created by Arsen Adzhiametov on 6/31/13.
 */
@EViewGroup(R.layout.message_list_item)
public class MessageItemView extends RelativeLayout {

    public static final int MESSAGES_PER_SCREEN = 4;

    @Bean HtmlCodePreparer htmlCodePreparer;
    @Bean DeviceScalingProperties deviceProperties;
    @Bean MessageViewClickListener clickListener;

    @ViewById(R.id.message_item) MessageViewTemplateLayout messageView;

    private int positionInList;
    private int croppedHeight;

    public MessageItemView(Context context) {
        super(context);
    }

    public MessageItemView bind(Message message, int position) {
        messageView.setOnTouchListener(clickListener);

        prepareLayout();
        setMessageData(message);

        positionInList = position;
        return this;
    }

    private void prepareLayout() {
        int itemHeight = deviceProperties.getScreenHeight() / MESSAGES_PER_SCREEN;
        setLayoutParams(new AbsListView.LayoutParams(MATCH_PARENT, itemHeight));
        ((MarginLayoutParams) messageView.getLayoutParams()).topMargin = itemHeight / 8;
        croppedHeight = itemHeight;
    }

    private void setMessageData(Message message) {
        Long orderIndex = message.getOrderIndex();
        messageView.loadMessage(message.getText());
        messageView.setMessageNumber(message.getType() == Message.Type.LAST ? ":)" : String.valueOf(orderIndex + 1));
        if (message.getType() == Message.Type.LAST) {
            messageView.resizeWhenReady();
        }
    }

    public int getPositionInList() {
        return positionInList;
    }

    public int getCroppedHeight() {
        return croppedHeight;
    }
}
