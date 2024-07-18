package com.dev.byc.model;

public class SuccessResponse {

    private Boolean result;
    private String message;

    public SuccessResponse(boolean b, String message2) {
        this.result = b;
        this.message = message2;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
