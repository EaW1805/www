<%@ page import="com.eaw1805.data.model.army.Battalion" %>
<%@ page import="java.util.List" %>
<%!
    String getBattalionCard(Battalion battalion, HttpServletRequest request) {
        final StringBuilder output = new StringBuilder();
        String type = "Normal";
        if (battalion.getType().getCrack()) {
            type = "Crack";
            if (battalion.getType().getElite()) {
                type = type + " Elite";
            }
        }

        final StringBuilder strCapabilities = new StringBuilder();
        boolean found = false;
        if (battalion.getType().getTroopSpecsLc()) {
            strCapabilities.append(", Light Cavalry");
            found = true;
        }

        if (battalion.getType().getTroopSpecsCu()) {
            strCapabilities.append(", Cuirassiers");
            found = true;
        }

        if (battalion.getType().getTroopSpecsLr()) {
            strCapabilities.append(", Lancers");
            found = true;
        }

        final String capabilities;
        if (!found) {
            capabilities = "";

        } else {
            capabilities = strCapabilities.substring(2);
        }

        final StringBuilder strFormations = new StringBuilder();
        found = false;

        if (battalion.getType().getFormationLi()) {
            strFormations.append(", L");
            found = true;
        }

        if (battalion.getType().getFormationCo()) {
            strFormations.append(", C");
            found = true;
        }

        if (battalion.getType().getFormationSq()) {
            strFormations.append(", Q");
            found = true;
        }

        if (battalion.getType().getFormationSk()) {
            strFormations.append(", S");
            found = true;
        }

        final String formation;
        if (!found) {
            formation = "No formation";

        } else {
            formation = strFormations.substring(2);
        }

        output.append("<div style='position: relative; overflow: hidden; width: 245px; height: 90px;' class='ToolTip245x90 clickArmyPanel'><img\n");
        output.append("src='http://static.eaw1805.com/images/armies/");
        output.append(battalion.getType().getNation().getId());
        output.append("/");
        output.append(battalion.getType().getIntId());
        output.append(".jpg' class='gwt-Image'\n");
        output.append("        style='position: absolute; left: 3px; top: 3px; width: 86px; height: 86px;'>\n");
        output.append("\n");
        output.append("    <div class='clearFontMedSmall' style='position: absolute; left: 92px; top: 4px; width: 154px; height: 18px;'>\n");
        output.append("        ");
        output.append(battalion.getType().getName());
        output.append("\n");
        output.append("    </div>\n");
        output.append("    <div class='clearFontMedSmall' style='position: absolute; left: 92px; top: 25px;'>");
        output.append(type);
        output.append("</div>\n");
        output.append("    <div class='clearFontMedSmall' style='position: absolute; left: 92px; top: 40px;'>");
        output.append(battalion.getExperience());
        output.append("/");
        output.append(battalion.getType().getMaxExp());
        output.append(" - ");
        output.append(battalion.getHeadcount());
        output.append(" - ");
        output.append(battalion.getType().getMps());
        output.append(" MPs</div>\n");
        output.append("    <div class='clearFontMedSmall' style='position: absolute; left: 92px; top: 55px;'>");
        output.append(capabilities);
        output.append("</div>\n");
        output.append("    <div class='clearFontMedSmall' style='position: absolute; left: 92px; top: 70px;'>");
        output.append(formation);
        output.append("</div>\n");
        output.append("</div>");


        return output.toString();
    }


    String getBattalionsCards(final List<Battalion> battalions, HttpServletRequest request) {
        int counter = 0;
        StringBuilder output = new StringBuilder();
//        output.append("<div style='width:520px;height:400px;overflow:auto;'><table>");

        output.append("<div style='left: 201px; top: 101px;z-index: 3; visibility: visible; position: absolute; overflow: visible;'\n" +
                "     class='draggablePopup'>\n" +
                "    <div class='popupContent'>\n" +
                "        <div style='position: relative; overflow: hidden; width: 560px; height: 480px;' class='smallDoubleSelector'>\n" +
                "            <div class='clearFontMiniTitle whiteText' style='color:white; position: absolute; left: 24px; top: 19px;'>Battalions\n" +
                "            </div>\n" +
                "            <div id='handleDrag' style='color:white; position: absolute; left: 0px; top: 0px; width:506px; height:51px;'></div>" +
                "<img src='http://static.eaw1805.com/images/buttons/ButAcceptOff.png' class='draggableCloser pointer' style='position: absolute; left: 507px; top: 10px; width: 36px; height: 36px;'>" +
                "            <table cellspacing='0' cellpadding='0'\n" +
                "                   style='width: 527px; height: 396px; position: absolute; left: 18px; top: 67px;'>\n" +
                "                <tbody>\n" +
                "                <tr>\n" +
                "                    <td align='left' style='vertical-align: top;'>\n" +
                "                        <div style='overflow: auto; position: relative; width: 528px; height: 396px;'\n" +
                "                             >\n" +
                "                            <div style='position: relative;'>\n" +
                "                                <table cellspacing='0' cellpadding='0' style='width: 513px;'>\n" +
                "                                    <tbody>\n" +
                "                                    <tr>\n" +
                "                                        <td align='left' style='vertical-align: top;'>\n" +
                "                                            <table cellspacing='0' cellpadding='0'>\n" +
                "                                                <tbody>");


        for (Battalion battalion : battalions) {
            counter++;
            if (counter % 2 == 1) {
                output.append("<tr><td>" + getBattalionCard(battalion, request) + "</td></tr>");
            }
        }

        output.append("</tbody>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                        <td align='left' style='vertical-align: top;'>\n" +
                "                                            <table cellspacing='0' cellpadding='0'>\n" +
                "                                                <tbody>");

        counter = 0;
        for (Battalion battalion : battalions) {
            counter++;
            if (counter % 2 == 0) {
                output.append("<tr><td>" + getBattalionCard(battalion, request) + "</td></tr>");
            }
        }
        output.append("\n" +
                "                                                </tbody>\n" +
                "                                            </table>\n" +
                "                                        </td>\n" +
                "                                    </tr>\n" +
                "                                    </tbody>\n" +
                "                                </table>\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>");
        return output.toString();
    }
%>









