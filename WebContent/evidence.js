function changeModality(modal){
	$("#modify").removeAttr('disabled');
	
	var scenario = $("#Scenario").val();
	$("#mod").val(modal);
	
	$( "#progressbar" ).progressbar({
        value: 0
      });
	
	$.ajax({
		url:"modality", 
		data:{
				scenarioID: scenario,
				mod: modal
			}, 
		success:function(jsonResponse){
				var trHTML="<thead>\n<tr><th>Name</th><th>Mass</th><th>Belief</th><th>Plausiability</th></tr>\n</thead>\n<tbody>\n";
				$.each(jsonResponse.info, function(key, value){
					trHTML+="<tr><td>"+key+"</td>";
					$.each(value, function(key, value){
						trHTML+="<td>"+value.toFixed(3)+"</td>";
					});
					trHTML+="</tr>\n";
				});
				trHTML+="</tbody>";
				$("#evTable").html(trHTML);
				$('#evTable').dataTable();
				$("#progressbar").progressbar("destroy")
			},
		xhr:function(){
			var xhr = new window.XMLHttpRequest();
			xhr.addEventListener("progress", function(evt){
			      if (evt.lengthComputable) {
			        var percentComplete = (evt.loaded / evt.total) * 100.0;
			        console.log(percentComplete);
			        $("#progressbar").progressbar("value", percentComplete);
			      }
			    }, false);
			    return xhr;
		}
	});
	
	return false;
}


$(document).ready(function(){
	$("#modify").attr('disabled','disabled');
	$("#modify").click(function(){
		var scenario = $("#Scenario").val();
		var modal = $("#mod").val();
		var url = "massModInfo?scenarioID="+scenario+"&mod="+modal
		$("#dialog").load(url).dialog({modal: true},"open");
	});
});



function modifyMasses(){
	$("#dialog").dialog("close");
	var scenario = $("#Scenario").val();
	var modal = $("#mod").val();
	var massMdID = $("#massMod:checked").val();
	
	$( "#progressbar" ).progressbar({
        value: 0
    });
	
	$.ajax({
		url:"modify",
		data:{
			scenarioID: scenario,
			mod: modal,
			massModID: massMdID
		},
		success:function(jsonResponse){
			var trHTML="<thead>\n<tr><th>Name</th><th>Mass</th><th>Belief</th><th>Plausiability</th></tr>\n</thead>\n<tbody>\n";
			$.each(jsonResponse.info, function(key, value){
				trHTML+="<tr><td>"+key+"</td>";
				$.each(value, function(key, value){
					trHTML+="<td>"+value.toFixed(3)+"</td>";
				});
				trHTML+="</tr>\n";
			});
			trHTML+="</tbody>";
			$("#evTable").html(trHTML);
			$('#evTable').dataTable();
			$("#progressbar").progressbar("destroy")
		},
		xhr:function(){
			var xhr = new window.XMLHttpRequest();
			xhr.addEventListener("progress", function(evt){
			      if (evt.lengthComputable) {
			        var percentComplete = (evt.loaded / evt.total) * 100.0;
			        $("#progressbar").progressbar("value", percentComplete);
			      }
			    }, false);
			    return xhr;
		}
	});
}