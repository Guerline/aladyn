package xmlrmi;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

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

import client.Serializer;

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
			Document db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(response));
			Object value = null;
			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("methodResponse");
			Element methodResponseElement = (Element)nodes.item(0);
			NodeList nodesParams = methodResponseElement.getElementsByTagName("params");
			Element paramsElement = (Element)nodesParams.item(0);
			NodeList nodesParam = paramsElement.getElementsByTagName("param");

			for(int i = 0; i < nodesParam.getLength(); i++) {
				Element paramElement = (Element)nodesParam.item(i);
				value = Serializer.getValueFromElement(parseValue(paramElement)); 
				
				paramsList.add(value);
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
			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("methodCall");
			Element methodCallElement = (Element)nodes.item(0);
			NodeList nodesMethod = methodCallElement.getElementsByTagName("methodName");
			Element methodNameElement = (Element)nodesMethod.item(0);
			methodName = getCharacterDataFromElement(methodNameElement);
			NodeList nodesParams = methodCallElement.getElementsByTagName("params");
			Element paramsElement = (Element)nodesParams.item(0);
			NodeList nodesParam = paramsElement.getElementsByTagName("param");

			for(int i = 0; i < nodesParam.getLength(); i++) {
				Element paramElement = (Element) nodesParam.item(i);
				NodeList nodesValue = paramElement.getElementsByTagName("value");
				Element elementValue = (Element) nodesValue.item(0);
				Node valueChild = elementValue.getFirstChild();
				
				if(valueChild.getNodeType() == Node.ELEMENT_NODE) {
					paramsList.add(createObjectFromElement((Element)valueChild));
				}else {
					Element valueObjectChild = (Element)(elementValue.getElementsByTagName("object").item(0));
					paramsList.add(createObjectFromElement((Element)valueObjectChild));
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
		
		if(element.getNodeName() == "object") {
			oid = element.getAttribute("oid");
			obj = new BuildObject("Object" + oid);
			obj.addField("private String oid=\""+oid + "\";" );
			NodeList nodesFieldList = element.getElementsByTagName("field");
			for(int j = 0; j< nodesFieldList.getLength(); j++) {
				Element fieldElement = (Element)nodesFieldList.item(j);
				addFieldforObjectFromElement(fieldElement, obj);
			}
			
			NodeList nodesMethodsList = element.getElementsByTagName("method");
			Element methodElement;
			for(int j = 0; j< nodesMethodsList.getLength(); j++) {
				methodElement = (Element)nodesMethodsList.item(j);
				addMethodforObjectFromElement(methodElement, obj);
			}
			
			obj.addSuperClass("ObjectSerializable");
			try {
				return obj.getMyClass().newInstance();
			}catch (InstantiationException e) {
				System.out.println(e + "error instantiating the object");
				return null;
			} catch (IllegalAccessException e) {
				System.out.println(e + "error accessing the object");
				return null;
			}
		}else if(element.getNodeName() == "integer") {
			return new Integer(getCharacterDataFromElement(element));
		}else if(element.getNodeName() == "double") {
			return new Double(getCharacterDataFromElement(element));
		}else {
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
		String fieldName = fieldElement.getAttribute("name");
		Element valueElementContent = parseValue(fieldElement);

		//Construire la chaine de caractere permettant de creer le champ
		bo.addField(getStringForField(fieldName, valueElementContent));
	}

	/**
	* ajoute la methode pour l'object depuis l'élément du DOM
	* @param methodElement l'élément du DOM correspondant à la methode
	* @param bo l'objet a construire
	*/	
	public static void addMethodforObjectFromElement(Element methodElement, BuildObject bo) {
		String methodLanguage = methodElement.getAttribute("language");
		
		if(methodLanguage.equals("java")) {
			bo.addMethod(getCharacterDataFromElement(methodElement));
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