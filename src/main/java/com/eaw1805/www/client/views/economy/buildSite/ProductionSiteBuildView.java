package com.eaw1805.www.client.views.economy.buildSite;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.ProductionSiteConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.common.ProductionSiteDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.views.tutorial.TutorialStore;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;

import java.util.List;

public class ProductionSiteBuildView
        extends DraggablePanel
        implements ProductionSiteConstants {

    private final AbsolutePanel prodSitesPanel;
    private final HorizontalPanel[] rows = new HorizontalPanel[4];
    private final SectorDTO sector;
    private final DataStore manInstance = DataStore.getInstance();

    public ProductionSiteBuildView(final SectorDTO tgSector) {
        sector = tgSector;
        final VerticalPanel basePanel = new VerticalPanel();
        basePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        basePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        basePanel.setStyleName("blackPanel");
        basePanel.setSize("100%", "100%");
        add(basePanel);

        final AbsolutePanel absolutePanel = new AbsolutePanel();
        absolutePanel.setStyleName("buildPrSitesPanel");
        absolutePanel.setSize("633px", "595px");
        basePanel.add(absolutePanel);

        final Label title = new Label(tgSector.positionToString() + " - Build Production Site");
        title.setStyleName("clearFont-large whiteText");
        absolutePanel.add(title, 20, 10);

        prodSitesPanel = new AbsolutePanel();
        prodSitesPanel.setSize("619px", "551px");
        absolutePanel.add(prodSitesPanel, 10, 55);
        basePanel.setCellVerticalAlignment(prodSitesPanel, HasVerticalAlignment.ALIGN_MIDDLE);
        basePanel.setCellHorizontalAlignment(prodSitesPanel, HasHorizontalAlignment.ALIGN_CENTER);
        attachProdSitesPanel();

        final ProductionSiteBuildView myself = this;
        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        imgX.setStyleName("pointer");
        imgX.setTitle("Close");
        (new DelEventHandlerAbstract() {
            @Override
            public void execute(final MouseEvent event) {
                GameStore.getInstance().getLayoutView().getOptionsMenu().getRelImage().deselect();
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(myself);
                imgX.deselect();
            }
        }).addToElement(imgX.getElement()).register();

        absolutePanel.add(imgX, 585, 10);
        imgX.setSize("36px", "36px");
    }

    private void attachProdSitesPanel() {
        final List<ProductionSiteDTO> prodSitesList = manInstance.getProdSites();
        final List<ProductionSiteDTO> aVailprodSitesList = manInstance.getAvailProdSites(String.valueOf(sector.getTerrain().getCode()), sector.getNatResId());
        for (final ProductionSiteDTO prodSite : prodSitesList) {
            boolean canBeBuilt = false;
            //if it does not already contain a production site
            //or it contains one of the barracks
            //..otherwise we only want the demolish button
            if (sector.getProductionSiteId() == -1
                    || sector.getProductionSiteId() == PS_BARRACKS
                    || sector.getProductionSiteId() == PS_BARRACKS_FS
                    || sector.getProductionSiteId() == PS_BARRACKS_FM
                    || sector.getProductionSiteId() == PS_BARRACKS_FL
                    || sector.getProductionSiteId() == PS_BARRACKS_FH) {
                for (final ProductionSiteDTO avProdSite : aVailprodSitesList) {
                    if (avProdSite.getId() == prodSite.getId()) {
                        // Before adding it to the available sites check about population issues
                        final int nextTurnPop = RegionStore.getInstance().getSectorNextTurnPopulation(sector);
                        if (nextTurnPop >= avProdSite.getMinPopDensity()
                                && nextTurnPop <= avProdSite.getMaxPopDensity()) {

                            // check if there is a barracks on the coordinate
                            switch (sector.getProductionSiteId()) {

                                case PS_BARRACKS:
                                    if (avProdSite.getId() == PS_BARRACKS_FS) {
                                        canBeBuilt = true;
                                    }
                                    break;

                                case PS_BARRACKS_FS:
                                    if (avProdSite.getId() == PS_BARRACKS_FM) {
                                        canBeBuilt = true;
                                    }
                                    break;

                                case PS_BARRACKS_FM:
                                    if (avProdSite.getId() == PS_BARRACKS_FL) {
                                        canBeBuilt = true;
                                    }
                                    break;

                                case PS_BARRACKS_FL:
                                    if (avProdSite.getId() == PS_BARRACKS_FH) {
                                        canBeBuilt = true;
                                    }
                                    break;

                                default:
                                    if (avProdSite.getId() == PS_BARRACKS_FS
                                            || avProdSite.getId() == PS_BARRACKS_FM
                                            || avProdSite.getId() == PS_BARRACKS_FL
                                            || avProdSite.getId() == PS_BARRACKS_FH) {
                                        // do nothing
                                    } else if (sector.getRegionId() != RegionConstants.EUROPE
                                            && avProdSite.getId() == PS_MINT) {
                                        //do nothing
                                    } else {
                                        canBeBuilt = true;
                                    }
                                    break;
                            }
                        }
                        break;
                    }
                }
            }
            for (int row = 0; row < 4; row++) {
                if (rows[row] == null) {
                    rows[row] = new HorizontalPanel();
                    prodSitesPanel.add(rows[row], 0, row * 174);
                }
                DOM.setElementAttribute(rows[row].getElement(), "cellpadding", "3");

                if (rows[row].getWidgetCount() < 5) {
                    final PrSiteButton psip = new PrSiteButton(prodSite, sector, canBeBuilt);
                    if (psip.isEligible()) {
                        rows[row].add(psip);
                    }
                    if (TutorialStore.getInstance().isTutorialMode()
                            && TutorialStore.getInstance().getAnimateProdSite() > 0
                            && prodSite.getId() == TutorialStore.getInstance().getAnimateProdSite()) {
                        psip.animate();
                    }
                    break;
                }
            }

        }

        if (sector.getProductionSiteId() != -1) {
            for (int row = 0; row < 4; row++) {
                if (rows[row] == null) {
                    rows[row] = new HorizontalPanel();
                }
                DOM.setElementAttribute(rows[row].getElement(), "cellpadding", "3");
                if (rows[row].getWidgetCount() < 5) {
                    ProductionSiteDTO prodSite = new ProductionSiteDTO();
                    prodSite.setName("Demolition");
                    prodSite.setDescription("A demolition gang is not a site per se. It is used to destroy already existing sites allowing the built of a new one.");
                    prodSite.setCost(5000);
                    final PrSiteButton psip = new PrSiteButton(prodSite, sector, true);
                    if (psip.isEligible()) {
                        rows[row].add(psip);
                    }
                    break;
                }
            }
        }
    }

}
