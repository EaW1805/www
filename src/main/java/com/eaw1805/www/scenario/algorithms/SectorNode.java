package com.eaw1805.www.scenario.algorithms;


import com.eaw1805.data.constants.TerrainConstants;
import com.eaw1805.data.dto.common.SectorDTO;

import java.util.List;

public class SectorNode implements AStarNode<SectorDTO> {

    private SectorDTO sector;
    private AStarNode parent;

    public SectorNode(final SectorDTO inSector, final AStarNode inParent) {
        sector = inSector;
        parent = inParent;
    }

    /**
     * A unique identifier for this node.
     * We use the position to string value.
     *
     * @return A uid.
     */
    @Override
    public String getId() {
        return sector.positionToString();
    }

    @Override
    public SectorDTO getObject() {
        return sector;
    }

    @Override
    public AStarNode<SectorDTO> getParent() {
        return parent;
    }

    @Override
    public double getWeight() {
        if (sector.getTerrain() == null) {
            return 0;
        }
        if (sector.getTerrain().getId() == TerrainConstants.TERRAIN_O
                || sector.getTerrain().getId() == TerrainConstants.TERRAIN_I) {
            return 100;
        }
        return sector.getTerrain().getActualMPs();
    }

    @Override
    public double getTotalWeight() {
        AStarNode node = this;
        double out = node.getWeight();
        //if you have a parent, and your fathers has a parent.
        //that means your parent isn't the supply source node.
        while (node.getParent() != null
                && node.getParent().getParent() != null) {
            node = node.getParent();
            out += node.getWeight();
        }
        return out;
    }

    @Override
    public int getIndexX() {
        return sector.getX();
    }

    @Override
    public int getIndexY() {
        return sector.getY();
    }

    @Override
    public AStarNode<SectorDTO> parentNode(final AStarNode<SectorDTO> node) {
        return new SectorNode(sector, node);
    }




}
