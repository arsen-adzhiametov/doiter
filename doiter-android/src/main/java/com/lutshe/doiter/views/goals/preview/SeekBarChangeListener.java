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

    @ViewById(R.id.daysSetting)
    TextView days;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String text = progress + " day";
        if (!String.valueOf(progress).endsWith("1")){
            text = text + "s";
        }
        days.setText(text);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
