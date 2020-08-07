package menu;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * Crée une bordure à partir de 8 images :
 * 1 pour chaque coin, et 1 pour chaque côté
 * Les images pour les côtés sont répétées
 * sous forme de texture
 *	paramètres : 
 *	Les 8 images
 *	La largeur et la hauteur de ces 8 images
 */
public class ImageBordure {
		Image topCenterImage, topLeftImage, topRight;
		Image leftCenterImage, rightCenterImage;
		Image bottomCenterImage, bottomLeftImage, bottomRightImage;
	  
	  	int tlw;
	    int tlh;
	    int tcw;
	    int tch;
	    int trw;
	    int trh;

	    int lcw;
	    int lch;
	    int rcw;
	    int rch;

	    int blw;
	    int blh;
	    int bcw;
	    int bch;
	    int brw;
	    int brh;
	    // CONSTRUCTEUR
	  public ImageBordure(
		Image top_left, 
		Image top_center, 
		Image top_right, 
		Image left_center,
		Image right_center, 
		Image bottom_left, 
		Image bottom_center, 
		Image bottom_right
	){

	    this.topLeftImage = top_left;
	    this.topCenterImage = top_center;
	    this.topRight = top_right;
	    this.leftCenterImage = left_center;
	    this.rightCenterImage = right_center;
	    this.bottomLeftImage = bottom_left;
	    this.bottomCenterImage = bottom_center;
	    this.bottomRightImage = bottom_right;
	    
	    tlw = topLeftImage.getWidth(null);
	    tlh = topLeftImage.getHeight(null);
	    tcw = topCenterImage.getWidth(null);
	    tch = topCenterImage.getHeight(null);
	    trw = topRight.getWidth(null);
	    trh = topRight.getHeight(null);

	    lcw = leftCenterImage.getWidth(null);
	    lch = leftCenterImage.getHeight(null);
	    rcw = rightCenterImage.getWidth(null);
	    rch = rightCenterImage.getHeight(null);

	    blw = bottomLeftImage.getWidth(null);
	    blh = bottomLeftImage.getHeight(null);
	    bcw = bottomCenterImage.getWidth(null);
	    bch = bottomCenterImage.getHeight(null);
	    brw = bottomRightImage.getWidth(null);
	    brh = bottomRightImage.getHeight(null);
	    

	  }
	  // RETOURNE LA BORDURE A PEINDRE SUR LE COMPOSANT <c> EN FONCTION DE L'INSTANCE  D'<ImageBordure>
	  public BufferedImage getBorder(JComponent c){
		  
		final int width = (int) (c.getWidth()>0?c.getWidth():c.getPreferredSize().getWidth());
		final int height = (int) (c.getHeight()>0?c.getHeight():c.getPreferredSize().getHeight());
		  
		BufferedImage buff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)buff.getGraphics();
		  
		fillTexture(g2d, topCenterImage, tlw, 0, width - tlw - trw, tch);
		fillTexture(g2d, leftCenterImage, 0, tlh, lcw, height - tlh - blh);
		fillTexture(g2d, rightCenterImage, width - rcw, trh, rcw, height - trh - brh);
		fillTexture(g2d, bottomCenterImage, blw, height - bch, width - blw - brw, bch);
		
		g2d.drawImage(topLeftImage, 0, 0, c);
		g2d.drawImage(topRight, width-trw, 0, c);
		g2d.drawImage(bottomLeftImage, 0, height-blh, c);
		g2d.drawImage(bottomRightImage,width-brw, height-brh, c);
		  
		return buff;
			  
	  }
	  // REPETE ET PAINT L'IMAGE <img> SUR LE RECTANGLE <(x,y,w,h)> 
	  public static void fillTexture(Graphics2D g2, Image img, int x, int y, int w, int h) {
	    final BufferedImage buff = createBufferedImage(img);
		Rectangle anchor = new Rectangle(x, y, img.getWidth(null), img.getHeight(null));
		TexturePaint paint = new TexturePaint(buff, anchor);
		g2.setPaint(paint);
		g2.fillRect(x, y, w, h);
	  }
	  // CREE UNE BufferedImage DE LA TAILLE DE <img> ET PEINT <img> DESSUS
	  public static BufferedImage createBufferedImage(Image img) {
	    BufferedImage buff = new BufferedImage(img.getWidth(null), img.getHeight(null),BufferedImage.TYPE_INT_ARGB);
	    Graphics gfx = buff.createGraphics();
	    gfx.drawImage(img, 0, 0, null);
	    gfx.dispose();
	    return buff;
	  }
	  // RENVOIE LA DIMENSION MAXIMUM DES COINS DE LA BORDURE
	  public Dimension getSize(){
		final int w = Math.max(Math.max(tlw, trw), Math.max(blw, brw));
		final int h = Math.max(Math.max(tlh, trh), Math.max(blh, brh));
		return new Dimension(w,h);
	  }
}
