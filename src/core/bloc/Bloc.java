package core.bloc;

import java.util.HashSet;
import java.util.Set;

import core.grille.Case;

public abstract class Bloc {
    public static final Bloc VIDE;

    static {
        VIDE = new Bloc(){
            public Set<Case> recouvre(Case origine){
                return new HashSet<Case>();
            }
        };
    }

    public abstract Set<Case> recouvre(Case origine);
}
