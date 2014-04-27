package com.lutshe.doiter.views.goals;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.ScalableImageView;
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

    public MessageViewTemplateLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
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
        goalDescriptionWebView.setHorizontalScrollBarEnabled(false);

        goalDescriptionWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
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
    public void setOnTouchListener(OnTouchListener listener) {
        setClickable(true);
        goalDescriptionWebView.setOnTouchListener(listener);
    }

    public void resizeWhenReady() {
        goalDescriptionWebView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (goalDescriptionWebView.getHeight() == 0) {
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

        int desiredHeight = goalDescriptionWebView.getHeight();
        int realHeight = ((ViewGroup) goalDescriptionWebView.getParent()).getHeight();

        int dh = desiredHeight - realHeight;

        if (dh > 0) {
            ViewGroup rootView = (ViewGroup) parent.getParent();
            rootView.getLayoutParams().height += dh;
            rootView.requestLayout();
        }

        return desiredHeight;
    }

    public void crop(int height) {
        ViewGroup parent = (ViewGroup) getParent().getParent();
        parent.getLayoutParams().height = height;
        parent.requestLayout();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        dividerLine.setSelected(selected);
        dottedLineFooter.setSelected(selected);
    }
}
