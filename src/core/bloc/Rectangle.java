package core.bloc;

import core.grille.Case;
import core.grille.Direction;

import java.util.HashSet;
import java.util.Set;

public class Rectangle extends Bloc{

    int hauteur,longueur;

    public Rectangle(int hauteur,int longueur){
        super();
        if(hauteur <= 0 || longueur <= 0) throw new IllegalArgumentException("Les dimensions du rectangle doivent être positives");
        this.hauteur = hauteur;
        this.longueur = longueur;
    }
    public Set<Case> recouvre(Case origine){
        Set<Case> recouvertes = new HashSet<>();
        Case lineHead = null, current = null;
        for (int i = 0; i < hauteur; i++){

            if(lineHead != null) {
                if (i != hauteur - 1 && !lineHead.aVoisin(Direction.DESSOUS))
                    throw new ArrayIndexOutOfBoundsException("L'origine de ce bloc ne peut être cette case");
                lineHead = lineHead.voisin(Direction.DESSOUS);
            }
            else
                lineHead = origine;

            for (int j = 0; j < longueur; j++){
                if(current != null) {
                    if (j != longueur - 1 && !current.aVoisin(Direction.DROITE))
                        throw new ArrayIndexOutOfBoundsException("L'origine de ce bloc ne peut être cette case");
                    else
                        current = current.voisin(Direction.DROITE);
                }
                else
                    current = lineHead;

                recouvertes.add(current);
            }
        }
        return recouvertes;
    }

}
