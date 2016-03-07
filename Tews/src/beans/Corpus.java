package beans;

import java.util.ArrayList;

public class Corpus {

	private DetectionQuery detectionQuery = new DetectionQuery();
	private ArrayList<Tweet> tweetsMedia = new ArrayList<>();
	private ArrayList<Tweet> tweets = new ArrayList<>();

	public ArrayList<Tweet> getTweetsMedia() {
		return tweetsMedia;
	}

	public void setTweetsMedia(ArrayList<Tweet> tweetsMedia) {
		this.tweetsMedia = tweetsMedia;
	}

	public DetectionQuery getDetectionQuery() {
		return this.detectionQuery;
	}

	public void setDetectionQuery(DetectionQuery detectionQuery) {
		this.detectionQuery = detectionQuery;
	}

	public ArrayList<Tweet> getTweets() {
		return this.tweets;
	}

	public void setTweets(ArrayList<Tweet> tweets) {
		this.tweets = tweets;
	}

}
