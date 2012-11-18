package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import xmlrmi.XMLParser;

//Classe correspondant au server
public class Server {
	private ServerSocket socket = null;
	private Socket connection = null;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String methodCall;

	/**
	 * methode pour l'execution du client
	 * @param message le message à envoyer au server
	 */
	public String receive() {
		ArrayList<Object> arrayParams = new ArrayList<Object>(); 
		String methodName;
		
		try {
			socket = new ServerSocket(2004, 10);
			System.out.println("Je suis le serveur. J'attends qu'un client se conecte");
			connection = socket.accept();
			System.out.println("Quelqu'un s'est connecté : " + connection.getInetAddress().getHostName());
			//Recuperer ce qui est envoyé et recu
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			try {
				methodCall = (String)in.readObject();
				//System.out.println("Voici ce que j'ai reçu : " + methodCall);
				methodName = XMLParser.parseCall(methodCall, arrayParams);
				System.out.println("hey hey hey!");
				for(Object obj :arrayParams) {
					System.out.println("server> " + obj.toString());
				}

				Object[] objects = arrayParams.toArray();
				try {
					Method method = this.getClass().getDeclaredMethod(methodName, Object.class, double.class);
					method.invoke(this, objects);
					//ObjetSerializable objSer = (ObjetSerializable)arrayParams.get(0);
					//System.out.println(objSer.toXML());
					// Send back the response with the object returned
					if(method.getReturnType() != Void.TYPE) {
						arrayParams.add(0, method.invoke(this, objects));
					}
					send(BuildResponse.buildXmlResponse(arrayParams));
					send("hey");
				}catch(NoSuchMethodException e) {
					System.err.println(e + "error finding the method");
				}catch(SecurityException e) {
					System.err.println(e + "error with security violation");
				}catch(IllegalAccessException e) {
					System.err.println(e + "error accessing");
				}catch(IllegalArgumentException e) {
					System.err.println(e + "error in the argument");
				}catch(InvocationTargetException e) {
					System.err.println(e + "error in the invocation of a method or constructor");
				}
			}catch(ClassNotFoundException classnot) {
				System.err.println(classnot + "error finding the classe");
			}
		}catch (IOException e) {
			System.err.println(e + "error with socket");
		}finally {
			//Fermer la connection
			try {
				in.close();
				out.close();
				socket.close();
			}catch(IOException ioException) {
				System.err.println(ioException + "error with socket");
			}
		}	
		return methodCall;
	}

	/**
	 * Prend un message en entrée et l'envoye au client.
	 * @param message le message à envoyer vers le client 
	 */
	public void send(String message) {
		try {
			out.writeObject(message);
			out.flush();
		}catch(IOException ioException) {
			System.out.println(ioException + "error in/out with file");
		}
	}

	public void display(Object o, double d) {
		System.out.println(o.toString());
	}
}
