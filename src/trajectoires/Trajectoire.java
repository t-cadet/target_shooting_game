package trajectoires;

import javax.swing.JComponent;

public interface Trajectoire extends Cloneable {
	//METHODES
	public abstract void next(JComponent target);
	public default Trajectoire getSymmetric(){
		return null;
	}
	public default Trajectoire clone(){
		return null;
	}
}