package aladyn.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Service {
	private final Socket s;
	private String text = "";

	private BufferedReader plec;
	private PrintWriter pred;

	public Service(Socket socket) {
		s = socket;
	}

	public String receive() {
		try {
			plec = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pred = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
			text = plec.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return(text);
	}

	public void send(String text) {
		try {
			pred.println(text);
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
	}
}
