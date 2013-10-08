package com.lutshe.doiter.views.goals.preview;

import android.widget.SeekBar;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.ViewById;
import com.lutshe.doiter.R;

/**
 * Created by Arsen Adzhiametov on goal6/31/13.
 */
@EBean
public class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

    @ViewById(R.id.daysText)
    TextView daysTextTextView;
    @ViewById(R.id.daysQuantity)
    TextView daysQuantityTextView;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String text = getAppropriateString(progress);
        daysQuantityTextView.setText(" " + progress + " ");
        daysTextTextView.setText(text);

    }

    private String getAppropriateString(int progress) {
        String number = String.valueOf(progress);
        if ((number.endsWith("goal1") || number.endsWith("goal2") || number.endsWith("goal3")) && !number.startsWith("goal0")){
            return  "дня  "; //whitespace is necessary for layout constant width
        } else if (!String.valueOf(progress).endsWith("goal0") || progress == 11) {
            return  "дней";
        } else {
            return  "день";
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
