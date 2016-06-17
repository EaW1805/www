<%@ page import="com.eaw1805.data.model.Nation" %>
<%@ page import="com.eaw1805.data.model.army.Spy" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%!
    String getSpyCard(Spy spy, HttpServletRequest request, final Map<String, Nation> nameToNation) {

        final StringBuilder out = new StringBuilder();
        out.append("<div style='position: relative; overflow: hidden;' class='pointer'>\n" +
                "    <div style='position: relative; overflow: hidden; width: 366px; height: 90px;' class='spyInfoPanel'><img\n" +
                "            src='http://static.eaw1805.com/images/buttons/icons/spy.png' class='gwt-Image'\n" +
                "            style='position: absolute; left: 3px; top: 3px; height: 82px;'>\n" +
                "\n" +
                "        <div class='clearFontMiniTitle' style='width: 155px; height: 20px; position: absolute; left: 90px; top: 3px;'>\n" +
                "            " + spy.getName() + "\n" +
                "        </div>\n" +
                "        <div class='clearFontSmall' style='position: absolute; left: 90px; top: 20px;'>On sector</div>\n");


        final Map<String, Object> reports = initSpyReportVars(spy);
        final Set<String> onSectorNations = (HashSet) reports.get("onSectorNations");
        if (onSectorNations.isEmpty()) {
            out.append("<div class='clearFontSmall' style='position: absolute; left: 210px; top: 34px;'>No reports</div>");
        } else {
            final Map<String, Integer> onSectorToBrigs = (Map<String, Integer>) reports.get("onSectorToBrigs");
            final Map<String, Integer> onSectorToShips = (Map<String, Integer>) reports.get("onSectorToShips");
            int posX = 0;
            for (String nation : onSectorNations) {
                out.append("   <img src='http://static.eaw1805.com/images/nations/nation-" + nameToNation.get(nation).getId() + "-36.png' class='gwt-Image'\n" +
                        "             style='height: 15px; position: absolute; left: " + (91 + posX) + "px; top: 34px;'><img\n" +
                        "                src='http://static.eaw1805.com/images/buttons/icons/formations/battalion.png' class='gwt-Image'\n" +
                        "                title='Battalions' style='height: 15px; position: absolute; left: " + (90 + posX) + "px; top: 51px;'>\n" +
                        "\n" +
                        "        <div class='clearFontSmall' style='position: absolute; left: " + (117 + posX) + "px; top: 51px;'>");
                if (onSectorToBrigs.containsKey(nation)) {
                    out.append(onSectorToBrigs.get(nation));
                } else {
                    out.append("0");
                }
                out.append("</div>\n" +
                        "        <img src='http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png' class='gwt-Image'\n" +
                        "             title='Ships' style='height: 15px; position: absolute; left: " + (90 + posX) + "px; top: 68px;'>\n" +
                        "\n" +
                        "        <div class='clearFontSmall' style='position: absolute; left: " + (117 + posX) + "px; top: 68px;'>");
                if (onSectorToShips.containsKey(nation)) {
                    out.append(onSectorToShips.get(nation));
                } else {
                    out.append("0");
                }
                out.append("</div>\n");
                posX += 40;
            }
        }


        out.append("        <div class='clearFontSmall' style='position: absolute; left: 208px; top: 20px;'>Surrounding</div>\n" +
                "        <img src='http://static.eaw1805.com/images/buttons/icons/formations/brigade.png' class='gwt-Image'\n" +
                "             title='Brigades' style='height: 15px; position: absolute; left: 210px; top: 34px;'>\n" +
                "\n" +
                "        <div class='clearFontSmall' style='position: absolute; left: 238px; top: 34px;'>" + reports.get("surTotalBrigades") + "</div>\n" +
                "        <img src='http://static.eaw1805.com/images/buttons/icons/formations/merchantShip.png' class='gwt-Image'\n" +
                "             title='Ships' style='height: 15px; position: absolute; left: 210px; top: 53px;'>\n" +
                "\n" +
                "        <div class='clearFontSmall' style='position: absolute; left: 238px; top: 53px;'>" + reports.get("surTotalShips") + "</div>\n" +
                "        <div class='clearFontSmall' title='Spy position.'\n" +
                "             style='width: 47px; height: 15px; position: absolute; left: 315px; top: 3px;'>" + spy.getPosition().toString() + "\n" +
                "        </div>\n" +
                "        <div class='clearFontSmall' title='Movement points.'\n" +
                "             style='position: absolute; left: 315px; top: 20px; width: 47px; height: 15px;'>80 MPs\n" +
                "        </div>\n" +
                "</div>\n" +
                "</div>");
        return out.toString();
    }

    Map<String, Object> initSpyReportVars(final Spy spy) {

        final Map<String, Integer> onSectorToBrigs = new HashMap<String, Integer>();
        final Set<String> onSectorNations = new HashSet<String>();
        final Map<String, Integer> onSectorToShips = new HashMap<String, Integer>();
        int surTotalBrigades = 0;
        int surTotalShips = 0;

        final String battalionReports = spy.getReportBattalions();
        final String brigadeReports = spy.getReportBrigades();
        final String shipReports = spy.getReportShips();
        final String nearShipReport = spy.getReportNearbyShips();

        //retrieve spy sector reports
        if (battalionReports != null && !battalionReports.isEmpty()) {
            final String battRep[] = battalionReports.split("\\|");
            for (final String aBattRep : battRep) {
                final String[] items = aBattRep.split(":");
                if (!items[0].equals("") && !items[1].equals("")) {
                    onSectorToBrigs.put(items[0], Integer.parseInt(items[1]));
                    onSectorNations.add(items[0]);
                }
            }
        }

        if (shipReports != null && !shipReports.isEmpty()) {
            final String shipRep[] = shipReports.split("\\|");
            for (final String aShipRep : shipRep) {
                final String[] items = aShipRep.split(":");
                if (!items[0].equals("") && !items[1].equals("")) {
                    int shipsReported = Integer.parseInt(items[1].substring(2));
                    if (items.length == 3 && !items[2].equals("")) {
                        shipsReported += Integer.parseInt(items[2].substring(2));
                    }
                    onSectorToShips.put(items[0], shipsReported);
                    onSectorNations.add(items[0]);
                }
            }
        }

        //retrieve surrounding sector report.
        if (brigadeReports != null && !brigadeReports.isEmpty()) {
            final String brigRep[] = brigadeReports.split("\\|");
            for (final String aBrigRep : brigRep) {
                final String[] items = aBrigRep.split(":");
                if (!items[0].equals("") && !items[1].equals("")) {
                    surTotalBrigades = surTotalBrigades + Integer.parseInt(items[1]);
                }
            }
        }

        if (nearShipReport != null && !nearShipReport.isEmpty()) {
            final String nearShipRep[] = nearShipReport.split("\\|");
            for (final String aNearShipRep : nearShipRep) {
                final String[] items = aNearShipRep.split(":");
                if (!items[0].equals("") && !items[1].equals("")) {
                    surTotalShips = surTotalShips + Integer.parseInt(items[1]);
                    if (items.length == 3 && !items[2].equals("")) {
                        surTotalShips = surTotalShips + Integer.parseInt(items[2]);
                    }
                }
            }
        }
        final Map<String, Object> out = new HashMap<String, Object>();
        out.put("onSectorToBrigs", onSectorToBrigs);
        out.put("onSectorNations", onSectorNations);
        out.put("onSectorToShips", onSectorToShips);
        out.put("surTotalBrigades", surTotalBrigades);
        out.put("surTotalShips", surTotalShips);

        return out;
    }

    String getSpiesCards(List<Spy> spies, HttpServletRequest request, final Map<String, Nation> nameToNation, final int containerId) {
        Set<Spy> spySet = new HashSet<Spy>(spies);
        return getSpiesCards(spySet, request, nameToNation, containerId);
    }

    String getSpiesCards(Set<Spy> spies, HttpServletRequest request, final Map<String, Nation> nameToNation, final int containerId) {
        final StringBuilder out = new StringBuilder();
        out.append("<div style='position: absolute; overflow: hidden; width: 800px; height: 478px; left: 560px; top: 228px; z-index: 5;'\n" +
                "     class='bigDoubleSelector draggablePopup'>\n" +
                "    <div class='clearFontMiniTitle whiteText' style='color:white; position: absolute; left: 32px; top: 19px;'>Brigades</div>\n" +
                "            <div id='handleDrag' style='color:white; position: absolute; left: 0px; top: 0px; width:737px; height:51px;'></div>" +
                "    <table cellspacing='0' cellpadding='0'\n" +
                "           style='width: 766px; height: 396px; position: absolute; left: 19px; top: 64px;'>\n" +
                "        <tbody>\n" +
                "        <tr>\n" +
                "            <td align='left' style='vertical-align: top;'>\n" +
                "                <div style='overflow: auto; position: relative; width: 768px; height: 396px;' class=''>\n" +
                "                    <div style='position: relative;'>\n" +
                "                        <table cellspacing='0' cellpadding='0' style='width: 752px;'>\n" +
                "                            <tbody>\n" +
                "                            <tr>\n" +
                "                                <td align='left' style='vertical-align: top;'>\n" +
                "                                    <table cellspacing='0' cellpadding='0'>\n" +
                "                                        <tbody>\n");

        //iterate first column brigades
        int count = 1;
        for (Spy spy : spies) {
            count++;
            if (count % 2 == 0) {
                out.append("<tr><td align='left' style='vertical-align: top;'>").append(getSpyCard(spy, request, nameToNation)).append("</td></tr>");
            }
        }
        out.append("</tbody>\n" +
                "                                    </table>\n" +
                "                                </td>\n" +
                "                                <td align='left' style='vertical-align: top;'>\n" +
                "                                    <table cellspacing='0' cellpadding='0'>\n" +
                "                                        <tbody>");
        //iterate second column brigades
        count = 0;
        for (Spy spy : spies) {
            count++;
            if (count % 2 == 0) {
                out.append("<tr>\n" +
                        "                                            <td align='left' style='vertical-align: top;'>")
                        .append(getSpyCard(spy, request, nameToNation)).append("</td></tr>");
            }
        }


        out.append("</tbody>\n" +
                "                                    </table>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                            </tbody>\n" +
                "                        </table>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        </tbody>\n" +
                "    </table>\n" +
                "    <img src='http://static.eaw1805.com/images/buttons/ButAcceptOff.png' class='draggableCloser pointer' id='widget-1197'\n" +
                "         title='Close popup' style='position: absolute; left: 750px; top: 8px; width: 36px; height: 36px;'></div>");
        return out.toString();
    }


%>

