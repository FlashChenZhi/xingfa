package com.rft;

/**
 * Created with IntelliJ IDEA.
 * User: Zhouyue
 * Date: 13-4-10
 * Time: 下午3:38
 * To change this template use File | Settings | File Templates.
 */
public class ReturnObj<T> {
    private boolean isOk;
    private T data;
    private String errorMessage;

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
