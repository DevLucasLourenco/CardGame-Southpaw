package models.characters.pawbase;

import java.util.List;
import models.characters.*;

public class ManagePaws {
    private final List<Class<? extends PawCard>> PawsAvailableForUsage = List.of(
        Pawarrior.class,
        Pawbayle.class,
        Pawskeleton.class,
        Pawclown.class
    );

    public List<Class<? extends PawCard>> getPawsAvailableForUsage() {
        return PawsAvailableForUsage;
    }
}
