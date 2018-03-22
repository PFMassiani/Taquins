package core.grille;

public enum Difficulte {
    FACILE,MOYEN,DIFFICILE;

    public int getNombreCoups(){
        if(this == FACILE) return 10;
        if(this == MOYEN) return 20;
        if(this == DIFFICILE) return 30;
        return 10;
    }
}
