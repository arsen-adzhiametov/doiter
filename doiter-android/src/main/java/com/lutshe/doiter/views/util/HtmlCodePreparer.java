package com.lutshe.doiter.views.util;

import android.content.Context;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by Arsen Adzhiametov on 2/14/14 in IntelliJ IDEA.
 */
@EBean(scope = EBean.Scope.Singleton)
public class HtmlCodePreparer {

    @RootContext
    Context context;

    private final String bodyTemplate = "<body> %s </body>";

    private String head;

    @AfterInject
    void bind(){
        copyFile(context, "Gabriola.ttf");
        head = "<head>" +
                    "<style>" +
                        "@font-face {" +
                            "font-family: 'Gabriola';" +
                            "src: url('file://"+context.getFilesDir().getAbsolutePath()+ "/Gabriola.ttf');" +
                        "}" +
                        "body {" +
                            "text-align: justify;" +
                            "font-family: 'Gabriola'; " +
                            "font-size: 1.5em; " +
                            "line-height: 95%;" +
                            "color: #4F8890" +
                        "}" +
                    "</style>" +
                "</head>";
    }

    public String getHtmlCode(String data){
        String body = String.format(bodyTemplate, data);
        return "<html>"+head+body+"</html>";
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
