package com.creative.context;



import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import com.lmax.disruptor.EventFactory;
import org.apache.log4j.Logger;

public class Context {
	final static Logger logger = Logger.getLogger(Context.class);
	private Socket client;
	public Context(){}
	public Context(Socket client, String message) {
		this.client = client;
		try {

			this.data = new JsonData(message);

		} catch(Exception e) {
			this.data = new JsonData("{}");
		}
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
		if(logger.isDebugEnabled()){
	    logger.debug(e);
			}
			return false;
		}finally{
			if(!client.isClosed())
			try {
				client.close();
			} catch (IOException e) {
				if(logger.isDebugEnabled()){
					logger.debug(e);
				}
			}
	}


	}
}
