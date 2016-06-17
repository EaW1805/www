package com.eaw1805.www.client.views.layout;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.eaw1805.data.HibernateUtil;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.extras.RegionImage;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.MapStore;

public class RegionSelectionView
        extends AbsolutePanel
        implements StyleConstants {

    private transient final RegionImage[] rgImgs = new RegionImage[4];

    public RegionSelectionView() {
        setSize("116px", SIZE_29PX);

        rgImgs[0] = new RegionImage("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButEuropeOn.png");
        rgImgs[0].setTitle("Go to Europe");
        rgImgs[0].setSelected(true);
        rgImgs[0].setStyleName(CLASS_POINTER);
        rgImgs[0].setSize(SIZE_29PX, SIZE_29PX);
        rgImgs[0].setRegionId(1);
        addImageActions(rgImgs[0]);
        add(rgImgs[0], 0, 0);

        rgImgs[1] = new RegionImage("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAfricaOff.png");
        rgImgs[1].setTitle("Go to Africa");
        rgImgs[1].setStyleName(CLASS_POINTER);
        rgImgs[1].setSize(SIZE_29PX, SIZE_29PX);
        rgImgs[1].setRegionId(4);
        addImageActions(rgImgs[1]);


        rgImgs[2] = new RegionImage("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButAmericaOff.png");
        rgImgs[2].setTitle("Go to The Caribbean");
        rgImgs[2].setStyleName(CLASS_POINTER);
        rgImgs[2].setSize(SIZE_29PX, SIZE_29PX);
        rgImgs[2].setRegionId(2);
        addImageActions(rgImgs[2]);


        rgImgs[3] = new RegionImage("http://static.eaw1805.com/images/layout/buttons/minimapMenu/ButIndiaOff.png");
        rgImgs[3].setTitle("Go to India");
        rgImgs[3].setStyleName(CLASS_POINTER);
        rgImgs[3].setSize(SIZE_29PX, SIZE_29PX);
        rgImgs[3].setRegionId(3);
        addImageActions(rgImgs[3]);

        if (GameStore.getInstance().getScenarioId() == HibernateUtil.DB_FREE) {
            add(rgImgs[2], 29, 0);

        } else {
            add(rgImgs[1], 29, 0);
            add(rgImgs[2], 58, 0);
            add(rgImgs[3], 87, 0);
        }
    }

    public final void addImageActions(final RegionImage regionImage) {
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(MouseEvent event) {
                if (!regionImage.isDisabled() &&
                        (!TutorialStore.getInstance().isTutorialMode()
                        || TutorialStore.canChangeRegion)) {
                    for (int region = 0; region < 4; region++) {
                        if (!regionImage.equals(rgImgs[region])) {
                            rgImgs[region].deselect();
                        }
                    }
                    MapStore.getInstance().selectRegion(regionImage.getRegionId());
                    GameStore.getInstance().getSectorMenu().setSelectedSectorInfo(RegionStore.getInstance().getSelectedSector(regionImage.getRegionId()));
                }
            }
        }).addToElement(regionImage.getElement()).register();
    }

    public void setDisabledRegionButtons(int... regions) {
        for (int i=0;i<4;i++) {
            rgImgs[i].setDisabled(false);
        }
        for (int region : regions) {
            rgImgs[region - 1].setDisabled(true);
        }
    }

}
