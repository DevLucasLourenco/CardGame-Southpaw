package game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import game.SouthPawGame;
import models.users.User;

public class PlayerSetupScreen implements Screen {

    private final SouthPawGame game;
    private final Stage stage;
    private TextField tf1, tf2;
    private Label errorLabel;

    public PlayerSetupScreen(SouthPawGame game) {
        this.game  = game;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        buildUI();
    }

    private void buildUI() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label title = new Label("Enter Player Names", game.skin, "title");
        title.setFontScale(1.8f);
        root.add(title).padBottom(50).row();

        root.add(new Label("Player 1:", game.skin)).padBottom(8).row();
        tf1 = new TextField("", game.skin);
        tf1.setMessageText("Player 1 name...");
        root.add(tf1).width(300).height(45).padBottom(25).row();

        root.add(new Label("Player 2:", game.skin)).padBottom(8).row();
        tf2 = new TextField("", game.skin);
        tf2.setMessageText("Player 2 name...");
        root.add(tf2).width(300).height(45).padBottom(30).row();

        errorLabel = new Label("", game.skin);
        root.add(errorLabel).padBottom(20).row();

        TextButton startBtn = new TextButton("  START  ", game.skin);
        startBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                String n1 = tf1.getText().trim().toUpperCase();
                String n2 = tf2.getText().trim().toUpperCase();
                if (n1.isEmpty() || n2.isEmpty()) {
                    errorLabel.setText("Both players must have a name.");
                    return;
                }
                game.player1 = new User(n1);
                game.player2 = new User(n2);
                game.setScreen(new CardSelectionScreen(game, game.player1, game.player2));
                dispose();
            }
        });
        root.add(startBtn).width(220).height(60);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.04f, 0.04f, 0.12f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void show()   {}
    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}
    @Override public void dispose() { stage.dispose(); }
}
