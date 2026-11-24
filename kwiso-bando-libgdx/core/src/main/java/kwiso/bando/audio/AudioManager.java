package kwiso.bando.audio;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

import kwiso.bando.config.GameSettings;

public final class AudioManager {
    public static final String KWISO = "kwiso";
    public static final String BANDO = "bando";
    public static final String TAP = "tap";
    public static final String DASH = "dash";
    public static final String PEBBLE = "pebble";
    public static final String WHISTLE = "whistle";
    public static final String SPOTTED = "spotted";
    public static final String STEP = "step";
    public static final String WIN = "win";
    public static final String LOSE = "lose";

    private static final AudioManager INSTANCE = new AudioManager();
    private final Map<String, Sound> sounds = new HashMap<String, Sound>();
    private boolean initialized;

    private AudioManager() {
    }

    public static AudioManager get() {
        return INSTANCE;
    }

    public void init() {
        if (initialized) {
            return;
        }
        initialized = true;
        String[] names = {KWISO, BANDO, TAP, DASH, PEBBLE, WHISTLE, SPOTTED, STEP, WIN, LOSE};
        for (String name : names) {
            try {
                sounds.put(name, Gdx.audio.newSound(
                    Gdx.files.internal("assets/audio/" + name + ".wav")));
            } catch (Exception exception) {
                Gdx.app.log("AudioManager", "Missing sound: " + name);
            }
        }
    }

    public void play(String name, float volume) {
        if (!GameSettings.isSoundEnabled()) {
            return;
        }
        Sound sound = sounds.get(name);
        if (sound != null) {
            sound.play(MathUtils.clamp(volume, 0f, 1f));
        }
    }

    public void dispose() {
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
        initialized = false;
    }
}
