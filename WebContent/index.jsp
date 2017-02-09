<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Insert title here</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script type="text/javascript" src="investigation.js"></script>
</head>
<body onload="go()">
	<div class="jumbotron text-center">
		<h1>Cafreci Web Interface</h1>
	</div>
	<div class="container">
		<div class="row">
			<div class="col-sm-6">
				<div>
					<h3>Scenario</h3>
				</div>
				<p  id="text">
					We consider a legal case in which a theft has occurred immediately following a party,
					 providing a list of possible suspects in the form of a guest list. Evidence from the
					 crime scene reveals a group photograph from a security camera with one guest with their
					 hand on the door where the valuables were kept and a fingerprint from that same door. 
					 This case can be described as a constellation of situations, centering around two separate 
					 id -situations for the two pieces of evidence: fingerprint and the snapshot. 
	    		</p>
			</div>
			<div class="col-sm-16">
				<form action="query" method="post">
					<select id="scenario" name="scenario">
						<option value="1">Scenario 1</option>
						<option value="2">Scenario 2</option>
						<option value="3">Scenario 3</option>
					</select><br />
					<div class="checkbox">
						<label><input type="checkbox" name="criteria">the officer who took the fingerprint of the suspect on record</label><br />
						<label><input type="checkbox" name="criteria">the evidence</label><br />
						<label><input type="checkbox" name="criteria">IDs</label><br />
						<label><input type="checkbox" name="criteria">Who lifted the forensic fingerprint</label><br />
						<label><input type="checkbox" name="criteria">Male</label><br />
						<label><input type="checkbox" name="criteria">Male</label><br />
						<label><input type="checkbox" name="criteria">Male</label><br />
						<label><input type="checkbox" name="criteria">Male</label><br />
						<label><input type="checkbox" name="criteria">Male</label><br />
					</div>
					<input type="submit" class="btn btn-info" value="Submit" />
				</form>
			</div>
	    </div>
		
	</div>
</body>
</html>