package model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.Serializable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import outils.ShinBackground;
import outils.ShinPanel;
import view.Fenetre;
import view.InterfaceLabel;
import view.InterfaceMenu;
import view.Main;
import view.ZoneTir;

@SuppressWarnings("serial")
public class Joueur implements Serializable{
	//ATTRIBUTS
	protected Fenetre frame;
	protected String nom;
	protected int hp;
	protected int score = 0;
	protected long temps;
	protected Arme equippedWeapon;
	protected Set<Arme> weapons = Collections.synchronizedSet(EnumSet.noneOf(Arme.class));//empêche les doublons
	protected Set<Modificateurs> mod=Collections.synchronizedSet(EnumSet.noneOf(Modificateurs.class));
	protected boolean cooldown = false;
	protected long previousShot;
	
	//CONSTRUCTEURS
	public Joueur(int hp, Fenetre f){		
		frame = f;		
		final Arme basicWeapon = Arme.LANCEPIERRE;
		loot(new Arme[]{basicWeapon},null);
		equip(basicWeapon);
		updateHp(Math.max(1,hp));
		updateScore(0);
	}
	public Joueur(int hp, Arme arme, Fenetre f){
		frame = f;
		loot(new Arme[]{arme},null);
		equip(arme);
		updateHp(Math.max(1,hp));
		updateScore(0);
	}
	
	//METHODES
	
	//RENVOIE VRAI SI LE JOUEUR A TOUCHE LA CIBLE
	public boolean hasHit(int x_centre, int y_centre, ShinPanel conteneur){
		double modR = 0;
		final Iterator<Modificateurs> li = mod.iterator();
		while(li.hasNext()){
			Modificateurs m = li.next();
			modR+=m.getModR();
		}	
		final int R = (int) ((1+modR)*equippedWeapon.getR());
		final AOE aoe = equippedWeapon.getAoe().setUp(x_centre, y_centre, R);
		//ColorCircle t = new ColorCircle(x_centre, y_centre, R);
		//System.out.println(aoe.collides(conteneur));
		return aoe.collides(conteneur, equippedWeapon.getImpact());
	}
	//GESTION DU COOLDOWN
	public void cooldown(){
		cooldown = true;
		previousShot = System.currentTimeMillis();
		final long waitingTime = getEquippedWeapon().getFireRate(); 
		final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.schedule(new Runnable(){
			public void run(){
				cooldown = false;
			}
		},waitingTime,TimeUnit.MILLISECONDS);
	}
	//RENVOIE <cooldown>
	public boolean isCoolingDown(){
		return cooldown;
	}
	//CALCULE LES DEGATS
	public int damage(){
		double modDamage = 0;
		final Iterator<Modificateurs> li = mod.iterator();
		while(li.hasNext())
			modDamage+=li.next().getModDamage();
		final int damage = (int) ((1+modDamage)*equippedWeapon.getDamage());
		return damage;		
	}
	//MET A JOUR LES PV 
	public void updateHp(int update){
		hp+=update;
		if(update!=0){
			frame.getInterfaceMenu().getLife1().setText(String.valueOf(hp));
			final Timer t = new Timer();
			final JLabel updateHP = frame.getInterfaceMenu().getUpdateHP();
			String string_update = update>0?"+ "+String.valueOf(update):"- "+String.valueOf(-update);
			updateHP.setText(string_update);
			t.schedule(new TimerTask(){
				@Override
				public void run() {
					if(updateHP.getText().equals(string_update))
						updateHP.setText("");
				}	
			}, 1000);
		}	
		if(hp<=0)
			kill();
	}
	//GERE LA DEFAITE
	public void kill(){
		frame.getInterfaceMenu().getMonTimer().stop();
		frame.getJeu().setSback(new ShinBackground(Color.BLACK));
		frame.getJeu().remove(frame.getZoneTir());
		final ShinPanel gameOver = new ShinPanel(new ShinBackground(Main.IMG.get("GameOver"),ShinBackground.FILL));
		gameOver.setPreferredSize(ZoneTir.SIZE);
		frame.getJeu().add(gameOver, BorderLayout.NORTH);
		frame.revalidate();
		Main.AMBIANCE.stop();
	}
	//MET A JOUR LE SCORE ET LA DIFFICULTE (vitesse et nb de cibles)
	public void updateScore(int update){
		score+=update;
		ZoneTir.NB_MAX_CIBLES = ZoneTir.NB_CIBLES_INITIAL+Math.max(0, score/20);
		ZoneTir.vitesse_seuil=ZoneTir.vitesse_initiale-Math.max(0, score*25);
		frame.getInterfaceMenu().getScore1().setText(String.valueOf(score));
		if(update!=0){
			final Timer t = new Timer();
			final JLabel updateScore = frame.getInterfaceMenu().getUpdateScore();
			String string_update = update>0?"+ "+String.valueOf(update):"- "+String.valueOf(-update);
			updateScore.setText(string_update);
			t.schedule(new TimerTask(){
				@Override
				public void run() {
					if(updateScore.getText().equals(string_update))
						updateScore.setText("");
				}	
			}, 500);
		}
	}
	//EQUIPE UNE ARME
	public void equip(Arme arme){
		equippedWeapon = arme;
		frame.getInterfaceMenu().getWeaponUsed().setText(equippedWeapon.describe());
	}
	//PERMET LE LOOT
	public void loot(Arme[] armes, Modificateurs[] mods){
		if(armes!=null)
			for(int i = 0; i<armes.length; i++){
				final Arme temp = armes[i];
				if(weapons.add(temp)){
					frame.getInterfaceMenu().getWeaponBox().addItem(temp);
					final JLabel newWeapon = frame.getInterfaceMenu().getNewWeapon();
					newWeapon.setText("+ "+temp.getName());
					final Timer t = new Timer();
					t.schedule(new TimerTask(){
						public void run(){
							if(newWeapon.getText().equals("+ "+temp.getName()))
								newWeapon.setText("");
						}
					}, 5000);
				}	
			}
		if(mods!=null)
			for(int i = 0; i<mods.length; i++){
				if(!mod.contains(mods[i])&& mod.add(mods[i]))
					activate(mods[i]);
			}
	}
	//PROGRAMME LA DUREE D'UN BUFF ET ACTIVE SON SOIN
	private void activate(Modificateurs modif){
		updateHp(modif.getHeal());
		InterfaceLabel lab = new InterfaceLabel(modif.toString(),Main.PURPLE);
		lab.setHorizontalAlignment(SwingConstants.CENTER);
		lab.setAlignmentX(Component.CENTER_ALIGNMENT);
		frame.getInterfaceMenu().getScrollPaneContent().add(lab);
		InterfaceMenu.setPCMinimumSize(95,15,lab);
		Timer selfDestruct = new Timer();
		selfDestruct.schedule(new TimerTask(){
			@Override
			public void run() {
				mod.remove(modif);
				frame.getInterfaceMenu().getScrollPaneContent().remove(lab);
				frame.getInterfaceMenu().getScrollPaneContent().repaint();
			}
		}, modif.getDuree());
	}
	//GETTERS & SETTERS
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = Math.max(1,hp);
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public long getTemps() {
		return temps;
	}
	public void setTemps(long temps) {
		this.temps = temps;
	}
	public Arme getEquippedWeapon() {
		return equippedWeapon;
	}
	public void setEquippedWeapon(Arme equippedWeapon) {
		this.equippedWeapon = equippedWeapon;
	}
	public long getPreviousShot() {
		return previousShot;
	}
	public void setPreviousShot(long previousShot) {
		this.previousShot = previousShot;
	}
	public Set<Arme> getWeapons() {
		return weapons;
	}
	public void setWeapons(Set<Arme> weapons) {
		this.weapons = weapons;
	}
	public Set<Modificateurs> getMod() {
		return mod;
	}
	public void setMod(Set<Modificateurs> mod) {
		this.mod = mod;
	}
}
