package core.mouvement;

import core.bloc.Bloc;
import core.grille.Case;

import java.util.Map;

public abstract class Mouvement {
    protected Bloc bloc;
    protected Case origineBloc;

    public Mouvement(Bloc bloc, Case origineBloc){
        this.bloc = bloc;
        this.origineBloc = origineBloc;
    }

    public abstract Map<Case,Bloc> getChangements();

    public void appliquer(){
        Map<Case,Bloc> changements = getChangements();
        for(Map.Entry<Case,Bloc> entry : changements.entrySet())
            entry.getKey().setOccupant(entry.getValue());
    }
}
