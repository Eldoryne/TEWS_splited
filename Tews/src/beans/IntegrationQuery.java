package beans;

import java.util.HashMap;
import java.util.Map;

public class IntegrationQuery {

	private Map<Integer, Event> events = new HashMap<>();
	private DetectionResult detectionResult = new DetectionResult();

	public Map<Integer, Event> getEvents() {
		return events;
	}

	public void setEvents(Map<Integer, Event> events) {
		this.events = events;
	}

	public DetectionResult getDetectionResult() {
		return detectionResult;
	}

	public void setDetectionResult(DetectionResult detectionResult) {
		this.detectionResult = detectionResult;
	}
	
}
