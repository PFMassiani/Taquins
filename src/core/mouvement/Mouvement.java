package core.mouvement;

import core.bloc.Bloc;
import core.grille.Case;

import java.util.Map;

public abstract class Mouvement {

    protected Bloc bloc;

    public Mouvement(Bloc bloc){
        this.bloc = bloc;}

    public abstract Map<Case,Bloc> getChangements();
    public abstract boolean estValide();
    protected abstract void deplacerBloc();

    public void appliquer(){
        Map<Case,Bloc> changements = getChangements();
        for(Map.Entry<Case,Bloc> entry : changements.entrySet())
            entry.getKey().setOccupant(entry.getValue());
        deplacerBloc();
    }
}
