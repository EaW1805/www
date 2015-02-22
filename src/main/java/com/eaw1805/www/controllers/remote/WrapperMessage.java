package com.eaw1805.www.controllers.remote;

/**
 * Extended result message for gwt async callbacks.
 * With this message we can provide several more information
 * to the client except from the basic expected result.
 * @param <H>
 */
public class WrapperMessage<H> implements  com.google.gwt.user.client.rpc.IsSerializable {
    private H result;
    private int status;
    private String message;

    public WrapperMessage<H> setResult(H result) {
        this.result = result;
        return this;
    }

    public WrapperMessage<H> setStatus(int status) {
        this.status = status;
        return this;
    }



    public H getResult() {
        return result;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public WrapperMessage<H> setMessage(String message) {
        this.message = message;
        return this;
    }
}
