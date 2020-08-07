package menu;

import outils.ShinBackground;

/**
 *	Stocke les ShinBackground qui apparaissent 
 *	aux côtés des boutons lors d'un survol
 * @param left image de gauche
 * @param right image de droite
 */
public class ButtonHoverIcon {
	protected ShinBackground left;
	protected ShinBackground right;
	
	// CONSTRUCTEUR
	public ButtonHoverIcon(ShinBackground left, ShinBackground right) {
		super();
		this.left = left;
		this.right = right;
	}
	
	// GETTERS & SETTERS
	public ShinBackground getLeft() {
		return left;
	}

	public void setLeft(ShinBackground left) {
		this.left = left;
	}

	public ShinBackground getRight() {
		return right;
	}

	public void setRight(ShinBackground right) {
		this.right = right;
	}
	
	

}
