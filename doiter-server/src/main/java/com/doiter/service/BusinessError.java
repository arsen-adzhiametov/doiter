package com.doiter.service;


public class BusinessError extends Exception {

    public static final BusinessError ERROR_UPGRADE = new BusinessError(Error.ERROR_UPGRADE);
    
    private static final long serialVersionUID = 5675457428984448131L;


    public enum Error {

        ERROR_UPGRADE(Message.UPGRADE_MESSAGE);
        
        private final Message message;

        private Error(Message message) {
            this.message = message;
        }

        public Message getMessage() {
            return message;
        }
    }
    
    private String errorCode;
    private Error error;
    private Object[] args;


    public BusinessError(Error error) {
        this.error = error;
        this.errorCode = error.name();
    }

    public BusinessError(Error error, Object... args) {
        this(error);
        this.args = args;
    }
    
    public BusinessError(String errorCode) {
        this(errorCode, errorCode);
    }

    public BusinessError(String errorCode, Object... args) {
        this.errorCode = errorCode;
        this.args = args;
    }
    
    public Error getError() {
        return error;
    }
    
    public String getErrorCode() {
        return errorCode;
    }

    public Object[] getArgs() {
        return args;
    }
    
}
