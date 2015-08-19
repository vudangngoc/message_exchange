package com.creative.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

	public static void main(String[] args) {
		String message;           
		try {
			ServerSocket listenSocket = new ServerSocket(10001);
			System.out.println("Start server");
			while(true)          {             
				Socket clientSocket = listenSocket.accept();
				Connection conn = new Connection(clientSocket);
				conn.run();
			} 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally{
			
		}
	}

}
