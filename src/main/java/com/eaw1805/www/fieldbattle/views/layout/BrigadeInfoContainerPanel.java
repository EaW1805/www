package com.eaw1805.www.fieldbattle.views.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.fieldbattle.LoadUtil;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcService;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcServiceAsync;
import com.eaw1805.www.fieldbattle.stores.ArmyStore;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.CommanderStore;
import com.eaw1805.www.fieldbattle.stores.utils.BasicHandler;
import com.eaw1805.www.fieldbattle.tooltips.Tips;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.BrigadeInfoPanel;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;
import com.eaw1805.www.fieldbattle.widgets.StyledCheckBox;


public class BrigadeInfoContainerPanel extends AbsolutePanel {

    private final AbsolutePanel brigadeInfo;
    public final Label debugText = new Label();
    boolean opened = true;
    final Timer openT;
    final Timer closeT;
    private final static EmpireFieldBattleRpcServiceAsync service = GWT.create(EmpireFieldBattleRpcService.class);

    public BrigadeInfoContainerPanel() {
        setSize("401px", "155px");
        setStyleName("brigadeInfoContainer");
        debugText.setStyleName("whiteText");
        add(debugText, 0, 15);
        debugText.setText("");
        brigadeInfo = new AbsolutePanel();
        brigadeInfo.setSize("366px", "90px");
        add(brigadeInfo, 10, 55);

        closeT = new Timer() {
            @Override
            public void run() {
                if (getAbsoluteTop() < Window.getClientHeight() - 40) {
                    MainPanel.getInstance().setWidgetPosition(BrigadeInfoContainerPanel.this, 0, getAbsoluteTop() + 5);
                } else {
                    MainPanel.getInstance().setWidgetPosition(BrigadeInfoContainerPanel.this, 0, Window.getClientHeight() - 40);
                    cancel();
                }

            }
        };

        openT = new Timer() {
            @Override
            public void run() {
                if (getAbsoluteTop() > Window.getClientHeight() - 155) {
                    MainPanel.getInstance().setWidgetPosition(BrigadeInfoContainerPanel.this, 0, getAbsoluteTop() - 5);
                } else {
                    MainPanel.getInstance().setWidgetPosition(BrigadeInfoContainerPanel.this, 0, Window.getClientHeight() - 155);
                    cancel();
                }
            }
        };

        if (!BaseStore.getInstance().isStartRound()) {
            final StyledCheckBox readyBox = new StyledCheckBox("Ready?", BaseStore.getInstance().isSideReady(), false, "Click this when you are done with all orders so the game can process");
            readyBox.addOnChangeHandler(new BasicHandler() {
                @Override
                public void run() {
                    BaseStore.getInstance().setSideReady(readyBox.isChecked());
                }
            });
            add(readyBox, 240, 21);


            final ImageButton saveLbl = new ImageButton("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButSaveOrdersOff.png", Tips.ELEMENT_SAVE);
            saveLbl.setSize("30px", "30px");
            add(saveLbl, 300, 21);
            saveLbl.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    service.saveBrigadesPositions(BaseStore.getInstance().getScenarioId(), BaseStore.getInstance().getBattleId(), BaseStore.getInstance().getNationId(), ArmyStore.getInstance().getBrigadesByNation(BaseStore.getInstance().getNationId()), BaseStore.getInstance().isSideReady(), new AsyncCallback<Integer>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            Window.alert("Failed to save brigades positions");
                        }

                        @Override
                        public void onSuccess(final Integer integer) {
                            if (integer == -1) {
                                Window.alert("Your position is ready, you can't save again!");
                                return;
                            }
                            Window.alert("Positions saved");
                            if (BaseStore.getInstance().isSideReady()) {
                                LoadUtil.getInstance().loadSocialPanel(true);
                            }
                        }
                    });
                }
            });
        }

    }


    public void open() {
        closeT.cancel();
        openT.scheduleRepeating(10);
    }

    public void close() {
        openT.cancel();
        closeT.scheduleRepeating(10);
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public void updateBrigadeInfoPanel(final BrigadeDTO brigade) {
        brigadeInfo.clear();
        brigadeInfo.add(new BrigadeInfoPanel(brigade, false, false, false), 0, 0);
    }


}
