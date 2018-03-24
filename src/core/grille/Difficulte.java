package core.grille;

public enum Difficulte {
    FACILE,MOYEN,DIFFICILE;

    public int getNombreCoups(){
        if(this == FACILE) return 5000;
        if(this == MOYEN) return 10000;
        if(this == DIFFICILE) return 15000;
        return 10;
    }
}
