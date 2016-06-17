<%@ page import="com.eaw1805.data.dto.web.fleet.ShipDTO" %>
<%@ page import="java.util.List" %>
<%!
    String getShipCard(final ShipDTO ship, final HttpServletRequest request) {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("<div style='position: relative; overflow: hidden; width: 366px; height: 90px;'>\n");
        buffer.append("    <div style='position: relative; overflow: hidden; width: 363px; height: 87px;' class='ShipInfoPanel clickArmyPanel'>\n");
        buffer.append("        <img src='http://static.eaw1805.com/images/ships/");
        buffer.append(ship.getNationId());
        buffer.append("/");
        buffer.append(ship.getType().getIntId());
        buffer.append(".png' class='gwt-Image' title='");
        buffer.append(ship.getType().getName());
        buffer.append("'\n");
        buffer.append("             style='position: absolute; left: 3px; top: 3px; height: 82px;'>\n");
        buffer.append("\n");
        buffer.append("        <div class='clearFontMiniTitle' style='position: absolute; left: 90px; top: 3px;'>");
        buffer.append(ship.getName());
        buffer.append("</div>\n");
        buffer.append("        <div class='clearFontSmall' title='Ships position'\n");
        buffer.append("             style='position: absolute; left: 315px; top: 3px; width: 49px; height: 18px;'>\n");
        buffer.append("        </div>\n");
        buffer.append("        </div>\n");
        buffer.append("        <div class='clearFontSmall' style='position: absolute; left: 90px; top: 20px;'>");
        buffer.append(ship.getType().getName());
        buffer.append("</div>\n");
        buffer.append("        <div class='clearFontSmall' style='position: absolute; left: 90px; top: 34px; width: 207px; height: 18px;'>");
        buffer.append(ship.getMarines());
        buffer.append("\n");

        switch (ship.getExp()) {
            case 1:
                buffer.append("           Veteran");
                break;
            case 2:
                buffer.append("           Elite");
                break;
            case 0:
            default:
                buffer.append("           Regular");
                break;
        }

        buffer.append(" marines\n");
        buffer.append("        </div>\n");
        buffer.append("        <div class='clearFontSmall' title='Condition' style='position: absolute; left: 90px; top: 48px;'>");
        buffer.append(ship.getCondition());
        buffer.append("%</div>\n");
        buffer.append("    </div>\n");
        buffer.append("    <div style='position: absolute; overflow: hidden; width: 150px; height: 35px; left: 170px; top: 30px;'></div>");
        return buffer.toString();
    }

    String getShipsCards(final List<ShipDTO> ships, final HttpServletRequest request) {
        final StringBuilder output = new StringBuilder();

//        output.append("<div style='width:760px;height:400px;overflow:auto;'><table>");
        int counter = 0;

        output.append("<div style='left: 201px; top: 101px; z-index: 3; visibility: visible; position: absolute; overflow: visible;'\n");
        output.append("     class='draggablePopup'\n");
        output.append("    <div class='popupContent'>\n");
        output.append("        <div style='position: relative; overflow: hidden; width: 801px; height: 478px;' class='bigDoubleSelector'>\n");
        output.append("            <div class='clearFontMiniTitle whiteText' style='color:white;position: absolute; left: 32px; top: 19px;'>Navy</div>\n");
        output.append("            <div id='handleDrag' style='color:white; position: absolute; left: 0px; top: 0px; width:737px; height:51px;'></div>");
        output.append("<img src='http://static.eaw1805.com/images/buttons/ButAcceptOff.png' class='draggableCloser pointer' style='position: absolute; left: 738px; top: 10px; width: 36px; height: 36px;'>");
        output.append("            <table cellspacing='0' cellpadding='0'\n");
        output.append("                   style='width: 760px; height: 396px; position: absolute; left: 19px; top: 64px;'>\n");
        output.append("                <tbody>\n");
        output.append("                <tr>\n");
        output.append("                    <td align='left' style='vertical-align: top;'>\n");
        output.append("                        <div style='overflow: auto; position: relative; width: 754px; height: 396px;'\n");
        output.append("                             >\n");
        output.append("                            <div style='position: relative;'>\n");
        output.append("                                <table cellspacing='0' cellpadding='0' style='width: 752px;'>\n");
        output.append("                                    <tbody>\n");
        output.append("                                    <tr>\n");
        output.append("                                        <td align='left' style='vertical-align: top;'>\n");
        output.append("                                            <table cellspacing='0' cellpadding='0'>\n");
        output.append("                                                <tbody>");

        for (ShipDTO ship : ships) {
            counter++;
            if (counter % 2 == 1) {
                output.append("<tr><td>").append(getShipCard(ship, request)).append("</td></tr>");
            }
        }

        output.append("</tbody>\n");
        output.append("                                            </table>\n");
        output.append("                                        </td>\n");
        output.append("                                        <td align='left' style='vertical-align: top;'>\n");
        output.append("                                            <table cellspacing='0' cellpadding='0'>\n");
        output.append("                                                <tbody>");

        counter = 0;
        for (ShipDTO ship : ships) {
            counter++;
            if (counter % 2 == 0) {
                output.append("<tr><td>").append(getShipCard(ship, request)).append("</td></tr>");
            }
        }

        output.append("</tbody>\n");
        output.append("                                            </table>\n");
        output.append("                                        </td>\n");
        output.append("                                    </tr>\n");
        output.append("                                    </tbody>\n");
        output.append("                                </table>\n");
        output.append("                            </div>\n");
        output.append("                        </div>\n");
        output.append("                    </td>\n");
        output.append("                   \n");
        output.append("                </tr>\n");
        output.append("                </tbody>\n");
        output.append("            </table>\n");
        output.append("        </div>\n");
        output.append("    </div>\n");
        output.append("</div>\n");
        return output.toString();
    }

%>


