package ihm;

import core.bloc.Bloc;
import core.grille.Case;
import core.grille.Direction;

import javax.swing.*;
import java.awt.*;

public class GraphicCase extends JPanel{

    public static final int LARGEUR=50,HAUTEUR=50,LARGEUR_LIGNE = 1;
    private Case caze = null;
    private boolean surbrillance;
    private static float filterOpacity = 0.5f;
    private int top = 0,left = 0,bottom = 0,right = 0;

    public GraphicCase(Case caze){
        this.caze = caze;
    }
    public void setCase(Case c){
        caze = c;
    }
    public Case getCase(){
        return caze;
    }
    public void tracerContour(int top, int left, int bottom, int right){
        setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
    }

    public int hashCode(){
        return caze.hashCode();
    }
    public boolean equals(Object o){
        if(!(o instanceof GraphicCase)) return false;
        GraphicCase gc = (GraphicCase) o;
        return caze.equals(gc.getCase());
    }
    public void setSurbrillance(boolean valeur){
        surbrillance = valeur;
        repaint();
    }
    public Bloc getBlocDeLaCase(){
        return caze.occupant();
    }
    public boolean condTop(){
        return !caze.aVoisin(Direction.DESSUS) || !caze.voisin(Direction.DESSUS).estDansLeMemeBloc(caze);
    }
    public boolean condLeft(){
        return !caze.aVoisin(Direction.GAUCHE) || !caze.voisin(Direction.GAUCHE).estDansLeMemeBloc(caze);
    }
    public boolean condBottom(){
        return !caze.aVoisin(Direction.DESSOUS);
    }
    public boolean condRight(){
        return !caze.aVoisin(Direction.DROITE);
    }

    public void paintComponent(Graphics g){
        // TODO prendre en compte la surbrillance
        super.paintComponent(g);
        if(caze.estVide())
            g.setColor(Color.LIGHT_GRAY);
        else
            g.setColor(new Color(0xff,0xc6,0x18));
        g.fillRect(0,0,LARGEUR,HAUTEUR);
        if(surbrillance){
            g.setColor(new Color(0x00,0xbf,0x30));
            g.fillRect(0,0,LARGEUR,HAUTEUR);
        }
        top = (condTop())? LARGEUR_LIGNE : 0;
        left = (condLeft())? LARGEUR_LIGNE : 0;
        bottom = (condBottom())? LARGEUR_LIGNE : 0;
        right = (condRight())? LARGEUR_LIGNE : 0;
        tracerContour(top,left,bottom,right);
    }
}
