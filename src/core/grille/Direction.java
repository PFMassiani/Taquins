package core.grille;

public enum Direction {
    DESSUS,DESSOUS,DROITE,GAUCHE,NA;

    public Direction opposee(){
        if (this == DESSUS) return DESSOUS;
        else if (this == DESSOUS) return DESSUS;
        else if (this == DROITE) return GAUCHE;
        else if (this == GAUCHE) return DROITE;
        else return NA;
    }
}
