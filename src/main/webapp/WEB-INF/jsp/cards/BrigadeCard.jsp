<%@ page import="com.eaw1805.data.model.army.Battalion" %>
<%@ page import="com.eaw1805.data.model.army.Brigade" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Set" %>
<%!
    String getBrigadeCard(Brigade brigade, HttpServletRequest request) {
        brigade.updateMP();
        final StringBuilder out = new StringBuilder();
        out.append("<div style='position: relative; overflow: hidden; width: 366px; height: 90px;'>\n" +
                "    <div style='position: relative; overflow: hidden; width: 363px; height: 87px;'\n" +
                "         class='brigadeInfoPanel clickArmyPanel'><img src='http://static.eaw1805.com/images/infopanels/selected.png'\n" +
                "                                                      class='gwt-Image'\n" +
                "                                                      style='display: none; position: absolute; left: 0px; top: 0px; width: 363px; height: 87px;'\n" +
                "                                                      aria-hidden='true'>\n" +
                "\n" +
                "        <div class='clearFontMiniTitle' style='position: absolute; left: 8px; top: 3px;'>" + brigade.getName() + "</div>\n" +
                "        <div class='clearFontSmall' title='Movement points' style='position: absolute; left: 315px; top: 20px;'>" + brigade.getMps() + " MPs\n" +
                "        </div>\n" +
                "        <div class='clearFontSmall' title='Brigades position'\n" +
                "             style='position: absolute; left: 315px; top: 3px; width: 47px; height: 15px;'>" + brigade.getPosition().toString() + "\n" +
                "        </div>\n");
        int posX = 8;
        int posY = 23;
        for (Battalion battalion : brigade.getBattalions()) {
            out.append("<img class='gwt-Image pointer' src='http://static.eaw1805.com/images/armies/" + brigade.getNation().getId() + "/" + battalion.getType().getIntId() + ".jpg' title='" + battalion.getType().getName() + "'\n" +
                    "             style='width: 49px; height: 49px; position: absolute; left: " + posX + "px; top: " + posY + "px;'>\n");
            out.append("<div class='clearFontSmall' style='position: absolute; left: " + posX + "px; top: 72px;'>" + battalion.getExperience() + " - " + battalion.getHeadcount() + "</div>\n");
            posX += 49;
        }
        out.append("    </div>\n" +
                "</div>");
        return out.toString();
    }

    String getBrigadesCards(List<Brigade> brigades, HttpServletRequest request, final int containerId) {
        Set<Brigade> brigadeSet = new HashSet<Brigade>(brigades);
        return getBrigadesCards(brigadeSet, request, containerId);
    }

    String getBrigadesCards(Set<Brigade> brigades, HttpServletRequest request, final int containerId) {
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
        for (Brigade brigade : brigades) {
            count++;
            if (count % 2 == 0) {
                out.append("<tr><td align='left' style='vertical-align: top;'>").append(getBrigadeCard(brigade, request)).append("</td></tr>");
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
        for (Brigade brigade : brigades) {
            count++;
            if (count % 2 == 0) {
                out.append("<tr>\n" +
                        "                                            <td align='left' style='vertical-align: top;'>")
                        .append(getBrigadeCard(brigade, request)).append("</td></tr>");
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
