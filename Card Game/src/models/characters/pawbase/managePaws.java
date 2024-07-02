package models.characters.pawbase;

import java.util.List;
import models.characters.Pawarrior;
import models.characters.Pawclown;
import models.characters.Pawskeleton;

public class managePaws {
    private static List<Class<? extends PawCard>> PawsAvailableForUsage = List.of(
        Pawarrior.class,
        Pawskeleton.class,
        Pawclown.class
    );
}