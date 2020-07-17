package com.example.akash.myhighway1.reset;

public class RestResponse<T> {
    public static final String CODE_TOKEN_EXPIRED = "1024";
    public static final String CODE_APP_UPDATED = "433";

    final boolean flag;
    final T data;
    final String message;
    final String code;

    public RestResponse(boolean flag, T data, String message, String code) {
        this.flag = flag;
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public boolean isFlag() {
        return flag;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public boolean tokenExpired() {
        return code != null && code.equals(CODE_TOKEN_EXPIRED);
    }

    public boolean updateAPK() {
        return code != null && code.equals(CODE_APP_UPDATED);
    }
}
