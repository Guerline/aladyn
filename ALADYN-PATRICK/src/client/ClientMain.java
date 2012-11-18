package client;

import tests.Point;
import tools.ObjectsMap;

//Main pour lancer le client
public class ClientMain {

	public static void main(String[] args) {
		String callXml ;
		// Creer les parametres objets
		Integer[] m = {new Integer(3)};
		Point p1 = new Point(1, 2,m );
		
		ObjectsMap.addObject(p1);
		Object[] objects = {p1, 1.3};
		//Serialisation des parametres et de la methode a appeler
		BuildCall buildCall = new BuildCall();
		callXml = buildCall.buildMethodCall("display", objects);
		System.out.println(callXml);
		Client c = new Client();
		
		c.start(callXml);
	}
}
