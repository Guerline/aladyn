package xmlrmi;

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
import org.w3c.dom.Node;
import org.w3c.dom.CharacterData;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import server.BuildObject;

import client.Updater;

/*
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import aladyn.client.Serializer;
import aladyn.parser.Request;
import aladyn.server.BuildObject;*/

//Classe pour parler le XML
public class XMLParser {

	/**
	 * Construit la valeur textuel d'un élément xml
	 * @param e l'element du DOM dont on extrait la valeur
	 * @return le caracter
	 */
	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		
		if(child instanceof CharacterData) {
			CharacterData cd = (CharacterData)child;
			return cd.getData();
		}else {
			return "";
		}
	}

	/**
	 * Méthode permettant au serveur de parser une réponse du client. 
	 * Cela remplit un tableau de paramètres et rend le nom de la méthode à appeler.
	 * @param response la réponse reçue par le client
	 * @return la liste des paramêtres rendus par le serveur
	 */
	public static ArrayList<Object> parseResponse(String response){
			ArrayList<Object> paramsList = new ArrayList<Object>();
			try {
				DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
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
						value = Updater.getValueFromElement(childNode); 	
						paramsList.add(value);	
					}
					//System.out.println(paramElement.toString());
				}

				//NodeList nodes1 = doc.getElementsByTagName("object");
		}catch(IOException e) {
			System.out.println("error in/out with file");
		}catch(ParserConfigurationException e) {
			System.out.println("error configuring the parser");
		}catch(SAXException e) {
			System.out.println("general sax error");
		}
		return paramsList;
	}

	/**
	 * 
	 * 
	 * @param 
	 * @return 
	 */
	public static String parseCall(String call, ArrayList<Object> paramsList){
		String methodName;

		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
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
		}catch(IOException e) {
			System.out.println("error in/out with file");
		}catch(ParserConfigurationException e) {
			System.out.println("error configuring the parser");
		}catch(SAXException e) {
			System.out.println("general sax error");
		}
		return null;
	}

	/**
	 * Parse un element du DOM 
	 * @param element l'élément à parser
	 * @return 
	 */		
	public static Element parseValue(Element element) {
		NodeList nodesValue = element.getElementsByTagName("value");
		Element elementValue = (Element)nodesValue.item(0);
		Node valueChild = elementValue.getFirstChild();
		if(valueChild.getNodeType() == Node.ELEMENT_NODE) {
			return (Element)valueChild;
		}else {
			return (Element)(elementValue.getElementsByTagName("object").item(0));
		}
	}

	/**
	* 
	* 
	* @param 
	* @return 
	*/	
	public static Object createObjectFromElement(Element element) {
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
			obj.addSuperClass("tools.ObjectSerializable");
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
	
	/**
	* 
	* 
	* @param 
	* @return 
	*/	
	public static void addFieldforObjectFromElement(Element fieldElement, BuildObject bo) {
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
			try {
				Date d = dateformatter.parse(fieldValue);
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

	/**
	* ajoute la methode pour l'object depuis l'élément du DOM
	* @param methodElement le noeud du DOM correspondant à la methode
	* @param bo l'objet a construire
	*/	
	public static void addMethodforObjectFromElement (Node methodElement, BuildObject bo) {
		String methodLanguage;
		methodLanguage= ((Element)methodElement).getAttribute("language");
		if( methodLanguage.equals("java")){
			bo.addMethod(methodElement.getTextContent());
		}

	}

	/**
	* rend la chaine correspondant à un champ depuis un element du DOM
	* @param fieldName le nom du champ
	* @param valueElementContent la valeur de l'élément
	* @return la chaine correspondant à la valeur
	*/	
	public static String getStringForField(String fieldName, Element valueElementContent) {
		String fieldValue = getCharacterDataFromElement(valueElementContent);
		
		if(valueElementContent.getNodeName() == "integer") {
			return "public int " + fieldName + "=" + fieldValue + ";";
		}else if(valueElementContent.getNodeName() == "double") {
			return "public double " + fieldName + "=" + fieldValue + ";";
		}else if(valueElementContent.getNodeName() == "string") {
			return "public String " + fieldName + " = \"" + fieldValue + "\";" ;
		}else if(valueElementContent.getNodeName() == "boolean") {
			return "public Boolean " + fieldName + " = \"" + fieldValue + "\";" ;
		}else if(valueElementContent.getNodeName() == "base64") {
			return "public Byte " + fieldName + " = \"" + fieldValue + "\";" ;
		}else if(valueElementContent.getNodeName() == "array") {
			return "public Object[] " + fieldName + " = \"" + fieldValue + "\";" ;
		}else {
			return null;
		}
	}
	
}