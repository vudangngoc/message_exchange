<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Manage devices states</title>
</head>
<body>
  <jsp:include page="./menu.jsp" />
	<form:form method="POST" action="./state" modelAttribute="stateModel">
		<div>
			<div style="display: inline;">Select device</div>
			<div style="display: inline;">
				<form:select path="deviceId" multiple="false">
					<form:options items="${stateModel.getDeviceList()}" />
				</form:select>
			</div>
			<div style="display: inline;">
				<input type="button" value="Refresh" onclick="window.location='./state';" /> 
			</div>
		</div>
		<div>
			<div style="display: inline;">Set state of device</div>
			<div style="display: inline;">
				<form:select path="state">
					<form:option value="ON">ON</form:option>
					<form:option value="OFF">OFF</form:option>
				</form:select>
			</div>
			<div style="display: inline;">
				<input type="submit" value="Submit" name="action" />
			</div>
		</div>
	</form:form>
</body>
</html>