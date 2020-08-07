package view;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.Clip;

import model.Arme;
import model.Cible;
import model.Modificateurs;
import outils.ShinPanel;
import trajectoires.PolyLine;
import trajectoires.Trajectoire;

@SuppressWarnings("serial")
public class ZoneTir extends ShinPanel{
	public final static double RATIO_SIZE=0.75;
	public final static int NB_IMG = 2;
	public final static Dimension SIZE = new Dimension(Fenetre.W,(int)Math.floor(RATIO_SIZE*Fenetre.H));
	
	public static final long vitesse_initiale = 5000;
	public static long vitesse_seuil = vitesse_initiale;
	public final static int dt =1000; // en µs
	public static final int NB_CIBLES_INITIAL = 10;
	public static int NB_MAX_CIBLES = NB_CIBLES_INITIAL;
	
	protected long vitesse = 0;
	protected ArrayList<Cible> listeCible;
	protected long start_time;
	
	//CONSTRUCTEUR
	public ZoneTir(){
		setLayout(null);
		setPreferredSize(SIZE);
		setSize(SIZE);
		
		//LISTENER
		addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
			}
			@Override
			public void mousePressed(MouseEvent e) {

				if(Fenetre.player.isCoolingDown()){
					Main.COOLDOWN.setMicrosecondPosition(0);
					Main.COOLDOWN.start();
				}
				else{
					Fenetre.player.cooldown();
					final Clip sound = Fenetre.player.getEquippedWeapon().getSound();
					if(sound!=null){
						sound.setMicrosecondPosition(0);
						sound.start();
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
					Fenetre.player.cooldown();
					final Clip sound = Fenetre.player.getEquippedWeapon().getSound();
					if(sound!=null){
						sound.setMicrosecondPosition(0);
						sound.start();
					}	
				}}

			@Override
			public void mouseMoved(MouseEvent e) {}
			
		});
	}
	//METHODES
	//ON CREE UNE METHODE POUR COMMENCER LE JEU
	public void startNewGame(){
		start_time=System.currentTimeMillis();
		listeCible = new ArrayList<Cible>();
		//TIMER
		final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				if(vitesse>=vitesse_seuil){
					vitesse=vitesse-Math.max(0, vitesse_seuil);
					//AJOUT DES CIBLES
					if(listeCible.size()<=Math.ceil(NB_MAX_CIBLES/2)){
						final int ncall = (int) Math.ceil(Math.random()*NB_MAX_CIBLES/2);
						for(int i = 0; i<ncall; i++){
							addTarget();
						}
					}
					//SUPPRESSION OU AVANCEMENT DES CIBLES
					final ListIterator<Cible> it = listeCible.listIterator();
					while(it.hasNext()){
						Cible target = it.next();
						//suppression des cibles si sortie écran ou mort
						if(target.isDead()||target.sortie()){
							it.remove();
							target.setVisible(false);
							remove(target);
							Main.Trajectoires.add(target.getCopyOfTrajectoire());				
						}
						else{//On fait avancer les cibles
							//if((System.currentMillis-start_time)%target.getVitesse()){
							target.getTrajectoire().next(target);
						}
					}
				}else
					vitesse+=dt;
		}	

		},0, dt, TimeUnit.MICROSECONDS);	
		Main.AMBIANCE.setMicrosecondPosition(0);
		Main.AMBIANCE.loop(Clip.LOOP_CONTINUOUSLY);
	}
	//PERMET L'AJOUT D'UNE CIBLE
	private void addTarget(){
		Cible new_target = null;
		//On tire au hasard si la cible est ennemie ou neutre
		if(Math.random()>0.1){// mechant
			final String number = String.valueOf((int) (1+Math.random()*ZoneTir.NB_IMG));
			new_target = new Cible("Mechant"+number, choseTrajectoire(), (int) Math.ceil(((float)Fenetre.player.getScore()+1)/5), choseWeapons(), choseBuffs(), true);	
		}else // gentil
			new_target = new Cible("Gentil1", choseTrajectoire(), 1000+Fenetre.player.getScore(), null, null, false);
		
		listeCible.add(new_target);	 
		add(new_target);
		InterfaceMenu.setPCSize(15, 40, new_target);
	}
	//CHOISIT TRAJECTOIRE DE LA LISTE OU AVEC UNE TRAJECTOIRE
	//ALEATOIRE SI ELLES SONT TOUTES UTILISEES.
	private Trajectoire choseTrajectoire(){
		Trajectoire trajectoire;
		//Choix d'une trajectoire dans la liste du Main
		if(Main.Trajectoires.size()>0){
			final int index = (int) (Math.random()*Main.Trajectoires.size());
			trajectoire = Main.Trajectoires.get(index);
			Main.Trajectoires.remove(index);
		}else{//on crée une trajectoire aléatoire si toutes les trajectoires de la liste sont utilisées
			//final int n = NB_MAX_CIBLES; //car le score peut être négatif
			trajectoire = PolyLine.randomPolyLine(NB_MAX_CIBLES);
		}
		return trajectoire;
	}
	//CHOISIT LES BUFFS
	private Modificateurs[] choseBuffs(){
		final double probability = Math.random();
		if(probability<0.01){
			Modificateurs mods[] = {Modificateurs.getRandomModificateur(),Modificateurs.getRandomModificateur()};
			return mods;
		}
		if(probability<0.1){
			Modificateurs mods[] = {Modificateurs.getRandomModificateur()};
			return mods;
		}
		return null;			
	}
	//CHOISIT LES ARMES
	private Arme[] choseWeapons(){
		final double probability = Math.random();
		if(probability<0.001){
			Arme weapons[] = {Arme.getRandomArme(),Arme.getRandomArme()};
			return weapons;
		}
		if(probability<0.1){
			Arme weapons[] = {Arme.getRandomArme()};
			return weapons;
		}
		return null;	
	}
	//GETTERS & SETTERS 
	public ArrayList<Cible> getListeCible() {
		return listeCible;
	}
	public void setListeCible(ArrayList<Cible> listeCible) {
		this.listeCible = listeCible;
	}
	public long getStart_time() {
		return start_time;
	}
	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}
	//NON FONCTIONNEL 
	//(pb de synchronisation car le delay est plus long que dt ?)
	//PERMET D'APPELER <ncall> FOIS LA METHODE <addTarget> AVEC <delay> MS D'INTERVALLE
	/*private void delayAddTarget(int ncall, long delay){
		if(ncall>0){
			ScheduledExecutorService sched = Executors.newSingleThreadScheduledExecutor();
			sched.schedule(new Runnable(){
				@Override
				public void run(){
						addTarget();
						delayAddTarget(ncall-1, delay);
				}
			},delay,TimeUnit.MILLISECONDS);
		}
	}*/
}
