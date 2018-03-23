package ihm;

import core.bloc.Bloc;
import core.grille.Case;
import core.grille.Direction;
import core.grille.Grille;
import core.mouvement.Translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

public class FenetrePrincipale extends JFrame implements MouseListener,KeyListener {

    private final int DIMX = 5,DIMY = 4;

    private Grille grille;
    private Bloc blocCourant = null;

    private JButton generer,aide,solution;
    GraphicCase[][] graphicCases;

    public FenetrePrincipale(){
        grille = new Grille(DIMX,DIMY);
        Case[][] cases = grille.cases();

        graphicCases = new GraphicCase[DIMX][DIMY];
        for(int i = 0; i < DIMX; i++)
            for(int j = 0; j < DIMY; j++)
                graphicCases[i][j] = new GraphicCase(cases[i][j]);


        JPanel panelGrille = construirePanelGrille();
        add(panelGrille);

        addKeyListener(this);

        setSize(600,400);
        setVisible(true);
    }

    private JPanel construirePanelGrille(){
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints;

        panel.setLayout(layout);

        //Variables pour tracer le contour
        int top,left,bottom,right;
        boolean condTop=false,condLeft=false,condBottom=false,condRight=false;
        GraphicCase tmp;
        int offset = 11;
        for(int i = 0; i < DIMX; i++){
            for(int j = 0; j < DIMY; j++){
                constraints = new GridBagConstraints();
                //Définition de la case
                constraints.gridx = i;
                constraints.gridy = j;

                //Définition de la largeur des cases
                constraints.ipadx = GraphicCase.LARGEUR-offset;
                constraints.ipady = GraphicCase.HAUTEUR-offset;

                //Définition des bordures
//                tmp = graphicCases[i][j].getCase();
//                condTop = (j == 0) || ( tmp.aVoisin(Direction.DESSUS) && !tmp.estDansLeMemeBloc(tmp.voisin(Direction.DESSUS)) );
//                condLeft = (i == 0) || ( tmp.aVoisin(Direction.GAUCHE) && !tmp.estDansLeMemeBloc(tmp.voisin(Direction.GAUCHE)) );
//                condBottom = (j == DIMY - 1);
//                condRight = (i == DIMX - 1);

                tmp = graphicCases[i][j];
                top = (tmp.condTop()) ? GraphicCase.LARGEUR_LIGNE : 0;
                left = (tmp.condLeft()) ? GraphicCase.LARGEUR_LIGNE : 0;
                bottom = (tmp.condBottom()) ? GraphicCase.LARGEUR_LIGNE : 0;
                right = (tmp.condRight()) ? GraphicCase.LARGEUR_LIGNE : 0;

                graphicCases[i][j].tracerContour(top,left,bottom,right);

                //Ajout de la case au Panel
                panel.add(graphicCases[i][j],constraints);

                graphicCases[i][j].addMouseListener(this);
            }
        }
        return panel;
    }

    public void setSurbrillance(Bloc b,boolean valeur){
        Set<GraphicCase> gcasesDansMemeBloc = new HashSet<>();
        Set<Case> recouvertes = b.recouvre();

        for(Case c : recouvertes)
            gcasesDansMemeBloc.add(getGraphicCaseFromCase(c));

        for(GraphicCase gc : gcasesDansMemeBloc)
            gc.setSurbrillance(valeur);
    }

    public GraphicCase getGraphicCaseFromCase(Case c){
        for(int i = 0; i< DIMX; i++)
            for(int j = 0; j<DIMY; j++)
                if(graphicCases[i][j].getCase().equals(c))
                    return graphicCases[i][j];
        return null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() instanceof GraphicCase) {
            GraphicCase gcase = (GraphicCase) e.getSource();
            if(blocCourant != null && !blocCourant.recouvre().isEmpty())
                setSurbrillance(blocCourant, false);

            blocCourant = gcase.getBlocDeLaCase();
            setSurbrillance(blocCourant,true);
            repaint();
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void keyPressed(KeyEvent e){
        boolean fleche = false;
        Direction direction = null;
        System.out.println("UP " + KeyEvent.VK_UP);
        System.out.println("LEFT " + KeyEvent.VK_LEFT);
        System.out.println("DOWN " + KeyEvent.VK_DOWN);
        System.out.println("RIGHT " + KeyEvent.VK_RIGHT);
        if(e.getKeyCode() == KeyEvent.VK_UP){
            direction = Direction.DESSUS;
            fleche = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            direction = Direction.GAUCHE;
            fleche = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            direction = Direction.DESSOUS;
            fleche = true;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            direction = Direction.DROITE;
            fleche = true;
        }

        if (fleche && blocCourant != null){
            setSurbrillance(blocCourant,false);
            grille.effectuerMouvement(new Translation(direction,blocCourant));
            setSurbrillance(blocCourant,true);
            repaint();
        }
    }
    @Override
    public void keyReleased(KeyEvent e){}
    @Override
    public void keyTyped(KeyEvent e){}

    public static void main(String[] args){
        FenetrePrincipale fen = new FenetrePrincipale();
    }
}
