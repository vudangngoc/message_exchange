package com.creative.context;



import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import com.lmax.disruptor.EventFactory;

public class Context {
	private Socket client;
	public Context(){}
	public Context(Socket client, String message) {
		this.client = client;
		this.data = new JsonData(message);
	}
	public Socket getClient() {
		return client;
	}
	private IData data = new JsonData("{}");
	public IData getRequest(){
		return data;
	}
	public boolean setResponse(String response){
		DataOutputStream outStream;
		try {
			outStream = new DataOutputStream(client.getOutputStream());
			if(outStream != null){
				outStream.writeBytes(response == null ? "" : response);
				outStream.flush();
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}


	}
}
