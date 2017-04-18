package edu.ncat.struts2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import com.opensymphony.xwork2.ActionSupport;
import com.saxonica.xqj.SaxonXQDataSource;

import edu.ncat.VelocityUtils;
import edu.ncat.XQueryUtilities;

public class ScenarioDescriptionAction extends ActionSupport{
	private int scenarioID;
	private Map<String, String> stateMap;

	public int getScenarioID() {
		return scenarioID;
	}

	public void setScenarioID(int scenarioID) {
		this.scenarioID = scenarioID;
	}
	
	public String execute() throws XQException, IOException{
		stateMap = new LinkedHashMap<String, String>();
		
		ClassLoader classLoader = getClass().getClassLoader();
		
		XQueryUtilities xquSusDescription = new XQueryUtilities("sDescription.xq");
		xquSusDescription.prepareQuery(classLoader.getResource("/edu/ncat/resources").toString(), "scenario.xml");
		xquSusDescription.addVariable("sID", new Integer(scenarioID));
	    XQResultSequence result = xquSusDescription.executeQuery();
	    
	    if(result.next()){
	    	stateMap.put("Description", result.getItemAsString(null));
	    }
		
		return this.SUCCESS;
	}

	public Map<String, String> getStateMap() {
		return stateMap;
	}

	public void setStateMap(Map<String, String> stateMap) {
		this.stateMap = stateMap;
	}

}
