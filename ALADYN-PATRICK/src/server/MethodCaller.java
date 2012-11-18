package server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MethodCaller
{

	
	public String callMethod(String methodName, ArrayList<Object> arrayParams, Object methodObject){
		BuildResponse br = new BuildResponse();
		try {
			Object[] objects = arrayParams.toArray();
			Method method = methodObject.getClass().getDeclaredMethod(methodName, Object.class, double.class);
			Object returnedObject = method.invoke(methodObject, objects);
			//ObjectSerializable objSer = (ObjectSerializable)arrayParams.get(0);
			//System.out.println(objSer.toXML());
			// Send back the response with the object returned
			arrayParams.add(0, returnedObject);
			
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
