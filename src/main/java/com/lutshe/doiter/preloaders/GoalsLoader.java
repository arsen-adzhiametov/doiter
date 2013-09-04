package com.lutshe.doiter.preloaders;

import android.app.AlarmManager;
import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.Trace;
import com.googlecode.androidannotations.annotations.res.StringRes;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.model.Message;
import com.lutshe.doiter.data.rest.clients.GoalsRestClient;
import com.lutshe.doiter.data.rest.clients.ImageRestClient;
import com.lutshe.doiter.data.rest.clients.MessagesRestClient;

import org.apache.http.HttpStatus;

import java.io.IOException;

import retrofit.client.Response;

import static com.lutshe.doiter.views.util.IoUtils.getBitmap;
import static com.lutshe.doiter.views.util.IoUtils.writeBitmapToFile;

/**
 * Created by Arturro on 26.08.13.
 */
@EBean
class GoalsLoader implements Loader {
    public static final long LOADING_INTERVAL = AlarmManager.INTERVAL_DAY * 7;

    @Bean GoalsDao goalsDao;
    @Bean MessagesDao messagesDao;

    @Bean GoalsRestClient goalsRestClient;
    @Bean MessagesRestClient messagesRestClient;
    @Bean ImageRestClient imageRestClient;

    @StringRes(R.string.images_dir)
    String imagesDir;

    @Override
    public long getLoadingInterval() {
        return LOADING_INTERVAL;
    }

    @Trace
    @Override
    public void load() {
        new java.io.File(imagesDir).mkdirs();

        Goal[] allGoals = goalsRestClient.getAllGoals();
        for (Goal goal : allGoals) {
            storeGoal(goal);
        }
    }

    private void storeGoal(Goal goal) {
        if (goalsDao.getGoal(goal.getId()) == null) {
            Log.d(GoalsLoader.class.getName(), "adding new goal " + goal.toString());
            loadMessagesForGoal(goal);
            loadImageForGoal(goal);
            goalsDao.addGoal(goal);
        }
    }

    private void loadMessagesForGoal(Goal goal) {
        Message[] messages = messagesRestClient.getMessagesForGoal(goal.getId(), 0L, 10L);
        Log.d(GoalsLoader.class.getName(), "number of messages: " + messages.length);
        for (Message message : messages) {
            messagesDao.addMessage(message);
        }
    }

    private void loadImageForGoal(Goal goal) {
        Response res = imageRestClient.getImage(goal.getImageName());

        if (res.getStatus() != HttpStatus.SC_OK) {
            Log.w(GoalsLoader.class.getName(), "server return status " + res.getStatus() + " for image " + goal.getImageName());
            return;
        }

        try {

            Bitmap bitmap = getBitmap(res.getBody().in());
            writeBitmapToFile(bitmap, imagesDir + goal.getImageName());

        } catch (IOException e) {
            Log.e(GoalsLoader.class.getName(), "error loading image for goal " + goal, e);
        }
    }
}
