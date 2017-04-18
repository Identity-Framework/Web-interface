package edu.ncat.struts2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.xquery.XQException;
import javax.xml.xquery.XQResultSequence;

import com.opensymphony.xwork2.ActionSupport;

import edu.ncat.XQueryUtilities;

public class MassModAction extends ActionSupport{
	private int scenarioID;
	private String mod;
	private HashMap<Integer, String> massModText;
	public String execute() throws MalformedURLException, XQException, IOException{
		
		massModText = new HashMap<Integer, String>();
		System.out.println(mod);
		XQueryUtilities xquMassMod = new XQueryUtilities("massMod.xq");
		
		String url = this.getClass().getResource("/edu/ncat/resources/dempstershafer.xml").toString();
		
		xquMassMod.prepareQuery(url, "dempstershafer.xml");
		xquMassMod.addVariable("sID", new Integer(scenarioID));
		xquMassMod.addVariable("feature", mod);
		XQResultSequence result = xquMassMod.executeQuery();
		int id = 1;
		while(result.next()){
			String text = result.getItemAsString(null);
			getMassModText().put(id, text);
			id++;
		}
		
		return this.SUCCESS;
	}

	public int getScenarioID() {
		return scenarioID;
	}

	public void setScenarioID(int scenarioID) {
		this.scenarioID = scenarioID;
	}

	public String getMod() {
		return mod;
	}

	public void setMod(String mod) {
		this.mod = mod;
	}

	public HashMap<Integer, String> getMassModText() {
		return massModText;
	}

	public void setMassModText(HashMap<Integer, String> massModText) {
		this.massModText = massModText;
	}
}
