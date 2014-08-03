package com.lutshe.doiter.preloaders;

import android.app.AlarmManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.rest.clients.GoalsRestClient;
import com.lutshe.doiter.data.rest.clients.ImageRestClient;
import com.lutshe.doiter.data.rest.clients.MessagesRestClient;
import com.lutshe.doiter.dto.GoalDTO;
import com.lutshe.doiter.dto.MessageDTO;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.Trace;
import org.apache.http.HttpStatus;
import retrofit.client.Response;

import java.io.IOException;
import java.io.InputStream;

import static com.lutshe.doiter.data.converter.DtoToModelConverter.convertToGoalModel;
import static com.lutshe.doiter.data.converter.DtoToModelConverter.convertToMessageModel;
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
    @RootContext Context context;
    @Bean GoalsRestClient goalsRestClient;
    @Bean MessagesRestClient messagesRestClient;
    @Bean ImageRestClient imageRestClient;

    @Override
    public long getLoadingInterval() {
        return LOADING_INTERVAL;
    }

    @Trace
    @Override
    public void load() {
        GoalDTO[] allGoals = goalsRestClient.getAllGoals();
        for (GoalDTO goalDTO : allGoals) {
            storeGoal(convertToGoalModel(goalDTO));
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
        MessageDTO[] messageDTOs = messagesRestClient.getAllMessagesForGoal(goal.getId());
        Log.d(GoalsLoader.class.getName(), "number of messages: " + messageDTOs.length);
        for (MessageDTO messageDTO : messageDTOs) {
            Message message = convertToMessageModel(messageDTO);
            message.setId(null);
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
            InputStream is = res.getBody().in();
            Bitmap bitmap = getBitmap(is);
            writeBitmapToFile(bitmap, goal.getImageName(), context);

        } catch (IOException e) {
            Log.e(GoalsLoader.class.getName(), "error loading image for goal " + goal, e);
        }
    }
}
