package com.eaw1805.www.controllers.remote.hotspot.army;


import com.eaw1805.data.dto.web.ClientOrderDTO;
import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitsSaveChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor {

    private transient List<ArmyDTO> dbArmiesList = new ArrayList<ArmyDTO>(), chArmiesList = new ArrayList<ArmyDTO>();
    private transient Map<Integer, List<BrigadeDTO>> newBrigMap = new HashMap<Integer, List<BrigadeDTO>>();
    private transient final Map<Integer, List<BattalionDTO>> newBattMap = new HashMap<Integer, List<BattalionDTO>>();
    private transient final ArmySaveChangesProcessor aChProcc;
    private transient final CorpsSaveChangesProcessor cChProcc;
    private transient final BrigadesChangesProcessor bChProcc;
    private transient final BattalionsSaveChangesProcessor battChProcc;

    /**
     * Default constructor.
     *
     * @param gameId    the game of the order.
     * @param nationId  the owner of the order.
     * @param turn      the turn of the order.
     * @param relOrders the orders.
     */
    public UnitsSaveChangesProcessor(final int thisScenario, final int gameId,
                                     final int nationId,
                                     final int turn,
                                     final Map<Integer, List<ClientOrderDTO>> relOrders) {
        super(thisScenario, gameId, nationId, turn);
        aChProcc = new ArmySaveChangesProcessor(thisScenario, gameId, nationId, turn, relOrders);
        cChProcc = new CorpsSaveChangesProcessor(thisScenario, gameId, nationId, turn, relOrders);
        bChProcc = new BrigadesChangesProcessor(thisScenario, gameId, nationId, turn, relOrders);
        battChProcc = new BattalionsSaveChangesProcessor(thisScenario, gameId, nationId, turn, relOrders);
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        dbArmiesList = (List<ArmyDTO>) dbData;
        chArmiesList = (List<ArmyDTO>) chData;
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        newBrigMap = (Map<Integer, List<BrigadeDTO>>) chData;

    }

    public List<OrderDTO> processChanges() {
        // The returned list of commands
        final List<OrderDTO> allArmyCommands = new ArrayList<OrderDTO>();

        // Preparing data for the other processors
        final Map<Integer, ArmyDTO> dbArmies = new HashMap<Integer, ArmyDTO>(), chArmies = new HashMap<Integer, ArmyDTO>();
        final Map<Integer, CorpDTO> dbCorps = new HashMap<Integer, CorpDTO>(), chCorps = new HashMap<Integer, CorpDTO>();
        final Map<Integer, BrigadeDTO> dbBrigades = new HashMap<Integer, BrigadeDTO>(), chBrigades = new HashMap<Integer, BrigadeDTO>();
        final Map<Integer, BattalionDTO> dbBattalions = new HashMap<Integer, BattalionDTO>(), chBattalions = new HashMap<Integer, BattalionDTO>();

        for (final ArmyDTO army : dbArmiesList) {
            if (army.getArmyId() != 0) {
                dbArmies.put(army.getArmyId(), army);
            }

            dbCorps.putAll(army.getCorps());
            dbCorps.remove(0);
            for (final CorpDTO corp : army.getCorps().values()) {
                dbBrigades.putAll(corp.getBrigades());
                for (final BrigadeDTO brigade : corp.getBrigades().values()) {
                    for (final BattalionDTO battalion : brigade.getBattalions()) {
                        dbBattalions.put(battalion.getId(), battalion);
                    }
                }
            }
        }

        for (final ArmyDTO army : chArmiesList) {
            if (army.getArmyId() != 0) {
                chArmies.put(army.getArmyId(), army);
            }
            chCorps.putAll(army.getCorps());
            chCorps.remove(0);
            for (final CorpDTO corp : army.getCorps().values()) {
                chBrigades.putAll(corp.getBrigades());
                for (final BrigadeDTO brigade : corp.getBrigades().values()) {
                    for (final BattalionDTO battalion : brigade.getBattalions()) {
                        chBattalions.put(battalion.getId(), battalion);
                    }
                }
            }
        }

        aChProcc.addData(dbArmies, chArmies);
        allArmyCommands.addAll(aChProcc.processChanges());

        cChProcc.addData(dbCorps, chCorps);
        allArmyCommands.addAll(cChProcc.processChanges());

        bChProcc.addData(dbBrigades, chBrigades);
        bChProcc.addData(null, newBrigMap);
        allArmyCommands.addAll(bChProcc.processChanges());

        battChProcc.addData(dbBattalions, chBattalions);
        battChProcc.addData(null, newBattMap);
        allArmyCommands.addAll(battChProcc.processChanges());

        return allArmyCommands;
    }

}
