package com.eaw1805.www.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.www.client.events.GameEventManager;
import com.eaw1805.www.client.remote.EmpireRpcService;
import com.eaw1805.www.client.remote.EmpireRpcServiceAsync;
import com.eaw1805.www.shared.stores.GameStore;

import java.util.Date;

public class CountdownLabel
        extends Label {

    private long days, hours, minutes, seconds;

    private final EmpireRpcServiceAsync eService = GWT.create(EmpireRpcService.class);

    private boolean isProcessed;

    public CountdownLabel(final String targetDate) {
        super();
        isProcessed = false;
        setTargetDate(targetDate);
        this.setStyleName("clearFontSmall redText");
    }

    /**
     * @param targetDate the targetDate to set
     */
    public final void setTargetDate(final String targetDate) {
        final Date nowDate = new Date();

        // make sure we take into account the timezone
        final TimeZone timezone = TimeZone.createTimeZone(-60);
        final int offset = timezone.getOffset(nowDate);

        final DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
        final long tm = dtf.parse(targetDate.split("\\.")[0]).getTime();
        long diff = tm - nowDate.getTime() + offset;

        // add 24 hours
        diff += 24 * 60 * 60 * 1000;

        if (diff < 0) {
            setText("Ready");
        }

        final long finalDiff = diff;

        seconds = (diff / (1000)) % 60;
        minutes = (diff / (1000 * 60)) % 60;
        hours = (diff / (60 * 60 * 1000)) % 24;
        days = ((diff / (60 * 60 * 1000)) - hours) / 24;

        Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
            public boolean execute() {
                if (finalDiff < 0) {
                    eService.getGameStatus(GameStore.getInstance().getScenarioId(), GameStore.getInstance().getGameId(), new AsyncCallback<Boolean>() {
                        public void onFailure(final Throwable caught) {
                            // do nothing
                        }

                        public void onSuccess(final Boolean beingProcessedNow) {
                            if (beingProcessedNow) {
                                setText("Processed");
                                isProcessed = true;
                                GameEventManager.reportInProcess(true);

                            } else {
                                setText("Ready");
                            }
                        }
                    });

                    return !isProcessed;
                }

                seconds--;
                if (seconds < 0) {
                    if (minutes == 0 && hours == 0 && days == 0) {
                        eService.getGameStatus(GameStore.getInstance().getScenarioId(), GameStore.getInstance().getGameId(), new AsyncCallback<Boolean>() {
                            public void onFailure(final Throwable caught) {
                                // do nothing
                            }

                            public void onSuccess(final Boolean beingProcessedNow) {
                                if (beingProcessedNow) {
                                    setText("Processed");
                                    isProcessed = true;
                                    GameEventManager.reportInProcess(true);

                                } else {
                                    setText("Ready");
                                }
                            }
                        });

                        return !isProcessed;
                    }

                    seconds = 59;
                    minutes--;
                    if (minutes < 0) {
                        minutes = 59;
                        if (hours < 0) {
                            hours = 23;
                            if (days < 0) {
                                days--;

                                eService.getGameStatus(GameStore.getInstance().getScenarioId(), GameStore.getInstance().getGameId(), new AsyncCallback<Boolean>() {
                                    public void onFailure(final Throwable caught) {
                                        // do nothing
                                    }

                                    public void onSuccess(final Boolean beingProcessedNow) {
                                        if (beingProcessedNow) {
                                            setText("Processed");
                                            isProcessed = true;
                                            GameEventManager.reportInProcess(true);

                                        } else {
                                            setText("Ready");
                                        }
                                    }
                                });

                                return !isProcessed;
                            }
                        }
                    }
                }

                setText(days + "d " + format(hours) + ":" + format(minutes) + ":" + format(seconds));
                return true;
            }

            private String format(final long target) {
                if (target < 10) {
                    return "0" + target;

                } else {
                    return String.valueOf(target);
                }
            }
        }, 1000);
    }

}
