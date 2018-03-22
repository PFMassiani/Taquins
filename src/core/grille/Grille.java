package core.grille;

import core.bloc.Rectangle;
import core.mouvement.Mouvement;

import java.util.List;

public class Grille {
    Case[][] grille;

    public Grille(int dimX, int dimY){
        grille = new Case[dimX][dimY];
        for(int i = 0; i < dimX; i++)
            for(int j = 0; j < dimY; j++)
                grille[i][j] = new Case();
        for(int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                if(i<dimX) grille[i][j].setVoisin(Direction.DESSOUS, grille[i+1][j]);
                if(j<dimY) grille[i][j].setVoisin(Direction.DROITE, grille[i][j+1]);
            }
        }
    }
    public List<Mouvement> resoudre(){
        // TODO
        return null;
    }
    public static Grille generer(int nb1x1, int nb1x2, int nb2x1,Difficulte difficulte){
        Grille grille = new Grille(4,5);
        Rectangle r1x1 = new Rectangle(1,1),
                r1x2 = new Rectangle(1,2),
                r2x1 = new Rectangle(2,1);
        Rectangle carre = new Rectangle(2,2);
        // TODO FINIR GÉNÉRER GRILLE
        return grille;
    }
}
