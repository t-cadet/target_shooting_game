package outils;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Classe de commodité pour ne pas avoir à se soucier
 * de la façon dont java gère les fichiers sons
 */
public abstract class Music {
	// RENVOIE LE CLIP D'UN ECHANTILLON DE SON EN FONCTION DU FILE <f>
	public static Clip getClip(File f){
		Clip son = null;
		try {
			final AudioInputStream audio = AudioSystem.getAudioInputStream(f);
			son = AudioSystem.getClip();
			son.open(audio);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e2) {
			e2.printStackTrace();
		}
		return son;
	}
}
