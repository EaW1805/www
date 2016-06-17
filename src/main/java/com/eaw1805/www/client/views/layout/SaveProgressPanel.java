package com.eaw1805.www.client.views.layout;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.economy.OrderStore;

public class SaveProgressPanel extends PopupPanel implements OrderConstants {

    private static SaveProgressPanel instance;
    AbsolutePanel hideBackground = new AbsolutePanel();

    public static SaveProgressPanel getInstance() {
        if (instance == null) {
            instance = new SaveProgressPanel();
        }
        return instance;
    }

    int[] process = new int[13];

    Grid progressTable = new Grid(14, 3);
    final Label title = new Label("Saving...");
    private SaveProgressPanel() {
        setAutoHideEnabled(false);
        setModal(true);
        setStyleName("tipPanel");
        //ensure this panel is on top of everything else
        getElement().getStyle().setZIndex(9999999);

        final ImageButton closeImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        closeImg.setTitle("Close panel");
        closeImg.setSize("20px", "20px");
        closeImg.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                SaveProgressPanel.this.hide();
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 10
                        && TutorialStore.getInstance().getTutorialStep() == 18) {
                    TutorialStore.getInstance().setSaveOrdersDone(true);

                }
                if (TutorialStore.getInstance().isTutorialMode()) {
                    Window.Location.replace("/processing/scenario/" + GameStore.getInstance().getScenarioStr() + "/game/" + GameStore.getInstance().getGameId() + "/turn/" + GameStore.getInstance().getTurn());
                }
            }
        });

        HorizontalPanel titlePanel = new HorizontalPanel();
        VerticalPanel panel = new VerticalPanel();

        titlePanel.add(title);
        titlePanel.setCellWidth(title, "315px");
        title.setStyleName("clearFontMiniTitle");
        titlePanel.add(closeImg);
        panel.add(titlePanel);
        panel.add(progressTable);

        progressTable.setWidget(0, 0, new Label(""));
        progressTable.setWidget(0, 1, new Label(""));
        progressTable.setWidget(0, 2, new Label("Orders saved successfully/Total orders"));

        progressTable.setWidget(1, 0, new Label("Unit Changes"));
        progressTable.setWidget(1, 1, getLoadingImg());
        progressTable.setWidget(1, 2, createOrdersLabel());

        progressTable.setWidget(2, 0, new Label("Commanders"));
        progressTable.setWidget(2, 1, getLoadingImg());
        progressTable.setWidget(2, 2, createOrdersLabel());

        progressTable.setWidget(3, 0, new Label("Movements"));
        progressTable.setWidget(3, 1, getLoadingImg());
        progressTable.setWidget(3, 2, createOrdersLabel());

        progressTable.setWidget(4, 0, new Label("Transport"));
        progressTable.setWidget(4, 1, getLoadingImg());
        progressTable.setWidget(4, 2, createOrdersLabel());

        progressTable.setWidget(5, 0, new Label("Navy"));
        progressTable.setWidget(5, 1, getLoadingImg());
        progressTable.setWidget(5, 2, createOrdersLabel());

        progressTable.setWidget(6, 0, new Label("Economy"));
        progressTable.setWidget(6, 1, getLoadingImg());
        progressTable.setWidget(6, 2, createOrdersLabel());

        progressTable.setWidget(7, 0, new Label("Relations"));
        progressTable.setWidget(7, 1, getLoadingImg());
        progressTable.setWidget(7, 2, createOrdersLabel());

        progressTable.setWidget(8, 0, new Label("Region"));
        progressTable.setWidget(8, 1, getLoadingImg());
        progressTable.setWidget(8, 2, createOrdersLabel());

        progressTable.setWidget(9, 0, new Label("Taxation"));
        progressTable.setWidget(9, 1, getLoadingImg());
        progressTable.setWidget(9, 2, createOrdersLabel());

        progressTable.setWidget(10, 0, new Label("Trade"));
        progressTable.setWidget(10, 1, getLoadingImg());
        progressTable.setWidget(10, 2, createOrdersLabel());

        progressTable.setWidget(11, 0, new Label("Baggage trains"));
        progressTable.setWidget(11, 1, getLoadingImg());
        progressTable.setWidget(11, 2, createOrdersLabel());

        progressTable.setWidget(12, 0, new Label("Barracks"));
        progressTable.setWidget(12, 1, getLoadingImg());
        progressTable.setWidget(12, 2, createOrdersLabel());

        progressTable.setWidget(13, 0, new Label("Game settings"));
        progressTable.setWidget(13, 1, getLoadingImg());
        progressTable.setWidget(13, 2, createOrdersLabel());


        setWidget(panel);

    }

    public Label createOrdersLabel() {
        final Label out = new Label("");
        out.setWidth("200px");
        out.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        return out;
    }


    public int[] getOrdersByState(final int state) {
        switch (state) {
            case 0:
                return new int[] {ORDER_ADD_BATT, ORDER_B_ARMY, ORDER_B_CORP, ORDER_B_BATT, ORDER_D_ARMY, ORDER_D_CORP,
                        ORDER_D_BRIG, ORDER_REN_ARMY, ORDER_REN_CORP, ORDER_REN_BRIG, ORDER_ADDTO_ARMY,
                        ORDER_ADDTO_CORP, ORDER_ADDTO_BRIGADE, ORDER_INC_EXP, ORDER_INC_HEADCNT, ORDER_INC_EXP_ARMY,
                        ORDER_INC_EXP_CORPS, ORDER_INC_HEADCNT_ARMY, ORDER_INC_HEADCNT_CORPS, ORDER_MRG_BATT};
            case 1:
                return new int[] {ORDER_ARMY_COM, ORDER_CORP_COM, ORDER_LEAVE_COM, ORDER_REN_COMM, ORDER_HIRE_COM,
                        ORDER_DISS_COM};
            case 2:
                return new int[] {ORDER_M_UNIT};
            case 3:
                return new int[] {ORDER_LOAD_TROOPSF, ORDER_LOAD_TROOPSS, ORDER_UNLOAD_TROOPSF, ORDER_UNLOAD_TROOPSS};
            case 4:
                return new int[] {ORDER_B_FLT, ORDER_B_SHIP, ORDER_R_FLT, ORDER_D_FLT, ORDER_REN_FLT, ORDER_REN_SHIP,
                        ORDER_R_SHP, ORDER_ADDTO_FLT, ORDER_HOVER_SHIP, ORDER_SCUTTLE_SHIP};
            case 5:
                return new int[] {ORDER_B_PRODS, ORDER_D_PRODS};
            case 6:
                return new int[] {ORDER_POLITICS};
            case 7:
                return new int[] {ORDER_INC_POP, ORDER_DEC_POP, ORDER_HOVER_SEC};
            case 8:
                return new int[] {ORDER_TAXATION};
            case 9:
                return new int[] {ORDER_EXCHF, ORDER_EXCHS};
            case 10:
                return new int[] {ORDER_B_BTRAIN, ORDER_R_BTRAIN, ORDER_REN_BTRAIN, ORDER_SCUTTLE_BTRAIN};
            case 11:
                return new int[] {ORDER_REN_BARRACK};
            case 12:
            default:
                return new int[] {};

        }

    }

    public int getOrdersCount(final int state) {
        if (state == 12) {
            return 1;
        }
        return OrderStore.getInstance().getOrdersByTypes(getOrdersByState(state)).size();
    }

    public void checkDone() {
        boolean done = true;
        boolean error = false;
        boolean hardError = false;
        for (int index = 0; index <= 12; index++) {
            if (process[index] == 0) {
                done = false;
            }
            if (process[index] == -1) {
                hardError = true;
            }
            String txt = ((Label)progressTable.getWidget(index + 1, 2)).getText();
            if (!txt.isEmpty()) {
                final String first = txt.split("/")[0];
                final String second = txt.split("/")[1];
                if (!first.trim().equals(second.trim())) {
                    error = true;
                }
            }
        }
        if (done) {
            if (hardError) {
                title.setText("Errors found, please try to save again");
            } else if (error) {
                title.setText("Save completed");
            } else {
                title.setText("Save completed");
            }
        }
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

    public void reset() {
        title.setText("Saving...");
        //fix variables for new save
        for (int index = 0; index <= 12; index++) {
            process[index] = 0;
            progressTable.setWidget(index + 1, 1, getLoadingImg());
            ((Label)progressTable.getWidget(index + 1, 2)).setText("?/" + getOrdersCount(index));
        }
    }
    public void setOk(int state, final int count) {
        process[state] = 1;
        progressTable.setWidget(state + 1, 1, getOkImg());
        ((Label)progressTable.getWidget(state + 1, 2)).setText(count + "/" + getOrdersCount(state));
        checkDone();
    }

    public void setNoOk(int state) {
        process[state] = -1;
        progressTable.setWidget(state + 1, 1, getNotOkImg());
        ((Label)progressTable.getWidget(state + 1, 2)).setText("?/" + getOrdersCount(state));
        checkDone();
    }

    public Image getLoadingImg() {
        final Image img = new Image("http://static.eaw1805.com/images/loading/loading2.gif");
        img.setSize("14px", "14px");
        return img;
    }

    public Image getOkImg() {
        final Image img = new Image("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        img.setSize("14px", "14px");
        return img;
    }

    public Image getNotOkImg() {
        final Image img = new Image("http://static.eaw1805.com/images/buttons/ButCancelOff.png");
        img.setSize("14px", "14px");
        return img;
    }

}
