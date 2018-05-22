package streamAPI;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.DB;
import com.mongodb.util.JSON;

public class MongoDBManager {
	
	private ProcessBuilder pb;
	private Process p;
	private Mongo mongo;
	private DB database;
	private DBCollection collection;
	
	
	public MongoDBManager()
	{
		try {
			mongo = new Mongo("localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void startMongoServer()
	{
		String path = "/home/ozan/workspace/TwitterStream/src/streamAPI/mongoDB/mongod";
		ArrayList<String> commands = new ArrayList<String>();
		commands.add(path);
		
		pb = new ProcessBuilder(commands);
		try {
			p = pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void startMongoServer(String pathForMongoServer)
	{
		pb = new ProcessBuilder(pathForMongoServer);
		try {
			p = pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void shutDownMongoServer()
	{
		p.destroy();
	}
	
	public void connectDatabase(String db)
	{
		database = mongo.getDB(db);
	}
	
	public void getCollection(String collectionName)
	{
		collection = database.getCollection(collectionName);
	}
	
	public boolean insertElement(DBObject dbo)
	{
		BasicDBObject query = new BasicDBObject();
		query.put("text", dbo.get("text").toString());
		if (collection.find(query).count() == 0)
		{
			collection.insert(dbo);
			return true;
		}
		else
			return false;
	}
	
	public void insertElement(String jsonObject)
	{
		DBObject dbObject = (DBObject) JSON.parse(jsonObject);
		collection.insert(dbObject);
	}
	
	public long getCount()
	{
		return collection.getCount();
	}

}
