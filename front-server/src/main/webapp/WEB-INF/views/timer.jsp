<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Manage devices states</title>
<script type="text/javascript">
	function createRequest() {
		var result = null;
		if (window.XMLHttpRequest) {
			// FireFox, Safari, etc.
			result = new XMLHttpRequest();
			if (typeof xmlhttp.overrideMimeType != 'undefined') {
				result.overrideMimeType('text/xml'); // Or anything else
			}
		} else if (window.ActiveXObject) {
			// MSIE
			result = new ActiveXObject("Microsoft.XMLHTTP");
		} else {
			// No known mechanism -- consider aborting the application
		}
		return result;
	}

	function getTimers(n) {
		request = '/getTimer?name=' + n;
		var xmlHttp = createRequest();
		xmlHttp.onreadystatechange = function() {
			if (req.readyState != 4)
				return; // Not there yet
			if (req.status != 200) {
				// Handle request failure here...
				return;
			}
			// Request successful, read the response
			var resp = req.responseText;
			// ... and use it as needed by your app.
		}
		xmlHttp.open("GET", request, true);
		xmlHttp.send();
	}
</script>
</head>
<body>
	<form:form method="POST" action="./timer" modelAttribute="timerModel">
		<div>
			<div style="display: inline;">Select device</div>
			<div style="display: inline;">
				<form:select path="deviceId" multiple="false">
					<form:options items="${timerModel.getDeviceList()}" />
				</form:select>
			</div>
			<div style="display: inline;">
				<input type="button" value="Refresh"
					onclick="window.location='./timer';" />
			</div>
		</div>
		<div>
			<div style="display: inline;">Select timer</div>
			<div style="display: inline;">
				<form:select id="timerList" path="timerId">
				</form:select>
			</div>
		</div>
		<div>
			<div style="display: inline;">Select repeat type</div>
			<div style="display: inline;">
				<form:select path="repeatType">
					<form:option value="REPEAT_NONE">REPEAT_NONE</form:option>
					<form:option value="REPEAT_MINUTELY">REPEAT_MINUTELY</form:option>
					<form:option value="REPEAT_HOURLY">REPEAT_HOURLY</form:option>
					<form:option value="REPEAT_DAILY">REPEAT_DAILY</form:option>
					<form:option value="REPEAT_WEEKLY">REPEAT_WEEKLY</form:option>
				</form:select>
			</div>
		</div>
		<div>
					<div style="display: inline;">Set time</div>
			<div style="display: inline;">
				<form:input type="text" path="time"/>
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