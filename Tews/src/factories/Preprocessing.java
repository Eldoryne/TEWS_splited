package factories;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import beans.Corpus;
import beans.DetectionQuery;
import beans.Tweet;

/**
 * Classe avec une unique méthode statique appelant les autres permettant de
 * lire le corpus spécifié avec le formulaire de détection 1/3, de le mettre au
 * bon format pour MABED et de le placer dans le bon dossier avant de changer
 * les paramètres d'initialisation de MABED
 * 
 * @author Myk
 *
 */
public class Preprocessing {

	private static final String PREFIX_CORPUS_CROWD = "crowd-";
	private static final String PREFIX_CORPUS_MEDIA = "media-";
	private static final String SUFFIX_CORPUS_RAW = ".txt";
	private static final String PATTERN_DATE = "yyyy-MM-dd HH:mm:ss.S";
	private static final DecimalFormat FORMAT_FILE = new DecimalFormat("00000000");
	private static final String SUFFIX_CONTEXT_PATH_IMPORT = "corpus\\";
	private static final String SUFFIX_CONTEXT_PATH_MABED = "MABED\\";
	private static final String SUFFIX_CONTEXT_PATH_EXPORT = SUFFIX_CONTEXT_PATH_MABED + "input\\";
	private static final String SUFFIX_PARAMETERS = SUFFIX_CONTEXT_PATH_MABED + "parameters.txt";
	private static final String SUFFIX_INPUT_MABED_TIME = ".time";
	private static final String SUFFIX_INPUT_MABED_TEXT = ".text";

	/**
	 * Méthode de récupération d'un corpus comprenant les tweets de la foule et
	 * les tweets des médias à partir des paramètres du formulaire de détection
	 * 1/3 et du dossier courant d'exécution
	 * 
	 * @param detectionQuery
	 * @param contextPath
	 * @return un corpus
	 * @throws Exception
	 */
	public static Corpus prepareCorpus(DetectionQuery detectionQuery, String contextPath) throws Exception {

		// Lecture du corpus
		Corpus corpus = Preprocessing.importCorpus(detectionQuery, contextPath + SUFFIX_CONTEXT_PATH_IMPORT);

		if (corpus != null && corpus.getTweets() != null && corpus.getTweets().get(0) != null
				&& corpus.getTweets().get(0).getDate() != null) {
			// Le formater et l'écrire dans le bon dossier pour MABED
			Preprocessing.exportCorpusForMabed(corpus, contextPath + SUFFIX_CONTEXT_PATH_EXPORT);
		} else {
			throw new Exception("Corpus variable error.");
		}
		// Changer les paramètres d'initialisation de MABED
		Preprocessing.modifyParameters(detectionQuery, contextPath);

		return corpus;
	}

	/**
	 * Lecture à proprement dite des données brutes et transformation en entité
	 * corpus
	 * 
	 * @param detectionQuery
	 * @param contextPath
	 * @return un corpus
	 * @throws Exception
	 */
	private static Corpus importCorpus(DetectionQuery detectionQuery, String contextPath) throws Exception {

		Corpus corpus = new Corpus();
		ArrayList<Tweet> tweets = new ArrayList<>();
		ArrayList<Tweet> tweetsMedia = new ArrayList<>();
		String row = null;
		BufferedReader reader = null;

		// Récupération des tweets de la foule
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(contextPath + PREFIX_CORPUS_CROWD
					+ detectionQuery.getDateCorpus().toString("yyyy-MM-dd") + SUFFIX_CORPUS_RAW)));
			while ((row = reader.readLine()) != null) {
				Tweet tweet = new Tweet();
				tweet.setDate(Preprocessing.parseDateTime(row));
				row = reader.readLine();
				tweet.setUser(row);
				row = reader.readLine();
				tweet.setText(row);
				tweets.add(tweet);
			}

			reader.close();

		} catch (Exception exception) {
			throw new Exception("Import corpus failed.");
		} finally {
			reader.close();
		}

		// Récupération des tweets des médias
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(contextPath + PREFIX_CORPUS_MEDIA
					+ detectionQuery.getDateCorpus().toString("yyyy-MM-dd") + SUFFIX_CORPUS_RAW)));
			while ((row = reader.readLine()) != null) {
				Tweet tweetMedia = new Tweet();
				tweetMedia.setDate(Preprocessing.parseDateTime(row));
				row = reader.readLine();
				tweetMedia.setUser(row);
				row = reader.readLine();
				tweetMedia.setText(row);
				tweetsMedia.add(tweetMedia);
			}

			reader.close();

		} catch (Exception exception) {
			throw new Exception("Import corpus media failed.");
		} finally {
			reader.close();
		}

		// Mise à jour des données du corpus avec les collectes
		corpus.setTweets(tweets);
		corpus.setTweetsMedia(tweetsMedia);
		corpus.setDetectionQuery(detectionQuery);

		return corpus;
	}

	// Permet de placer le corpus dans le format et le dossier adéquat pour
	// qu'il puisse être lu par MABED
	private static void exportCorpusForMabed(Corpus corpus, String contextPath) throws Exception {

		Iterator<Tweet> tweetsCorpus = corpus.getTweets().iterator();
		int timeSlice = corpus.getDetectionQuery().getTimeSlice();
		ArrayList<Tweet> tweetsTimeSlice = new ArrayList<>();
		int timeSliceCount = 0;
		Tweet tweetCorpus = tweetsCorpus.next();
		DateTime stepDate = tweetCorpus.getDate().plusMinutes(timeSlice);

		// La boucle permet d'écrire le corpus sous forme de tranche temporelle
		// pour que MABED puisse l'utiliser avec le temps spécifié dans le
		// formulaire
		while (tweetsCorpus.hasNext()) {

			if (tweetCorpus.getDate().isAfter(stepDate)) {
				Preprocessing.writeTweets(tweetsTimeSlice, timeSliceCount, contextPath);
				tweetsTimeSlice = new ArrayList<>();
				stepDate = stepDate.plusMinutes(timeSlice);
				timeSliceCount++;
			}

			tweetsTimeSlice.add(tweetCorpus);
			tweetCorpus = tweetsCorpus.next();
		}

		// Ecriture des fichiers pour MABED
		Preprocessing.writeTweets(tweetsTimeSlice, timeSliceCount, contextPath);
	}

	// Lecture des dates de tweets
	private static DateTime parseDateTime(String date) throws Exception {
		DateTime dateTime = null;
		try {
			dateTime = DateTime.parse(date, DateTimeFormat.forPattern(PATTERN_DATE));
		} catch (Exception exception) {
			throw new Exception("Invalid date input corpus : " + date);
		}

		return dateTime;
	}

	// Ecriture des tweets au format MABED
	private static void writeTweets(ArrayList<Tweet> tweetsTimeSlice, int timeSliceCount, String contextPath)
			throws Exception {
		PrintWriter writerText = null;
		try {
			writerText = new PrintWriter(new BufferedWriter(
					new FileWriter(contextPath + FORMAT_FILE.format(timeSliceCount) + SUFFIX_INPUT_MABED_TEXT)));
			for (Tweet tweetTimeSlice : tweetsTimeSlice) {
				writerText.println(tweetTimeSlice.getText());
			}
		} catch (Exception exception) {
			throw new Exception("Writing text error on timeSlice n�" + timeSliceCount);
		} finally {
			writerText.close();
		}

		try {
			writerText = new PrintWriter(new BufferedWriter(
					new FileWriter(contextPath + FORMAT_FILE.format(timeSliceCount) + SUFFIX_INPUT_MABED_TIME)));
			for (Tweet tweetTimeSlice : tweetsTimeSlice) {
				writerText.println(tweetTimeSlice.getDate().toString(PATTERN_DATE));
			}
		} catch (Exception exception) {
			throw new Exception("Writing time error on timeSlice n�" + timeSliceCount);
		} finally {
			writerText.close();
		}
	}

	// Permet de modifier les paramètres de lancement de MABED pour qu'ils
	// correspondant avec la demande du formulaire de détection 1/3
	private static void modifyParameters(DetectionQuery detectionQuery, String contextPath) throws Exception {
		String parameters = "# If you are running MABED for the first time, or if the content of the input directory has been modified, this parameter should be set to 'true', otherwise 'false'\n"
				+ "prepareCorpus = true\n" + "# The length of each time-slice, expressed in minutes, e.g. 30\n"
				+ "timeSliceLength = " + detectionQuery.getTimeSlice() + "\n" + "# Number of threads to use\n"
				+ "numberOfThreads = 2\n" + "# MABED parameters\n" + "k = " + detectionQuery.getNumberEvent() + "\n"
				+ "p = " + detectionQuery.getNumberWord() + "\n" + "theta = " + detectionQuery.getWeightWord() + "\n"
				+ "sigma = " + detectionQuery.getMergerThreshold() + "\n"
				+ "# List of stopwords that are removed from the vocabularies\n" + "stopwords = stopwords.txt\n"
				+ "# Adjust support to speed up the first phase\n" + "minSupport = 0.001\n" + "maxSupport = 0.01\n";
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(contextPath + SUFFIX_PARAMETERS));
			writer.print(parameters);
		} catch (Exception exception) {
			throw new Exception("Writing parameters error");
		} finally {
			writer.close();
		}
	}
}
