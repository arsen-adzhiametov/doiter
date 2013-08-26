//
// DO NOT EDIT THIS FILE, IT HAS BEEN GENERATED USING AndroidAnnotations.
//


package com.lutshe.doiter.data.database;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import com.googlecode.androidannotations.api.BackgroundExecutor;
import com.lutshe.doiter.data.database.dao.GoalsDao_;
import com.lutshe.doiter.data.database.dao.MessagesDao_;

public final class TestDataSetup_
    extends TestDataSetup
{

    private Context context_;

    private TestDataSetup_(Context context) {
        context_ = context;
        init_();
    }

    public void afterSetContentView_() {
        if (!(context_ instanceof Activity)) {
            return ;
        }
        ((GoalsDao_) goalsDao).afterSetContentView_();
        ((MessagesDao_) messagesDao).afterSetContentView_();
    }

    /**
     * You should check that context is an activity before calling this method
     * 
     */
    public View findViewById(int id) {
        Activity activity_ = ((Activity) context_);
        return activity_.findViewById(id);
    }

    @SuppressWarnings("all")
    private void init_() {
        if (context_ instanceof Activity) {
            Activity activity = ((Activity) context_);
        }
        goalsDao = GoalsDao_.getInstance_(context_);
        messagesDao = MessagesDao_.getInstance_(context_);
    }

    public static TestDataSetup_ getInstance_(Context context) {
        return new TestDataSetup_(context);
    }

    public void rebind(Context context) {
        context_ = context;
        init_();
    }

    @Override
    public void setup() {
        if (Log.isLoggable("TestDataSetup", Log.INFO)) {
            long start = System.currentTimeMillis();
            Log.i("TestDataSetup", "Entering [setup()]");
            try {
                TestDataSetup_.super.setup();
            } finally {
                long duration = (System.currentTimeMillis()-start);
                Log.i("TestDataSetup", ("Exiting [setup()], duration in ms: "+ duration));
            }
        } else {
            TestDataSetup_.super.setup();
        }
    }

    @Override
    public void setupMessages() {
        BackgroundExecutor.execute(new Runnable() {


            @Override
            public void run() {
                try {
                    TestDataSetup_.super.setupMessages();
                } catch (RuntimeException e) {
                    Log.e("TestDataSetup_", "A runtime exception was thrown while executing code in a runnable", e);
                }
            }

        }
        );
    }

}
