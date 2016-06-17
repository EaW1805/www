package com.eaw1805.www.client.views.extras;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.views.infopanels.units.BrigadeInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.CorpsInfoPanel;
import com.eaw1805.www.client.views.infopanels.units.mini.BattalionInfoMini;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.ExchangeDragHandler;
import com.eaw1805.www.client.widgets.UnitImage;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;

import java.util.ArrayList;
import java.util.List;

public class ExchangeUnitsWidget extends AbsolutePanel implements ArmyConstants {
    private PickupDragController dragController;
    private ScrollPanel firstUnitScroll;
    private ScrollPanel secondUnitScroll;
    private VerticalPanel firstVpanel;
    private VerticalPanel secondVpanel;
    private int sectorId = 0;
    private int first = 0, second = 0, type = -1;
    private ArmyStore arStore = ArmyStore.getInstance();
    private boolean enabled = false;
    private List<AbsolutePositionDropController> firstPanelDc = new ArrayList<AbsolutePositionDropController>();
    private List<AbsolutePositionDropController> secondPanelDc = new ArrayList<AbsolutePositionDropController>();

    public ExchangeUnitsWidget(final int sectorId) {
        this.sectorId = sectorId;
        dragController = new PickupDragController(this, false);
        dragController.addDragHandler(new ExchangeDragHandler(this));
        setSize("628px", "358px");

        this.firstUnitScroll = new ScrollPanel();
        this.firstUnitScroll.setStyleName("freeArmyList");
        this.firstUnitScroll.setAlwaysShowScrollBars(true);
        add(this.firstUnitScroll, 0, 0);
        this.firstUnitScroll.setSize("628px", "164px");

        this.firstVpanel = new VerticalPanel();
        this.firstUnitScroll.setWidget(this.firstVpanel);
        this.firstVpanel.setSize("614px", "0");

        this.secondUnitScroll = new ScrollPanel();
        this.secondUnitScroll.setStyleName("freeCorpList");
        this.secondUnitScroll.setAlwaysShowScrollBars(true);
        add(this.secondUnitScroll, 0, 193);
        this.secondUnitScroll.setSize("628px", "164px");

        this.secondVpanel = new VerticalPanel();
        this.secondUnitScroll.setWidget(this.secondVpanel);
        this.secondVpanel.setSize("100%", "0");
    }

    public void populatePanel(final int order, final int unitId, final int type) {
        VerticalPanel targetVp = null;
        List<AbsolutePositionDropController> apDc = null;
        if (order == 1) {
            targetVp = firstVpanel;
            apDc = firstPanelDc;
            first = unitId;
        } else {
            targetVp = secondVpanel;
            apDc = secondPanelDc;
            second = unitId;
        }
        switch (type) {
            case ARMY:
                populateVpanels(getCorpUnitImages(unitId), targetVp, BRIGLIMIT[GameStore.getInstance().getNationId() - 1], type, apDc);
                break;
            case CORPS:
                populateVpanels(getBrigUnitImages(unitId), targetVp, BRIGLIMIT[GameStore.getInstance().getNationId() - 1], type, apDc);
                break;
            case BRIGADE:
                populateVpanels(getBattUnitImages(unitId), targetVp, 7, type, apDc);
                break;
            default:
                break;
        }

    }

    private List<UnitImage> getBattUnitImages(int unitId) {
        List<UnitImage> firstList = new ArrayList<UnitImage>();
        SectorDTO sector = RegionStore.getInstance().getRegionSectorsMap()
                .get(MapStore.getInstance().getActiveRegion()).get(sectorId);
        for (BattalionDTO batt : arStore.getBattalionsBySectorAndCorpId(
                sector, unitId)) {
            PopupPanel pPanel = new PopupPanel();
            pPanel.add(new BattalionInfoMini(batt));
            UnitImage battImage = new UnitImage("http://static.eaw1805.com/images/figures/"
                    + GameStore.getInstance().getNationId() + "/UnitMap00.png", pPanel);
            battImage.setId(batt.getId());
            firstList.add(battImage);
        }
        return firstList;
    }

    private List<UnitImage> getBrigUnitImages(final int unitId) {
        List<UnitImage> firstList = new ArrayList<UnitImage>();
        SectorDTO sector = RegionStore.getInstance().getRegionSectorsMap()
                .get(MapStore.getInstance().getActiveRegion()).get(sectorId);
        for (BrigadeDTO brig : arStore.getBrigadesBySectorAndCorpId(sector,
                unitId).values()) {
            PopupPanel pPanel = new PopupPanel();
            pPanel.add(new BrigadeInfoPanel(brig, false));
            UnitImage brigImage = new UnitImage("http://static.eaw1805.com/images/figures/"
                    + GameStore.getInstance().getNationId() + "/UnitMap00.png", pPanel);
            brigImage.setId(brig.getCorpId());
            firstList.add(brigImage);
        }
        return firstList;
    }

    private List<UnitImage> getCorpUnitImages(final int unitId) {
        List<UnitImage> firstList = new ArrayList<UnitImage>();
        SectorDTO sector = RegionStore.getInstance().getRegionSectorsMap()
                .get(MapStore.getInstance().getActiveRegion()).get(sectorId);
        for (CorpDTO corp : arStore.getCorpsBySectorAndArmyId(sector,
                unitId).values()) {
            PopupPanel pPanel = new PopupPanel();
            pPanel.add(new CorpsInfoPanel(corp, false));
            UnitImage corpImage = new UnitImage("http://static.eaw1805.com/images/figures/"
                    + GameStore.getInstance().getNationId() + "/UnitMap00.png", pPanel);
            corpImage.setId(corp.getCorpId());
            firstList.add(corpImage);
        }
        return firstList;
    }

    private void populateVpanels(final List<UnitImage> unitList,
                                 final VerticalPanel Vpanel,
                                 final int slots,
                                 final int type,
                                 final List<AbsolutePositionDropController> apDc) {
        for (AbsolutePositionDropController dc : apDc) {
            dragController.unregisterDropController(dc);
        }

        Vpanel.clear();
        int counter = 0;
        HorizontalPanel pnl = null;
        for (UnitImage unit : unitList) {
            if (counter == 0) {
                pnl = new HorizontalPanel();

                Vpanel.add(pnl);
            }
            if (counter < 9) {
                ClickAbsolutePanel slot = new ClickAbsolutePanel();
                slot.setSize("64px", "64px");
                if (type == BRIGADE && counter > 3) {
                    slot.setStyleName("freeBrigadeList");
                    slot.setId(counter + 1);
                } else if (type == BRIGADE) {
                    slot.setStyleName("selectArmyPanel");
                    slot.setId(counter + 1);
                } else {
                    slot.setStyleName("selectArmyPanel");
                }
                AbsolutePositionDropController dc = new AbsolutePositionDropController(slot);
                dragController.registerDropController(dc);
                apDc.add(dc);
                dragController.makeDraggable(unit);
                slot.add(unit);
                pnl.add(slot);

            } else {
                counter = 0;
            }
            counter++;
        }
        int dropSlots = (Vpanel.getWidgetCount() - 1) * 9 + pnl.getWidgetCount();
        while (dropSlots < slots) {
            if (counter == 0) {
                pnl = new HorizontalPanel();

                Vpanel.add(pnl);
            }
            if (counter < 9) {
                ClickAbsolutePanel slot = new ClickAbsolutePanel();
                slot.setSize("64px", "64px");
                if (type == BRIGADE && pnl.getWidgetCount() > 4) {
                    slot.setStyleName("freeBrigadeList");
                    slot.setId(pnl.getWidgetCount());
                } else if (type == BRIGADE) {
                    slot.setStyleName("selectArmyPanel");
                    slot.setId(pnl.getWidgetCount());
                } else {
                    slot.setStyleName("selectArmyPanel");
                }
                AbsolutePositionDropController dc = new AbsolutePositionDropController(slot);
                dragController.registerDropController(dc);
                apDc.add(dc);
                pnl.add(slot);
                dropSlots++;
                counter++;
            } else {
                counter = 0;
            }
        }
    }

    public void rearrangePanels() {

    }

    public void clearAll() {
        this.sectorId = 0;
        this.first = -1;
        this.second = -1;
        this.firstVpanel.clear();
        this.secondVpanel.clear();
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(final boolean enabled, final int firstId, final int secondId, final int type) {
        this.enabled = enabled;
        this.type = type;
        this.first = firstId;
        this.second = secondId;
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @return the first
     */
    public int getFirst() {
        return first;
    }

    /**
     * @return the second
     */
    public int getSecond() {
        return second;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(final int type) {
        this.type = type;
    }


}
