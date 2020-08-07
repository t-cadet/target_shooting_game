package trajectoires;

import javax.swing.JComponent;

public class Line implements Trajectoire{
	//ATTRIBUTS
	protected double x_start;
	protected double y_start;
	protected double x_end;
	protected double y_end;
	protected int r = 0;
	protected boolean terminus = false;
	//CONSTRUCTEUR
	//Prend les coordonnées du début et de la fin de la ligne en pourcentage
	public Line(double x_start, double y_start, double x_end, double y_end) {
		this.x_start = x_start;
		this.y_start = y_start;
		this.x_end = x_end;
		this.y_end = y_end;
	}
	//METHODES
	//FAIT RECULER LA CIBLE
	public void previous(JComponent target) {
		r--;
		move(target);
	}
	@Override
	//AVANCE LA CIBLE
	public void next(JComponent target) {
		r++;
		move(target);
	}
	public boolean hasEnded(){
		return terminus;
	}
	//Le calcul x_start*(W-w) (idem pour y) assure que la cible apparaît
	//entièrement dans le panel au début pour des coordonnées entre 0 et 1
	//(à condition qu'elle soit de taille inférieure au panel)
	//r*Math.cos(angle) permet d'incrémenter la distance à start lorsque
	//r augmente (idem pour y).
	private void move(JComponent target){
		final int W = target.getParent().getWidth();
		final int H = target.getParent().getHeight();
		
		final int w = target.getWidth();
		final int h = target.getHeight();
		
		final double dx = (x_end-x_start)*(W-w)/100;
		final double dy = (y_end-y_start)*(H-h)/100;
		
		final double angle = Math.atan2(dy,dx);
		
		final double rx = r*Math.cos(angle);
		final double ry = r*Math.sin(angle);
		
		final int new_x = (int) (x_start*(W-w)/100+rx);
		final int new_y = (int) (y_start*(H-h)/100+ry);
		
		target.setLocation(new_x,new_y);
		//Si la distance parcourue par <Cible> est supérieure
		// à la longueur de la ligne, la ligne est finie.
		if(rx*rx + ry*ry >= dx*dx + dy*dy)
			terminus = true;
	}
	//RENVOIE UNE NOUVELLE LIGNE AVEC START ET END INVERSE
	public Line getReverse(){
		return new Line(x_end,y_end,x_start, y_start);
	}
	public void reset(){
		r = 0;
		terminus = false;
	}
	//RENVOIE UNE NOUVELLE <Line> DEPLACEE DE X ET Y
	public Line getShifted(double x, double y){
		return new Line(x_start+x, y_start+y, x_end+x, y_end+y);
	}
	@Override
	//CLONE LA LIGNE
	public Line clone(){
		Line clone = null;
		try {
			clone = (Line)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}

	//GETTERS & SETTERS
	public double getX_start() {
		return x_start;
	}
	public void setX_start(double x_start) {
		this.x_start = x_start;
	}
	public double getY_start() {
		return y_start;
	}
	public void setY_start(double y_start) {
		this.y_start = y_start;
	}
	public double getX_end() {
		return x_end;
	}
	public void setX_end(double x_end) {
		this.x_end = x_end;
	}
	public double getY_end() {
		return y_end;
	}
	public void setY_end(double y_end) {
		this.y_end = y_end;
	}
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}

}
