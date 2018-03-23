package core.mouvement;

import core.bloc.Bloc;
import core.bloc.Forme;
import core.grille.Case;
import core.grille.Direction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

public class Translation extends Mouvement{

    // TODO mise à jout avec le changement de la notion de Bloc

    protected Direction direction;

    public Translation(Direction dir,Bloc bloc){
        super(bloc);
        this.direction = dir;
    }

    @Override
    public Map<Case,Bloc> getChangements(){
        Set<Case> anciennes = bloc.recouvre(),
                occupees = new HashSet<>(),
                liberees = new HashSet<>(anciennes);
    if (!estValide()) throw new UnsupportedOperationException("La position actuelle du bloc ne permet pas le mouvement demandé");
        for (Case c : anciennes)
            occupees.add(c.voisin(direction));
        liberees.removeAll(occupees);
        occupees.removeAll(anciennes);

        Map<Case,Bloc> changements = new HashMap<>();
        for(Case c : liberees)
            changements.put(c,new Bloc(Forme.VIDE,c));
        for(Case c : occupees)
            changements.put(c,bloc);

        return changements;
    }

    @Override
    protected void deplacerBloc(){
        bloc.setOrigine(bloc.origine().voisin(direction));
    }

    @Override
    public boolean estValide(){
        Set<Case> recouvertes = bloc.recouvre();
        for(Case c : recouvertes)
            if (!c.aVoisin(direction) || ( !c.voisin(direction).estVide() && !c.voisin(direction).estDansLeMemeBloc(c) ) )
                return false;
        return true;
    }
}
