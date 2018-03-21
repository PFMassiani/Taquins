package core.mouvement;

import core.bloc.Bloc;
import core.grille.Case;
import core.grille.Direction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

public class Translation extends Mouvement{
    protected Direction direction;

    public Translation(Direction dir,Bloc bloc,Case origineBloc){
        super(bloc,origineBloc);
        this.direction = dir;
    }

    @Override
    public Map<Case,Bloc> getChangements(){
        Set<Case> anciennes = bloc.recouvre(origineBloc),
                occupees = new HashSet<>(anciennes),
                liberees = new HashSet<>(anciennes);
        occupees.forEach( (Case c) -> {
            if(!c.aVoisin(direction))
                throw new UnsupportedOperationException("La position actuelle du bloc ne permet pas le mouvement demandé");
            c.voisin(direction);
        });
        liberees.removeAll(occupees);
        occupees.removeAll(anciennes);

        Map<Case,Bloc> changements = new HashMap<>();
        for(Case c : liberees)
            changements.put(c,Bloc.VIDE);
        for(Case c : occupees)
            changements.put(c,bloc);

        return changements;
    }
}
