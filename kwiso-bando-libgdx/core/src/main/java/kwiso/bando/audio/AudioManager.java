package kwiso.bando.audio;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    private final Map<String, Float> durations = new HashMap<String, Float>();
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
                String path = "assets/audio/" + name + ".wav";
                sounds.put(name, Gdx.audio.newSound(Gdx.files.internal(path)));
                durations.put(name, readWavDuration(path));
            } catch (Exception exception) {
                Gdx.app.log("AudioManager", "Missing sound: " + name);
            }
        }
    }

    public long play(String name, float volume) {
        if (!GameSettings.isSoundEnabled()) {
            return -1L;
        }
        Sound sound = sounds.get(name);
        if (sound != null) {
            return sound.play(MathUtils.clamp(volume, 0f, 1f));
        }
        return -1L;
    }

    public void stop(String name) {
        Sound sound = sounds.get(name);
        if (sound != null) {
            sound.stop();
        }
    }

    public float duration(String name) {
        Float duration = durations.get(name);
        return duration == null ? 0.5f : duration;
    }

    private float readWavDuration(String path) throws IOException {
        DataInputStream in = new DataInputStream(Gdx.files.internal(path).read());
        try {
            byte[] riff = new byte[4];
            byte[] wave = new byte[4];
            in.readFully(riff);
            readLittleEndianInt(in);
            in.readFully(wave);
            if (!new String(riff, StandardCharsets.US_ASCII).equals("RIFF")
                || !new String(wave, StandardCharsets.US_ASCII).equals("WAVE")) {
                throw new IOException("Not a WAV file: " + path);
            }
            int byteRate = 0;
            int dataSize = 0;
            while (in.available() > 0) {
                byte[] idBytes = new byte[4];
                if (in.read(idBytes) != 4) {
                    break;
                }
                int size = readLittleEndianInt(in);
                String id = new String(idBytes, StandardCharsets.US_ASCII);
                if (id.equals("fmt ")) {
                    byte[] fmt = new byte[size];
                    in.readFully(fmt);
                    byteRate = littleEndianInt(fmt, 8);
                } else if (id.equals("data")) {
                    dataSize = size;
                    break;
                } else {
                    skipFully(in, size + (size & 1));
                }
            }
            if (byteRate > 0 && dataSize > 0) {
                return (float) dataSize / byteRate;
            }
        } finally {
            in.close();
        }
        return 0.5f;
    }

    private int readLittleEndianInt(DataInputStream in) throws IOException {
        byte[] bytes = new byte[4];
        in.readFully(bytes);
        return littleEndianInt(bytes, 0);
    }

    private int littleEndianInt(byte[] bytes, int offset) {
        return (bytes[offset] & 0xff)
            | ((bytes[offset + 1] & 0xff) << 8)
            | ((bytes[offset + 2] & 0xff) << 16)
            | ((bytes[offset + 3] & 0xff) << 24);
    }

    private void skipFully(DataInputStream in, int count) throws IOException {
        int remaining = count;
        while (remaining > 0) {
            int skipped = in.skipBytes(remaining);
            if (skipped > 0) {
                remaining -= skipped;
            } else if (in.read() == -1) {
                throw new IOException("Unexpected end of WAV data");
            } else {
                remaining--;
            }
        }
    }

    public void dispose() {
        for (Sound sound : sounds.values()) {
            sound.dispose();
        }
        sounds.clear();
        durations.clear();
        initialized = false;
    }
}
