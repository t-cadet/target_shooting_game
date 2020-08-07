package model;

import java.awt.Color;
import java.awt.Polygon;

import outils.ShinPanel;
/* On utilise une superclasse non instanciable pour l'aire d'effet de l'arme
 * Cela nous permet de définir si l'envie nous prend une aire d'effet différente
 * du <Cercle> (par exemple en étoile ?)
 * Cette classe hérite de <Polygon>, ce qui nous permet d'utiliser la méthode 
 * contains() pour coder collides() si on le juge utile
 * On note qu'hormis le centre le seul paramètre est un rayon, il faudra donc
 * définir toute l'aire d'effet par rapport à ce paramètre.
 */
@SuppressWarnings("serial")
public abstract class AOE extends Polygon {
	protected int x_centre;
	protected int y_centre;
	protected int R;
	public AOE(){}
	public AOE(int x_centre, int y_centre, int R){
		this.x_centre = x_centre;
		this.y_centre = y_centre;
		this.R = R;
	}
	public abstract boolean collides(ShinPanel im);
	public abstract boolean collides(ShinPanel im, Color impact);
	
	//Permet de changer aoe lors d'un clic
	//du joueur sans connaître le type d'aoe
	public AOE setUp(int x, int y, int r){
		x_centre = x;
		y_centre = y;
		R = r;
		return this;
	}
}
