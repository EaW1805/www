package com.eaw1805.www.shared.orders.army;

import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.dto.web.army.ArmyDTO;
import com.eaw1805.data.dto.web.army.BattalionDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.shared.orders.Order;

import java.util.List;
import java.util.Map;

public class MergeBattalionsOrder implements Order, NationConstants {

    /**
     * A list of the merged battalions that no longer exist for the client
     */
    private List<BattalionDTO> mergedBatts;

    private final Map<Integer, ArmyDTO> armiesMap;
    private final int secBatId;
    private int nationId;

    public MergeBattalionsOrder(final Map<Integer, ArmyDTO> armiesMap,
                                final List<BattalionDTO> mergedBatts,
                                final int secBatId) {
        this.mergedBatts = mergedBatts;
        this.armiesMap = armiesMap;
        this.secBatId = secBatId;
    }

    public int execute(final int battId) {
        try {
            final BattalionDTO battalion1, battalion2, newBattalion = new BattalionDTO();
            battalion1 = getBattalionFromId(battId);
            battalion2 = getBattalionFromId(secBatId);
            copyBattalions(battalion1, newBattalion);

            final float exp = (float) (battalion1.getExperience()
                    * battalion1.getHeadcount() + battalion2.getExperience()
                    * battalion2.getHeadcount())
                    / (float) (battalion1.getHeadcount() + battalion2
                    .getHeadcount());

            newBattalion.setExperience(Math.round(exp));

            final int totalHd = battalion1.getHeadcount() + battalion2.getHeadcount();
            if (nationId == NationConstants.NATION_MOROCCO
                    || nationId == NationConstants.NATION_EGYPT
                    || nationId == NationConstants.NATION_OTTOMAN) {

                if (totalHd < 1000) {
                    newBattalion.setHeadcount(totalHd);

                } else {
                    newBattalion.setHeadcount(1000);
                }

            } else {

                if (totalHd < 800) {
                    newBattalion.setHeadcount(totalHd);
                } else {
                    newBattalion.setHeadcount(800);
                }
            }

            newBattalion.setMergedWith(battalion2.getId());

            getBrigadeById(battalion1.getBrigadeId()).getBattalions().remove(battalion1);
            getBrigadeById(battalion2.getBrigadeId()).getBattalions().remove(battalion2);

            getBrigadeById(battalion1.getBrigadeId()).getBattalions().add(newBattalion);

            mergedBatts.add(battalion1);
            mergedBatts.add(battalion2);

            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

    private void copyBattalions(final BattalionDTO battalion1,
                                final BattalionDTO newBattalion) {
        newBattalion.setAttackedByCav(battalion1.isAttackedByCav());
        newBattalion.setBrigadeId(battalion1.getBrigadeId());
        newBattalion.setEmpireArmyType(battalion1.getEmpireArmyType());
        newBattalion.setExperience(battalion1.getExperience());
        newBattalion.setExpIncByComm(battalion1.isExpIncByComm());
        newBattalion.setFleeing(battalion1.isFleeing());
        newBattalion.setHeadcount(battalion1.getHeadcount());
        newBattalion.setId(battalion1.getId());
        newBattalion.setMergedWith(battalion1.getMergedWith());
        newBattalion.setOrder(battalion1.getOrder());
        newBattalion.setParticipated(battalion1.isParticipated());
        newBattalion.setTypeId(battalion1.getTypeId());
    }

    private BattalionDTO getBattalionFromId(final int batId) {
        for (final ArmyDTO army : armiesMap.values()) {
            for (final CorpDTO corp : army.getCorps().values()) {
                for (final BrigadeDTO brig : corp.getBrigades().values()) {
                    nationId = brig.getNationId();
                    for (BattalionDTO batt : brig.getBattalions()) {
                        if (batt.getId() == batId) {
                            return batt;
                        }
                    }
                }
            }
        }
        return null;
    }

    public BrigadeDTO getBrigadeById(final int brigadeId) {
        for (final ArmyDTO army : armiesMap.values()) {
            for (final CorpDTO corp : army.getCorps().values()) {
                if (corp.getBrigades().containsKey(brigadeId)) {
                    return corp.getBrigades().get(brigadeId);
                }
            }
        }
        return null;
    }

}
