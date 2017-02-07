package edu.ncat.struts2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQResultSequence;

import com.opensymphony.xwork2.ActionSupport;
import com.saxonica.xqj.SaxonXQDataSource;

import edu.ncat.VelocityUtils;
import edu.ncat.dempstershafer.DSInfo;
import edu.ncat.dempstershafer.DempsterShafer;

public class DempsterShaferAction extends ActionSupport{
	
	private int scenarioID;
	
	private ArrayList<HashMap<String, DSInfo>> info;
	
	public String execute() throws Exception{
		ClassLoader classLoader = getClass().getClassLoader();
		VelocityUtils veXquery = new VelocityUtils(classLoader.getResource("sClassName.vm").getFile());
		HashMap<String, Object> tm = new HashMap<String, Object>();
		tm.put("sID", new Integer(getScenarioID()));
		veXquery.setupTemplate(tm);
		String xQueryStr = veXquery.toString();
		InputStream is = new ByteArrayInputStream(xQueryStr.getBytes());
		
	    XQDataSource ds = new SaxonXQDataSource();
	    XQConnection conn = ds.getConnection();
	    XQPreparedExpression exp = conn.prepareExpression(is);
	    XQResultSequence result = exp.executeQuery();
	    
	    String classname = result.getItemAsString(null);
	    
	    Class temp = Class.forName(classname);
	    
	    DempsterShafer dempster = (DempsterShafer) temp.newInstance();
	    dempster.prepareOutFile();
	    this.setInfo(dempster.runInterpreter());
	    
	    return this.SUCCESS;
	    
	}

	public ArrayList<HashMap<String, DSInfo>> getInfo() {
		return info;
	}

	public void setInfo(ArrayList<HashMap<String, DSInfo>> info) {
		this.info = info;
	}

	public int getScenarioID() {
		return scenarioID;
	}

	public void setScenarioID(int scenarioID) {
		this.scenarioID = scenarioID;
	}
}
