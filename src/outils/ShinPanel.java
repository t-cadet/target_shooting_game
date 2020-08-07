package outils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Panel non-opaque sur lequel on peut facilement
 * mettre une image/texture/couleur de fond
 * grâce à l'objet ShinBackground
 * @param sback background du ShinPanel
 */
@SuppressWarnings("serial")
public class ShinPanel extends JPanel {
	protected ShinBackground sback;
	
	// CONSTRUCTEURS
	public ShinPanel(){
		setOpaque(false);
	}
	public ShinPanel(BufferedImage im){
		setOpaque(false);
		sback = new ShinBackground(im);
	}
	public ShinPanel(Color c){
		setOpaque(false);
		sback = new ShinBackground(c);
	}
	public ShinPanel(ShinBackground sb){
		setOpaque(false);
		sback = sb;
	}
	// REDEFINITION DE PAINTCOMPONENT
	public void paintComponent(Graphics g){
		if(sback!=null)
			sback.paintShinBackground(getWidth(),getHeight(),g);
	}
	// GETTERS & SETTERS
	public ShinBackground getSback() {
		return sback;
	}
	public void setSback(ShinBackground sback) {
		this.sback = sback;
		repaint();
	}
	

}
