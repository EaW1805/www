package com.eaw1805.www.fieldbattle.widgets;

import com.google.gwt.user.client.Timer;

/**
 * This class can be used if we want to make an iteration that will not cause the browser to stop responding.
 * It uses timer and iterates each step every 10 miliseconds.
 */
public abstract class DelayIterator {
    protected int ITERATE_INDEX = 0;
    protected int ITERATE_START_VALUE;
    protected int ITERATE_END_VALUE;
    protected int ITERATE_STEP;

    public DelayIterator(int start, int end, int step) {
        ITERATE_INDEX = start;
        ITERATE_START_VALUE = start;
        ITERATE_END_VALUE=end;
        ITERATE_STEP = step;
    }

    public void run() {
        ITERATE_INDEX = ITERATE_START_VALUE;
        new Timer() {

            @Override
            public void run() {
                if (ITERATE_INDEX < ITERATE_END_VALUE) {
                    executeStep();
                    schedule(10);
                    ITERATE_INDEX+=ITERATE_STEP;
                } else {
                    executeLast();
                }


            }
        }.run();
    }

    public abstract void executeStep();
    public abstract void executeLast();


}
