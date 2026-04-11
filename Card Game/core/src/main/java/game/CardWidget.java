package game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import models.characters.pawbase.PawCard;

/**
 * Widget visual de carta posicionado no tabuleiro de batalha.
 *
 * Dimensões: 240 × 360 px  (W × H)
 *
 * Layout interno:
 * ┌───────────────────────┐
 * │     [ARTE PNG]        │  ← y: H-224 .. H-4   (220 px de altura)
 * ├───────────────────────┤
 * │  NOME DA CARTA        │  ← y: H-256 .. H-224  (fontScale 1.5)
 * ├───────────────────────┤
 * │  HP xxx/xxx           │  ← y: 8 .. 108        (fontScale 1.3)
 * │  ATK xxx              │
 * └───────────────────────┘
 *
 * Estado selecionado → borda dourada (Color.GOLD)
 * Estado alvo        → borda vermelha (Color.RED)
 *
 * W e H são constantes públicas referenciadas por BattleScreen e
 * CardSelectionScreen para garantir consistência de tamanho.
 */
public class CardWidget extends Group {

    public static final float W = 240f;
    public static final float H = 360f;

    private final PawCard card;
    private boolean selected   = false;
    private boolean targetable = false;
    private boolean isOwner    = false;   // true when this card belongs to the active player

    // Overlay for the selection border — drawn in draw()
    private final com.badlogic.gdx.graphics.Pixmap borderPm;
    private final Texture borderTex;
    private final Texture bgTex;

    private final Label nameLabel;
    private final Label statsLabel;

    private Runnable onClick;

    public CardWidget(PawCard card, BitmapFont font, boolean isOwner) {
        this.card    = card;
        this.isOwner = isOwner;
        setBounds(0, 0, W, H);

        // ── Background ──────────────────────────────────────────
        com.badlogic.gdx.graphics.Pixmap bgPm =
                new com.badlogic.gdx.graphics.Pixmap(1, 1,
                        com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        bgPm.setColor(0.08f, 0.08f, 0.18f, 1f);
        bgPm.fill();
        bgTex = new Texture(bgPm);
        bgPm.dispose();

        Image bg = new Image(bgTex);
        bg.setBounds(0, 0, W, H);
        addActor(bg);

        // ── Card art ─────────────────────────────────────────────
        Texture art = CardTextures.get(card.getClass().getSimpleName());
        Image artImg = new Image(art);
        artImg.setBounds(4, H - 224, W - 8, 220);
        addActor(artImg);

        // ── Border pixmap (1px border, redrawn based on state) ──
        borderPm = new com.badlogic.gdx.graphics.Pixmap((int) W, (int) H,
                com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        borderTex = new Texture(borderPm);

        // ── Name label ────────────────────────────────────────────
        Label.LabelStyle ls = new Label.LabelStyle(font, Color.WHITE);
        nameLabel = new Label(card.getName(), ls);
        nameLabel.setFontScale(1.5f);
        nameLabel.setBounds(8, H - 256, W - 16, 32);
        addActor(nameLabel);

        // ── Stats label ───────────────────────────────────────────
        Label.LabelStyle ss = new Label.LabelStyle(font, new Color(0.85f, 0.95f, 0.85f, 1f));
        statsLabel = new Label("", ss);
        statsLabel.setFontScale(1.3f);
        statsLabel.setBounds(8, 8, W - 16, 100);
        addActor(statsLabel);
        refreshStats();

        // ── Click listener ────────────────────────────────────────
        addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                if (onClick != null) onClick.run();
            }
        });

        // ── Border overlay (drawn last via draw override) ─────────
        borderPm.dispose();
    }

    public void setOnClick(Runnable r)   { this.onClick    = r; }
    public void setSelected(boolean v)   { this.selected   = v; }
    public void setTargetable(boolean v) { this.targetable = v; }
    public PawCard getCard()             { return card;         }

    public void refreshStats() {
        String hp  = card.getLife() + "/" + card.getMaxLife();
        statsLabel.setText("HP " + hp + "\nATK " + card.getAttack());
        // Dim the card if it's dead
        if (!card.isOnTheField()) {
            setColor(0.3f, 0.3f, 0.3f, 0.5f);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Draw border before children
        if (selected) {
            batch.setColor(1f, 0.85f, 0.1f, 1f);  // gold = selected
        } else if (targetable) {
            batch.setColor(1f, 0.2f, 0.2f, 1f);   // red = targetable
        }
        // 4px border drawn as 4 thin rectangles
        if (selected || targetable) {
            Color c = batch.getColor().cpy();
            float bx = getX(), by = getY();
            batch.setColor(selected ? Color.GOLD : Color.RED);
            // top / bottom / left / right lines
            batch.draw(bgTex, bx,      by + H - 3, W,  3);
            batch.draw(bgTex, bx,      by,          W,  3);
            batch.draw(bgTex, bx,      by,          3, H);
            batch.draw(bgTex, bx + W - 3, by,       3, H);
            batch.setColor(c);
        }
        batch.setColor(1, 1, 1, 1);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        refreshStats();
    }
}
