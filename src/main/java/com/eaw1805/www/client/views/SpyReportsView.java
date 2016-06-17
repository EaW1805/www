package com.eaw1805.www.client.views;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.NationConstants;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.DualStateImage;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.units.AlliedUnitsStore;
import com.eaw1805.www.shared.stores.units.SpyStore;

import java.util.List;

public class SpyReportsView
        extends DraggablePanel
        implements StyleConstants {

    private final HorizontalPanel nationSelectPanel;
    private final DualStateImage[] nationImgs = new DualStateImage[17];
    private final AbsolutePanel relationsPanel;
    private final AbsolutePanel relationsAbsPanel;
    private Label lblNoNationSelected;
    private final Label lblOrNoInformation;

    public SpyReportsView(final NationDTO selectedNation) {

        this.setStyleName("relationsPanel");
        setSize("810px", "656px");
        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        final SpyReportsView self = this;

        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(self);
                imgX.deselect();
                if (TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 10
                        && TutorialStore.getInstance().getTutorialStep() == 6) {
                    TutorialStore.nextStep(false);
                }
            }
        }).addToElement(imgX.getElement()).register();

        this.add(imgX, 764, 7);
        imgX.setStyleName(CLASS_POINTER);


        this.nationSelectPanel = new HorizontalPanel();
        add(this.nationSelectPanel, 10, 10);
        this.nationSelectPanel.setSize("684px", "30px");


        this.relationsPanel = new AbsolutePanel();
        this.relationsPanel.setSize("552px", "593px");

        this.relationsAbsPanel = new AbsolutePanel();
        this.relationsPanel.add(this.relationsAbsPanel, 0, 0);
        this.relationsAbsPanel.setSize("551px", "595px");
        add(relationsPanel, 245, 56);

        this.lblNoNationSelected = new Label("No nation selected");
        this.lblNoNationSelected.setStyleName(CLASS_CLEARFONT_LARGE);
        this.lblNoNationSelected.setStyleName(CLASS_WHITETEXT, true);

        this.relationsAbsPanel.add(this.lblNoNationSelected, 164, 145);

        this.lblOrNoInformation = new Label("or no information is available!");
        this.lblOrNoInformation.setStyleName(CLASS_CLEARFONT_LARGE);
        this.lblOrNoInformation.setStyleName(CLASS_WHITETEXT, true);
        this.relationsAbsPanel.add(this.lblOrNoInformation, 107, 184);

        initNationSelectionImages(selectedNation);
        initRelationVars(selectedNation.getNationId());
    }

    private void initNationSelectionImages(final NationDTO selectedNation) {
        final List<NationDTO> nations = DataStore.getInstance().getNations();
        int index = 0;
        for (final NationDTO nation : nations) {
            if (nation.getNationId() != -1) {
                this.nationImgs[index] = new DualStateImage("http://static.eaw1805.com/images/nations/nation-" + nation.getNationId() + "-36.png");
                this.nationImgs[index].setId(nation.getNationId());
                this.nationImgs[index].setTitle(nation.getName());
                if (nation.getNationId() == selectedNation.getNationId()) {
                    this.nationImgs[index].setStyleName("prodSitePanelSel");
                } else {
                    this.nationImgs[index].setStyleName("prodSitePanel");
                }
                this.nationSelectPanel.add(this.nationImgs[index]);
                this.nationImgs[index].setStyleName(CLASS_POINTER, true);
                this.nationImgs[index].setSize("36px", "26px");
                final int i = index;
                final int nationId = nation.getNationId();
                (new DelEventHandlerAbstract() {
                    @Override
                    public void execute(final MouseEvent event) {
                        for (int j = 0; j < nationSelectPanel.getWidgetCount(); j++) {
                            if (j != i) {
                                nationImgs[j].setStyleName("prodSitePanel");
                                nationImgs[j].setStyleName(CLASS_POINTER, true);
                                nationImgs[j].deselect();
                            }
                        }
                        nationImgs[i].setStyleName("prodSitePanelSel");
                        nationImgs[i].setStyleName(CLASS_POINTER, true);

                        initRelationVars(nationId);
                    }
                }).addToElement(nationImgs[index].getElement()).register();

                if (nation.getNationId() == NationConstants.NATION_GREATBRITAIN
                        && TutorialStore.getInstance().isTutorialMode()
                        && TutorialStore.getInstance().getMonth() == 10
                        && TutorialStore.getInstance().getTutorialStep() == 6) {
                    TutorialStore.highLightButton(nationImgs[index]);
                }
                index++;
            }
        }
    }

    private void initRelationVars(final int nationId) {
        relationsAbsPanel.clear();
        boolean hasRights = false;
        //check if your spies have rights for this nation
        for (SpyDTO curSpy : SpyStore.getInstance().getSpyList()) {
            if (curSpy.getReportRelations() == nationId || GameStore.getInstance().getNationId() == nationId) {
                hasRights = true;
                break;
            }
        }

        //if no check if allied spies have rights for this nation
        if (!hasRights) {
            for (SpyDTO curSpy : AlliedUnitsStore.getInstance().getSpies()) {
                if (curSpy.getReportRelations() == nationId || GameStore.getInstance().getNationId() == nationId) {
                    hasRights = true;
                    break;
                }
            }
        }

        if (DataStore.getInstance().isNationDead(nationId)) {
            lblNoNationSelected = new Label(DataStore.getInstance().getNationNameByNationId(nationId) + " is dead...");
            lblNoNationSelected.setStyleName(CLASS_CLEARFONT_LARGE);
            lblNoNationSelected.setStyleName(CLASS_WHITETEXT, true);
            relationsAbsPanel.add(lblNoNationSelected, 100, 145);

        } else if (hasRights) {
            final SpyRelationsView spyRelationsView = new SpyRelationsView(hasRights, nationId);
            relationsAbsPanel.add(spyRelationsView);

        } else {
            lblNoNationSelected.setText("No nation selected");
            lblNoNationSelected.setStyleName(CLASS_CLEARFONT_LARGE);
            lblNoNationSelected.setStyleName(CLASS_WHITETEXT, true);
            relationsAbsPanel.add(lblNoNationSelected, 100, 145);

            lblOrNoInformation.setText("or no information is available!");
            lblOrNoInformation.setStyleName(CLASS_CLEARFONT_LARGE);
            lblOrNoInformation.setStyleName(CLASS_WHITETEXT, true);
            relationsAbsPanel.add(lblOrNoInformation, 100, 184);
        }
    }


}
