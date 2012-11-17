package aladyn.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import aladyn.ObjectSerializable;
import aladyn.test_classes.*;
import aladyn.parser.XMLParser;

public class Server {
	private ServerSocket socket = null;
	private Socket connection = null;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String methodCall;


	public  String receive() {
		ArrayList<Object> arrayParams = new ArrayList<Object>(); 
		String methodName;
		
		try {
			socket = new ServerSocket(2004, 10);
			System.out.println("Je suis le serveur. J'attends qu'un client se conecte");
			connection = socket.accept();
			System.out.println("Quelqu'un s'est connecté : " + connection.getInetAddress().getHostName());
			
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			try{
				methodCall = (String)in.readObject();
				//System.out.println("Voici ce que j'ai reçu : " + methodCall);

				methodName = XMLParser.parseCall(methodCall, arrayParams);
				System.out.println("hey hey hey!");
				for( Object obj :arrayParams ) {
					System.out.println("server> " + obj.toString());
				}

				send( callMethod(methodName, arrayParams));
				
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
		return methodCall;
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

	
	public String callMethod(String methodName, ArrayList<Object> arrayParams){
		BuildResponse br = new BuildResponse();
		try {
			Object[] objects = arrayParams.toArray();
			Method method = this.getClass().getDeclaredMethod(methodName, Object.class, double.class);
			Object returnedObject = method.invoke(this, objects);
			//ObjectSerializable objSer = (ObjectSerializable)arrayParams.get(0);
			//System.out.println(objSer.toXML());
			// Send back the response with the object returned
			if(method.getReturnType() != Void.TYPE){
				arrayParams.add(0, returnedObject);
			}
			
			return br.buildXmlResponse(arrayParams);
		} catch (NoSuchMethodException e) 
		{
			return br.buildXmlFaultResponse(2, "No such method");
		}
		catch (SecurityException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IllegalArgumentException e) 
		{
			return br.buildXmlFaultResponse(2, "Wrong type of arguments given");
		} 
		catch (InvocationTargetException e)
		{
			return br.buildXmlFaultResponse(3, "Problem  occured in method invocation");
		}
		return br.buildXmlFaultResponse(3, "Problem  occured in method invocation");
	}

}
