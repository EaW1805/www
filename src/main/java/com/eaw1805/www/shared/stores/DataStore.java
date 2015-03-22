package com.eaw1805.www.shared.stores;

import com.eaw1805.data.constants.NaturalResourcesConstants;
import com.eaw1805.data.constants.ProductionSiteConstants;
import com.eaw1805.data.dto.collections.DataCollection;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.data.dto.common.NaturalResourceDTO;
import com.eaw1805.data.dto.common.ProductionSiteDTO;
import com.eaw1805.www.client.events.loading.LoadEventManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DataStore
        implements ProductionSiteConstants, NaturalResourcesConstants {

    /**
     * A collection of data needed in the game
     */
    private transient DataCollection dataCollection = new DataCollection();

    /**
     * Our instance of the Manager.
     */
    private static DataStore ourInstance = null;

    /**
     * Variable telling us if our data are initialized.
     */
    private boolean isInitialized = false;

    private List<String> chatMessages = new ArrayList<String>();

    private boolean chatMessagesLoaded = false;

    private Map<Integer, Boolean> nationsToDead = new HashMap<Integer, Boolean>();

    // Method returning the economy manager if already initialized
    // or the a new instance
    public static DataStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataStore();
        }
        return ourInstance;
    }

    public void initDataCollection(final DataCollection dataCol) {
        dataCollection = dataCol;
        isInitialized = true;
    }

    public void initChatMessages(final List<String> oldMessages) {
        chatMessages = oldMessages;
        chatMessagesLoaded = true;
        LoadEventManager.loadChatMessages(chatMessages);
    }

    public void initNationsToDead(final Map<Integer, Boolean> inNationsToDead) {
        nationsToDead = inNationsToDead;
    }

    /**
     * Method that returns a nation's name by it's id
     *
     * @param nationId the id of the nation
     * @return a string that represents the name of the nation
     */
    public String getNationNameByNationId(final int nationId) {
        for (final NationDTO nation : dataCollection.getNations()) {
            if (nation.getNationId() == nationId) {
                return nation.getName();
            }
        }
        return "No nation";
    }

    /**
     * Method that returns a nation by it's id
     *
     * @param nationId the id of the nation in question
     * @return the data table object of the nation
     */
    public NationDTO getNationById(final int nationId) {
        return dataCollection.getNationById(nationId);
    }

    public NationDTO getNationByCode(final char code) {
        return dataCollection.getNationByCode(code);
    }

    public NationDTO getNationByName(final String nationName) {
        return dataCollection.getNationByName(nationName);
    }

    /**
     * @return the isInitialized
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * Get if old chat messages have been retrieved.
     *
     * @return True if chat messages have been retrieved.
     */
    public boolean isChatMessagesLoaded() {
        return chatMessagesLoaded;
    }

    /**
     * Get the old chat messages
     *
     * @return The old chat messages
     */
    public List<String> getChatMessages() {
        return chatMessages;
    }

    /**
     * Get if a nation is dead.
     *
     * @param nationId The nation id to check.
     * @return True if nation is dead.
     */
    public boolean isNationDead(final int nationId) {
        return nationsToDead.get(nationId);
    }

    public List<NationDTO> getNations() {
        return dataCollection.getNations();
    }

    public void setNations(final List<NationDTO> value) {
        dataCollection.setNations(value);
    }

    public String getNameByNationId(final int nationId) {
        return dataCollection.getNameByNationId(nationId);
    }

    public List<NaturalResourceDTO> getNatResources() {
        return dataCollection.getNatResources();
    }

    public ProductionSiteDTO getProdSite(final int prodSiteId) {
        return dataCollection.getProdSite(prodSiteId);
    }

    public List<ProductionSiteDTO> getProdSites() {
        return dataCollection.getProdSitesList();
    }

    // Returns a list of available production sites for this type of terrain
    public List<ProductionSiteDTO> getAvailProdSites(final String terrainCode, final int natResId) {
        final List<ProductionSiteDTO> availProdSites = new ArrayList<ProductionSiteDTO>();

        // No production site can be built in impassable terrain types.
        if ("X".equals(terrainCode)) {
            return availProdSites;
        }

        for (final ProductionSiteDTO site : dataCollection.getProdSitesList()) {
            final int prodSiteId = site.getId();
            if (prodSiteId == PS_MINE) {
                if (natResId == NATRES_ORE || natResId == NATRES_GEMS || natResId == NATRES_METALS) {
                    availProdSites.add(site);
                }

            } else if (prodSiteId == PS_VINEYARD) {
                if (natResId == NATRES_WINE) {
                    availProdSites.add(site);
                }

            } else if (prodSiteId == PS_PLANTATION) {
                if (natResId == NATRES_COLONIAL) {
                    availProdSites.add(site);
                }

            } else if (site.getTerrainsSuitable().contains(terrainCode)) {
                availProdSites.add(site);
            }

        }
        return availProdSites;
    }


}
