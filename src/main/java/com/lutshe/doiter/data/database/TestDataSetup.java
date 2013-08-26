package com.lutshe.doiter.data.database;

import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.Trace;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.data.model.Goal;
import com.lutshe.doiter.data.model.Message;

import static com.lutshe.doiter.data.model.Message.Type.FIRST;
import static com.lutshe.doiter.data.model.Message.Type.LAST;

/**
 * Created by Arturro on 21.08.13.
 */
@EBean
public class TestDataSetup {

    private static Goal[] initialGoals = new Goal[]{
        new Goal("Learn to ride a donkey", 0L),
                new Goal("Rate this app", 1L),
                new Goal("Go on a date", 2L),
                new Goal("Buy a car", 3L),
                new Goal("Gain a super power", 4L),
                new Goal("Kick Chuck Norris ass", 5L),
                new Goal("Don't get AIDS", 6L),
                new Goal("Learn to play guitar", 7L),
                new Goal("Learn new language", 8L),
                new Goal("Run 10 kilometers", 9L)};

    private static Message [] testMessages = {
            new Message((Long) null, "Life is not a bed of roses.", 0L, 0L),
            new Message((Long) null, "Life is not all cakes and ale (beer and skittles).", 0L, 1L),
            new Message((Long) null, "Like a cat on hot bricks.", 1L, 0L),
            new Message((Long) null, "Like a needle in a haystack.", 1L, 1L),
            new Message((Long) null, "Like begets like.", 2L, 0L),
            new Message((Long) null, "Like cures like.", 2L, 1L),
            new Message((Long) null, "Like father, like son.", 3L, 0L),
            new Message((Long) null, "Little things amuse little minds.", 3L, 1L),
            new Message((Long) null, "Little thieves are hanged, but great ones escape.", 4L, 0L),
            new Message((Long) null, "A new broom sweeps clean.", 4L, 1L),
            new Message((Long) null, "A quiet conscience sleeps in thunder.", 5L, 0L),
            new Message((Long) null, "A silent fool is counted wise.", 5L, 1L),
            new Message((Long) null, "A stitch in time saves nine.", 6L, 0L),
            new Message((Long) null, "A storm in a teacup.", 6L, 1L),
            new Message((Long) null, "A threatened blow is seldom given.", 7L, 0L),
            new Message((Long) null, "As drunk as a lord.", 7L, 1L),
            new Message((Long) null, "As like as two peas.", 8L, 0L),
            new Message((Long) null, "Keep your mouth shut and your eyes open.", 8L, 0L),
            new Message((Long) null, "Lies have short legs.", 9L, 0L),
            new Message((Long) null, "Marriages are made in heaven.", 9L, 1L),
            new Message((Long) null, "Misfortunes never come alone (singly).", 0L, 3L),
            new Message((Long) null, "Life is not a bed of roses.", 0L, 4L)

    };

    @Bean
    GoalsDao goalsDao;
    @Bean
    MessagesDao messagesDao;

    @Trace
    public void setup() {
        for (Goal goal : initialGoals) {
            setupGoal(goal);
        }

        setupMessages();
    }

    @Background
    void setupMessages() {
        for (Message message : testMessages) {
            setupMessage(message);
        }
    }

    private void setupMessage(Message message) {
        messagesDao.addMessage(message);
    }

    private void setupGoal(Goal goal) {
        goalsDao.addGoal(goal);

        Message firstMessage = new Message((Long) null, "first msg for goal " + goal.getId(), goal.getId(), 0L);
        firstMessage.setType(FIRST);
        messagesDao.addMessage(firstMessage);

        Message lastMessage = new Message((Long) null, "last msg for goal " + goal.getId(), goal.getId(), null);
        lastMessage.setType(LAST);
        messagesDao.addMessage(lastMessage);
    }
}
