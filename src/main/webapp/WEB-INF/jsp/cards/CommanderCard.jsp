<%@ page import="com.eaw1805.data.model.army.Commander" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Set" %>
<%!
    String getCommanderCard(Commander commander, HttpServletRequest request) {
        final StringBuilder out = new StringBuilder();
        out.append("<div style='position: relative; overflow: hidden; width: 366px; height: 90px;'>\n" +
                "    <div style='position: relative; overflow: hidden; width: 363px; height: 87px;' class='commanderInfoPanel'><img\n" +
                "            src='http://static.eaw1805.com/img/commanders/s" + commander.getPosition().getGame().getScenarioId() +
                "/" + commander.getNation().getId() + "/" + commander.getIntId() + ".jpg' class='gwt-Image'\n" +
                "            style='height: 82px; position: absolute; left: 3px; top: 3px;'>\n" +
                "\n" +
                "        <div class='clearFontMiniTitle' style='width: 190px; height: 20px; position: absolute; left: 90px; top: 3px;'>\n" +
                "            " + commander.getName() + "\n" +
                "        </div>\n" +
                "        <div class='clearFontSmall' style='width: 190px; height: 20px; position: absolute; left: 90px; top: 25px;'>\n" +
                "            " + commander.getRank().getName() +
                " (" + commander.getStrc() + "-" + commander.getComc() + ")\n" +
                "        </div>\n");
        if (commander.getDead()) {
            out.append("        <div class='clearFontSmall' style='position: absolute; left: 90px; top: 39px;'>Commander is dead</div>\n");
        } else if (commander.getArmy() > 0) {
            if (commander.getInTransit()) {
                out.append("        <div class='clearFontSmall' style='position: absolute; left: 90px; top: 39px;'>In transit to lead Army</div>\n");
            } else {
                out.append("        <div class='clearFontSmall' style='position: absolute; left: 90px; top: 39px;'>leads Army</div>\n");
            }
        } else if (commander.getCorp() > 0) {
            if (commander.getInTransit()) {
                out.append("        <div class='clearFontSmall' style='position: absolute; left: 90px; top: 39px;'>In transit to lead Corps</div>\n");
            } else {
                out.append("        <div class='clearFontSmall' style='position: absolute; left: 90px; top: 39px;'>leads Corps</div>\n");
            }
        } else if (commander.getPool()) {
            if (commander.getInTransit()) {
                out.append("        <div class='clearFontSmall' style='position: absolute; left: 90px; top: 39px;'>Commander is in transit to the pool</div>\n");
            } else {
                out.append("        <div class='clearFontSmall' style='position: absolute; left: 90px; top: 39px;'>Commander is in the pool</div>\n");
            }
        } else {
            out.append("        <div class='clearFontSmall' style='position: absolute; left: 90px; top: 39px;'>Commander is unassigned</div>\n");
        }


        if (commander.getSick() > 0) {
            out.append("<img\n" +
                    "                src='http://static.eaw1805.com/img/commanders/redcross.png' class='gwt-Image'\n" +
                    "                title='Commander is sick'\n" +
                    "                style='width: 20px; height: 20px; position: absolute; left: 109px; top: 63px;'\n" +
                    "                aria-hidden='true'>");
        }
        if (commander.getSupreme()) {
            out.append("<img src='http://static.eaw1805.com/img/commanders/supreme.png' class='gwt-Image'\n" +
                    "                                        title='Supreme Commander'\n" +
                    "                                        style='width: 20px; height: 20px; position: absolute; left: 90px; top: 63px; display: none;'\n" +
                    "                                        aria-hidden='true'>\n");
        }

        out.append("\n" +
                "        <div class='clearFontSmall' title='Commanders position.'\n" +
                "             style='position: absolute; left: 315px; top: 3px; width: 47px; height: 15px;'>" + commander.getPosition().toString() + "\n" +
                "        </div>\n" +
                "        <div class='clearFontSmall' title='Movement points'\n" +
                "             style='position: absolute; left: 315px; top: 20px; width: 47px; height: 15px;'>" + commander.getMps() + " MPs\n" +
                "        </div>\n" +
                "</div>\n" +
                "</div>");
        return out.toString();
    }

    String getCommandersCards(List<Commander> commanders, HttpServletRequest request, final int containerId) {
        Set<Commander> brigadeSet = new HashSet<Commander>(commanders);
        return getCommandersCards(brigadeSet, request, containerId);
    }

    String getCommandersCards(Set<Commander> commanders, HttpServletRequest request, final int containerId) {
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
        for (Commander commander : commanders) {
            count++;
            if (count % 2 == 0) {
                out.append("<tr><td align='left' style='vertical-align: top;'>").append(getCommanderCard(commander, request)).append("</td></tr>");
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
        for (Commander commander : commanders) {
            count++;
            if (count % 2 == 0) {
                out.append("<tr>\n" +
                        "                                            <td align='left' style='vertical-align: top;'>")
                        .append(getCommanderCard(commander, request)).append("</td></tr>");
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

