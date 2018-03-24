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
        Forme choicForme;
        while (!casesARemplir.isEmpty()){;
            tmp = casesARemplir.get(0);
            possibilites = new ArrayList<>();
            possibilites.add(f1x1);
            possibilites.add(f1x2);
            possibilites.add(f2x1);

            choicForme = possibilites.get(RANDOM.nextInt(possibilites.size()));
            while(!choicForme.estApplicableDepuis(tmp) || !estInclus(choicForme.recouvre(tmp),casesARemplir)){//!intersectionEstVide(casesTampon,choix.recouvre(tmp)) || !intersectionEstVide(carre.recouvre(),choix.recouvre(tmp))){
                possibilites.remove(choicForme);
                choicForme = possibilites.get(RANDOM.nextInt(possibilites.size()));
            }

            b = new Bloc(choicForme,tmp);

            casesARemplir.removeAll(b.recouvre());
        }
        Set<Mouvement> mouvements;
        Mouvement mvmt, choixMvmt;
        for(int i = 0; i < difficulte.getNombreCoups(); i++) {
            mouvements = new HashSet<>();
            // Détermination d'un mouvement pour chaque bloc déplaçable
            for(Case[] ligne : grille.cases) {
                for (Case c : ligne) {
                    if ((mvmt = Grille.genererTranslationAleatoire(c.occupant())) != null) {
                        mouvements.add(mvmt);
                    }
                }
            }
            //Sélection d'un mouvement aléatoire parmi les mouvements trouvés
            choixMvmt = elementSetAleatoire(mouvements);

            //Application du mouvement
            grille.effectuerMouvement(choixMvmt);
//
//            //Détermination des blocs déplaçables
//            for (Case[] line : grille.cases) {
//                for (Case c : line) {
//                    translations = Translation.translationsPossibles(c.occupant());
//                    if (!translations.isEmpty())
//                        mouvements.put(c.occupant(), translations);
//                }
//            }
//
//            //Sélection aléatoire de la translation
//            numEntry = RANDOM.nextInt(mouvements.size());
//            for(Map.Entry<Bloc,List<Direction>> entry : mouvements.entrySet()){
//                if (n == numEntry){
//                    choisi = entry.getKey();
//                    translations = entry.getValue();
//                    dir = translations.get(RANDOM.nextInt(translations.size()));
//                    break;
//                }
//                n++;
//            }
//
//            //Maintenant qu'on a sélectionné un bloc, on le bouge aléatoirement
//            trans = genererTranslationAleatoire(choisi);
//            if(trans != null)
//                grille.effectuerMouvement(trans);
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
            if(translation.estValide()) {
//                System.out.println("" + translation);
//                System.out.println("Check validité 0 :" + translation.estValide());
//                translation.appliquer();
                return translation;
            }
            directionsPossibles.remove(direction);
        }
        return null;
    }
    private static Mouvement elementSetAleatoire(Set<Mouvement> mvmts){
        int n = RANDOM.nextInt(mvmts.size());
        Iterator<Mouvement> it = mvmts.iterator();
        int i = 0;
        while(i < n -1 && it.hasNext()) {
            it.next();
            i++;
        }

        return (it.hasNext()) ? it.next() : null;
    }

    public void effectuerMouvement(Mouvement mvmt){
        mvmt.appliquer();
    }

    public Case[][] cases(){
        return cases;
    }
    private static int n=0;
    public static void testGenerationTranslationAleatoire(Grille g){
        Set<Mouvement> mouvements;
        Mouvement mvmt;
        for(int i = 0; i < 100; i++){
            mouvements = new HashSet<>();
            System.out.println("Début première phase test");
            for(Case[] ligne : g.cases) {
                for (Case c : ligne) {
                    if ((mvmt = Grille.genererTranslationAleatoire(c.occupant())) != null) {
                        mouvements.add(mvmt);
                        System.out.println("valide:"+mvmt.estValide());
                    }
                }
            }
            System.out.println("Fin première phase test");
            for(Mouvement m:mouvements)
                System.out.println("valide:" + m.estValide());
            System.out.println("Fin deuxième phase test");
            mvmt = elementSetAleatoire(mouvements);
            for(Mouvement m:mouvements)
                System.out.println("valide:" + m.estValide());
            System.out.println("Fin troisième phase test");
            if (mvmt != null){
                System.out.println("Non null");
                if (!mvmt.estValide()) {
                    mvmt.estValide();
                    System.out.println("mvmt invalide");
                }
                mvmt.appliquer();
                n++;
            }
        }
//
//        Translation t1 = genererTranslationAleatoire(b1),
//                t2,
//                t3;
//        System.out.println("Positions:\n 1: " + g.position(b1)[0] + "" + g.position(b1)[1] + "\n 2:" + g.position(b2)[0] + "" + g.position(b2)[1]  + "\n 3:" + g.position(b3)[0] + "" + g.position(b3)[1]);
//
//        if (t1 != null){
//            System.out.print("t1 ");
//            if (!t1.estValide())
//                System.out.println("Erreur t1");
//            t1.appliquer();
//            n1++;
//        }
//        t2 = genererTranslationAleatoire(b2);
//        if (t2 != null){
//            System.out.print("t2 ");
//            if (!t2.estValide())
//                System.out.println("Erreur t2");
//            t2.appliquer();
//            n2++;
//        }
//        t3 = genererTranslationAleatoire(b3);
//        if (t3 != null){
//            System.out.print("t3 ");
//            if (!t3.estValide())
//                System.out.println("Erreur t3");
//            t3.appliquer();
//            n3++;
//        }
        //        System.out.println("n1 = " + n1 + ", n2 = " + n2 + ", n3 = " + n3);
    }

    private int[] position(Bloc b){
        int dimX = cases.length;
        int dimY = cases[0].length;
        for(int i = 0; i<dimX; i++) {
            for (int j = 0; j<dimY; j++) {
                if (cases[i][j].occupant() == b)
                    return new int[] {i,j};
            }
        }
        return null;
    }

    public static void main(String[] args){
        System.out.println("Début test");
        Grille g = new Grille(2,2);
        Rectangle r = new Rectangle(1,1);
        Bloc b1 = new Bloc(r,g.cases[0][0]),
                b2 = new Bloc(r,g.cases[1][0]),
                b3 = new Bloc(r,g.cases[0][1]);
        Grille.testGenerationTranslationAleatoire(g);

        System.out.println("Test terminé");
    }
}
