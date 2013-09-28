package com.doiter.services.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artur
 */
public abstract class Service {
    private static final Logger logger = LoggerFactory.getLogger(Service.class);

    private List<UriHandler> uriHandlers = new ArrayList<UriHandler>();

    public void init() {
        for (Method method : this.getClass().getDeclaredMethods()) {
            Path methodUri = method.getAnnotation(Path.class);
            if (methodUri != null) {
                logger.info("registering {} to handle '{}' requests", method.getDeclaringClass().getSimpleName() + "#" +  method.getName(), methodUri.value());
                uriHandlers.add(new UriHandler(methodUri, method));
            }
        }
    }

    protected Object defaultMethod(String uri, Object body) {
        logger.error("unable to handle the request {} [{}]", uri, body);
        throw new UnsupportedOperationException();
    }

    public Object processRequest(String uri, Object body) {
        UriHandler handler = findHandler(uri);
        if (handler != null) {
            try {
                logger.debug("calling {} for {}", handler.targetMethod.getName(), uri);
                return handler.targetMethod.invoke(this, handler.extractArgs(uri, body));
            } catch (Exception e) {
                logger.error("exception handling request", e);
                // TODO rethrow up
            }
        }
        logger.debug("using defaultMethod for {}", uri);
        return defaultMethod(uri, body);
    }

    private UriHandler findHandler(String uri) {
        for (UriHandler handler : uriHandlers) {
            if (handler.matches(uri)) {
                return handler;
            }
        }
        return null;
    }

    class UriHandler {
        PathElement[] elements;
        int varsNum;
        Method targetMethod;

        Accept acceptedBody;

        UriHandler(Path path, Method method) {
            String[] els = StringUtils.split(path.value(), "/");
            elements = new PathElement[els.length];
            targetMethod = method;

            acceptedBody = targetMethod.getAnnotation(Accept.class);

            Class<?>[] types = method.getParameterTypes();
            varsNum = types.length;
            int currentParameterNumber = 0;
            int currentPathElement = 0;

            for (String el : els) {
                if (el.startsWith("{")) {
                    elements[currentPathElement++] = new PathElement(types[currentParameterNumber++]);
                }
                else {
                    elements[currentPathElement++] = new PathElement(el);
                }
            }
        }

        boolean matches(String path) {
            String[] els = StringUtils.split(path, "/");
            if (els.length != elements.length) {
                return false;
            }
            for (int i = 0; i < elements.length; i++) {
                if (!elements[i].isVariable && !elements[i].path.equalsIgnoreCase(els[i])) {
                    return false;
                }
            }
            return true;
        }

        private Object[] extractArgs(String path, Object body) {
            String[] pathElements = StringUtils.split(path, "/");
            Object[] args = new Object[acceptedBody == null ? varsNum : varsNum + 1];
            int currArg = 0;

            for (int i = 0; i < elements.length; i++) {
                if (elements[i].isVariable) {
                    args[currArg++] = cast(elements[i].type, pathElements[i]);
                }
            }

            if (acceptedBody != null) {
                args[currArg++] = cast(acceptedBody.value(), body);
            }

            return args;
        }

        private Object cast(Class type, Object value) {
            if (type == String.class) {
                return value.toString();
            } else if (type == Long.class) {
                return Long.valueOf(value.toString());
            } else if (type == byte[].class) {
                return value;
            }
            return value;
        }
    }

    class PathElement {
        private boolean isVariable;
        private Class type;
        private String path;

        PathElement(Class variableType) {
            isVariable = true;
            this.type = variableType;
        }
        PathElement(String path) {
            this.path = path;
        }
    }
}
