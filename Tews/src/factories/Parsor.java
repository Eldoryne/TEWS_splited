package factories;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import beans.DetectionResult;
import beans.Event;

/**
 * Classe permettant de lire le résultat de la détection par MABED
 * 
 * @author Myk
 *
 */
public class Parsor {

	private static final String ATT_YEAR_TO_DELETE = "/2015";
	private static final String FORMAT_DATE_OUTPUT = "dd/MM/yyyy";
	private static final String SUFFIX_OUTPUT = "output\\MABED.tex";
	private static final String REGEX_RAW = "(\\d+) & (\\d\\d/\\d\\d).*& (.*):  (.*)\\\\\\\\";
	private static final String REGEX_RELATED_WORD = "(.*) \\((\\d.\\d\\d)\\)";
	private static final Pattern PATTERN_RAW = Pattern.compile(REGEX_RAW);
	private static final Pattern PATTERN_RELATED_WORD = Pattern.compile(REGEX_RELATED_WORD);

	/**
	 * Méthode parsant la réponse MABED et donnant le résultat dans un objet de
	 * réponse
	 * 
	 * @param contextPath
	 * @return
	 * @throws Exception
	 */
	public static DetectionResult readResultDetection(String contextPath) throws Exception {

		DetectionResult detectionResult = new DetectionResult();
		String row = null;
		BufferedReader reader = null;

		// Utilisation des REGEX pour parser la réponse
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(contextPath + SUFFIX_OUTPUT)));
			while ((row = reader.readLine()) != null) {
				Event event = new Event();
				Matcher matcher = PATTERN_RAW.matcher(row);
				matcher.find();
				event.setId(Integer.parseInt(matcher.group(1)));
				event.setDate(DateTime.parse(matcher.group(2) + ATT_YEAR_TO_DELETE,
						DateTimeFormat.forPattern(FORMAT_DATE_OUTPUT)));
				event.setMainWord(new ArrayList<>(Arrays.asList(matcher.group(3).split(", "))));
				for (String relatedWord : matcher.group(4).split(", ")) {
					Matcher matcherWord = PATTERN_RELATED_WORD.matcher(relatedWord);
					matcherWord.find();
					event.getRelatedWord().put(matcherWord.group(1), Double.parseDouble(matcherWord.group(2)));
				}
				detectionResult.getEvents().put(event.getId(), event);
			}

			reader.close();

		} catch (Exception exception) {
			throw new Exception("Parse result MABED failed.");
		} finally {
			reader.close();
		}

		return detectionResult;

	}
}
