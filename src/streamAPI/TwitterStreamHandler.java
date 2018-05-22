package streamAPI;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamHandler {
	
	private Configuration configuration;
	private static Twitter twitter = null;
	
	public TwitterStreamHandler()
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("")
		  .setOAuthConsumerSecret("")
		  .setOAuthAccessToken("")
		  .setOAuthAccessTokenSecret("");
		cb.setJSONStoreEnabled(true);
		configuration = cb.build();
	}
	
	public Configuration getConfiguration() { return this.configuration; }
	public Twitter getTwitterInstance()
	{ 
		if(twitter == null)
		{
			TwitterFactory tf = new TwitterFactory(configuration);
			twitter = tf.getInstance();
		}
		
		return twitter;
	}
	
	public void resetTwitter()
	{
		twitter = null;
	}
}
