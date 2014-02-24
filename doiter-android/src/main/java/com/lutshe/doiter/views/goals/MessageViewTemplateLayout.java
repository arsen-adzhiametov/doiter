package com.lutshe.doiter.views.goals;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.util.HtmlCodePreparer;
import org.androidannotations.annotations.AfterViews;
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

    @AfterViews
    public void setDefaultSettings() {
        ScrollView parent = (ScrollView) goalDescriptionWebView.getParent();

        parent.setVerticalScrollBarEnabled(false);
        parent.setClickable(false);
        parent.setLongClickable(false);
        parent.setFocusable(false);
        parent.setFocusableInTouchMode(false);

        goalDescriptionWebView.getSettings().setBuiltInZoomControls(false);
        goalDescriptionWebView.getSettings().setDisplayZoomControls(false);
        goalDescriptionWebView.getSettings().setJavaScriptEnabled(false);
        goalDescriptionWebView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);

        goalDescriptionWebView.setLongClickable(false);
        goalDescriptionWebView.setFocusable(false);
        goalDescriptionWebView.setFocusableInTouchMode(false);
        goalDescriptionWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        goalDescriptionWebView.setVerticalScrollBarEnabled(false);

        goalDescriptionWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        goalDescriptionWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        goalDescriptionWebView.setBackgroundColor(Color.TRANSPARENT);
        goalDescriptionWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
    }

    public void loadMessage(String messageText) {
        String htmlCode = htmlCodePreparer.getHtmlCode(messageText);
        goalDescriptionWebView.loadDataWithBaseURL(null, htmlCode, "text/html", "utf-8", "about:blank");
    }

    public void setMessageNumber(String messageNumber){
         messageNumberTextView.setText(messageNumber);
    }

    @Override
    public void setOnTouchListener(OnTouchListener listener) {
        setClickable(true);
        goalDescriptionWebView.setOnTouchListener(listener);
    }

    public void resizeWhenReady() {
        goalDescriptionWebView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (v == goalDescriptionWebView && bottom != 0) {
                    wrapAllContent();
                    goalDescriptionWebView.removeOnLayoutChangeListener(this);
                }
            }
        });
    }

    public synchronized void wrapAllContent() {
        ViewGroup parent = (ViewGroup) getParent();

        int desiredHeight = goalDescriptionWebView.getHeight();
        int realHeight = ((ViewGroup) goalDescriptionWebView.getParent()).getHeight();

        int dh = desiredHeight - realHeight;

        if (dh > 0) {
            ViewGroup rootView = (ViewGroup) parent.getParent();
            rootView.getLayoutParams().height += dh;
            rootView.requestLayout();
        }
    }
}