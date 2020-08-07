package view;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import menu.ImageBordure;
import outils.Music;
import trajectoires.Function;
import trajectoires.PolyLine;
import trajectoires.Trajectoire;

public class Main {
	//MUSIQUES
	public final static Clip BOUTON_HOVER = Music.getClip(new File("son/button_hover.wav"));
	public final static Clip LANCE_PIERRE = Music.getClip(new File("son/lance_pierre.wav"));
	public final static Clip LASER = Music.getClip(new File("son/laser_sound.wav"));
	public final static Clip ERASER_3000 = Music.getClip(new File("son/Eraser.wav"));
	public final static Clip COOLDOWN = Music.getClip(new File("son/cooldown.wav"));
	public final static Clip AMBIANCE = Music.getClip(new File("son/Western_theme_2.wav"));
	//IMAGES
	public final static Hashtable<String, BufferedImage> IMG = new Hashtable<String, BufferedImage>(); 
	public final static String TOUCHE = "touche";
	public final static String DETRUIT = "detruit";
	//TRAJECTOIRES
	public final static ArrayList<Trajectoire> Trajectoires = new ArrayList<Trajectoire>();
	//BORDURE
	public static ImageBordure ib;
	public static ImageBordure ib2;
	//COULEUR
	public final static Color PURPLE = new Color(0x9370DB); 
	public final static Color MATRIX_GREEN = new Color(0x00FF11);
	public final static Color GOLD = new Color(0xFFD700);
	public static void main(String[] args){
		//IMAGES
		try {
			//FONDS
			IMG.put("fondZoneTir", ImageIO.read(new File("./image/fond.png")));
			IMG.put("MLP",ImageIO.read(new File("image/MLP.jpg")));
			IMG.put("GameOver", ImageIO.read(new File("image/game_over.png")));
			//MENU ACCUEIL
			IMG.put("ArrowLeft",ImageIO.read(new File("image/imLeft.png")));
			IMG.put("ArrowRight",ImageIO.read(new File("image/imRight.png")));
			//CIBLES
			IMG.put("Cactus3",ImageIO.read(new File("image/cibles/Cactus3.png")));
			IMG.put("Gentil1"+TOUCHE,ImageIO.read(new File("image/cibles/Gentil1_touche.png")));
			IMG.put("Gentil1",ImageIO.read(new File("image/cibles/Gentil1.png")));
			IMG.put("Mechant1"+TOUCHE,ImageIO.read(new File("image/cibles/Mechant1_touche.png")));
			IMG.put("Mechant1",ImageIO.read(new File("image/cibles/Mechant1.png")));
			IMG.put("Mechant2"+TOUCHE,ImageIO.read(new File("image/cibles/Mechant2_touche.png")));
			IMG.put("Mechant2",ImageIO.read(new File("image/cibles/Mechant2.png")));
			//IMG BORDURES
		  	IMG.put("coin1tl",ImageIO.read(new File("./image/bordure1/coin1tl.png")));
	        IMG.put("top1", ImageIO.read(new File("./image/bordure1/top1.png"))); 
	        IMG.put("coin1tr",ImageIO.read(new File("./image/bordure1/coin1tr.png")));	    	        
	        IMG.put("left1",ImageIO.read(new File("./image/bordure1/left1.png")));
	        IMG.put("right1",ImageIO.read(new File("./image/bordure1/right1.png")));
	        IMG.put("coin1bl",ImageIO.read(new File("./image/bordure1/coin1bl.png"))); 
	        IMG.put("bot1",ImageIO.read(new File("./image/bordure1/bot1.png"))); 
	        IMG.put("coin1br",ImageIO.read(new File("./image/bordure1/coin1br.png")));
	        IMG.put("filler", new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));
	        IMG.put("lilBlackSquare", ImageIO.read(new File("./image/LilBlackSquare.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//TRAJECTOIRES
		//SIN
		Trajectoires.add(new Function(0,0,50,20,Function.SIN));
		Trajectoires.add(Trajectoires.get(0).getSymmetric());
		//ABS_SIN
		Trajectoires.add(new Function(0,100,-40,20,Function.ABS_SIN));
		Trajectoires.add(Trajectoires.get(2).getSymmetric());
		//FONCTION CARRE
		Trajectoires.add(new PolyLine(new Point(0,50), new Point(10,50), new Point(10,100), new Point(20,100), new Point(20,50)));
		Trajectoires.add(Trajectoires.get(4).getSymmetric());
		//FONCTION TRIANGLE
		Trajectoires.add(new PolyLine(new Point(0,100), new Point(10,0), new Point(20,100)));
		Trajectoires.add(Trajectoires.get(6).getSymmetric());
		//ALEATOIRE  N = 3, PERIOD = 20;
		Trajectoires.add(PolyLine.randomPolyLine(3, 20));
		Trajectoires.add(Trajectoires.get(8).getSymmetric());
		//COS
		Trajectoires.add(new Function(0,0,-50,20,Function.COS));
		Trajectoires.add(Trajectoires.get(10).getSymmetric());
		//ABS_COS
		Trajectoires.add(new Function(0,0,40,20, Function.ABS_COS));
		Trajectoires.add(Trajectoires.get(12).getSymmetric());
		ib = new ImageBordure(
					IMG.get("coin1tl"),
					IMG.get("top1"),
					IMG.get("coin1tr"),
					IMG.get("left1"),
					IMG.get("right1"),
					IMG.get("coin1bl"),
					IMG.get("bot1"),
					IMG.get("coin1br")	  	
		);
		ib2 = new ImageBordure(
					IMG.get("coin1tl"),
					IMG.get("top1"),
					IMG.get("coin1tr"),
					IMG.get("left1"),
					IMG.get("right1"),
					IMG.get("filler"),
					IMG.get("bot1"),
					IMG.get("filler")
		);

		try {
			 UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (InstantiationException e) {}
		catch (ClassNotFoundException e) {}
		catch (UnsupportedLookAndFeelException e) {}
		catch (IllegalAccessException e) {}
		
		@SuppressWarnings("unused")
		Fenetre fen = new Fenetre();

		
	}

}
