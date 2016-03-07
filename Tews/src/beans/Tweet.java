package beans;

import org.joda.time.DateTime;

public class Tweet {

	private DateTime date = new DateTime();
	private String text = new String();
	private String user = new String();

	public DateTime getDate() {
		return this.date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
