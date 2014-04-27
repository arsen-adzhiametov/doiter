package com.lutshe.doiter.views.usergoals.details;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.common.OurFont;
import com.lutshe.doiter.views.util.StringUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Arsen Adzhiametov on 27-Apr-14 in IntelliJ IDEA.
 */
@EViewGroup(R.layout.user_goal_details_layout)
public class UserGoalDetailLayout extends RelativeLayout {

    @ViewById(R.id.remaining)TextView remainingTextView;
    @ViewById(R.id.days_quantity)TextView daysQuantityTextView;
    @ViewById(R.id.days_text)TextView daysTextTextView;
    @ViewById(R.id.goal_name)TextView goalNameTextView;
    @ViewById(R.id.more_tips_button_text)TextView backToAllButtonTextView;
    @ViewById(R.id.goal_cover)ImageView goalCover;
    @ViewById(R.id.message_number)TextView messageNumberTextView;
    @ViewById(R.id.web_view_content)WebView messageTextWebView;
    @ViewById(R.id.next_arrow_button)ImageView nextButton;
    @ViewById(R.id.prev_arrow_button)ImageView previousButton;

    @Bean OurFont font;

    public UserGoalDetailLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    public void bindData() {
        setTypefaceToTextViews();
    }

    public void showGoalName(String goalName){
        goalNameTextView.setText(goalName);
    }

    public void showDaysRemaining(int daysRemaining) {
        daysQuantityTextView.setText(' ' + String.valueOf(daysRemaining) + ' ');
        daysTextTextView.setText(StringUtils.getDayOrDaysString(daysRemaining));
    }

    public void showGoalCover(Bitmap bitmap) {
        goalCover.setImageBitmap(bitmap);
    }

    public void showCurrentMessageOnWebView(String htmlMessage) {
        messageTextWebView.loadDataWithBaseURL(null, htmlMessage, "text/html", "utf-8", "about:blank");
        messageTextWebView.setBackgroundColor(Color.TRANSPARENT);
        messageTextWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
    }

    public void hideNavigationButtons(){
        nextButton.setVisibility(View.INVISIBLE);
        previousButton.setVisibility(View.INVISIBLE);
    }

    private void setTypefaceToTextViews() {
        Typeface typeface = font.get();
        remainingTextView.setTypeface(typeface);
        daysQuantityTextView.setTypeface(typeface);
        daysTextTextView.setTypeface(typeface);
        goalNameTextView.setTypeface(typeface);
        backToAllButtonTextView.setTypeface(typeface);
        messageNumberTextView.setTypeface(typeface);
    }
}
