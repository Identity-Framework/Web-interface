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
  <script src="https://code.jquery.com/jquery-3.2.1.min.js" integrity="sha256-hwg4gsxgFZhOsEEamdOYGBf13FyQuiTwlAQgxVSNgt4=" crossorigin="anonymous"></script>
  <script type="text/javascript" src="investigation.js"></script>
</head>
<body>
	<div class="jumbotron text-center">
		<h1>Cafreci Web Interface</h1>
	</div>
	<div class="container">
		<div class="row">
			<div class="col-sm-6">
			<form action="query" id="action">
					<select id="scenario" name="scenario" style="float: left">
						<s:iterator value="scenarios">
							<option value="<s:property value="key"/>"><s:property value="value" /></option>
						</s:iterator>
						
					</select>
				<br />
				<div>
					<h3>Scenario</h3>
				</div>
				<p  id="text">
					<span><s:property value="scenarioText" /></span>
	    		</p>
			
			<input type="button" onclick="evidence()" value="View Evidence"  class="btn" />
			<input type="submit" value="View Queries" class="btn" />
			</form>
			</div>
	    
		    <div class="col-sm-6">
			    <ul class="nav nav-pills" id="tabs">
			    	<s:iterator value="info">
		 				<li><a href="#" onclick="changeSuspectInfo(<s:property value="key" />)"><s:property value="value"/></a></li>
		 			</s:iterator>
		  			
				</ul>
				<table id="suspect" class="table table-striped">
				
				</table>
			</div>
		</div>
		
	</div>
</body>
</html>