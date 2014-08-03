package com.lutshe.doiter.notifications;

import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.database.dao.MessagesDao;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.joda.time.DateTime;
import org.joda.time.Days;

import static com.lutshe.doiter.dto.MessageDTO.Type;

/**
 * Created by Arsen Adzhiametov on 12-Jul-14 in IntelliJ IDEA.
 */
@EBean
public class MessagesUpdater {

    @Bean MessagesDao messagesDao;
    @Bean GoalsDao goalsDao;

    public int deliverMessages() {
        int messagesSent = 0;
        Goal[] userGoals = goalsDao.getActiveUserGoals();
        if (userGoals == null) return messagesSent;
        for (Goal goal : userGoals) {
            messagesSent += ensureMessagesDeliveredOrDeliver(goal);
        }
        return messagesSent;
    }

    private int ensureMessagesDeliveredOrDeliver(Goal goal) {
        int messagesSent = 0;
        DateTime goalEndTime = getGoalEndTime(goal);
        DateTime goalStartTime = getGoalStartTime(goal);
        Days goalDistance = Days.daysBetween(goalStartTime, goalEndTime);

        DateTime nextMessageTime = goalStartTime.plusDays(getDaysCountWithDeliveredMessages(goal.getId()));
        while (nextMessageTime.isBeforeNow() &&
                getDaysCountWithDeliveredMessages(goal.getId()) < goalDistance.getDays()) {
            deliverNextMessage(goal.getId());
            messagesSent++;
            nextMessageTime = nextMessageTime.plusDays(1);
        }
        if (goalEndTime.isBeforeNow()) {
            deliverLastMessage(goal.getId());
            messagesSent++;
        }
        return messagesSent;
    }

    private DateTime getGoalEndTime(Goal goal) {
        return new DateTime(goal.getEndTime()).withTimeAtStartOfDay();
    }

    private DateTime getGoalStartTime(Goal goal) {
        Message firstDeliveredMessage = messagesDao.getMessage(goal.getId(), Type.FIRST);
        return new DateTime(firstDeliveredMessage.getDeliveryTime()).withTimeAtStartOfDay();
    }

    private int getDaysCountWithDeliveredMessages(Long goalId) {
        Goal goal = goalsDao.getGoal(goalId);
        long lastMessageIndex = goal.getLastMessageIndex();
        return (int)lastMessageIndex + 1;
    }

    private void deliverNextMessage(Long goalId) {
        Goal goal = goalsDao.getGoal(goalId);
        long nextMessageIndex = goal.getLastMessageIndex() + 1;
        Message message = messagesDao.getMessage(goal.getId(), nextMessageIndex);
        if (message == null) {
            deliverLastMessage(goal.getId());
            return;
        }
        messagesDao.updateMessageDeliveryTime(message.getId());
        goalsDao.updateGoalLastMessage(goal.getId(), nextMessageIndex);
    }

    private void deliverLastMessage(Long goalId) {
        Message message = messagesDao.getMessage(goalId, Type.LAST);
        messagesDao.updateMessageDeliveryTime(message.getId());
        goalsDao.updateGoalStatus(goalId, Goal.Status.INACTIVE);
    }
}
