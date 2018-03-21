package core.grille;

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
}
