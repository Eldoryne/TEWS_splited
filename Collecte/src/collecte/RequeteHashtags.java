package collecte;

import parametres.Parametres;
import twitter4j.HashtagEntity;
import twitter4j.Status;

public class RequeteHashtags extends Requete {

	/*
	 * Requete sur les médias
	 */
	public RequeteHashtags() {
		super();
		this.setPrefixeFichier("media");
	}

	@Override
	public void run() {
		this.getTwitterStream().user();
	}

	//Ajoute les hashtags à la liste de la classe statique "parametres" au passage
	@Override
	public void onStatus(Status status) {
		super.onStatus(status);
		System.out.println(status.getUser().getScreenName());
		System.out.println(status.getText());
		for (HashtagEntity hashtag : status.getHashtagEntities()) {
			Parametres.addToListeHashtags("#" + hashtag.getText());
			System.out.println(hashtag.getText());
		}
	}
}
