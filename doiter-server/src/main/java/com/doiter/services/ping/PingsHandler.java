package com.doiter.services.ping;

import com.doiter.services.common.Rest;
import com.doiter.services.common.Service;

/**
 * @author Artur.
 */
@Rest("/ping")
public class PingsHandler extends Service {

    @Override
    protected String defaultMethod(String uri, Object body) {
        return "pong!";
    }
}
