package com.doiter.services.ping;

import com.doiter.AbstractFunctionalTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * User: Artur
 */
public class PingsHandlerTest extends AbstractFunctionalTest {

    @Test
    public void shouldAnswerPong() {
        String result = doGetRequest("/v1/ping");
        assertEquals("\"pong!\"", result);
    }
}
