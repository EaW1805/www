package com.eaw1805.www.controllers.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Extended handler for AsyncCallback that handles messages using the WrapperMessage class
 * @param <H>
 */
public abstract class AsyncCallbackWrapper<H> implements AsyncCallback<WrapperMessage<H>> {


    @Override
    public void onFailure(Throwable throwable) {
        onFailure(StatusMesssage.FAILED, throwable.getMessage());
    }

    @Override
    public void onSuccess(WrapperMessage<H> msg) {
        if (msg.getStatus() != StatusMesssage.OK) {
            onFailure(msg.getStatus(), msg.getMessage());
        } else {
            onSuccess(msg.getResult(), msg.getStatus(), msg.getMessage());
        }
    }

    /**
     * Executes when result is not as expected.
     *
     * @param status The status of the response.
     * @param msg A message describing what went wrong.
     */
    public abstract void onFailure(int status, String msg);

    /**
     * Executes when everything went ok.
     *
     * @param result The expected result.
     * @param status  A status, StatusMessages.OK.
     * @param msg A message providing extra information.
     */
    public abstract void onSuccess(H result, int status, String msg);
}
