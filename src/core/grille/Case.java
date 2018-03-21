package core.grille;

import java.util.Map;
import java.util.HashMap;

import core.bloc.Bloc;
import core.grille.Direction;

public class Case{
    private static int nombreCases;

    static {
        nombreCases = 0;
    }

    private int identifiant;
    Case dessus,dessous,droite,gauche;
    boolean estVide = true;
    Map<Direction,Case> voisins;
    Bloc occupant;
    public Case() {
        identifiant = nombreCases;
        nombreCases++;
        estVide = true;
        voisins = new HashMap<Direction,Case>();
        occupant = Bloc.VIDE;
    }
    public void setVoisin(Direction direction, Case voisin) {
        voisins.put(direction,voisin);
        if (!voisin.voisin(direction.opposee()).equals(this))
            voisin.setVoisin(direction.opposee(),this);
    }
    public Case voisin(Direction direction){
        return voisins.get(direction);
    }

    public boolean aVoisin(Direction direction){
        return voisin(direction) == null;
    }

    public boolean estVide(){
        return estVide;
    }

    public Bloc occupant() {
        return occupant;
    }
    public void setOccupant(Bloc nouveau){
        if(nouveau == null)
            throw new IllegalArgumentException("Le bloc doit être initialisé");
        occupant = nouveau;
    }
    @Override
    public int hashCode(){
        return identifiant;
    }
    public boolean equals(Object o){
        if (!(o instanceof Case)) return false;
        return identifiant == ((Case) o).identifiant;
    }
}
