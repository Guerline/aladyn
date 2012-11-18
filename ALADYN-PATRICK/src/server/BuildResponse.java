package server;

import java.util.ArrayList;
import java.util.Hashtable;

import client.Serializer;

//Classe pour la reconstruction d'une réponse
public class BuildResponse {

	/**
	 * methode pour la reconstruction de la methodResponse pour le client
	 * @param tab le tableau
	 * @return le XML representant la reponse pour le client
	 */
	public String buildXmlResponse(ArrayList<Object> tab) {
		StringBuilder xmlResponse = new StringBuilder();
		
		xmlResponse.append("<?xml version=\"1.0\"?><methodResponse>" + "<params>");
		buildParams(tab, xmlResponse);
		xmlResponse.append("</params>" + "</methodResponse>");
		return(xmlResponse.toString());
	}
	
	public String buildParams(ArrayList<Object> tab, StringBuilder xmlResponse){
		String serializedParam;
		
		for(int i = 0; i < tab.size(); i++) {
			xmlResponse.append("<param>");
			serializedParam= Serializer.serialize(tab.get(i));
			if(serializedParam != null) {
				xmlResponse.append(serializedParam);
			}else {
				return buildXmlFaultResponse(1, "Parameters couldn't be serialized");
			}
			xmlResponse.append("</param>");
		}
		return xmlResponse.toString();
	}

	/**
	 * methode pour la reconstruction des fautes de la methodResponse pour le client
	 * @param faultCode le code erreur
	 * @param faultString la chaine representant l'erreur
	 * @return le XML representant la faute
	 */
	public String buildXmlFaultResponse(int faultCode, String faultString) {
		StringBuilder xmlFaultResponse = new StringBuilder();
		Hashtable<String, Object> response = new Hashtable<String, Object>();
		
		response.put("faultCode", faultCode);
		response.put("faultString", faultString);
		xmlFaultResponse.append("<methodResponse><fault>");
		xmlFaultResponse.append(Serializer.serialize(response));
		xmlFaultResponse.append("</fault></methodResponse>");
		return(xmlFaultResponse.toString());
	}

}
