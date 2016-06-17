package com.eaw1805.www.fieldbattle.stores;


import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.google.gwt.user.client.Window;

public class SoundStore {
    private static SoundStore instance = null;

    /**
     * Instance of SoundController object to create sounds.
     */
    private final SoundController soundController = new SoundController();

    /**
     * The base url for the music content.
     */
    private final static String BASE_URL = "http://static.eaw1805.com/music/FieldBattleResults/";




    private SoundStore() {}

    public static SoundStore getInstance() {
        if (instance == null) {
            instance = new SoundStore();
        }
        return instance;
    }

    public void playVictorySong() {
        playSound(BASE_URL + "Fangs_VICTORY.ogg");
    }

    public void playDefeatSong() {
        playSound(BASE_URL + "Across_The_World_DEFEAT.ogg");
    }

    public void playDrawSong() {
        playSound(BASE_URL + "Dethrone_The_King_DRAW.ogg");

    }


    /**
     * Create the sound object and play the song from the give url
     *
     * @param soundUrl The url to the sound.
     */
    public void playSound(final String soundUrl) {
        try {
                final Sound sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_OGG_VORBIS, soundUrl);
                sound.play();
        } catch (Exception e) {
            Window.alert("Failed to load sound : " + e.toString());
        }
    }

}
