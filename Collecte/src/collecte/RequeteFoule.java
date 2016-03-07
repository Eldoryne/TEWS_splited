package collecte;

import parametres.Parametres;
import twitter4j.FilterQuery;
import twitter4j.Status;

public class RequeteFoule extends Requete {

	/*
	 * REquête avec critères
	 */
	public RequeteFoule() {
		this.setPrefixeFichier("foule");
	}

	// Récupéère la liste de hashtags présents dans la classe statique
	// "Parametres"
	@Override
	public void run() {
		FilterQuery filter = new FilterQuery();
		filter.language(Parametres.getListeLangues().toArray(Parametres.getTableauModeleVide()));
		filter.track(Parametres.getListeHashtags().toArray(Parametres.getTableauModeleVide()));
		this.getTwitterStream().filter(filter);
	}

	@Override
	public void onStatus(Status status) {
		super.onStatus(status);
	}
}
