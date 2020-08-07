package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RadialGradientPaint;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;

import outils.ShinBackground;
import outils.ShinPanel;

/**
 * Construit un bouton pour le menu avec divers paramètres réglables
 * Le fond est soit une image/texture/couleur soit un dégradé de couleur
 * 
 * GENERAL
 * @param hover 		indique si le bouton est survolé ou non
 * @param sonHover 		son produit lors du survol
 * @param imabor		bordure faite avec des images
 * @param imLeft		image apparaissant à gauche lors du survol
 * @param imRight		image apparaissant à droite lors du survol
 * 
 * DEGRADE
 * @param surbrillance	couleur au centre du dégradé (de 0 à d)
 * @param back			couleur atteinte en d
 * @param bordure		couleur finale du dégradé
 * @param d				distance variable (lors du clic sur le bouton), 
 * 						indique le début de la 2ème couleur du dégradé
 *
 *IMAGE/TEXTURE/COULEUR
 *@param sback				fond du bouton
 *@param sbHover			fond au survol
 *@param mousePressedBorder	bordure lors du clic sur le bouton
 *@param defaut				bordure par défaut du bouton
 */
@SuppressWarnings("serial")
public class BoutonMenu extends JButton{
	
	protected boolean hover = false;
	
	protected Clip sonHover;
	protected ImageBordure imabor;
	protected ShinPanel imLeft;
	protected ShinPanel imRight;
	
	protected Color surbrillance;
	protected Color back;
	protected Color bordure;
	protected float d;
	
	protected ShinBackground sback;
	protected ShinBackground sbHover;
	protected Border mousePressedBorder;
	protected Border defaut;
	
	//CONSTRUCTEUR POUR AVOIR EN FOND UN DEGRADE
	public BoutonMenu(JLabel jltext, Color surbri, Color b, Color bord, ImageBordure ib){
		setRolloverEnabled(false);
		setBorderPainted(false);
		setLayout(new GridBagLayout());
		add(jltext);
		
		d=0.35f;
		imabor = ib;
		surbrillance=surbri;
		back=b;
		bordure=bord;
		
		addMouseListener(new Hover());
	}
	//CONSTRUCTEUR POUR AVOIR EN FOND UNE IMAGE/TEXTURE/COULEUR
	public BoutonMenu(JLabel jltext, ShinBackground sb, ShinBackground sbh, ImageBordure ib, Border mpb){
		setRolloverEnabled(false);
		setLayout(new GridBagLayout());
		add(jltext);
		
		mousePressedBorder = mpb;
		defaut = getBorder();
		
		imabor = ib;
		sback = sb;
		sbHover = sbh;
		
		addMouseListener(new Hover());
	}
	@Override
	//PAINT LE FOND ET LA BORDURE DU BOUTON
	public void paintComponent(Graphics g){
		final int w = getWidth();
		final int h = getHeight();
		if(w!=0 && h!=0){
			if(sback != null && sbHover != null){
				if(hover)
					sbHover.paintShinBackground(w, h, g);
				else
					sback.paintShinBackground(w, h, g);
			}	
			else{
				final Graphics2D g2d = (Graphics2D)g;
				final Point2D center = new Point2D.Float((int)w/2,(int)h/2);
			    
				if(hover){
				    final float[] dist = {0.0f, (float)d};
				    final Color[] colors = {surbrillance, back};
				    final RadialGradientPaint p = new RadialGradientPaint(center, w, dist, colors);
					g2d.setPaint(p);
					g2d.fillOval((int)-w/2,(int)-h/2,2*w,2*h);	
				}
				else{
				    final float[] dist = {0.0f, 1.0f};
				    final Color[] colors = {back, bordure};
				    final RadialGradientPaint p = new RadialGradientPaint(center, w, dist, colors);
					g2d.setPaint(p);
					g2d.fillOval((int)-w/2,(int)-h/2,2*w,2*h);
				}
			}
			if(imabor!=null && (w>=imabor.getSize().getWidth() && h>=imabor.getSize().getHeight()))
				g.drawImage(imabor.getBorder(this),0,0,null);
		}	
	}
	//RENVOIE UN TABLEAU DE BOUTONS AVEC EN FOND UN DEGRADE
	public static JComponent[] createButtons(JLabel[] jltext, ActionListener[] al, int nbBoutons, Dimension tailleBoutons,ImageBordure ib, Color c1, Color c2, Color c3, Clip son, ButtonHoverIcon icon){
		JComponent[] jTab = new JComponent[nbBoutons];
		for(int i = 0; i<nbBoutons; i++)
		{
			BoutonMenu jb = new BoutonMenu(jltext[i], c1, c2, c3, ib);
			
			final int w = (int)((int)tailleBoutons.getWidth()*1.2);
			final int h = (int)((int)tailleBoutons.getHeight()*4/3);
			jTab[i] = new ShinPanel();
			jTab[i].setLayout(new GridBagLayout());
			jTab[i].setPreferredSize(new Dimension(w,h));
			
			jb.addActionListener(al[i]);
			jb.setPreferredSize(tailleBoutons);
			jb.setSonHover(son);
			
			if(icon.getLeft()!=null && icon.getRight()!=null){
				jb.setImLeft(new ShinPanel(icon.getLeft()));
				jb.imLeft.setPreferredSize(new Dimension((int) ((w-tailleBoutons.getWidth())/2),h));
				jb.imLeft.setVisible(false);
				
				jb.setImRight(new ShinPanel(icon.getRight()));
				jb.imRight.setPreferredSize(new Dimension((int) ((w-tailleBoutons.getWidth())/2),h));
				jb.imRight.setVisible(false);
				
				jTab[i].add(jb.imLeft);
				jTab[i].add(jb);
				jTab[i].add(jb.imRight);
			}
			else
				jTab[i].add(jb);
		}	
		return jTab;
	}
	//RENVOIE UN TABLEAU DE BOUTONS AVEC EN FOND UNE IMAGE/TEXTURE/COULEUR
	public static JComponent[] createButtons(JLabel[] jltext, ActionListener[] al, int nbBoutons, Dimension tailleBoutons, ImageBordure ib, ShinBackground sb, ShinBackground sbh, Clip son, ButtonHoverIcon icon, Border mousePressedBorder){
		JComponent[] jTab = new JComponent[nbBoutons];
		for(int i = 0; i<nbBoutons; i++)
		{
			final int w = (int)((int)tailleBoutons.getWidth()*1.2);
			final int h = (int)((int)tailleBoutons.getHeight()*4/3);
			jTab[i]= new ShinPanel();
			jTab[i].setLayout(new GridBagLayout());
			jTab[i].setPreferredSize(new Dimension(w,h));
			
			BoutonMenu jb = new BoutonMenu(jltext[i], sb, sbh, ib, mousePressedBorder);
			jb.addActionListener(al[i]);
			jb.setPreferredSize(tailleBoutons);
			jb.setSonHover(son);
			
			if(icon.getLeft()!=null && icon.getRight()!=null){
				jb.setImLeft(new ShinPanel(icon.getLeft()));
				jb.imLeft.setPreferredSize(new Dimension((int) ((w-tailleBoutons.getWidth())/2),h));
				jb.imLeft.setVisible(false);
				
				jb.setImRight(new ShinPanel(icon.getRight()));
				jb.imRight.setPreferredSize(new Dimension((int) ((w-tailleBoutons.getWidth())/2),h));
				jb.imRight.setVisible(false);
				
				jTab[i].add(jb.imLeft);
				jTab[i].add(jb);
				jTab[i].add(jb.imRight);
			}
			else
				jTab[i].add(jb);
		}
		return jTab;
	}
	//CHANGEMENT DU FOND DU BOUTON AU SURVOL DE LA SOURIS ET AU CLIC
	class Hover implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {
			if(mousePressedBorder!=null)
				setBorder(mousePressedBorder);
			else
				d=0.7f;
		}	
		@Override
		public void mouseReleased(MouseEvent e) {
			if(mousePressedBorder!=null)
				setBorder(defaut);
			else
				d=0.35f;
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			hover = true;
			if(imLeft!=null && imRight!=null){
				imLeft.setVisible(true);
				imRight.setVisible(true);
			}
			if(sonHover!=null){
				sonHover.setMicrosecondPosition(0);
				sonHover.start();
			}	
			repaint();
		}
		@Override
		public void mouseExited(MouseEvent e) {
			hover = false;
			if(imLeft!=null && imRight!=null){
				imLeft.setVisible(false);
				imRight.setVisible(false);
			}
			if(sonHover!=null)
				sonHover.stop();
			repaint();
		}
		
		
	}
	//ACCESSEURS ET MUTATEURS
	public boolean getHover(){
		return hover;
	}
	public Clip getSonHover() {
		return sonHover;
	}
	public void setSonHover(Clip sonHover) {
		this.sonHover = sonHover;
	}
	public ImageBordure getImabor() {
		return imabor;
	}
	public void setImabor(ImageBordure imabor) {
		this.imabor = imabor;
	}
	public ShinPanel getImLeft() {
		return imLeft;
	}
	public void setImLeft(ShinPanel imLeft) {
		this.imLeft = imLeft;
	}
	public ShinPanel getImRight() {
		return imRight;
	}
	public void setImRight(ShinPanel imRight) {
		this.imRight = imRight;
	}
	public ShinBackground getSback() {
		return sback;
	}
	public void setSback(ShinBackground sback) {
		this.sback = sback;
	}
	public ShinBackground getSbHover() {
		return sbHover;
	}
	public void setSbHover(ShinBackground sbHover) {
		this.sbHover = sbHover;
	}
	public Border getMousePressedBorder() {
		return mousePressedBorder;
	}
	public void setMousePressedBorder(Border mousePressedBorder) {
		this.mousePressedBorder = mousePressedBorder;
	}
	public Color getSurbrillance() {
		return surbrillance;
	}
	public void setSurbrillance(Color surbrillance) {
		this.surbrillance = surbrillance;
	}
	public Color getBack() {
		return back;
	}
	public void setBack(Color back) {
		this.back = back;
	}
	public Color getBordure() {
		return bordure;
	}
	public void setBordure(Color bordure) {
		this.bordure = bordure;
	}
	
}
