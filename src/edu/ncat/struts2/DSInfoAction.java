package edu.ncat.struts2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.xml.xquery.XQException;
import javax.xml.xquery.XQResultSequence;

import com.opensymphony.xwork2.ActionSupport;

import edu.ncat.XQueryUtilities;

public class DSInfoAction extends ActionSupport{
	private int scenario;
	private ArrayList<String> modalities;
	private String scenarioText;
	
	public String populate() throws MalformedURLException, XQException, IOException{
		setModalities(new ArrayList<String>());
		
		XQueryUtilities xquModInfo = new XQueryUtilities("DSMod.xq");
		String url = this.getClass().getResource("/edu/ncat/resources/dempstershafer.xml").toString();
		
		xquModInfo.prepareQuery(url, "dempstershafer.xml");
		xquModInfo.addVariable("sID", new Integer(scenario));
		XQResultSequence result = xquModInfo.executeQuery();
		while(result.next()){
			String mod = result.getItemAsString(null);
			getModalities().add(mod);
		}
		
		XQueryUtilities xquPopulate = new XQueryUtilities("sDescription.xq");
		xquPopulate.prepareQuery(this.getClass().getResource("/edu/ncat/resources/scenario.xml").toString(), "scenario.xml");
		xquPopulate.addVariable("sID",  new Integer(scenario));
		result = xquPopulate.executeQuery();
		
		if(result.next()){
			setScenarioText(result.getItemAsString(null));
		}
		
		return this.SUCCESS;
	}

	public int getScenario() {
		return scenario;
	}

	public void setScenario(int scenario) {
		this.scenario = scenario;
	}

	public ArrayList<String> getModalities() {
		return modalities;
	}

	public void setModalities(ArrayList<String> modalities) {
		this.modalities = modalities;
	}

	public String getScenarioText() {
		return scenarioText;
	}

	public void setScenarioText(String scenarioText) {
		this.scenarioText = scenarioText;
	}


}
