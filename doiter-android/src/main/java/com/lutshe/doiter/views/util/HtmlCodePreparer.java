package com.lutshe.doiter.views.util;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;
import com.lutshe.doiter.R;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
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

    private final String bodyTemplate = "<body> %s </body>";

    private String head;

    @AfterInject
    void bind() {
        detectScreenSize();
        copyFile(context, "Gabriola.ttf");
        head = "<head>" +
                "<style>" +
                "@font-face {" +
                "font-family: 'Gabriola';" +
                "src: url('file://" + context.getFilesDir().getAbsolutePath() + "/Gabriola.ttf');" +
                "}" +
                "body {" +
                "text-align: justify;" +
                "font-family: 'Gabriola'; " +
                "font-size: " + fontSize/10 + "em; " +
                "line-height: 95%;" +
                "color: #4F8890" +
                "}" +
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
