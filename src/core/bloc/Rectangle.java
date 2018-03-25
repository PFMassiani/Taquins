package core.bloc;

import core.grille.Case;
import core.grille.Direction;
import org.w3c.dom.css.Rect;

import java.util.HashSet;
import java.util.Set;

public class Rectangle extends Forme {

    private int hauteur, longueur;

    public Rectangle(int hauteur, int longueur) {
        super();
        if (hauteur <= 0 || longueur <= 0)
            throw new IllegalArgumentException("Les dimensions du rectangle doivent être positives");
        this.hauteur = hauteur;
        this.longueur = longueur;
    }

    public Set<Case> recouvre(Case origine) {
        if (!estApplicableDepuis(origine)) throw new IllegalArgumentException("Cette forme ne peut être appliquée ici");
        Set<Case> recouvertes = new HashSet<>();
        Case lineHead = null, current = null;
        for (int i = 0; i < hauteur; i++) {

            if (lineHead != null)
                lineHead = lineHead.voisin(Direction.DESSOUS);
            else
                lineHead = origine;

            for (int j = 0; j < longueur; j++) {
                if (j != 0)
                    current = current.voisin(Direction.DROITE);
                else
                    current = lineHead;

                recouvertes.add(current);
            }
        }
        return recouvertes;
    }

    @Override
    public boolean estApplicableDepuis(Case origine) {

        if (origine == null) return false;
        Case lineHead = null, current = null;
        for (int i = 0; i < hauteur; i++) {
            if (lineHead != null) {
                if (!lineHead.aVoisin(Direction.DESSOUS))
                    return false;
                lineHead = lineHead.voisin(Direction.DESSOUS);
            } else
                lineHead = origine;

            for (int j = 0; j < longueur; j++) {
                if (j != 0) {
                    if (!current.aVoisin(Direction.DROITE))
                        return false;
                    current = current.voisin(Direction.DROITE);
                } else
                    current = lineHead;

            }
        }
        return true;
    }
    @Override
    public int hashCode(){
        return longueur + hauteur;
    }
    @Override
    public boolean equals(Object o){
        if (!(o instanceof Rectangle)) return false;
        Rectangle r = (Rectangle) o;
        return longueur == r.longueur && hauteur == r.hauteur;
    }
}
