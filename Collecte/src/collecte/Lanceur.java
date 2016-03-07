package collecte;

import java.util.Timer;

/**
 * Point de départ de la méthode de collecte, se lance directement en faisant
 * "Run", s'arrête en coupant le processus
 * 
 * @author Myk
 *
 */
public class Lanceur {

	public static void main(String[] args) {

		// Ouvre un thread pour recueillir des tweets échantillonées (sans
		// critères)
		/*
		 * Timer timer = new Timer(); Requete requete = new Requete();
		 * timer.schedule(requete, 0);
		 */

		// Ouvre un thread pour recueillir des tweets des médias
		Timer timerMedia = new Timer();
		RequeteHashtags requeteHashtags = new RequeteHashtags();
		timerMedia.schedule(requeteHashtags, 0);

		// Ouvre un thread pour recueillir des tweets de la foule contenant des
		// hashtags des médias
		Timer timerFoule = new Timer();
		RequeteFoule requeteFoule = new RequeteFoule();
		// Le second chiffre donne le temps entre deux mise à jour de la liste
		// des hashtags
		timerFoule.schedule(requeteFoule, 0, 600000);
	}

}
