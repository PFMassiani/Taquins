package core.bloc;

import java.util.Set;

import core.grille.Case;

public class Bloc {
    public static final Bloc VIDE;
    protected Forme forme;
    protected Case origine;

    static {
        VIDE = new Bloc(Forme.VIDE,null){
            @Override
            public int hashCode() {
                return forme.hashCode();
            }
            @Override
            public boolean equals(Object o){
                if(!(o instanceof Bloc)) return false;
                Bloc b = (Bloc) o;
                return b.forme.equals(forme);
            }
        };
    }
    public Bloc(Forme forme, Case origine){
        this.forme = forme;
        this.origine = origine;
        if(!forme.estApplicableDepuis(origine) || !estPositionnable())
            throw new IllegalArgumentException("Ce bloc ne peut être créé sur cette case");
        Set<Case> recouvertes = recouvre();
        for(Case c : recouvertes)
            c.setOccupant(this);
    }

    public Set<Case> recouvre(){
        return forme.recouvre(origine);
    }
    public void setOrigine(Case newOrigine){
        origine = newOrigine;
    }
    public Case origine(){
        return origine;
    }

    @Override
    public int hashCode() {
        return forme.hashCode() + origine.hashCode();
    }
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Bloc)) return false;
        Bloc b = (Bloc) o;
        return b.forme.equals(forme) && b.origine.equals(origine);
    }

    private boolean estPositionnable(){
        Set<Case> recouvertes = recouvre();
        for(Case c : recouvertes)
            if(c.occupant() != Bloc.VIDE && c.occupant() != this)
                return false;
        return true;
    }

    public static boolean blocCreableDepuis(Case origine, Forme forme){
        if(forme.estApplicableDepuis(origine)) return false;
        Set<Case> recouvertes = forme.recouvre(origine);
        for(Case c: recouvertes)
            if(c.occupant() != Bloc.VIDE)
                return false;
        return true;
    }
}
