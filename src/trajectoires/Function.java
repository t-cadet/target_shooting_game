package trajectoires;

import javax.swing.JComponent;
/* Permet de déplacer un JComponent selon une fonction mathématique
 * 
 * Les fonctions SIN et COS sont centrées sur l'axe y=50%
 * La période est exprimée en % de la largeur du conteneur
 * 
 * Lorsque que la sinusoïde vaut 1 cela correspond à 1% sur l'axe y 
 * (+50% car ils sont centrés en y=50%)
 * Une amplitude de 50 permet donc d'aller de 0 à 100%
 * 
 * Par défaut le sens de parcours est vers la droite si x_start<=50%
 * vers la gauche sinon, avec un pas sur l'axe des abcisses de 0.01%
 * 
 * ABS_SIN/ABS_COS : 
 * valeur absolue du sin/cos elles  marchent de la même façon 
 * que SIN/COS mais elles sont centrées sur y = 0%
 * NB : om est divisé par 2 car à cause de abs la période passe de 2*Pi à Pi
 * il faut donc new_om = Pi/period = om/2
 * 
 * LN : amplitude*ln(period*x)
 * EXP : amplitude*exp(period*x)
 * POW : amplitude*x^(periode)
 */
public class Function implements Trajectoire{
	public final static String SIN = "sin";
	public final static String COS = "cos";
	public final static String ABS_SIN = "abs_sin";
	public final static String ABS_COS = "abs_cos";
	public final static String LN = "ln";
	public final static String EXP = "exp";
	public final static String POW = "pow";
	
	//Mode d'appel de setLocation, par défaut setLocation(X,Y);
	//le _ représente un moins
	//NB : il existe 8 autres possibilités (total de 16), elles sont déjà réalisables 
	//en donnant une amplitude négative, ou en inversant le sens de parcours
	//NB2 : pas implanté dans <Line> car toutes les trajectoires sont réalisables
	public static final int XY = 1;
	public static final int YX = 2;
	public static final int XX = 3;
	public static final int YY = 4;
	
	/*Ces 4 modes semblent peu utiles
	 * Je les laisse en commentaire au cas où
	public static final int _XX = -1;
	public static final int _YY = -2;	
	public static final int X_X = -3;
	public static final int Y_Y = -4;*/
	protected int mode = XY; 
	
	//ATTRIBUTS
	protected double x_start;
	protected double y_start;
	protected double amp;
	protected double period;
	protected String function="";
	
	protected double x = 0;
	protected double step;
	//CONSTRUCTEUR X ET Y CORRESPONDENT A L'ORIGINE DE LA FONCTION
	public Function(double x_start, double y_start, double amplitude, double period, String function) {
		this.x_start = x_start;
		this.y_start = y_start;
		this.amp = amplitude;
		this.period = period;
		this.function = function;
		
		step = (x_start<=50)? 0.01:-0.01;
	}
	@Override
	//PERMET D'AVANCER/RECULER SELON LA VALEUR DE <step>
	public void next(JComponent target) {
		x+=step;
		move(target);
	}
	//INVERSE LE SENS DE PARCOURS
	public void reverse(){
		step=-step;
	}
	//RENVOIE UNE NOUVELLE <Function> DONT LE POINT
	//DE DEPART EST SYMETRIQUE PAR RAPPORT A X=50
	//ET DONT LE SENS DE PARCOURS EST INVERSE
	//NB : SI LA FONCTION N'EST PAS PERIODIQUE IL NE 
	//S'AGIT PAS D'UNE VRAIE SYMETRIE
	public Function getSymmetric(){
		final Function sym = this.clone();
		sym.setX_start(100-x_start);
		sym.setStep(-step);
		return sym;
	}
	//CLONE LA <Function>
	public Function clone(){
		Function clone = null;
		try {
			clone = (Function)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return clone;
	}
	//BOUGE LE <JComponent> SELON L'ATTRIBUT <function> CHOISI
	private void move(JComponent target){
		final int W = target.getParent().getWidth();
		final int H = target.getParent().getHeight();
		
		final int w = target.getWidth();
		final int h = target.getHeight();
		final double om = 2*Math.PI/period;//oméga = 2*Pi/T
		
		final int new_x = (int) ((x_start+x)*(W-w)/100);
		int new_y = 0;
		
		switch(function){
		case SIN :
			new_y = (int) ((y_start+amp*Math.sin(om*x)+50)*(H-h)/100);
		break;
		case COS :
			new_y = (int) ((y_start+amp*Math.cos(om*x)+50)*(H-h)/100);
		break;	
		case ABS_SIN :
			new_y = (int) ((y_start+amp*Math.abs(Math.sin(om/2*x)))*(H-h)/100);
		break;
		case ABS_COS :
			new_y = (int) ((y_start+amp*Math.abs(Math.cos(om/2*x)))*(H-h)/100);
		break;
		case LN :
			new_y = (int) ((y_start+amp*Math.log(period*Math.abs(x+0.000001)))*(H-h)/100);//on fait en sorte que l'arg du log soit !=0
		break;
		case EXP :
			new_y = (int) ((y_start+amp*Math.exp(period*x))*(H-h)/100);
		break;
		case POW :
			new_y = (int) ((y_start+amp*Math.pow(x,period))*(H-h)/100);
		break;	
		default:
			try {
				throw new Exception("There is no such function : "+function);
			} catch (Exception e) {
				e.printStackTrace();
			}
		break;	
		}
		moveMode(target, new_x, new_y);
	}
	//CHOISIT LE MODE DE <setLocation> EN FONCTION de <mode>
	private void moveMode(JComponent target, int new_x, int new_y){
		switch(mode){
			case XY:
				target.setLocation(new_x, new_y);
			break;
			case YX:
				target.setLocation(new_y, new_x);
			break;	
			case XX:
				target.setLocation(new_x, new_x);
			break;
			case YY:
				target.setLocation(new_y, new_y);
			break;
			/*Ces 4 modes semblent peu utiles
			case _XX:
				target.setLocation(-new_x, new_x);
			break;

			case _YY:
				target.setLocation(-new_y, new_y);
			break;
			case X_X:
				target.setLocation(new_x, -new_x);
			break;	
			case Y_Y:
				target.setLocation(new_y, -new_y);
			break;*/
			default:
			try {
				throw new Exception("No such mode : "+mode);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;		
		}
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
	public double getAmp() {
		return amp;
	}
	public void setAmp(double amp) {
		this.amp = amp;
	}
	public double getPeriod() {
		return period;
	}
	public void setPeriod(double period) {
		this.period = period;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getStep() {
		return step;
	}
	public void setStep(double step) {
		this.step = step;
	}
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	
}
