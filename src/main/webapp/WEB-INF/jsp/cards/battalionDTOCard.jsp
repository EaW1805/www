<%@ page import="com.eaw1805.data.dto.web.army.BattalionDTO" %>
<%@ page import="java.util.List" %>
<%!
    String getBattalionCard(final BattalionDTO battalion, final HttpServletRequest request, final boolean showMoraleIcons) {
        final StringBuffer output = new StringBuffer();
        String type = "Normal";
        if (battalion.getEmpireArmyType().isCrack()) {
            type = "Crack";
            if (battalion.getEmpireArmyType().isElite()) {
                type = type + " Elite";
            }
        }

        final StringBuilder strCapabilities = new StringBuilder();
        boolean found = false;
        if (battalion.getEmpireArmyType().getTroopSpecsLc()) {
            strCapabilities.append(", Light Cavalry");
            found = true;
        }

        if (battalion.getEmpireArmyType().getTroopSpecsCu()) {
            strCapabilities.append(", Cuirassiers");
            found = true;
        }

        if (battalion.getEmpireArmyType().getTroopSpecsLr()) {
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

        if (battalion.getEmpireArmyType().getFormationLi()) {
            strFormations.append(", L");
            found = true;
        }

        if (battalion.getEmpireArmyType().getFormationCo()) {
            strFormations.append(", C");
            found = true;
        }

        if (battalion.getEmpireArmyType().getFormationSq()) {
            strFormations.append(", Q");
            found = true;
        }

        if (battalion.getEmpireArmyType().getFormationSk()) {
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
        output.append("        src='http://static.eaw1805.com/images/armies/");
        output.append(battalion.getEmpireArmyType().getNationId());
        output.append("/");
        output.append(battalion.getEmpireArmyType().getIntId());
        output.append(".jpg' class='gwt-Image'\n");
        output.append("        style='position: absolute; left: 3px; top: 3px; width: 86px; height: 86px;'>\n");
        output.append("\n");
        output.append("    <div class='clearFontMedSmall' style='position: absolute; left: 92px; top: 4px; width: 154px; height: 18px;'>\n");
        output.append("        ");
        output.append(battalion.getEmpireArmyType().getName());
        output.append("\n");
        output.append("    </div>\n");
        output.append("    <div class='clearFontMedSmall' style='position: absolute; left: 92px; top: 25px;'>");
        output.append(type);
        output.append("</div>\n");
        output.append("    <div class='clearFontMedSmall' style='position: absolute; left: 92px; top: 40px;'>");
        output.append(battalion.getExperience());
        output.append("/");
        output.append(battalion.getEmpireArmyType().getMaxExp());
        output.append(" - ");
        output.append(battalion.getHeadcount());
        output.append(" - ");
        output.append(battalion.getEmpireArmyType().getMps());
        output.append(" MPs</div>\n");
        output.append("    <div class='clearFontMedSmall' style='position: absolute; left: 92px; top: 55px;'>");
        output.append(capabilities);
        output.append("</div>\n");
        output.append("    <div class='clearFontMedSmall' style='position: absolute; left: 92px; top: 70px;'>");
        output.append(formation);
        output.append("</div>\n");

        if (showMoraleIcons) {
            output.append("    <div class='clearFontMedSmall' style='position: absolute; left: 207px; top: 54px;'>");
            if (battalion.isFleeing()) {
                output.append("<img src='http://static.eaw1805.com/images/battles/icon-routed.png' class='gwt-Image' title='Routed battalion'>");

            } else {
                output.append("<img src='http://static.eaw1805.com/images/battles/icon-rallied.png' class='gwt-Image' title='Rallied battalion'>");

            }
            output.append("</div>\n");
        }

        output.append("</div>");

        return output.toString();
    }

    String getBattalionsCards(final List<BattalionDTO> battalions, final HttpServletRequest request, final boolean showMoraleIcons) {
        int counter = 0;
        final StringBuilder output = new StringBuilder();
//        output.append("<div style='width:520px;height:400px;overflow:auto;'><table>");

        output.append("<div style='left: 201px; top: 101px;z-index: 3; visibility: visible; position: absolute; overflow: visible;'\n");
        output.append("     class='draggablePopup'>\n");
        output.append("    <div class='popupContent'>\n");
        output.append("        <div style='position: relative; overflow: hidden; width: 560px; height: 480px;' class='smallDoubleSelector'>\n");
        output.append("            <div class='clearFontMiniTitle whiteText' style='color:white; position: absolute; left: 24px; top: 19px;'>Battalions\n");
        output.append("            </div>\n");
        output.append("            <div id='handleDrag' style='color:white; position: absolute; left: 0px; top: 0px; width:506px; height:51px;'></div>");
        output.append("<img src='http://static.eaw1805.com/images/buttons/ButAcceptOff.png' class='draggableCloser pointer' style='position: absolute; left: 507px; top: 10px; width: 36px; height: 36px;'>");
        output.append("            <table cellspacing='0' cellpadding='0'\n");
        output.append("                   style='width: 527px; height: 396px; position: absolute; left: 18px; top: 67px;'>\n");
        output.append("                <tbody>\n");
        output.append("                <tr>\n");
        output.append("                    <td align='left' style='vertical-align: top;'>\n");
        output.append("                        <div style='overflow: auto; position: relative; width: 528px; height: 396px;'\n");
        output.append("                             >\n");
        output.append("                            <div style='position: relative;'>\n");
        output.append("                                <table cellspacing='0' cellpadding='0' style='width: 513px;'>\n");
        output.append("                                    <tbody>\n");
        output.append("                                    <tr>\n");
        output.append("                                        <td align='left' style='vertical-align: top;'>\n");
        output.append("                                            <table cellspacing='0' cellpadding='0'>\n");
        output.append("                                                <tbody>");


        for (BattalionDTO battalion : battalions) {
            counter++;
            if (counter % 2 == 1) {
                output.append("<tr><td>");
                output.append(getBattalionCard(battalion, request, showMoraleIcons));
                output.append("</td></tr>");
            }
        }

        output.append("</tbody>\n");
        output.append("                                            </table>\n");
        output.append("                                        </td>\n");
        output.append("                                        <td align='left' style='vertical-align: top;'>\n");
        output.append("                                            <table cellspacing='0' cellpadding='0'>\n");
        output.append("                                                <tbody>");

        counter = 0;
        for (BattalionDTO battalion : battalions) {
            counter++;
            if (counter % 2 == 0) {
                output.append("<tr><td>");
                output.append(getBattalionCard(battalion, request, showMoraleIcons));
                output.append("</td></tr>");
            }
        }

        output.append("\n");
        output.append("                                                </tbody>\n");
        output.append("                                            </table>\n");
        output.append("                                        </td>\n");
        output.append("                                    </tr>\n");
        output.append("                                    </tbody>\n");
        output.append("                                </table>\n");
        output.append("                            </div>\n");
        output.append("                        </div>\n");
        output.append("                    </td>\n");
        output.append("                </tr>\n");
        output.append("                </tbody>\n");
        output.append("            </table>\n");
        output.append("        </div>\n");
        output.append("    </div>\n");
        output.append("</div>");
        return output.toString();
    }
%>

