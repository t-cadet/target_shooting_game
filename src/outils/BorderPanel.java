package outils;

import java.awt.Color;
import java.awt.Graphics;

import menu.ImageBordure;
//PERMET D'AJOUTER FACILEMENT UNE IMAGE BORDURE A UNE CLASSE QUI HERITE DE SHINPANEL
@SuppressWarnings("serial")
public class BorderPanel extends ShinPanel {
	
	//ATTRIBUT
	ImageBordure imgBordure;
	
	//CONSTRUCTEURS
	public BorderPanel(Color c, ImageBordure imgBordure){
		super(c);
		this.imgBordure=imgBordure;
	}
	public BorderPanel(ShinBackground sBack, ImageBordure imgBordure){
		super(sBack);
		this.imgBordure = imgBordure;
	}
	
	//REDEFINITION DE <paintComponent>
	public void paintComponent(Graphics g){
		final int w = getWidth();
		final int h = getHeight();
		if(w!=0 && h!=0){
			super.paintComponent(g);
			if(imgBordure!=null  && (w>=imgBordure.getSize().getWidth() && h>=imgBordure.getSize().getHeight()))
				g.drawImage(imgBordure.getBorder(this),0,0,this);
		}
	}
}
