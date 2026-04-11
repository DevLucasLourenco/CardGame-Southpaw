package game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * Builds a programmatic LibGDX Skin — no external .json/.atlas files required.
 */
public final class UIFactory {

    private UIFactory() {}

    public static Skin createSkin() {
        Skin skin = new Skin();

        BitmapFont font = new BitmapFont(); // built-in default font (no external file)
        skin.add("default-font", font);
        skin.add("default", font); // also register as "default" for getFont("default") calls

        // Single white pixel used as the base for all tinted drawables
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        skin.add("white", new Texture(pm));
        pm.dispose();

        // ── Label ──────────────────────────────────────────────────
        skin.add("default", new Label.LabelStyle(font, Color.WHITE));
        skin.add("title",   new Label.LabelStyle(font, new Color(0.95f, 0.88f, 0.35f, 1f)));

        // ── TextButton ────────────────────────────────────────────
        TextButton.TextButtonStyle btn = new TextButton.TextButtonStyle();
        btn.font      = font;
        btn.fontColor = Color.WHITE;
        btn.up        = skin.newDrawable("white", new Color(0.15f, 0.15f, 0.40f, 1f));
        btn.over      = skin.newDrawable("white", new Color(0.25f, 0.25f, 0.60f, 1f));
        btn.down      = skin.newDrawable("white", new Color(0.40f, 0.40f, 0.80f, 1f));
        skin.add("default", btn);

        // ── TextField ─────────────────────────────────────────────
        TextField.TextFieldStyle tf = new TextField.TextFieldStyle();
        tf.font       = font;
        tf.fontColor  = Color.WHITE;
        tf.background = skin.newDrawable("white", new Color(0.10f, 0.10f, 0.25f, 1f));
        tf.cursor     = skin.newDrawable("white", Color.WHITE);
        tf.selection  = skin.newDrawable("white", new Color(0.30f, 0.45f, 0.90f, 0.50f));
        skin.add("default", tf);

        return skin;
    }
}
