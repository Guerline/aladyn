package aladyn.parser;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javassist.CtClass;

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
	Request request;

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
	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData)
		{
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}

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
				value = Serializer.getValueFromElement(parseValue(paramElement) ); 
				paramsList.add(value);
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
			
			Element methodNameElement = (Element) nodesMethod.item(0);
			methodName =  getCharacterDataFromElement(methodNameElement);
			
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
				else {
					Element valueObjectChild = (Element)(elementValue.getElementsByTagName("object").item(0));
					paramsList.add(createObjectFromElement((Element)valueObjectChild));
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

	public static Element parseValue(Element element){
		NodeList nodesValue = element.getElementsByTagName("value");
		Element elementValue = (Element) nodesValue.item(0);
		Node valueChild = elementValue.getFirstChild();
		if (valueChild.getNodeType() == Node.ELEMENT_NODE){
			return  (Element)valueChild;
		}
		else {
			return (Element)(elementValue.getElementsByTagName("object").item(0));
		}
	}
	
	
	public static Object createObjectFromElement ( Element element) {
		String oid;
		BuildObject obj;
		if ( element.getNodeName() == "object") 
		{
			oid =element.getAttribute("oid");
			obj = new BuildObject("Object" + oid);
			obj.addField("private String oid=\""+oid + "\";" );
			NodeList nodesFieldList = element.getElementsByTagName("field");
			for (int j = 0; j< nodesFieldList.getLength(); j++ )
			{
				Element fieldElement = (Element) nodesFieldList.item(j);
				addFieldforObjectFromElement(fieldElement, obj);
			}
			 NodeList nodesMethodsList = element.getElementsByTagName("method");
			 Element methodElement;
			 for (int j = 0; j< nodesMethodsList.getLength(); j++ )
			 {
				methodElement = (Element) nodesMethodsList.item(j);
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
		} else if (element.getNodeName() == "integer") {
			return new Integer(getCharacterDataFromElement(element));
		}  else if (element.getNodeName() == "double") {
			return new Double(getCharacterDataFromElement(element));
		} else {
			return null;
		}
	}

	public static void addFieldforObjectFromElement ( Element fieldElement, BuildObject bo) {
		String fieldName;
		fieldName = fieldElement.getAttribute("name");
		Element valueElementContent = parseValue(fieldElement);
		
		//Construire la chaine de caractere permettant de creer le champ
		bo.addField(getStringForField(fieldName,valueElementContent ));
	}
	
	public static void addMethodforObjectFromElement ( Element methodElement, BuildObject bo) {
		String methodLanguage;
		methodLanguage= methodElement.getAttribute("language");
		 if( methodLanguage.equals("java")){
			 	bo.addMethod(getCharacterDataFromElement(methodElement));
		 }
		
	}
	
	
	public static String getStringForField( String fieldName ,Element valueElementContent){
		String fieldValue = getCharacterDataFromElement(valueElementContent);
		if (valueElementContent.getNodeName() == "integer") 
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
			return "public Boolean " + fieldName + " = \"" + fieldValue + "\";" ;
		}
		else if (valueElementContent.getNodeName() == "base64")
		{
			return "public Byte " + fieldName + " = \"" + fieldValue + "\";" ;
		}
		else if (valueElementContent.getNodeName() == "array")
		{
			return "public Object[] " + fieldName + " = \"" + fieldValue + "\";" ;
		}
		else
		{
			return null;
		}
	}
	

}
