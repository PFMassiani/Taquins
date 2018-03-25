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
    private Stack<Mouvement> solution, mouvementsUtilisateur;
    private static final Random RANDOM = new Random();
    private static final List<Direction> DIRECTIONS =
            Collections.unmodifiableList(Arrays.asList(Direction.values()));
    private static final int SIZE = DIRECTIONS.size();

    public Grille(int dimX, int dimY){
        cases = new Case[dimX][dimY];
        solution = new Stack<>();
        mouvementsUtilisateur = new Stack<>();
        for(int i = 0; i < dimX; i++)
            for (int j = 0; j < dimY; j++)
                cases[i][j] = new Case();


        for(int i = 0; i < dimX; i++) {
            for (int j = 0; j < dimY; j++) {
                if(i<dimX - 1) cases[i][j].setVoisin(Direction.DROITE, cases[i+1][j]);
                if(j<dimY - 1) cases[i][j].setVoisin(Direction.DESSOUS, cases[i][j+1]);
            }
        }
    }
    public Bloc resoudreUneEtape(){
        Mouvement mvmt;
        if(!mouvementsUtilisateur.empty()) {
//            System.out.println("Utilisateur a effectué des mouvements:" + mouvementsUtilisateur.size());
            while (!mouvementsUtilisateur.empty()){
                mvmt = mouvementsUtilisateur.pop();
                System.out.println("Mouvement:" + mvmt);
                if(mvmt.annulationEstValide()) annulerMouvement(mvmt);
            }
//            System.out.println("Fin d'annulation des mouvements utilisateur");
        }
        mvmt = solution.pop();
//        System.out.println("Mouvement automatique : " + mvmt);
//        System.out.println("Bloc : "+mvmt.bloc);
//        System.out.println("Origine bloc : " + mvmt.bloc.origine());
//        System.out.println("Occupant origine : " + mvmt.bloc.origine().occupant());
//        System.out.println("Direction originelle :" + ((Translation) mvmt).direction);
//        System.out.println("Annulation est valide: " + mvmt.annulationEstValide());
        Bloc bouge = mvmt.getBloc();
        if(mvmt.annulationEstValide()) annulerMouvement(mvmt);
        return bouge;
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
        Forme choixForme;
        while (!casesARemplir.isEmpty()){;
            tmp = casesARemplir.get(0);
            possibilites = new ArrayList<>();
            possibilites.add(f1x1);
            possibilites.add(f1x2);
            possibilites.add(f2x1);

            choixForme = possibilites.get(RANDOM.nextInt(possibilites.size()));
            while(!choixForme.estApplicableDepuis(tmp) || !estInclus(choixForme.recouvre(tmp),casesARemplir)){//!intersectionEstVide(casesTampon,choix.recouvre(tmp)) || !intersectionEstVide(carre.recouvre(),choix.recouvre(tmp))){
                possibilites.remove(choixForme);
                choixForme = possibilites.get(RANDOM.nextInt(possibilites.size()));
            }

            b = new Bloc(choixForme,tmp);

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
            grille.effectuerMouvementAutomatique(choixMvmt);
        }
        return grille;
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

    public void effectuerMouvementAutomatique(Mouvement mvmt){
        mvmt.appliquer();
        solution.push(mvmt);
    }
    public void effectuerMouvementUtilisateur(Mouvement mvmt){
        mvmt.appliquer();
        mouvementsUtilisateur.push(mvmt);
    }
    public void annulerMouvement(Mouvement mvmt){
        mvmt.annuler();
    }
    public boolean utilisateurAAgi(){
        return !mouvementsUtilisateur.empty();
    }

    public Case[][] cases(){
        return cases;
    }
}
