<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Insert title here</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
</head>
<body>
	<div class="jumbotron text-center">
		<h1>Cafreci Web Interface</h1>
	</div>
	<div class="container">
		<div class="row">
			<div class="col-sm-6">
				<h3>Scenario</h3>
				<p><s:property value="scenarioText"/></p>
			
			</div>
			<div class="col-sm-6">
				<table class="table table-condensed">
				</table>
			</div>
		</div>
	</div>
</body>
</html>