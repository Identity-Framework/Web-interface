$(document).ready(function(){
	$("#scenario").on("change", function(ev){
		var sID = $(this).val();
		$.getJSON('description', {
			scenarioID : sID
      }, function(jsonResponse) {
		$("#text span").text(jsonResponse.Description)
	  });
	});
});

function changeSuspectInfo(susID){
	var sID = $("#scenario").find(":selected").val();
	$.getJSON('suspectInfo', {
		scenarioID: sID,
		index: susID
	}, function(jsonResponse){
		var trHTML="";
		$.each(jsonResponse.susInfo, function(key, value) {
			trHTML+="<tr><td>"+key+"</td><td>"+value+"</td></tr>\n";
		});
		 $('#suspect').html(trHTML);
	});
}

function evidence(){
	var form = document.getElementById("action");
	form.action = "evidence";
	form.submit();
}