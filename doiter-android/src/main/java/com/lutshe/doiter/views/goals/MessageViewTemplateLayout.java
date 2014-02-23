package com.lutshe.doiter.views.goals;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.util.HtmlCodePreparer;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Arsen Adzhiametov on 2/23/14 in IntelliJ IDEA.
 */
@EViewGroup(R.layout.message_view_template_layout)
public class MessageViewTemplateLayout extends RelativeLayout {

    @ViewById(R.id.message_number)TextView messageNumberTextView;
    @ViewById(R.id.web_view_content)WebView goalDescriptionWebView;

    @Bean HtmlCodePreparer htmlCodePreparer;

    public MessageViewTemplateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void loadMessage(String messageText){
        String htmlCode = htmlCodePreparer.getHtmlCode(messageText);
        goalDescriptionWebView.loadDataWithBaseURL(null, htmlCode, "text/html", "utf-8", "about:blank");
        goalDescriptionWebView.setBackgroundColor(Color.TRANSPARENT);
        goalDescriptionWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
    }

    public void setMessageNumber(int messageNumber){
         messageNumberTextView.setText(messageNumber);
    }
}
