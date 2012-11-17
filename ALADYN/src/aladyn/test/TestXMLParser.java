package aladyn.test;

import java.util.ArrayList;

import aladyn.ObjectsMap;
import aladyn.parser.XMLParser;
import aladyn.test_classes.Point;

public class TestXMLParser {
	public static void main(String arg[]){
		String xmlRecords = "<methodCall><methodName>haha</methodName><params><param><value><double>1.2</double></value></param>" +
				"<param><value>"+
				"<object oid=\"100\"><fields>" +
				"<field name=\"a\"><value><double>1.3</double></value></field>"+
				"<field name=\"b\"><value><double>2.4</double></value></field>"+
				"</fields>"+
				"<methods>"+
				"<method language=\"java\">public String toString() { a=3.3 +b; return String.valueOf(a); }</method>"  +
				"</methods>"+
				"</object>" + "</value></param></params></methodCall>";

		Point p1 = new Point(4.4,5.4,"esdc");
		ObjectsMap.addObject("100", p1);
		ArrayList<Object> paramsList = new ArrayList<Object>();
		String methodName = XMLParser.parseCall(xmlRecords, paramsList);
		for( Object obj : paramsList) {
			System.out.println(obj.toString());
		}
	}
}
