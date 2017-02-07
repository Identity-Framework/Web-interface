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
		VelocityUtils veXquery = new VelocityUtils(classLoader.getResource("sDescription.vm").getFile());
		HashMap<String, Object> tm = new HashMap<String, Object>();
		tm.put("sID", new Integer(scenarioID));
		veXquery.setupTemplate(tm);
		String xQueryStr = veXquery.toString();
		InputStream is = new ByteArrayInputStream(xQueryStr.getBytes());
		
	    XQDataSource ds = new SaxonXQDataSource();
	    XQConnection conn = ds.getConnection();
	    XQPreparedExpression exp = conn.prepareExpression(is);
	    XQResultSequence result = exp.executeQuery();
	    
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
