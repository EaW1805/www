package com.eaw1805.www.controllers.remote.hotspot.movement;

import com.eaw1805.data.dto.web.OrderDTO;
import com.eaw1805.data.dto.web.movement.MovementDTO;
import com.eaw1805.data.dto.web.movement.PathDTO;
import com.eaw1805.data.dto.web.movement.PathSectorDTO;
import com.eaw1805.data.model.map.Position;
import com.eaw1805.data.model.map.Sector;
import com.eaw1805.www.controllers.remote.EmpireRpcServiceImpl;
import com.eaw1805.www.controllers.remote.hotspot.AbstractChangesProcessor;
import com.eaw1805.www.controllers.remote.hotspot.ChangesProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MovementApplyChangesProcessor
        extends AbstractChangesProcessor
        implements ChangesProcessor {

    /**
     * The order types that this processor is handling.
     */
    public static final Object[] ORDERS_TYPES = {
            ORDER_M_UNIT
    };

    private transient Map<Integer, Map<Integer, MovementDTO>> typeIdMvMap;

    private transient List<OrderDTO> orders;

    private transient final EmpireRpcServiceImpl service;

    /**
     * Default constructor.
     *
     * @param thisGame   the game of the order.
     * @param thisNation the owner of the order.
     * @param thisTurn   the turn of the order.
     */
    public MovementApplyChangesProcessor(final int thisScenario, final int thisGame,
                                         final int thisNation,
                                         final int thisTurn,
                                         final EmpireRpcServiceImpl service) {
        super(thisScenario, thisGame, thisNation, thisTurn);
        this.service = service;
    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Collection<?> dbData, final Collection<?> chData) {
        orders = (List<OrderDTO>) chData;

    }

    @SuppressWarnings({"unchecked", "restriction"})
    public void addData(final Map<?, ?> dbData, final Map<?, ?> chData) {
        typeIdMvMap = (Map<Integer, Map<Integer, MovementDTO>>) dbData;
    }

    @SuppressWarnings("restriction")
    public List<?> processChanges() {
        final List<Map<Integer, Map<Integer, MovementDTO>>> dummyList = new ArrayList<Map<Integer, Map<Integer, MovementDTO>>>();
        for (final OrderDTO order : orders) {
            if (order.getType() == ORDER_M_UNIT) {
                final int unitType = Integer.parseInt(order.getParameter1());
                final int unitId = Integer.parseInt(order.getParameter2());
                if (!typeIdMvMap.containsKey(unitType)) {
                    typeIdMvMap.put(unitType, new HashMap<Integer, MovementDTO>());
                }

                if (!typeIdMvMap.get(unitType).containsKey(unitId)) {
                    typeIdMvMap.get(unitType).put(unitId, new MovementDTO());
                }

                final MovementDTO mvDto = new MovementDTO();
                mvDto.setArmyType(unitType);
                mvDto.setId(unitId);
                mvDto.setRegionId(Integer.parseInt(order.getTemp1()));

                final List<PathDTO> pathList = new ArrayList<PathDTO>();
                if (order.getParameter3().contains("!")) {
                    final StringTokenizer stokPaths = new StringTokenizer(order.getParameter3(), "!");

                    // iterate through all other tokens
                    while (stokPaths.hasMoreElements()) {
                        final PathDTO thisPath = constructPathDTO(stokPaths.nextToken(), mvDto.getRegionId());
                        thisPath.setRegionId(mvDto.getRegionId());
                        pathList.add(thisPath);
                    }

                } else {
                    final PathDTO thisPath = constructPathDTO(order.getParameter3(), mvDto.getRegionId());
                    thisPath.setRegionId(mvDto.getRegionId());
                    pathList.add(thisPath);
                }

                int totalCost = 0;
                for (PathDTO path : pathList) {
                    totalCost += path.getTotalCost();
                }

                mvDto.setTotalCost(totalCost);
                mvDto.setPaths(pathList);

                final int movetype = Integer.parseInt(order.getParameter5());
                if (movetype == ORDER_P_SHIP
                        || movetype == ORDER_P_FLEET) {
                    mvDto.setPatrol(true);

                } else if (movetype == ORDER_FM_ARMY
                        || movetype == ORDER_FM_CORP
                        || movetype == ORDER_FM_BRIG) {
                    mvDto.setForcedMarch(true);
                }

                if (!order.getParameter4().trim().equals("")
                        && Integer.parseInt(order.getParameter4()) == 1) {
                    mvDto.setPatrol(true);
                }

                typeIdMvMap.get(unitType).put(unitId, mvDto);
            }
        }
        dummyList.add(typeIdMvMap);
        return dummyList;
    }

    @SuppressWarnings("restriction")
    private PathDTO constructPathDTO(final String pathAsString, final int regionId) {
        final PathDTO thisPath = new PathDTO();
        final List<PathSectorDTO> pathSectors = new ArrayList<PathSectorDTO>();
        final StringTokenizer stokSinglePath = new StringTokenizer(
                pathAsString, "-");
        // iterate through all other tokens
        while (stokSinglePath.hasMoreElements()) {
            final StringTokenizer stokCoords = new StringTokenizer(stokSinglePath.nextToken(), ":");
            final String coordX = stokCoords.nextToken();
            final String coordY = stokCoords.nextToken();

            final PathSectorDTO thisPos = new PathSectorDTO();
            thisPos.setX(Integer.parseInt(coordX));
            thisPos.setY(Integer.parseInt(coordY));
            thisPos.setRegionId(regionId);
            pathSectors.add(thisPos);
        }

        final int totalCost = addPathUrlsToPathSectors(pathSectors);
        thisPath.setPathSectors(pathSectors);
        thisPath.setTotalCost(totalCost);
        thisPath.setTotLength(pathSectors.size());
        return thisPath;
    }

    private int addPathUrlsToPathSectors(final List<PathSectorDTO> sectors) {
        try {
            int preX = sectors.get(0).getX();
            int preY = sectors.get(0).getY();
            int posX;
            int posY;
            int nextX = 0;
            int nextY = 0;
            int totalCost = 0;
            for (final PathSectorDTO thisSector : sectors) {
                final StringBuilder path = new StringBuilder();
                if (sectors.indexOf(thisSector) == 0) {
                    path.append("start-");

                } else if (sectors.indexOf(thisSector) == sectors.size() - 1) {
                    path.append("end-");

                } else {
                    path.append("move-");
                }
                posX = thisSector.getX();
                posY = thisSector.getY();

                if ((sectors.indexOf(thisSector) + 1) <= (sectors.size() - 1)) {
                    nextX = sectors.get(sectors.indexOf(thisSector) + 1).getX();
                    nextY = sectors.get(sectors.indexOf(thisSector) + 1).getY();
                }

                if ((sectors.indexOf(thisSector) - 1) > 0) {
                    preX = sectors.get(sectors.indexOf(thisSector) - 1).getX();
                    preY = sectors.get(sectors.indexOf(thisSector) - 1).getY();
                }

                final String thisPath1 = path.toString();
                if (!thisPath1.contains("start") && !thisPath1.contains("end")) {
                    path.append(getStartingDirection(preX, preY, posX, posY));
                }

                final String thisPath2 = path.toString();
                if (!thisPath2.contains("start") && !thisPath2.contains("end")) {
                    path.append("-");
                }

                if (path.toString().contains("end")) {
                    path.append(getEndingDirection(preX, preY, posX, posY));

                } else {
                    path.append(getEndingDirection(posX, posY, nextX, nextY));
                }

                thisSector.setPath(path.toString());

                final Position pos = new Position();
                pos.setRegion(service.regionManager.getByID(thisSector.getRegionId()));
                pos.setGame(service.gameManager.getByID(getGameId()));
                pos.setX(thisSector.getX());
                pos.setY(thisSector.getY());

                final Sector sec = service.getSectorManager().getByPosition(pos);
                if (sectors.indexOf(thisSector) != 0) {
                    totalCost += sec.getTerrain().getMps();
                }
            }

            return totalCost;

        } catch (Exception ex) {
            return 0;
        }
    }

    private String getEndingDirection(final int startX, final int startY,
                                      final int thisX, final int thisY) {
        if (startX < thisX && startY == thisY) {
            return "R";

        } else if (startX < thisX && startY < thisY) {
            return "RD";

        } else if (startX < thisX && startY > thisY) {
            return "RU";

        } else if (startX > thisX && startY == thisY) {
            return "L";

        } else if (startX > thisX && startY < thisY) {
            return "LD";

        } else if (startX > thisX && startY > thisY) {
            return "LU";

        } else if (startX == thisX && startY < thisY) {
            return "D";

        } else if (startX == thisX && startY > thisY) {
            return "U";
        }

        return "";
    }

    private String getStartingDirection(final int startX, final int startY,
                                        final int thisX, final int thisY) {

        if (startX < thisX && startY == thisY) {
            return "L";

        } else if (startX < thisX && startY < thisY) {
            return "LU";

        } else if (startX < thisX && startY > thisY) {
            return "LD";

        } else if (startX > thisX && startY == thisY) {
            return "R";

        } else if (startX > thisX && startY < thisY) {
            return "RU";

        } else if (startX > thisX && startY > thisY) {
            return "RD";

        } else if (startX == thisX && startY < thisY) {
            return "U";

        } else if (startX == thisX && startY > thisY) {
            return "D";
        }

        return "";
    }

}
