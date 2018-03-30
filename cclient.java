package chatclient;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class cclient extends Application{

	
	static DatagramSocket dsocket;
	public static final int dPORT = 2000;
	//ArrayList<Label> messages = new ArrayList<Label>();
	static	InetAddress host;
	public VBox messgBox; 
	ArrayList<String> messages = new ArrayList<String>();
	AnimationTimer timer;
	public int counter=0;
	
	
	public class listen extends Thread{
		DatagramPacket command = new DatagramPacket (new byte[1024], 1024);
		

	    public void run() {
	    	try {
	    		while(true)
	    		{
					dsocket.receive(command);
					String newCommand = new String(command.getData(),0,command.getLength(),"UTF-8");
					String requestRead[] = newCommand.split(":");
					String text = requestRead[0];
					int id = Integer.parseInt(requestRead[1]);
					System.out.printf("There is a string in text: %s \n",text);
					messages.add(text);
					
//					System.out.println(messages.get(0));
	    			
	    		}


				
			} catch (IOException e) {
				// TODO: handle exception
			}
	    	
	        
	    }


	}
	
	
	public void setUP()
	{
		try
		{
			dsocket = new DatagramSocket(0);
			host = InetAddress.getByName("130.240.202.237");
			String init = "INVITE";
			byte[] initbyte = init.getBytes("UTF-8");
			DatagramPacket initpacket = new DatagramPacket(initbyte, initbyte.length,host,dPORT);
			dsocket.send(initpacket);
			System.out.println("INIT UDP PACKET");
			timer.start();
			
		}catch(IOException xxe) {
			System.out.println("INIT ISSUE");
		}
	}


	
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{


		
		primaryStage.setTitle("PublicTest");
		//PLAY SCENE
		Pane root = new Pane();
		messgBox = new VBox(5);
		

		// all i need
		Scene lobby = new Scene(root, 300,450);	

		Label bubble = new Label();
		bubble.setText("test");
		messgBox.getChildren().add(bubble);
		
		TextField messg = new TextField();
		messg.setLayoutX(30);
		messg.setLayoutY(410);
		messg.setPromptText("Enter message here");
		
		Button send = new Button();
		send.setPrefWidth(70);
		send.setLayoutX(200);
		send.setLayoutY(410);
		send.setText("Send");
		
		root.getChildren().addAll(messgBox,messg,send);


		primaryStage.setScene(lobby);
		primaryStage.show();

	

		
		
		
		messg.setOnKeyReleased(e->{
			if(e.getCode()== KeyCode.ENTER)
			{
				try
				{
					String text = messg.getText();
					byte[] initbyte = text.getBytes("UTF-8");
					DatagramPacket initpacket = new DatagramPacket(initbyte, initbyte.length,host,dPORT);
					dsocket.send(initpacket);
					System.out.println("Message sent");
				}catch(IOException xxe) {
					System.out.println("Message issue");
				}
				messg.clear();
				

				
			}
		});
		
		send.setOnAction(e->
		{
			try
			{
				String text = messg.getText();
				byte[] initbyte = text.getBytes("UTF-8");
				DatagramPacket initpacket = new DatagramPacket(initbyte, initbyte.length,host,dPORT);
				dsocket.send(initpacket);
				System.out.println("Message sent");
			}catch(IOException xxe) {
				System.out.println("Message issue");
			}
			messg.clear();
			
		});
		primaryStage.setOnCloseRequest(e->{
			

			
		});
		
		
		timer = new AnimationTimer() {
			
			

			@Override
			public void handle(long now) {
				
				if(messages.size()>counter) {
					Label testingmess = new Label();
					testingmess.setText(messages.get(counter));
					messgBox.getChildren().add(testingmess);
					System.out.println("I did something");
					counter++;
				}
				
			}
			
		};
		setUP();
		Thread task = new listen();
		task.start();
		

	}
		
	public static void main(String[] args) 
	{
		launch(args);

	}

}
