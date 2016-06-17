package com.eaw1805.www.client.views.infopanels.units.mini;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.fleet.ShipDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.infopanels.TradeInfoViewInterface;
import com.eaw1805.www.client.widgets.ClickAbsolutePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.ToolTipPanel;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.NavyStore;

public class ShipInfoMini
        extends VerticalPanel
        implements ArmyConstants, TradeInfoViewInterface {

    private ClickAbsolutePanel ShipPanel;
    private ShipDTO ship;

    public ShipInfoMini(final ShipDTO ship, final boolean enabled, final boolean enableRepair) {
        setSize("170px", "89px");
        this.setShip(ship);

        ShipPanel = new ClickAbsolutePanel();
        this.add(ShipPanel);
        this.ShipPanel.setStyleName("shipMiniInfoPanel");
        ShipPanel.setStyleName("clickArmyPanel", true);
        this.ShipPanel.setSize("170px", "89px");

        final Image shipTypeImg = new Image("http://static.eaw1805.com/images/ships/" + ship.getNationId() + "/" + ship.getType().getIntId() + ".png");
        shipTypeImg.setTitle(ship.getType().getName());
        this.ShipPanel.add(shipTypeImg, 3, 3);
        shipTypeImg.setSize("32px", "32px");

        final Label lblName = new Label(ship.getName());
        lblName.setStyleName("clearFontSmall");
        this.ShipPanel.add(lblName, 40, 4);
        lblName.setSize("124px", "15px");

        final Label lblCondition = new Label(ship.getCondition() + "%");
        lblCondition.setStyleName("clearFontSmall");
        this.ShipPanel.add(lblCondition, 3, 41);
        lblCondition.setSize("41px", "15px");

        final Label lblMp = new Label((int) (ship.getType().getMovementFactor() * ship.getCondition() / 100d) + " MPs");
        lblMp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblMp.setStyleName("clearFontMini");
        this.ShipPanel.add(lblMp, 123, 71);
        lblMp.setSize("41px", "12px");

        final Label lblMoney = new Label(ship.getType().getName());
        lblMoney.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lblMoney.setStyleName("clearFontMini");
        this.ShipPanel.add(lblMoney, 42, 29);
        lblMoney.setSize("122px", "27px");
        final String marinesExp;
        switch (ship.getExp()) {
            case 0:
                marinesExp = "R";
                break;
            case 1:
                marinesExp = "V";
                break;
            case 2:
                marinesExp = "E";
                break;
            default:
                marinesExp = "";
        }
        final Label lblMarines = new Label(ship.getMarines() + " " + marinesExp + " marines");
        lblMarines.setStyleName("clearFontSmall");
        this.ShipPanel.add(lblMarines, 3, 70);
        lblMarines.setSize("122px", "15px");

        com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
        final Label lblWeight = new Label(numberFormat.format(ship.getType().getLoadCapacity()) + " tons");
        lblWeight.setStyleName("clearFontSmall");
        this.ShipPanel.add(lblWeight, 3, 56);

        if (!enabled) {
            final Image disabledImage = new Image("http://static.eaw1805.com/images/infopanels/disabledMini.png");
            disabledImage.setTitle("Further orders are disabled due to orders already issued that may be in conflict");
            ShipPanel.add(disabledImage);
        }

        if (enableRepair && ship.getCondition() < 100) {
            final ImageButton repairImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButRepairOff.png");
            repairImg.setTitle("Repair");
            repairImg.setSize("20px", "20px");
            repairImg.setStyleName("pointer");
            (new DelEventHandlerAbstract() {
                @Override
                public void execute(MouseEvent event) {
                    SectorDTO sectorDTO = MapStore.getInstance().getRegionSectorByRegionIdXY(getShip().getRegionId(), getShip().getX(), getShip().getY());
                    NavyStore.getInstance().repairShip(getShip().getId(), sectorDTO.getId());
                }
            }).addToElement(repairImg.getElement()).register();

            //add tooltip for more information about the repair
            new ToolTipPanel(repairImg) {
                @Override
                public void generateTip() {
                    setTooltip(new ShipRepairCostMini(ship));
                }
            };
            ShipPanel.add(repairImg, 36, 38);
        }
    }

    public void closeTradePanel() {
        // do nothing
    }

    /**
     * @return the shipPanel
     */
    public ClickAbsolutePanel getShipPanel() {
        return ShipPanel;
    }

    /**
     * @return the ship
     */
    public ShipDTO getShip() {
        return ship;
    }

    /**
     * @param ship the ship to set
     */
    public void setShip(final ShipDTO ship) {
        this.ship = ship;
    }

}

