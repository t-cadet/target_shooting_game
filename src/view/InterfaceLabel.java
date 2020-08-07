package view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
//JLabel qui sert dans <InterfaceMenu>
@SuppressWarnings("serial")
public class InterfaceLabel extends JLabel{
	public InterfaceLabel(String text, Color color){
		super(text);
		setFont(new Font("Arial",Font.BOLD,(int) (20*Fenetre.H/1080)));
		setForeground(color);
	}

}
