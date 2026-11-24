# Game Settings and Configuration

This project uses Java 17+ with libGDX 1.14.2. Runtime settings are available in-game, and balance values are code-based.

## In-game settings

Press **S** on the main menu, or click **SETTINGS**, to open the settings screen.

| Setting | Key | Persistence |
| --- | --- | --- |
| Sound effects on/off | `S` | Saved in libGDX preferences |
| Fullscreen on/off | `F` | Saved in libGDX preferences |
| Back to menu | `ESC` / `ENTER` | — |

Personal best score is also saved in libGDX preferences and shown on the main menu and round summary.

## Runtime settings

| Setting | Value | Source |
| --- | --- | --- |
| Logical resolution | 1280 x 720 | `GameConfig.VIEW_WIDTH` / `VIEW_HEIGHT` |
| Scaling | `FitViewport` (letterboxed, keeps 16:9) | `BaseScreen` |
| Target frame rate | 60 FPS, V-sync on | `DesktopLauncher` |
| Desktop window | 1280 x 720, not resizable | `DesktopLauncher` |
| Pause | `Esc` toggles; `R` restarts while paused | `PlayScreen` |

## Gameplay tuning

All balance values live in `core/src/main/java/kwiso/bando/config/GameConfig.java`.

| Constant | Value | Effect |
| --- | --- | --- |
| `HIDE_DURATION` | 7 s | Hiding countdown |
| `SEARCH_DURATION` | 60 s | Round timer |
| `BASE_SPEED` | 210 px/s | Player walking speed |
| `SPRINT_MULTIPLIER` | 1.6 | Sprint speed multiplier |
| `SNEAK_MULTIPLIER` | 0.6 | Sneak speed multiplier |
| `SEEKER_PATROL_SPEED` | 130 px/s | Patrol speed |
| `SEEKER_RACE_MULTIPLIER` | 0.95 | Seeker sprint vs player sprint |
| `STAMINA_MAX` | 100 | Stamina pool |
| `STAMINA_DRAIN` | 26 /s | Sprint drain |
| `STAMINA_REGEN` | 16 /s | Regeneration |
| `DETECTION_TIME` | 0.5 s | Time in view before alert |
| `VISION_RANGE` | 380 px | Seeker sight distance |
| `VISION_HALF_ANGLE_DEG` | 45 deg | Half of the 90 degree cone |
| `VISION_RAYS` | 48 | Rays used to draw/clip the cone |
| `DASH_MULTIPLIER` | 2.2 | Dash speed multiplier |
| `DASH_COST` | 20 | Stamina cost per dash |
| `DASH_DURATION` | 0.22 s | Dash duration |
| `DASH_COOLDOWN` | 1.2 s | Minimum time between dashes |
| `SPRINT_NOISE` | 150 px | Noise radius while sprinting |
| `WALK_NOISE` | 55 px | Noise radius while walking |
| `DASH_NOISE` | 190 px | Noise radius while dashing |
| `INVESTIGATE_DURATION` | 5 s | How long the Seeker follows a sound |

Change a value, rebuild, and the whole round uses the new balance.

## Audio

Short synthesized WAV effects are bundled in `core/src/main/resources/assets/audio/` and loaded by `AudioManager`.

Implemented effects: `kwiso`, `bando`, `tap`, `dash`, `pebble`, `whistle`, `spotted`, `step`, `win`, `lose`.

Voice callouts, music, and volume sliders are not implemented; the settings screen currently provides a single sound on/off toggle.

## Graphics and accessibility

- Graybox `ShapeRenderer` visuals; no texture pack or sprite atlas yet.
- Default libGDX font; no bundled Outfit/Chivo/Space Grotesk fonts yet.
- Key bindings are fixed in `PlayScreen.keyDown`/`keyUp`; remapping is not implemented.
- Fullscreen can be toggled in the in-game settings screen.

## Build and test commands

```powershell
cd kwiso-bando-libgdx
.\gradlew.bat desktop:run
.\gradlew.bat core:test
.\gradlew.bat desktop:fatJar
```

The runnable JAR is written to `desktop/build/libs/`.
