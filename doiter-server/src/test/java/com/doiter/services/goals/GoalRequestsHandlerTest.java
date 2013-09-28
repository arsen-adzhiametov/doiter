package com.doiter.services.goals;

import com.doiter.AbstractFunctionalTest;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * User: Artur
 */
public class GoalRequestsHandlerTest extends AbstractFunctionalTest {

    @Test
    public void testGetAllGoals() throws Exception {
        String response = doGetRequest("/v1/goals/all");
        assertThat(response, is("[{\"id\":1,\"name\":\"test goal one\",\"imageCoverId\":1},{\"id\":2,\"name\":\"test goal two\",\"imageCoverId\":2}]"));
    }

    @Test
    public void testGetGoalById() throws Exception {
        String response = doGetRequest("/v1/goals/2");
        assertThat(response, is("{\"id\":2,\"name\":\"test goal two\",\"imageCoverId\":2}"));
    }

    @Test
    public void testGetAdvicesForGoal() throws Exception {
        String response = doGetRequest("/v1/goals/2/messages");
        assertThat(response, is("[{\"id\":4,\"goalId\":2,\"content\":\"some advice\"},{\"id\":5,\"goalId\":2,\"content\":\"some other advice\"},{\"id\":6,\"goalId\":2,\"content\":\"some third advice\"}]"));
    }

    @Test
    public void testGetMessagesForGoal() throws Exception {
        String response = doGetRequest("/v1/goals/2/messages/2/1");
        assertThat(response, is("[{\"id\":6,\"goalId\":2,\"content\":\"some third advice\"}]"));
    }
}
