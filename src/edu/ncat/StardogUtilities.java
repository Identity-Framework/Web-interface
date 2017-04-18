package edu.ncat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.riot.web.HttpOp;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

/**
 * This class provideds utility functions for the Stardog Triplestore.
 * The only  function provided now is the select query execution.
 * But the construct and ask query capiablities  will be added in the future.
 * @author William Nick
 *
 */

public class StardogUtilities {
	
	private String connStr;
	private String dbName;
	
	/**
	 * The constructor reads the config.prop file and constructs the query string for the triplestore
	 * @throws IOException
	 */
	
	public StardogUtilities() throws IOException{
		//open and load the properties file for the triple store
		Properties prop = new Properties();
		InputStream is = getClass().getClassLoader().getResourceAsStream("/edu/ncat/config.prop");
		prop.load(is);
		// retrives the properties db for the name of the database and creates the connection string
		dbName = prop.getProperty("db");
		if(Integer.parseInt(prop.getProperty("stardog.port")) != 80){
			connStr = prop.getProperty("stardog.url") +":"+ Integer.parseInt(prop.getProperty("stardog.port")) +"/"+dbName+"/query";
		}
		else{
			connStr = prop.getProperty("stardog.url")  +"/"+ dbName+"/query";
		}
		
	}
	
	/**
	 * This method is used to execute select queries on the stardog triplestore using the RESTful web service exposed by stardog.
	 * @param qStr the select query string
	 * @param useReasoner boolean value to denote if we use the built-in reasoner in the triplestore
	 * @return the resultset with the data from the query from the triplestore
	 */
	
	public ResultSet query(String qStr, boolean useReasoner){
		// create the select query from the value qStr
		Query q = QueryFactory.create(qStr);
		//create the provider that allows the triple store to authenticate the HTTP client using the right crendentials
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		Credentials credentials = new UsernamePasswordCredentials("admin", "admin");
		credsProvider.setCredentials(AuthScope.ANY, credentials);
		//create the HTTP client that will allow for data to be queried from the triplestore
		HttpClient httpclient = HttpClients.custom()
		    .setDefaultCredentialsProvider(credsProvider)
		    .build();
		HttpOp.setDefaultHttpClient(httpclient);
		//create the mechanism that allows for querying the triplestore using the stardog REST API
		QueryEngineHTTP qexec =new QueryEngineHTTP(connStr, q, httpclient);
		qexec.addParam("Accept", "application/sparql-results+json");
		//checks to see whether we use the reasoner if true we set the reasoner to true meaning we use the built-in reasoner
		if(useReasoner){
			qexec.addParam("reasoning", "true");
		}
		return qexec.execSelect();
	}
	
}
