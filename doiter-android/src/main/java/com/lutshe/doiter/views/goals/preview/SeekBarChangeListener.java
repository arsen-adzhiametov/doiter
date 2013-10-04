package com.lutshe.doiter.views.goals.preview;

import android.widget.SeekBar;
import android.widget.TextView;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.ViewById;
import com.lutshe.doiter.R;

/**
 * Created by Arsen Adzhiametov on 7/31/13.
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
        if ((number.endsWith("2") || number.endsWith("3") || number.endsWith("4")) && !number.startsWith("1")){
            return  "дня  "; //whitespace is necessary for layout constant width
        } else if (!String.valueOf(progress).endsWith("1") || progress == 11) {
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
