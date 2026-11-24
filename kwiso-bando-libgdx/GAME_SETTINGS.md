# Game Settings and Configuration

This project uses Java 17+ with libGDX 1.14.2. Settings are currently code-based; there is no in-game settings screen yet.

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

Change a value, rebuild, and the whole round uses the new balance.

## Audio

Not implemented yet. Planned: master volume toggle in the menu and gameplay HUD, "Kwiso!" / "Bando!" voice callouts, footsteps, alert whistle, victory chime, and tag buzzer.

## Graphics and accessibility

- Graybox `ShapeRenderer` visuals; no texture pack or sprite atlas yet.
- Default libGDX font; no bundled Outfit/Chivo/Space Grotesk fonts yet.
- Key bindings are fixed in `PlayScreen.keyDown`/`keyUp`; remapping is not implemented.
- Fullscreen mode is not implemented.

## Build and run commands

```powershell
cd kwiso-bando-libgdx
.\gradlew.bat desktop:run
.\gradlew.bat desktop:fatJar
```

The runnable JAR is written to `desktop/build/libs/`.

## Planned settings screen

A future update will add a dedicated settings screen with sound on/off, volume sliders, fullscreen toggle, and key rebinding, replacing the current code-only configuration.
