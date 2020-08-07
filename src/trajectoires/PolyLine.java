package trajectoires;

import java.awt.Point;

import javax.swing.JComponent;

public class PolyLine implements Trajectoire{
	/* @param lines
	 * @param L 	longeur du tableau lines
	 * @param cur  	curseur qui indique la ligne 
	 * 				du tableau parcourue en ce moment 
	 */
	protected Line[] lines;
	protected int L;
	protected int cur = 0;
	
	//CONSTRUCTEURS 
	//AVEC DES LIGNES
	public PolyLine(Line... l){
		L = l.length;
		lines = l;
	}
	//AVEC DES POINTS (n+1 points pour n lignes)
	public PolyLine(Point... p){
		if(p.length<2)
			try {
				throw new Exception("Enter more than one Point to create a PolyLine");
			} catch (Exception e) {
				e.printStackTrace();
			}
		L = p.length-1;
		lines = new Line[L];
		for(int k = 0; k<L; k++){
			lines[k]=new Line(p[k].getX(),p[k].getY(),p[k+1].getX(),p[k+1].getY());
	}}
	// AVEC DES COORDONNEES (n+1 couples pour n lignes)
	public PolyLine(double x[], double y[]){
		if(x.length!=y.length || x.length<2 || y.length<2)
			try {
				throw new Exception("x and y must be of a same length >=2 x.length= "+ x.length+" y.length= "+y.length);
			} catch (Exception e) {
				e.printStackTrace();
			}
		L = x.length-1;
		lines = new Line[L];
		for(int k = 0; k<L; k++){
			lines[k]=new Line(x[k],y[k],x[k+1],y[k+1]);
		}
	}
	
	//METHODES
	public void next(JComponent target){
		lines[cur].next(target);
		if(lines[cur].hasEnded())
			moveLine();
	}
	//DEPLACE LA LIGNE APRES LA DERNIERE LIGNE SUR L'AXE X
	//POUR PERMETTRE LA REPETITION PERIODIQUE DU MOTIF FORME PAR LES LIGNES
	private void moveLine(){
		//distance entre la fin de la dernière ligne et le début de la première
		final double x_shift = lines[(cur-1+L)%L].getX_end()-lines[cur].getX_start();
		lines[cur]=lines[cur].getShifted(x_shift, 0);
		//on passe à la ligne suivante
		cur=(cur+1)%L;	
	}
	//RENVOIE LE SYMETRIQUE PAR RAPPORT A L'AXE X=50
	public PolyLine getSymmetric(){
		final Line[] l = new Line[L];
		for(int k = 0; k<L; k++){
			final double x_start = 100-lines[k].getX_start();
			final double x_end = 100-lines[k].getX_end();
			l[k] = new Line(x_start, lines[k].getY_start(),x_end, lines[k].getY_end());
		}	
		return new PolyLine(l);
	}
	//RENVOIE UNE NOUVELLE <PolyLine> DEPLACEE DE X ET Y
	public PolyLine getShifted(double x, double y){
		final Line [] l = new Line[L];
		for(int k = 0; k < L ; k++)
			l[k]=lines[k].getShifted(x, y);
		return new PolyLine(l);
	}
	//GENERE UNE TRAJECTOIRE ALEATOIRE DE <n> LIGNES
	//ET DONT LE MOTIF S'ETEND SUR UNE <period>
	public static PolyLine randomPolyLine(int n, double period){
		final double x[] = new double[n+1];
		final double y[] = new double[n+1];
		//X : on limite la largeur d'une ligne à 0.75*period
		double sum = 0;
		for(int k = 0; k<n; k++){
			sum+=Math.random()*(period-sum)*0.75;
			x[k]=sum;
		}
		x[n]=period;
		//Y : y_end =(y+1)_start
		for(int k = 1; k<n+1; k++){
			if(k%2==0)
				y[k]=y[k-1];
			else
				y[k]=Math.random()*100;	
		}
		y[0]=y[n];
		return (Math.random()<0.5)?new PolyLine(x,y):new PolyLine(x,y).getSymmetric();
	}
	//IDENTIQUE A <randomPolyLine(int n, double period)
	// MAIS AVEC UNE <period> ALEATOIRE ENTRE 2*n ET 100
	public static PolyLine randomPolyLine(int n){
		final double period = Math.min(2*n,50)+Math.random()*50;
		return randomPolyLine(n, period);
	}
	@Override
	//ON CLONE <PolyLine> EN MODIFIANT L'ATTRIBUT <lines>
	//CAR LES <Line> QU'IL CONTIENT FONCTIONNENT PAR REFERENCE
	public PolyLine clone(){
		PolyLine clone = null;
		try {
			clone = (PolyLine)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		final Line [] l = new Line [L];
		for(int k = 0; k<L; k++)
			l[k]=lines[k].clone();
		clone.setLines(l);
		return clone;
	}
	//GETTERS & SETTERS
	public Line[] getLines() {
		return lines;
	}
	public void setLines(Line[] lines) {
		this.lines = lines;
	}
}
