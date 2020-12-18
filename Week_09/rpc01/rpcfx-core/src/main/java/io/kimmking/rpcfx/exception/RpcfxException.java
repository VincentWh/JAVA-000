package io.kimmking.rpcfx.exception;

public class RpcfxException extends RuntimeException {
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private int code;
    private String message;

    public RpcfxException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
