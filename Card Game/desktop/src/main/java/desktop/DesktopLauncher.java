package desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import game.SouthPawGame;

/**
 * Entry point desktop — abre a janela do jogo via LibGDX/LWJGL3.
 *
 * Para rodar:
 *   .\gradlew.bat desktop:run
 *
 * Configuração da janela:
 *   - Título    : SouthPaw
 *   - Resolução : 1600 × 950 px  (acomoda 5 slots × 240px + painel lateral)
 *   - FPS alvo  : 60 fps com VSync
 *
 * A linha {@code new Lwjgl3Application(new SouthPawGame(), config)}
 * é a que efetivamente cria e abre a janela do sistema operacional.
 */
public class DesktopLauncher {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("SouthPaw");
        config.setWindowedMode(1600, 950); // largura × altura em pixels
        config.setForegroundFPS(60);
        config.useVsync(true);

        // ← esta linha cria a janela e inicia o loop de jogo
        new Lwjgl3Application(new SouthPawGame(), config);
    }
}
