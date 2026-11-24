package kwiso.bando.ui;

import java.util.Locale;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import kwiso.bando.config.GameConfig;
import kwiso.bando.config.Palette;
import kwiso.bando.config.RoundPhase;
import kwiso.bando.entities.HiderPlayer;
import kwiso.bando.entities.SeekerAI;

public class HudRenderer {
    public void render(SpriteBatch batch, BitmapFont font, ShapeRenderer shapes,
                       RoundPhase phase, float timer, HiderPlayer player, SeekerAI seeker) {
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        panel(shapes, 24f, 628f, 260f, 68f);
        panel(shapes, 490f, 620f, 300f, 76f);
        panel(shapes, 24f, 24f, 280f, 64f);
        panel(shapes, 410f, 24f, 460f, 54f);

        shapes.setColor(Palette.TIMBER_LIGHT);
        shapes.rect(40f, 640f, 228f, 14f);
        shapes.setColor(player.getStamina() < 25f ? Palette.PRIMARY : Palette.SECONDARY);
        shapes.rect(40f, 640f, 228f * player.getStamina() / GameConfig.STAMINA_MAX, 14f);

        float noise = 0f;
        if (player.isSprinting()) {
            noise = 3f;
        } else if (!player.isSneaking()) {
            noise = 2f;
        }
        for (int i = 0; i < 4; i++) {
            shapes.setColor(i < noise ? Palette.OCHRE : Palette.TIMBER_LIGHT);
            shapes.rect(150f + i * 14f, 44f, 9f, 18f + i * 4f);
        }

        if (seeker.getDetectionProgress() > 0f) {
            shapes.setColor(Palette.ALERT);
            shapes.rect(510f, 566f, 260f, 38f);
        }
        shapes.end();

        batch.begin();
        font.setColor(Palette.PARCHMENT);
        font.draw(batch, "STAMINA", 40f, 682f);
        font.draw(batch, String.format(Locale.US, "%3.0f/100", player.getStamina()), 176f, 682f);
        font.draw(batch, phaseName(phase), 508f, 682f);
        font.setColor(Palette.OCHRE);
        font.draw(batch, String.format(Locale.US, "%02d:%02d",
            (int) Math.floor(Math.max(0f, timer) / 60f),
            (int) (Math.max(0f, timer) % 60f)), 542f, 638f);
        font.setColor(Palette.PARCHMENT);
        font.draw(batch, "Objective: touch Home Base", 502f, 606f);
        font.setColor(Palette.MUTED);
        font.draw(batch, "STEALTH", 40f, 66f);
        font.draw(batch, player.isSneaking() ? "SILENT" : (player.isSprinting() ? "LOUD" : "LOW"), 40f, 42f);
        font.draw(batch, "WASD move   SHIFT sprint   CTRL sneak   SPACE tap base   ESC pause", 426f, 46f);

        if (seeker.getDetectionProgress() > 0f) {
            font.setColor(Palette.PARCHMENT);
            font.draw(batch, "! SPOTTED", 562f, 592f);
        }
        batch.end();
    }

    private String phaseName(RoundPhase phase) {
        switch (phase) {
            case HIDING:
                return "HIDE! KWISO / BANDO";
            case SEARCHING:
                return "SEARCHING";
            case RACE:
                return "RACE TO BASE!";
            default:
                return "ROUND OVER";
        }
    }

    private void panel(ShapeRenderer shapes, float x, float y, float width, float height) {
        shapes.setColor(Palette.DEEP);
        shapes.rect(x, y - 5f, width, height);
        shapes.setColor(Palette.TIMBER);
        shapes.rect(x, y, width, height);
    }
}
