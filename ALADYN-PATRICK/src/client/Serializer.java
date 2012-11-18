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

import tools.ObjectsMap;
import xmlrmi.XMLRMISerializable;

//Classe permettant de sérialiser un objet
public class Serializer {
	
	/**
	 * Construit le xml représentant un objet sérialisé
	 * @param obj l'objet à sérialiser
	 * @return le xml représentant l'objet à sérialiser
	 */
	public static String serialize(Object obj) {
		if(obj == null) {
			return "<value>void</value>";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("<value>");
		if(obj instanceof Integer || obj instanceof Short) {
			sb.append("<int>" + (Integer) obj +"</int>");
		}else if(obj instanceof Boolean) {
			sb.append("<boolean>");
			if((Boolean)obj) {
				sb.append("1");
			}else {
				sb.append("0");
			}
			sb.append("</boolean>");
		}else if(obj instanceof Double || obj instanceof Float) {
			sb.append("<double>" + obj + "</double>");
		}else if (obj instanceof Date || obj instanceof Calendar) {
			Date d;
			if(obj instanceof Date) {
				d = (Date)obj;
			}else {
				d = ((Calendar)obj).getTime();
			}
			SimpleDateFormat dateformatter = new SimpleDateFormat("yyyyMMdd'T'HH:MM:ss");
			sb.append("<dateTime.iso8601>");
			sb.append(dateformatter.format(d) );
			sb.append("</dateTime.iso8601>");
		}else if(obj instanceof String || obj instanceof Character) {
			sb.append("<string>" +obj.toString() + "</string>");
		}else if(obj instanceof byte[]) {
			byte[] base64 = (byte[]) obj;
			sb.append("<base64>");
			for(int i = 0; i < base64.length; i++) {
				sb.append(base64[i]);
			}
			sb.append("</base64>");
		}else if(obj.getClass().isArray()) {
			sb.append("<array><data>");
			Object[] arrayObj = (Object[])obj;
			Class<?> commonClass = getCommonSuperClassFromArray(arrayObj);
			
			for(Object o : arrayObj) {
				sb.append(serialize(commonClass.cast(o)));
			}
			sb.append("</data></array>");
		}else if(obj instanceof List) {
			sb.append("<array><data>");
			List<?> listObj = (List<?>)obj;
			Class<?> commonClass = getCommonSuperClassFromArray(listObj.toArray());
			
			for(Object o : listObj) {
				sb.append(serialize(commonClass.cast(o)));
			}
			sb.append("</array></data>");
		}
		else if(obj instanceof Collection) {
			sb.append("<array><data>");
			Collection<?> collectionObj = (Collection<?>)obj;
			Class<?> commonClass = getCommonSuperClassFromArray(collectionObj.toArray());
			Iterator<?> i = collectionObj.iterator();
			
			while(i.hasNext()) {
				sb.append(serialize(commonClass.cast(i.next())));
			}
			sb.append("</data></array>");
		}else if(obj instanceof Map) {
			 sb.append("<struct>");
			 Map<?, ?> mapObj = (Map<?, ?>)obj;
		     Iterator<?> i = mapObj.keySet().iterator();

		     while(i.hasNext()) {
		    	 Object key = i.next();
		    	 sb.append("<member><name>" + key.toString() + "</name>");
		    	 sb.append(serialize(mapObj.get(key)));
		            sb.append("</member>");
		     }
		     sb.append("</struct>");
		}else if(obj instanceof Vector) {
	         Vector<?> vector = (Vector<?>)obj;
	         sb.append("<array><data>");
	         for(Object item : vector) {
	            sb.append(serialize(item));
	         }
	         sb.append("</data></array>");
	    }else {
			System.out.println(obj.getClass().getSuperclass().toString());
			XMLRMISerializable xmlrmiobject = (XMLRMISerializable)obj;
			ObjectsMap.addObject(obj);
			sb.append(xmlrmiobject.toXML());
		}
		sb.append("</value>");
		return sb.toString();
	}
	
	
	
	public static String serializeAs(Object obj, String type) {
		StringBuilder sb = new StringBuilder();
		sb.append("<value>");
		if(type.equals("int")) {
			sb.append("<int>" + (Integer) obj +"</int>");
		}else if(type.equals("boolean")) {
			sb.append("<boolean> ");
			if((Boolean)obj) {
				sb.append("1");
			}else {
				sb.append("0");
			}
			sb.append(" </boolean>");
		}else if(type.equals("double")) {
			sb.append("<double>" + obj + "</double>");
		}else if (type.equals("dateTime.iso8601")) {
			Date d;
			if(obj instanceof Date) {
				d = (Date)obj;
			}else {
				d = ((Calendar)obj).getTime();
			}
			SimpleDateFormat dateformatter = new SimpleDateFormat("yyyyMMdd'T'HH:MM:ss");
			sb.append("<dateTime.iso8601>");
			sb.append(dateformatter.format(d) );
			sb.append("</dateTime.iso8601>");
		}else if(type.equals("string")) {
			sb.append("<string>" +obj.toString() + "</string>");
		}else if(type.equals("double")) {
			byte[] base64 = (byte[]) obj;
			sb.append("<base64>");
			for(int i = 0; i < base64.length; i++) {
				sb.append(base64[i]);
			}
			sb.append("</base64>");
		}else if (type.equals("array")){
			if(obj instanceof Object[]) {
				sb.append("<array><data>");
				Object[] arrayObj = (Object[])obj;
				Class<?> commonClass = getCommonSuperClassFromArray(arrayObj);

				for(Object o : arrayObj) {
					sb.append(serialize(commonClass.cast(o)));
				}
				sb.append("</data></array>");
			}else if(obj instanceof List) {
				sb.append("<array><data>");
				List<?> listObj = (List<?>)obj;
				Class<?> commonClass = getCommonSuperClassFromArray(listObj.toArray());

				for(Object o : listObj) {
					sb.append(serialize(commonClass.cast(o)));
				}
				sb.append("</array></data>");
			}
			else if(obj instanceof Collection) {
				sb.append("<array><data>");
				Collection<?> collectionObj = (Collection<?>)obj;
				Class<?> commonClass = getCommonSuperClassFromArray(collectionObj.toArray());
				Iterator<?> i = collectionObj.iterator();

				while(i.hasNext()) {
					sb.append(serialize(commonClass.cast(i.next())));
				}
				sb.append("</data></array>");
			}else if(obj instanceof Vector) {
				Vector<?> vector = (Vector<?>)obj;
				sb.append("<array><data>");
				for(Object item : vector) {
					sb.append(serialize(item));
				}
				sb.append("</data></array>");
			}
		}  else if ( type.equals("struct")){
			sb.append("<struct>");
			Map<?, ?> mapObj = (Map<?, ?>)obj;
			Iterator<?> i = mapObj.keySet().iterator();

			while(i.hasNext()) {
				Object key = i.next();
				sb.append("<member><name>" + key.toString() + "</name>");
				sb.append(serialize(mapObj.get(key)));
				sb.append("</member>");
			}
			sb.append("</struct>");

		}else if (type.equals("object")){
			if( obj instanceof XMLRMISerializable){
			XMLRMISerializable xmlrmiobject = (XMLRMISerializable)obj;
			ObjectsMap.addObject(obj);
			sb.append(xmlrmiobject.toXML());
			}
		}
		else 
			return null;
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
			
			for(int i = 1; i < objs.length; i++) {
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
	
	
}