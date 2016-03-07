package collecte;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;
import java.util.TimerTask;

import parametres.Parametres;
import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import twitter4j.conf.ConfigurationBuilder;

public class Requete extends TimerTask implements UserStreamListener {

	private TwitterStream twitterStream;
	private String prefixeFichier;

	/**
	 * Requete standard, sans critères
	 */
	public Requete() {
		this.twitterStream = new TwitterStreamFactory(new ConfigurationBuilder().setDebugEnabled(true)
				.setOAuthConsumerKey(Parametres.getConsumerKey()).setOAuthConsumerSecret(Parametres.getConsumerSecret())
				.setOAuthAccessToken(Parametres.getAccessToken())
				.setOAuthAccessTokenSecret(Parametres.getAccessTokenSecret()).build()).getInstance();
		this.twitterStream.addListener(this);
		this.prefixeFichier = "sample";
	}

	public TwitterStream getTwitterStream() {
		return twitterStream;
	}

	public void setTwitterStream(TwitterStream twitterStream) {
		this.twitterStream = twitterStream;
	}

	public String getPrefixeFichier() {
		return prefixeFichier;
	}

	public void setPrefixeFichier(String prefixeFichier) {
		this.prefixeFichier = prefixeFichier;
	}

	@Override
	public void run() {
		this.twitterStream.sample();
	}

	/*
	 * Lors de la récupération d'un tweet = status
	 */
	@Override
	public void onStatus(Status status) {
		try {
			PrintWriter ecrivain = new PrintWriter(new BufferedWriter(new FileWriter(
					"collecte\\" + this.prefixeFichier + "-" + Parametres.getDateFichier().format(new Date()) + ".txt",
					true)));
			if (status.getLang().equals("en")) {
				ecrivain.println(Parametres.getDateTweet().format(status.getCreatedAt()));
				ecrivain.println(status.getUser().getScreenName());
				ecrivain.println(status.getText().replaceAll(Parametres.getDelimiteurRegex(), " "));
				ecrivain.close();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void onException(Exception exception) {
		exception.printStackTrace();
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrubGeo(long arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStallWarning(StallWarning arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTrackLimitationNotice(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBlock(User arg0, User arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeletionNotice(long arg0, long arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDirectMessage(DirectMessage arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFavorite(User arg0, User arg1, Status arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFollow(User arg0, User arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFriendList(long[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnblock(User arg0, User arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnfavorite(User arg0, User arg1, Status arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUnfollow(User arg0, User arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserListCreation(User arg0, UserList arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserListDeletion(User arg0, UserList arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserListMemberAddition(User arg0, User arg1, UserList arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserListMemberDeletion(User arg0, User arg1, UserList arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserListSubscription(User arg0, User arg1, UserList arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserListUnsubscription(User arg0, User arg1, UserList arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserListUpdate(User arg0, UserList arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUserProfileUpdate(User arg0) {
		// TODO Auto-generated method stub

	}

}
