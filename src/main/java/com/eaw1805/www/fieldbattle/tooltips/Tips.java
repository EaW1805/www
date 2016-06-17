package com.eaw1805.www.fieldbattle.tooltips;

import com.google.gwt.user.client.ui.Widget;
import com.eaw1805.www.fieldbattle.widgets.ToolTipPanel;

import java.util.HashMap;
import java.util.Map;

public class Tips {
    final public static Map<String, String> OrderTip = new HashMap<String, String>();
    static {
        OrderTip.put("SELECT_AN_ORDER", "When SELECT AN ORDER is selected, the brigade has not ordered to do anything. It will act as it was given the Defend Position command.");
        OrderTip.put("ENGAGE_IF_IN_RANGE", "The brigade will attack all troop types indicated in the additional order if they are within its field battle movement range in one of the half-rounds. It will try to engage in melee combat.");
        OrderTip.put("MOVE_TO_FIRE", "The brigade will attack all troop types indicated in the additional order if they are in line of sight during a half-round (the enemy's distance does not affect on this order - if they can be  seen, the brigade will attempt to attack). The unit will move towards the enemy, stop at 2 co-ordinates distance and fire at the enemy.");
        OrderTip.put("MOVE_TO_ENGAGE", "The brigade will attack all troop types indicated in the additional order if they are in line of sight during a half-round (the enemy's distance does not affect on this order - if they can be  seen, the brigade will attempt to attack). The unit will move towards the enemy and try to engage it in melee combat.");
        OrderTip.put("ATTACK_AND_REFORM", "The brigade will attack the troop type indicated in the additional order if it is within its field battle movement range during its own half-round. After the attack it will return to its starting position.");
        OrderTip.put("DEFEND_POSITION", "The brigade moves toward the indicated destination and will try to defend it. It will stand still at that location and will shoot at those troop types indicated in the additional order (If specified troop type isn’t found the brigade will find other targets) or towards a specific target (for example a fortress wall or a bridge).");
        OrderTip.put("MAINTAIN_DISTANCE", "If enemy brigades are closer than 'x' co-ordinates from the current position (in direction of the enemy set-up-area) then it will pull back 'x' co-ordinates towards its own setup-area. The distance ‘x’ is to be indicated in the additional order. If no number is specified then the brigade will attempt to keep a distance of 5.");
        OrderTip.put("RETREAT", "The brigade retreats until it reaches the furthest position back and then will flee from the battle field. It will try to avoid melee combat and stay away from the enemy while retreating and while waiting to leave the battlefield.");
        OrderTip.put("BREAK_DETACHMENT", "The brigades in the detachment will break their common formation and will start acting independently following a new order defined from the above orders.");
        OrderTip.put("HEAVY_CAVALRY_CHARGE", "This order is available only to heavy cavalry brigades. The brigade will move to the first indicated destination and will charge towards the second destination. Heavy cavalry charge lasts for 2 combat rounds and increases your cavalry’s efficiency in melee combat by 50%, however after the 2 rounds of the charge the brigade has 60% less efficiency for 4 combat rounds due to exhaustion. The two combat rounds start from the moment your cavalry reaches destination 1 and begins its charge towards destinations 2 regardless of the distance or the time it actually takes your troops to reach the second destination. The charge effect may be used once per battle so after the 6 rounds (2 for the charge and 4 for recovery) your cavalry acts as if it is ordered.");
        OrderTip.put("ATTACK_ENEMY_STRATEGIC_POINTS", "Move towards the enemy's strategic points.");
        OrderTip.put("RECOVER_OWN_STRATEGIC_POINTS", "Move towards own strategic points if they are captured by the enemy.");
        OrderTip.put("FOLLOW_DETACHMENT", "Apart from giving orders to individual brigades, it is also possible to group some brigades together and form a \" battle division\". Once the division of brigades has been formed, it can be ordered to move and act as a single entity, by following the actions of one specific brigade that must be designated as the division's leader.");
        OrderTip.put("DIG_ENTRENCHMENTS", "The pioneer brigade will try to move to the indicated destination and dig entrenchments there.");
        OrderTip.put("BUILD_PONTOON_BRIDGE", "The pioneer brigade will move to the indicated destination and build a pontoon bridge across neighboring river co-ordinates. It takes 3 full turns to build the pontoon bridge per river tile. Thus, if a major river is 3 tiles-wide, it takes 9 turns to build a pontoon bridge over it. ");
        OrderTip.put("MOVE_TO_DESTROY_FORTIFICATIONS", "The pioneer brigade will move towards the indicated destination. If in the beginning of a half-round after it reaches its destination, an enemy fortification is in its line of sight it will move towards that and try to destroy it. ");
        OrderTip.put("MOVE_TO_DESTROY_BRIDGES", "The pioneer brigade will move towards the indicated destination. If in the beginning of a half-round after it reaches its destination an enemy pontoon bridge is in its line of sight it will move towards it and try to destroy it.");
    }

    /**
     * Tips related to order panel
     */
    public static String ORDER_COMMAND = "Give brigade an order";
    public static String ORDER_FORMATION = "Assign formation";
    public static String ORDER_DISTANCE = "Set the distance to maintain";
    public static String ORDER_MOVE = "Move brigade to a position";
    public static String ORDER_SP = "Move brigade to capture strategic points";
    public static String ORDER_LEADER = "Select the leader this brigade will follows";
    public static String ORDER_DETACHMENT = "Select the position in the detachment brigade will have";
    public static String ORDER_ENEMY_TYPE = "Select the type of the enemies this brigade will prefer to attack.";
    public static String ORDER_ENEMY_FORMATION = "Select the formation this brigade will prefer to attack.";
    public static String ORDER_HEADCOUNT = "Brigade will prefer enemies with hightest head count.";
    public static String ORDER_RANGE = "Brigade will prefer enemies that are closest in range.";
    public static String ORDER_TRIGGER_ROUND = "Give the round that the order should be activated.";
    public static String ORDER_TRIGGER_HEADCOUNT = "If the headcount falls bellow this value the order will be activated.";
    public static String ORDER_TRIGGER_LASTDEST = "If this is checked, the order will be activated when the last destination reached.";
    public static String ORDER_TRIGGER_CAPTURE_ENEMY = "Activate the order when our forces capture an enemy strategic point.";
    public static String ORDER_TRIGGER_CAPTURE_OWN = "Activate the order when enemy forces capture one of our strategic points.";


    /**
     * Tips related to filters
     */
    public static String FILTER_INFANTRY = "Show/hide infantry Brigades.";
    public static String FILTER_CAVALRY = "Show/hide cavalry Brigades.";
    public static String FILTER_ARTILLERY = "Show/hide artillery Brigades.";
    public static String FILTER_ORDERS = "Show/hide brigades that have orders, don't have orders or all of them.";
    public static String FILTER_PLACED = "Show/hide brigades that are placed on field, not placed on field or all of them.";
    public static String FILTER_SHORT= "Short brigades. Short them by power, headcount or efficiency";

    /**
     * Tips related to other UI elements.
     */
    public static String ELEMENT_SAVE = "Save orders";
    public static String ELEMENT_ZOOM_IN = "Zoom in";
    public static String ELEMENT_ZOOM_OUT = "Zoom out";
    public static String ELEMENT_SHOW_MINIMAP = "Show/hide minimap";
    public static String ELEMENT_SHOW_PANELS = "Show/hide panel";
    public static String ELEMENT_SETTINGS = "Open settings";

    /**
     * Tips related to brigades and commanders actions
     */
    public static String ADD_COMMANDER = "Assign a commander to this brigade.";
    public static String REMOVE_COMMANDER = "Remove commander from this brigade.";
    public static String OVERALL_COMMANDER = "Set or unset commander as overall commander of the battle.";
    public static String BRIGADE_LOCK = "Lock/unlock brigade in panel. By locking it you can interact with the map and always view this brigade in the panel.";
    public static String BRIGADE_NEXT_ROUND = "View state of the brigade in the next half round.";
    public static String BRIGADE_PREVIOUS_ROUND = "View state of the brigade in the previous half round.";
    public static String BRIGADE_LOCK_ORDER = "Lock/unlock orders for this brigade. Unlock the order if you want to change the orders for this brigade. You can change orders only to a limited number of brigades. Lock order if you want to undo changes in order or change orders to an other brigade.";
    public static String BRIGADE_CLEAR_ORDER= "Clear order, reset to default values.";
    public static String BRIGADE_ACTIVATE_PANEL = "Show/hide activation options. You can set the conditions for the additional order to be executed.";
    public static String BRIGADE_TARGET_PANEL = "Show/hide enemy target options. You can set what enemy troops this brigade will prefer to attack.";

    /**
     * Tips related to playback panel
     */
    public static String PLAYBACK_PLAY = "Replay the battle and see details of what happened.";
    public static String PLAYBACK_PAUSE = "Pause playback.";
    public static String PLAYBACK_NEXT = "Go to next half round.";
    public static String PLAYBACK_PREVIOUS = "Go to previous half round.";

    /**
     * Generates a tooltip for the corresponding element.
     *
     * @param widget The element to create the tooltip for.
     * @param tip The text inside the tooltip.
     */
    public static void generateTip(final Widget widget, final String tip) {
        new ToolTipPanel(widget, false) {

            @Override
            public void generateTip() {
                setTooltip(new TipPanel(tip));
            }


        };
    }

}
