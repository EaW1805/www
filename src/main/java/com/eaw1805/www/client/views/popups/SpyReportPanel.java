package com.eaw1805.www.client.views.popups;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.eaw1805.data.constants.StyleConstants;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.web.army.SpyDTO;
import com.eaw1805.www.shared.stores.DataStore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpyReportPanel extends Composite implements StyleConstants {
    private SpyDTO spy;
    private final Label lblTile;
    private int brigades = 0, nearShipsWar = 0, nearShipsMerch = 0;
    private boolean isSpyTile;

    private final Map<String, Integer> countryToBats = new HashMap<String, Integer>();
    private final Map<String, Integer> countryToWarShips = new HashMap<String, Integer>();
    private final Map<String, Integer> countryToMerchantShips = new HashMap<String, Integer>();

    public SpyReportPanel(final SpyDTO spy, final boolean isSpyTile) {
        final com.google.gwt.i18n.client.NumberFormat numberFormat = com.google.gwt.i18n.client.NumberFormat.getDecimalFormat();
        setSpy(spy);
        setSpyTile(isSpyTile);
        initSpyReportVars();
        final AbsolutePanel absolutePanel = new AbsolutePanel();
        absolutePanel.setStyleName("battalionInfoPanel");
        initWidget(absolutePanel);
        absolutePanel.setSize("232px", "89px");
        this.lblTile = new Label(spy.positionToString());
        this.lblTile.setStyleName("clearFontMiniTitle");
        absolutePanel.add(this.lblTile, 3, 3);

        if (isSpyTile) {
            //collect all countries
            final Set<String> countries = new HashSet<String>();
            for (String country : countryToBats.keySet()) {
                countries.add(country);
            }
            for (String country : countryToMerchantShips.keySet()) {
                countries.add(country);
            }
            int posX = 3;
            final int posY = 26;

            if (!countries.isEmpty()) {

                for (String country : countries) {
                    final List<NationDTO> nations = DataStore.getInstance().getNations();
                    int nationId = 0;
                    for (NationDTO entry : nations) {
                        if (country.equalsIgnoreCase(entry.getName())) {
                            nationId = entry.getNationId();
                            break;
                        }
                    }

                    final Image empireFlag = new Image("http://static.eaw1805.com/images/nations/nation-" + nationId + "-36.png");
                    empireFlag.setSize("", SIZE_15PX);
                    absolutePanel.add(empireFlag, posX, posY);

                    final Image batImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/battalion.png");
                    batImg.setTitle("Battalions");
                    batImg.setSize("", SIZE_15PX);
                    absolutePanel.add(batImg, posX, posY + 16);


                    final Label lblBats;
                    if (countryToBats.containsKey(country)) {
                        lblBats = new Label(numberFormat.format(countryToBats.get(country)));
                    } else {
                        lblBats = new Label("-");
                    }
                    lblBats.setStyleName(CLASS_CLEARFONTSMALL);
                    absolutePanel.add(lblBats, posX + 27, posY + 16);

                    final Image shipsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png");
                    shipsImg.setTitle("Ships");
                    shipsImg.setSize("", SIZE_15PX);
                    absolutePanel.add(shipsImg, posX, posY + 32);


                    final Label merchShips;
                    if (countryToMerchantShips.containsKey(country)) {
                        merchShips = new Label(numberFormat.format(countryToMerchantShips.get(country)));
                    } else {
                        merchShips = new Label("-");
                    }

                    merchShips.setStyleName(CLASS_CLEARFONTSMALL);
                    absolutePanel.add(merchShips, posX + 27, posY + 32);

                    final Image warImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/warship.png");
                    warImg.setTitle("Ships");
                    warImg.setSize("", SIZE_15PX);
                    absolutePanel.add(warImg, posX, posY + 48);


                    final Label warShips;
                    if (countryToWarShips.containsKey(country)) {
                        warShips = new Label(numberFormat.format(countryToWarShips.get(country)));
                    } else {
                        warShips = new Label("-");
                    }

                    warShips.setStyleName(CLASS_CLEARFONTSMALL);
                    absolutePanel.add(warShips, posX + 27, posY + 48);

                    posX += 50;
                }

            } else {
                final Label noReport = new Label("No Reports Available");
                noReport.setStyleName(CLASS_CLEARFONTSMALL);
                absolutePanel.add(noReport, 3, 26);
            }


        } else {
            if (hasBrigadeReports || hasNearShipReports) {
                final Label surrounding = new Label("Surrounding");
                surrounding.setStyleName(CLASS_CLEARFONTSMALL);
                absolutePanel.add(surrounding, 3, 26);

                final Image brigsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/brigade.png");
                brigsImg.setTitle("Brigades");
                brigsImg.setSize("", SIZE_15PX);
                absolutePanel.add(brigsImg, 3, 43);
                final Label brigsCountLabel;
                if (hasBrigadeReports) {
                    brigsCountLabel = new Label(numberFormat.format(brigades));
                } else {
                    brigsCountLabel = new Label("-");
                }
                brigsCountLabel.setStyleName(CLASS_CLEARFONTSMALL);
                absolutePanel.add(brigsCountLabel, 30, 43);

                final Image shipsImg = new Image("http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png");
                shipsImg.setTitle("Ships");
                shipsImg.setSize("", SIZE_15PX);
                absolutePanel.add(shipsImg, 3, 60);
                final Label shipsCountLabel;
                if (hasNearShipReports) {
                    shipsCountLabel = new Label(numberFormat.format(nearShipsMerch + nearShipsWar));
                } else {
                    shipsCountLabel = new Label("-");
                }
                shipsCountLabel.setStyleName(CLASS_CLEARFONTSMALL);
                absolutePanel.add(shipsCountLabel, 30, 60);
            } else {
                final Label noReport = new Label("No Reports Available");
                noReport.setStyleName(CLASS_CLEARFONTSMALL);
                absolutePanel.add(noReport, 3, 26);
            }
        }

    }


    private boolean hasBrigadeReports = false;

    private boolean hasNearShipReports = false;

    private void initSpyReportVars() {
        final String battalionReports = getSpy().getReportBattalions();
        final String brigadeReports = getSpy().getReportBrigades();
        final String shipReports = getSpy().getReportShips();
        String nearShipReport = getSpy().getReportNearbyShips();

        final String battRep[] = battalionReports.split("\\|");
        final String brigRep[] = brigadeReports.split("\\|");
        final String shipRep[] = shipReports.split("\\|");
        if (nearShipReport == null) {
            nearShipReport = "";
        }
        final String nearShipRep[] = nearShipReport.split("\\|");

        if (isSpyTile()) {
            for (int i = 0; i < battRep.length; i++) {
                final String[] items = battRep[i].split(":");
                if (!items[0].equals("")) {
                    if (countryToBats.containsKey(items[0])) {
                        countryToBats.put(items[0], countryToBats.get(items[0]) + Integer.parseInt(items[1]));
                    } else {
                        countryToBats.put(items[0], Integer.parseInt(items[1]));
                    }
                }
            }
            for (int i = 0; i < shipRep.length; i++) {
                final String[] items = shipRep[i].split(":");
                if (!items[0].equals("")) {
                    if (countryToMerchantShips.containsKey(items[0])) {
                        countryToMerchantShips.put(items[0], countryToMerchantShips.get(items[0]) + Integer.parseInt(items[1].substring(2)));
                        if (items.length == 3) {
                            countryToWarShips.put(items[0], countryToWarShips.get(items[0]) + Integer.parseInt(items[2].substring(2)));
                        }
                    } else {
                        countryToMerchantShips.put(items[0], Integer.parseInt(items[1].substring(2)));
                        if (items.length == 3) {
                            countryToWarShips.put(items[0], Integer.parseInt(items[2].substring(2)));
                        } else {
                            countryToWarShips.put(items[0], 0);
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < brigRep.length; i++) {
                final String[] items = brigRep[i].split(":");
                if (!items[0].equals("")) {
                    hasBrigadeReports = true;
                    brigades = brigades + Integer.parseInt(items[1]);
                }
            }
            for (int i = 0; i < nearShipRep.length; i++) {
                final String[] items = nearShipRep[i].split(":");
                if (!items[0].isEmpty()) {
                    hasNearShipReports = true;
                    nearShipsWar = nearShipsWar + Integer.parseInt(items[1]);
                    if (items.length == 3) {
                        nearShipsMerch = nearShipsMerch + Integer.parseInt(items[2]);
                    }
                }
            }
        }
    }

    public final void setSpy(final SpyDTO spy) {
        this.spy = spy;
    }

    public final SpyDTO getSpy() {
        return spy;
    }

    public final void setSpyTile(final boolean isSpyTile) {
        this.isSpyTile = isSpyTile;
    }

    public final boolean isSpyTile() {
        return isSpyTile;
    }
}
