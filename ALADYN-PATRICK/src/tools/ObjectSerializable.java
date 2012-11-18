package tools;

import java.lang.reflect.Field;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import client.Serializer;
import client.Updater;

import xmlrmi.XMLRMISerializable;

//Classe concrete representant un objet serializable
public class ObjectSerializable implements XMLRMISerializable {
	
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
				}catch (IllegalArgumentException e) {
					System.err.println(e + "error in the argument");
				}catch (IllegalAccessException e) {
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
		return "<object oid=\"" + ObjectsMap.getKey(this) + "\">\n" + res + "</object>";
	}

	@Override
	public void updateFromXML(Element theXML) {
		NodeList fields = theXML.getElementsByTagName("fields");

		Element fields_element = (Element) fields.item(0);
		NodeList fieldNodes = fields_element.getElementsByTagName("field");

		for (int i = 0; i < fieldNodes.getLength(); i++) {

			Element elementField = (Element) fieldNodes.item(i);
			NamedNodeMap attributes= elementField.getAttributes();
			Attr nodeAttr = (Attr)attributes.item(0);

			// Get the new value of the object
			Element  elementValue = (Element)elementField.getElementsByTagName("value").item(0);
			NodeList valueChilds = elementValue.getChildNodes();
			Element valueTypeElement = (Element)valueChilds.item(0);
			//Update the field
			//Remember to set all field accessible
			
			Field f1;
			try 
			{
				f1 = this.getClass().getDeclaredField(nodeAttr.getValue().toString());
				Object paramValue = Updater.getValueFromElementAs(valueTypeElement, f1.getType());
				f1.setAccessible(true);
				Updater.setValue(f1, this, paramValue);

			}
			catch (NoSuchFieldException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (SecurityException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IllegalArgumentException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
