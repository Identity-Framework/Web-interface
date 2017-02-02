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


public class StardogUtilities {
	
	private String connStr;
	private String dbName;
	
	public StardogUtilities() throws IOException{
		Properties prop = new Properties();
		InputStream is = getClass().getClassLoader().getResourceAsStream("/edu/ncat/config.prop");
		prop.load(is);
		dbName = prop.getProperty("db");
		if(Integer.parseInt(prop.getProperty("stardog.port")) != 80){
			connStr = prop.getProperty("stardog.url") +":"+ Integer.parseInt(prop.getProperty("stardog.port")) +"/"+dbName+"/query";
		}
		else{
			connStr = prop.getProperty("stardog.url")  +"/"+ dbName+"/query";
		}
		
	}
	
	public ResultSet query(String qStr, boolean useReasoner){
		Query q = QueryFactory.create(qStr);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		Credentials credentials = new UsernamePasswordCredentials("admin", "admin");
		credsProvider.setCredentials(AuthScope.ANY, credentials);
		HttpClient httpclient = HttpClients.custom()
		    .setDefaultCredentialsProvider(credsProvider)
		    .build();
		HttpOp.setDefaultHttpClient(httpclient);
		QueryEngineHTTP qexec =new QueryEngineHTTP(connStr, q, httpclient);
		qexec.addParam("Accept", "application/sparql-results+json");
		if(useReasoner){
			qexec.addParam("reasoning", "true");
		}
		return qexec.execSelect();
	}
	
}
