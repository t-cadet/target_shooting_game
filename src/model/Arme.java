package model;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;

import view.Main;


public enum Arme{
	//ENUM
	LANCEPIERRE("Lance-pierre",null,null,new Color(0x754B1C), Main.LANCE_PIERRE,1000,1,10, new ColorCircle()),
	LASER("Laser", null, null,Main.MATRIX_GREEN,Main.LASER, 250, 8, 20, new ColorCircle()),
	ERASER_3000("ERASER_3000", null, null, new Color(0,0,0,0), Main.ERASER_3000, 0, 5, 25, new ColorCircle());
	
	//ATTRIBUTS
	private String name="";
	private BufferedImage image;
	private BufferedImage scope;
	private Color impact;
	private Clip sound;
	private long fireRate;
	private int damage;
	private int R;
	private AOE aoe;
	
	//CONSTRUCTEUR	
	private Arme(String name, BufferedImage image, BufferedImage scope, Color impact, Clip sound, long fireRate,
			int damage, int r, AOE aoe) {
		this.name = name;
		this.image = image;
		this.scope = scope;
		this.impact = impact;
		this.sound = sound;
		this.fireRate = fireRate;
		this.damage = damage;
		this.R = r;
		this.aoe = aoe;
	}
	//METHODES
	public static Arme getRandomArme(){
		Arme[] mod = Arme.values();
		final int index = (int) (Math.random()*mod.length);
		return mod[index];
	}
	public boolean equals(Arme arme){
		return this.name.equals(arme.name);
	}
	public String toString(){
		return name;
	}
	//GETTERS
	public String getName() {
		return name;
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getScope() {
		return scope;
	}

	public Color getImpact() {
		return impact;
	}

	public Clip getSound() {
		return sound;
	}

	public long getFireRate() {
		return fireRate;
	}

	public int getDamage() {
		return damage;
	}

	public int getR() {
		return R;
	}

	public AOE getAoe() {
		return aoe;
	}
	public String describe() {
		return "R: "+R+"   dmg: "+damage+"   CD: "+(double)(Math.ceil(fireRate/100)/10);
	}
}	