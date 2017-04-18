<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Insert title here</title>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<link rel="stylesheet" href="//cdn.datatables.net/1.10.13/css/jquery.dataTables.min.css">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/fancybox/3.0.47/jquery.fancybox.min.css">
	<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
	
	<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
	<script type="text/javascript" src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script type="text/javascript" src="//cdn.datatables.net/1.10.13/js/jquery.dataTables.min.js"></script>
	
	<script type="text/javascript" src="evidence.js"></script>
</head>
<body>
	<div class="jumbotron text-center">
		<h1>Cafreci Web Interface</h1>
	</div>
	<input type="hidden" id="Scenario" value="<s:property value="scenario"/>" />
	<input type="hidden" id="mod" />
	
	<div class="container">
		<div class="row">
			<div class="col-sm-6">
				<h3>Scenario</h3>
				<p><s:property value="scenarioText"/></p>
			
			</div>
			<div class="col-sm-6">
				
				<ul class="nav nav-pills" id="tabs">
					<s:iterator value="modalities">
				 		<li><a href="#" onclick='changeModality("<s:property />")'><s:property /></a></li>
				 	</s:iterator>
				 	<li><a href="#" >Combine</a></li>		
				</ul>
				<div id="progressbar"></div>
				<div id="dialog">
				</div>
						
				<table id="evTable">
				</table>
				
				<button id="modify" class="btn">Modify Evidence</button>
		</div>
	</div>
</div>
</body>
</html>