package core.grille;

import java.util.Map;
import java.util.HashMap;

import core.bloc.Bloc;
import core.bloc.Forme;


public class Case{
    private static int nombreCases;

    static {
        nombreCases = 0;
    }

    private int identifiant;
    private Map<Direction,Case> voisins;
    private Bloc occupant;
    public Case() {
        identifiant = nombreCases;
        nombreCases++;
        voisins = new HashMap<Direction,Case>();
        occupant = new Bloc(Forme.VIDE,this);
    }
    public void setVoisin(Direction direction, Case voisin) {
        voisins.put(direction,voisin);
        if (!voisin.aVoisin(direction.opposee()) || !voisin.voisin(direction.opposee()).equals(this))
            voisin.setVoisin(direction.opposee(),this);
    }
    public Case voisin(Direction direction){
        return voisins.get(direction);
    }

    public boolean aVoisin(Direction direction){
        return voisin(direction) != null;
    }

    public boolean estVide(){
        return occupant.estVide();
    }

    public Bloc occupant() {
        return occupant;
    }
    public void setOccupant(Bloc nouveau){
        if(nouveau == null)
            throw new IllegalArgumentException("Le bloc doit être initialisé");
        occupant = nouveau;
    }
    public boolean estDansLeMemeBloc(Case c){
        return c.occupant.equals(occupant);
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
