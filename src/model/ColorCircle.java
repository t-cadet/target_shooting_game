package model;



import java.awt.Color;
import java.awt.image.BufferedImage;

import outils.ShinPanel;

//Cette classe est en concurrence avec la classe Cercle,
//Dont on  se sert pas pour l'instant car on a préféré que 
//Le tir colore la cible
@SuppressWarnings("serial")
public class ColorCircle extends AOE {
	//CONSTRUCTEURS
	public ColorCircle(){}
	public ColorCircle(int x_centre, int y_centre, int R) {
		super(x_centre, y_centre, R);
	}
	
	//ON REGARDE SI LE <Cercle> CONTIENT UN PIXEL COLORE DU <ShinPanel>
	public boolean collides(ShinPanel sp, Color impact) {
		boolean collides = false;
		final BufferedImage target = sp.getSback().getActualBackground();
		final int w = target.getWidth();
		final int h = target.getHeight();
		//Plus besoin car on parcourt toute l'image pour la colorier
		//SI LE CERCLE CONTIENT LES 4 COINS DE L'IMAGE ON RENVOIE VRAI
		/*if(containsPoint(0,0) && containsPoint(w-1,0) && containsPoint(0,h-1) && containsPoint(w-1,h-1))
			target.setRGB(x_centre    , y_centre ,impact.getRGB()); */
		//ON CREE UNE BOITE RECTANGULAIRE QUI ENCADRE LE CERCLE MAIS QUI S'ARRETE AUX BORDS DE L'IMAGE
		final int x_left = Math.max(x_centre-R, 0);
		final int x_right = Math.min(x_centre+R, w-1);
		final int y_top = Math.max(y_centre-R,0);
		final int y_bot = Math.min(y_centre+R,h-1);
		//ON COMPARE L'AIRE DU CERCLE ET L'AIRE DE LA BOITE
		final int Aboite = (x_right-x_left)*(y_bot-y_top);
		final int Acercle = (int) (Math.PI*R*R+3.1658084033*R+3.2175801865);
		//NB : on ne peut pas accéder à l'aire exacte du cercle discret
		//sans le parcourir, on ajoute un facteur de correction trouvé 
		//par regression linéaire à la formule de l'aire  
		//erreur moyenne de 4.5 px pour r entre 0 et 50, 
		//pour r de 0 à 1000 : 21px soit 0,003% d'erreur
		//SI LA BOITE EST PLUS PETITE QUE LE CERCLE
		//ON PARCOURT LA BOITE DE GAUCHE A DROITE ET DE HAUT EN BAS
		if(Aboite<=Acercle){
			for(int j = y_top; j<= y_bot; j++)
				for(int i = x_left; i<= x_right; i++)
					if(target.getRGB(i,j)>>24!=0 && containsPoint(i,j)){//On teste si le pixel n'est pas transparent et s'il est dans le cercle
						target.setRGB(i, j, impact.getRGB());
						collides = true;
					}	
		}
		//SINON ON PARCOURT LE CERCLE PAR ANNEAU SUCCESSIFS A PARTIR DU CENTRE
		//SELON L'ALGORITHME D'ANDRES (https://fr.wikipedia.org/wiki/Algorithme_de_trac%C3%A9_de_cercle_d%27Andres)
		//ON REMARQUE QUE L'ALGORITHME D'ANDRES PASSE 2 FOIS PAR LES AXES SEPARANT LES OCTANTES ET 8 FOIS PAR LE CENTRE
		//ON LE MODIFIE DONC UN PEU POUR QU'IL PASSE SEULEMENT UNE FOIS PAR CHAQUE PIXEL (on examine 8R +7 pixels en moins)
		else{
			//On traite le centre séparément pour ne pas tester 8 fois le même pixel
			if(inW(x_centre,w) && inH(y_centre,h) && (target.getRGB(x_centre, y_centre)>>24) != 0 ){
	        	target.setRGB(x_centre,y_centre,impact.getRGB());
	        	collides = true;
			}	
			for(int r = 1; r<=R ; r++){
			    int x = 0;
			    int y = r;
			    int d = r - 1;
			    //On s'occupe des axes verticaux et horizontaux
			    if(inW(x_centre    ,w) && inH(y_centre + y,h) && (target.getRGB(x_centre    , y_centre + y)>>24) != 0 ){
		        	target.setRGB(x_centre    , y_centre + y,impact.getRGB()); 
		        	collides = true;
			    }	
			    if(inW(x_centre - y,w) && inH(y_centre    ,h) && (target.getRGB(x_centre - y, y_centre    )>>24) != 0 ){
			        target.setRGB(x_centre - y, y_centre    ,impact.getRGB());
			    	collides = true;
				}
			    if(inW(x_centre + y,w) && inH(y_centre    ,h) && (target.getRGB(x_centre + y, y_centre    )>>24) != 0 ){
		        	target.setRGB(x_centre + y, y_centre    ,impact.getRGB());
			    	collides = true;
				}
			    if(inW(x_centre    ,w) && inH(y_centre - y,h) && (target.getRGB(x_centre    , y_centre - y)>>24) != 0 ){
		        	target.setRGB(x_centre    , y_centre - y,impact.getRGB());
		        	collides = true;
			    }	
			    //On incrémente selon Andres avant le while
		        //=> le while ne parcourt plus les axes verticaux et horizontaux
		        if (d >= 2*x)
		        {
		            d -= 2*x + 1;
		            x ++;
		        }
		        else if (d < 2 * (r-y))
		        {
		            d += 2*y - 1;
		            y --;
		        }
		        else
		        {
		            d += 2*(y - x - 1);
		            y --;
		            x ++;
		        }
		        // On parcourt tant que y>x et non plus tant que y>=x
		        //=> Le while ne parcourt plus les diagonales
			    while(y > x){//On regarde si les bits 25 à 32 (transparence) sont non nuls sur chaque octant du cercle
			        if(inW(x_centre + x,w) && inH(y_centre + y,h) && (target.getRGB(x_centre + x, y_centre + y)>>24) != 0 ){
			        	target.setRGB(x_centre + x, y_centre + y,impact.getRGB());
			        	collides = true;
			    	}
			        if( inW(x_centre + y,w) && inH(y_centre + x,h) && (target.getRGB(x_centre + y, y_centre + x)>>24) != 0 ){
			        	target.setRGB(x_centre + y, y_centre + x,impact.getRGB());
			        	collides = true;
			        }	
			        if( inW(x_centre - x,w) && inH(y_centre + y,h) && (target.getRGB(x_centre - x, y_centre + y)>>24) != 0 ){
			        	target.setRGB(x_centre - x, y_centre + y,impact.getRGB());
			        	collides = true;
			        }	
			        if(inW(x_centre - y,w) && inH(y_centre + x,h) && (target.getRGB(x_centre - y, y_centre + x)>>24) != 0 ){
			        	target.setRGB(x_centre - y, y_centre + x,impact.getRGB());
			        	collides = true;
			        }	
			        if(  inW(x_centre + x,w) && inH(y_centre - y,h) && (target.getRGB(x_centre + x, y_centre - y)>>24) != 0 ){
			        	target.setRGB(x_centre + x, y_centre - y,impact.getRGB());
			        	collides = true;
			        }	
			        if(  inW(x_centre + y,w) && inH(y_centre - x,h) && (target.getRGB(x_centre + y, y_centre - x)>>24) != 0 ){
			        	target.setRGB(x_centre + y, y_centre - x,impact.getRGB());
			        	collides = true;
			        }	
			        if( inW(x_centre - x,w) && inH(y_centre - y,h) && (target.getRGB(x_centre - x, y_centre - y)>>24) != 0 ){
			        	target.setRGB(x_centre - x, y_centre - y,impact.getRGB());
			        	collides = true;
			        }	
			        if(  inW(x_centre - y,w) && inH(y_centre - x,h) && (target.getRGB(x_centre - y, y_centre - x)>>24) != 0 ){
			        	target.setRGB(x_centre - y, y_centre - x,impact.getRGB());
			        	collides = true;
			        }	
			        //On incrémente selon Andres
			        if (d >= 2*x){
			            d -= 2*x + 1;
			            x ++;
			        }
			        else if (d < 2 * (r-y)){
			            d += 2*y - 1;
			            y --;
			        }
			        else{
			            d += 2*(y - x - 1);
			            y --;
			            x ++;
			        }
			    }
			    //On s'occupe de parcourir les diagonales
			    if(r<=R*Math.sqrt(2)/2){	
			    	if(  inW(x_centre + r,w) && inH(y_centre + r,h) && (target.getRGB(x_centre + r, y_centre + r)>>24) != 0 ){
			        	target.setRGB(x_centre + r, y_centre + r,impact.getRGB());
			        	collides = true;
			    	}	
			    	if(  inW(x_centre - r,w) && inH(y_centre + r,h) && (target.getRGB(x_centre - r, y_centre + r)>>24) != 0 ){
			        	target.setRGB(x_centre - r, y_centre + r,impact.getRGB());
			        	collides = true;
			    	}	
			    	if(  inW(x_centre + r,w) && inH(y_centre - r,h) && (target.getRGB(x_centre + r, y_centre - r)>>24) != 0 ){
			        	target.setRGB(x_centre + r, y_centre - r,impact.getRGB());
			        	collides = true;
			    	}	
			    	if(  inW(x_centre - r,w) && inH(y_centre - r,h) && (target.getRGB(x_centre - r, y_centre - r)>>24) != 0 ){
			        	target.setRGB(x_centre - r, y_centre - r,impact.getRGB());
			        	collides = true;
			    	}
			    }
			}
		}
		sp.getSback().setImageBackground(target);
		return collides;
	}
	public boolean inW(int x, int w){
		return 0<=x && x < w;
	}
	public boolean inH(int y, int h){
		return 0<=y && y < h;
	}
	//renvoie vrai si (xp,yp) est dans le cercle de rayon R
	public boolean containsPoint(int xp, int yp){
		final int x = xp-x_centre;
		final int y = yp-y_centre;
		final int d = (int) Math.sqrt(x*x+y*y);
		return (d<=R);
	}

	@Override
	public boolean collides(ShinPanel im) {
		return collides(im, new Color(0,0,0,0));
	}
	

}

