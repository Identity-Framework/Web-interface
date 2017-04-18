package edu.ncat.struts2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.xml.xquery.XQException;
import javax.xml.xquery.XQResultSequence;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import edu.ncat.XQueryUtilities;
import edu.ncat.dempstershafer.DempsterShafer;

public class ModifyAction extends ActionSupport{
	private int scenarioID;
	private String mod;
	private int massModID;
	private Map<String, Map<String, Double>> info;
	
	public String execute() throws MalformedURLException, XQException, IOException{
		String sessionName = "scenario"+getScenarioID()+"_"+getMod()+"modID_"+getMassModID();
		String sessionName2 = "scenario"+getScenarioID()+"_"+getMod();
		Map session = ActionContext.getContext().getSession();
		
		if(session.containsKey(sessionName)){
			DempsterShafer ds = (DempsterShafer) session.get(sessionName);
			setInfo(ds.calculateBelAndPlaus());
			return this.SUCCESS;
		}
		
		String url = this.getClass().getResource("/edu/ncat/resources/dempstershafer.xml").toString();
		
		XQueryUtilities xquMod = new XQueryUtilities("mod.xq");
		xquMod.prepareQuery(url, "dempstershafer.xml");
		xquMod.addVariable("sID", new Integer(scenarioID));
		xquMod.addVariable("feature", mod);
		xquMod.addVariable("mID", massModID);
		
		XQResultSequence result = xquMod.executeQuery();
		
		String templateFilename = null;
		if(result.next()){
			templateFilename = result.getItemAsString(null);
		}
		
		DempsterShafer ds = (DempsterShafer) session.get(sessionName2);
		ds.modifyMass(templateFilename);
		setInfo(ds.calculateBelAndPlaus());
		
		session.put(sessionName, ds);
		
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

	public int getMassModID() {
		return massModID;
	}

	public void setMassModID(int massModID) {
		this.massModID = massModID;
	}

	public Map<String, Map<String, Double>> getInfo() {
		return info;
	}

	public void setInfo(Map<String, Map<String, Double>> info) {
		this.info = info;
	}
}
