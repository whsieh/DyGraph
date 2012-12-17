package example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerExample {

	public static void main(String[] args) {
		
		ServerSocket serverSocket = null;
		
		try {
		    serverSocket = new ServerSocket(8080);
		    
		    Socket clientSocket = null;
		    try {
		        clientSocket = serverSocket.accept();
		        
		        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		        BufferedReader in = 
		            new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		        String inputLine;
		        out.println("Connection established!");

		        while ((inputLine = in.readLine()) != null) {   
		            out.println("Echoing: " + inputLine);
		            if (inputLine.equals("exit")) {
		            	out.println("Exiting...");
		            	break;
		            }
		        }
		        
		        out.close();
		        in.close();
		        clientSocket.close();
		        serverSocket.close();
		        
		    } 
		    catch (IOException e) {
		        System.out.println("Accept failed: 8080");
		        System.exit(-1);
		    }
		    
		} 
		catch (IOException e) {
		    System.out.println("Could not listen on port: 8080");
		    System.exit(1);
		}
		
		
	}
	
	
	
	
}
