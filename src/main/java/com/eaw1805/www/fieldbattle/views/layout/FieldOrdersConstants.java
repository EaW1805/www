package com.eaw1805.www.fieldbattle.views.layout;

public interface FieldOrdersConstants {


    static public final int ORDER_TYPE_FIELD = 1;
    static public final int ORDER_DISTANCE_FIELD = 2;
    static public final int ORDER_POSITION_X1_FIELD = 3;
    static public final int ORDER_POSITION_Y1_FIELD = 4;
    static public final int ORDER_POSITION_X2_FIELD = 5;
    static public final int ORDER_POSITION_Y2_FIELD = 6;
    static public final int ORDER_POSITION_X3_FIELD = 7;
    static public final int ORDER_POSITION_Y3_FIELD = 8;
    static public final int ORDER_TARGET_ARMY_FIELD = 9;
    static public final int ORDER_TARGET_FORMATION_FIELD = 10;
    static public final int ORDER_TARGET_NATION_FIELD = 11;
    static public final int ORDER_PREFER_HEADCOUNT_FIELD = 12;
    static public final int ORDER_PREFER_RANGE_FIELD = 13;
    static public final int ORDER_ACTIVATE_ROUND_FIELD = 14;
    static public final int ORDER_ACTIVATE_HEADCOUNT_FIELD = 15;
    static public final int ORDER_ACTIVATE_LAST_DESTINATION_FIELD = 16;
    static public final int ORDER_ACTIVATE_CAPTURE_ENEMY_SP_FIELD = 17;
    static public final int ORDER_ACTIVATE_ENEMY_CAPTURE_OWN_SP_FIELD = 18;
    static public final int ORDER_CUSTOM_POSITION_X1_FIELD = 19;
    static public final int ORDER_CUSTOM_POSITION_Y1_FIELD = 20;
    static public final int ORDER_CUSTOM_POSITION_X2_FIELD = 21;
    static public final int ORDER_CUSTOM_POSITION_Y2_FIELD = 22;
    static public final int ORDER_CUSTOM_POSITION_X3_FIELD = 23;
    static public final int ORDER_CUSTOM_POSITION_Y3_FIELD = 24;
    static public final int ORDER_FORMATION_FIELD = 25;

    static public final int ORDER_STRATEGIC_POSITION_X1_FIELD = 26;
    static public final int ORDER_STRATEGIC_POSITION_Y1_FIELD = 27;
    static public final int ORDER_STRATEGIC_POSITION_X2_FIELD = 28;
    static public final int ORDER_STRATEGIC_POSITION_Y2_FIELD = 29;
    static public final int ORDER_STRATEGIC_POSITION_X3_FIELD = 30;
    static public final int ORDER_STRATEGIC_POSITION_Y3_FIELD = 31;

    static public final int ORDER_DETACHMENT_LEADER_FIELD = 42;
    static public final int ORDER_DETACHMENT_POSITION = 43;

    static public final int MAGNIFYING_POSITION_1 = 100;
    static public final int MAGNIFYING_POSITION_2 = 101;
    static public final int MAGNIFYING_POSITION_3 = 102;
    static public final int MAGNIFYING_CUSTOM_POSITION_1 = 103;
    static public final int MAGNIFYING_CUSTOM_POSITION_2 = 104;
    static public final int MAGNIFYING_CUSTOM_POSITION_3 = 105;
    static public final int MAGNIFYING_STRATEGIC_POSITION_1 = 106;
    static public final int MAGNIFYING_STRATEGIC_POSITION_2 = 107;
    static public final int MAGNIFYING_STRATEGIC_POSITION_3 = 108;

    public enum FormationEnum {
        ALL,
        COLUMN,
        LINE,
        SQUARE,
        SKIRMISH,
        FLEE;
    }


    public enum ArmEnum {
        ALL,
        INFANTRY,
        CAVALRY,
        ARTILLERY;
    }

    public enum OrdersEnum {
        SELECT_AN_ORDER(false),
        ENGAGE_IF_IN_RANGE(true),
        MOVE_TO_FIRE(true),
        MOVE_TO_ENGAGE(true),
        ATTACK_AND_REFORM(true),
        DEFEND_POSITION(false),
        MAINTAIN_DISTANCE(false),
        RETREAT(false),
        BREAK_DETACHMENT(false),
        HEAVY_CAVALRY_CHARGE(true),
        // strategic points-related orders
        ATTACK_ENEMY_STRATEGIC_POINTS(true),
        RECOVER_OWN_STRATEGIC_POINTS(true),
        FOLLOW_DETACHMENT(false),
        // pioneer orders
        DIG_ENTRENCHMENTS(false),
        BUILD_PONTOON_BRIDGE(false),
        MOVE_TO_DESTROY_FORTIFICATIONS(true),
        MOVE_TO_DESTROY_BRIDGES(true);

        /**
         * Indicates whether the particular order is an attack order.
         */
        private final boolean attackOrder;

        private OrdersEnum(boolean attackOrder) {
            this.attackOrder = attackOrder;
        }
    }

    public enum DetachmentPosition {

        TOP_LEFT(-1, -1),
        TOP_CENTER(0, -1),
        TOP_RIGHT(1, -1),

        LEFT(-1, 0),
        RIGHT(1, 0),

        BOTTOM_LEFT(-1, 1),
        BOTTOM_CENTER(0, 1),
        BOTTOM_RIGHT(1, 1);

        private final int displacementX;
        private final int displacementY;

        private DetachmentPosition(int displacementX, int displacementY) {
            this.displacementX = displacementX;
            this.displacementY = displacementY;
        }

        public int getDisplacementX() {
            return displacementX;
        }

        public int getDisplacementY() {
            return displacementY;
        }

    }


}
