package edu.ncat.demstershafer;

import java.io.IOException;

import edu.ncat.StardogUtilities;

public abstract class DempsterShafer {

	private StardogUtilities sdUtil;
	
	public DempsterShafer(){
		try {
			sdUtil = new StardogUtilities();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	

}
