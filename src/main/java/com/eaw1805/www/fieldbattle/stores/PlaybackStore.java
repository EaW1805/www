package com.eaw1805.www.fieldbattle.stores;

import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CommanderDTO;
import com.eaw1805.data.dto.web.field.BrigadeMovementLogEntryDTO;
import com.eaw1805.data.dto.web.field.FieldBattleHalfRoundStatisticsDTO;
import com.eaw1805.data.dto.web.field.FieldBattlePositionDTO;
import com.eaw1805.data.dto.web.field.FieldBattleSectorDTO;
import com.eaw1805.data.dto.web.field.LongRangeAttackLogEntryDTO;
import com.eaw1805.data.dto.web.field.MeleeAttackLogEntryDTO;
import com.eaw1805.data.dto.web.field.RallyLogEntryDTO;
import com.eaw1805.data.dto.web.field.StructureStatusDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaybackStore implements ClearAble {

    private static PlaybackStore instance;

    private PlaybackStore() {
    }


    int rounds;

    final Map<Integer, BrigadeDTO[]> brigadeToRounds = new HashMap<Integer, BrigadeDTO[]>();
    final Map<Integer, Map<Integer, List<BrigadeDTO>>> nationToBrigadesRound = new HashMap<Integer, Map<Integer, List<BrigadeDTO>>>();
    Map<Integer, Map<Integer, RallyLogEntryDTO>> brigToRoundToRally = new HashMap<Integer, Map<Integer, RallyLogEntryDTO>>();
    Map<Integer, Map<Integer, BrigadeMovementLogEntryDTO>> brigToRoundToMovement = new HashMap<Integer, Map<Integer, BrigadeMovementLogEntryDTO>>();
    Map<Integer, Map<Integer, LongRangeAttackLogEntryDTO>> brigToRoundToLongRange = new HashMap<Integer, Map<Integer, LongRangeAttackLogEntryDTO>>();
    Map<Integer, Map<Integer, MeleeAttackLogEntryDTO>> brigToRoundToMelee = new HashMap<Integer, Map<Integer, MeleeAttackLogEntryDTO>>();
    Map<Integer, Map<Integer, List<LongRangeAttackLogEntryDTO>>> brigToRoundToReverseLongRange = new HashMap<Integer, Map<Integer, List<LongRangeAttackLogEntryDTO>>>();
    Map<Integer, Map<Integer, List<MeleeAttackLogEntryDTO>>> brigToRoundToReverseMelee = new HashMap<Integer, Map<Integer, List<MeleeAttackLogEntryDTO>>>();
    Map<Integer, Map<Integer, Integer>> brigToRoundToTotalKills = new HashMap<Integer, Map<Integer, Integer>>();
    final Map<Integer, List<FieldBattleSectorDTO>> strategicPointsByRound = new HashMap<Integer, List<FieldBattleSectorDTO>>();
    final Map<Integer, List<StructureStatusDTO>> structuresByRound = new HashMap<Integer, List<StructureStatusDTO>>();

    Map<Integer, FieldBattleHalfRoundStatisticsDTO> roundToStatistics = new HashMap<Integer, FieldBattleHalfRoundStatisticsDTO>();
    Map<Integer, Map<FieldBattlePositionDTO, Integer>> roundToCorpsesToNation = new HashMap<Integer, Map<FieldBattlePositionDTO, Integer>>();

    public static PlaybackStore getInstance() {
        if (instance == null) {
            instance = new PlaybackStore();
        }
        return instance;
    }

    public void initStatistics(List<FieldBattleHalfRoundStatisticsDTO> stats) {

        rounds = stats.size() - 1;

        for (FieldBattleHalfRoundStatisticsDTO stat : stats) {
            int round = stat.getHalfRound();
            roundToStatistics.put(round, stat);
            Map<Integer, Integer> thisRoundTotalKills = new HashMap<Integer, Integer>();
            brigToRoundToTotalKills.put(round, thisRoundTotalKills);

            for (BrigadeDTO brigade : stat.getAllBrigades()) {
                thisRoundTotalKills.put(brigade.getBrigadeId(), 0);
                if (!brigadeToRounds.containsKey(brigade.getBrigadeId())) {
                    brigadeToRounds.put(brigade.getBrigadeId(), new BrigadeDTO[stats.size()]);
                }
                brigadeToRounds.get(brigade.getBrigadeId())[round] = brigade;
                if (!nationToBrigadesRound.containsKey(brigade.getNationId())) {
                    nationToBrigadesRound.put(brigade.getNationId(), new HashMap<Integer, List<BrigadeDTO>>());
                }
                if (!nationToBrigadesRound.get(brigade.getNationId()).containsKey(round)) {
                    nationToBrigadesRound.get(brigade.getNationId()).put(round, new ArrayList<BrigadeDTO>());
                }
                nationToBrigadesRound.get(brigade.getNationId()).get(round).add(brigade);
            }
            brigToRoundToRally.put(round, new HashMap<Integer, RallyLogEntryDTO>());
            for (RallyLogEntryDTO entry : stat.getRallyLogEntriesSide0()) {
                brigToRoundToRally.get(round).put(entry.getBrigadeId(), entry);
            }
            for (RallyLogEntryDTO entry : stat.getRallyLogEntriesSide1()) {
                brigToRoundToRally.get(round).put(entry.getBrigadeId(), entry);
            }
            brigToRoundToMovement.put(round, new HashMap<Integer, BrigadeMovementLogEntryDTO>());
            for (BrigadeMovementLogEntryDTO entry : stat.getBrigadeMovementLogEntriesSide0()) {
                brigToRoundToMovement.get(round).put(entry.getBrigadeId(), entry);
            }
            for (BrigadeMovementLogEntryDTO entry : stat.getBrigadeMovementLogEntriesSide1()) {
                brigToRoundToMovement.get(round).put(entry.getBrigadeId(), entry);
            }
            brigToRoundToLongRange.put(round, new HashMap<Integer, LongRangeAttackLogEntryDTO>());
            brigToRoundToReverseLongRange.put(round, new HashMap<Integer, List<LongRangeAttackLogEntryDTO>>());
            for (LongRangeAttackLogEntryDTO entry : stat.getLongRangeAttackLogEntriesSide0()) {
                addToTotalMap(thisRoundTotalKills, entry.getAttackerBrigadeId(), entry.getCasualties());
                brigToRoundToLongRange.get(round).put(entry.getAttackerBrigadeId(), entry);
                if (!brigToRoundToReverseLongRange.get(round).containsKey(entry.getTargetBrigadeId())) {
                    brigToRoundToReverseLongRange.get(round).put(entry.getTargetBrigadeId(), new ArrayList<LongRangeAttackLogEntryDTO>());
                }
                brigToRoundToReverseLongRange.get(round).get(entry.getTargetBrigadeId()).add(entry);
            }
            for (LongRangeAttackLogEntryDTO entry : stat.getLongRangeAttackLogEntriesSide1()) {
                addToTotalMap(thisRoundTotalKills, entry.getAttackerBrigadeId(), entry.getCasualties());
                brigToRoundToLongRange.get(round).put(entry.getAttackerBrigadeId(), entry);
                if (!brigToRoundToReverseLongRange.get(round).containsKey(entry.getTargetBrigadeId())) {
                    brigToRoundToReverseLongRange.get(round).put(entry.getTargetBrigadeId(), new ArrayList<LongRangeAttackLogEntryDTO>());
                }
                brigToRoundToReverseLongRange.get(round).get(entry.getTargetBrigadeId()).add(entry);
            }
            brigToRoundToMelee.put(round, new HashMap<Integer, MeleeAttackLogEntryDTO>());
            brigToRoundToReverseMelee.put(round, new HashMap<Integer, List<MeleeAttackLogEntryDTO>>());
            for (MeleeAttackLogEntryDTO entry : stat.getMeleeAttackLogEntries()) {
                addToTotalMap(thisRoundTotalKills, entry.getAttackerBrigadeId(), entry.getCasualties());
                brigToRoundToMelee.get(round).put(entry.getAttackerBrigadeId(), entry);
                if (!brigToRoundToReverseMelee.get(round).containsKey(entry.getTargetBrigadeId())) {
                    brigToRoundToReverseMelee.get(round).put(entry.getTargetBrigadeId(), new ArrayList<MeleeAttackLogEntryDTO>());
                }
                brigToRoundToReverseMelee.get(round).get(entry.getTargetBrigadeId()).add(entry);
            }

            strategicPointsByRound.put(round, new ArrayList<FieldBattleSectorDTO>());

            for (FieldBattleSectorDTO[] row : stat.getMap().getSectors()) {
                for (FieldBattleSectorDTO sector : row) {
                    if (sector.isStrategicPoint()) {
                        strategicPointsByRound.get(round).add(sector);
                    }
                }
            }

            structuresByRound.put(round, stat.getStructureStatusEntries());

        }

        for (final FieldBattleHalfRoundStatisticsDTO stat : stats) {
            final int round = stat.getHalfRound();
            roundToCorpsesToNation.put(round, new HashMap<FieldBattlePositionDTO, Integer>());
            if (round > -1) {
                roundToCorpsesToNation.get(round).putAll(roundToCorpsesToNation.get(round - 1));
                for (BrigadeDTO brigade : stat.getAllBrigades()) {
                    int directionMultiplier = 1;
                    if ((!BaseStore.getInstance().isNationAllied(brigade.getNationId())
                            && BaseStore.getInstance().getSide() == 1)
                            || (BaseStore.getInstance().isNationAllied(brigade.getNationId())
                            && BaseStore.getInstance().getSide() == 2)) {
                        directionMultiplier = -1;
                    }

                    final BrigadeDTO before = getBrigadeByIdRound(brigade.getBrigadeId(), round - 1);
                    if (before != null) {
                        if (before.getTotalHeadCount() - brigade.getTotalHeadCount() >= 100) {
                            roundToCorpsesToNation.get(round).put(getPositionByBrigade(before), directionMultiplier * brigade.getNationId());
                        }
                    }
                }
            }
        }
    }

    public BrigadeDTO getBrigadeByRoundPosition(final int round, final FieldBattlePositionDTO pos) {
        for (BrigadeDTO brig : roundToStatistics.get(round).getAllBrigades()) {
            if (brig.getFieldBattleX() == pos.getX() && brig.getFieldBattleY() == pos.getY()) {
                return brig;
            }
        }
        return null;
    }

    public List<BrigadeDTO> getAlliedBrigadesByCorpsRound(final int round, final int corps) {
        List<BrigadeDTO> out = new ArrayList<BrigadeDTO>();
        for (BrigadeDTO brig : roundToStatistics.get(round).getAllBrigades()) {
            if (BaseStore.getInstance().isNationAllied(brig.getNationId())) {
                if (brig.getCorpId() == corps) {
                    out.add(brig);
                }
            }
        }
        return out;
    }


    public List<BrigadeDTO> getEnemyBrigadesByCorpsRound(final int round, final int corps) {
        List<BrigadeDTO> out = new ArrayList<BrigadeDTO>();
        for (BrigadeDTO brig : roundToStatistics.get(round).getAllBrigades()) {
            if (!BaseStore.getInstance().isNationAllied(brig.getNationId())) {
                if (brig.getCorpId() == corps) {
                    out.add(brig);
                }
            }
        }
        return out;
    }



    public Map<FieldBattlePositionDTO, Integer> getCorpsesByRound(final int round) {
        if (roundToCorpsesToNation.containsKey(round)) {
            return roundToCorpsesToNation.get(round);
        }
        return new HashMap<FieldBattlePositionDTO, Integer>();
    }


    public FieldBattlePositionDTO getPositionByBrigade(BrigadeDTO brig) {
        final FieldBattlePositionDTO pos = new FieldBattlePositionDTO();
        pos.setX(brig.getFieldBattleX());
        pos.setY(brig.getFieldBattleY());
        return pos;
    }


    public List<StructureStatusDTO> getStructuresByRound(final int round) {
        return structuresByRound.get(round);
    }

    public String getStructureNameByXYRound(final int x, final int y, final int round) {
        for (StructureStatusDTO structure : getStructuresByRound(round)) {
            if (structure.getX() == x
                    && structure.getY() == y) {
                return structure.getType() + " " + structure.getHitPoints();

            }
        }
        return "";
    }


    public int getTotalKillsByBrigadeRound(final int brigadeId, final int round) {
        try {
            return brigToRoundToTotalKills.get(round).get(brigadeId);
        } catch (Exception e) {
            return 0;
        }
    }

    public void addToTotalMap(Map<Integer, Integer> totals, int id, int value) {
        if (totals.containsKey(id)) {
            totals.put(id, totals.get(id) + value);
        } else {
            totals.put(id, value);
        }
    }


    public List<FieldBattleSectorDTO> getStrategicPointsByRound(final int round) {
        if (strategicPointsByRound.containsKey(round)) {
            return strategicPointsByRound.get(round);
        }
        return new ArrayList<FieldBattleSectorDTO>();
    }

    public List<RallyLogEntryDTO> getRallyEntriesByRound(final int round) {
        final List<RallyLogEntryDTO> out = new ArrayList<RallyLogEntryDTO>();
        out.addAll(roundToStatistics.get(round).getRallyLogEntriesSide0());
        out.addAll(roundToStatistics.get(round).getRallyLogEntriesSide1());
        return out;
    }

    public RallyLogEntryDTO getRallyEntryByBrigadeRound(final int brigadeId, final int round) {
        try {
            return brigToRoundToRally.get(round).get(brigadeId);
        } catch (Exception e) {
            return null;
        }
    }

    public BrigadeMovementLogEntryDTO getMovementEntryByBrigadeRound(final int brigadeId, final int round) {
        try {
            return brigToRoundToMovement.get(round).get(brigadeId);
        } catch (Exception e) {
            return null;
        }
    }

    public LongRangeAttackLogEntryDTO getLongRangeByBrigadeRound(final int brigadeId, final int round) {
        try {
            return brigToRoundToLongRange.get(round).get(brigadeId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<LongRangeAttackLogEntryDTO> getLongRangeReverseByBrigadeRound(final int brigadeId, final int round) {
        try {
            return brigToRoundToReverseLongRange.get(round).get(brigadeId);
        } catch (Exception e) {
            return null;
        }
    }

    public MeleeAttackLogEntryDTO getMeleeByBrigadeRound(final int brigadeId, final int round) {
        try {
            return brigToRoundToMelee.get(round).get(brigadeId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<MeleeAttackLogEntryDTO> getMeleeReverseByBrigadeRound(final int brigadeId, final int round) {
        try {
            return brigToRoundToReverseMelee.get(round).get(brigadeId);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Integer> getFleeingBrigadesByRound(final int round) {
        return roundToStatistics.get(round).getBrigadesLeftBattlefieldLogEntries();
    }

    public boolean isBrigadeFleeingByRound(final int brigadeId, final int round) {
        return roundToStatistics.get(round).getBrigadesLeftBattlefieldLogEntries().contains(Integer.valueOf(brigadeId));
    }

    public List<BrigadeMovementLogEntryDTO> getMovementsByRound(final int round) {
        final List<BrigadeMovementLogEntryDTO> out = new ArrayList<BrigadeMovementLogEntryDTO>();
        out.addAll(roundToStatistics.get(round).getBrigadeMovementLogEntriesSide0());
        out.addAll(roundToStatistics.get(round).getBrigadeMovementLogEntriesSide1());
        return out;
    }

    public List<LongRangeAttackLogEntryDTO> getLongRangeByRound(final int round) {
        final List<LongRangeAttackLogEntryDTO> out = new ArrayList<LongRangeAttackLogEntryDTO>();
        out.addAll(roundToStatistics.get(round).getLongRangeAttackLogEntriesSide0());
        out.addAll(roundToStatistics.get(round).getLongRangeAttackLogEntriesSide1());
        return out;
    }

    public List<MeleeAttackLogEntryDTO> getMeleeAttacksByRound(final int round) {
        return roundToStatistics.get(round).getMeleeAttackLogEntries();
    }


    public int getHeadcountByRoundNation(final int round, final int nation) {
        try {
            int headCount = 0;
            for (BrigadeDTO brigade : nationToBrigadesRound.get(nation).get(round)) {
                for (BattalionDTO battalion : brigade.getBattalions()) {
                    headCount += battalion.getHeadcount();
                }
            }
            return headCount;
        } catch (Exception e) {
            return 0;
        }
    }

    public int getInitialBrigadeCountByNation(final int nation) {
        return getBrigadeCountByNationRound(-1, nation);
    }

    public int getInitialHeadCountByNation(final int nation) {
        return getHeadcountByRoundNation(-1, nation);
    }

    public int getBrigadeCountByNationRound(final int round, final int nation) {
        try {
            return nationToBrigadesRound.get(nation).get(round).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public String getNationImgHTML(final int nationId) {
        return "<img src='http://static.eaw1805.com/images/nations/nation-" + nationId + "-36.png' style='height:10px;' title='" + BaseStore.getInstance().getNationById(nationId).getName() + "'>";
    }

    public String stringOfRallyEntry(final RallyLogEntryDTO entry, final int round) {
        try {
            final BrigadeDTO brigade = getBrigadeByIdRound(entry.getBrigadeId(), round);
            if (entry.getMoralStatusOutCome() != null && brigade != null) {
                if ("Normal".equalsIgnoreCase(entry.getMoralStatusOutCome())) {
                    return brigade.getName() + " " + getNationImgHTML(brigade.getNationId()) + " rallied at " + brigade.positionFieldBattleToString() + ".";

                } else if ("Routing".equalsIgnoreCase(entry.getMoralStatusOutCome())) {
                    return brigade.getName() + " " + getNationImgHTML(brigade.getNationId()) + " failed to rally at " + brigade.positionFieldBattleToString() + " and will continue its rout.";

                } else {
                    return brigade.getName() + " " + getNationImgHTML(brigade.getNationId()) + " failed to rally at " + brigade.positionFieldBattleToString() + " and will remain disordered.";
                }
            }
        } catch (Exception e) {/*eat it */}
        return "Unknown rally state";
    }

    public String stringOfRallyEntryShort(final RallyLogEntryDTO entry, final int round) {
        try {
            final BrigadeDTO brigade = getBrigadeByIdRound(entry.getBrigadeId(), round);

            if (entry.getMoralStatusOutCome() != null) {
                if ("Normal".equalsIgnoreCase(entry.getMoralStatusOutCome())) {
                    return "• Rallied at " + brigade.positionFieldBattleToString() + ".";

                } else if ("Routing".equalsIgnoreCase(entry.getMoralStatusOutCome())) {
                    return "• Failed to rally at " + brigade.positionFieldBattleToString() + " and will continue its rout.";

                } else {
                    return "• Failed to rally at " + brigade.positionFieldBattleToString() + " and will remain disordered.";
                }
            }
        } catch (Exception e) {/*eat it */}
        return "• Unknown rally state";
    }

    public String stringOfFleeingEntry(final int brigadeId, final int round) {
        try {
            final BrigadeDTO brigade = getBrigadeByIdRound(brigadeId, round);
            return brigade.getName() + " " + getNationImgHTML(brigade.getNationId()) + " at " + brigade.positionFieldBattleToString() + " has left the battlefield in panic! ";
        } catch (Exception e) {/*eat it */}
        return "Unknown fleeing state";
    }

    public String stringOfFleeingEntryShort(final int brigadeId, final int round) {
        return "• Has left the battlefield in panic! ";
    }

    public String stringOfMovementEntry(final BrigadeMovementLogEntryDTO entry, final int round) {
        try {
            final BrigadeDTO brigade = getBrigadeByIdRound(entry.getBrigadeId(), round);
            return brigade.getName() + " " + getNationImgHTML(brigade.getNationId()) + " moves from " +
                    convertPointsToPositionString(entry.getStartX(), entry.getStartY()) + " to "
                    + convertPointsToPositionString(entry.getEndX(), entry.getEndY());
        } catch (Exception e) {/*eat it */}
        return "Unknown movement state";
    }

    public String convertPointsToPositionString(final int x, final int y) {
        return (x + 1) + "/" + (y + 1);
    }

    public String stringOfMovementEntryShort(final BrigadeMovementLogEntryDTO entry, final int round) {
        return "• Moves from " + convertPointsToPositionString(entry.getStartX(), entry.getStartY())
                + " to " + convertPointsToPositionString(entry.getEndX(), entry.getEndY());
    }

    public String stringOfLongRangeAttack(final LongRangeAttackLogEntryDTO entry, final int round) {
        try {
            final BrigadeDTO attacker = getBrigadeByIdRound(entry.getAttackerBrigadeId(), round);
            final BrigadeDTO target = getBrigadeByIdRound(entry.getTargetBrigadeId(), round);

            if (!entry.isRicochetAttack()) {
                return attacker.getName() + " " + getNationImgHTML(attacker.getNationId()) + " at " + attacker.positionFieldBattleToString() + "  fired at Brigade " + target.getName() + " " + getNationImgHTML(target.getNationId()) + " at " + target.positionFieldBattleToString() + " and killed " + entry.getCasualties() + " men.";

            } else {
                return attacker.getName() + " " + getNationImgHTML(attacker.getNationId()) + " at " + attacker.positionFieldBattleToString() + " artillery fire caused ricochet damage to Brigade " + target.getName() + " " + getNationImgHTML(target.getNationId()) + " at " + target.positionFieldBattleToString() + " and killed " + entry.getCasualties() + " men.";
            }
        } catch (Exception e) {/*eat it */}
        return "Unknown long range state";
    }

    public String stringOfLongRangeReverseAttackShort(final LongRangeAttackLogEntryDTO entry, final int round) {
        try {
            final BrigadeDTO attacker = getBrigadeByIdRound(entry.getAttackerBrigadeId(), round);
            if (attacker != null) {
                if (!entry.isRicochetAttack()) {
                    return "• Fired by " + attacker.getName() + " " + getNationImgHTML(attacker.getNationId()) + " at " + attacker.positionFieldBattleToString() + " and lost " + entry.getCasualties() + " men.";

                } else {
                    return "• Ricochet damaged by artillery " + attacker.getName() + " " + getNationImgHTML(attacker.getNationId()) + " at " + attacker.positionFieldBattleToString() + " and lost " + entry.getCasualties() + " men.";
                }

            } else {
                return "• Fired and lost " + entry.getCasualties() + " men.";
            }
        } catch (Exception e) {/*eat it */}

        return "• Unknown reverse long range state";
    }

    public String stringOfLongRangeAttackShort(final LongRangeAttackLogEntryDTO entry, final int round) {
        try {
            final BrigadeDTO target = getBrigadeByIdRound(entry.getTargetBrigadeId(), round);

            if (!entry.isRicochetAttack()) {
                return "• Fired at " + target.getName() + " " + getNationImgHTML(target.getNationId()) + " at " + target.positionFieldBattleToString() + " and killed " + entry.getCasualties() + " men.";

            } else {
                return "• Artillery fire caused ricochet damage to Brigade " + target.getName() + " " + getNationImgHTML(target.getNationId()) + " at " + target.positionFieldBattleToString() + " and killed " + entry.getCasualties() + " men.";
            }
        } catch (Exception e) {/*eat it */}
        return "• Unknown long range state";
    }

    public String stringOfMeleeAttack(final MeleeAttackLogEntryDTO entry, final int round) {
        try {
            final BrigadeDTO attacker = getBrigadeByIdRound(entry.getAttackerBrigadeId(), round);
            final BrigadeDTO target = getBrigadeByIdRound(entry.getTargetBrigadeId(), round);
            return attacker.getName() + " " + getNationImgHTML(attacker.getNationId()) + " at " + attacker.positionFieldBattleToString() + " engaged in melee with Brigade " + target.getName() + " " + getNationImgHTML(target.getNationId()) + " at " + target.positionFieldBattleToString() + "  and killed " + entry.getCasualties() + " men.";
        } catch (Exception e) {/*eat it */}
        return "Unknown melee state";
    }

    public String stringOfMeleeReverseAttackShort(final MeleeAttackLogEntryDTO entry, final int round) {
        try {
            final BrigadeDTO attacker = getBrigadeByIdRound(entry.getAttackerBrigadeId(), round);
            final BrigadeDTO target = getBrigadeByIdRound(entry.getTargetBrigadeId(), round);
            if (attacker != null) {
                return "• Melee attacked by " + attacker.getName() + " " + getNationImgHTML(attacker.getNationId()) + " at " + attacker.positionFieldBattleToString() + " and lost " + entry.getCasualties() + " men.";
            } else {
                return "• Melee attacked and lost " + entry.getCasualties() + " men.";
            }
        } catch (Exception e) {/*eat it */}
        return "• Unknown melee state";
    }

    public String stringOfMeleeAttackShort(final MeleeAttackLogEntryDTO entry, final int round) {
        try {
            final BrigadeDTO target = getBrigadeByIdRound(entry.getTargetBrigadeId(), round);
            return "• Engaged in melee with " + target.getName() + " " + getNationImgHTML(target.getNationId()) + " at " + target.positionFieldBattleToString() + " and killed " + entry.getCasualties() + " men.";
        } catch (Exception e) {/*eat it */}
        return "• Unknown reverse melee state";
    }

    public BrigadeDTO getBrigadeByIdRound(final int brigId, final int round) {
        try {
        return brigadeToRounds.get(brigId)[round];
        } catch (Exception e) {
            return null;
        }
    }

    public FieldBattleHalfRoundStatisticsDTO getRoundStatistics(int halfRound) {
        return roundToStatistics.get(halfRound);
    }

    public int getRounds() {
        return rounds;
    }

    public String getRoundName(final int halfRound) {
        if (halfRound == -1) {
            return "Placement";
        }
        final String side;
        if ((halfRound + 1) % 2 == 1) {
            side = "A";
        } else {
            side = "B";
        }
        return "Round " + ((halfRound + 2) / 2) + " side " + side;

    }

    public int getMoraleByRoundNation(final int round, final int nationId) {
        int side = BaseStore.getInstance().getSide();
        if (BaseStore.getInstance().isNationAllied(nationId)) {
            if (side == 2) {
                side = 1;
            } else {
                side = 0;
            }

        } else {
            if (side == 2) {
                side = 0;
            } else {
                side = 1;
            }

        }
        if (side == 0) {
            return roundToStatistics.get(round).getMoraleSide0();
        } else {
            return roundToStatistics.get(round).getMoraleSide1();
        }
    }


    @Override
    public void clear() {
        rounds = 0;

        brigadeToRounds.clear();
        nationToBrigadesRound.clear();
        brigToRoundToRally.clear();
        brigToRoundToMovement.clear();
        brigToRoundToLongRange.clear();
        brigToRoundToMelee.clear();
        brigToRoundToReverseLongRange.clear();
        brigToRoundToReverseMelee.clear();
        brigToRoundToTotalKills.clear();
        strategicPointsByRound.clear();
        structuresByRound.clear();

        roundToStatistics.clear();
        roundToCorpsesToNation.clear();
    }
}
