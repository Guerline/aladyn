package server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

//Classe pour appeler la methode
public class MethodCaller {

	/**
	 * rend la reponse XML
	 * @param methodName le nom de la methode
	 * @param arrayParams les parametres de l'objets
	 * @param methodObject la methode de l'objet
	 * @return l'appel de la method sous format XML
	 */
	public String callMethod(String methodName, ArrayList<Object> arrayParams, Object methodObject) {
		BuildResponse br = new BuildResponse();
		
		try {
			Object[] objects = arrayParams.toArray();
			Method method = methodObject.getClass().getDeclaredMethod(methodName, Object.class, double.class);
			Object returnedObject = method.invoke(methodObject, objects);
			
			arrayParams.add(0, returnedObject);
			return br.buildXmlResponse(arrayParams);
		}catch(NoSuchMethodException e) {
			return br.buildXmlFaultResponse(2, "No such method");
		}catch(SecurityException e) {
			System.err.println(e + "error security violation");
		}catch(IllegalAccessException e) {
			System.out.println(e + "error accessing the file");
		}catch(IllegalArgumentException e) {
			return br.buildXmlFaultResponse(2, "Wrong type of arguments given");
		}catch (InvocationTargetException e) {
			return br.buildXmlFaultResponse(3, "Problem  occured in method invocation");
		}
			return br.buildXmlFaultResponse(3, "Problem  occured in method invocation");
	}
}
