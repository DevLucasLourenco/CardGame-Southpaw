package game.screens;

import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import game.BattleEngine;
import game.CardTextures;
import game.CardWidget;
import game.SouthPawGame;
import models.characters.pawbase.ManagePaws;
import models.characters.pawbase.PawCard;
import models.users.User;

/**
 * Each player picks which paws to deploy before the battle starts.
 * Players take turns: first player 1 builds their team, then player 2.
 *
 * Shows the card art (PNG) + stats for every available card.
 * Max 5 monsters per player (enforced via User.hasRoom()).
 */
public class CardSelectionScreen implements Screen {

    private static final int MAX_ELIXIR = 10; // starting elixir budget

    private final SouthPawGame game;
    private final Stage  stage;
    private final User[] players;
    private final ManagePaws registry = new ManagePaws();
    private int currentPlayerIndex = 0;
    private Label statusLabel;

    public CardSelectionScreen(SouthPawGame game, User p1, User p2) {
        this.game    = game;
        this.players = new User[]{ p1, p2 };
        this.stage   = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        // Give each player a fresh elixir budget
        p1.setElixir(MAX_ELIXIR);
        p2.setElixir(MAX_ELIXIR);
        buildUI();
    }

    private void buildUI() {
        stage.clear();
        User current = players[currentPlayerIndex];

        Table root = new Table();
        root.setFillParent(true);
        root.top().pad(20);
        stage.addActor(root);

        // ── Header ───────────────────────────────────────────────
        Label header = new Label(
                (currentPlayerIndex == 0 ? "Player 1: " : "Player 2: ")
                + current.getName() + " — Choose your Paws",
                game.skin, "title");
        header.setFontScale(1.2f);
        root.add(header).padBottom(6).row();

        statusLabel = new Label(buildStatus(current), game.skin);
        root.add(statusLabel).padBottom(16).row();

        // ── Card grid ─────────────────────────────────────────────
        BitmapFont font = game.skin.getFont("default");
        Table cardRow = new Table();
        List<Class<? extends PawCard>> paws = registry.getPawsAvailableForUsage();
        for (Class<? extends PawCard> cls : paws) {
            try {
                PawCard dummy = cls.getDeclaredConstructor(User.class).newInstance(current);
                // Prevent the dummy from occupying a real slot
                current.getPawUnderControl().remove(dummy);

                // ── Card visual (art + stats overlay) ────────────
                Table card = new Table();
                Texture art = CardTextures.get(cls.getSimpleName());
                Image artImg = new Image(art);
                artImg.setColor(Color.WHITE);
                card.add(artImg).size(CardWidget.W, CardWidget.H).row();

                // Stats below the image
                Label stats = new Label(
                        dummy.getName()
                        + "\nHP "  + dummy.getLife()
                        + "  ATK " + dummy.getAttack()
                        + "\nAGI " + dummy.getAgility()
                        + "  ⚡ "  + dummy.getElixirCost(),
                        game.skin);
                stats.setFontScale(0.7f);
                stats.setWrap(true);
                card.add(stats).width(CardWidget.W).padTop(4).row();

                // Summon button
                TextButton btn = new TextButton("Summon", game.skin);
                final Class<? extends PawCard> cardClass = cls;
                btn.addListener(new ChangeListener() {
                    @Override public void changed(ChangeEvent event, Actor actor) {
                        invokePaw(current, cardClass);
                    }
                });
                card.add(btn).width(CardWidget.W).height(38).padTop(4);

                cardRow.add(card).pad(10).top();
            } catch (Exception ignored) {}
        }
        root.add(cardRow).padBottom(24).row();

        // ── Field preview (cards already placed) ─────────────────
        if (!current.getPawUnderControl().isEmpty()) {
            Label fieldLbl = new Label("Field (" + current.getPawUnderControl().size() + "/5):",
                    game.skin);
            root.add(fieldLbl).padBottom(4).row();

            Table fieldRow = new Table();
            for (Object c : current.getPawUnderControl().toArray()) {
                if (c instanceof PawCard) {
                    PawCard p = (PawCard) c;
                    Image mini = new Image(CardTextures.get(p.getClass().getSimpleName()));
                    fieldRow.add(mini).size(60, 90).pad(4);
                }
            }
            root.add(fieldRow).padBottom(16).row();
        }

        // ── Done / Start button ───────────────────────────────────
        String doneLabel = currentPlayerIndex == 0 ? "Done — Next Player ▶" : "Start Battle!";
        TextButton doneBtn = new TextButton(doneLabel, game.skin);
        doneBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                if (current.getPawUnderControl().isEmpty()) {
                    statusLabel.setText("Place at least 1 Paw!  " + buildStatus(current));
                    return;
                }
                currentPlayerIndex++;
                if (currentPlayerIndex < players.length) {
                    buildUI();
                } else {
                    game.setScreen(new BattleScreen(game,
                            new BattleEngine(players[0], players[1])));
                    dispose();
                }
            }
        });
        root.add(doneBtn).width(280).height(60);
    }

    private void invokePaw(User owner, Class<? extends PawCard> cls) {
        try {
            if (!owner.hasRoom()) {
                statusLabel.setText("Field full! (5/5)  " + buildStatus(owner));
                return;
            }
            PawCard paw = cls.getDeclaredConstructor(User.class).newInstance(owner);
            if (owner.getElixir() < paw.getElixirCost()) {
                statusLabel.setText("Not enough Elixir!  " + buildStatus(owner));
                return;
            }
            paw.positionateCard();
            statusLabel.setText("Placed " + cls.getSimpleName() + "!  " + buildStatus(owner));
            buildUI(); // refresh field preview
        } catch (Exception ignored) {}
    }

    private String buildStatus(User u) {
        return "Elixir: " + u.getElixir() + " | Field: " + u.getPawUnderControl().size() + "/5";
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.04f, 0.04f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void show()   { Gdx.input.setInputProcessor(stage); }
    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() { stage.dispose(); }
}
