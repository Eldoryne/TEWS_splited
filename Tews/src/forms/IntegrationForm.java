package forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import beans.DetectionResult;
import beans.IntegrationQuery;

/**
 * Objet de contrôle du formulaire d'intégration 2/3
 * 
 * @author Myk
 *
 */
public class IntegrationForm {

	public static final String FIELD_LOCATIONS = "locations";
	public static final String FIELD_ENTITIES = "entities";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_SOURCES = "sources";
	public static final String FIELD_PAGE_NAME = "pageName";
	public static final String DETECTION_RESULT = "detectionResult";

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
	 * @param session
	 * @return
	 */
	public IntegrationQuery createIntegrationQuery(HttpServletRequest request, HttpSession session) {
		// Récupération des champs du formulaire
		String locations = this.getField(request, FIELD_LOCATIONS);
		String entities = this.getField(request, FIELD_ENTITIES);
		String description = this.getField(request, FIELD_DESCRIPTION);
		String sources = this.getField(request, FIELD_SOURCES);

		// Objet résultat
		IntegrationQuery integrationQuery = new IntegrationQuery();
		// Ajout du résultat du formulaire précédent
		integrationQuery.setDetectionResult((DetectionResult) session.getAttribute(DETECTION_RESULT));
		// Mappage des paramètres
		Map<String, String[]> parameters = request.getParameterMap();

		// Validation de chaque évènement détecté
		parameters.forEach((eventId, nothing) -> {
			Integer eventIdParsed = null;
			try {
				eventIdParsed = this.validationEvent(eventId);
				if (eventIdParsed != null) {
					integrationQuery.getEvents().put(eventIdParsed,
							integrationQuery.getDetectionResult().getEvents().get(eventIdParsed));
				}
			} catch (Exception exception) {
				this.catchError(DETECTION_RESULT, exception.getMessage());
			}

		});

		// Gestion des erreurs
		if (errors.isEmpty()) {
			result = "Form OK. Please wait";
		} else {
			result = "Wrong field. Please try again";
		}

		return integrationQuery;
	}

	/**
	 * Méthode de validation d'un évènement détecté
	 */
	private Integer validationEvent(String eventId) throws Exception {
		Integer eventIdParsed = null;
		if (!eventId.equals(FIELD_DESCRIPTION) && !eventId.equals(FIELD_ENTITIES) && !eventId.equals(FIELD_LOCATIONS)
				&& !eventId.equals(FIELD_PAGE_NAME) && !eventId.equals(FIELD_SOURCES)) {
			eventIdParsed = Integer.parseInt(eventId);
			if (eventIdParsed == null || eventIdParsed < 1 || eventIdParsed > 80) {
				throw new Exception("Out of range eventId.");
			}
		}
		return eventIdParsed;
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
