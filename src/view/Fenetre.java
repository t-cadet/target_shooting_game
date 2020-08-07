package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import menu.BoutonMenu;
import menu.ButtonHoverIcon;
import menu.GameMenu;
import model.Joueur;
import outils.ShinBackground;
import outils.ShinPanel;

/**
 * Fenetre générale du jeu
 * Certaines lignes sont en commentaire 
 * pour tester différentes configurations de menu
 */
@SuppressWarnings("serial")
public class Fenetre extends JFrame {
	public static Joueur player;
	
	final static Dimension SCREEN_SIZE = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	final static int W = (int)SCREEN_SIZE.getWidth();
	final static int H = (int)SCREEN_SIZE.getHeight();
	
	
	protected Fenetre myself;
	protected ShinPanel accueil, jeu, pause, zoneTir, highscore, option, aPropos, boutique;
	protected InterfaceMenu interfaceMenu;
	// CONSTRUCTEUR
	public Fenetre(){
		myself = this;
		// CARACTERISTIQUES DE LA FENETRE
		setSize(SCREEN_SIZE);
		setResizable(false);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// NB ET TAILLE DES BOUTONS
		final int nbBoutons = 5;
		final Dimension tailleBoutons = new Dimension(500*W/1920,100*H/1080);
		
		//SET-UP DU TEXTE DES BOUTONS
		final String[] text = {"JOUER","<en dév>","<en dév>","<en dév>","QUITTER"};
		final Font f = new Font("Times New Roman", Font.BOLD, (int) (20*Fenetre.H/1080));
		final JLabel[] jlText = new JLabel[nbBoutons];
		
		for(int i = 0; i<nbBoutons; i++){
			jlText[i]= new JLabel(text[i]);
			jlText[i].setFont(f);
			jlText[i].setForeground(Color.WHITE);
		}
		
		// LISTENERS DES BOUTONS
		ActionListener[] al = {
			//LANCE UNE NOUVELLE PARTIE	
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					zoneTir = new ZoneTir();
					interfaceMenu = new InterfaceMenu();
					jeu = new ShinPanel(new ShinBackground(Main.IMG.get("fondZoneTir"),ShinBackground.FILL));
					jeu.setLayout(new BorderLayout());
					jeu.add(zoneTir, BorderLayout.NORTH);
					jeu.add(interfaceMenu);
					setContentPane(jeu);
					revalidate();
					player = new Joueur(300, myself);
					((ZoneTir) zoneTir).startNewGame();
				}
			},
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					System.out.println("<en dév>");					
				}
			},
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					System.out.println("<en dév>");					
				}
			},
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					System.out.println("<en dév>");					
				}
			},
			//QUITTE LE JEU
			new ActionListener(){
				public void actionPerformed(ActionEvent e){	
					System.exit(0);
				}
			}
		};
		// CREATION DES ButtonHoverIcon
		ButtonHoverIcon icon2 = new ButtonHoverIcon(new ShinBackground(Main.IMG.get("ArrowLeft")), new ShinBackground(Main.IMG.get("ArrowRight")));
		
		//CREATION DU TABLEAU DE BOUTONS
		JComponent compo[] = BoutonMenu.createButtons(jlText, al, nbBoutons, tailleBoutons, Main.ib, Color.ORANGE, Color.RED, new Color(175,50,50), Main.BOUTON_HOVER, icon2);
		//JComponent compo[] = BoutonMenu.createButtons(jlText, al, nbBoutons, tailleBoutons, ib, Color.CYAN, Color.BLUE, new Color(0,0,0), son, icon2);
		
		//CREATION DE LA BORDURE(MARGE) ET DU TITRE DU MENU
		Border marge = BorderFactory.createEmptyBorder(30,0,10,0);
		//JLabel titre = new JLabel("MENU");
		//titre.setFont(new Font("Arial", Font.BOLD, 25));
		//titre.setForeground(Color.WHITE);
		
		// SET-UP DU CONTENEUR, CREATION ET AJOUT DU GameMenu AU CONTENEUR, AJOUT DU CONTENEUR A LA FENETRE
		JPanel conteneur = new ShinPanel(new ShinBackground(Main.IMG.get("MLP"), ShinBackground.FILL));
		conteneur.setLayout(new GridBagLayout());
		//conteneur.add(new GameMenu(compo ,marge, new ShinBackground(background), ib, new Insets(0,0,0,0), titre));
		conteneur.add(new GameMenu(compo ,marge, new ShinBackground(new Color(153, 0, 51, 85)), Main.ib, new Insets(0,0,0,0), null));
		
		setContentPane(conteneur);
		setVisible(true);
	}
	
	//GETTERS
	public ShinPanel getAccueil() {
		return accueil;
	}

	public ShinPanel getJeu() {
		return jeu;
	}

	public ShinPanel getPause() {
		return pause;
	}

	public InterfaceMenu getInterfaceMenu() {
		return interfaceMenu;
	}

	public ShinPanel getZoneTir() {
		return zoneTir;
	}

	public ShinPanel getHighscore() {
		return highscore;
	}

	public ShinPanel getOption() {
		return option;
	}

	public ShinPanel getaPropos() {
		return aPropos;
	}

	public ShinPanel getBoutique() {
		return boutique;
	}

}
