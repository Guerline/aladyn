package aladyn.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import aladyn.ObjectsMap;
import aladyn.XMLRMISerializable;
import aladyn.parser.XMLParser;

public class Serializer {

	public static String serialize (Object obj){
		Class<? extends Object> type = obj.getClass();

		if ( obj instanceof Integer ||
				obj instanceof Short  )
		{
			return("<value> <int>" + (Integer) obj +"</int> </value>");
		}
		else if (obj instanceof Boolean) 
		{
			return("<value> <boolean> " + (Boolean)obj + " </boolean> </value>");
		}
		else if (obj instanceof Double || obj instanceof Float) 
		{
			return("<value><double>" + obj + "</double></value>");
		}
		else if (obj instanceof Date || obj instanceof Calendar)
		{
			Date d= (Date) obj;
			return "<value><dateTime.iso8601>" + d.getYear() + 
					"-" + d.getMonth() + "-" + d.getDate() +
					"</dateTime.iso8601></value>";
		}
		else if(obj instanceof String || obj instanceof Character)
		{
			return("<value><string>" +obj.toString() + "</string></value>");
		}
		else if (type == byte[].class) 
		{
			//Construire byte
			byte[] base64 = (byte[]) obj;
			StringBuffer xml = new StringBuffer("<value><base64>");
			for(int i = 0; i < base64.length; i++) {
				xml.append(base64[i]);
			}
			xml.append("</base64></value>");
			return(xml.toString());
		}
		else if (obj instanceof Object[]){
			StringBuilder sb = new StringBuilder();
			sb.append("<array><data>");
			Object[] arrayObj = (Object[])obj;
			Class<?> commonClass = getCommonSuperClassFromArray(arrayObj);
			for( Object o : arrayObj)
			{
				sb.append(serialize(commonClass.cast(o)));
			}
			sb.append("</data></array>");
			return sb.toString();
		}
		else if (obj instanceof List){
			StringBuilder sb = new StringBuilder();
			sb.append("<array><data>");
			List<?> listObj = (List<?>)obj;
			Class<?> commonClass = getCommonSuperClassFromArray(listObj.toArray());
			for( Object o : listObj)
			{
				sb.append(serialize(commonClass.cast(o)));
			}
			sb.append("</array></data>");
			return sb.toString();
		}
		else if (obj instanceof Collection){
			StringBuilder sb = new StringBuilder();
			sb.append("<array><data>");
			Collection<?> collectionObj = (Collection<?>)obj;
			Class<?> commonClass = getCommonSuperClassFromArray(collectionObj.toArray());
			Iterator<?> i = collectionObj.iterator();
			while(i.hasNext())
			{
				sb.append(serialize(commonClass.cast(i.next())));
			}
			sb.append("</array></data>");
			return sb.toString();
		}
		else if (obj instanceof Map) {
			StringBuilder sb = new StringBuilder();
			 sb.append( "<struct>" );

		        Map<?, ?> mapObj     = ( Map<?,?> ) obj;
		        Iterator<?> i = mapObj.keySet().iterator();

		        while ( i.hasNext() )
		        {
		        	Object key = i.next();
		            sb.append( "<member><name>" + key.toString() + "</name>" );
		            sb.append(serialize( mapObj.get( key )));
		            sb.append( "</member>" );
		        }
		        sb.append( "</struct>" );
			return sb.toString();
		}
		else
		{
			System.out.println(obj.getClass().getInterfaces().toString());
			XMLRMISerializable xmlrmiobject = (XMLRMISerializable) obj;
			return xmlrmiobject.toXML();
		}
	}
	
	public static Class<?> getCommonSuperClassFromArray(Object[] objs)
	{
		Class<?> myClass, myCommonSuperClass = myClass = objs[0].getClass();
		ArrayList<Class<?>> classesArray = new ArrayList<Class<?>>();
		int n = objs.length;
		if(n != 0){
			myClass = objs[0].getClass();
			while(myClass.getClass().equals(Object.class))
			{
				classesArray.add(myClass);
				myClass = myClass.getSuperclass();
			}
			for( int i=1; i < objs.length;i++ )
			{
				myClass = objs[i].getClass();
				while (!classesArray.contains(myClass)){
					myClass = myClass.getSuperclass();
				}
				myCommonSuperClass = myClass;
			}
			return myCommonSuperClass;
		}
		else
		{
			return null;
		}
	}
	
	public static Object getValueFromElement( Element elem) {
		String type=elem.getTagName();
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
		}
		else if (type == "array"){
			NodeList objs = elem.getElementsByTagName("value");
			int n = objs.getLength();
			ArrayList<Object> objsArray = new ArrayList<Object>();
			ArrayList<Object> castedObjsArray = new ArrayList<Object>();
			for(int i = 0; i < n ; i++)
			{
				//A Adapter lors de la rencontre d'un objet
				objsArray.add(getValueFromElement((Element)objs.item(i)));
			}
			Class<?> classes = getCommonSuperClassFromArray(objsArray.toArray());
			for(int i = 0; i <n; i++)
			{
				//A Adapter lors de la rencontre d'un objet
				castedObjsArray.add(classes.cast(objsArray.get(i)));
			}
			return castedObjsArray.toArray();
		}
		else
		{
			return null;
		}
		
	}
}