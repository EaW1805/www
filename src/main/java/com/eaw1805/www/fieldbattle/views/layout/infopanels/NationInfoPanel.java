package com.eaw1805.www.fieldbattle.views.layout.infopanels;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.www.fieldbattle.LoadUtil;
import com.eaw1805.www.fieldbattle.events.loading.ArmiesLoadedEvent;
import com.eaw1805.www.fieldbattle.events.loading.ArmiesLoadedHandler;
import com.eaw1805.www.fieldbattle.events.loading.LoadingEventManager;
import com.eaw1805.www.fieldbattle.stores.ArmyStore;
import com.eaw1805.www.fieldbattle.stores.BaseStore;
import com.eaw1805.www.fieldbattle.stores.PlaybackStore;

public class NationInfoPanel extends AbsolutePanel {
    private final Label headCountTotal;
    private final Label moraleTotal;
    private final int nationId;
    final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getFormat("#,##0.0");

    public NationInfoPanel(int nationId) {

        this.nationId = nationId;
        setSize("160px", "40px");
        setStyleName("nationTitleFieldBattlePanel");
        final Image flag = new Image("http://static.eaw1805.com/images/nations/nation-" + nationId + "-36.png");
        final NationDTO nation = BaseStore.getInstance().getNationById(nationId);
        flag.setTitle(nation.getName());
        flag.setSize("", "34px");
        add(flag, 3, 3);

        final Label morale = new Label("Morale : ");
        morale.setStyleName("clearFontSmall");
        add(morale, 57, 20);

        headCountTotal = new Label("");
        headCountTotal.setTitle("Head count on field  - Head count of losses");
        headCountTotal.setStyleName("clearFontSmall");
        add(headCountTotal, 57, 3);

        moraleTotal = new Label("");
        moraleTotal.setTitle("Morale");
        moraleTotal.setStyleName("clearFontSmall");
        add(moraleTotal, 120, 20);
        if (ArmyStore.getInstance().isInitialized()) {
            setLabelValues();
        } else {
            LoadUtil.getInstance().registerHandlerForClean(LoadingEventManager.addArmiesLoadedHandler(new ArmiesLoadedHandler() {
                @Override
                public void onUnitChanged(ArmiesLoadedEvent event) {
                    setLabelValues();
                }
            }));
        }
    }

    public void setLabelValues() {
        int totalHead = ArmyStore.getInstance().getBrigadeHeadCountByNation(nationId);

        int morale = 100;
        headCountTotal.setText(totalHead + " - 0");
        moraleTotal.setText(String.valueOf(morale));
    }

    public void setLabelValues(final int round) {
        final int initHead = PlaybackStore.getInstance().getInitialHeadCountByNation(nationId);
        final int activeHead = PlaybackStore.getInstance().getHeadcountByRoundNation(round, nationId);
        headCountTotal.setText(activeHead + " - " + (initHead - activeHead));
        final int morale = PlaybackStore.getInstance().getMoraleByRoundNation(round, nationId);
        moraleTotal.setTitle("Morale : " + morale + " - brigades : " + PlaybackStore.getInstance().getBrigadeCountByNationRound(round, nationId));
        moraleTotal.setText(String.valueOf(morale));
    }
}
