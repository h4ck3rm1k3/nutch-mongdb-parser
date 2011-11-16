package org.apache.nutch.tools;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.nutch.util.NutchConfiguration;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * Simple mongodb parser for nutch.  Allows for the generation of seed list of urls
 * directly from a mongodb collection.  Simple queries and limits can also be specified.
 * 
 * Logic example is similiar to that of the DmozParser that comes with the Nutch 1.3 source code.
 * 
 * @author cmorgan
 *
 */
public class MongodbParser {
	public static final Log LOG = LogFactory.getLog(MongodbParser.class);
	
	static String mongodb = "localhost";
	static String database = "dmoz";
	static String collection = "content";
	static String query = "";
	static String limit = "";
	static String field = "url";
	static String location = "mongodb/urls";
	
  /**
   * Iterate through all the items in this from the mongodb query file.
   * Add each URL to the web db.
   * 
   * 
   */	
	public void parseMongodb() {
		
		// Create a a new text file for the urls
		PrintWriter pw;
		Mongo mongo = null;
				    	
    	try {
    		// create the file for the mongodb urls (must ensure that folder is created first)
			pw = new PrintWriter(new FileWriter(location));
			
			mongo = new Mongo(mongodb);
			DB db = mongo.getDB(database);
	        DBCollection col = db.getCollection(collection);
	        
	        // Get the list of sources
	        BasicDBObject search = new BasicDBObject();

	        DBCursor cursor = null;
	        // ADD LOGIC FOR QUERY AND LIMIT HERE
	        // Query goes here
	        if (query != "") {
		        String[] values = query.split(":");
		        String key = values[0];
		        String value = values[1];
		        search.put(key, value);
		        if (limit != "") {
		        	cursor = col.find(search).limit(Integer.parseInt(limit));
		        } else {
		        	cursor = col.find(search);
		        }
	        } else {
	        	if (limit != "") {
		        	cursor = col.find().limit(Integer.parseInt(limit));
		        } else {
		        	cursor = col.find();
		        }
	        }
	        
	        //DBCursor cursor = collection.find(search);

	        while(cursor.hasNext()) {
	        	cursor.next();
	            //System.out.println(cursor.next());
	        	System.out.println(cursor.curr().get(field));
	            pw.println(cursor.curr().get(field));
	        }
	        
	        pw.flush();
	        
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		} finally {
			
			mongo.close();
			mongo = null;
		}
		
	}

	
  /**
   * Command-line access.  User may add URLs from a mongodb database collection
   * for a specified query
   * 
   */
	  public static void main(String argv[]) throws Exception {
	    if (argv.length < 1) {
	      System.err.println("Usage: MongodbParser <mongodb> [-database <database>] [-collection <collection>] [-query <key:value>] [-limit <limit>] [-field <field>] [-location <location>]");
	      return;
	    }
	    
	    Configuration conf = NutchConfiguration.create();
	    FileSystem fs = FileSystem.get(conf);
	    
	    try {
		    for (int i = 1; i < argv.length; i++) {
				if ("-mongodb".equals(argv[i])) {
		        	mongodb = argv[i];
		        } else if ("-db".equals(argv[i])) {
			          collection = argv[i+1];
			          i++;
		        } else if ("-collection".equals(argv[i])) {
		          collection = argv[i+1];
		          i++;
		        } else if ("-query".equals(argv[i])) {
			          query = argv[i+1];
			          i++;
			    } else if ("-limit".equals(argv[i])) {
			          limit = argv[i+1];
			          i++;
			    } else if ("-field".equals(argv[i])) {
			          field = argv[i+1];
			          i++;
			    } else if ("-location".equals(argv[i])) {
			    	location = argv[i+1];
			          i++;
			    } 
		      }
	    	MongodbParser parser = new MongodbParser();
	    	parser.parseMongodb();
	    } finally {
	    	fs.close();
	    }
	  }
}
