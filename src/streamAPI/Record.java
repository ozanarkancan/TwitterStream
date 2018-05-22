package streamAPI;

import java.util.Date;

import com.mongodb.BasicDBObject;

public class Record {
	public String username;
	public String text;
	public Date date;
	public String[] trends;
	
	public Record(String username, String text, Date date, String[] trends)
	{
		this.username = username;
		this.text = text;
		this.date = date;
		this.trends = trends;
	}
	
	@SuppressWarnings("deprecation")
	public Record(BasicDBObject doc)
	{
		this.username = doc.getString("username");
		this.text = doc.getString("text");
		this.date = new Date(doc.getString("date"));
	}
	
	public BasicDBObject convertDBObject()
	{
		BasicDBObject doc = new BasicDBObject();
		doc.put("username", this.username);
		doc.put("text", text);
		doc.put("date", date);
		doc.put("trends", this.trends);
		return doc;
	}
}
