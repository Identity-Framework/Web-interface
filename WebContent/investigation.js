
function go(){
		document.getElementById("scenario").addEventListener("change", function(e){
		var a = document.getElementById("scenario").selectedIndex;
		 
    if(a=="0"){
		
		document.getElementById("text").innerHTML=("We consider a legal case in which a theft has occurred immediately following a party, providing a list of possible suspects in the form of a guest list. Evidence from the crime scene reveals a group photograph from a security camera with one guest with their hand on the door where the valuables were kept and a fingerprint from that same door. This case can be describe as a constellation of situations, centering around two separate id -situations for the twopieces of evidence: fingerprint and the snapshot.");
	}
	else if(a=="1"){
		document.getElementById("text").innerHTML="Scenario 2";
		
	}
	else if(a=="2"){
		document.getElementById("text").innerHTML="Scenario 3";
		
		
	}
	 });
	
}





