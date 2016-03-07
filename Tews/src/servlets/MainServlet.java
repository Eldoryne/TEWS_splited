package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.DetectionQuery;
import beans.DetectionResult;
import beans.IntegrationQuery;
import beans.IntegrationResult;
import factories.Detector;
import factories.Integrator;
import forms.DetectionForm;
import forms.IntegrationForm;

/**
 * Controleur central qui gère le routing, l'accès se fait par l'adresse
 * http://localhost:8080/Tews/run
 * 
 * @author Myk
 *
 */
@WebServlet("/run")
public class MainServlet extends HttpServlet {

	public static final String ATT_PAGE_NAME = "pageName";
	public static final String DETECTION_CASE = "detection";
	public static final String INTEGRATION_CASE = "integration";
	public static final String VISUALISATION_CASE = "visualisation";

	public static final String ATT_DETECTION_QUERY = "detectionQuery";
	public static final String ATT_DETECTION_FORM = "detectionForm";
	public static final String ATT_DETECTION_RESULT = "detectionResult";
	public static final String ATT_DETECTOR = "detector";

	public static final String ATT_INTEGRATION_QUERY = "integrationQuery";
	public static final String ATT_INTEGRATION_FORM = "integrationForm";
	public static final String ATT_INTEGRATION_RESULT = "integrationResult";
	public static final String ATT_INTEGRATOR = "integrator";

	public static final String VIEW_DETECTION_FORM = "/WEB-INF/detectionForm.jsp";
	public static final String VIEW_INTEGRATION_FORM = "/WEB-INF/integrationForm.jsp";
	public static final String VIEW_VISUALISATION_FORM = "/WEB-INF/visualisationForm.jsp";

	/**
	 * Accès au programme par la méthode "get"
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		this.getServletContext().getRequestDispatcher(VIEW_DETECTION_FORM).forward(request, response);
	}

	/**
	 * Accès aux volets suivant par le biais de méthode "post"
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Ouvre une session utilisateur
		HttpSession session = request.getSession();

		// Gère les différents phases du traitement
		switch (request.getParameter(ATT_PAGE_NAME)) {

		// Gestion de la première étape : Detection 1/3
		case DETECTION_CASE:

			// Objet de contrôle du formulaire de détection
			DetectionForm detectionForm = new DetectionForm();
			// Objet fomulaire
			DetectionQuery detectionQuery = detectionForm.createDetectionQuery(request);

			// Ajout des objets dans le scope de la requête
			request.setAttribute(ATT_DETECTION_QUERY, detectionQuery);
			request.setAttribute(ATT_DETECTION_FORM, detectionForm);

			// Si l'objet de contrôle ne trouve pas d'erreurs dans le formulaire
			if (detectionForm.getErrors().isEmpty()) {

				// Création de l'objet métier de détection d'évènements
				Detector detector = new Detector();

				// Récupération des événements détectés par l'objet métier
				DetectionResult detectionResult = detector.detectEvents(request);

				// Ajout des objets dans le scope de la requête
				request.setAttribute(ATT_DETECTOR, detector);
				request.setAttribute(ATT_DETECTION_RESULT, detectionResult);
				// Ajout des objets dans le scope de la session
				session.setAttribute(ATT_DETECTION_RESULT, detectionResult);

				// Redirige la requête vers la suite du formulaire
				this.getServletContext().getRequestDispatcher(VIEW_INTEGRATION_FORM).forward(request, response);

			} else {

				// Redirige la requête vers la même étape pour être retenter
				this.getServletContext().getRequestDispatcher(VIEW_DETECTION_FORM).forward(request, response);

			}

			break;

		// Gestion de la première étape : Integration 2/3
		case INTEGRATION_CASE:

			// Object de contrôle du formulaire d'intégration
			IntegrationForm integrationForm = new IntegrationForm();
			// Objet fomulaire
			IntegrationQuery integrationQuery = integrationForm.createIntegrationQuery(request, session);

			// Ajout des objets dans le scope de la requête
			request.setAttribute(ATT_INTEGRATION_QUERY, integrationQuery);
			request.setAttribute(ATT_INTEGRATION_FORM, integrationForm);

			// Si l'objet de contrôle ne trouve pas d'erreurs dans le formulaire
			if (integrationForm.getErrors().isEmpty()) {

				// Création de l'objet métier d'enrichissement des évènements
				Integrator integrator = new Integrator();
				// Récupération des évènements enrichis
				IntegrationResult integrationResult = integrator.integrateEvents(request);

				// Ajout des objets dans le scope de la requête
				request.setAttribute(ATT_INTEGRATOR, integrator);
				request.setAttribute(ATT_INTEGRATION_RESULT, integrationResult);

				// Redirige la requête vers la suite du formulaire
				this.getServletContext().getRequestDispatcher(VIEW_VISUALISATION_FORM).forward(request, response);

			} else {

				// Redirige la requête vers la même étape pour être retenter
				this.getServletContext().getRequestDispatcher(VIEW_INTEGRATION_FORM).forward(request, response);

			}

			break;

		// Gestion de la première étape : Visualisation 3/3
		case VISUALISATION_CASE:

			// HERE suite du code pour traiter les évènements avec Jena et les
			// afficher par exemple.

			break;

		default:

			// Redirige la requête vers le début du formulaire
			this.getServletContext().getRequestDispatcher(VIEW_DETECTION_FORM).forward(request, response);

		}

	}
}