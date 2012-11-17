package aladyn.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import aladyn.client.Serializer;

public class TestSerializer {
	
	public static void main(String[] args){
		String[] testArray = {"hey"};
		boolean testBool = true;
		Date testDate = new java.util.GregorianCalendar().getTime();
		//testDate.set(2012, 12, 31);
		
		//System.out.println(callXml);
		/*callXml = "<methodCall><methodName>haha</methodName><params>" +
				"<param><value>"+
				"<object oid=\"100\"><fields>" +
				"<field name=\"a\"><value><double>1.3</double></value></field>"+
				"<field name=\"b\"><value><double>2.4</double></value></field>"+
				"</fields>"+
				"<methods>"+
				"<method language=\"java\">public String toString() { return String.valueOf(a+b); }</method>"  +
				"</methods>"+
				"</object>" + "</value></param></params></methodCall>";*/
		
		
		ArrayList<Object> testArrayList = new ArrayList<Object>();
		testArrayList.add('c');
		testArrayList.add("hey");
		testArrayList.add(1.3);
		
		HashMap<String,Object> testMap = new HashMap<String,Object>();
		
		testMap.put("01", 3);
		System.out.println("Tests");
		System.out.println(Serializer.serialize(testBool));
		System.out.println(Serializer.serialize(testArray));
		System.out.println(Serializer.serialize(testArrayList));
		System.out.println(Serializer.serialize(testMap));
		System.out.println(Serializer.serialize(testDate));
		System.out.println(Boolean.parseBoolean("0"));
	}
}
