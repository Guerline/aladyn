package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import xmlrmi.XMLParser;

//Classe correspondant au client
public class Client {
	private Socket socket = null;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	/**
	 * methode pour l'execution du client
	 * @param message le message à envoyer au server
	 */
	public void start(String message) {
		try {
			socket = new Socket("localhost", 2004);
			System.out.println(" : " + socket.getInetAddress().getHostName());

			//Envoyer le method call
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			send(message);

			//Recuperer la method response
			message = (String)in.readObject();
			System.out.println("client> " + message);
			ArrayList<Object> paramsList = new ArrayList<Object>();
			paramsList = XMLParser.parseResponse(message);

			for(Object o : paramsList) {
				System.out.println(o.getClass().getName());
			//System.out.println(((Point)paramsList.get(0)).getA());
			}
		}catch(UnknownHostException e) {
			System.err.println(e + "from client : Host not found");
		}catch(IOException e) {
			System.err.println(e + "error with socket");
		}catch(ClassNotFoundException e) {
			System.err.println(e + "class is not found");
		}finally {
			//4: Closing connection
			try {
				in.close();
				out.close();
				socket.close();
			}catch(IOException ioException) {
				System.err.println(ioException + "error with socket");
			}
		}
	}

	/**
	 * Prend un message en entrée et l'envoye au server.
	 * @param message le message à envoyer vers le server 
	 */
	public void send(String message) {
		try {
			out.writeObject(message);
			out.flush();
		}catch(IOException ioException) {
			System.out.println(ioException + "error in/out with file");
		}
	}
}
