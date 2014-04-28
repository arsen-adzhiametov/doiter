package com.lutshe.doiter.views.util;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.widget.Toast;
import com.lutshe.doiter.R;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.Trace;
import org.androidannotations.annotations.res.DimensionRes;

import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Arsen Adzhiametov on 2/14/14 in IntelliJ IDEA.
 */
@EBean(scope = EBean.Scope.Singleton)
public class HtmlCodePreparer {

    @RootContext Context context;
    @DimensionRes(R.dimen.web_view_text_size) float fontSize;

    private final String bodyTemplate = "<body><div>%s</div></body>";

    private String head;

    @Trace
    @AfterInject
    void bind() {
        detectScreenSize();
        copyFile(context, "Gabriola.ttf");

        // Starting with KITKAT android's WebView is based Chromium instead of it's own implementation.
        // We use stupid Gabriola font which becomes stupid on some phones and looks fine on other.
        // Assumption is that it depends on WebView implementation which depends on android version.
        // To fix that we write extra hacky ccs for KITKAT and above.
        // Extra CSS shifts some blocks up/down with parameter overflow:hidden which cuts off everything that is out of parent's bounds (extra empty space in our case).
        boolean isChromiumBased = Build.VERSION.SDK_INT >= 19;//Build.VERSION_CODES.KITKAT;

        head = "<head>" +
                "<style>" +
                "@font-face {" +
                    "font-family: 'gabriola';" +
                    "src: url('file://" + context.getFilesDir().getAbsolutePath() + "/Gabriola.ttf') format('truetype');" +
                    "font-weight: normal;" +
                    "font-style: normal;" +
                "}" +
                "body {" +
                    "text-align: justify;" +
                    "font-family: 'gabriola'; " +
                    "font-size: " + fontSize/10 + "em; " +
                    "line-height: 99%;" +
                    "color: #4F8890;" +

                    // shifting body up to compensate the shift of div below
                    (isChromiumBased ? "position: relative;" + "bottom: " +fontSize/10 +"em;" + "overflow: hidden;" : "" ) +
                "}" +

                // shifting div with text lower
                (isChromiumBased ? "div {padding-top: "+ fontSize/40 +"em;position:absolute; top: " + fontSize/10 +"em; overflow: hidden;}" : "") +

                "</style>" +
                "</head>";
    }

    // temporal method. Should be removed
    private void detectScreenSize() {
        if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            Toast.makeText(context, "Large screen", Toast.LENGTH_LONG).show();

        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            Toast.makeText(context, "Normal sized screen", Toast.LENGTH_LONG)
                    .show();

        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            Toast.makeText(context, "Small sized screen", Toast.LENGTH_LONG)
                    .show();

        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            Toast.makeText(context, "xLarge sized screen", Toast.LENGTH_LONG)
                    .show();

        } else {
            Toast.makeText(context, "Screen size is neither large, normal or small", Toast.LENGTH_LONG).show();

        }
        System.out.println("fontSize = " + fontSize);
    }

    public String getHtmlCode(String data) {
        String body = String.format(bodyTemplate, data);
        return "<html>" + head + body + "</html>";
    }

    private void copyFile(Context context, String fileName) {
        try {
            FileOutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            InputStream in = context.getAssets().open("fonts/" + fileName);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
