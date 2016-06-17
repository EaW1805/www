package com.eaw1805.www.client.events.loading;

import com.google.gwt.event.shared.HandlerManager;
import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.data.dto.web.economy.BaggageTrainDTO;
import com.eaw1805.data.dto.web.fleet.FleetDTO;
import com.eaw1805.www.shared.AlliedUnits;
import com.eaw1805.www.shared.ForeignUnits;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public final class LoadEventManager
        implements ArmyConstants {

    static public HandlerManager handlerManager = new HandlerManager(null);

    public static void loadChatMessages(final List<String> messages) {
        handlerManager.fireEvent(new ChatMessagesLoadedEvent(messages));
    }

    public static void loadAllUnits() {
        handlerManager.fireEvent(new AllUnitsLoadedEvent());
    }

    public static void loadRegion() {
        handlerManager.fireEvent(new RegionLoadedEvent());
    }

    public static void loadSectors() {
        handlerManager.fireEvent(new SectorsLoadedEvent());
    }

    public static void loadArmies(final Collection<ArmyDTO> armies) {
        handlerManager.fireEvent(new ArmiesLoadedEvent(ARMY, armies));
    }

    public static void loadBtrains(final List<BaggageTrainDTO> btrains) {
        handlerManager.fireEvent(new BtrainLoadedEvent(BAGGAGETRAIN, btrains));

    }

    public static void loadNavy(final List<FleetDTO> fleets) {
        handlerManager.fireEvent(new NavyLoadedEvent(FLEET, fleets));

    }

    public static void loadCommanders(final List<CommanderDTO> commanders) {
        handlerManager.fireEvent(new CommLoadedEvent(COMMANDER, commanders));

    }

    public static void loadSpies(final List<SpyDTO> spies) {
        handlerManager.fireEvent(new SpiesLoadedEvent(SPY, spies));
    }


    public static void loadAlliedUnits(final Map<Integer, Map<Integer, AlliedUnits>> alliedUnits) {
        handlerManager.fireEvent(new AlliedUnitsLoadedEvent(ALLIED, alliedUnits));
    }

    public static void loadForeignUnits(final ForeignUnits foreignUnits) {
        handlerManager.fireEvent(new ForeignUnitsLoadedEvent(FOREIGN, foreignUnits));
    }

    public static void loadPrSites() {
        handlerManager.fireEvent(new ProSiteLoadedEvent());
    }

    public static void loadOrders() {
        handlerManager.fireEvent(new OrdersLoadedEvent());
    }

    public static void loadNations(final List<NationDTO> nations) {
        handlerManager.fireEvent(new NationsLoadedEvent(nations));
    }

    public static void addChatMessagesLoadedHandler(final ChatMessagesLoadedHandler handler) {
        handlerManager.addHandler(ChatMessagesLoadedEvent.getType(), handler);
    }

    public static void addAllUnitsLoadedHandler(final AllUnitsLoadedHandler handler) {
        handlerManager.addHandler(AllUnitsLoadedEvent.TYPE, handler);
    }

    public static void addRegionLoadedHandler(final RegionLoadedHandler handler) {
        handlerManager.addHandler(RegionLoadedEvent.getType(), handler);
    }

    public static void addSectorsLoadedHandler(final SectorsLoadedHandler handler) {
        handlerManager.addHandler(SectorsLoadedEvent.getType(), handler);
    }

    public static void addArmiesLoadedHandler(final ArmiesLoadedHandler handler) {
        handlerManager.addHandler(ArmiesLoadedEvent.getType(), handler);
    }

    public static void addBtrainLoadedHandler(final BtrainLoadedHandler handler) {
        handlerManager.addHandler(BtrainLoadedEvent.getType(), handler);

    }

    public static void addNavyLoadedHandler(final NavyLoadedHandler handler) {
        handlerManager.addHandler(NavyLoadedEvent.getType(), handler);
    }

    public static void addCommLoadeddHandler(final CommLoadedHandler handler) {
        handlerManager.addHandler(CommLoadedEvent.getType(), handler);
    }

    public static void addSpiesLoadedHandler(final SpiesLoadedHandler handler) {
        handlerManager.addHandler(SpiesLoadedEvent.getType(), handler);
    }

    public static void addProSiteLoadedHandler(final ProSiteLoadedHandler handler) {
        handlerManager.addHandler(ProSiteLoadedEvent.getType(), handler);
    }

    public static void addAlliedUnitsLoadedHandler(final AlliedUnitsLoadedHandler handler) {
        handlerManager.addHandler(AlliedUnitsLoadedEvent.getType(), handler);
    }

    public static void addForeignUnitsLoadedHandler(final ForeignUnitsLoadedHandler handler) {
        handlerManager.addHandler(ForeignUnitsLoadedEvent.getType(), handler);
    }

    public static void addOrdersLoadedHandler(final OrdersLoadedHandler handler) {
        handlerManager.addHandler(OrdersLoadedEvent.getType(), handler);
    }

    public static void addNationsLoadedHandler(final NationsLoadedHandler handler) {
        handlerManager.addHandler(NationsLoadedEvent.getType(), handler);
    }

    public static void addBaseMapConstructedHandler(final BaseMapConstructedHandler handler) {
        handlerManager.addHandler(BaseMapConstructedEvent.TYPE, handler);
    }

    public static void addArmiesInitHandler(final ArmiesInitdHandler handler) {
        handlerManager.addHandler(ArmiesInitEvent.TYPE, handler);
    }

    public static void addAppInitHandler(final AppInitHandler handler) {
        handlerManager.addHandler(AppInitEvent.TYPE, handler);
    }

    public static void addRelationsLoadedHandler(final RelationsLoadedHandler handler) {
        handlerManager.addHandler(RelationsLoadedEvent.TYPE, handler);
    }

    public static void addMapUnitsLoadedHandler(final MapUnitsLoadedHandler handler) {
        handlerManager.addHandler(MapUnitsLoadedEvent.TYPE, handler);
    }

    public static void baseMapConstructed() {
        handlerManager.fireEvent(new BaseMapConstructedEvent());
    }

    public static void armiesInitialised() {
        handlerManager.fireEvent(new ArmiesInitEvent());
    }

    public static void initApp() {
        handlerManager.fireEvent(new AppInitEvent());
    }

    public static void relationsLoaded() {
        handlerManager.fireEvent(new RelationsLoadedEvent());
    }

    public static void mapUnitsLoaded() {
        handlerManager.fireEvent(new MapUnitsLoadedEvent());
    }

}
