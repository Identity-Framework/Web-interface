package edu.ncat.struts2;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import javax.xml.xquery.XQResultSequence;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import edu.ncat.XQueryUtilities;
import edu.ncat.dempstershafer.DempsterShafer;

public class DempsterShaferAction extends ActionSupport{
		
	private Map<String, Map<String, Double>> info;
	private int scenarioID;
	private String mod;
	
	public String execute() throws Exception{
		
		String sessionName = "scenario"+scenarioID+"_"+mod;
		Map session = ActionContext.getContext().getSession();
		if(session.containsKey(sessionName)){
			DempsterShafer ds = (DempsterShafer) session.get(sessionName);
			info = ds.calculateBelAndPlaus();
			return this.SUCCESS;
		}
		
		
		String qStr = null;
		double threshold = 0.0;
		XQueryUtilities xquModInfo = new XQueryUtilities("modInfo.xq");
		
		String url = this.getClass().getResource("/edu/ncat/resources/dempstershafer.xml").toString();
	    xquModInfo.prepareQuery(url, "dempstershafer.xml");
		
		xquModInfo.addVariable("sID", scenarioID);
		xquModInfo.addVariable("feature", mod);
		
		XQResultSequence result = xquModInfo.executeQuery();
		
		if(result.next()){
			qStr = result.getItemAsString(null);
			qStr = qStr.replaceAll("&lt;", "<");
			qStr = qStr.replaceAll("&gt;", ">");
		}
		
		if(result.next()){
			threshold = Double.parseDouble(result.getItemAsString(null));
		}
		
		DempsterShafer ds = new DempsterShafer();
		ds.createMasses(threshold, qStr);
		info = ds.calculateBelAndPlaus();
		session.put(sessionName, ds);
	    return this.SUCCESS;
	    
	}

	public Map<String, Map<String, Double>> getInfo() {
		return info;
	}

	public void setInfo(Map<String, Map<String, Double>> info) {
		this.info = info;
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
	
}
