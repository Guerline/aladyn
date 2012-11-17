package aladyn;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import org.w3c.dom.Element;
import aladyn.client.Serializer;

public class ObjectSerializable implements XMLRMISerializable{

	@Override
	public String toXML() {
		String res_fields="",res="",intR, serializedValue, oid= "";
		Annotation[] annotations;
		Class<?> innerClass = this.getClass();
		
		Field[] inter_fields= innerClass.getDeclaredFields();
		
		for(Field field : inter_fields)
		{
			field.setAccessible(true);
			annotations = field.getDeclaredAnnotations();
			intR="";
			System.out.println(field.getType().toString());
			if( field.getName() == "oid"){
				try {
					oid = (String)field.get(this);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else 
			{
				try {
					serializedValue = Serializer.serialize(field.get(this));
					intR="\t<field name=\""+ field.getName() +"\">" + serializedValue + "</field>\n";
				}
				catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			res_fields+= intR;
		}
		res_fields = "<fields>\n" + res_fields + "</fields>\n";
		res += res_fields;
		
		//Block for methods
		
		/*
		 * Needs to add object key and tag
		 */
		return "<value>\n<object oid=\"" + oid+ "\">\n" + res + "</object>\n</value>\n";
		
	}

	@Override
	public void updateFromXML(Element theXML) {
		// TODO Auto-generated method stub
		
	}

}
