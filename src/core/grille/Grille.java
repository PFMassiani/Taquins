package core.grille;

import core.bloc.Bloc;
import core.bloc.Forme;
import core.bloc.Rectangle;
import core.mouvement.Mouvement;
import core.mouvement.Translation;
import ihm.FenetrePrincipale;

import java.util.*;

public class Grille {
    private Case[][] cases;

    private static final Random RANDOM = new Random();
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
//        Bloc test1 = new Bloc(new Rectangle(2,2),cases[0][0]);
//        Bloc test2 = new Bloc(new Rectangle(1,1),cases[2][0]);


    }
    public List<Mouvement> resoudre(){
        // TODO
        return new LinkedList<Mouvement>();
    }
    public static Grille generer(Difficulte difficulte){
        int dimX = FenetrePrincipale.DIMX,dimY = FenetrePrincipale.DIMY;
        Grille grille = new Grille(dimX,dimY);
        Rectangle f1x1 = new Rectangle(1,1),
                f1x2 = new Rectangle(1,2),
                f2x1 = new Rectangle(2,1);
        Rectangle fCarre = new Rectangle(2,2);


        // REMPLISSAGE DE LA GRILLE

        Bloc carre = new Bloc(fCarre,grille.cases[dimX-2][0]);
        List<Case> casesARemplir = new ArrayList<>();
        Set<Case> casesTampon = new HashSet<>();
        int rand = RANDOM.nextInt(2);
        if (rand == 0){//Cases du bas
            casesTampon.add(grille.cases[dimX - 1][2]);
            casesTampon.add(grille.cases[dimX - 2][2]);
        }
        else if (rand == 1){//Cases du haut
            casesTampon.add(grille.cases[dimX - 3][0]);
            casesTampon.add(grille.cases[dimX - 3][1]);
        }
        for(int i = 0; i < dimX; i++)
            for(int j = 0; j < dimY; j++)
                if(!grille.cases[i][j].occupant().equals(carre)
                        && !casesTampon.contains(grille.cases[i][j]))
                    casesARemplir.add(grille.cases[i][j]);
        Case tmp;
        Bloc b;
        List<Forme> possibilites;
        Forme choix;
        while (!casesARemplir.isEmpty()){;
            tmp = casesARemplir.get(0);
            possibilites = new ArrayList<>();
            possibilites.add(f1x1);
            possibilites.add(f1x2);
            possibilites.add(f2x1);

            choix = possibilites.get(RANDOM.nextInt(possibilites.size()));
            while(!choix.estApplicableDepuis(tmp) || !estInclus(choix.recouvre(tmp),casesARemplir)){//!intersectionEstVide(casesTampon,choix.recouvre(tmp)) || !intersectionEstVide(carre.recouvre(),choix.recouvre(tmp))){
                possibilites.remove(choix);
                choix = possibilites.get(RANDOM.nextInt(possibilites.size()));
            }

            b = new Bloc(choix,tmp);

            casesARemplir.removeAll(b.recouvre());
        }
        Map<Bloc,List<Direction>> mouvements = new HashMap<>();
        List<Direction> translations;
        int numEntry, n = 0;
        Bloc choisi=null;
        Direction dir;
        Translation trans;
        for(int i = 0; i < difficulte.getNombreCoups(); i++) {
            //Détermination des blocs déplaçables
            for (Case[] line : grille.cases) {
                for (Case c : line) {
                    translations = Translation.translationsPossibles(c.occupant());
                    if (!translations.isEmpty())
                        mouvements.put(c.occupant(), translations);
                }
            }

            //Sélection aléatoire de la translation
            numEntry = RANDOM.nextInt(mouvements.size());
            for(Map.Entry<Bloc,List<Direction>> entry : mouvements.entrySet()){
                if (n == numEntry){
                    choisi = entry.getKey();
                    translations = entry.getValue();
                    dir = translations.get(RANDOM.nextInt(translations.size()));
                    break;
                }
                n++;
            }

            //Maintenant qu'on a sélectionné un bloc, on le bouge aléatoirement
            trans = genererTranslationAleatoire(choisi);
            if(trans != null)
                grille.effectuerMouvement(trans);
        }

        // TODO FINIR GÉNÉRER GRILLE


        return grille;
    }
    private static boolean intersectionEstVide(Collection<Case> c1, Collection<Case> c2){
        Collection<Case> c3 = new HashSet<>(c1);
        boolean res = c3.removeAll(c2);
        return !res;
    }
    private static boolean estInclus(Collection<Case> c1, Collection<Case> c2){
        Collection<Case> c3 = new HashSet<>(c1);
        boolean res = c3.retainAll(c2);
        return !res;
    }

    private static Translation genererTranslationAleatoire(Bloc bloc){
        Direction direction;
        Translation translation;
        List<Direction> directionsPossibles = new ArrayList<>(DIRECTIONS);
        while(!directionsPossibles.isEmpty()){
            direction = directionsPossibles.get(RANDOM.nextInt(directionsPossibles.size()));
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
