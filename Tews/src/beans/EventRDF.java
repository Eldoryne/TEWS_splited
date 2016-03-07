package beans;

import java.util.ArrayList;

import org.joda.time.DateTime;

public class EventRDF {

	private int id = 0;
	private DateTime date = new DateTime();
	private String location =  new String();
	private ArrayList<String> entities = new ArrayList<>();
	private ArrayList<String> sources = new ArrayList<>();
	private String description = new String();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public DateTime getDate() {
		return date;
	}
	public void setDate(DateTime date) {
		this.date = date;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public ArrayList<String> getEntities() {
		return entities;
	}
	public void setEntities(ArrayList<String> entities) {
		this.entities = entities;
	}
	public ArrayList<String> getSources() {
		return sources;
	}
	public void setSources(ArrayList<String> sources) {
		this.sources = sources;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
