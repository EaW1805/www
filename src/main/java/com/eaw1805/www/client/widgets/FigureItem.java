package com.eaw1805.www.client.widgets;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.www.client.events.units.UnitEventManager;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.MovementStore;

public class FigureItem
        extends AbsolutePanel
        implements ArmyConstants {

    private final DualStateImage figImg = new DualStateImage() {
        public void onDetach() {
            super.onDetach();
            GameStore.getInstance().deselectUnit();
        }
    };

    public FigureItem(final String url, final int tileSize, final int type, final int id,
                      final int nationId, final int sectorId,
                      final boolean lockInfoPanel, final int powerInit) {
        super();

        final int power;
        if (powerInit < 0) {
            power = 0;
        } else if (powerInit > 10) {
            power = 10;
        } else {
            power = powerInit;
        }

        final Image baseImg = new Image();
        final Image powerImg = new Image();
        switch (type) {
            case ARMY:
                baseImg.setUrl("http://static.eaw1805.com/images/figures/armyBase.png");
                baseImg.setSize(tileSize + "px", tileSize + "px");
                add(baseImg, 0, 0);
                if (power < 10) {
                    powerImg.setUrl("http://static.eaw1805.com/images/figures/" + nationId
                            + "/UnitMap0" + power + ".png");
                } else {
                    powerImg.setUrl("http://static.eaw1805.com/images/figures/" + nationId
                            + "/UnitMap" + power + ".png");
                }
                powerImg.setSize(tileSize + "px", tileSize + "px");
                add(powerImg, 0, 0);
                break;

            case FLEET:
                baseImg.setUrl("http://static.eaw1805.com/images/figures/fleetBase.png");
                baseImg.setSize(tileSize + "px", tileSize + "px");
                add(baseImg, 0, 0);
                if (power < 10) {
                    powerImg.setUrl("http://static.eaw1805.com/images/figures/" + nationId
                            + "/FleetMap0" + power + ".png");
                } else {
                    powerImg.setUrl("http://static.eaw1805.com/images/figures/" + nationId
                            + "/FleetMap" + power + ".png");
                }
                powerImg.setSize(tileSize + "px", tileSize + "px");
                add(powerImg, 0, 0);
                break;

            case CORPS:
                baseImg.setUrl("http://static.eaw1805.com/images/figures/corpBase.png");
                baseImg.setSize(tileSize + "px", tileSize + "px");
                add(baseImg, 0, 0);
                if (power < 10) {
                    powerImg.setUrl("http://static.eaw1805.com/images/figures/" + nationId
                            + "/UnitMap0" + power + ".png");
                } else {
                    powerImg.setUrl("http://static.eaw1805.com/images/figures/" + nationId
                            + "/UnitMap" + power + ".png");
                }
                powerImg.setSize(tileSize + "px", tileSize + "px");
                add(powerImg, 0, 0);
                break;

            default:
                // do nothing

        }

        this.setSize(tileSize + "px", tileSize + "px");

        this.figImg.setUrl(url);
        this.figImg.setSize(tileSize + "px", tileSize + "px");
        this.figImg.setStyleName("pointer");
        this.add(figImg, 0, 0);
        this.setId(id);

        final Image moveImg = new Image("http://static.eaw1805.com/images/figures/walk.png");
        final MovementStore mvStore = MovementStore.getInstance();
        if (mvStore.hasMovedThisTurn(type, id)) {
            moveImg.setVisible(true);
        } else {
            moveImg.setVisible(false);
        }
        this.add(moveImg, tileSize - tileSize / 3, 0);

        moveImg.setSize(tileSize / 3 + "px", tileSize / 3 + "px");

        if (lockInfoPanel) {
            UnitEventManager.doSelection(type, id, nationId, sectorId);

        } else {
            switch (type) {
                case ARMY:
                    if (nationId == GameStore.getInstance().getNationId()) {
                        figImg.setTitle("Click b to view the corps for this army");
                    }
                    break;
                case CORPS:
                    if (nationId == GameStore.getInstance().getNationId()) {
                        figImg.setTitle("Click b to view the brigades for this corps");
                    }
                    break;
                case FLEET:
                    if (nationId == GameStore.getInstance().getNationId()) {
                        figImg.setTitle("Click b to view the ships for this fleet");
                    }
                    break;
                default:
                    //do nothing here
            }
            figImg.addMouseOverHandler(new MouseOverHandler() {
                public void onMouseOver(final MouseOverEvent event) {
                    GameStore.getInstance().selectUnit(type, id);
                    UnitEventManager.doSelection(type, id, nationId, sectorId);
                }
            });
            figImg.addMouseOutHandler(new MouseOutHandler() {
                public void onMouseOut(final MouseOutEvent event) {
                    GameStore.getInstance().deselectUnit();
                    UnitEventManager.undoSelection();
                }
            });
        }
    }

    public final void setId(final int id) {
        figImg.setId(id);
    }

    /**
     * @return the figImg
     */
    public DualStateImage getFigImg() {
        return figImg;
    }

}
