package factories;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;
import org.jdom.input.SAXBuilder;

import beans.Corpus;
import beans.Event;
import beans.EventRDF;
import beans.IntegrationQuery;
import beans.IntegrationResult;
import beans.Pair;

/**
 * Classe métier gérant le récupération des informations complémentaires aux
 * évènements détectés par MABED grace aux tweets des médias et formatage de
 * leur forme en un évènement Wikitimes
 * 
 * @author Myk
 *
 */
public class Integrator {

	private static final String ATT_INTEGRATION_QUERY = "integrationQuery";
	private static final String REGEX_URL = "(http|https).*?(\\s|$)";
	private static final Pattern PATTERN_URL = Pattern.compile(REGEX_URL);

	/**
	 * Méthode lançant la récupération des informations complémentaires des
	 * évènements à partir des infos spécifié dans le formulaire d'intégration
	 * 2/3
	 * 
	 * @param request
	 * @return
	 */
	public IntegrationResult integrateEvents(HttpServletRequest request) {

		IntegrationResult integrationResult = new IntegrationResult();
		integrationResult.setIntegrationQuery((IntegrationQuery) request.getAttribute(ATT_INTEGRATION_QUERY));
		integrationResult.getIntegrationQuery().getEvents().forEach(
				(eventId, event) -> integrationResult.getEventsRDF().put(eventId, this.transcriptionEvent(event,
						integrationResult.getIntegrationQuery().getDetectionResult().getCorpus())));

		return integrationResult;
	}

	/**
	 * Transformation d'un évènement MABED en son équivalent RDF grâce aux
	 * informations recueillies
	 * 
	 * @param event
	 * @param corpus
	 * @return
	 */
	private EventRDF transcriptionEvent(Event event, Corpus corpus) {
		EventRDF eventRDF = new EventRDF();

		eventRDF.setId(event.getId());
		eventRDF.setDate(event.getDate());
		Map<String, Double> words = new HashMap<>();
		// Fixe le poids d'un mot principal à 1.1
		event.getMainWord().forEach(word -> words.put(word, 1.1));
		event.getRelatedWord().forEach((word, weight) -> words.put(word, weight));
		Pair location = new Pair();
		Pair description = new Pair();

		// Pour chaque mot lié, détermine si c'est un lieu et si son poids est
		// supérieur au précédent
		words.forEach((word, weight) -> {
			if (this.isLocation(word) && weight > location.getWeight()) {
				location.setWeight(weight);
				location.setWord(word);
			}
		});
		eventRDF.setLocation(location.getWord());

		// Récupére les mots liés correspondant à des URIS dans DBpedia
		words.forEach((word, weight) -> {
			if (this.URI(word) == true) {
				eventRDF.getEntities().add(word);
			}
		});

		// Utilise les tweets des médias pour trouver une description
		corpus.getTweetsMedia().forEach(tweet -> {
			Pair pair = findDescription(tweet.getText(), words);
			if (pair.getWeight() > description.getWeight()) {
				description.setWeight(pair.getWeight());
				description.setWord(pair.getWord());
			}
		});

		Matcher matcher = PATTERN_URL.matcher(description.getWord());
		while (matcher.find()) {
			description.setWord(description.getWord().replaceFirst(REGEX_URL, ""));
			eventRDF.getSources().add(matcher.group(0));
		}

		eventRDF.setDescription(description.getWord());

		return eventRDF;
	}

	/**
	 * Utilise les tweets des médias pour trouver une description à un évènement
	 * 
	 * @param tweetText
	 * @param words
	 * @return
	 */
	private Pair findDescription(String tweetText, Map<String, Double> words) {
		Pair tweetWeight = new Pair();
		tweetWeight.setWord(tweetText);
		words.forEach((word, weight) -> {
			if (tweetText.contains(word)) {
				tweetWeight.setWeight(weight + tweetWeight.getWeight());
			}
		});
		return tweetWeight;
	}

	/**
	 * Méthode permettant d'aller vérifier si oui ou non un mot donné est une
	 * location dans Geonames
	 * 
	 * @param noun
	 * @return
	 */
	private boolean isLocation(String noun) {
		try {
			// "tewsaccount" est un compte geonames associé au programme
			WebService.setUserName("tewsaccount");
			ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
			searchCriteria.setQ(noun);

			// Les critères de recherche sur Geonames sont les entités
			// politiques, capitales, villes et régions de premier ordre,
			// continent
			searchCriteria.setFeatureCodes(new String[] { "PCLI", "ADM1", "CONT", "PPLC", "PPLA" });
			ToponymSearchResult searchResult = WebService.search(searchCriteria);

			if (!searchResult.getToponyms().isEmpty()) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * Détermine si un mot est une ressource sur DBpedia ou non
	 * 
	 * @param ressource
	 * @return
	 */
	private boolean URI(String ressource) {
		boolean bool = false;
		// Très vieille méthode de requetage HTTP que j'ai récupéré sur le
		// projet précédent, qui marche mais mériterai d'être mise à jour
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			httpclient.addRequestInterceptor(new HttpRequestInterceptor() {
				public void process(final HttpRequest request, final HttpContext context)
						throws HttpException, IOException {
					if (!request.containsHeader("Accept-Encoding")) {
						request.addHeader("Accept", "text/xml");
					}
				}
			});

			// Url de requête DBpedia
			HttpGet httpget = new HttpGet("http://spotlight.dbpedia.org/rest/annotate?text=" + ressource
					+ ".0&support=0&spotter=CoOccurrenceBasedSelector&disambiguator=Default&policy=whitelist&types=&sparql=");

			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			// Lorsque la réponse est récupéré, analyser son contenu avec un
			// parser XML
			if (entity != null && response.getStatusLine().getStatusCode() == 200) {
				String content = EntityUtils.toString(entity);

				org.jdom.input.SAXBuilder saxBuilder = new SAXBuilder();
				org.jdom.Document doc = saxBuilder.build(new StringReader(content));
				if (doc.getRootElement().getChild("Resources") != null) {
					String type = doc.getRootElement().getChild("Resources").getChild("Resource")
							.getAttributeValue("types");
					// Récupére les entités de types suivants :
					bool = type.matches("(?i).*(organisation|organization|team|person).*");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return bool;
	}
}
