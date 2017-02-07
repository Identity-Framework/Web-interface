package edu.ncat.dempstershafer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.python.core.PyObject;
import org.python.core.PyString;

public class MugFpDempsterShafer extends DempsterShafer{
	
	private String fpFilename;
	private String mugFilename;
	
	public MugFpDempsterShafer(){
		super();
		fpFilename = "fp_from_sit.txt";
		mugFilename = "photo_from_sit.txt";
	}

	@Override
	public void prepareOutFile() throws Exception {
		ArrayList<Double> similarities = new ArrayList<Double>();
		ArrayList<String> ids = new ArrayList<String>();
		ResultSet rs;
		PrintWriter w;
		ClassLoader classLoader = getClass().getClassLoader();
		//Setting up SPARQL query to print out record numbers and fp similarity measures
		StringBuffer qString = new StringBuffer();
		qString.append(
				"PREFIX sitterms: <http://esterline.ncat.edu/identity#>"+
						"PREFIX biom: <http://esterline.ncat.edu/biometric#>"+
						"PREFIX recterms: <http://esterline.ncat.edu/personalrecords#>"+
						"SELECT ?num ?sim "+
						"WHERE { "
						+ "?infon sitterms:simMeasure ?sim ."
						+"?infon sitterms:fpRecorded ?fp ."
						+"?rec biom:hasFpImage ?fp ."
						+"?num recterms:hasRecord ?rec ."
						+"}");
		rs = sdUtil.query(qString.toString(), false);

		while(rs.hasNext()){
			QuerySolution sol = rs.nextSolution();
			System.out.print(sol.get("?num").toString()+", ");
			System.out.println(sol.get("?sim").toString());
			similarities.add(Double.valueOf(sol.get("?sim").toString()));
			String id = sol.get("?num").toString();
			ids.add(id.substring(id.indexOf('#')+1));
		}


		//adding another item to the list for all
		similarities.add(0.0);
		ids.add("All");
		

		Double[] sim = similarities.toArray(new Double[similarities.size()]);
		String[] recordNums = ids.toArray(new String[ids.size()]);
		Double total = 0.0;
		for(int i=0; i<sim.length-1;i++){
			//First we apply the threshold
			if (sim[i]>.65){sim[i] = 0.0;}
			//Then a reverse sigmoid function (reversed by scaling by -8) with a cutpoint at 0.65
			else{sim[i]=1/(1+Math.pow(Math.E, -8*(0.65-1*sim[i])));}
			System.out.print(recordNums[i] + ", ");
			System.out.println(sim[i]);
			total+=sim[i];

		}

		//If the total is more than 1, normalize
		if (total>1){
			for(int i=0; i<sim.length;i++){
				sim[i]/=total;
			}
		}
		//Otherwise,remaining mass goes to all
		else{
			sim[sim.length-1] = 1.0 - total;
		}

		w = new PrintWriter(classLoader.getResource("").getPath()+"unmodified_fp.txt");
		for(int i=0; i<sim.length;i++){
			System.out.println(recordNums[i]+" \t"+String.format("%.3f", sim[i]));
			w.print(recordNums[i]);
			w.print(" \t");
			w.println(String.format("%.3f", sim[i]));
		}
		w.close();
		
		for(int i=0; i<recordNums.length-1; i++){
			String idNum = recordNums[i];
			//SITUATION 1 MASS MODIFICATION for analyst ratings
			StringBuffer s1qString = new StringBuffer();
			s1qString.append(
					"PREFIX sitterms: <http://esterline.ncat.edu/identity#>"+
							"PREFIX lawterms: <http://esterline.ncat.edu/lawenforcement#>"+
							"PREFIX biom: <http://esterline.ncat.edu/biometric#>"+
							"PREFIX recterms: <http://esterline.ncat.edu/personalrecords#>"+
							"PREFIX insys: <http://esterline.ncat.edu/insystem#>"+
							"SELECT ?rel ?fp"+
							"WHERE { "
							+"insys:"+idNum + " recterms:hasRecord ?rec ."
							+"?rec biom:hasFpImage ?fp ."
							+"?infon sitterms:fpRecorded ?fp ."
							+"?infon sitterms:fpAnalyst ?officer ."
							+"?officer lawterms:Reliability ?rel ."
							+"}");


				rs = sdUtil.query(s1qString.toString(), false);
				QuerySolution sol = rs.nextSolution();
				System.out.println(sol);
				double mult = Double.valueOf(sol.get("?rel").toString());
				System.out.println(recordNums[i]+", "+mult+","+sim[i]*mult);
				sim[sim.length-1] += sim[i]*(1 - mult);
				sim[i] *= mult;
				
				StringBuffer s3qString = new StringBuffer();
				s3qString.append(
						"PREFIX sitterms: <http://esterline.ncat.edu/identity#>"+
								"PREFIX lawterms: <http://esterline.ncat.edu/lawenforcement#>"+
								"PREFIX biom: <http://esterline.ncat.edu/biometric#>"+
								"PREFIX recterms: <http://esterline.ncat.edu/personalrecords#>"+
								"PREFIX insys: <http://esterline.ncat.edu/insystem#>"+
								"SELECT ?rel "+
								"WHERE { "
								+"?infon sitterms:fpSubject insys:"+idNum + " ."
								+"?infon sitterms:fpTakingOfficer ?officer ."
								+"?officer lawterms:Reliability ?rel ."
								+"}");

				rs = sdUtil.query(s3qString.toString(), false);

					while(rs.hasNext()){
						sol = rs.nextSolution();
						sim[sim.length-1] += sim[i]*(1 - Double.valueOf(sol.get("?rel").toString()));
						sim[i] *= Double.valueOf(sol.get("?rel").toString());
					}
		}
		w = new PrintWriter(classLoader.getResource("").getPath()+"fp_from_sit.txt");
		for(int i=0; i<sim.length;i++){
			w.print(recordNums[i]);
			w.print(" \t");
			w.println(String.format("%.3f", sim[i]));
		}
		w.close();
		
		ArrayList<Double> photoSimilarities = new ArrayList<Double>();
		ArrayList<String> photoIds = new ArrayList<String>();
		//Setting up SPARQL query to print out record numbers and photo similarity measures
		StringBuffer qString2 = new StringBuffer();
		qString2.append(
				"PREFIX sitterms: <http://esterline.ncat.edu/identity#>"+
						"PREFIX biom: <http://esterline.ncat.edu/biometric#>"+
						"PREFIX recterms: <http://esterline.ncat.edu/personalrecords#>"+
						"SELECT ?num ?sim "+
						"WHERE { "
						+ "?infon sitterms:simPicMeasure ?sim ."
						+"?infon sitterms:picRecorded ?fp ."
						+"?rec biom:hasFacialImage ?fp ."
						+"?num recterms:hasRecord ?rec ."
						+"}");

		rs = sdUtil.query(qString2.toString(), false);

			while(rs.hasNext()){
				QuerySolution sol = rs.nextSolution();
				System.out.print(sol.get("?num").toString()+", ");
				System.out.println(sol.get("?sim").toString());
				photoSimilarities.add(Double.valueOf(sol.get("?sim").toString()));
				String photoId = sol.get("?num").toString();
				photoIds.add(photoId.substring(photoId.indexOf('#')+1));
			}

		//adding another item to the list for all
		photoSimilarities.add(0.0);
		photoIds.add("All");

		//Modified mass function:
		//Threshold for mass: similarity<.5 => 0
		//similarity>.5 => sigmoid function

		Double[] photoSim = photoSimilarities.toArray(new Double[photoSimilarities.size()]);
		String[] suspectNums = photoIds.toArray(new String[photoIds.size()]);
		Double total2 = 0.0;
		for(int i=0; i<photoSim.length-1;i++){
			if (photoSim[i]>.65){photoSim[i] = 0.0;}
			else{
				photoSim[i]=1/(1+Math.pow(Math.E, -8*(0.65-1*photoSim[i])));
				total2+=photoSim[i];
			}
		}
		//If the total is more than 1, normalize
		if (total2>1){
			for(int i=0; i<photoSim.length;i++){
				photoSim[i]/=total2;
			}
		}
		//Otherwise,remaining mass goes to all
		else{
			photoSim[photoSim.length-1] = 1.0 - total;
		}

		for(int i=0; i<suspectNums.length; i++){
			String idNum = suspectNums[i];
			//System.out.println(idNum);

			//Situation 2 Mass Modification
			StringBuffer s2qString = new StringBuffer();
			s2qString.append(
					"PREFIX sitterms: <http://esterline.ncat.edu/identity#>"+
							"PREFIX lawterms: <http://esterline.ncat.edu/lawenforcement#>"+
							"PREFIX biom: <http://esterline.ncat.edu/biometric#>"+
							"PREFIX recterms: <http://esterline.ncat.edu/personalrecords#>"+
							"PREFIX insys: <http://esterline.ncat.edu/insystem#>"+
							"SELECT ?rel "+
							"WHERE { "
							+"insys:"+idNum + " recterms:hasRecord ?rec ."
							+"?rec biom:hasFacialImage ?pic ."
							+"?infon sitterms:picRecorded ?pic ."
							+"?infon sitterms:picAnalyst ?officer ."
							+"?officer lawterms:Reliability ?rel ."
							+"}");

				rs = sdUtil.query(s2qString.toString(), false);
				while(rs.hasNext()){
					QuerySolution sol = rs.nextSolution();
					//System.out.println(sol.get("?rel").toString());
					photoSim[photoSim.length-1] += photoSim[i]*(1 - Double.valueOf(sol.get("?rel").toString()));
					photoSim[i] *= Double.valueOf(sol.get("?rel").toString());
				}

			//Situation 6* mass modifications

			StringBuffer s6qString = new StringBuffer();
			s6qString.append(
					"PREFIX sitterms: <http://esterline.ncat.edu/identity#>"+
							"PREFIX lawterms: <http://esterline.ncat.edu/lawenforcement#>"+
							"PREFIX biom: <http://esterline.ncat.edu/biometric#>"+
							"PREFIX recterms: <http://esterline.ncat.edu/personalrecords#>"+
							"PREFIX insys: <http://esterline.ncat.edu/insystem#>"+
							"SELECT ?rel "+
							"WHERE { "
							+"insys:"+idNum + " recterms:hasRecord ?rec ."
							+"?rec biom:hasFacialImage ?pic ."
							+"?infon sitterms:mugRecorded ?pic ."
							+"?infon sitterms:mugTakingOfficer ?officer ."
							+"?officer lawterms:Reliability ?rel ."
							+"}");

			
				rs = sdUtil.query(s6qString.toString(), false);
				while(rs.hasNext()){
					QuerySolution sol = rs.nextSolution();
					//System.out.println(sol.get("?rel").toString());
					photoSim[photoSim.length-1] += photoSim[i]*(1 - Double.valueOf(sol.get("?rel").toString()));
					photoSim[i] *= Double.valueOf(sol.get("?rel").toString());
				}
			}
		

		w = new PrintWriter(classLoader.getResource("").getPath()+"photo_from_sit.txt");
		for(int i=0; i<photoSim.length;i++){
			w.print(suspectNums[i]);
			w.print(" \t");
			w.println(String.format("%.3f", photoSim[i]));
		}
		w.close();
		
	}

	@Override
	public ArrayList<HashMap<String, DSInfo>> runInterpreter() {
		ClassLoader classLoader = getClass().getClassLoader();
		
		StringWriter out = new StringWriter();
		interpreter.setOut(out);
		
		interpreter.execfile(classLoader.getResource("ds1.py").getFile());
		
		PyObject makeMass = interpreter.get("make_mass");
		PyObject outMeasures = interpreter.get("out_measures");
		PyObject combine = interpreter.get("combine");

		PyObject massFp = makeMass.__call__(new PyString(classLoader.getResource("").getPath()+fpFilename));
		PyObject printData = outMeasures.__call__(massFp);
		
		
		PyObject massPhoto = makeMass.__call__(new PyString(classLoader.getResource("").getPath()+mugFilename));
		PyObject printData2 = outMeasures.__call__(massPhoto);
		
		//Now to combine the masses: currently using Dempster's Rule
		PyObject combinedMass = combine.__call__(massFp, massPhoto);
		PyObject printCombined = outMeasures.__call__(combinedMass);
		
		interpreter.close();
		
		Scanner scanner = new Scanner(out.toString());
		
		


		
		return null;
	}

}
