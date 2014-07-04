package com.lutshe.doiter.views.usergoals.messages;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.ScalableImageView;
import com.lutshe.doiter.views.common.DeviceScalingProperties;
import com.lutshe.doiter.views.common.OurFont;
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

    @ViewById(R.id.message_number) TextView messageNumberTextView;
    @ViewById(R.id.web_view_content) WebView goalDescriptionWebView;
    @ViewById(R.id.divider_line) ScalableImageView dividerLine;
    @ViewById(R.id.dotted_line_footer) View dottedLineFooter;

    @Bean HtmlCodePreparer htmlCodePreparer;
    @Bean OurFont font;
    @Bean DeviceScalingProperties properties;

    public MessageViewTemplateLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @AfterViews
    public void setDefaultSettings() {
        goalDescriptionWebView.getSettings().setBuiltInZoomControls(false);
        goalDescriptionWebView.getSettings().setDisplayZoomControls(false);
        goalDescriptionWebView.getSettings().setJavaScriptEnabled(false);
        goalDescriptionWebView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);

        goalDescriptionWebView.setLongClickable(false);
        goalDescriptionWebView.setFocusable(false);
        goalDescriptionWebView.setFocusableInTouchMode(false);
        goalDescriptionWebView.setVerticalScrollBarEnabled(false);
        goalDescriptionWebView.setHorizontalScrollBarEnabled(false);

        goalDescriptionWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        goalDescriptionWebView.getSettings().setAllowContentAccess(false);
        goalDescriptionWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        goalDescriptionWebView.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);

        goalDescriptionWebView.setBackgroundColor(Color.TRANSPARENT);
        goalDescriptionWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        messageNumberTextView.setTypeface(font.get());
    }

    public void loadMessage(String messageText) {
        String htmlCode = htmlCodePreparer.getHtmlCode(messageText);
        goalDescriptionWebView.loadDataWithBaseURL(null, htmlCode, "text/html", "utf-8", "about:blank");
    }

    public void setMessageNumber(String messageNumber){
         messageNumberTextView.setText(messageNumber);
    }

    @Override
    public void setOnTouchListener(final OnTouchListener listener) {
        setClickable(true);
        // forwarding events to WebView
        super.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                listener.onTouch(goalDescriptionWebView, event);
                return false;
            }
        });
        goalDescriptionWebView.setOnTouchListener(listener);
    }

    public void resizeWhenReady() {
        goalDescriptionWebView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (goalDescriptionWebView.getHeight() == 0 || goalDescriptionWebView.getContentHeight() == 0) {
                    return false;
                }
                wrapAllContent();
                forceLayout();
                goalDescriptionWebView.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
    }

    public int wrapAllContent() {
        ViewGroup parent = (ViewGroup) getParent();
        int desiredHeight = (int) (goalDescriptionWebView.getContentHeight() * getContext().getResources().getDisplayMetrics().density);// + layoutParams.topMargin + layoutParams.bottomMargin;
        int realHeight = goalDescriptionWebView.getHeight();

        int dh = desiredHeight - realHeight;

        if (dh > 0) {
            final ViewGroup rootView = (ViewGroup) parent.getParent();
            rootView.startAnimation(new MessageSelectionAnimation(rootView.getLayoutParams().height, dh, rootView));
        }

        return desiredHeight;
    }

    public void crop(int height) {
        ViewGroup parent = (ViewGroup) getParent().getParent();

        int currentHeight = parent.getLayoutParams().height;
        parent.startAnimation(new MessageSelectionAnimation(currentHeight, height - currentHeight, parent));
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        dividerLine.setSelected(selected);
        dottedLineFooter.setSelected(selected);
    }
}
