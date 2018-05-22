package streamAPI;

import java.util.Date;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;

import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Trend;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class StreamThread implements Runnable {

	public String[] trends;
	public Twitter twitter;
	public TwitterStreamHandler tsh;
	public TwitterStream twitterStream;
	public static long numberOfTweetsLastStart = 0;
	public MongoDBManager mongoManager;
	public Date now;
	
	public void run() {
		System.out.print("Number of total tweets : ");
    	System.out.println(mongoManager.getCount());
    	System.out.println();
    	StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
            	if(status == null)
            		return;
            	if(!status.isRetweet())
            	{
            		Record r = new Record(status.getUser().getName(), status.getText(), status.getCreatedAt(), trends);
            		Detector detector;
            		String lang = "";
					try {
						detector = DetectorFactory.create();
						detector.append(r.text);
						lang = detector.detect();
					} catch (LangDetectException e) {
						e.printStackTrace();
						lang = "";
					}
                    
            		if(lang.equals("tr"))
            		{
            			Boolean isInserted = mongoManager.insertElement(r.convertDBObject());
            			if(isInserted)
            				numberOfTweetsLastStart++;
            			if(numberOfTweetsLastStart % 5000 == 0)
            			{
            				System.out.println("Restart Time: " + now.toString());
            				System.out.print("Last execution count: ");
            				System.out.println(numberOfTweetsLastStart);
            				synchronized (twitterStream) {
            					twitterStream.shutdown();
            					twitterStream.notify();
							}
            			}
            		}
            	}
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                //System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            	synchronized (twitterStream) {
					twitterStream.shutdown();
					twitterStream.notify();
				}
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                //System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
                synchronized (twitterStream) {
					twitterStream.shutdown();
					twitterStream.notify();
				}
            }
        };
        
        tsh = new TwitterStreamHandler();
        twitterStream = new TwitterStreamFactory(tsh.getConfiguration()).getInstance();
        startStream(listener, 10);
        synchronized (twitterStream){
        	try{
				twitterStream.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
        
        synchronized (Thread.currentThread()) {
        	try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Thread.currentThread().notify();
		}
    }
	
	public void updateTrackList(int limit)
    {
    	trends = new String[limit];
    	try {
    		tsh.resetTwitter();
    		twitter = tsh.getTwitterInstance();
			Trend[] trendsFromTwitter = twitter.getLocationTrends(23424969).getTrends();
			for(int i = 0; i < limit; i++)
	    	{
	    		trends[i] = trendsFromTwitter[i].getName();
	    		System.out.println(trendsFromTwitter[i].getName());
	    	}
			
		} catch (TwitterException e) {
			e.printStackTrace();
		}
    }
    
    public void startStream(StatusListener listener, int limitOfTrack)
    {
        twitterStream.addListener(listener);
        updateTrackList(10);
        twitterStream.filter(new FilterQuery(0, null, trends));
    }

}
