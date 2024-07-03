package models.characters.pawbase;

import java.util.List;
import models.characters.*;

public class managePaws {
    private static final List<Class<? extends PawCard>> PawsAvailableForUsage = List.of(
        Pawarrior.class,
        Pawskeleton.class,
        Pawclown.class
    );
}