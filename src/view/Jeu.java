package view;

import outils.ShinPanel;
//Panel qui contient le fond d'�cran de la fen�tre de jeu, ainsi qu'un <ZoneTir> 
// et un <InterfaceMenu>
@SuppressWarnings("serial")
public class Jeu extends ShinPanel {
	public Jeu(){
		add(new ZoneTir());
	}

}
