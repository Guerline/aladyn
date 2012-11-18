package aladyn.parser;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.CharacterData;
import aladyn.client.Serializer;
import aladyn.server.BuildObject;

public class XMLParser {

	/*public static void main(String arg[]){
		String xmlRecords = "<methodCall><methodName>haha</methodName><params><param><value><double>1.2</double></value></param>" +
				"<param><value>"+
				"<object oid=\"100\"><fields>" +
				"<field name=\"a\"><value><double>1.3</double></value></field>"+
				"<field name=\"b\"><value><double>2.4</double></value></field>"+
				"</fields>"+
				"<methods>"+
				"<method language=\"java\">public aladyn.test_classes.Point(double,double,char,aladyn.test_classes.Point)</method>"  +
				"</methods>"+
				"</object>" + "</value></param></params></methodCall>";

		Point p1 = new Point(4.4,5.4,'c');
		ObjectsMap.addObject("100", p1);
		ArrayList<Object> paramsList = new ArrayList<Object>();
		String methodName = parseCall(xmlRecords, paramsList);
		for( Object obj : paramsList) {
			System.out.println(obj.toString());
		}
	}*/

	/**
	 * Rend la valeur textuel d'une balise.
	 * @param e l'element du DOM dont on extrait la valeur
	 * @return  valeur de l'élément
	 */
	/*public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData)
		{
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}*/

	/**
	 * Méthode permettant au serveur de parser ce qu'il recoit du client. Cela remplit un tableau de paramètres
	 * rend le nom de la méthode à appeler.
	 * @param response  la réponse reçue par le client
	 * @return          la liste des paramêtres rendus par le serveur
	 */
	public static ArrayList<Object> parseResponse(String response){
		DocumentBuilder db;
		ArrayList<Object> paramsList = new ArrayList<Object>();
		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(response));
			Object value= null;
			Document doc;


			doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("methodResponse");

			Element methodResponseElement = (Element) nodes.item(0);
			NodeList nodesParams = methodResponseElement.getElementsByTagName("params");

			Element paramsElement =  (Element) nodesParams.item(0);
			NodeList nodesParam = paramsElement.getElementsByTagName("param");

			for(int i = 0; i < nodesParam.getLength(); i++)
			{
				Element paramElement = (Element) nodesParam.item(i);
				Node childNode = parseValue(paramElement) ;
				if(childNode != null){
					value = Serializer.getValueFromElement(childNode); 	
					paramsList.add(value);	
				}
				//System.out.println(paramElement.toString());
			}

			//NodeList nodes1 = doc.getElementsByTagName("object");
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paramsList;
	}



	/**
	 * Methode permettant au serveur de parser ce qu'il recoit du client. Cela renplit un tableau de parametres
	 * rend le nom de la méthode à appeler
	 * @param call : L'appel envoyé par le client
	 */
	public static String parseCall(String call, ArrayList<Object> paramsList){
		DocumentBuilder db;
		String methodName;

		try {
			db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(call));
			Document doc;


			doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("methodCall");

			Element methodCallElement = (Element) nodes.item(0);
			NodeList nodesMethod = methodCallElement.getElementsByTagName("methodName");

			Node methodNameElement =  nodesMethod.item(0);
			methodName = methodNameElement.getTextContent();

			NodeList nodesParams = methodCallElement.getElementsByTagName("params");

			Element paramsElement =  (Element) nodesParams.item(0);
			NodeList nodesParam = paramsElement.getElementsByTagName("param");

			for(int i = 0; i < nodesParam.getLength(); i++)
			{

				Element paramElement = (Element) nodesParam.item(i);
				NodeList nodesValue = paramElement.getElementsByTagName("value");

				Element elementValue = (Element) nodesValue.item(0);

				Node valueChild = elementValue.getFirstChild();
				if (valueChild.getNodeType() == Node.ELEMENT_NODE){
					paramsList.add(createObjectFromElement((Element)valueChild));
				}
			}
			return methodName;
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Node parseValue(Element element){
		NodeList nodesValue = element.getElementsByTagName("value");
		Element elementValue = (Element) nodesValue.item(0);
		Node valueChild = elementValue.getFirstChild();
		System.out.println(nodesValue.item(0).getTextContent() + valueChild.getNodeValue());
			
		if (valueChild.getNodeValue() == "void"){
			return null;
		} else if (valueChild.getNodeType() == Node.ELEMENT_NODE){
			return  valueChild;
		}
		else {
			return elementValue.getElementsByTagName("object").item(0);
		}
	}


	public static Object createObjectFromElement ( Element element) {
		String oid;
		BuildObject obj;
		if ( element.getNodeName() == "object") 
		{
			oid =element.getAttribute("oid");
			obj = new BuildObject("Object" + element.getAttribute("type"));
			obj.addStringField("oid",oid);
			NodeList nodesFieldList = element.getElementsByTagName("field");
			for (int j = 0; j< nodesFieldList.getLength(); j++ )
			{
				Element fieldElement = (Element) nodesFieldList.item(j);
				addFieldforObjectFromElement(fieldElement, obj);
			}
			NodeList nodesMethodsList = element.getElementsByTagName("method");
			Node methodElement;
			for (int j = 0; j< nodesMethodsList.getLength(); j++ )
			{
				methodElement = nodesMethodsList.item(j);
				addMethodforObjectFromElement(methodElement, obj);
			}
			obj.addSuperClass("aladyn.ObjectSerializable");
			try {
				return obj.getMyClass().newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		} else if (element.getNodeName() == "int") {
			return new Integer(((Node)element).getTextContent());
		}  else if (element.getNodeName() == "double") {
			return new Double(((Node)element).getTextContent());
		} else {
			return null;
		}
	}

	public static void addFieldforObjectFromElement ( Element fieldElement, BuildObject bo) {
		String fieldName;
		fieldName = fieldElement.getAttribute("name");
		Node valueElementContent = parseValue(fieldElement);
		String fieldValue = valueElementContent.getTextContent();

		if (valueElementContent.getNodeName() == "int") 
		{
			bo.addIntField(fieldName, Integer.parseInt(fieldValue));
		}  
		else if (valueElementContent.getNodeName() == "double")
		{
			bo.addDoubleField(fieldName,Double.parseDouble(fieldValue));
		} 
		else if (valueElementContent.getNodeName() == "string")
		{
			bo.addStringField(fieldName,fieldValue);
		}
		else if (valueElementContent.getNodeName() == "boolean")
		{
			bo.addBoolField(fieldName, Boolean.parseBoolean(fieldValue)) ;
		}
		else if (valueElementContent.getNodeName() == "dateTime.iso8601")
		{
			SimpleDateFormat dateformatter = new SimpleDateFormat("yyyyMMdd'T'HH:MM:ss");
			Date d;
			try {
				d = dateformatter.parse(fieldValue);
				dateformatter.applyPattern("yyyy','MM','dd','HH','MM','ss");
				bo.addDateField(fieldName,dateformatter.format(d)) ;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else if (valueElementContent.getNodeName() == "base64")
		{
			//TODO
			//return "public Byte " + fieldName + " = \"" + fieldValue + "\";" ;
		}
		else if (valueElementContent.getNodeName() == "array")
		{
			NodeList dataElement = ((Element)valueElementContent).getElementsByTagName("data");
			if (dataElement.getLength() == 1){
				NodeList arrayItemList =((Element) dataElement.item(0)).getElementsByTagName("value");

				//bo.addField(fieldName, CtClass.booleanType, ) ;
			}
			else
			{

			}
		}
		//Construire la chaine de caractere permettant de creer le champ
		//bo.addField(fieldName,getType(valueElementContent),getInitializer(valueElementContent));
	}

	public static void addMethodforObjectFromElement (Node methodElement, BuildObject bo) {
		String methodLanguage;
		methodLanguage= ((Element)methodElement).getAttribute("language");
		if( methodLanguage.equals("java")){
			bo.addMethod(methodElement.getTextContent());
		}

	}


	/*public static String getStringForField( String fieldName, Element valueElementContent){
		String fieldValue = getCharacterDataFromElement(valueElementContent);
		StringBuilder sb = new StringBuilder();

		if (valueElementContent.getNodeName() == "int") 
		{
			return "public int " + fieldName + "=" + fieldValue + ";";
		}  
		else if (valueElementContent.getNodeName() == "double")
		{
			return "public double " + fieldName + "=" + fieldValue + ";";
		} 
		else if (valueElementContent.getNodeName() == "string")
		{
			return "public String " + fieldName + " = \"" + fieldValue + "\";" ;
		}
		else if (valueElementContent.getNodeName() == "boolean")
		{
			return "public Boolean " + fieldName + " = \"" + Boolean.parseBoolean(fieldValue) + "\";" ;
		}
		else if (valueElementContent.getNodeName() == "base64")
		{
			return "public Byte " + fieldName + " = \"" + fieldValue + "\";" ;
		}
		else if (valueElementContent.getNodeName() == "array")
		{
			sb.append("public Object[] " + fieldName + "= {");

			sb.append("} ;");
			return "public Object[] " + fieldName + " = \"" + fieldValue + "\";" ;
		}
		else
		{
			return null;
		}
	}


	/*public static String getWellFormedValue(Element valueElementContent ){
		String fieldValue = getCharacterDataFromElement(valueElementContent);
		String result = "";
		if (valueElementContent.getNodeName() == "integer" && valueElementContent.getNodeName() == "double") 
		{
			result = fieldValue ;
		}
		else if (valueElementContent.getNodeName() == "string")
		{
			result = "\"" + fieldValue + "\"" ;
		}
		else if (valueElementContent.getNodeName() == "boolean")
		{
			if (fieldValue == "0")
				result = "false";
			else if (fieldValue == "1")
				result = "true";
			else 
				result = "";
				//TODO
		}
		else if (valueElementContent.getNodeName() == "base64")
		{
			result  = "\"" + fieldValue + "\";" ;
			//TODO
		}
		else if (valueElementContent.getNodeName() == "array")
		{
			NodeList dataElement = valueElementContent.getElementsByTagName("data");
			if (dataElement.getLength() == 1){
				result = "{";
				NodeList arrayItemList =((Element) dataElement.item(0)).getElementsByTagName("value");
				for (int i=0; i < arrayItemList.getLength(); i++){
					Element valueElement = (Element) arrayItemList.item(i);
					Element childElement = parseValue(valueElement);
					result += getWellFormedValue(childElement);
					if ( i < arrayItemList.getLength() - 1)
						result += ",";
					else
						result += "}";
				}
			}
			else
			{
				//TODO
				// Return chaine de caractere vide ou lance une exception
			}
		}
		else if (valueElementContent.getNodeName() == "struct"){

		}
		else if (valueElementContent.getNodeName() == "datetime.iso8601"){
			SimpleDateFormat dateformatter = new SimpleDateFormat ("yyyyMMdd'T'HH:MM:ss");

		}
		return result;
	}*/
}
