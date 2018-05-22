package streamAPI;

import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import twitter4j.StatusAdapter;


public final class ITUTwitterStream extends StatusAdapter {

    public static void main(String[] args){
    	if(args.length != 4)
    	{
    		System.out.println("Wrong program call!!!");
    		System.out.println("ITUTwitterStream dbName collectionName mongodPath langDetectPath");
    		return;
    	}
    	
    	final MongoDBManager mongoManager = new MongoDBManager();
    	mongoManager.startMongoServer(args[2]);
    	try {
			Thread.currentThread().sleep(2500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    	mongoManager.connectDatabase(args[0]);
    	mongoManager.getCollection(args[1]);
    	
    	try {
			DetectorFactory.loadProfile(args[3]);
		} catch (LangDetectException e) {
			System.out.println("Lang Detect Error : No feature.");
		}
    	
    	while(true)
    	{
    		StreamThread st = new StreamThread();
    		st.mongoManager = mongoManager;
    		Thread t = new Thread(st);
    		t.start();
			
    		synchronized (t) {
				try {
					t.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
    	}
    	
    }
}
