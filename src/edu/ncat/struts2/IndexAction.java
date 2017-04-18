package edu.ncat.struts2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.xquery.XQException;
import javax.xml.xquery.XQResultSequence;

import com.opensymphony.xwork2.ActionSupport;

import edu.ncat.XQueryUtilities;

public class IndexAction extends ActionSupport{
	private String scenarioText;
	private HashMap<Integer, String> scenarios;
	private HashMap<Integer, Integer> info;
	
	public String populate() throws XQException, URISyntaxException, MalformedURLException, IOException{
		URL url = this.getClass().getResource("/edu/ncat/resources/");
		String documentURI = url.toString()+"scenario.xml";
		
		scenarios = new HashMap<Integer, String>();
		
		XQueryUtilities xquPopulate = new XQueryUtilities("scenarios.xq");
		xquPopulate.prepareQuery(documentURI, "scenario.xml");
		
		XQResultSequence result = xquPopulate.executeQuery();
		
		int numScenario = 1;
		while(result.next()){
			scenarios.put(numScenario, result.getItemAsString(null));
			numScenario++;
		}
		
		xquPopulate = new XQueryUtilities("sDescription.xq");
		xquPopulate.prepareQuery(documentURI, "scenario.xml");
		xquPopulate.addVariable("sID", new Integer(1));
		result = xquPopulate.executeQuery();
		
		if(result.next()){
			scenarioText = result.getItemAsString(null);
		}
		
		xquPopulate = new XQueryUtilities("scSuspectID.xq");
		xquPopulate.prepareQuery(documentURI, "scenario.xml");
		xquPopulate.addVariable("sID", new Integer(1));
		result = xquPopulate.executeQuery();
		
		int susNum = 1;
		setInfo(new HashMap<Integer, Integer>());
		
		while(result.next()){
			getInfo().put(susNum, Integer.parseInt(result.getItemAsString(null)));
			susNum++;
		}
		
		return this.SUCCESS;
	}

	public String getScenarioText() {
		return scenarioText;
	}

	public void setScenarioText(String scenarioText) {
		this.scenarioText = scenarioText;
	}

	public HashMap<Integer, String> getScenarios() {
		return scenarios;
	}

	public void setScenarios(HashMap<Integer, String> scenarios) {
		this.scenarios = scenarios;
	}

	public HashMap<Integer, Integer> getInfo() {
		return info;
	}

	public void setInfo(HashMap<Integer, Integer> info) {
		this.info = info;
	}

	
}
