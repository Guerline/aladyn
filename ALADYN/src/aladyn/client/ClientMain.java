package aladyn.client;

import aladyn.ObjectsMap;
import aladyn.test_classes.Point;

public class ClientMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			String callXml ;
		
			// Creer les parametres objets
			String[] arrayString = {"coco"};
			
			Point p1=new Point(1, 2, new java.util.GregorianCalendar());
			ObjectsMap.addObject("100",p1);
			Object[] objects = {p1, 1.3};
			//Serialisation des parametres et de la methode a appeler
			BuildCall buildCall= new BuildCall();
			callXml = buildCall.buildMethodCall("display", objects);
			
			
			Client c = new Client();
			c.start(callXml);
			  
		}
	}

