package empire.webapp.shared.stores;


import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.allen_sauer.gwt.voices.client.handler.PlaybackCompleteEvent;
import com.allen_sauer.gwt.voices.client.handler.SoundHandler;
import com.allen_sauer.gwt.voices.client.handler.SoundLoadStateChangeEvent;
import com.google.gwt.user.client.Timer;
import empire.data.constants.ArmyConstants;
import empire.data.dto.web.army.BarrackDTO;
import empire.webapp.client.views.BarrackShipYardView;
import empire.webapp.client.views.TaxationView;
import empire.webapp.client.views.TradePanelView;
import empire.webapp.client.views.military.deployment.UnloadTroopsView;
import empire.webapp.client.widgets.ErrorPopup;
import empire.webapp.client.widgets.WindowPanelEAW;
import empire.webapp.shared.stores.economy.ProductionSiteStore;
import empire.webapp.shared.stores.units.ArmyStore;
import empire.webapp.shared.stores.util.ArmyUnitInfoDTO;
import empire.webapp.shared.stores.util.calculators.MiscCalculators;

public final class SoundStore implements ArmyConstants {

    /**
     * Static instance of sound store for our singleton class.
     */
    private static transient SoundStore ourInstance = null;

    /**
     * The base url for the music content.
     */
    private final static String BASE_URL = "http://static.eaw1805.com/music/";

    /**
     * Sub path for the intro.
     */
    private final static String INTRO = "intro/";

    /**
     * Sub path for the sound effects.
     */
    private final static String SOUND_EFFECTS = "Sound effects/";

    /**
     * Sub path for the battle results.
     */
    private final static String BATTLE_RESULTS = "Battle Results/";

    /**
     * Instance of SoundController object to create sounds.
     */
    private final SoundController soundController = new SoundController();

    /**
     * The delay between plays of the intro song.
     */
    private final static int INTRO_DELAY_BETWEEN_PLAYS = 60000 * 20;

    /**
     * Timer to play the intro song after a predefined period of time.
     */
    private final Timer t;

    /**
     * The volume of intro song.
     */
    private int mainMusicVolume = 100;

    /**
     * The sound object that plays the intro music.
     */
    private final Sound music;

    /**
     * Method returning the game store
     *
     * @return the GameStore
     */
    public static SoundStore getInstance() {
        if (ourInstance == null) {
            ourInstance = new SoundStore();
        }
        return ourInstance;
    }

    private SoundStore() {


        t = new Timer() {
            @Override
            public void run() {
                if (GameStore.getInstance().isPlayMusic() && !GameStore.getInstance().isMobileDevice()) {
                    try {
                        if (music != null) {
                            music.setVolume(mainMusicVolume);
                            music.play();
                        }
                    } catch (Exception e) {
                        new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to load sound", false);
                    }
                }
            }
        };
        if (!GameStore.getInstance().isMobileDevice()) {
            music = soundController.createSound(Sound.MIME_TYPE_AUDIO_OGG_VORBIS, BASE_URL + INTRO + "EaW1805_theme.ogg");
            music.addEventHandler(new SoundHandler() {

                public void onPlaybackComplete(final PlaybackCompleteEvent event) {
                    mainMusicVolume = 60;
                    t.schedule(INTRO_DELAY_BETWEEN_PLAYS);
                }

                public void onSoundLoadStateChange(final SoundLoadStateChangeEvent event) {
                    //do nothing here.
                }
            });
        } else {
            music = null;
        }

    }

    /**
     * Depends on the window that is opening to the client play a song.
     *
     * @param panel The current opening window
     */
    public void playWindowOpenSound(final WindowPanelEAW panel) {
        if (panel instanceof BarrackShipYardView) {
            final BarrackDTO barrack = ((BarrackShipYardView) panel).getThisBarrack();
            if (ProductionSiteStore.getInstance().isTileNeighborWithSeaById(barrack.getX(), barrack.getY(), barrack.getRegionId())) {
                playShipyard();
            } else {

                playBarrack();
            }

        } else if (panel instanceof TradePanelView) {
            playOpenTradePanel();

        } else if (panel instanceof TaxationView) {
            playChangeTaxation();

        } else if (panel instanceof UnloadTroopsView) {
            playDisembarkTroops();
        }
    }

    /**
     * Depends on the unit that moves play a song.
     *
     * @param type The type of the unit.
     */
    public void playMoveSound(final int type, final int unitId) {
        ArmyUnitInfoDTO info = null;
        switch (type) {
            case COMMANDER:
                playMoveCommander();
                break;

            case SHIP:
            case FLEET:
                playMoveFleet();
                break;

            case ARMY:
                info = MiscCalculators.getArmyInfo(ArmyStore.getInstance().getArmyById(unitId));

            case CORPS:
                if (info == null) {
                    info = MiscCalculators.getCorpInfo(ArmyStore.getInstance().getCorpByID(unitId));
                }

            case BRIGADE:
                if (info == null) {
                    info = MiscCalculators.getBrigadeInfo(ArmyStore.getInstance().getBrigadeById(unitId));
                }
                if (info.getCavalry() > info.getInfantry()) {
                    playMoveCavalry();
                } else {
                    playMoveInfantry();
                }
                break;

            default:
                playMoveScout();
        }
    }


    /**
     * This function initializes the timer and schedules it to play the intro imidiately.
     */
    public void initIntro() {
        try {
            t.run();
        } catch (Exception e) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "could not play sound", false);
        }
    }

    /**
     * Stops the music of the game.
     */
    public void stopIntro() {
        t.cancel();
        if (music != null) {
            music.stop();
        }

    }

    /**
     * Play the barrack song.
     */
    public void playBarrack() {
        playSound(BASE_URL + SOUND_EFFECTS + "barrack.ogg");
    }

    /**
     * Play the artillery song.
     */
    public void playArtillery() {
        playSound(BASE_URL + SOUND_EFFECTS + "artillery.ogg");
    }

    /**
     * Play the battle land song.
     */
    public void playBattleLand() {
        playSound(BASE_URL + SOUND_EFFECTS + "battle_land.ogg");
    }

    /**
     * Play the battle sea song.
     */
    public void playBattleSea() {
        playSound(BASE_URL + SOUND_EFFECTS + "battle_sea.ogg");
    }

    /**
     * Play the buy sell song.
     */
    public void playBuySell() {
        playSound(BASE_URL + SOUND_EFFECTS + "buy_sell_button.ogg");
    }

    /**
     * Play the change panel 1 song.
     */
    public void playChangePanel1() {
        playSound(BASE_URL + SOUND_EFFECTS + "change_panel_1.ogg");
    }

    /**
     * Play the change panel 2 song.
     */
    public void playChangePanel2() {
        playSound(BASE_URL + SOUND_EFFECTS + "change_panel_2.ogg");
    }

    /**
     * Play the change taxation song.
     */
    public void playChangeTaxation() {
        playSound(BASE_URL + SOUND_EFFECTS + "change_taxation.ogg");
    }

    /**
     * Play the click glass song.
     */
    public void playClickGlass() {
        playSound(BASE_URL + SOUND_EFFECTS + "click_glass.ogg");
    }

    /**
     * Play the click wooden song.
     */
    public void playClickWooden() {
        playSound(BASE_URL + SOUND_EFFECTS + "click_wooden.ogg");
    }

    /**
     * Play the diplomacy room song.
     */
    public void playDiplomacyRoom() {
        playSound(BASE_URL + SOUND_EFFECTS + "diplomacy_room.ogg");
    }

    /**
     * Play the disembark song.
     */
    public void playDisembarkTroops() {
        playSound(BASE_URL + SOUND_EFFECTS + "disembark_troops.ogg");
    }

    /**
     * Play the enter panel song.
     */
    public void playEnterPanel() {
        playSound(BASE_URL + SOUND_EFFECTS + "enter_panel.ogg");
    }

    /**
     * Play the move cavalry song.
     */
    public void playMoveCavalry() {
        playSound(BASE_URL + SOUND_EFFECTS + "move_cavalry.ogg");
    }

    /**
     * Play the move commander song.
     */
    public void playMoveCommander() {
        playSound(BASE_URL + SOUND_EFFECTS + "move_commander.ogg");
    }

    /**
     * Play the move fleet song.
     */
    public void playMoveFleet() {
        playSound(BASE_URL + SOUND_EFFECTS + "move_fleet.ogg");
    }

    /**
     * Play the move infantry song.
     */
    public void playMoveInfantry() {
        playSound(BASE_URL + SOUND_EFFECTS + "move_infantry.ogg");
    }

    /**
     * Play the move scout song.
     */
    public void playMoveScout() {
        playSound(BASE_URL + SOUND_EFFECTS + "move_scout.ogg");
    }

    /**
     * Play the open trade panel song.
     */
    public void playOpenTradePanel() {
        playSound(BASE_URL + SOUND_EFFECTS + "open_trade_panel.ogg");
    }

    /**
     * Play the production site song.
     */
    public void playProductionSite() {
        playSound(BASE_URL + SOUND_EFFECTS + "production_site.ogg");
    }

    /**
     * Play the scroll 1 song.
     */
    public void playScroll1() {
        playSound(BASE_URL + SOUND_EFFECTS + "scroll_1.ogg");
    }

    /**
     * Play the scroll 2 song.
     */
    public void playScroll2() {
        playSound(BASE_URL + SOUND_EFFECTS + "scroll_2.ogg");
    }

    /**
     * Play the send diplo song.
     */
    public void playSendDiplo() {
        playSound(BASE_URL + SOUND_EFFECTS + "send_diplo.ogg");
    }

    /**
     * Play the shipyard song.
     */
    public void playShipyard() {
        playSound(BASE_URL + SOUND_EFFECTS + "shipyard.ogg");
    }

    /**
     * Play the defeat song.
     */
    public void playDefeat() {
        playSound(BASE_URL + BATTLE_RESULTS + "Defeat.ogg");
    }

    /**
     * Play the draw song.
     */
    public void playDraw() {
        playSound(BASE_URL + BATTLE_RESULTS + "Draw.ogg");
    }

    /**
     * Play the victory song.
     */
    public void playVictory() {
        playSound(BASE_URL + BATTLE_RESULTS + "Victory.ogg");
    }

    /**
     * Create the sound object and play the song from the give url
     *
     * @param soundUrl The url to the sound.
     */
    public void playSound(final String soundUrl) {
        try {
            if (GameStore.getInstance().isPlaySoundEffects() && !GameStore.getInstance().isMobileDevice()) {
                final Sound sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_OGG_VORBIS, soundUrl);
                sound.play();
            }
        } catch (Exception e) {
            new ErrorPopup(ErrorPopup.Level.WARNING, "Failed to load sound", false);
        }
    }

}
