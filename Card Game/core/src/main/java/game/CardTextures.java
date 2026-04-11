package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 * Central loader for card artwork.
 *
 * Images must be placed at:  assets/cards/<ClassName>.png
 * e.g.  assets/cards/Pawarrior.png
 *
 * If the expected file is missing, a 1x1 fallback texture is used so the
 * game never crashes on a missing asset.
 */
public final class CardTextures {

    private static final Map<String, Texture> cache = new HashMap<>();
    private static Texture fallback;

    private CardTextures() {}

    /**
     * Returns the Texture for the given card class simple name.
     * Falls back to a solid-colour 1×1 pixel if the file is not found.
     */
    public static Texture get(String cardClassName) {
        if (cache.containsKey(cardClassName)) {
            return cache.get(cardClassName);
        }
        String path = "cards/" + cardClassName + ".png";
        Texture tex;
        if (Gdx.files.internal(path).exists()) {
            tex = new Texture(Gdx.files.internal(path));
        } else {
            tex = getFallback();
        }
        cache.put(cardClassName, tex);
        return tex;
    }

    private static Texture getFallback() {
        if (fallback == null) {
            com.badlogic.gdx.graphics.Pixmap pm =
                    new com.badlogic.gdx.graphics.Pixmap(1, 1,
                            com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
            pm.setColor(0.2f, 0.2f, 0.3f, 1f);
            pm.fill();
            fallback = new Texture(pm);
            pm.dispose();
        }
        return fallback;
    }

    /** Dispose all loaded textures — call from SouthPawGame.dispose(). */
    public static void disposeAll() {
        cache.values().forEach(Texture::dispose);
        cache.clear();
        if (fallback != null) { fallback.dispose(); fallback = null; }
    }
}
