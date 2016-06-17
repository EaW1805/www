package com.eaw1805.www.fieldbattle.views.layout.infopanels;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.field.FieldBattleOrderDTO;

public class OrderInfoPanel extends AbsolutePanel {
    final BrigadeDTO brigade;
    FieldBattleOrderDTO order;
    final boolean isBasic;
    public OrderInfoPanel(final BrigadeDTO brigade, final boolean isBasic) {
        setSize("366px", "90px");
        setStyleName("selectorOption");
        this.brigade = brigade;
        this.isBasic = isBasic;
//        if (brigade.getAdditionalOrder() != null
//                && !"SELECT_AN_ORDER".equals(brigade.getAdditionalOrder().getOrderType())) {
//            add(createLabel(brigade.getAdditionalOrder().getOrderType().replaceAll("_", " ")), 5, 50);
//
//        } else {
//            add(createLabel("No additional order given"), 5, 50);
//        }
    }

    public Label createLabel(String text) {
        Label out = new Label(text);
        out.setStyleName("whiteText");
        return out;
    }

    public void setupPanel() {
        clear();
        if (isBasic) {
            order = brigade.getBasicOrder();
            add(createLabel("Basic Order"), 151, 3);
        } else {
            order = brigade.getAdditionalOrder();
            add(createLabel("Additional Order"), 131, 3);
        }

        if (order != null
                && !"SELECT_AN_ORDER".equals(order.getOrderType())) {
            add(createLabel(order.getOrderType().replaceAll("_", " ")), 3, 17);
            add(createLabel("Formation : " + order.getFormation()), 234, 17);
            if (order.getOrderType() != null &&
                    order.getOrderType().equals("MAINTAIN_DISTANCE")) {
                add(createLabel(order.getMaintainDistance() + " sectors"), 141, 17);
            }
            if (order.hasCheckPoint1() || order.hasCheckPoint2() || order.hasCheckPoint3()) {
                add(createLabel("Move : "), 3, 31);
            }
            int offX = 50;
            if (order.hasCheckPoint1()) {
                add(createLabel(order.getCheckPoint1().toString()+","), offX, 31);
                offX += 40;
            }
            if (order.hasCheckPoint2()) {
                add(createLabel(order.getCheckPoint2().toString()+","), offX, 31);
                offX+=40;
            }
            if (order.hasCheckPoint3()) {
                add(createLabel(order.getCheckPoint3().toString()+","), offX, 31);

            }
            if (order.getOrderType() != null
                    && (order.getOrderType().equals("ATTACK_ENEMY_STRATEGIC_POINTS")
                        || order.getOrderType().equals("RECOVER_OWN_STRATEGIC_POINTS"))) {
                offX = 183;
                if (order.hasStrategicPoint1() || order.hasStrategicPoint2() || order.hasStrategicPoint3()) {
                    add(createLabel("SPs : "), offX, 31);
                    offX+=40;
                }
                if (order.hasStrategicPoint1()) {
                    add(createLabel(order.getStrategicPoint1().toString()), offX, 31);
                    offX+=40;
                }
                if (order.hasStrategicPoint2()) {
                    add(createLabel(order.getStrategicPoint2().toString()), offX, 31);
                    offX+=40;
                }
                if (order.hasStrategicPoint3()) {
                    add(createLabel(order.getStrategicPoint3().toString()), offX, 31);
                    offX+=40;
                }
            }
            add(createLabel("Attack : " + order.getTargetArm() + " types"), 3, 46);
            add(createLabel(order.getTargetFormation() + " formations"), 124, 46);
            if (order.isTargetHighestHeadcount()) {
                add(createLabel("Prefer brigades with high headcount"), 3, 61);
            } else if (order.isTargetClosestInRange()) {
                add(createLabel("Prefer brigades closest in range"), 3, 61);
            }
        } else {
            add(createLabel("No order given"), 17, 3);
        }
    }

    public void onAttach() {
        super.onAttach();
        setupPanel();
    }

}
