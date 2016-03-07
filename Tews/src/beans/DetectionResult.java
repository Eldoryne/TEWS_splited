package beans;

import java.util.HashMap;
import java.util.Map;

public class DetectionResult {

	private Map<Integer, Event> events = new HashMap<>();
	private Corpus corpus = new Corpus();

	public Map<Integer, Event> getEvents() {
		return events;
	}

	public void setEvents(Map<Integer, Event> events) {
		this.events = events;
	}

	public Corpus getCorpus() {
		return corpus;
	}

	public void setCorpus(Corpus corpus) {
		this.corpus = corpus;
	}
	
	
}
