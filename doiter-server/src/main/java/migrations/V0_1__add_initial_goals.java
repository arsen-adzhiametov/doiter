package migrations;

import com.googlecode.flyway.core.api.migration.jdbc.JdbcMigration;
import com.googlecode.flyway.core.util.ClassPathResource;
import com.lutshe.doiter.config.DataSourceConfig;
import com.lutshe.doiter.config.PropertiesConfig;
import com.lutshe.doiter.dao.GoalsDao;
import com.lutshe.doiter.dao.MessagesDao;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Connection;
import java.util.List;

import static com.lutshe.doiter.dto.MessageDTO.Type;

/**
 * @Author: Art
 */
public class V0_1__add_initial_goals implements JdbcMigration {
    private static final Logger logger = LoggerFactory.getLogger(V0_1__add_initial_goals.class);

    private AnnotationConfigApplicationContext context;

    @Override
    public void migrate(Connection connection) throws Exception {
        // try to move into constructor
        context = createSpringContext();

        GoalsDao goalsDao = context.getBean(GoalsDao.class);

        List<Goal> allGoals = goalsDao.getAllGoals();
        logger.info("We have {} goals", allGoals.size());
        for (Goal goal : allGoals) {
            createMessagesForGoal(goal);
        }

        context.stop();
    }

    private void createMessagesForGoal(Goal goal) {
        MessagesDao messagesDao = context.getBean(MessagesDao.class);
        String goalName = goal.getName();
        logger.info("migrating '{}'", goalName);

        ClassPathResource resource = new ClassPathResource("goals/" + goalName);

        if (!resource.exists()) {
            logger.warn("messages for '{}' not found!", goalName);
            return;
        }

        String fileContent = resource.loadAsString("UTF-8");
        String[] messages = fileContent.split("\n");
        long orderIndex = 0;

        for (String text : messages) {
            text = text.trim();
            if (text.equals("")){
                logger.warn("empty line!");
                continue;
            }

            Message message = createMessage(goal, messages, orderIndex, text);
            logger.debug("adding message: {} as {} with order {}", text, message.getType(), message.getOrderIndex());

            messagesDao.saveMessage(message);
            orderIndex ++;
        }
    }

    private Message createMessage(Goal goal, String[] messages, long orderIndex, String text) {
        Message message = new Message(text, goal.getId(), orderIndex);

        if (orderIndex == 0) {
            message.setType(Type.FIRST);
        } else if (orderIndex == messages.length - 1) {
            message.setType(Type.LAST);
            message.setOrderIndex(null);
        }
        return message;
    }

    private AnnotationConfigApplicationContext createSpringContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(DataSourceConfig.class);
        context.register(PropertiesConfig.class);
        context.scan("com.lutshe.doiter");
        context.refresh();
        return context;
    }
}
