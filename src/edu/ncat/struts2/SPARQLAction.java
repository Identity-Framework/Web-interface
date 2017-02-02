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
	private boolean [] criteria;
	private String qStr;
	private String scenario;
	
	private List<String> variables;
	private List<HashMap<String, String>> qResults;
	
	
	public boolean [] getCriteria() {
		return criteria;
	}

	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}

	public void setCriteria(boolean [] criteria) {
		this.criteria = criteria;
	}

	public String execute() throws IOException{
		if(criteria.length == 0 || criteria == null){
			return this.ERROR;
		}
		
		HashMap<String, Object> tempMap = createTemplateMapping();
		VelocityUtils vuSparql = new VelocityUtils("sparqlTemplate.vm");
		vuSparql.setupTemplate(tempMap);
		qStr = vuSparql.toString();
		sdUtil = new StardogUtilities();
		ResultSet rs = sdUtil.query(qStr, false);
		
		if(rs == null){
			return this.ERROR;
		}
		
		variables = rs.getResultVars();
		qResults = new ArrayList<HashMap<String, String>>();
		while(rs.hasNext()){
			HashMap<String, String> temp = new HashMap<String, String>();
			QuerySolution soln = rs.nextSolution();
			for(String var: variables){
				RDFNode x = soln.get(var);
				temp.put(var, x.toString());
			}
			qResults.add(temp);
		}
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
	
	public HashMap<String, Object> createTemplateMapping(){
		HashMap<String, Object> temp = new HashMap<String, Object>();
		ArrayList <String> vars = new ArrayList<String>();
		
		for(int i = 0; i < criteria.length; i++){
			if(criteria[i]){
				switch(i){
				case 0:
					temp.put("OfficerName", new Boolean(true));
					temp.put("offNameVar", new String("?offName"));
					vars.add("?offName");
					break;
				case 1:
					
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				case 6:
					break;
				case 7:
					break;
				case 8:
					break;
				}
			}
		}
		temp.put("varList", vars);
		return temp;
	}
	
}
