package client;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tools.ObjectSerializable;
import tools.ObjectsMap;
import xmlrmi.XMLRMISerializable;

//Classe permettant de setter la valeur d'un champ
public class Updater {
	
	/**
	 * place la valeur correspondant à un champ
	 * @param f le champ à setter
	 * @param obj l'objet
	 * @param paramValue la valeur du champ
	 */
	public static void setValue(Field f, Object obj, Object paramValue){
		Class<?> type = f.getType();

		if(type.equals(Integer.class) || type.equals(int.class)) {
			try {
				f.setInt(obj, (Integer)paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if(type.equals(Short.class) || type.equals(short.class)) {
			try {
				f.setShort(obj, (Short)paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if(type.equals(Boolean.class) || type.equals(boolean.class)) {
			try {
				f.setBoolean(obj, (Boolean)paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if (type.equals(Double.class) || type.equals(double.class)) {
			try {
				f.setDouble(obj, (Double)paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if (type.equals(Float.class) || type.equals(float.class)) {
			try {
				f.setFloat(obj, (Float)paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if (type.equals(Date.class) || type.equals(Calendar.class)) {
			//TODO
		}else if (type.equals(Character.class) || type.equals(char.class)) {
			try {
				f.setChar(obj, String.valueOf(paramValue).toCharArray()[0]);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if (type.equals(Byte.class) || type.equals(byte.class)) {
			try {
				f.setByte(obj,(Byte) paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if (type.equals(Long.class) || type.equals(long.class)) {
			try {
				f.setLong(obj, (Long) paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else if (type.equals(String.class)) {
			try {
				f.set(obj,paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}else {
			try {
				f.set(obj, paramValue);
			}catch (IllegalArgumentException e) {
				System.err.println(e + "error setting the field with the argument");
			}catch (IllegalAccessException e) {
				System.err.println(e + "error accessing the field");
			}
		}
	}
	
	/**
	 * rend l'objet correspondant à un noeud XML
	 * @param elem le noeud représentant un objet
	 * @return l'objet représentant le noeud d'un élément
	 */
	public static Object getValueFromElement(Node elem) {
			String type = elem.getNodeName();
			
			if(type == "int") {
				return Integer.getInteger(elem.getTextContent());
			}else if(type == "boolean") {
				return Boolean.parseBoolean(elem.getTextContent());
			}else if(type == "double") {
				return Double.parseDouble(elem.getTextContent());
			}else if(type == "dateTime.iso8601") {
				return null;
			}else if (type =="string") {
				return elem.getTextContent();
			}else if (type ==  "base64") {
				return null;
			}else if(type == "object") {
				NamedNodeMap attributes= elem.getAttributes();
				Attr nodeAttr = (Attr)attributes.item(0);
				String elementKey = nodeAttr.getValue();
				
				//Get the right object from the map
				Object objectFound = ObjectsMap.getObject(elementKey);
				if(objectFound == null) {
					return null;
				}
				System.out.println(objectFound.toString());
				ObjectSerializable objectFoundRmi = (ObjectSerializable)  objectFound;
				objectFoundRmi.updateFromXML((Element)elem);
				return objectFoundRmi;
			}else if (type == "array") {
				NodeList objs = ((Element)elem).getElementsByTagName("value");
				int n = objs.getLength();
				ArrayList<Object> objsArray = new ArrayList<Object>();
				ArrayList<Object> castedObjsArray = new ArrayList<Object>();
				
				for(int i = 0; i < n; i++) {
					//A Adapter lors de la rencontre d'un objet
					objsArray.add(getValueFromElement(objs.item(i)));
				}
				
				Class<?> classes = objsArray.get(0).getClass();
				for(int i = 0; i < n; i++) {
					//A Adapter lors de la rencontre d'un objet
					castedObjsArray.add(classes.cast(objsArray.get(i)));
				}
				return castedObjsArray.toArray();
			}else {
				return null;
			}
	}
	
	/**
	 * rend l'objet correspondant à un noeud XML
	 * @param elem le noeud représentant un objet
	 * @return l'objet représentant le noeud d'un élément
	 */
	public static Object getValueFromElementAs(Node elem, Class<?> type) {
			
			if(type.equals(Integer.class) || type.equals(int.class)) {
				return Integer.valueOf(elem.getTextContent());
			}else if(type.equals(Boolean.class) || type.equals(boolean.class)) {
				return Boolean.valueOf(elem.getTextContent());
			}else if(type.equals(Double.class) || type.equals(double.class)) {
				return Double.valueOf(elem.getTextContent());
			}else if(type.equals(Date.class)){
				SimpleDateFormat dateformatter = new SimpleDateFormat("yyyyMMdd'T'HH:MM:ss");
				try {
					Date d =  dateformatter.parse(elem.getTextContent());
				} catch (DOMException e) {
					System.err.println(e + "error Dom not well formed");
				} catch (ParseException e) {
					System.err.println(e + "error cannot create Date object from element");
				}
			}else if(type.equals(Calendar.class)) {
				SimpleDateFormat dateformatter = new SimpleDateFormat("yyyyMMdd'T'HH:MM:ss");
				Calendar c = Calendar.getInstance();
				try {
					  c.setTime(dateformatter.parse(elem.getTextContent()));
					  return c;
				} catch (DOMException e) {
					System.err.println(e + "error Dom not well formed");
				} catch (ParseException e) {
					System.err.println(e + "error cannot create Date object from element");
				}
			}else if (type.equals(Character.class)){
				return elem.getTextContent().charAt(0);
			}else if (type.equals(String.class)) {
				return elem.getTextContent();
			}else if (type.equals(Byte[].class) || type.equals(byte[].class)) {
				//TODO
			}else if(type.equals(ObjectSerializable.class)) {
				NamedNodeMap attributes= elem.getAttributes();
				Attr nodeAttr = (Attr)attributes.item(0);
				String elementKey = nodeAttr.getValue();
				Object objectFound = ObjectsMap.getObject(elementKey);
				ObjectSerializable objectFoundRmi = (ObjectSerializable)  objectFound;
				objectFoundRmi.updateFromXML((Element)elem);
				return objectFoundRmi;
			}else if (type.equals(Object[].class)) {
				NodeList objs = ((Element)elem).getElementsByTagName("value");
				int n = objs.getLength();
				ArrayList<Object> objsArray = new ArrayList<Object>();
				
				for(int i = 0; i < n; i++) {
					objsArray.add(getValueFromElement(objs.item(i)));
				}
				return objsArray.toArray();
			}else {
				return null;
			}
			return null;
	}
}