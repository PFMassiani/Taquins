package ihm;

import core.bloc.Bloc;
import core.grille.Case;
import core.grille.Difficulte;
import core.grille.Direction;
import core.grille.Grille;
import core.mouvement.Translation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class FenetrePrincipale extends JFrame implements MouseListener,KeyListener,ActionListener {

    public static final int DIMX = 5,DIMY = 4;

    private Grille grille;
    private Bloc blocCourant = null;

    private JButton generer,solution;
    private JComboBox<Difficulte> difficulte;
    private JPanel panelGrille;
    private GraphicCase[][] graphicCases;

    public FenetrePrincipale(){
        grille = Grille.generer(Difficulte.FACILE);
        Case[][] cases = grille.cases();

        graphicCases = new GraphicCase[DIMX][DIMY];
        for(int i = 0; i < DIMX; i++)
            for(int j = 0; j < DIMY; j++)
                graphicCases[i][j] = new GraphicCase(cases[i][j]);

        setLayout(new BorderLayout());

        panelGrille = construirePanelGrille();
        add(BorderLayout.CENTER,panelGrille);

        generer = new JButton("Nouvelle grille");
        difficulte = new JComboBox<>(Difficulte.values());
        solution = new JButton("Aide");

        JPanel panelGeneration = new JPanel();
        panelGeneration.setLayout(new BorderLayout());
        panelGeneration.add(BorderLayout.NORTH,difficulte);
        panelGeneration.add(BorderLayout.SOUTH,generer);

        JPanel boutons = new JPanel();
        boutons.add(panelGeneration);
        boutons.add(solution);

        generer.setFocusable(false);
        difficulte.setFocusable(false);
        solution.setFocusable(false);

        add(BorderLayout.SOUTH,boutons);

        generer.addActionListener(this);
        solution.addActionListener(this);
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
            if (gc != null)
                gc.setSurbrillance(valeur);
    }

    public GraphicCase getGraphicCaseFromCase(Case c){
        for(int i = 0; i< DIMX; i++)
            for(int j = 0; j<DIMY; j++)
                if(graphicCases[i][j].getCase().equals(c))
                    return graphicCases[i][j];
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
            grille.effectuerMouvementUtilisateur(new Translation(direction,blocCourant));
            setSurbrillance(blocCourant,true);
            repaint();
        }
    }
    @Override
    public void keyReleased(KeyEvent e){}
    @Override
    public void keyTyped(KeyEvent e){}
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == generer){
            Difficulte diff = difficulte.getItemAt(difficulte.getSelectedIndex());
            grille = Grille.generer(diff);
            Case[][] cases = grille.cases();
            for(int i = 0; i < DIMX; i++) {
                for (int j = 0; j < DIMY; j++) {
                    graphicCases[i][j].setCase(cases[i][j]);
                    graphicCases[i][j].setSurbrillance(false);
                }
            }
            JPanel tmp = construirePanelGrille();
            remove(panelGrille);
            panelGrille = tmp;
            add(BorderLayout.CENTER,panelGrille);
            repaint();
        }
        else if(e.getSource() == solution){
            int confirme = JOptionPane.YES_OPTION;
            if (grille.utilisateurAAgi())
                confirme = JOptionPane.showConfirmDialog (null, "Attention: cette action réinitialisera la grille. Continuer ?","Confirmation",JOptionPane.YES_NO_OPTION);
            if(confirme == JOptionPane.YES_OPTION){
                if(blocCourant != null)
                    setSurbrillance(blocCourant,false);
                blocCourant = grille.resoudreUneEtape();
                setSurbrillance(blocCourant,true);
            }
//                while(!grille.resolutionFinie())
//                    grille.resoudreUneEtape();
        }
    }

    public static void main(String[] args){
        FenetrePrincipale fen = new FenetrePrincipale();
    }
}
