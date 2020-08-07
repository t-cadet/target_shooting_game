package outils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

/**
 * Objet permettant de peindre une image/texture/couleur
 * d'une façon donnée et à un endroit donné via la méthode
 * paintShinBackground
 * @param fitType façon dont on veut peindre l'image 
 * (valeurs possibles : NO_REPEAT, REPEAT, FILL, FIT)
 * @param position endroit où on veut peindre l'image
 * (valeurs possibles : CENTER, EAST, ..., TOP_LEFT, ...)
 *
 */
public class ShinBackground {
	public final static int NO_REPEAT = 0;
	public final static int REPEAT = -1;
	public final static int FILL = -2;
	public final static int FIT = -3;
	
	public final static int CENTER = 9;
	public final static int EAST = 10;
	public final static int SOUTH = 11;
	public final static int WEST = 12;
	public final static int NORTH = 13; 
	
	public final static int TOP_LEFT = 14;
	public final static int TOP_RIGHT = 15;
	public final static int BOTTOM_RIGHT = 16;
	public final static int BOTTOM_LEFT = 17;
	
	protected int fitType = NO_REPEAT;
	protected int position = CENTER;
	
	protected BufferedImage imageBackground;
	protected Color colorBackground;
	protected BufferedImage actualBackground;
	
	protected Object lock = new Object();
	
	// CONSTRUCTEURS
	public ShinBackground(Color c){
		colorBackground = c;
	}
	public ShinBackground(BufferedImage im){
		imageBackground = im;
	}
	public ShinBackground(BufferedImage im, int cst){
		imageBackground = im;
		if(cst<=0)
			fitType = cst;
		else
			position = cst;
	}
	public ShinBackground(BufferedImage im, int pos, int fitType){
		imageBackground = im;
		if(pos<=0)
			this.fitType = pos;
		else
			position = pos;
		if(fitType<=0)
			this.fitType = fitType;
		else
			position = fitType;
	}
	
	// PAINT LE FOND DE LA FACON INDIQUEE PAR <fitType> ET A L'ENDROIT INDIQUE PAR <position>
	// NOTE : si l'image et la couleur sont non nuls, seule l'image est painte.
	public void paintShinBackground(int w, int h, Graphics gPanel){
		final BufferedImage buffer = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffer.getGraphics();
		if(imageBackground!=null){
			final int wi = imageBackground.getWidth();
			final int hi = imageBackground.getHeight();
			
			switch(fitType){
				case NO_REPEAT:
					positionImage(w, h, wi, hi, g);
				break;
				case REPEAT:
					Graphics2D g2d = (Graphics2D)g;
					Rectangle anchor = positionAnchor(w, h, wi, hi);
					TexturePaint paint = new TexturePaint(imageBackground, anchor);
					g2d.setPaint(paint);
					g2d.fillRect(0, 0, w, h);
				break;
				case FILL:
					g.drawImage(imageBackground, 0, 0, w, h, null);
				break;
				case FIT:
					final float ratio = (float)wi/(float)hi;
					final float hFit = (h <= w/ratio) ? h : w/ratio;
					final float wFit = hFit*ratio;
					positionImage(w, h, (int)wFit, (int)hFit, g);
				break;
				default:
				try {
					throw new Exception("variable <fitType> cannot take such value");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}	
				
			
		}
		else{
			g.setColor(colorBackground);
			g.fillRect(0, 0, w, h);
		}
		actualBackground = buffer;
		gPanel.drawImage(actualBackground, 0, 0, null);
		
	}
	// POSITIONNE L'IMAGE EN FONCTION DE <position>
	protected void positionImage(int w, int h, int wi, int hi, Graphics g){
		switch(position){
			case CENTER:
				g.drawImage(imageBackground,((int)(w-wi)/2),((int)(h-hi)/2), wi, hi, null);
			break;
			case EAST:
				g.drawImage(imageBackground, w-wi,((int)(h-hi)/2), wi, hi, null);
			break;
			case SOUTH:
				g.drawImage(imageBackground, ((int)(w-wi)/2), h-hi, wi, hi, null);
			break;
			case WEST:
				g.drawImage(imageBackground, 0, ((int)(h-hi)/2), wi, hi, null);
			break;
			case NORTH:
				g.drawImage(imageBackground, ((int)(w-wi)/2), 0, wi, hi, null);
			break;
			case TOP_LEFT:
				g.drawImage(imageBackground, 0, 0, wi, hi, null);
			break;
			case TOP_RIGHT:
				g.drawImage(imageBackground, w-wi, 0, wi, hi, null);
			break;
			case BOTTOM_RIGHT:
				g.drawImage(imageBackground, w-wi, h-hi, wi, hi, null);
			break;
			case BOTTOM_LEFT:
				g.drawImage(imageBackground, 0, h-hi, wi, hi, null);
			break;
			default:
			try {
				throw new Exception("variable <position> cannot take such value");
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
				
		}
			
	}
	// RETOURNE LE RECTANGLE SERVANT D'ANCRE POUR LA TEXTURE EN FONCTION DE <position>
	protected Rectangle positionAnchor(int w, int h, int wi, int hi){
		switch(position){
		case CENTER:
			return new Rectangle(((int)(w-wi)/2),((int)(h-hi)/2), wi, hi);
		case EAST:
			return new Rectangle(w-wi,((int)(h-hi)/2), wi, hi);
		case SOUTH:
			return new Rectangle(((int)(w-wi)/2), h-hi, wi, hi);
		case WEST:
			return new Rectangle(0, ((int)(h-hi)/2), wi, hi);
		case NORTH:
			return new Rectangle(((int)(w-wi)/2), 0, wi, hi);
		case TOP_LEFT:
			return new Rectangle(0, 0, wi, hi);
		case TOP_RIGHT:
			return new Rectangle(w-wi, 0, wi, hi);
		case BOTTOM_RIGHT:
			return new Rectangle(w-wi, h-hi, wi, hi);
		case BOTTOM_LEFT:
			return new Rectangle(0, h-hi, wi, hi);
		default:
		try {
			throw new Exception("variable <position> cannot take such value");
		} catch (Exception e) {
			e.printStackTrace();
		}
		break;
		}
		return null;		
	}
	// GETTERS & SETTERS
	public int getFitType() {
		return fitType;
	}
	public void setFitType(int fitType) {
		this.fitType = fitType;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public BufferedImage getImageBackground() {
		return imageBackground;
	}
	public void setImageBackground(BufferedImage imageBackground) {
		this.imageBackground = imageBackground;
	}
	public Color getColorBackground() {
		return colorBackground;
	}
	public void setColorBackground(Color colorBackground) {
		this.colorBackground = colorBackground;
	}
	public BufferedImage getActualBackground() {
		return actualBackground;
	}
}
