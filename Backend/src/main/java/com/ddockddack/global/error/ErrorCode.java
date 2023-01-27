package com.ddockddack.global.error;

public enum ErrorCode {

    MISSING_REQUIRED_VALUE(400, "missing input value"),
    ALREADY_EXIST_BESTCUT_LIKE(400, "already like bestcut"),
    ALREADY_EXIST_REPORT(400, "already report bestcut"),
    ALREADY_EXIST_REPORTEDGAME(400, "already report game"),
    ALREADY_EXIST_STTAREDGAME(400, "already starred game"),
    INVALID_INPUT_VALUE(400, "valid check."),
    LOGIN_REQUIRED(401, "login required"),
    NOT_AUTHORIZED(401, "not authorized"),
    MEMBER_NOT_FOUND(404, "member not found"),
    GAME_NOT_FOUND(404, "game not found"),
    STARREDGAME_NOT_FOUND(404, "starredGame not found"),
    BESTCUT_NOT_FOUND(404, "bestcut not found"),
    BESTCUT_LIKE_NOT_FOUND(404, "bestcut_like not found"),
    EXCEED_FILE_SIZE(413, "file size can't exceed 2MB"),
    EXTENSION_NOT_ALLOWED(415, "extension not allowed.")
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