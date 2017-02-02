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
		<table class="table table-condensed">
			<thead>
				<tr>
					<s:iterator value="variables">
						<th><s:property/></th>
					</s:iterator>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="qResults" >     
	             	<tr>
	                        <s:iterator>
	                            <td><s:property value="value"/></td>
	                        </s:iterator>
	                 </tr>
	                    
	                </s:iterator>
	               </tbody>
		</table>
	</div>
</body>
</html>