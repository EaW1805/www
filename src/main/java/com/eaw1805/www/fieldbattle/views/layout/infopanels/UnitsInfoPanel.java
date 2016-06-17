package com.eaw1805.www.fieldbattle.views.layout.infopanels;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcService;
import com.eaw1805.www.fieldbattle.remote.EmpireFieldBattleRpcServiceAsync;
import com.eaw1805.www.fieldbattle.widgets.ImageButton;

public class UnitsInfoPanel extends AbsolutePanel {

    private final AbsolutePanel unitsBasePanel;
    private final static EmpireFieldBattleRpcServiceAsync service = GWT.create(EmpireFieldBattleRpcService.class);

    public UnitsInfoPanel() {
        setStyleName("unitsPanel");
        setSize("412px", "165px");

        unitsBasePanel = new AbsolutePanel();
        add(unitsBasePanel, 20, 67);

        final ImageButton orders = new ImageButton("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButReviewOrdersOff.png", "");
        orders.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

            }
        });
        orders.setSize("30px", "30px");
        add(orders, 330, 30);

        final ImageButton saveLbl = new ImageButton("http://static.eaw1805.com/images/layout/buttons/unitMenu/ButSaveOrdersOff.png", "");
        saveLbl.setSize("30px", "30px");
        add(saveLbl, 363, 30);
//        saveLbl.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent clickEvent) {
//                service.saveBrigadesPositions(BaseStore.getInstance().getScenarioId(), BaseStore.getInstance().getBattleId(), ArmyStore.getInstance().getBrigadesByNation(BaseStore.getInstance().getNationId()), new AsyncCallback<Integer>() {
//                    @Override
//                    public void onFailure(Throwable throwable) {
//                        Window.alert("Failed to save brigades positions");
//                    }
//
//                    @Override
//                    public void onSuccess(final Integer integer) {
//                        Window.alert("Positions saved");
//                    }
//                });
//            }
//        });


    }

    public void updateBrigadeInfo(final BrigadeDTO brigade) {
        unitsBasePanel.clear();
        unitsBasePanel.add(new BrigadeInfoPanel(brigade, false, false, false));
    }
}
