package edu.ncat;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class VelocityUtils {
	private VelocityContext context;
	private Template tempSPARQL;
	
	public VelocityUtils(String template){
		Velocity.init();
		context = new VelocityContext();
		tempSPARQL = Velocity.getTemplate(template);
	}
	
	public VelocityUtils(Properties prop, String template){
		Velocity.init(prop);
		context = new VelocityContext();
		tempSPARQL = Velocity.getTemplate(template);
	}
	public void setupTemplate(HashMap<String, Object> map){
		for (Entry<String, Object> entry : map.entrySet()) {
			context.put(entry.getKey(), entry.getValue());
		}
	}
	
	public String toString(){
		StringWriter writer = new StringWriter();
		tempSPARQL.merge(context, writer);
		return writer.toString();
	}

}
