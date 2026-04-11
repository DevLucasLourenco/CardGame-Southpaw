package game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import game.screens.MainMenuScreen;
import models.users.User;

public class SouthPawGame extends Game {

    public SpriteBatch batch;
    public Skin skin;

    /** Shared player references set during PlayerSetupScreen and used by later screens. */
    public User player1;
    public User player2;

    @Override
    public void create() {
        batch = new SpriteBatch();
        skin  = UIFactory.createSkin();
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        skin.dispose();
        CardTextures.disposeAll();
    }
}
