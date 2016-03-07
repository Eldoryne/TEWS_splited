package factories;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import beans.Corpus;
import beans.DetectionQuery;
import beans.DetectionResult;

/**
 * Classe métier gérant la détection des évènements par MABED et lançant la
 * prépration du corpus en amont et la lecture des résultats en aval
 * 
 * @author Myk
 *
 */
public class Detector {

	private static final String SUFFIX_CONTEXT_PATH = "WEB-INF\\";
	private static final String SUFFIX_MABED = SUFFIX_CONTEXT_PATH + "MABED\\";
	private static final String SUFFIX_INPUT_MABED = SUFFIX_MABED + "input\\";
	private static final String ATT_DETECTION_QUERY = "detectionQuery";
	private static final String[] MABED_COMMAND = { "java", "-jar", "MABED.jar", "-run" };

	/**
	 * Méthode lançant la détection des évènements à partir des infos spécifié
	 * dans le formulaire de détection 1/3
	 * 
	 * @param request
	 * @return
	 */
	public DetectionResult detectEvents(HttpServletRequest request) {

		DetectionResult detectionResult = new DetectionResult();
		// Récupération du contexte d'application du programme, méthode deprécié
		String contextPath = request.getRealPath("/");
		Corpus corpus = new Corpus();

		// Lecture, formatage et écriture du corpus pour MABED
		try {
			corpus = Preprocessing.prepareCorpus((DetectionQuery) request.getAttribute(ATT_DETECTION_QUERY),
					contextPath + SUFFIX_CONTEXT_PATH);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		// Lancement de MABED en ligne de commande
		File workingDirectory = new File(contextPath + SUFFIX_MABED);
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command(MABED_COMMAND);
		processBuilder.directory(workingDirectory);

		// Patiente le temps que MABED s'exécute
		try {
			Process process = processBuilder.start();
			process.waitFor();
		} catch (IOException | InterruptedException exception) {
			exception.printStackTrace();
		}

		// Lecture du résultat de la détection
		try {
			detectionResult = Parsor.readResultDetection(contextPath + SUFFIX_MABED);
			detectionResult.setCorpus(corpus);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		// Vide le dossier d'entrées de MABED pour la prochaine exécution
		File mabedInput = new File(contextPath + SUFFIX_INPUT_MABED);
		for (File subFile : mabedInput.listFiles()) {
			if (subFile.isDirectory()) {
				for (File subSubFile : subFile.listFiles()) {
					subSubFile.delete();
				}
			}
			subFile.delete();
		}

		return detectionResult;
	}
}
