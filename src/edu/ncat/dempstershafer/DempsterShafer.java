package edu.ncat.dempstershafer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.python.core.PyDictionary;
import org.python.core.PyFloat;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PyTuple;
import org.python.util.PythonInterpreter;

import edu.ncat.StardogUtilities;
import edu.ncat.VelocityUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * This class provides the mechanisms for dempster-shafer theory. 
 * 
 * @author William Nick
 *
 */


public class DempsterShafer{

	private PythonInterpreter interpreter;
	private ClassLoader classLoader;
	private HashMap<String, Double> mass;
	private StardogUtilities sdUtil;
	
	/**
	 * Initalizes the neccessary classes required for dempster-shafer
	 * @throws IOException
	 */
	
	public DempsterShafer() throws IOException{
		interpreter = new PythonInterpreter();
		classLoader = getClass().getClassLoader();
		sdUtil = new StardogUtilities();
		interpreter.execfile(classLoader.getResourceAsStream("/edu/ncat/dempstershafer/ds1.py"));
	}



	/**
	 * 
	 * @param threshold
	 * @param qString
	 */
	public void createMasses(double threshold, String qString){
		setMass(new HashMap<String, Double>());
		ResultSet rs = sdUtil.query(qString, false);
		double total = 0.0;
		
		while(rs.hasNext()){
		//
			QuerySolution sol = rs.nextSolution();
			String id = sol.get("?num").toString().split("#")[1];
			double sim = Double.valueOf(sol.get("?sim").toString());
			//
			if(sim > threshold){
				sim = 0.0;
				mass.put(id, sim);
			}
			//
			else{
				sim = 1/(1+Math.pow(Math.E, -8*(threshold*1-sim)));
				mass.put(id, sim);
			}
			total+=sim;
		}
		mass.put("All", 0.0);
		//if the total mass is greater 
		if(total > 1.0){
			for(String ID:mass.keySet()){
				mass.put(ID, mass.get(ID)/total);
			}
		}
		//
		else{
			mass.put("All", 1-total);
		}
		
	}
	
	public void modifyMass(String templateFilename){
		HashSet<String> IDs = new HashSet<String>(mass.keySet());
		IDs.remove("All");
		double all = mass.get("All");
		for(String ID: IDs){
			VelocityUtils template = new VelocityUtils(templateFilename);
			HashMap<String, Object> temp = new HashMap<String, Object>();
			temp.put("idNum", ID);
			template.setupTemplate(temp);
			ResultSet rs = sdUtil.query(template.toString(), false);
			while(rs.hasNext()){
				QuerySolution sol = rs.nextSolution();
				double mult = Double.valueOf(sol.get("?rel").toString());
				double m = mass.get(ID);
				mass.put(ID, (m * mult));
				all += m * (1 - mult);
				
			}
		}
		mass.put("All", all);
	}
	
	public Map<String, Map<String, Double>> calculateBelAndPlaus(){
		PyDictionary masses = hashmapToDictionary();
		
		PyObject outMeasures = interpreter.get("out_measures");
		PyObject data = outMeasures.__call__(masses);
		
		Map<String, Map<String, Double>> map = null;
		
		if(data instanceof PyString){
			ObjectMapper mapper = new ObjectMapper();
			map = new HashMap<String, Map<String, Double>>();
			try {
				map = mapper.readValue(data.asString(), new TypeReference<Map<String, Map<String, Double>>>(){});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return map;
		
	}
	
	

	public HashMap<String, Double> getMass() {
		return mass;
	}

	public void setMass(HashMap<String, Double> mass) {
		this.mass = mass;
	}
	
	private PyDictionary hashmapToDictionary(){
		PyDictionary masses = new PyDictionary();
		for(String ID: mass.keySet()){
			if(!ID.equals("All")){
				PyObject newID = new PyTuple(new PyObject[]{new PyString(ID)});
				PyObject mass1 = new PyFloat(mass.get(ID).doubleValue());
				masses.put(newID, mass1);
			}
			else{
				PyObject newID = new PyString(ID);
				PyObject mass1 = new PyFloat(mass.get(ID).doubleValue());
				masses.put(newID, mass1);
			}
			
		}
		return masses;
	}
	
	
	public Map<String, Map<String, Double>> combine(String RuleID, DempsterShafer demp, double...rel ){
		PyObject mass1 = hashmapToDictionary();
		PyObject mass2 = demp.hashmapToDictionary();
		PyObject newMass = null;
		PyObject com;
		
		switch(RuleID){
			case "dem":
				com = interpreter.get("combine");
				newMass = com.__call__(mass1, mass2);
				break;
			case "yag":
				com = interpreter.get("yager_combine");
				newMass = com.__call__(mass1, mass2);
				break;
		}
		
		PyObject outMeasures = interpreter.get("out_measures");
		PyObject data = outMeasures.__call__(newMass);
		
		Map<String, Map<String, Double>> map = null;
		
		if(data instanceof PyString){
			ObjectMapper mapper = new ObjectMapper();
			map = new HashMap<String, Map<String, Double>>();
			try {
				map = mapper.readValue(data.toString(), new TypeReference<Map<String, Map<String, Double>>>(){});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return map;
	}

}
