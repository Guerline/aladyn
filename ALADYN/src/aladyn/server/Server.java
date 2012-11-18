package aladyn.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import aladyn.parser.XMLParser;

public class Server {
	private ServerSocket socket = null;
	private Socket connection = null;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String methodCall;


	public void receive() {
		ArrayList<Object> arrayParams = new ArrayList<Object>(); 
		String methodName;
		MethodCaller methodCaller = new MethodCaller();
		
		try
		{
			socket = new ServerSocket(2004, 10);
			System.out.println("Je suis le serveur. J'attends qu'un client se conecte");
			connection = socket.accept();
			System.out.println("Quelqu'un s'est connecté : " + connection.getInetAddress().getHostName());
			
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			try
			{
				methodCall = (String)in.readObject();
				methodName = XMLParser.parseCall(methodCall, arrayParams);
				
				for( Object obj :arrayParams ) {
					System.out.println("server> " + obj.toString());
				}
				String response = methodCaller.callMethod(methodName, arrayParams, this);
				send(response);
				
			}
			catch(ClassNotFoundException classnot){
				System.err.println("Je ne reconnait pas le format de ce que l'on m'a envoyé! DSL!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			//Fermer la connection
			try{
				in.close();
				out.close();
				socket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}	
	}

	public void send(String message){
		try{
			out.writeObject(message);
			out.flush();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

	public void display(Object o, double d){
		System.out.println(o.toString() + "heykkkkkkkkkkkkkkk");
	}


}
