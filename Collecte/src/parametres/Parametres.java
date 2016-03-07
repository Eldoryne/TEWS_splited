package parametres;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.regex.Pattern;

/*
 * Classe utilitaire permettant de stocker divers paramètres à mettre en commun entre les différents threads
 *  Certains de ces paramètres sont peut-être inutilisé
 */
public class Parametres {

	private static String consumerKey = "";
	private static String consumerSecret = "";
	private static String accessToken = "";
	private static String accessTokenSecret = "";

	private static String consumerKeyBis = "";
	private static String consumerSecretBis = "";
	private static String accessTokenBis = "";
	private static String accessTokenSecretBis = "";

	private static String hashtagRegex = "#.*?(\\s|,|'|$)";
	private static Pattern hashtagPattern = Pattern.compile(hashtagRegex);
	private static String delimiteurRegex = "(?m)\\s";
	private static SimpleDateFormat dateFichier = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat dateTweet = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	private static ArrayList<String> listeHashtags = new ArrayList<>();
	private static int nombreMaxHashtags = 20;
	private static ArrayList<String> listeLangues = new ArrayList<>();
	private static String[] tableauModeleVide = new String[0];
	private static DecimalFormat formatFichier = new DecimalFormat("00000000");

	public static String getConsumerKey() {
		return consumerKey;
	}

	public static void setConsumerKey(String consumerKey) {
		Parametres.consumerKey = consumerKey;
	}

	public static String getConsumerSecret() {
		return consumerSecret;
	}

	public static void setConsumerSecret(String consumerSecret) {
		Parametres.consumerSecret = consumerSecret;
	}

	public static String getAccessToken() {
		return accessToken;
	}

	public static void setAccessToken(String accessToken) {
		Parametres.accessToken = accessToken;
	}

	public static String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	public static void setAccessTokenSecret(String accessTokenSecret) {
		Parametres.accessTokenSecret = accessTokenSecret;
	}

	public static String getConsumerKeyBis() {
		return consumerKeyBis;
	}

	public static void setConsumerKeyBis(String consumerKeyBis) {
		Parametres.consumerKeyBis = consumerKeyBis;
	}

	public static String getConsumerSecretBis() {
		return consumerSecretBis;
	}

	public static void setConsumerSecretBis(String consumerSecretBis) {
		Parametres.consumerSecretBis = consumerSecretBis;
	}

	public static String getAccessTokenBis() {
		return accessTokenBis;
	}

	public static void setAccessTokenBis(String accessTokenBis) {
		Parametres.accessTokenBis = accessTokenBis;
	}

	public static String getAccessTokenSecretBis() {
		return accessTokenSecretBis;
	}

	public static void setAccessTokenSecretBis(String accessTokenSecretBis) {
		Parametres.accessTokenSecretBis = accessTokenSecretBis;
	}

	public static String getHashtagRegex() {
		return hashtagRegex;
	}

	public static void setHashtagRegex(String hashtagRegex) {
		Parametres.hashtagRegex = hashtagRegex;
	}

	public static Pattern getHashtagPattern() {
		return hashtagPattern;
	}

	public static void setHashtagPattern(Pattern hashtagPattern) {
		Parametres.hashtagPattern = hashtagPattern;
	}

	public static String getDelimiteurRegex() {
		return delimiteurRegex;
	}

	public static void setDelimiteurRegex(String delimiteurRegex) {
		Parametres.delimiteurRegex = delimiteurRegex;
	}

	public static SimpleDateFormat getDateFichier() {
		return dateFichier;
	}

	public static void setDateFichier(SimpleDateFormat dateFichier) {
		Parametres.dateFichier = dateFichier;
	}

	public static SimpleDateFormat getDateTweet() {
		return dateTweet;
	}

	public static void setDateTweet(SimpleDateFormat dateTweet) {
		Parametres.dateTweet = dateTweet;
	}

	public synchronized static ArrayList<String> getListeHashtags() {
		if (Parametres.listeHashtags.size() < 1) {
			Parametres.listeHashtags.add("#1mp0551bl3");
		}
		ArrayList<String> listeHashtags = new ArrayList<>();
		listeHashtags.addAll(Parametres.listeHashtags);
		return listeHashtags;
	}

	public synchronized static void setListeHashtags(ArrayList<String> listeHashtags) {
		Parametres.listeHashtags = listeHashtags;
	}

	public synchronized static void addToListeHashtags(String hashtag) {
		Parametres.listeHashtags.add(hashtag);
	}

	public static int getNombreMaxHashtags() {
		return nombreMaxHashtags;
	}

	public static void setNombreMaxHashtags(int nombreMaxHashtags) {
		Parametres.nombreMaxHashtags = nombreMaxHashtags;
	}

	public synchronized static ArrayList<String> getListeLangues() {
		if (Parametres.listeLangues.size() < 1) {
			Parametres.listeLangues.add("en");
		}
		ArrayList<String> listeLangues = new ArrayList<>();
		listeLangues.addAll(Parametres.listeLangues);
		return listeLangues;
	}

	public synchronized static void setListeLangues(ArrayList<String> listeLangues) {
		Parametres.listeLangues = listeLangues;
	}

	public static String[] getTableauModeleVide() {
		return tableauModeleVide;
	}

	public static void setTableauModeleVide(String[] tableauModeleVide) {
		Parametres.tableauModeleVide = tableauModeleVide;
	}

	public static DecimalFormat getFormatFichier() {
		return formatFichier;
	}

	public static void setFormatFichier(DecimalFormat formatFichier) {
		Parametres.formatFichier = formatFichier;
	}
}
