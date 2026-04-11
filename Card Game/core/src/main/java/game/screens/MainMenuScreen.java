package game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import game.SouthPawGame;

public class MainMenuScreen implements Screen {

    private final SouthPawGame game;
    private final Stage stage;

    public MainMenuScreen(SouthPawGame game) {
        this.game  = game;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        buildUI();
    }

    private void buildUI() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label title = new Label("S  O  U  T  H  P  A  W", game.skin, "title");
        title.setFontScale(2.5f);
        root.add(title).padBottom(20).row();

        root.add(new Label("A turn-based Paw card game", game.skin)).padBottom(60).row();

        TextButton playBtn = new TextButton("  PLAY  ", game.skin);
        playBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new PlayerSetupScreen(game));
                dispose();
            }
        });
        root.add(playBtn).width(220).height(60).padBottom(20).row();

        TextButton exitBtn = new TextButton("  EXIT  ", game.skin);
        exitBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        root.add(exitBtn).width(220).height(60);
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
