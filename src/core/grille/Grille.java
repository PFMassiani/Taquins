package core.grille;

import core.bloc.Bloc;
import core.bloc.Rectangle;
import core.mouvement.Mouvement;
import core.mouvement.Translation;

import java.util.*;

public class Grille {
    private Case[][] cases;

    private final Random RANDOM = new Random();
    private static final List<Direction> DIRECTIONS =
            Collections.unmodifiableList(Arrays.asList(Direction.values()));
    private static final int SIZE = DIRECTIONS.size();

    public Grille(int dimX, int dimY){
        cases = new Case[dimX][dimY];
        for(int i = 0; i < dimX; i++)
            for (int j = 0; j < dimY; j++)
                cases[i][j] = new Case();


        for(int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                if(i<dimX - 1) cases[i][j].setVoisin(Direction.DROITE, cases[i+1][j]);
                if(j<dimY - 1) cases[i][j].setVoisin(Direction.DESSOUS, cases[i][j+1]);
            }
        }
        Bloc test1 = new Bloc(new Rectangle(2,2),cases[0][0]);
        Bloc test2 = new Bloc(new Rectangle(1,1),cases[2][0]);


    }
    public List<Mouvement> resoudre(){
        // TODO
        return new LinkedList<Mouvement>();
    }
    public static Grille generer(int nb1x1, int nb1x2, int nb2x1,Difficulte difficulte){
        int dimX = 4,dimY = 5;
        Grille grille = new Grille(dimX,dimY);
        Rectangle f1x1 = new Rectangle(1,1),
                f1x2 = new Rectangle(1,2),
                f2x1 = new Rectangle(2,1);
        Rectangle fCarre = new Rectangle(2,2);


        // REMPLISSAGE DE LA GRILLE

        Bloc carre = new Bloc(fCarre,grille.cases[0][dimY-2]);

        // TODO FINIR GÉNÉRER GRILLE

        return grille;
    }

    private Translation genererTranslationAleatoire(Bloc bloc){
        Direction direction;
        Translation translation;
        List<Direction> directionsPossibles = new ArrayList<>(DIRECTIONS);
        while(!directionsPossibles.isEmpty()){
            direction = directionsPossibles.get(RANDOM.nextInt());
            translation = new Translation(direction,bloc);
            if(translation.estValide()) return translation;
            directionsPossibles.remove(direction);
        }
        return null;

    }

    public void effectuerMouvement(Mouvement mvmt){
        mvmt.appliquer();
    }

    public Case[][] cases(){
        return cases;
    }
}
