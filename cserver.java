package chatserver;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;






public class cserver {

	static ArrayList<Socket> clientsock = new ArrayList<Socket>();
	public final static int dPORT = 2000;
	static DatagramSocket dsocket;
	private final static Logger errors = Logger.getLogger("errors");
	static ArrayList<Integer> AllPorts = new ArrayList<Integer>();
	static ArrayList<InetAddress> clientIP= new ArrayList<InetAddress>();
	
	
	
	private class broadcast extends Thread{
		
	    public void run() {
	    	
	    	
	        
	    }
		
	}
	
	
	public static void main(String[] args)
	
	{
		int id=0;
		//Set up udp PORT
		try
		{
			dsocket = new DatagramSocket(dPORT);
			System.out.println("UDP SERVERSocket open");
			DatagramPacket clientPacket = new DatagramPacket(new byte[1024],1024);
			while(true) {
				dsocket.receive(clientPacket);
				String clientRequest = new String(clientPacket.getData(),0,clientPacket.getLength(),"UTF-8");
				
				if(clientRequest.contains("INVITE")) {
					AllPorts.add(clientPacket.getPort());
					clientIP.add(clientPacket.getAddress());
					System.out.println(AllPorts);
					System.out.println(clientIP);
				}else {
					
					for(int i=0;i<AllPorts.size();i++)
					{
						String iD = Integer.toString(id);
						//tell everyone Online about the new message
						String message = clientRequest+":"+iD;
						byte[] data = message.getBytes("UTF-8");
						System.out.println("Looping through connections");
						DatagramPacket response = new DatagramPacket(data,data.length,clientIP.get(i),AllPorts.get(i));
						System.out.println("Client:"+AllPorts.get(i)+" notified.");
						dsocket.send(response);
						System.out.println("Response sent");
					}
					id++;
				}
					
				
			}
		} catch (IOException ex) {
			System.out.println("Error at UDP socket");
			errors.log(Level.SEVERE, ex.getMessage(), ex);
			ex.printStackTrace();
		}
		

	}

}
