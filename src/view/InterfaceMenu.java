package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import menu.BoutonMenu;
import model.Arme;
import outils.BorderPanel;
import outils.ShinBackground;
import outils.ShinPanel;
//Interface qui permet à l'utilisateur d'intéragir avec le jeu
//Pendant la partie et de voir ses statistiques
@SuppressWarnings("serial")
public class InterfaceMenu extends BorderPanel implements ActionListener, MouseListener {
	
    // Dimensions
    final static Dimension SCREEN_SIZE = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	final static int W = (int)SCREEN_SIZE.getWidth();
	final static int H = (int)SCREEN_SIZE.getHeight()/4;
	
	// Timer
	private final int TPS_TIMER_MS = 100;
	protected long starting_time;
    private Timer monTimer;
    
    private int temps;
    private int a = 0;
    
    protected JLabel Life, Score, Weapon, Bonus, Watch;
    
    protected JLabel Life1, WeaponUsed, Score1, NewWeapon, UpdateScore, UpdateHP;
    
    protected ShinPanel MyLife, MyScore, MyWeapon, MyBonusPanel, ScrollPaneContent, Clock;
    
    protected JScrollPane MyBonus ;
    protected JComboBox<Arme> WeaponBox;
    protected BoutonMenu Pause, Quit;
    
    
	public InterfaceMenu (){
		
		// Initialisation du panel
		super(new Color(60,60,60,170),Main.ib2);
		setSize(W, H);
		setLocation(0, H*3);
		
		final Color translucidBlack = new Color(0,0,0,200);
		
		//Création des conteneurs
		MyLife = new BorderPanel(translucidBlack,Main.ib) ;
			MyLife.setLayout(null);
		MyScore = new BorderPanel(translucidBlack,Main.ib) ;
			MyScore.setLayout(null);
        MyWeapon = new BorderPanel(translucidBlack,Main.ib) ;
        	MyWeapon.setLayout(null);
        MyBonusPanel = new BorderPanel(translucidBlack,Main.ib);
        MyBonusPanel.setLayout(null);
        	ScrollPaneContent = new ShinPanel();
        	ScrollPaneContent.setLayout(new BoxLayout(ScrollPaneContent,BoxLayout.Y_AXIS));
			MyBonus = new JScrollPane(ScrollPaneContent);
			//MyBonus.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			MyBonus.setBackground(translucidBlack);
			MyBonus.setOpaque(false);
			MyBonus.getViewport().setOpaque(false);
			MyBonus.setBorder(null);
			MyBonusPanel.add(MyBonus);
			
		Clock = new ShinPanel(new ShinBackground(Main.IMG.get("lilBlackSquare"),ShinBackground.REPEAT));//,Main.ib);
		Clock.setLayout(new BorderLayout());
		

		//Bouton pause
		JLabel pauseLabel = new JLabel("Pause");
		pauseLabel.setForeground(Color.DARK_GRAY);
		pauseLabel.setFont(new Font("Times New Roman",Font.BOLD,(int) (20*Fenetre.H/1080)));
		Pause = new BoutonMenu(pauseLabel, Color.YELLOW, Color.ORANGE, Color.RED, Main.ib);
		Pause.addMouseListener(this);
		//Bouton quitter
		JLabel quitLabel = new JLabel("Quitter");
		quitLabel.setForeground(Color.DARK_GRAY);
		quitLabel.setFont(new Font("Times New Roman",Font.BOLD,(int) (20*Fenetre.H/1080)));
		Quit = new BoutonMenu(quitLabel, Color.YELLOW, Color.ORANGE, Color.RED, Main.ib);
		Quit.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}@Override
			public void mousePressed(MouseEvent e) {}@Override
			public void mouseReleased(MouseEvent e) {}@Override
			public void mouseEntered(MouseEvent e) {}@Override
			public void mouseExited(MouseEvent e) {}
		});
		//Initialisation légendes
			//Vie du joueur	
			Life = new InterfaceLabel("Life :", Color.GREEN);
				Life.setHorizontalAlignment(SwingConstants.LEFT);
				MyLife.add(Life);
				
			//Liste des armes du joueur	
			Weapon = new InterfaceLabel("Weapons"	, Color.RED);	
				Weapon.setHorizontalAlignment(SwingConstants.CENTER);
				MyWeapon.add(Weapon);
				
			//Score du joueur	
			Score = new InterfaceLabel("Score :", Color.BLUE);
				Score.setHorizontalAlignment(SwingConstants.LEFT);
				MyScore.add(Score);
			Bonus = new InterfaceLabel("Buffs", Main.PURPLE);
				Bonus.setHorizontalAlignment(SwingConstants.CENTER);
				MyBonusPanel.add(Bonus);
				
		//Initialisation champs	
				//Vie du joueur
			Life1 = new InterfaceLabel("", Color.GREEN);
				Life1.setHorizontalAlignment(SwingConstants.RIGHT);
				MyLife.add(Life1);
				//Affichage de la dernière modif des pv du joueur
			UpdateHP = new InterfaceLabel("", Main.GOLD);	
				UpdateHP.setHorizontalAlignment(SwingConstants.CENTER);
				MyLife.add(UpdateHP);	
				
				//Caractéristique de l'arme utilisée par le joueur//Arme utilisée par le joueur	
			WeaponUsed 	= new JLabel();
				WeaponUsed.setForeground(Color.RED);
				WeaponUsed.setFont(new Font("Arial",Font.BOLD,(int) (18*Fenetre.H/1080)));
				WeaponUsed.setHorizontalAlignment(SwingConstants.CENTER);
				MyWeapon.add(WeaponUsed);
				//Affichage de la dernière arme obtenue par le joueur
			NewWeapon = new InterfaceLabel("", Main.GOLD);	
				NewWeapon.setHorizontalAlignment(SwingConstants.CENTER);
				MyWeapon.add(NewWeapon);	
				//Score du joueur
			Score1 = new InterfaceLabel("", Color.BLUE);
				Score1.setHorizontalAlignment(SwingConstants.RIGHT);
				MyScore.add(Score1);
				//affichage de la dernière modif du score du joueur
			UpdateScore = new InterfaceLabel("", Main.GOLD);	
				UpdateScore.setHorizontalAlignment(SwingConstants.CENTER);
				MyScore.add(UpdateScore);

			WeaponBox = new JComboBox<Arme>();
				WeaponBox.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						if(Fenetre.player!=null)
							Fenetre.player.equip((Arme) WeaponBox.getSelectedItem());
					}
				});
				MyWeapon.add(WeaponBox);
			Watch = new JLabel();
			Watch.setForeground(Main.MATRIX_GREEN);
			Watch.setFont(new Font("Times New Roman", Font.BOLD, (int) (30*Fenetre.H/1080)));
			Watch.setHorizontalAlignment(SwingConstants.CENTER);
			Clock.add(Watch,BorderLayout.CENTER);
				
		//Ajout des conteneurs au panneau général
			this.setLayout(null) ;
			this.add(MyLife) ;
			this.add(MyScore) ;
	        this.add(MyWeapon);
	        this.add(MyBonusPanel);
	        this.add(Clock);
	        this.add(Pause);
	        this.add(Quit);
				
	    //PLACEMENT DES CONTENEURS
	        final double xPan, yPan, wPan, hPan;
	        xPan = 3; yPan = 10; wPan= 35; hPan = 35;
	            
			setPCBounds(xPan              , yPan           , wPan         , hPan      , MyLife);
			setPCBounds(xPan              , 100-(hPan+yPan), wPan         , hPan      , MyScore);
			setPCBounds(100-(wPan+xPan)   , yPan           , (wPan-xPan)/2, 100-2*yPan, MyWeapon);
			setPCBounds(100-(xPan+wPan)/2 , yPan           , (wPan-xPan)/2, 100-2*yPan, MyBonusPanel);
			
			setPCBounds(0,25,100,75,MyBonus);
			//Chrono
			setPCBounds(2*xPan+wPan,15,100-2*(2*xPan+wPan),20, Clock);
			//Bouton Pause
			setPCBounds(2*xPan+wPan,40,100-2*(2*xPan+wPan),20, Pause);
			//Bouton Quit
			setPCBounds(2*xPan+wPan,65,100-2*(2*xPan+wPan),20, Quit);
			
		//PLACEMENT DES LEGENDES ET CHAMPS
			final double xLab1, yLab1, wLab1, hLab1;
			xLab1 = 10; yLab1 = 40; wLab1 = 30; hLab1 = 20;
			
			//Life
			setPCBounds(xLab1            , yLab1, wLab1              , hLab1, Life);
			setPCBounds(100-(wLab1+xLab1), yLab1, wLab1              , hLab1, Life1);
			setPCBounds(xLab1+wLab1      , yLab1, 100-2*(wLab1+xLab1), hLab1, UpdateHP);
			
			//Score
			setPCBounds(xLab1            , yLab1, wLab1, hLab1, Score);
			setPCBounds(100-(wLab1+xLab1), yLab1, wLab1, hLab1, Score1);
			setPCBounds(xLab1+wLab1      , yLab1, 100-2*(wLab1+xLab1), hLab1, UpdateScore);
			
			//Weapons
			setPCBounds(5, 15 ,90, 10, Weapon);
			setPCBounds(5, 35, 90, 10, WeaponBox);
			setPCBounds(5, 55, 90, 10, NewWeapon);
			setPCBounds(5, 75, 90,10, WeaponUsed);
			
			//Bonus
			setPCBounds(5, 15, 90, 10, Bonus);
		
		//initialisation of the timer
		monTimer = new Timer(TPS_TIMER_MS,this);
        starting_time = System.currentTimeMillis();
		monTimer.start();
	}
	public Timer getMonTimer() {
		return monTimer;
	}
	public void setMonTimer(Timer monTimer) {
		this.monTimer = monTimer;
	}
	//METHODES
	//setBounds avec des coordonnées en % de celle du parent (entre 0 et 100, NB : le composant doit avoir un parent)
	public static void setPCBounds(double x, double y, double w, double h, JComponent component){
		setPCLocation(x, y, component);
		setPCSize(w, h, component);
	}
	//setLocation avec des coordonnées en % de celle du parent (entre 0 et 100, NB : le composant doit avoir un parent)
	public static void setPCLocation(double x, double y, JComponent component){
		final int W = component.getParent().getWidth();
		final int H = component.getParent().getHeight();
		
		final int new_x = (int) (x*W/100);
		final int new_y = (int) (y*H/100);
		
		component.setLocation(new_x, new_y);
	}
	//setSize avec des coordonnées en % de celle du parent (entre 0 et 100, NB : le composant doit avoir un parent)
	public static void setPCSize(double w, double h, JComponent component){
		final int W = component.getParent().getWidth();
		final int H = component.getParent().getHeight();
		
		final int new_w = (int) (w*W/100);
		final int new_h = (int) (h*H/100);
		
		component.setSize(new_w, new_h);
	}
	public static void setPCPreferredSize(double w, double h, JComponent component){
		final int W = component.getParent().getWidth();
		final int H = component.getParent().getHeight();
		
		final int new_w = (int) (w*W/100);
		final int new_h = (int) (h*H/100);
		
		component.setPreferredSize(new Dimension(new_w, new_h));
	}
	public static void setPCMinimumSize(double w, double h, JComponent component){
		final int W = component.getParent().getWidth();
		final int H = component.getParent().getHeight();
		
		final int new_w = (int) (w*W/100);
		final int new_h = (int) (h*H/100);
		
		component.setMinimumSize(new Dimension(new_w, new_h));
	}	
	//ACTIONPERFORMED
	 public void actionPerformed(ActionEvent e){
		temps += TPS_TIMER_MS;
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Watch.setText(sdf.format(temps));
    }
    
	
	public void mouseReleased(MouseEvent e){}
	
	public void mouseEntered(MouseEvent e){}
	
	public void mousePressed(MouseEvent e){}
	
	public void mouseExited(MouseEvent e){}
	
	//Met le timer en pause lorsqu'on clique sur le bouton
	public void mouseClicked(MouseEvent e){
		if(a%2 == 0){
			monTimer.stop();
		}else{
			monTimer.start();
		}
		a=a+1;
	}	/*//TRISTAN A :L AIDE 
	String[] WeaponList = new String[5];
	WeaponList[0] = "Weapons availables";
	WeaponList[1] = "AK47 SISSI";
	WeaponList[2] = "LANCE PATATE";
	WeaponList[3] = "SABRE JEDI";
	WeaponList[4] = "FRANCK LEBOURGEOIS";*/
	//GETTERS & SETTERS
	public JLabel getLife1() {
		return Life1;
	}

	public void setLife1(JLabel life1) {
		Life1 = life1;
	}

	public JScrollPane getMyBonus() {
		return MyBonus;
	}

	public void setMyBonus(JScrollPane myBonus) {
		MyBonus = myBonus;
	}
	
	public JLabel getWeaponUsed() {
		return WeaponUsed;
	}
	public void setWeaponUsed(JLabel weaponUsed) {
		WeaponUsed = weaponUsed;
	}
	public JComboBox<Arme> getWeaponBox() {
		return WeaponBox;
	}
	public void setWeaponBox(JComboBox<Arme> weaponBox) {
		WeaponBox = weaponBox;
	}
	public JLabel getScore1() {
		return Score1;
	}

	public void setScore1(JLabel score1) {
		Score1 = score1;
	}
	public ShinPanel getScrollPaneContent() {
		return ScrollPaneContent;
	}
	public void setScrollPaneContent(ShinPanel scrollPaneContent) {
		ScrollPaneContent = scrollPaneContent;
	}
	public JLabel getNewWeapon() {
		return NewWeapon;
	}
	public void setNewWeapon(JLabel newWeapon) {
		NewWeapon = newWeapon;
	}
	public JLabel getUpdateScore() {
		return UpdateScore;
	}
	public void setUpdateScore(JLabel updateScore) {
		UpdateScore = updateScore;
	}
	public JLabel getUpdateHP() {
		return UpdateHP;
	}
	public void setUpdateHP(JLabel updateHP) {
		UpdateHP = updateHP;
	}
}
