package edu.ncat.dempstershafer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.python.util.PythonInterpreter;

import edu.ncat.StardogUtilities;

public abstract class DempsterShafer {

	protected StardogUtilities sdUtil;
	protected PythonInterpreter interpreter;
	protected String filename;
	
	public DempsterShafer(){
		try {
			sdUtil = new StardogUtilities();
			interpreter = new PythonInterpreter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public abstract void prepareOutFile() throws Exception;
	
	public abstract ArrayList<HashMap<String, DSInfo>> runInterpreter();

}
