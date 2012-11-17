package aladyn.server;

import java.util.ArrayList;
import java.util.Hashtable;

import aladyn.client.Serializer;

public class BuildResponse {

	public String buildXmlResponse(ArrayList<Object> tab) {
		StringBuilder xmlResponse = new StringBuilder();
		xmlResponse.append("<methodResponse>" + "<params>");
		buildParams(tab, xmlResponse);
		xmlResponse.append("</params>" + "</methodResponse>");
		return(xmlResponse.toString());
	}

	public String buildParams(ArrayList<Object> tab, StringBuilder xmlResponse){
		for(int i = 0; i < tab.size(); i++) {
			xmlResponse.append("<param>");
			String result = Serializer.serialize(tab.get(i));
			if(result != null){
				xmlResponse.append(result);
			}
			else{
				return buildXmlFaultResponse(1, "Parameters couldn't be serialized");
			}
			xmlResponse.append("</param>");
		}
		return xmlResponse.toString();
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
