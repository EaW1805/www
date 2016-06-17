package com.eaw1805.www.client.views;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.dto.web.RelationDTO;
import com.eaw1805.www.client.views.extras.RelationRow;
import com.eaw1805.www.shared.stores.RelationsStore;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SpyRelationsView extends VerticalPanel {

    public SpyRelationsView(final boolean hasRights, final int nationId) {
        if (hasRights) {
            initRelations(RelationsStore.getInstance().getNationRelations(nationId, this));

        } else {
            initRelations(null);
        }
        setSize("551px", "595px");
    }

    public final void initRelations(final List<RelationDTO> relations) {
        this.clear();
        if (relations == null) {
            for (int nationId = 1; nationId <= 17; nationId++) {
                final RelationRow relRow = new RelationRow(0, 0, 0);
                this.add(relRow);
            }

        } else {
            Map<Integer, RelationDTO> sortedRelations = new TreeMap<Integer, RelationDTO>();
            for (RelationDTO relation : relations) {
                sortedRelations.put(relation.getTargetNationId(), relation);
            }

            final int nationId = relations.get(0).getNationId();
            for (RelationDTO relation : sortedRelations.values()) {
                if (nationId == 1 && relation.getTargetNationId() == 2) {
                    this.add(new RelationRow(0, 0, 0));
                }
                final RelationRow relRow = new RelationRow(relation.getNationId(), relation.getTargetNationId(), relation.getRelation());
                this.add(relRow);
                if (nationId == relation.getTargetNationId() + 1) {
                    this.add(new RelationRow(0, 0, 0));
                }
            }
        }

    }

}
