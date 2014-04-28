package com.lutshe.doiter.views.goals.preview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.common.OurFont;
import com.lutshe.doiter.views.goals.MessageViewTemplateLayout;
import com.lutshe.doiter.views.util.StringUtils;
import org.androidannotations.annotations.*;

import static com.lutshe.doiter.AchievementTimeConstants.SEEK_BAR_MIN_DAYS_AMOUNT;

/**
 * Created by Arsen Adzhiametov on 2/22/14 in IntelliJ IDEA.
 */
@EViewGroup(R.layout.goal_preview_layout)
public class GoalPreviewLayout extends RelativeLayout {

    @ViewById(R.id.goal_name)TextView goalNameTextView;
    @ViewById(R.id.days_quantity)TextView daysQuantityTextView;
    @ViewById(R.id.i_will_do_it_in)TextView iWillDoItInTextView;
    @ViewById(R.id.days_text)TextView daysTextTextView;
    @ViewById(R.id.add_goal_text)TextView addGoalTextView;
    @ViewById(R.id.goal_description_message_view) MessageViewTemplateLayout goalDescriptionMessageView;
    @ViewById(R.id.seekbar)SeekBar seekBar;
    @ViewById(R.id.seek_bar_bg)ImageView seekBarBackground;
    @ViewById(R.id.message_number) TextView messageNumberTextView;

    @Bean OurFont font;

    public GoalPreviewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    public void bindData() {
        setTopMenuVisibility(View.INVISIBLE);
        scaleSeekBarThumb();
        setTypefaceToTextViews();
    }

    @SeekBarProgressChange(R.id.seekbar)
    public void onProgressChanged(int progress) {
        int pseudoProgress = progress + SEEK_BAR_MIN_DAYS_AMOUNT;
        String text = StringUtils.getDayOrDaysString(pseudoProgress);
        daysQuantityTextView.setText(" " + pseudoProgress + " ");
        daysTextTextView.setText(text);
    }

    public void setSeekBarMaximum(int progressMaxValue){
        seekBar.setMax(progressMaxValue - SEEK_BAR_MIN_DAYS_AMOUNT);
    }

    public int getSeekBarCurrentValue(){
        return seekBar.getProgress() + SEEK_BAR_MIN_DAYS_AMOUNT;
    }

    private void scaleSeekBarThumb() {
        ViewTreeObserver vto = seekBar.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                Resources res = getContext().getResources();
                int height = seekBarBackground.getMeasuredHeight();
                int weight = height;
                Bitmap bmpOrg = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.circle);
                Bitmap bmpScaled = Bitmap.createScaledBitmap(bmpOrg, weight, height, true);
                Drawable newThumb = new BitmapDrawable(res, bmpScaled);
                newThumb.setBounds(0, 0, newThumb.getIntrinsicWidth(), newThumb.getIntrinsicHeight());
                seekBar.setThumb(newThumb);
                seekBar.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    private void setTypefaceToTextViews() {
        Typeface typeface = font.get();
        goalNameTextView.setTypeface(typeface);
        daysQuantityTextView.setTypeface(typeface);
        iWillDoItInTextView.setTypeface(typeface);
        daysTextTextView.setTypeface(typeface);
        addGoalTextView.setTypeface(typeface);
        messageNumberTextView.setTypeface(typeface);
    }

    protected void setTopMenuVisibility(int visibility) {
        View topMenuSlidingDrawer = ((Activity)getContext()).findViewById(R.id.sliding_layout);
        topMenuSlidingDrawer.setVisibility(visibility);
    }

    public void showGoalName(String goalName){
        goalNameTextView.setText(goalName);
    }

    public void showGoalDescription(String goalDescription){
        goalDescriptionMessageView.loadMessage(goalDescription);
    }

}
