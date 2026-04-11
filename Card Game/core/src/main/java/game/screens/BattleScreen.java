package game.screens;

import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import game.BattleEngine;
import game.CardWidget;
import game.SouthPawGame;
import models.characters.pawbase.PawCard;
import models.contracts.Card;
import models.contracts.HasPower;
import models.users.User;

/**
 * Tela de batalha estilo Yu-Gi-Oh.
 *
 * Resolucao alvo: 1600 x 950 px  (definida em DesktopLauncher)
 *
 * Layout:
 * +--------------------------------------------------+---------------+
 * |  HP [bar] 8000   NOME INIMIGO                    |               |
 * |  [carta][carta][carta][carta][carta]  (5 slots)  | PAINEL ACAO   |
 * +---- separador azul petroleo (5px) ---------------+  [Atacar]     |
 * |  [carta][carta][carta][carta][carta]  (5 slots)  |  [Poder]      |
 * |  HP [bar] 8000   SEU NOME                        |  [Passar]     |
 * +--------------------------------------------------+               |
 * |  Log da ultima acao                              |               |
 * +--------------------------------------------------+---------------+
 *
 * Cartas    : 240 x 360 px  (CardWidget.W x CardWidget.H)
 * Separador : azul petroleo RGB(0.06, 0.40, 0.50), 5 px de altura
 * Painel    : 220 px de largura fixa
 */
public class BattleScreen implements Screen {

    private static final int SLOTS = 5;

    private final SouthPawGame game;
    private final BattleEngine engine;
    private final Stage        stage;

    // Selected card (for action dispatch) and target selection mode
    private CardWidget         selectedAttacker = null;
    private boolean            selectingTarget  = false;

    // Textures for board background
    private Texture fieldTex;
    private Texture panelTex;
    private Texture separatorTex;

    public BattleScreen(SouthPawGame game, BattleEngine engine) {
        this.game   = game;
        this.engine = engine;
        this.stage  = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        initTextures();
        rebuildBoard();
    }

    // â”€â”€ Textures â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void initTextures() {
        fieldTex     = solidColor(0.06f, 0.10f, 0.06f, 1f);
        panelTex     = solidColor(0.05f, 0.05f, 0.15f, 1f);
        separatorTex = solidColor(0.06f, 0.40f, 0.50f, 1f); // azul petróleo
    }

    private Texture solidColor(float r, float g, float b, float a) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(r, g, b, a);
        pm.fill();
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }

    // â”€â”€ Board build â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void rebuildBoard() {
        stage.clear();

        User enemy  = engine.getUser2();
        User player = engine.getUser1();
        PawCard currentCard = engine.getCurrentCard();
        User    currentOwner = engine.getCurrentOwner();

        // â”€â”€ Root table â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // â”€â”€ Board column (left/center, ~1060px wide) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        Table board = new Table();
        board.setBackground(new TextureRegionDrawable(fieldTex));

        // --- ENEMY side ---
        board.add(buildPlayerRow(enemy, false)).expandX().fillX().padTop(8).padBottom(4).row();
        board.add(buildFieldRow(enemy, currentCard, currentOwner, false))
             .height(CardWidget.H + 20).expandX().padBottom(4).row();

        // --- Separator line ---
        board.add(new Image(separatorTex)).height(5).expandX().fillX().padBottom(4).row();

        // --- PLAYER side ---
        board.add(buildFieldRow(player, currentCard, currentOwner, true))
             .height(CardWidget.H + 20).expandX().padBottom(4).row();
        board.add(buildPlayerRow(player, true)).expandX().fillX().padBottom(8).row();

        // --- Log bar ---
        Label logLbl = new Label(engine.getLastLog(), game.skin);
        logLbl.setWrap(true);
        board.add(logLbl).expandX().fillX().padLeft(10).padRight(10).padBottom(6).row();

        root.add(board).expandX().fillX().expandY().fillY();

        // â”€â”€ Action panel (right, ~220px wide) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€
        Table panel = new Table();
        panel.setBackground(new TextureRegionDrawable(panelTex));
        panel.top().pad(20);

        if (!engine.isGameOver()) {
            if (engine.isRoundOver()) {
                engine.startNextRound();
                rebuildBoard();
                return;
            }
            buildActionPanel(panel, engine.getCurrentCard(), engine.getCurrentOwner());
        } else {
            buildGameOverPanel(panel);
        }

        root.add(panel).width(220).expandY().fillY().pad(0, 0, 0, 0);
    }

    // â”€â”€ Player row (HP bar + name) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private Table buildPlayerRow(User user, boolean isBottom) {
        Table row = new Table();
        row.pad(4, 12, 4, 12);

        Label name = new Label(user.getName(), game.skin);

        int hp    = user.getPlayerHP();
        int maxHp = user.getMaxPlayerHP();
        float pct = maxHp > 0 ? (float) hp / maxHp : 0f;

        // Colour the bar: green > 50%, yellow > 25%, red otherwise
        Color barColor = (pct > 0.5f) ? new Color(0.2f, 0.9f, 0.2f, 1f)
                       : (pct > 0.25f) ? new Color(1f, 0.8f, 0.1f, 1f)
                                       : new Color(0.9f, 0.1f, 0.1f, 1f);

        Texture barTex  = solidColor(barColor.r, barColor.g, barColor.b, barColor.a);
        Texture bgBarTx = solidColor(0.2f, 0.05f, 0.05f, 1f);

        // HP bar container
        Table hpBar = new Table();
        float barW = 340f;
        float barH = 18f;

        // Background
        Image bg = new Image(bgBarTx);
        hpBar.addActor(bg);
        bg.setBounds(0, 0, barW, barH);

        // Fill
        float fillW = barW * pct;
        if (fillW > 0) {
            Image fill = new Image(barTex);
            fill.setBounds(0, 0, fillW, barH);
            hpBar.addActor(fill);
        }

        Label hpLabel = new Label(" " + hp + " / " + maxHp, game.skin);
        hpLabel.setFontScale(0.75f);

        if (isBottom) {
            row.add(hpBar).size(barW, barH).padRight(8);
            row.add(hpLabel).padRight(20);
            row.add(name);
        } else {
            row.add(name).padRight(20);
            row.add(hpLabel).padRight(8);
            row.add(hpBar).size(barW, barH);
        }
        return row;
    }

    // â”€â”€ Field row (5 card slots) â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private Table buildFieldRow(User user, PawCard currentCard,
                                User currentOwner, boolean isPlayerSide) {
        Table row = new Table();
        List<Card> paws = user.getPawUnderControl();

        for (int i = 0; i < SLOTS; i++) {
            if (i < paws.size() && paws.get(i) instanceof PawCard) {
                PawCard paw = (PawCard) paws.get(i);
                boolean isOwner = currentOwner != null && currentOwner == user;
                CardWidget w = new CardWidget(paw, game.skin.getFont("default"), isOwner);

                // Highlight the current card
                if (paw == currentCard) w.setSelected(true);

                // Highlight targetable enemy cards
                if (selectingTarget && currentOwner != null
                        && user != currentOwner && paw.isOnTheField()) {
                    w.setTargetable(true);
                    final PawCard target = paw;
                    final CardWidget attacker = selectedAttacker;
                    w.setOnClick(() -> {
                        selectingTarget = false;
                        engine.attack(attacker.getCard(), target);
                        rebuildBoard();
                    });
                }

                row.add(w).size(CardWidget.W, CardWidget.H).pad(4);
            } else {
                // Empty slot placeholder
                Table empty = new Table();
                empty.setBackground(new TextureRegionDrawable(
                        solidColor(0.05f, 0.12f, 0.05f, 0.9f)));
                row.add(empty).size(CardWidget.W, CardWidget.H).pad(4);
            }
        }
        return row;
    }

    // â”€â”€ Action panel â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void buildActionPanel(Table panel, PawCard card, User owner) {
        if (card == null) return;

        Label turnLbl = new Label("Round " + engine.getRound(), game.skin);
        Label cardLbl = new Label(owner.getName() + "\nâ†’ " + card.getName(), game.skin);
        cardLbl.setWrap(true);

        panel.add(turnLbl).padBottom(8).row();
        panel.add(cardLbl).width(180).padBottom(20).row();

        User   opponent = engine.getOpponent(owner);
        List<Card> targets = opponent.getPawUnderControl();

        // â”€â”€ Attack button â”€â”€
        TextButton atkBtn = new TextButton("Attack", game.skin);
        atkBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent ev, Actor a) {
                if (targets.isEmpty()) {
                    engine.attackPlayer(card);
                    selectingTarget = false;
                } else {
                    selectedAttacker = findWidget(card);
                    selectingTarget  = true;
                }
                rebuildBoard();
            }
        });
        panel.add(atkBtn).width(180).height(48).padBottom(8).row();

        // â”€â”€ Power button (only if HasPower) â”€â”€
        if (card instanceof HasPower) {
            TextButton pwrBtn = new TextButton("Power", game.skin);
            pwrBtn.addListener(new ChangeListener() {
                @Override public void changed(ChangeEvent ev, Actor a) {
                    engine.usePower(card);
                    selectingTarget = false;
                    rebuildBoard();
                }
            });
            panel.add(pwrBtn).width(180).height(48).padBottom(8).row();
        }

        // â”€â”€ Pass button â”€â”€
        TextButton passBtn = new TextButton("Pass", game.skin);
        passBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent ev, Actor a) {
                engine.pass();
                selectingTarget = false;
                rebuildBoard();
            }
        });
        panel.add(passBtn).width(180).height(48);
    }

    // â”€â”€ Game over panel â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private void buildGameOverPanel(Table panel) {
        User winner = engine.getWinner();
        Label wl = new Label(
                winner != null ? winner.getName() + " WINS!" : "DRAW!", game.skin, "title");
        wl.setFontScale(1.4f);
        wl.setWrap(true);
        panel.add(wl).width(180).padBottom(20).row();

        for (User u : new User[]{ engine.getUser1(), engine.getUser2() }) {
            Label stats = new Label(
                    u.getName() + "\nHP " + u.getPlayerHP() + "\n" + u.getStatistic(),
                    game.skin);
            stats.setWrap(true);
            panel.add(stats).width(180).padBottom(12).row();
        }

        TextButton menuBtn = new TextButton("Main Menu", game.skin);
        menuBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent ev, Actor a) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });
        panel.add(menuBtn).width(180).height(52).padTop(20);
    }

    // â”€â”€ Helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /** Finds the CardWidget for a PawCard that's already on stage. */
    private CardWidget findWidget(PawCard card) {
        for (Actor a : stage.getActors()) {
            CardWidget found = findInActor(a, card);
            if (found != null) return found;
        }
        return null;
    }

    private CardWidget findInActor(Actor actor, PawCard card) {
        if (actor instanceof CardWidget) {
            CardWidget w = (CardWidget) actor;
            if (w.getCard() == card) return w;
        }
        if (actor instanceof com.badlogic.gdx.scenes.scene2d.Group) {
            for (Actor child : ((com.badlogic.gdx.scenes.scene2d.Group) actor).getChildren()) {
                CardWidget found = findInActor(child, card);
                if (found != null) return found;
            }
        }
        return null;
    }

    // â”€â”€ Screen interface â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.03f, 0.06f, 0.03f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void show()   { Gdx.input.setInputProcessor(stage); }
    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        stage.dispose();
        if (fieldTex     != null) fieldTex.dispose();
        if (panelTex     != null) panelTex.dispose();
        if (separatorTex != null) separatorTex.dispose();
    }
}
