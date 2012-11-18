package tools;

import java.lang.reflect.Field;

import org.w3c.dom.Element;

import client.Serializer;

import xmlrmi.XMLRMISerializable;

//Classe concrete representant un objet serializable
public class ObjetSerializable implements XMLRMISerializable {
	
	/**
	 * traduit ubn objet vers un format XML
	 * @return l'XML corespondant à un objet
	 */
	@Override
	public String toXML() {
		String resFields = "", res = "", intR, serializedValue, oid = "";
		Class<?> innerClass = this.getClass();
		Field[] interFields = innerClass.getDeclaredFields();
		
		for(Field field : interFields) {
			field.setAccessible(true);
			intR = "";
			System.out.println(field.getType().toString());
			if(field.getName() == "oid") {
				try {
					oid = (String)field.get(this);
				} catch (IllegalArgumentException e) {
					System.err.println(e + "error in the argument");
				} catch (IllegalAccessException e) {
					System.err.println(e + "error accessing");
				}
			}else {
				try {
					serializedValue = Serializer.serialize(field.get(this));
					intR = "\t<field name=\"" + field.getName() + "\">" + serializedValue + "</field>\n";
				}catch (IllegalAccessException e) {
					System.err.println(e + "error accessing");
				}catch (IllegalArgumentException e) {
					System.err.println(e + "error in the argument");
				}
			}
			resFields += intR;
		}
		resFields = "<fields>\n" + resFields + "</fields>\n";
		res += resFields;
		
		//Block for methods
		
		/*
		 * Needs to add object key and tag
		 */
		return "<object oid=\"" + oid + "\">\n" + res + "</object>";
	}

	@Override
	public void updateFromXML(Element theXML) {
		// TODO Auto-generated method stub
		
	}

}
