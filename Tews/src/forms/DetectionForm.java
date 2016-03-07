package forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;

import beans.DetectionQuery;

/**
 * Objet de contrôle du formulaire de détection 1/3
 * 
 * @author Myk
 *
 */
public class DetectionForm {

	public static final String FIELD_YEAR = "year";
	public static final String FIELD_MONTH = "month";
	public static final String FIELD_DAY = "day";
	public static final String FIELD_DATE_CORPUS = "dateCorpus";
	public static final String FIELD_TIME_SLICE = "timeSlice";
	public static final String FIELD_NUMBER_EVENT = "numberEvent";
	public static final String FIELD_NUMBER_WORD = "numberWord";
	public static final String FIELD_WEIGHT_WORD = "weightWord";
	public static final String FIELD_MERGER_THRESHOLD = "mergerThreshold";

	private String result;
	private Map<String, String> errors = new HashMap<String, String>();

	public String getResult() {
		return this.result;
	}

	public Map<String, String> getErrors() {
		return this.errors;
	}

	private void catchError(String field, String message) {
		errors.put(field, message);
	}

	/**
	 * Méthode principale, controlant les informations venant du formulaire et
	 * créant l'objet de réponse
	 * @param request
	 * @return
	 */
	public DetectionQuery createDetectionQuery(HttpServletRequest request) {
		// Récupération des champs du formulaire
		String year = this.getField(request, FIELD_YEAR);
		String month = this.getField(request, FIELD_MONTH);
		String day = this.getField(request, FIELD_DAY);
		String timeSlice = this.getField(request, FIELD_TIME_SLICE);
		String numberEvent = this.getField(request, FIELD_NUMBER_EVENT);
		String numberWord = this.getField(request, FIELD_NUMBER_WORD);
		String weightWord = this.getField(request, FIELD_WEIGHT_WORD);
		String mergerThreshold = this.getField(request, FIELD_MERGER_THRESHOLD);

		// Objet résultat
		DetectionQuery detectionQuery = new DetectionQuery();

		// Validation de chaque paramètre en fonction de ce qui est attendu
		try {
			detectionQuery.setDateCorpus(this.validationDateCorpus(year, month, day));
		} catch (Exception exception) {
			this.catchError(FIELD_DATE_CORPUS, exception.getMessage());
		}

		try {
			detectionQuery.setTimeSlice(this.validationTimeSlice(timeSlice));
		} catch (Exception exception) {
			this.catchError(FIELD_TIME_SLICE, exception.getMessage());
		}

		try {
			detectionQuery.setNumberEvent(this.validationNumberEvent(numberEvent));
		} catch (Exception exception) {
			this.catchError(FIELD_NUMBER_EVENT, exception.getMessage());
		}

		try {
			detectionQuery.setNumberWord(this.validationNumberWord(numberWord));
		} catch (Exception exception) {
			this.catchError(FIELD_NUMBER_WORD, exception.getMessage());
		}

		try {
			detectionQuery.setWeightWord(this.validationWeightWord(weightWord));
		} catch (Exception exception) {
			this.catchError(FIELD_WEIGHT_WORD, exception.getMessage());
		}

		try {
			detectionQuery.setMergerThreshold(this.validationMergerThreshold(mergerThreshold));
		} catch (Exception exception) {
			this.catchError(FIELD_MERGER_THRESHOLD, exception.getMessage());
		}

		// Gestion des erreurs
		if (errors.isEmpty()) {
			result = "Form OK. Please wait";
		} else {
			result = "Wrong field. Please try again";
		}

		return detectionQuery;
	}

	/**
	 * Méthode de validation de la date du corpus d'entrée
	 */
	private DateTime validationDateCorpus(String year, String month, String day) throws Exception {
		if (year == null || month == null || day == null) {
			throw new Exception("Missing date corpus");
		}
		DateTime dateCorpus = new DateTime(year + "-" + month + "-" + day);
		return dateCorpus;
	}

	/**
	 * Méthode de validation de la tranche temporelle utilisée par MABED
	 */
	private Integer validationTimeSlice(String timeSlice) throws Exception {
		if (timeSlice == null) {
			throw new Exception("Missing time slice");
		}
		Integer timeSliceParsed = Integer.parseInt(timeSlice);
		if (timeSliceParsed < 5 || timeSliceParsed > 120) {
			throw new Exception("Out of range time slice");
		}
		return timeSliceParsed;
	}

	/**
	 * Méthode de validation de nombre d'évènement voulu
	 */
	private Integer validationNumberEvent(String numberEvent) throws Exception {
		if (numberEvent == null) {
			throw new Exception("Missing number event");
		}
		Integer numberEventParsed = Integer.parseInt(numberEvent);
		if (numberEventParsed < 5 || numberEventParsed > 80) {
			throw new Exception("Out of range number event");
		}
		return numberEventParsed;
	}

	/**
	 * Méthode de validation du nombre de mot voulu par évènement
	 */
	private Integer validationNumberWord(String numberWord) throws Exception {
		if (numberWord == null) {
			throw new Exception("Missing number word");
		}
		Integer numberWordParsed = Integer.parseInt(numberWord);
		if (numberWordParsed < 1 || numberWordParsed > 40) {
			throw new Exception("Out of range number word");
		}
		return numberWordParsed;
	}

	/**
	 * Méthode de validation du poids minimum de chaque mot liés
	 */
	private Double validationWeightWord(String weightWord) throws Exception {
		if (weightWord == null) {
			throw new Exception("Missing weight word");
		}
		Double weightWordParsed = Double.parseDouble(weightWord);
		if (weightWordParsed < 0.01 || weightWordParsed > 1) {
			throw new Exception("Out of range weight word");
		}
		return weightWordParsed;
	}

	/**
	 * Méthode de validation de la marge de fusion de deux évènements
	 */
	private Double validationMergerThreshold(String mergerThreshold) throws Exception {
		if (mergerThreshold == null) {
			throw new Exception("Missing merger threshold");
		}
		Double mergerThresholdParsed = Double.parseDouble(mergerThreshold);
		if (mergerThresholdParsed < 0.01 || mergerThresholdParsed > 1) {
			throw new Exception("Out of range merger threshold");
		}
		return mergerThresholdParsed;
	}

	/**
	 * Méthode de récupération d'un champ du scope de la requête
	 */
	private String getField(HttpServletRequest request, String fieldName) {
		String field = request.getParameter(fieldName);
		if (field == null || field.trim().length() == 0) {
			return null;
		} else {
			return field;
		}
	}

}
