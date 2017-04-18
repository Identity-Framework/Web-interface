package edu.ncat;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQItemType;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import com.saxonica.xqj.SaxonXQDataSource;

/**
 * This class provides utilities for the saxon XQuery processor for java
 * 
 * @author William Nick
 *
 */

public class XQueryUtilities {
	
	private XQDataSource ds;
	private XQConnection conn;
	private XQPreparedExpression exp;
	private ClassLoader classLoader;
	
	String filename;
	
	/**
	 * Initalizes the filename and classloader for our XQuery utilities
	 * @param filename the filename for the XQuery files
	 */
	
	public XQueryUtilities(String filename){
		this.filename = filename;
		classLoader = getClass().getClassLoader();
		
	}
	/**
	 * This method prepares the XQuery processor for Querying XML documents
	 * 
	 * @param documentLocation the absolute pathname of the XML file
	 * @param docName the name of the XML document
	 * @throws XQException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	
	public void prepareQuery(String documentLocation, String docName) throws XQException, MalformedURLException, IOException{
		InputStream inputStream = classLoader.getResourceAsStream("/edu/ncat/resources/"+filename);
		
		ds = new SaxonXQDataSource();
		conn = ds.getConnection();
		exp = conn.prepareExpression(inputStream);
		InputStream is = new URL(documentLocation).openStream();
		XQItemType stringType = conn.createAtomicType(XQItemType.XQBASETYPE_STRING);
		exp.bindDocument(new QName("doc"), is, null, null);
		
		
	}
	
	/**
	 * 
	 * 
	 * @param qname the fully qualified name of the variable in the XQuery file
	 * @param var the variable data that will go into the query
	 * @throws XQException
	 */
	
	public void addVariable(String qname, Object var) throws XQException{
		if(var instanceof Integer){
			XQItemType integerType = conn.createAtomicType(XQItemType.XQBASETYPE_INTEGER);
			exp.bindInt(new QName(qname), (Integer)var, integerType);
		}
		else{
			XQItemType stringType = conn.createAtomicType(XQItemType.XQBASETYPE_STRING);
			exp.bindString(new QName(qname), (String)var, stringType);
		}
	}
	
	/**
	 * 
	 * @return an XQResultSequence that returns the data that was queried
	 * @throws XQException
	 */
	public XQResultSequence executeQuery() throws XQException{
		return exp.executeQuery();
	}

}
