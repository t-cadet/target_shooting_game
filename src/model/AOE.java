package model;

import java.awt.Color;
import java.awt.Polygon;

import outils.ShinPanel;
/* On utilise une superclasse non instanciable pour l'aire d'effet de l'arme
 * Cela nous permet de d�finir si l'envie nous prend une aire d'effet diff�rente
 * du <Cercle> (par exemple en �toile ?)
 * Cette classe h�rite de <Polygon>, ce qui nous permet d'utiliser la m�thode 
 * contains() pour coder collides() si on le juge utile
 * On note qu'hormis le centre le seul param�tre est un rayon, il faudra donc
 * d�finir toute l'aire d'effet par rapport � ce param�tre.
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
	//du joueur sans conna�tre le type d'aoe
	public AOE setUp(int x, int y, int r){
		x_centre = x;
		y_centre = y;
		R = r;
		return this;
	}
}
