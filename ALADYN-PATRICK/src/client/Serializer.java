package client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import tools.ObjectsMap;
import xmlrmi.XMLParser;
import xmlrmi.XMLRMISerializable;

//Classe permettant de sérialiser un objet
public class Serializer {
	
	/**
	 * Construit le xml représentant un objet sérialisé
	 * @param obj l'objet à sérialiser
	 * @return le xml représentant l'objet à sérialiser
	 */
	public static String serialize(Object obj) {
		Class<? extends Object> type = obj.getClass();
		StringBuilder sb = new StringBuilder();
		
		sb.append("<value>");
		if(obj instanceof Integer || obj instanceof Short) {
			sb.append("<int>" + (Integer)obj +"</int>");
		}else if(obj instanceof Boolean) {
			sb.append("<boolean>");
			if((Boolean)obj) {
				sb.append("1");
			}else {
				sb.append("0");
			}
			sb.append(" </boolean>");
		}else if(obj instanceof Double || obj instanceof Float) {
			sb.append("<double>" + obj + "</double>");
		}else if(obj instanceof Date || obj instanceof Calendar) {
			Date d;
			if(obj instanceof Date) {
				d = (Date)obj;
			}else {
				d = ((Calendar)obj).getTime();
			}
			SimpleDateFormat dateformatter = new SimpleDateFormat("yyyyMMdd'T'HH:MM:ss");
			sb.append("<dateTime.iso8601>");
			sb.append(dateformatter.format(d));
			sb.append("</dateTime.iso8601>");
		}else if(obj instanceof String || obj instanceof Character) {
			sb.append("<string>" + obj.toString() + "</string>");
		}else if(type == byte[].class) {
			byte[] base64 = (byte[])obj;
			
			sb.append("<base64>");
			for(int i = 0; i < base64.length; i++) {
				sb.append(base64[i]);
			}
			sb.append("</base64>");
		}else if(obj instanceof Object[]) {
			Object[] arrayObj = (Object[])obj;
			Class<?> commonClass = getCommonSuperClassFromArray(arrayObj);
			
			sb.append("<array><data>");
			for(Object o : arrayObj) {
				sb.append(serialize(commonClass.cast(o)));
			}
			sb.append("</data></array>");
		}else if(obj instanceof List) {
			List<?> listObj = (List<?>)obj;
			Class<?> commonClass = getCommonSuperClassFromArray(listObj.toArray());
			
			sb.append("<array><data>");
			for(Object o : listObj) {
				sb.append(serialize(commonClass.cast(o)));
			}
			sb.append("</array></data>");
		}else if (obj instanceof Collection) {
			Collection<?> collectionObj = (Collection<?>)obj;
			Class<?> commonClass = getCommonSuperClassFromArray(collectionObj.toArray());
			Iterator<?> i = collectionObj.iterator();
			
			sb.append("<array><data>");
			while(i.hasNext()) {
				sb.append(serialize(commonClass.cast(i.next())));
			}
			sb.append("</data></array>");
		}else if(obj instanceof Map) {
			Map<?, ?> mapObj = (Map<?, ?>)obj;
			Iterator<?> i = mapObj.keySet().iterator();
		        
			sb.append( "<struct>" );
		    while (i.hasNext()) {
		    	Object key = i.next();
		    	sb.append("<member><name>" + key.toString() + "</name>");
		    	sb.append(serialize(mapObj.get(key)));
		        sb.append("</member>" );
		    }
		    sb.append("</struct>");
		}else if(obj instanceof Vector) {
	         Vector<?> vector = (Vector<?>) obj;
	         
	         sb.append("<array><data>");
	         for(Object item : vector) {
	            sb.append(serialize(item));
	         }
	         sb.append("</data></array>");
	    }else {
			System.out.println(obj.getClass().getInterfaces().toString());
			XMLRMISerializable xmlrmiobject = (XMLRMISerializable)obj;
			sb.append(xmlrmiobject.toXML());
		}
		sb.append("</value>");
		return sb.toString();
	}
	
	/**
	 * recupère la superclass commune d'un tableau
	 * @param objs le tableau des objets
	 * @return la superclass commune
	 */
	public static Class<?> getCommonSuperClassFromArray(Object[] objs) {
		Class<?> myClass, myCommonSuperClass = myClass = objs[0].getClass();
		ArrayList<Class<?>> classesArray = new ArrayList<Class<?>>();
		int n = objs.length;
		
		if(n != 0) {
			myClass = objs[0].getClass();
			while(!myClass.equals(Object.class)) {
				classesArray.add(myClass);
				myClass = myClass.getSuperclass();
			}
			classesArray.add(myClass);
			
			for(int i = 1; i < objs.length; i++ ) {
				myClass = objs[i].getClass();
				while(!classesArray.contains(myClass)) {
					myClass = myClass.getSuperclass();
				}
				myCommonSuperClass = myClass;
				System.out.println(myCommonSuperClass.toString());
				Iterator<?> it = classesArray.iterator();
				while(it.hasNext() && !it.next().equals(myCommonSuperClass)) {
					it.remove();
				}
			}
			return myCommonSuperClass;
		}else {
			return null;
		}
	}
	
	/**
	 * rend l'objet correspondant à un noeud XML
	 * @param elem le noeud représentant un objet
	 * @return l'objet représentant le noeud d'un élément
	 */
	public static Object getValueFromElement(Element elem) {
		String type = elem.getTagName();
		if(type == "integer") {
			return Integer.getInteger(XMLParser.getCharacterDataFromElement(elem));
		}else if(type == "boolean") {
			return Boolean.parseBoolean(XMLParser.getCharacterDataFromElement(elem));
		}else if(type == "double") {
			return Double.parseDouble(XMLParser.getCharacterDataFromElement(elem));
		}else if(type == "datetime") {
			return null;
		}else if (type =="string") {
			return XMLParser.getCharacterDataFromElement(elem);
		}else if (type ==  "base64") {
			return null;
		}else if(type == "object") {
			//Point p1 = new Point(4.4,5.4,'c',null);
			NamedNodeMap attributes= elem.getAttributes();
			Attr nodeAttr = (Attr)attributes.item(0);
			String elementKey = nodeAttr.getValue();
			//Get the right object from the map
			Object objectFound = ObjectsMap.getObject(elementKey);
			
			if( objectFound == null) {
				//????????
			}
			System.out.println(objectFound.toString());
			XMLRMISerializable objectFoundRmi = (XMLRMISerializable) objectFound;
			objectFoundRmi.updateFromXML(elem);
			return objectFoundRmi;
		}else if(type == "array"){
			NodeList objs = elem.getElementsByTagName("value");
			int n = objs.getLength();
			ArrayList<Object> objsArray = new ArrayList<Object>();
			ArrayList<Object> castedObjsArray = new ArrayList<Object>();
			
			for(int i = 0; i < n ; i++) {
				//A Adapter lors de la rencontre d'un objet
				objsArray.add(getValueFromElement((Element)objs.item(i)));
			}
			Class<?> classes = getCommonSuperClassFromArray(objsArray.toArray());
			
			for(int i = 0; i < n; i++) {
				//A Adapter lors de la rencontre d'un objet
				castedObjsArray.add(classes.cast(objsArray.get(i)));
			}
			return castedObjsArray.toArray();
		}else {
			return null;
		}
	}
}