package client;

import tools.ObjectsMap;

//Main pour lancer le client
public class ClientMain {

	public static void main(String[] args) {
		String callXml ;
		// Creer les parametres objets
		String[] arrayString = {"coco"};
		Point p1 = new Point(1, 2, "cococ");
		ObjectsMap.addObject("100", p1);
		Object[] objects = {p1, 1.3};
		//Serialisation des parametres et de la methode a appeler
		BuildCall buildCall = new BuildCall();
		callXml = buildCall.buildMethodCall("display", objects);
		System.out.println(callXml);
		Client c = new Client();
		
		c.start(callXml);
		
		/*callXml = "<methodCall><methodName>haha</methodName><params>" +
					"<param><value>"+
					"<object oid=\"100\"><fields>" +
					"<field name=\"a\"><value><double>1.3</double></value></field>"+
					"<field name=\"b\"><value><double>2.4</double></value></field>"+
					"</fields>"+
					"<methods>"+
					"<method language=\"java\">public String toString() { return String.valueOf(a+b); }</method>"  +
					"</methods>"+
					"</object>" + "</value></param></params></methodCall>";*/

		

		/* ClassPool pool = ClassPool.getDefault();

			CtClass evalClass = pool.makeClass("Eval");

			 try {
				evalClass.addMethod( CtNewMethod.make("public String toString() { return (\"hhey\") ; }",
				             evalClass));
				Class clazz = evalClass.toClass();

				try {
					Object obj = clazz.newInstance();
					System.out.println(obj.toString());

				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (CannotCompileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 */

	}
}
