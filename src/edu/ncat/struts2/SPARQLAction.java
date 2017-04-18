package edu.ncat.struts2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;

import com.opensymphony.xwork2.ActionSupport;

import edu.ncat.StardogUtilities;
import edu.ncat.VelocityUtils;

public class SPARQLAction extends ActionSupport {
	private StardogUtilities sdUtil;
	private String qStr;
	private String scenario;
	
	private List<String> variables;
	private List<HashMap<String, String>> qResults;
	
	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public String execute() throws IOException{
		
		
		
		return this.SUCCESS;
	}

	public List<String> getVariables() {
		return variables;
	}

	public void setVariables(List<String> variables) {
		this.variables = variables;
	}

	public List<HashMap<String, String>> getqResults() {
		return qResults;
	}

	public void setqResults(List<HashMap<String, String>> qResults) {
		this.qResults = qResults;
	}
		
}
