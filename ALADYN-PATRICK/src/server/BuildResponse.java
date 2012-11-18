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
	public static String buildXmlResponse(ArrayList<Object> tab) {
		StringBuilder xmlResponse = new StringBuilder();
		
		xmlResponse.append("<methodResponse>" + "<params>");
		for(int i = 0; i < tab.size(); i++) {
			xmlResponse.append("<param>" + Serializer.serialize(tab.get(i)) + "</param>");
		}
		xmlResponse.append("</params>" + "</methodResponse>");
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
		xmlFaultResponse.append("<methodResponse><fault>" + "</fault></methodResponse>");
		return xmlFaultResponse.toString();
	}

	public static void main(String[] args) {
		ArrayList<Object> tab = new ArrayList<Object>();
		
		tab.add(true);
	}
}
