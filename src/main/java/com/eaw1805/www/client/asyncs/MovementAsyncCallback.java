package com.eaw1805.www.client.asyncs;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eaw1805.data.dto.web.movement.PathDTO;
import com.eaw1805.www.client.movement.FiguresGroup;
import com.eaw1805.www.client.widgets.ErrorPopup;

import java.util.Date;
import java.util.Set;

public class MovementAsyncCallback
        implements AsyncCallback<Set<PathDTO>> {

    private final FiguresGroup figures;

    public MovementAsyncCallback(final FiguresGroup figuresGroup) {
        this.figures = figuresGroup;
    }

    public void onFailure(final Throwable caught) {
        new ErrorPopup(ErrorPopup.Level.WARNING, "Could not retrieve movement paths! it appears that you are offline. Click here for the login screen to open, login again, and then try to move your unit again.", false) {
            public void onAccept() {
                Window.open("/login", "_blank", "");
            }
        };
    }

    public void onSuccess(final Set<PathDTO> result) {
        figures.addToAvailableMovementRectanglesGroup(result);
    }

}
