package com.eaw1805.www.client.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.www.shared.stores.SoundStore;
import com.eaw1805.www.shared.stores.economy.BarrackStore;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.BaggageTrainStore;
import com.eaw1805.www.shared.stores.units.CommanderStore;
import com.eaw1805.www.shared.stores.units.NavyStore;
import com.eaw1805.www.shared.stores.units.SpyStore;

@SuppressWarnings({"deprecation", "restriction"})
public class RenamingLabel
        extends Label
        implements ArmyConstants {

    private int orgArmyType;
    private int typeId;

    public RenamingLabel(final String text, final int orgArmyType, final int typeId) {
        super(text);
        setLogicFunctions();
        setOrgArmyType(orgArmyType);
        setTypeId(typeId);
        this.setStyleName("editName", true);
    }

    private final void setLogicFunctions() {
        final RenamingLabel mySelf = this;
        this.addClickHandler(new ClickHandler() {
            public void onClick(final ClickEvent event) {
                event.stopPropagation();
                SoundStore.getInstance().playClickWooden();
                final AbsolutePanel parent = (AbsolutePanel) mySelf.getParent();
                final int top = parent.getWidgetTop(mySelf);
                final int left = parent.getWidgetLeft(mySelf);

                mySelf.removeFromParent();
                final TextBox nameTxtBx = new TextBox();
                nameTxtBx.setWidth("200px");
                nameTxtBx.addClickHandler(new ClickHandler() {
                    public void onClick(final ClickEvent event) {
                        event.stopPropagation();
                    }
                });

                nameTxtBx.addKeyPressHandler(new KeyPressHandler() {
                    public void onKeyPress(final KeyPressEvent event) {
                        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                            if (nameTxtBx.getText().length() >= 4 && nameTxtBx.getText().length() <= 30) {
                                setText(nameTxtBx.getText());
                                parent.add(mySelf, left, top);
                                nameTxtBx.removeFromParent();
                                doChangeName(getOrgArmyType());

                            } else {
                                new ErrorPopup(ErrorPopup.Level.WARNING, "A name must have at least 4 characters and at most 30", false);
                            }
                            MapStore.getInstance().setFocusLocked(false);
                        } else if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
                            nameTxtBx.setText(mySelf.getText());
                            parent.add(mySelf, left, top);
                            nameTxtBx.removeFromParent();
                        }
                    }
                });

                nameTxtBx.addFocusListener(new FocusListener() {
                    public void onLostFocus(final Widget sender) {
                        if (nameTxtBx.getText().length() >= 4 && nameTxtBx.getText().length() <= 30) {
                            setText(nameTxtBx.getText());
                            parent.add(mySelf, left, top);
                            nameTxtBx.removeFromParent();
                            doChangeName(getOrgArmyType());

                        } else {
                            parent.add(mySelf, left, top);
                            nameTxtBx.removeFromParent();
                        }

                        //relies
                        MapStore.getInstance().setFocusLocked(false);
                    }

                    public void onFocus(final Widget sender) {
                        //grab focus so the focus panel won't take it from you
                        MapStore.getInstance().setFocusLocked(true);
                    }
                });

                nameTxtBx.addFocusHandler(new FocusHandler() {

                    public void onFocus(final FocusEvent event) {
                        event.stopPropagation();

                    }
                });

                parent.add(nameTxtBx, left, top);
                nameTxtBx.setText(mySelf.getText());
                nameTxtBx.setFocus(true);
                //grab focus so the focus panel won't take it from you
                MapStore.getInstance().setFocusLocked(true);
            }
        });

    }

    protected void doChangeName(final int orgArmyType) {
        switch (orgArmyType) {
            case ARMY:
                ArmyStore.getInstance().renameArmy(getTypeId(), this.getText());
                break;

            case CORPS:
                ArmyStore.getInstance().renameCorps(getTypeId(), this.getText());
                break;

            case BRIGADE:
                // Update name only if it is not a new brigade
                if (getTypeId() != 0) {
                    ArmyStore.getInstance().renameBrigade(getTypeId(), this.getText());
                }
                break;

            case FLEET:
                NavyStore.getInstance().renameFleet(getTypeId(), this.getText());
                break;

            case SHIP:
                NavyStore.getInstance().renameShip(getTypeId(), this.getText());
                break;

            case SPY:
                if (getTypeId() != 0) {
                    SpyStore.getInstance().renameSpy(typeId, getText());
                }
                break;

            case BAGGAGETRAIN:
                BaggageTrainStore.getInstance().renameBaggageTrain(typeId, getText());
                break;

            case COMMANDER:
                CommanderStore.getInstance().renameCommander(getTypeId(), this.getText());
                break;

            case BARRACK:
                BarrackStore.getInstance().renameBarrack(getTypeId(), this.getText());
                break;

            default:
                //nothing to do here
                break;

        }
    }

    /**
     * @return the orgArmyType
     */
    public final int getOrgArmyType() {
        return orgArmyType;
    }

    /**
     * @return the typeId
     */
    public final int getTypeId() {
        return typeId;
    }

    /**
     * @param orgArmyType the orgArmyType to set
     */
    public final void setOrgArmyType(final int orgArmyType) {
        this.orgArmyType = orgArmyType;
    }

    /**
     * @param typeId the typeId to set
     */
    public final void setTypeId(final int typeId) {
        this.typeId = typeId;
    }
}
