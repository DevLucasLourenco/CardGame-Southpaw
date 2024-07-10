package models.characters.pawbase;

import java.util.List;
import models.characters.*;

public class managePaws {
    private final List<Class<? extends PawCard>> PawsAvailableForUsage = List.of(
        Pawarrior.class,
        Pawskeleton.class,
        Pawclown.class
    );

    public List<Class<? extends PawCard>> getPawsAvailableForUsage() {
        return PawsAvailableForUsage;
    }
}