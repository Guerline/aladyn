package aladyn.server;

import java.util.ArrayList;
import java.util.Hashtable;

import aladyn.client.Serializer;

public class BuildResponse {

	public static String buildXmlResponse(ArrayList<Object> tab) {
		StringBuilder xmlResponse = new StringBuilder();
		xmlResponse.append("<methodResponse>" + "<params>");
		for(int i = 0; i < tab.size(); i++) {
			xmlResponse.append("<param>"
					+ Serializer.serialize(tab.get(i)) + "</param>");
		}
		xmlResponse.append("</params>" + "</methodResponse>");
		return(xmlResponse.toString());
	}

	public String buildXmlFaultResponse(int faultCode, String faultString) {
		StringBuilder xmlFaultResponse = new StringBuilder();
		Hashtable<String, Object> response = new Hashtable<String, Object>();
		response.put("faultCode", faultCode);
		response.put("faultString", faultString);
		xmlFaultResponse.append("<methodResponse><fault>" + "</fault></methodResponse>");
		return(xmlFaultResponse.toString());
	}

	public static void main(String[] args) {
		BuildResponse build = new BuildResponse();

		ArrayList<Object> tab = new ArrayList<Object>();
		tab.add(true);
		}
}
