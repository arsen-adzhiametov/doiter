//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.lutshe.doiter.views.usergoals.details.messages;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import com.lutshe.doiter.R.id;
import com.lutshe.doiter.R.layout;


/**
 * We use @SuppressWarning here because our java code
 * generator doesn't know that there is no need
 * to import OnXXXListeners from View as we already
 * are in a View.
 * 
 */
@SuppressWarnings("unused")
public final class MessageItemView_
    extends MessageItemView
{

    private Context context_;
    private boolean mAlreadyInflated_ = false;

    public MessageItemView_(Context context) {
        super(context);
        init_();
    }

    private void init_() {
        context_ = getContext();
        if (context_ instanceof Activity) {
            Activity activity = ((Activity) context_);
        }
    }

    private void afterSetContentView_() {
        messageText = ((TextView) findViewById(id.messageText));
    }

    /**
     * The mAlreadyInflated_ hack is needed because of an Android bug
     * which leads to infinite calls of onFinishInflate()
     * when inflating a layout with a parent and using
     * the <merge /> tag.
     * 
     */
    @Override
    public void onFinishInflate() {
        if (!mAlreadyInflated_) {
            mAlreadyInflated_ = true;
            inflate(getContext(), layout.message_list_item, this);
            afterSetContentView_();
        }
        super.onFinishInflate();
    }

    public static MessageItemView build(Context context) {
        MessageItemView_ instance = new MessageItemView_(context);
        instance.onFinishInflate();
        return instance;
    }

}
