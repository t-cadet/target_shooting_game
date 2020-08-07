package menu;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;

import outils.ShinBackground;
import outils.ShinPanel;

/**
 * Construit un menu de jeu (panel avec des boutons)
 * @param compo 		tableau de boutons à ajouter au menu
 * @param b 			bordure du panel
 * @param background 	fond du panel
 * @param ib 			bordure à partir d'image 
 * @param ins 			espace entre chaque composant du panel
 * @param titre 		JLabel placé en haut du panel et pouvant servir de titre
  */
@SuppressWarnings("serial")
public class GameMenu extends ShinPanel {
	
	ImageBordure imabor;
	
	// CONSTRUCTEUR
	public GameMenu(JComponent[] compo, Border b, ShinBackground background, ImageBordure ib,Insets ins, JLabel titre){
		super(background);
		
		imabor = ib;
		setBorder(b);
		
		setLayout(new GridBagLayout());
		GridBagConstraints gb = new GridBagConstraints();
		
		if(ins!=null)
			gb.insets=ins;
		
		gb.gridy = 0;
		if(titre!=null){
			add(titre, gb);
			gb.gridy++;
		}
		
		for(int i = 0; i<compo.length;i++){
			gb.gridy++;
			add(compo[i],gb);
		}
	}
	// REDEFINITION DE paintComponent
	public void paintComponent(Graphics g){
		final int w = getWidth();
		final int h = getHeight();
		if(w!=0 && h!=0){
			super.paintComponent(g);
			if(imabor!=null  && (w>=imabor.getSize().getWidth() && h>=imabor.getSize().getHeight()))
				g.drawImage(imabor.getBorder(this),0,0,this);
		}
			
	}
}
