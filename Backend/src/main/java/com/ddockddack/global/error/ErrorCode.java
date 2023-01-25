package com.ddockddack.global.error;

public enum ErrorCode {

    MISSING_REQUIRED_VALUE(400, "missing input value"),
    ALREADY_EXIST_BESTCUT_LIKE(400, "already like bestcut"),
    ALREADY_EXIST_REPORT(400, "already report bestcut"),
    LOGIN_REQUIRED(401, "login required"),
    NOT_AUTHORIZED(401, "not authorized"),
    BESTCUT_NOT_FOUND(404, "bestcut not found"),
    EXCEED_FILE_SIZE(413, "file size can't exceed 2MB"),
    EXCEED_FILE_NUMBER(414, "number of files can't exceed 10")
    ;

    private int code;
    private String message;

    ErrorCode(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }
}