package core.bloc;

import core.grille.Case;

import java.util.HashSet;
import java.util.Set;

public abstract class Forme {
    public static final Forme VIDE;

    static {
        VIDE = new Forme(){
            @Override
            public Set<Case> recouvre(Case origine){
                return new HashSet<>();
            }
            @Override
            public boolean estApplicableDepuis(Case origine){
                return origine != null;
            }
        };
    }
    public abstract Set<Case> recouvre(Case origine);
    public abstract boolean estApplicableDepuis(Case origine);
}
