package aladyn;

import java.util.HashMap;
import java.util.Map;

public class ObjectsMap {
	private final static Map<String, Object> map = new HashMap<String, Object>();  
	
	public static Object getObject ( String key) {
		return map.get(key);
	}
	
	public static void addObject(String key, Object obj){
		map.put(key, obj);
	}
}
