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
    protected abstract void remettreBloc();
    protected abstract Map<Case,Bloc> changementsAnnulation();
    public abstract boolean annulationEstValide();

    public Bloc getBloc(){
        return bloc;
    }

    public void appliquer(){
        Map<Case,Bloc> changements = getChangements();
        for(Map.Entry<Case,Bloc> entry : changements.entrySet())
            entry.getKey().setOccupant(entry.getValue());
        deplacerBloc();
    }
    public void annuler(){
        Map<Case,Bloc> changements = changementsAnnulation();
        for(Map.Entry<Case,Bloc> entry : changements.entrySet())
            entry.getKey().setOccupant(entry.getValue());
        remettreBloc();
    }
}
