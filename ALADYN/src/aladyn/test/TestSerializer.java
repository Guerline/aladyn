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
		Calendar testDate = Calendar.getInstance();
		testDate.set(2012, 12, 31);
		
		ArrayList<Object> testArrayList = new ArrayList<Object>();
		testArrayList.add('c');
		testArrayList.add("hey");
		
		HashMap<String,Object> testMap = new HashMap<String,Object>();
		testMap.put("01", 3);
		System.out.println("Tests");
		System.out.println(Serializer.serialize(testBool));
		System.out.println(Serializer.serialize(testArray));
		System.out.println(Serializer.serialize(testArrayList));
		System.out.println(Serializer.serialize(testMap));
		System.out.println(Serializer.serialize(testDate));
	}
}