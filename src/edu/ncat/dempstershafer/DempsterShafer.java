package edu.ncat.dempstershafer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import com.github.andrewoma.dexx.collection.Function;
import com.itranswarp.compiler.JavaStringCompiler;

import edu.ncat.StardogUtilities;
import edu.ncat.VelocityUtils;
import pl.joegreen.lambdaFromString.LambdaCreationException;
import pl.joegreen.lambdaFromString.LambdaFactory;
import pl.joegreen.lambdaFromString.TypeReference;

public class DempsterShafer implements Runnable{

	private PythonInterpreter interpreter;
	private String filename;
	private ClassLoader classLoader;
	private LambdaFactory lambdaFactory;
	private Function<Double, Double> massFunc;
	private HashMap<String, Double> mass;
	private int threshold;
	private Method[] methods;
	
	public DempsterShafer() throws IOException{
		lambdaFactory = LambdaFactory.get();
		interpreter = new PythonInterpreter();
		classLoader = getClass().getClassLoader();
		interpreter.execfile(classLoader.getResource("ds1.py").getFile());
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public void setMassFunction(String mFunc) throws LambdaCreationException{
		lambdaFactory = LambdaFactory.get();
		massFunc = lambdaFactory.createLambda(mFunc, new TypeReference<Function<Double, Double>>(){});
	}
	
	public void prepareData(String prepCode, String modCode) throws ClassNotFoundException, IOException{
		JavaStringCompiler jdkCompiler = new JavaStringCompiler();
		VelocityUtils vuProg = new VelocityUtils(classLoader.getResource("dsPrepCode.vm").getFile()); 
		
		HashMap<String, Object> obj = new HashMap<String, Object>();
		obj.put("prepcode", prepCode);
		obj.put("modcode", modCode);
		
		vuProg.setupTemplate(obj);
		
		Map<String, byte[]> results = jdkCompiler.compile("DempsterShaferUtils.java", vuProg.toString());
        Class<?> clazz = jdkCompiler.loadClass("edu.ncat.dempstershafer.DempsterShaferUtils", results);
        
        methods = clazz.getMethods();
        
	}

	@Override
	public void run() {
		if(new File(filename).exists()){
			return;
		}
		try {
			mass = (HashMap<String, Double>) methods[0].invoke(null, null);
			double total = 0;
			for(String key: mass.keySet()){
				if(mass.get(key) > getThreshold()){
					mass.put(key, 0.0);
				}
				else{
					mass.put(key, massFunc.invoke(mass.get(key)));
				}
				total+=mass.get(key);
			}
			if (total>1){
				for(String key: mass.keySet()){
					mass.put(key, mass.get(key)/total );
				}
			}
			
			else{
				mass.put("All", 1.0 - total);
			}
			
			mass = (HashMap<String, Double>) methods[1].invoke(null, mass);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter fout = null;
		
		try {
			fout = new PrintWriter(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String key: mass.keySet()){
			fout.print(key);
			fout.print("\t");
			fout.print(String.format("%.3f", mass.get(key)));
		}
		fout.close();
		
	}
	
	public HashMap<String, DSInfo> runInterpreter(){
		StringWriter out = new StringWriter();
		interpreter.setOut(out);
		
		PyObject makeMass = interpreter.get("make_mass");
		PyObject outMeasures = interpreter.get("out_measures");
		
		PyObject mass = makeMass.__call__(new PyString(filename));
		PyObject printData = outMeasures.__call__(mass);
		
		Scanner scanner = new Scanner(out.toString());
		
		Pattern pat = Pattern.compile("([A-Za-z0-9]{,15})\\s+(0\\.\\d{3}|1)\\s+(0\\.\\d{3}|1)");
		
		scanner.nextLine();
		HashMap<String, DSInfo> hm = new HashMap<String, DSInfo>();
		
		while(scanner.hasNext()){
			Matcher m = pat.matcher(scanner.nextLine());
			if(m.find()){
				DSInfo info = new DSInfo();
				info.setBelief(Double.parseDouble(m.group(1)));
				info.setPlausiblity(Double.parseDouble(m.group(2)));
				hm.put(m.group(0), info);
			}
			
		}
		return hm;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	

}
