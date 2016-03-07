package beans;

import java.util.HashMap;
import java.util.Map;

public class IntegrationResult {

	private Map<Integer, EventRDF> eventsRDF = new HashMap<>();
	private IntegrationQuery integrationQuery = new IntegrationQuery();

	public Map<Integer, EventRDF> getEventsRDF() {
		return eventsRDF;
	}

	public void setEventsRDF(Map<Integer, EventRDF> eventsRDF) {
		this.eventsRDF = eventsRDF;
	}

	public IntegrationQuery getIntegrationQuery() {
		return integrationQuery;
	}

	public void setIntegrationQuery(IntegrationQuery integrationQuery) {
		this.integrationQuery = integrationQuery;
	}
	
	
}
