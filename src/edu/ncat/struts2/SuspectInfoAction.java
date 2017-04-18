package edu.ncat.struts2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import javax.xml.xquery.XQException;
import javax.xml.xquery.XQResultSequence;

import com.opensymphony.xwork2.ActionSupport;

import edu.ncat.XQueryUtilities;

public class SuspectInfoAction extends ActionSupport{
	private int scenarioID;
	private int index;
	private HashMap<String, String> susInfo;
	
	public String execute() throws MalformedURLException, XQException, IOException{
		setSusInfo(new HashMap<String, String>());
		ClassLoader classLoader = getClass().getClassLoader();
		
		XQueryUtilities xquSuspectInfo = new XQueryUtilities("susInfo.xq");
		xquSuspectInfo.prepareQuery(classLoader.getResource("/edu/ncat/resources").toString()+"scenario.xml", "scenario.xml");
		xquSuspectInfo.addVariable("ind", new Integer(getIndex()));
		xquSuspectInfo.addVariable("sID", new Integer(scenarioID));
		XQResultSequence result = xquSuspectInfo.executeQuery();
		
		if(result.next()){
			susInfo.put("name", result.getItemAsString(null));
		}
		if(result.next()){
			susInfo.put("query", result.getItemAsString(null));
		}
		
		return this.SUCCESS;
	}
	
	public int getScenarioID() {
		return scenarioID;
	}
	public void setScenarioID(int scenarioID) {
		this.scenarioID = scenarioID;
	}

	public HashMap<String, String> getSusInfo() {
		return susInfo;
	}

	public void setSusInfo(HashMap<String, String> susInfo) {
		this.susInfo = susInfo;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	
}
