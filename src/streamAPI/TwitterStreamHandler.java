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
		  .setOAuthConsumerKey("CttJv7ikyd6xoAuAF8G1w")
		  .setOAuthConsumerSecret("V2OROxvEH3p6oMmHC5biI9ZocqcMuLlNbISOMr2ahng")
		  .setOAuthAccessToken("512102247-1QlNfmJqTLcfVbOjzmEnR3ctR6Y2nKilwrxWStDd")
		  .setOAuthAccessTokenSecret("U2FfH0iUPKVXl92EuIhRkZKJ0kp1rJOSWJEL5ZY4y0");
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
