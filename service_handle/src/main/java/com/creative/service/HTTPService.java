package com.creative.service;

import java.io.PrintStream;

import com.creative.context.Context;
import com.creative.disruptor.DisruptorHandle;

public class HTTPService {
	private DisruptorHandle disrupt;
	private StringBuilder builder;
	public HTTPService(){
		super();
		this.builder = new StringBuilder();
		builder.append("<html>")
				.append("<head>")
				.append("<script type=\"text/javascript\">")
				.append("function sendMessage(){")
				.append("request = '%7B';")
				.append("request += 'COMMAND:' + document.getElementById(\"command\").value + ',';")
				.append("request += 'FROM:' + document.getElementById(\"from\").value + ',';")
				.append("request += 'TO:' + document.getElementById(\"to\").value + ',';")
				.append("request += 'DATA:' + document.getElementById(\"data\").value;")
				.append("request += '%7D';")
				.append("var xmlHttp = new XMLHttpRequest();")
				.append("xmlHttp.open(\"GET\", request, true );")
				.append("xmlHttp.send();")
				.append("document.getElementById(\"result\").value = xmlHttp.responseText;")
				.append("}")
				.append("</script>")
				.append("</head>")
				.append("<body>")
				.append("<div>")
				.append("<div style=\"height:auto;width:30%;\">From:</div><div style=\"height:auto;width:700%;\"><input type=\"text\" id=\"from\"></div>")
				.append("<div style=\"height:auto;width:30%;\">To:</div><div style=\"height:auto;width:700%;\"><input type=\"text\" id=\"to\"></div>")
				.append("<div style=\"height:auto;width:30%;\">Command:</div><div style=\"height:auto;width:700%;\"><input type=\"text\" id=\"command\"></div>")
				.append("<div style=\"height:auto;width:30%;\">Data:</div><div style=\"height:auto;width:700%;\"><input type=\"text\" id=\"data\"></div>")
				.append("</div>")
				.append("<input type=\"submit\" onclick=\"sendMessage()\" value=\"Submit\">")
				.append("<div id=\"result\"></div>")
				.append("</body>")
				.append("</html>");
		
	}

	public void onMessage(PrintStream printStream, String path) {
		if(path.startsWith("/demo"))
			printStream.println(builder.toString());
		printStream.close();
	}



}
