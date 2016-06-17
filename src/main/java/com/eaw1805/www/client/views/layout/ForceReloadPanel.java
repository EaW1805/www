package com.eaw1805.www.client.views.layout;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.GameStore;

public class ForceReloadPanel extends PopupPanel {

    private static ForceReloadPanel instance;
    AbsolutePanel hideBackground = new AbsolutePanel();

    public static ForceReloadPanel getInstance() {
        if (instance == null) {
            instance = new ForceReloadPanel();
        }
        return instance;
    }

    final VerticalPanel fails = new VerticalPanel();

    private ForceReloadPanel() {
        setAutoHideEnabled(false);
        setModal(true);
        setStyleName("tipPanel");
        //ensure this panel is on top of everything else
        getElement().getStyle().setZIndex(9999999);

        final VerticalPanel container = new VerticalPanel();

        final Label label = new Label("Oops! something went wrong while retrieving data from server.");
        label.setStyleName("clearFontMiniTitle");
        label.setWidth("343px");
        container.add(label);
        container.add(new HTML("<br>"));
        final Label label2 = new Label("The following data failed to load:");
        container.add(label2);
        container.add(fails);
        container.add(new HTML("<br>"));
        final Label tryAgain = new Label("Reload client to try again.");
        tryAgain.setStyleName("clearFontMiniTitle");
        container.add(tryAgain);
        container.add(new HTML("<br>"));
        final ImageButton ok = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        ok.setSize("36px", "36px");
        ok.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //reload the client.
                Window.Location.reload();
            }
        });
        container.add(ok);
        container.setCellHorizontalAlignment(ok, HasHorizontalAlignment.ALIGN_CENTER);
        setWidget(container);
    }

    public void onAttach() {
        super.onAttach();
        hideBackground.getElement().getStyle().setZIndex(9999998);
        hideBackground.setSize(Window.getClientWidth() +"px", Window.getClientHeight()+"px");
        hideBackground.getElement().getStyle().setBackgroundColor("black");
        hideBackground.getElement().getStyle().setOpacity(0.5d);
        GameStore.getInstance().getLayoutView().addWidgetToPanel(hideBackground, 0, 0);

    }

    public void onDetach() {
        super.onDetach();
        GameStore.getInstance().getLayoutView().removeWidgetFromPanel(hideBackground);
    }

    public ForceReloadPanel addFail(final String fail) {
        final Label lbl = new Label((fails.getWidgetCount() + 1) + ". " + fail);
        lbl.getElement().getStyle().setMarginLeft(40d, Style.Unit.PX);
        fails.add(lbl);
        return this;
    }
}
