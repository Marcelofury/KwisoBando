# Kwiso Bando: The Base Race

A desktop stealth-and-sprint game inspired by the Ugandan playground game **Kwiso Bando** (also called **Ajeri Bado**). The player hides from a patrolling Seeker, avoids its vision cone, creates noise distractions, and races to the Home Base before being spotted or beaten to the base.

**Status:** playable graybox prototype with a complete round loop, audio effects, settings, scoring, and automated tests.

---

## Table of Contents

- [Highlights](#highlights)
- [Technology](#technology)
- [Repository Layout](#repository-layout)
- [Quick Start](#quick-start)
- [Controls](#controls)
- [Game Loop](#game-loop)
- [Architecture](#architecture)
- [Configuration](#configuration)
- [Documentation](#documentation)
- [Verification](#verification)
- [Roadmap](#roadmap)
- [Development Notes](#development-notes)

---

## Highlights

- Top-down 1280×720 desktop gameplay built with libGDX and LWJGL3.
- Three-phase rounds: **HIDE**, **SEARCH**, and **RACE TO BASE**.
- Smooth 8-direction movement with sprint stamina, silent sneak, and a stamina-cost dash.
- Sliding circle-versus-AABB collision against walls, trees, bushes, crates, and urns.
- Raycast vision cone clipped by obstacles, with line-of-sight detection.
- Seeker AI with waypoint patrol, detection timer, race behavior, and noise investigation.
- Decoy items: throw a pebble with `Q` or blow a whistle with `E`.
- HUD showing phase, timer, stamina, noise level, items, and detection state.
- Menu, in-game rules, settings, pause overlay, and round summary screens.
- Ten bundled synthesized WAV sound effects with a sound on/off toggle.
- Fullscreen toggle, saved preferences, score, rank, and personal best persisted with libGDX `Preferences`.
- Headless JUnit 5 tests for vision, collision, scoring, and AI behavior.

---

## Technology

| Area | Choice |
| --- | --- |
| Language | Java, compiled with `options.release = 17` |
| Game framework | libGDX `1.14.2` |
| Desktop backend | LWJGL3 |
| Build system | Gradle with wrapper pinned to `9.7.1` |
| IDE support | IntelliJ IDEA / Eclipse Gradle projects |
| Rendering | `ShapeRenderer`, `SpriteBatch`, `BitmapFont`, `FitViewport` |
| Audio | libGDX `Sound` with bundled WAV files |
| Persistence | libGDX `Preferences` |
| Tests | JUnit 5 |

No system Gradle installation is required; use the included wrapper scripts.

---

## Repository Layout

```text
C:\Users\USER\kwisoBando
├── Kwiso_Bando_SRS_and_Project_Plan.docx
├── Reverse_Engineering_and_From_Scratch_Build_Guide.docx
├── HOW_TO_PLAY.md                         (inside kwiso-bando-libgdx)
├── GAME_SETTINGS.md                       (inside kwiso-bando-libgdx)
├── screen.png / screen1.png / screen2.png / screen4.png
└── kwiso-bando-libgdx
    ├── build.gradle
    ├── settings.gradle
    ├── gradlew.bat
    ├── gradle/wrapper/
    ├── core
    │   ├── build.gradle
    │   └── src
    │       ├── main/java/kwiso/bando
    │       │   ├── audio/
    │       │   ├── config/
    │       │   ├── entities/
    │       │   ├── screens/
    │       │   ├── systems/
    │       │   ├── ui/
    │       │   └── KwisoBandoGame.java
    │       ├── main/resources/assets/audio/
    │       └── test/java/kwiso/bando/
    └── desktop
        ├── build.gradle
        └── src/main/java/kwiso/bando/desktop/DesktopLauncher.java
```

---

## Quick Start

### Prerequisites

- JDK 17 or newer. The project compiles with Java 17 release settings and was verified on JDK 25.
- Windows PowerShell is assumed in the commands below. On macOS/Linux, use `./gradlew` instead of `.\gradlew.bat`.

### Run the game

```powershell
cd kwiso-bando-libgdx
.\gradlew.bat desktop:run
```

### Build everything

```powershell
.\gradlew.bat desktop:build
```

### Run the tests

```powershell
.\gradlew.bat core:test
```

### Build a distributable JAR

```powershell
.\gradlew.bat desktop:fatJar
```

The runnable JAR is written to:

```text
kwiso-bando-libgdx\desktop\build\libs\kwiso-bando-1.0.0-desktop.jar
```

Run it with:

```powershell
java -jar desktop\build\libs\kwiso-bando-1.0.0-desktop.jar
```

---

## Controls

| Key | Action |
| --- | --- |
| `W` `A` `S` `D` / Arrow keys | Move in 8 directions |
| `Shift` | Sprint; faster, drains stamina, creates noise |
| `Ctrl` | Sneak; slower, silent |
| `Space` | Tap the Home Base when close; otherwise dash |
| `Q` | Throw a pebble decoy, 3 per round |
| `E` | Blow a whistle decoy, 8 second cooldown |
| `Esc` | Pause or resume |
| `R` | Restart while paused |
| `S` on menu | Open settings |
| `H` on menu | Open how-to-play |

Additional settings keys: `S` toggles sound, `F` toggles fullscreen, and `Esc` returns to the menu.

---

## Game Loop

| Phase | Duration | Player goal | Seeker behavior |
| --- | --- | --- | --- |
| `HIDING` | 7 seconds | Sprint behind cover before the countdown ends | Frozen, facing the base |
| `SEARCHING` | 60 seconds | Stay unseen or sneak to the base | Patrols, detects with cone, investigates noise |
| `RACE` | Until base contact or timeout | Reach the Home Base first | Sprints directly to the base |
| `FINISHED` | Until screen transition | Review score, replay, or exit | Frozen |

### Scoring

```text
base        = WIN ? 800 : 150
time_bonus  = max(0, round((60 - elapsed) * 12))
stealth     = WIN && !spotted ? 350 : 0
score       = base + time_bonus + stealth
```

Ranks: `TRAINEE`, `ROOKIE`, `SCOUT`, `ACE`, and `LEGEND`.

---

## Architecture

| Package | Responsibility |
| --- | --- |
| `kwiso.bando` | Game lifecycle, startup, settings/audio initialization, disposal |
| `config` | Tuning constants, palette, persisted preferences, phase and result types |
| `entities` | Player, Seeker AI, Home Base, and obstacles |
| `systems` | Input, sliding collision, vision raycasting, noise events, scoring |
| `screens` | Menu, rules, settings, gameplay, pause overlay, and round summary |
| `ui` | HUD rendering |
| `audio` | Loading and playback of bundled sound effects |
| `desktop` | LWJGL3 launcher and window configuration |

The design keeps rules in small deterministic systems, screens thin, and all balance values in `GameConfig`.

---

## Configuration

Gameplay tuning lives in:

```text
kwiso-bando-libgdx\core\src\main\java\kwiso\bando\config\GameConfig.java
```

Runtime settings are available in-game from the menu:

- Sound on/off
- Fullscreen on/off
- Personal best score, shown on the menu and summary screen

Build-level settings live in:

- `build.gradle` — Java release, UTF-8 encoding, libGDX version
- `core/build.gradle` — core dependency and JUnit 5
- `desktop/build.gradle` — `run` and `fatJar` tasks
- `gradle/wrapper/gradle-wrapper.properties` — wrapper version

See [GAME_SETTINGS.md](kwiso-bando-libgdx/GAME_SETTINGS.md) for the full reference.

---

## Documentation

| Document | Purpose |
| --- | --- |
| [HOW_TO_PLAY.md](kwiso-bando-libgdx/HOW_TO_PLAY.md) | Player-facing rules, controls, tips, scoring, and build status |
| [GAME_SETTINGS.md](kwiso-bando-libgdx/GAME_SETTINGS.md) | Runtime settings, tuning constants, audio, graphics, and commands |
| [Reverse_Engineering_and_From_Scratch_Build_Guide.docx](Reverse_Engineering_and_From_Scratch_Build_Guide.docx) | How to rebuild from a libGDX generator skeleton, how the implementation was done, and why packages were used or avoided |
| [Kwiso_Bando_SRS_and_Project_Plan.docx](Kwiso_Bando_SRS_and_Project_Plan.docx) | Original requirements and project plan |
| [Reference screenshots](screen.png) | Original visual references: [screen1.png](screen1.png), [screen2.png](screen2.png), [screen4.png](screen4.png) |

---

## Verification

The following checks pass in the current workspace:

```powershell
.\gradlew.bat desktop:build
.\gradlew.bat core:test
.\gradlew.bat desktop:fatJar
java -jar desktop\build\libs\kwiso-bando-1.0.0-desktop.jar
```

The JAR smoke test launches successfully. LWJGL/Java 25 warnings may appear but are harmless; the compiled target remains Java 17.

---

## Roadmap

- Replace graybox `ShapeRenderer` art with a sprite atlas and animated actors.
- Bundle display/body fonts and icon art from the visual references.
- Replace synthesized KWISO/BANDO tones with voice callouts.
- Add simple pathfinding and post-loss search behavior for the Seeker.
- Add separate music/SFX volume sliders and key rebinding.
- Expand tests to cover phase transitions and additional collision edge cases.
- Add a UML class diagram and a short developer onboarding page.

---

## Development Notes

- Keep gameplay constants in `GameConfig` rather than scattering magic numbers.
- Add logic tests under `core/src/test`; keep them headless and independent of OpenGL.
- Prefer small, descriptive commits such as `Add raycast vision cone line of sight`.
- Run `desktop:build` and `core:test` before opening a pull request.
- The project currently has no license file; add one before public distribution.

---

**Main branch:** `main`  
**Remote:** `https://github.com/Marcelofury/KwisoBando.git`
