package edu.ncat.dempstershafer.dao;

import java.util.HashMap;


import edu.ncat.dempstershafer.DSInfo;

public class DSInfoDO {
	private long scenarioID;
	private HashMap <String, DSInfo> info;
	
	public HashMap <String, DSInfo> getInfo() {
		return info;
	}
	public void setInfo(HashMap <String, DSInfo> info) {
		this.info = info;
	}
	
	public long getScenarioID() {
		return scenarioID;
	}
	public void setScenarioID(long scenarioID) {
		this.scenarioID = scenarioID;
	}
}
