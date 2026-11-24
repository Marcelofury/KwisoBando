# Kwiso Bando: The Base Race — How to Play

Kwiso Bando (also called Ajeri Bado) is a Ugandan playground game. This digital version turns it into a top-down stealth-and-sprint arcade round.

## Goal

Hide from the Seeker, stay out of the vision cone, and touch the green **Home Base** before the Seeker gets there.

## Quick start

1. Run the game on desktop.
2. Press **ENTER** (or click **PLAY GAME**) on the title screen.
3. Press **H** on the title screen for the in-game rules, or **S** for settings.
4. Press **ESC** during play to pause; press **R** while paused to restart the round.

## Controls

| Key | Action |
| --- | --- |
| `W` `A` `S` `D` / Arrow keys | Move in 8 directions |
| `Shift` | Sprint: 1.6x speed, drains stamina, makes noise |
| `Ctrl` | Sneak: 0.6x speed, silent |
| `Space` | Tap the Home Base when close; otherwise dash |
| `Q` | Throw a pebble to create a distracting noise (3 per round) |
| `E` | Blow a whistle decoy (8 second cooldown) |
| `Esc` | Pause / resume |
| `R` | Restart round while paused |

## Round phases

1. **HIDE! (7 seconds)** — The Seeker faces the base and cannot see. Sprint behind a wall, hut, tree, or bush before the countdown ends. The Seeker calls "Kwiso!" and hiders answer "Bando!".
2. **SEARCHING (60 seconds)** — The Seeker patrols with a cone-shaped view. Walls, trees, and huts block line of sight. Stay outside the cone, or sneak past while its back is turned. If you sprint or dash too close, the Seeker investigates the noise.
3. **RACE TO BASE** — If the Seeker sees you for half a second, it is spotted. Both racers sprint to the Home Base; you win if you touch it first.

You can also win during SEARCHING by sneaking all the way to the base unseen.

## Winning and losing

- **Win:** you touch the Home Base first.
- **Loss:** the Seeker touches the base first, or the 60-second round timer expires.

## Score and rank

The round summary shows elapsed time, score, rank, and a saved personal best.

- Faster wins earn a larger time bonus.
- Winning without being spotted adds a stealth bonus.
- Ranks are `TRAINEE`, `ROOKIE`, `SCOUT`, `ACE`, and `LEGEND`.

## Tips

- Hiding close to the base is risky but gives a short sprint.
- Deep cover is safer, but save stamina for the run back.
- Walking makes a small noise ring; sprinting makes a large one; dashing is loudest. Sneaking makes none.
- The vision cone is clipped by solid obstacles, so hug walls and tree cover.
- Use `Q` and `E` to pull the Seeker away from your route; a `!` over the Seeker means it is investigating a sound.

## Current build status

The game is a playable graybox prototype: core rules, timers, movement, stamina, collisions, line of sight, Seeker AI, noise investigation, HUD, synthesized sound effects, settings, scoring, and round summary are implemented. Sprites, bundled fonts/icons, voice callouts, and full visual polish are still pending.
