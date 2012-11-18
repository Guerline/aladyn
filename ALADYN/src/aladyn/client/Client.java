package aladyn.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import aladyn.parser.XMLParser;

public class Client {
	private static Socket socket = null;
	private ObjectOutputStream out;
	private ObjectInputStream in;


	public  void start(String message) {
		try
		{
			socket = new Socket("localhost", 2004);
			System.out.println("Server started! Waiting for a connection...");
			System.out.println(" : " + socket.getInetAddress().getHostName());
			
			//Envoyer le method call
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(socket.getInputStream());
			send(message);
			
			//Recuperer la method response
			message = (String)in.readObject();
			//System.out.println("client> " + message);
			ArrayList<Object> paramsList = new ArrayList<Object>();
			System.out.println(message);
			paramsList = XMLParser.parseResponse(message);
			System.out.println(message);
			for( Object o : paramsList)
				System.out.println(o.getClass().getName());
			//System.out.println(((Point)paramsList.get(0)).getA());
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			//4: Closing connection
			try
			{
				in.close();
				out.close();
				socket.close();
			}
			catch(IOException ioException)
			{
				ioException.printStackTrace();
			}
		}
	}

	public void send(String message)
	{
		try
		{
			out.writeObject(message);
			out.flush();
		}
		catch(IOException ioException)
		{
			ioException.printStackTrace();
		}
	}
}
