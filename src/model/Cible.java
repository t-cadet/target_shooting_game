package model;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;

import outils.ShinBackground;
import outils.ShinPanel;
import trajectoires.Trajectoire;
import view.Fenetre;
import view.Main;

@SuppressWarnings("serial")
public class Cible extends ShinPanel{
	// Attributs

	protected Trajectoire trajectoire;
	final protected Trajectoire copyOfTrajectoire;
	protected int pdv;
	protected Arme[] loot;
	protected Modificateurs [] buff;
	protected double vitesse;//On ne s'en sert pas pour l'instant, on pourrait l'utiliser pour moduler la vitesse d'appel de la fonction next() de Trajectoire
	protected boolean ennemi;
	
	protected boolean hurt = false;
	protected boolean dead = false;
	
	//IMAGES
	protected BufferedImage image;    
	protected BufferedImage imageTouche;
	protected BufferedImage imageDetruit;//Nous n'avons pas ces images pour le moment

	// CONSTRUCTEUR
	public Cible(String key, Trajectoire trajectoire, int pdv, Arme[] lootArme, Modificateurs[] buff, boolean ennemi) {
		super(new ShinBackground(Main.IMG.get(key),ShinBackground.FILL));
		
		this.image=Main.IMG.get(key);
		this.imageTouche=Main.IMG.get(key+Main.TOUCHE);
		//this.imageDetruit = Main.IMG_TARGET.get(key+Main.DETRUIT);
		
		this.trajectoire = trajectoire;
		this.copyOfTrajectoire = trajectoire.clone();
		this.pdv = pdv;
		this.loot = lootArme;
		this.buff = buff;
		this.vitesse = 1+Math.random();
		this.ennemi = ennemi;
		
		//LISTENER
		addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				if(Fenetre.player.isCoolingDown()){
					Main.COOLDOWN.setMicrosecondPosition(0);
					Main.COOLDOWN.start();
				}
				else{
					//BRUIT
					final Clip sound = Fenetre.player.getEquippedWeapon().getSound();
					if(sound!=null){
						sound.setMicrosecondPosition(0);
						sound.start();
					}
					//VERIFIE SI ON TOUCHE
					Fenetre.player.cooldown();
					final Cible target =(Cible)e.getSource();
					if(Fenetre.player.hasHit(e.getX(),e.getY(),target)){
						target.toucher(Fenetre.player.damage());
					}
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
		});
		addMouseMotionListener(new MouseMotionListener(){
			@Override
			public void mouseDragged(MouseEvent e) {
				if(Fenetre.player.isCoolingDown()){
					Main.COOLDOWN.setMicrosecondPosition(0);
					Main.COOLDOWN.start();
				}
				else{
					//BRUIT
					final Clip sound = Fenetre.player.getEquippedWeapon().getSound();
					if(sound!=null){
						sound.setMicrosecondPosition(0);
						sound.start();
					}
					//VERIFIE SI ON TOUCHE
					Fenetre.player.cooldown();
					final Cible target =(Cible)e.getSource();
					if(Fenetre.player.hasHit(e.getX(),e.getY(),target)){
						target.toucher(Fenetre.player.damage());
					}
				}
				
			}
			@Override
			public void mouseMoved(MouseEvent e) {				
			}
			
		});
	}
	//METHODES
	//verifie si la cible est sortie ou non de l'écran
	public boolean sortie(){
		final int largPanel = this.getParent().getWidth();
		final int hautPanel = this.getParent().getHeight();
           
		final int l = getWidth();
		final int h = getHeight();
		
		if((getX()+l)<=0 || getX()>=largPanel || (getY()+h)<=0 || getY()>=hautPanel){//si sortie de l'écran
			if(ennemi){	//met Ã  jour pv du joueur si ennemi sort		
				Fenetre.player.updateHp(-this.pdv); 
			}
			return true;
		} 
		return false;        
	}
	public void toucher(int degat){ 
			this.pdv -= degat;
			int update = 1;
			if(dead)
				update=0;
			if(!ennemi){
				update=-update;
				Fenetre.player.updateScore(update);
			}
			if (pdv <=0){
				Fenetre.player.updateScore(update);
				//changer image
				//this.getSback().setImageBackground(imageDetruit);
				dead = true; 
				Fenetre.player.loot(this.getLoot(),this.getBuff());
			}
			else if(!hurt){
				this.getSback().setImageBackground(imageTouche);
				hurt = true; 
			}
	}		
	//GETTERS & SETTERS	
	public double getVitesse() {
		return vitesse;
	}
	public void setVitesse(double vitesse) {
		this.vitesse = vitesse;
	}
	public boolean isEnnemi() {
		return ennemi;
	}
	public void setEnnemie(boolean ennemi) {
		this.ennemi = ennemi;
		}
	public Trajectoire getTrajectoire() {
		return trajectoire;
	}
	public void setTrajectoire(Trajectoire trajectoire) {
		this.trajectoire = trajectoire;
	}
	public Arme[] getLoot() {
		return loot;
	}
	public void setLoot(Arme[] loot) {
		this.loot = loot;
	}
	public Modificateurs[] getBuff() {
		return buff;
	}
	public void setBuff(Modificateurs[] buff) {
		this.buff = buff;
	}
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	public Trajectoire getCopyOfTrajectoire() {
		return copyOfTrajectoire;
	}
}