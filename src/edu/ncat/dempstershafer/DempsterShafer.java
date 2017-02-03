package edu.ncat.dempstershafer;

import java.io.IOException;
import java.util.ArrayList;
import org.python.util.PythonInterpreter;

import edu.ncat.StardogUtilities;

public abstract class DempsterShafer {

	protected StardogUtilities sdUtil;
	protected PythonInterpreter interpreter;
	
	public DempsterShafer(){
		try {
			sdUtil = new StardogUtilities();
			interpreter = new PythonInterpreter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public abstract void prepareOutFile();
	
	public abstract ArrayList runInterpreter();

}
