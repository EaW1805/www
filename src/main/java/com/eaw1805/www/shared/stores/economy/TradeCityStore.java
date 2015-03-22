package com.eaw1805.www.shared.stores.economy;

import com.eaw1805.data.constants.GoodConstants;
import com.eaw1805.data.constants.NaturalResourcesConstants;
import com.eaw1805.data.constants.OrderConstants;
import com.eaw1805.data.dto.common.PositionDTO;
import com.eaw1805.data.dto.common.SectorDTO;
import com.eaw1805.data.dto.web.TradeUnitAbstractDTO;
import com.eaw1805.data.dto.web.economy.TradeCityDTO;
import com.eaw1805.www.client.views.popups.TradeCityGoodsPopup;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.shared.stores.GameStore;
import com.eaw1805.www.shared.stores.RegionStore;
import com.eaw1805.www.shared.stores.map.economic.TradeCitiesGoodsGroupStore;
import org.vaadin.gwtgraphics.client.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TradeCityStore
        implements GoodConstants, NaturalResourcesConstants, OrderConstants {

    /**
     * A list containing all the available trade cities;
     */
    private transient List<TradeCityDTO> tradeCitiesList = new ArrayList<TradeCityDTO>();

    /**
     * Indexes barracks objects based on their position.
     */
    private final transient Map<String, TradeCityDTO> citiesMapSector = new HashMap<String, TradeCityDTO>();

    private final transient Map<Integer, TradeCityDTO> idCitiesMap = new HashMap<Integer, TradeCityDTO>();

    /**
     * Our instance of the Manager.
     */
    private static TradeCityStore ourInstance = null;

    /**
     * Variable telling us if our data are initialized.
     */
    private boolean isInitialized = false;

    Map<Integer, TradeCityGoodsPopup> tCityToPopupInfo = new HashMap<Integer, TradeCityGoodsPopup>();

    private TradeCitiesGoodsGroupStore trCitiesGoodGroup = new TradeCitiesGoodsGroupStore();

    /**
     * default constructor.
     */
    private TradeCityStore() {
        // do nothing
    }

    /**
     * Method returning the trade city manager if already initialized
     *
     * @return the unique instance of the store.
     */
    public static TradeCityStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new TradeCityStore();
        }
        return ourInstance;
    }

    /**
     * Method Used By the service to initialize the trade cities Map
     *
     * @param tradeCities as given by the asynchronous call-back
     */
    public void initTradeCities(final List<TradeCityDTO> tradeCities) {
        try {
            // clear any previous additions to collections
            if (!isInitialized()) {
                tradeCitiesList = tradeCities;
                for (final TradeCityDTO tCity : tradeCities) {
                    idCitiesMap.put(tCity.getId(), tCity);
                    citiesMapSector.put(tCity.positionToString(), tCity);
                    tCityToPopupInfo.put(tCity.getId(), new TradeCityGoodsPopup(tCity));
                }
                trCitiesGoodGroup.initGroups(tradeCities);
                isInitialized = true;
//                MapStore.getInstance().initSeaTradeCities(tradeCitiesList, RegionConstants.EUROPE, RegionConstants.CARIBBEAN, RegionConstants.AFRICA, RegionConstants.INDIES);
            }

        } catch (Exception Ex) {
            new ErrorPopup(ErrorPopup.Level.ERROR, "Failed to initialize trade cities due to unexpected reason", false);
        }
    }

    public Group getGroupGoodsPanelsByRegion(final int regionId) {
        return trCitiesGoodGroup.getTradeCitiesGoodsByRegion(regionId);
    }

    /**
     * Method that returns the trade city by coordinates
     *
     * @param sector the target sector
     * @return the Data Table Object or null when there is no
     *         trade city on this sector
     */
    public TradeCityDTO getTradeCityByPosition(final PositionDTO sector) {
        if (citiesMapSector.containsKey(sector.positionToString())) {
            return citiesMapSector.get(sector.positionToString());
        }

        return null;
    }

    /**
     * Method that buys a certain good from a trade city
     * and calculates the city funds
     *
     * @param tradeCityId the Id of the trade city.
     * @param goodType    the type of the good.
     * @param quantity    the quantity of the good.
     * @return true for a successful transaction.
     */
    public boolean buyGood(final int tradeCityId, final int goodType, final int quantity) {
        final TradeCityDTO tcity = getTradeCityById(tradeCityId);
        tcity.getSoldGoods().put(goodType, tcity.getSoldGoods().get(goodType) + quantity);
        return true;
    }

    /**
     * Method that undo the buying of a certain good from a trade city
     * and calculates the city funds
     *
     * @param tradeCityId the Id of the trade city.
     * @param goodType    the type of the good.
     * @param quantity    the quantity of the good.
     * @return true for a successful transaction.
     */
    public boolean undoBuyGood(final int tradeCityId, final int goodType, final int quantity) {
        final TradeCityDTO tcity = getTradeCityById(tradeCityId);
        tcity.getSoldGoods().put(goodType, tcity.getSoldGoods().get(goodType) - quantity);
        return true;
    }

    /**
     * Method that sells a certain good from a trade city
     * and calculates the city funds
     *
     * @param tradeCityId the Id of the trade city.
     * @param goodType    the type of the good.
     * @param quantity    the quantity of the good.
     * @return true for a successful transaction.
     */
    public boolean sellGood(final int tradeCityId, final int goodType, final int quantity) {
        final TradeCityDTO tcity = getTradeCityById(tradeCityId);
        tcity.getBoughtGoods().put(goodType, tcity.getBoughtGoods().get(goodType) + quantity);
        return true;
    }

    /**
     * Method that undoes a sell of a certain good from a trade city
     * and calculates the city funds
     *
     * @param tradeCityId the Id of the trade city.
     * @param goodType    the type of the good.
     * @param quantity    the quantity of the good.
     * @return true for a successful transaction.
     */
    public boolean undoSellGood(final int tradeCityId, final int goodType, final int quantity) {
        final TradeCityDTO tcity = getTradeCityById(tradeCityId);
        tcity.getBoughtGoods().put(goodType, tcity.getBoughtGoods().get(goodType) - quantity);
        return true;
    }

    /**
     * Method that returns the trade cities of the region
     *
     * @param regionId represents the id of the region
     * @param all      is true when all the cities are returned
     *                 else only your cities
     * @return A list of your cities
     */
    public List<TradeUnitAbstractDTO> getTradeCitiesByRegion(final int regionId, final boolean all) {
        final List<TradeUnitAbstractDTO> retList = new ArrayList<TradeUnitAbstractDTO>();
        final SectorDTO[][] sectors = RegionStore.getInstance().getRegionSectorsByRegionId(regionId);
        for (final TradeCityDTO tcity : tradeCitiesList) {
            if (tcity.getRegionId() == regionId) {
                if (all) {
                    retList.add(tcity);

                } else if (GameStore.getInstance().getNationId() == sectors[tcity.getX()][tcity.getY()].getNationId()) {
                    retList.add(tcity);
                }
            }
        }
        return retList;
    }

    /**
     * Method that returns a city by it's id
     *
     * @param tradeCityId the id of the requested city
     * @return the Trade City
     */
    public TradeCityDTO getTradeCityById(final int tradeCityId) {
        return idCitiesMap.get(tradeCityId);
    }

    /**
     * @param isInitialized the isInitialized to set
     */
    public void setInitialized(final boolean isInitialized) {
        this.isInitialized = isInitialized;
    }

    /**
     * @return the isInitialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * @return the tradeCitiesList
     */
    public List<TradeCityDTO> getTradeCitiesList() {
        return tradeCitiesList;
    }

    public TradeCityGoodsPopup getTradeCityGoodsPopupById(final int tCityId) {
        return tCityToPopupInfo.get(tCityId);
    }
}
