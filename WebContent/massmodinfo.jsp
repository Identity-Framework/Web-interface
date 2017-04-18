<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<s:iterator value="massModText">
	<label><input type="radio" id="massMod" value="<s:property value="key"/>"><s:property value="value"/></label><br />
</s:iterator>
<button onclick="modifyMasses()">Modify</button>