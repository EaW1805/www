package com.eaw1805.www.shared.stores;

import com.eaw1805.data.constants.ArmyConstants;
import com.eaw1805.data.constants.RegionConstants;
import com.eaw1805.data.dto.web.GameSettingsDTO;
import com.eaw1805.data.dto.web.army.BrigadeDTO;
import com.eaw1805.data.dto.web.army.CorpDTO;
import com.eaw1805.www.client.gui.GuiComponent;
import com.eaw1805.www.client.gui.GuiComponentBase;
import com.eaw1805.www.client.gui.GuiComponentMovement;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandler;
import com.eaw1805.www.client.views.layout.LayoutView;
import com.eaw1805.www.client.views.layout.MiniMapPanel;
import com.eaw1805.www.client.views.layout.SectorMenu;
import com.eaw1805.www.client.views.popups.BrigadesViewerPopup;
import com.eaw1805.www.client.views.popups.CorpsViewerPopup;
import com.eaw1805.www.client.views.popups.ShipsInFleetViewerPopup;
import com.eaw1805.www.client.widgets.CallAlliesPopup;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.WindowPanelEAW;
import com.eaw1805.www.shared.stores.map.MapStore;
import com.eaw1805.www.shared.stores.units.ArmyStore;
import com.eaw1805.www.shared.stores.units.NavyStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author tsakygr
 *         This singleton provides us with the data
 *         that are needed throughout the game.
 *         <p/>
 *         Here we are going to save the state of the game
 *         in order to free the Cookies and in general the
 *         session variables so that the player can play
 *         with multiple nation simultaneously
 */
public final class GameStore {

    /**
     * Scenario identification number.
     */
    private int scenarioId = 0;

    /**
     * Game identification number
     */
    private int gameId = 0;

    /**
     * Nation identification number
     */
    private int nationId = 0;

    /**
     * Game turn
     */
    private int turn = 0;

    /**
     * The name of the nation you are playing
     */
    private String nationName = "";

    /**
     * The total european population
     */
    private int totalPopulation = 0;

    /**
     * The total number of VPs earned by the nation.
     */
    private int vps = 0;

    /**
     * The maximum number of VPs required by the nation.
     */
    private int vpsMax = 0;

    /**
     * Captures if the Random Event Trade Surplus is in effect.
     */
    private int surplus = 0;

    /**
     * Captures if the Random Event Trade Deficit is in effect.
     */
    private int deficit = 0;

    private boolean fogOfWar;

    /**
     * Our instance of the GameStore
     */
    private static transient GameStore ourInstance = null;

    /**
     * Indicator that tell us the type of the unit that has been selected by hovering a figure image
     */
    private int selectedUnitType = 0;

    /**
     * Indicator that tell us the id of the unit that has been selected by hovering a figure image
     */
    private int selectedUnitId = 0;

    /**
     * Indicator that tell us if there is a unit selected by hovering a figure image
     */
    private boolean unitSelected = false;

    /**
     * Settings values
     */
    private GameSettingsDTO settings = null;

    private boolean showArmies = true;
    private boolean showNavy = true;
    private boolean showGrid = false;
    private boolean showBorders = true;
    private boolean lowResolution = true;
    private boolean showMovement = true;
    private boolean showInfluence = false;
    private boolean showSupplyLines = false;
    private boolean showPopulation = false;
    private boolean showMinimap = true;
    private boolean showVirtualReportedUnits = true;
    private boolean showPolitical = true;
    private boolean nationDead = false;
    private boolean gameEnded = false;
    private boolean playMusic = true;
    private boolean playSoundEffects = true;
    private boolean windowsVisible = true;
    private double zoomLevel = 1d;
    private String username;
    private boolean allowHarshTaxation = true;
    private boolean showTradeCities = true;
    private boolean fullscreen = false;
    private int coloniesMPsModifier = 2;
    private boolean fastAssignment = false;

    /**
     * Indicator if costs are doubled for land forces.
     */
    private boolean doubleCostsArmy = false;

    /**
     * Indicator if costs are doubled for naval forces.
     */
    private boolean doubleCostsNavy = false;

    /**
     * Indicator of ship construction is fast or normal.
     * Values :
     * false : normal ship construction.
     * true : fast ship construction.
     */
    private boolean fastShipConstruction;

    /**
     * Various Menus
     */
    private SectorMenu sectorMenu = null;

    private MiniMapPanel miniMapPanel = null;

    /**
     * The layout view of the game.
     * Also known as the client
     * in this case.
     */
    private LayoutView layoutView;

    private final List<GuiComponent> registeredComponents = new ArrayList<GuiComponent>();

    /**
     * This list will contain all the panels that should be closed when esc is pressed.
     */
    private final List<WindowPanelEAW> registeredPanels = new ArrayList<WindowPanelEAW>();

    /**
     * An auto-increment value to be set as id for registered panels
     */
    private int registeredPanelsId = 0;

    /**
     * An auto-increment value to be set as id for registered popups
     */
    private int registeredComponentsId = 0;


    private boolean firstLoad;

    /**
     * A list of strings about the call for allies in format ally:enemy
     */
    List<String> callForAllies = new ArrayList<String>();

    private final Map<String, List<DelEventHandler>> registeredEvents = new HashMap<String, List<DelEventHandler>>();

    /**
     * Method returning the game store
     *
     * @return the GameStore
     */
    public static GameStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new GameStore();
        }
        return ourInstance;
    }

    public Map<String, List<DelEventHandler>> getRegisteredEvents() {
        return registeredEvents;
    }

    public void initGameVariables(final int scenarioId, final int gameId, final int nationId, final int turn, final String nationName,
                                  final boolean firstLoad, final String username, final boolean fogOfWar, final boolean fullscreen,
                                  final boolean colonyMpsNormal, final boolean fastAssignment, final boolean fastShipConstruction,
                                  final boolean doubleCostsArmy, final boolean doubleCostsNavy) {
        setScenarioId(scenarioId);
        setTurn(turn);
        setNationId(nationId);
        setGameId(gameId);
        setNationName(nationName);
        setFirstLoad(firstLoad);
        setUsername(username);
        setFogOfWar(fogOfWar);
        if (colonyMpsNormal) {
            setColoniesMPsModifier(1);
        } else {
            setColoniesMPsModifier(2);
        }
        setFastAssignment(fastAssignment);
        setDoubleCostsArmy(doubleCostsArmy);
        setDoubleCostsNavy(doubleCostsNavy);
        setFastShipConstruction(fastShipConstruction);

        MapStore.getInstance().toggleFullScreen(fullscreen);
    }

    public void initCallForAllies(final List<String> calls) {
        callForAllies.clear();
        callForAllies = calls;
    }

    /**
     * @param value the turn to set
     */
    private void setTurn(final int value) {
        turn = value;
    }

    /**
     * @return the turn
     */
    public int getTurn() {
        return turn;
    }

    /**
     * @return the gameId
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * @return the nationId
     */
    public int getNationId() {
        return nationId;
    }

    public int getNationIdForFogOfWar() {
        if (fogOfWar) {
            return nationId;
        }
        return 0;
    }

    /**
     * @param value the gameId to set
     */
    private void setGameId(final int value) {
        gameId = value;
    }

    public int getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(int scenarioId) {
        this.scenarioId = scenarioId;
    }

    /**
     * @param value the nationId to set
     */
    private void setNationId(final int value) {
        nationId = value;
    }

    /**
     * @param value the nationName to set
     */
    public void setNationName(final String value) {
        nationName = value;
    }

    /**
     * @return the nationName
     */
    public String getNationName() {
        return nationName;
    }

    /**
     * @param value the layoutView to set
     */
    public void setLayoutView(final LayoutView value) {
        layoutView = value;
    }

    /**
     * @return the layoutView
     */
    public LayoutView getLayoutView() {
        return layoutView;
    }

    /**
     * @param value the totalPopulation to set
     */
    public void setTotalPopulation(final int value) {
        totalPopulation = value;
    }

    /**
     * @return the totalPopulation
     */
    public int getTotalPopulation() {
        return totalPopulation;
    }

    /**
     * Get the total number of VPs earned by the nation.
     *
     * @return the total number of VPs earned by the nation.
     */
    public int getVps() {
        return vps;
    }

    /**
     * Set the total number of VPs earned by the nation.
     *
     * @param value the total number of VPs earned by the nation.
     */
    public void setVps(final int value) {
        this.vps = value;
    }

    /**
     * Get the maximum number of VPs required by the nation.
     *
     * @return the maximum number of VPs required by the nation.
     */
    public int getVpsMax() {
        return vpsMax;
    }

    /**
     * Set the maximum number of VPs required by the nation.
     *
     * @param value the maximum number of VPs required by the nation.
     */
    public void setVpsMax(final int value) {
        this.vpsMax = value;
    }

    /**
     * Get if the Random Event Trade Deficit is in effect.
     *
     * @return if the Random Event Trade Deficit is in effect.
     */
    public int getDeficit() {
        return deficit;
    }

    /**
     * Set if the Random Event Trade Deficit is in effect.
     *
     * @param value if the Random Event Trade Deficit is in effect.
     */
    public void setDeficit(final int value) {
        deficit = value;
    }

    /**
     * Get if the Random Event Trade Surplus is in effect.
     *
     * @return if the Random Event Trade Surplus is in effect.
     */
    public int getSurplus() {
        return surplus;
    }

    /**
     * Set if the Random Event Trade Surplus is in effect.
     *
     * @param value if the Random Event Trade Surplus is in effect.
     */
    public void setSurplus(final int value) {
        surplus = value;
    }

    /**
     * @return the showArmies
     */
    public boolean isShowArmies() {
        return showArmies;
    }

    /**
     * @return the showNavy
     */
    public boolean isShowNavy() {
        return showNavy;
    }

    /**
     * @return the showGrid
     */
    public boolean isShowGrid() {
        return showGrid;
    }

    /**
     * @return the showBorders
     */
    public boolean isShowBorders() {
        return showBorders;
    }

    /**
     * @return the showMovement
     */
    public boolean isShowMovement() {
        return showMovement;
    }

    /**
     * @return the showInfluence
     */
    public boolean isShowInfluence() {
        return showInfluence;
    }

    public boolean isShowSupplyLines() {
        return showSupplyLines;
    }

    public void setShowSupplyLines(boolean showSupplyLines) {
        this.showSupplyLines = showSupplyLines;
    }

    /**
     * @return the showPopulation
     */
    public boolean isShowPopulation() {
        return showPopulation;
    }

    public boolean isPlayMusic() {
        return playMusic;
    }

    public void setPlayMusic(final boolean playMusic) {
        this.playMusic = playMusic;
    }

    public boolean isPlaySoundEffects() {
        return playSoundEffects;
    }

    public void setPlaySoundEffects(final boolean playSoundEffects) {
        this.playSoundEffects = playSoundEffects;
    }

    /**
     * Set true to show virtual units reported by spies.
     *
     * @param value The value to set.
     */
    public void setShowVirtualReportedUnits(final boolean value) {
        showVirtualReportedUnits = value;
    }

    /**
     * @return the showMinimap
     */
    public boolean isShowMinimap() {
        return showMinimap;
    }

    /**
     * @param value the showArmies to set
     */
    public void setShowArmies(final boolean value) {
        showArmies = value;
    }

    /**
     * @param value the showNavy to set
     */
    public void setShowNavy(final boolean value) {
        showNavy = value;
    }

    /**
     * @param value the showGrid to set
     */
    public void setShowGrid(final boolean value) {
        showGrid = value;
    }

    /**
     * @param value the showBorders to set
     */
    public void setShowBorders(final boolean value) {
        showBorders = value;
    }

    /**
     * @param value the showMovement to set
     */
    public void setShowMovement(final boolean value) {
        showMovement = value;
    }

    /**
     * @param value the showInfluence to set
     */
    public void setShowInfluence(final boolean value) {
        showInfluence = value;
    }

    /**
     * @param value the showPopulation to set
     */
    public void setShowPopulation(final boolean value) {
        showPopulation = value;
    }

    /**
     * @param value the showMinimap to set
     */
    public void setShowMinimap(final boolean value) {
        showMinimap = value;
    }

    /**
     * @param value the lowResolution to set
     */
    public void setLowResolution(final boolean value) {
        lowResolution = value;
    }

    public double getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(final double value) {
        this.zoomLevel = value;
    }

    /**
     * @param value the sectorMenu to set
     */
    public void setSectorMenu(final SectorMenu value) {
        sectorMenu = value;
    }

    /**
     * @return the sectorMenu
     */
    public SectorMenu getSectorMenu() {
        return sectorMenu;
    }

    /**
     * The minimap panel object to set.
     *
     * @param value The object to set.
     */
    public void setMiniMapPanel(final MiniMapPanel value) {
        miniMapPanel = value;
    }

    public GameSettingsDTO getSettings() {
        return settings;
    }

    public void setSettings(final GameSettingsDTO value) {
        settings = value;

        //update usage values
        showArmies = settings.isLandForces();
        showBorders = settings.isPoliticalBorders();
        showGrid = settings.isGrid();
        showInfluence = settings.isSphereOfInfluence();
        showMovement = settings.isMovements();
        showNavy = settings.isNavalForces();
        showPopulation = settings.isPopulationDensity();
        showVirtualReportedUnits = settings.isVirtualReportedUnits();
        lowResolution = settings.isLowResolution();
        nationDead = settings.isDeadNation();
        playMusic = settings.isMusic();
        playSoundEffects = settings.isSoundEffects();
        zoomLevel = settings.getZoom();
        allowHarshTaxation = settings.isAllowHarshTax();
        gameEnded = settings.isGameEnded();
        showTradeCities = settings.isTradeCities();
        fullscreen = settings.isFullscreen();

        //update buttons on settings panel
        miniMapPanel.getSettingsPanel().getChbPopSizes().setChecked(showPopulation);
        miniMapPanel.getSettingsPanel().getChbShowArmies().setChecked(showArmies);
        miniMapPanel.getSettingsPanel().getChbShowBorders().setChecked(showBorders);
        miniMapPanel.getSettingsPanel().getChbShowGrid().setChecked(showGrid);
        miniMapPanel.getSettingsPanel().getChbShowMovements().setChecked(showMovement);
        miniMapPanel.getSettingsPanel().getChbShowNationInfluence().setChecked(showInfluence);
        miniMapPanel.getSettingsPanel().getChbShowNavy().setChecked(showNavy);
        miniMapPanel.getSettingsPanel().getChbShowReportedUnits().setChecked(showVirtualReportedUnits);
        miniMapPanel.getSettingsPanel().getChbPlayMusic().setChecked(playMusic);
        miniMapPanel.getSettingsPanel().getChbPlaySoundEffects().setChecked(playSoundEffects);
        miniMapPanel.getSettingsPanel().getChbLowRes().setChecked(lowResolution);
        miniMapPanel.getSettingsPanel().getChbTradeCities().setChecked(showTradeCities);
        miniMapPanel.getSettingsPanel().getChbFullScreen().setChecked(fullscreen);
        MapStore.getInstance().setResolution(lowResolution);
        MapStore.getInstance().setBorders(showBorders);

        miniMapPanel.getSettingsPanel().getChbRememberZoom().setChecked(zoomLevel > 0);
        if (zoomLevel > 0) {
            MapStore.getInstance().setZoomLevel(zoomLevel);
        }

        // update map
        if (RegionStore.getInstance().isInitialized()) {
            MapStore.getInstance().getMapsView().constructMap(RegionConstants.REGION_FIRST, true);
        }

        if (nationDead) {
            new ErrorPopup(ErrorPopup.Level.NORMAL, "Your empire is dead...", false);
        }
    }

    public native boolean isMobileDevice() /*-{
        var isMob = navigator.userAgent.match(/(iPad)|(iPhone)|(iPod)|(android)|(webOS)/i)
        return isMob != null;
    }-*/;

    /**
     * Register panel to open panels list.
     *
     * @param panel The panel to register.
     */
    public void registerPanel(final WindowPanelEAW panel) {
        if (panel.getWindowId() == 0) {
            registeredPanelsId++;
            panel.setWindowId(registeredPanelsId);
        }

        //if exist in the list remove it... since we want to put it in the first position
        final Iterator<WindowPanelEAW> wIter = registeredPanels.iterator();
        while (wIter.hasNext()) {
            final WindowPanelEAW wPanel = wIter.next();
            if (wPanel.getWindowId() == panel.getWindowId()) {
                wIter.remove();
                break;
            }
        }

        //fix z-index values
        int position = 1;
        for (int index = registeredPanels.size() - 1; index >= 0; index--) {
            final WindowPanelEAW wPanel = registeredPanels.get(index);
            wPanel.setWindowPosition(position);
            position++;
        }
        //the position is equal to the z-index that it will get. so we want the new panel to be in the front
        //so we add the larger position value.
        panel.setWindowPosition(position);

        //now add the panel in the first place of the list
        registeredPanels.add(0, panel);
    }

    /**
     * Un-register panel from open panels list.
     *
     * @param panel The panel to un-register.
     */
    public void unRegisterPanel(final WindowPanelEAW panel) {
        //remove from list
        final Iterator<WindowPanelEAW> wIter = registeredPanels.iterator();
        while (wIter.hasNext()) {
            final WindowPanelEAW wPanel = wIter.next();
            if (wPanel.getWindowId() == panel.getWindowId()) {
                wIter.remove();
                break;
            }
        }
        int position = 1;
        //fix positions..
        for (int index = registeredPanels.size() - 1; index >= 0; index--) {
            final WindowPanelEAW wPanel = registeredPanels.get(index);
            wPanel.setWindowPosition(position);
            position++;
        }
    }

    /**
     * Bring panel on top of the others.
     *
     * @param panel The panel to bring on top.
     */
    public void bringPanelToTop(final WindowPanelEAW panel) {
        //we just re-register it...
        registerPanel(panel);
    }

    /**
     * Register panel to open panels list.
     *
     * @param component  The panel to register.
     * @param isMovement if the panel is related to movement.
     */
    public void registerComponent(final GuiComponent component, final boolean isMovement) {

        if (component.getId() == 0) {
            registeredComponentsId++;
            component.setId(registeredComponentsId);
        }

        //if exist in the list remove it... since we want to put it in the first position
        final Iterator<GuiComponent> cIter = registeredComponents.iterator();
        while (cIter.hasNext()) {
            final GuiComponent comp = cIter.next();
            if (isMovement) {
                //if it is movement search by unit type and id..
                if (comp.getUnitId() == component.getUnitId()
                        && comp.getUnitType() == component.getUnitType()) {
                    cIter.remove();
                    break;
                }
            } else {
                //if it is not movement search by component id.
                if (comp.getId() == component.getId()) {
                    cIter.remove();
                    break;
                }
            }
        }
        //now add the panel in the first place of the list
        registeredComponents.add(0, component);
    }

    /**
     * Bring panel on top of the others.
     *
     * @param component  The panel to bring on top.
     * @param isMovement if the panel is related to movement.
     */
    public void bringComponentToTop(final GuiComponent component, final boolean isMovement) {
        //we just re-register it...
        registerComponent(component, isMovement);
    }

    /**
     * Un-register panel from open panels list.
     *
     * @param component  The panel to un-register.
     * @param isMovement if the panel is related to movement.
     */
    public void unRegisterComponent(final GuiComponent component, final boolean isMovement) {
        //remove from list
        final Iterator<GuiComponent> cIter = registeredComponents.iterator();
        while (cIter.hasNext()) {
            final GuiComponent comp = cIter.next();
            if (isMovement) {
                if (comp.getUnitId() == component.getUnitId()
                        && comp.getUnitType() == component.getUnitType()) {
                    cIter.remove();
                    break;
                }
            } else {
                if (comp.getId() == component.getId()) {
                    cIter.remove();
                    break;
                }
            }
        }
    }

    public void unRegisterMovementComponent(final int unitId, final int typeId) {
        //make a lazy instance so it can get unregistered..
        final GuiComponentMovement comp = new GuiComponentMovement(unitId, typeId, null);
        unRegisterComponent(comp, true);
    }

    /**
     * Get the window that user selected last.
     * Has larger z-index.
     *
     * @return The active panel.
     */
    public GuiComponent getActiveComponent() {
        if (!registeredComponents.isEmpty()) {
            return registeredComponents.get(0);
        } else {
            return null;
        }
    }

    public List<GuiComponentBase> getWindowComponents() {
        final List<GuiComponentBase> out = new ArrayList<GuiComponentBase>();
        for (GuiComponent component : registeredComponents) {
            final GuiComponentBase baseComp = (GuiComponentBase) component;
            if (baseComp.getWidget() instanceof DraggablePanel) {
                out.add(baseComp);
            }
        }
        return out;
    }

    public void switchWindows() {
        final List<GuiComponentBase> windowComps = getWindowComponents();
        final GuiComponentBase lastComp = windowComps.get(windowComps.size() - 1);
        bringPanelToTop((DraggablePanel) lastComp.getWidget());
        bringComponentToTop(lastComp, false);
    }

    public void showHideWindows() {
        final List<GuiComponentBase> windowComps = getWindowComponents();
        for (GuiComponentBase component : windowComps) {
            final DraggablePanel panel = (DraggablePanel) component.getWidget();
            if (windowsVisible) {
                panel.hidePanel();
            } else {
                panel.showPanel();
            }
        }
        windowsVisible ^= true;
    }

    public void showCallForAllies(final int call) {
        if (call < callForAllies.size()) {
            new CallAlliesPopup(Integer.parseInt(callForAllies.get(call).split(":")[0]),
                    Integer.parseInt(callForAllies.get(call).split(":")[1]), call);
        }
    }

    public void registerEvent(final DelEventHandler handler) {
        if (!registeredEvents.containsKey(handler.getId())) {
            registeredEvents.put(handler.getId(), new ArrayList<DelEventHandler>());
        }
        registeredEvents.get(handler.getId()).add(handler);

    }

    public void unRegisterEvent(final String id) {
        registeredEvents.remove(id);
    }

    public List<DelEventHandler> getEventHandler(final String id) {
        return registeredEvents.get(id);
    }

    /**
     * Show the political map images.
     *
     * @return true, show the political map images, otherwise just the geo info.
     */
    public boolean isShowPolitical() {
        return showPolitical;
    }

    /**
     * Set the flag to show the political map images or the geographical.
     *
     * @param value true, show the political map images, otherwise just the geo info.
     */
    public void setShowPolitical(final boolean value) {
        this.showPolitical = value;
    }

    /**
     * Get if showing trade cities goods on map.
     *
     * @return A boolean.
     */
    public boolean isShowTradeCities() {
        return showTradeCities;
    }

    /**
     * Set if showing trade cities goods on map.
     *
     * @param value The value to set.
     */
    public void setShowTradeCities(final boolean value) {
        this.showTradeCities = value;
    }

    /**
     * Get true if nation is dead.
     *
     * @return True if nation is dead.
     */
    public boolean isNationDead() {
        return nationDead;
    }

    /**
     * Get if this is the first time the user loads the client for the current turn.
     *
     * @return True if this is the first time the user loads the client for the current turn.
     */
    public boolean isFirstLoad() {
        return firstLoad;
    }

    /**
     * Set if the user loads the client for the first time in the current turn.
     *
     * @param firstLoad The value to set.
     */
    public void setFirstLoad(final boolean firstLoad) {
        this.firstLoad = firstLoad;
    }

    public MiniMapPanel getMiniMapPanel() {
        return miniMapPanel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFogOfWar() {
        return fogOfWar;
    }

    public void setFogOfWar(boolean fogOfWar) {
        this.fogOfWar = fogOfWar;
    }

    /**
     * Get if harsh taxation is allowed in this round.
     *
     * @return A boolean.
     */
    public boolean isAllowHarshTaxation() {
        return allowHarshTaxation;
    }

    public String getScenarioStr() {
        if (scenarioId == -1) {
            return "1804";
        } else if (scenarioId == 1) {
            return "1802";
        } else if (scenarioId == 2) {
            return "1805";
        } else if (scenarioId == 3) {
            return "1808";
        }
        return "1805";
    }

    /**
     * Check if the game has ended.
     *
     * @return true, if the game has ended.
     */
    public boolean isGameEnded() {
        return gameEnded;
    }

    public void selectUnit(final int type, final int id) {
        selectedUnitId = id;
        selectedUnitType = type;
        unitSelected = true;
    }

    public void deselectUnit() {
        unitSelected = false;
    }

    public int getSelectedUnitType() {
        return selectedUnitType;
    }

    public void setSelectedUnitType(int selectedUnitType) {
        this.selectedUnitType = selectedUnitType;
    }

    public int getSelectedUnitId() {
        return selectedUnitId;
    }

    public void setSelectedUnitId(int selectedUnitId) {
        this.selectedUnitId = selectedUnitId;
    }

    public boolean isUnitSelected() {
        return unitSelected;
    }

    public void setUnitSelected(boolean unitSelected) {
        this.unitSelected = unitSelected;
    }

    public void openSelectedUnitInfoPanels() {
        switch (selectedUnitType) {
            case ArmyConstants.ARMY:
                new CorpsViewerPopup(new ArrayList<CorpDTO>(ArmyStore.getInstance().getArmyById(selectedUnitId).getCorps().values()), "Corps", false).open();
                break;
            case ArmyConstants.CORPS:
                new BrigadesViewerPopup(new ArrayList<BrigadeDTO>(ArmyStore.getInstance().getCorpByID(selectedUnitId).getBrigades().values()), "Brigades").open();
                break;
            case ArmyConstants.FLEET:
                new ShipsInFleetViewerPopup(NavyStore.getInstance().getFleetById(selectedUnitId), true, true).open();
                break;
            default:
                //do nothing here
        }
    }

    public int getColoniesMPsModifier() {
        return coloniesMPsModifier;
    }

    public void setColoniesMPsModifier(int coloniesMPsModifier) {
        this.coloniesMPsModifier = coloniesMPsModifier;
    }

    public boolean isFastAssignment() {
        return fastAssignment;
    }

    public void setFastAssignment(boolean fastAssignment) {
        this.fastAssignment = fastAssignment;
    }

    /**
     * Get if costs are doubled for army.
     *
     * @return if costs are doubled for army.
     */
    public boolean isDoubleCostsArmy() {
        return doubleCostsArmy;
    }

    /**
     * Set if costs are doubled for army.
     *
     * @param value if costs are doubled for army.
     */
    public void setDoubleCostsArmy(final boolean value) {
        this.doubleCostsArmy = value;
    }

    /**
     * Get if costs are doubled for naval forces.
     *
     * @return if costs are doubled for naval forces.
     */
    public boolean isDoubleCostsNavy() {
        return doubleCostsNavy;
    }

    /**
     * Set if costs are doubled for naval forces.
     *
     * @param value if costs are doubled for naval forces.
     */
    public void setDoubleCostsNavy(final boolean value) {
        this.doubleCostsNavy = value;
    }

    /**
     * Get if ship construction is fast.
     *
     * @return If ship construction is fast.
     */
    public boolean isFastShipConstruction() {
        return fastShipConstruction;
    }

    /**
     * Get if ship construction is fast.
     *
     * @param value The value to set.
     */
    public void setFastShipConstruction(final boolean value) {
        this.fastShipConstruction = value;
    }

}
