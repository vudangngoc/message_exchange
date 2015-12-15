<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
			if (typeof result.overrideMimeType != 'undefined') {
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
	function updateTimerList(d){
		var list = document.getElementById("timerList");
		while(list.firstChild)
			list.removeChild(list.firstChild);
		var objs = JSON.parse(d);
		var arr = objs['data'];
		if(arr === undefined) return;
		var tem = new Option('-----------','--------');
		list.options[list.options.length] = tem;
		for(i = 0; i < arr.length; i++){
			var jn = JSON.parse(arr[i]);
			var option = new Option(jn['TIMER_ID'],jn['TIMER_ID']);
			list.options[list.options.length] = option;
		}
	}

	function getTimers(n) {
		request = './getTimers?name=' + n;
		var xmlHttp = createRequest();
		xmlHttp.onreadystatechange = function() {
			if (xmlHttp.readyState != 4)
				return; // Not there yet
			if (xmlHttp.status != 200) {
				// Handle request failure here...
				return;
			}
			// Request successful, read the response
			var resp = xmlHttp.responseText;
			updateTimerList(resp);
		}
		xmlHttp.open("GET", request, true);
		xmlHttp.send();
	}
	function updateTimerDetail(t){
		var data = JSON.parse(t);
		var repeat = data["REPEATLY"];
		var time = data["TIME_FIRE"];
		var command = JSON.parse(data["COMMAND"]);
		var state = command["DATA"];
		document.getElementById("repeatType").value = repeat;
		document.getElementById("time").value = time;
		document.getElementById("state").value = state;
	}
	function getTimer(n) {
		request = './getTimer?id=' + n;
		var xmlHttp = createRequest();
		xmlHttp.onreadystatechange = function() {
			if (xmlHttp.readyState != 4)
				return; // Not there yet
			if (xmlHttp.status != 200) {
				// Handle request failure here...
				return;
			}
			// Request successful, read the response
			var resp = xmlHttp.responseText;
			updateTimerDetail(resp);
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
				<form:select path="deviceId" multiple="false" onchange="getTimers(this.value)">
					<form:option value="">-------------</form:option>
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
				<form:select id="timerList" path="timerId" onchange="getTimer(this.value)">
				</form:select>
			</div>
		</div>
		<div>
			<div style="display: inline;">Select repeat type</div>
			<div style="display: inline;">
				<form:select id="repeatType" path="repeatType">					
					<form:option value="REPEAT_MINUTELY">REPEAT_MINUTELY</form:option>
					<form:option value="REPEAT_HOURLY">REPEAT_HOURLY</form:option>
					<form:option value="REPEAT_DAILY">REPEAT_DAILY</form:option>
					<form:option value="REPEAT_WEEKLY">REPEAT_WEEKLY</form:option>
					<form:option value="REPEAT_NONE">REPEAT_NONE</form:option>
				</form:select>
			</div>
		</div>
		<div>
					<div style="display: inline;">Set time</div>
			<div style="display: inline;">
				<form:input id="time" type="text" cssStyle="width:200px" path="time"/>
			</div>
		</div>
		<div>
			<div style="display: inline;">Set state of device</div>
			<div style="display: inline;">
				<form:select id="state" path="state">
					<form:option value="ON">ON</form:option>
					<form:option value="OFF">OFF</form:option>
				</form:select>
			</div>
			</div>
			<div>
			<div style="display: inline;">
				
				<input type="submit" name="action" value="Create" />
				<input type="submit" name="action" value="Modify" />
				<input type="submit" name="action" value="Delete" />
			</div>
		</div>
		<div>
		<p>There are ${size} timer(s)</p>
		</div>
	</form:form>
</body>
</html>