package edu.ncat;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

/**
 * This class provides utilities for the velocity template engine
 * This class provides utilities to allow a person to:
 * <ul>
 * <li>setup the velocity engine</li>
 * <li>setup templates with the correct information</li>
 * <li>turn the results into a string</li>
 * </ul>
 * @author William Nick
 *
 */

public class VelocityUtils {
	private VelocityContext context;
	private Template tempSPARQL;
	
	/**
	 * sets up the velocity engine with the proper directory and sets up the velocity template and context
	 * @param template the file name of the template that we are using
	 */
	public VelocityUtils(String template){
		URL url = this.getClass().getResource("/edu/ncat/resources/");
		File file = new File(url.getFile());
		//Sets up the velocity engine and the the proper path location for the template engine to look for the templates 
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
		ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, file.getAbsolutePath());
		ve.init();
		//creates the velocity context and get the template from the proper location
		context = new VelocityContext();
		tempSPARQL = ve.getTemplate(template);
	}
	
	/**
	 * setups the template with the proper data in place of the variables in the template
	 * @param map a hashmap where the key-value pairs are the variable name and the value is the data that fills in that variable
	 */
	
	public void setupTemplate(HashMap<String, Object> map){
		for (Entry<String, Object> entry : map.entrySet()) {
			context.put(entry.getKey(), entry.getValue());
		}
	}
	/**
	 * Overloaded the toString method in the java class Object
	 * @return the template with the filled in places where the variables were
	 */
	
	public String toString(){
		StringWriter writer = new StringWriter();
		tempSPARQL.merge(context, writer);
		return writer.toString();
	}

}
