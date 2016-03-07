package beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

public class Event {

	private int id = 0;
	private DateTime date = new DateTime();
	private ArrayList<String> mainWord = new ArrayList<>();
	private Map<String, Double> relatedWord = new HashMap<>();

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public DateTime getDate() {
		return this.date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public ArrayList<String> getMainWord() {
		return this.mainWord;
	}

	public void setMainWord(ArrayList<String> mainWord) {
		this.mainWord = mainWord;
	}

	public Map<String, Double> getRelatedWord() {
		return this.relatedWord;
	}

	public void setRelatedWord(Map<String, Double> relatedWord) {
		this.relatedWord = relatedWord;
	}

}
