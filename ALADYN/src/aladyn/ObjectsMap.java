package aladyn;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class ObjectsMap {
	private final static HashMap<String, Object> map = new HashMap<String, Object>();  
	private static AtomicInteger it = new AtomicInteger(1);
	
	public static Object getObject ( String key) {
		return map.get(key);
	}
	
	public static String getKey ( Object objectSerializable) {
		for(Entry<String, Object> entryInMap : map.entrySet()){
			if( entryInMap.getValue().equals(objectSerializable))
				return entryInMap.getKey();
		}
		return null;
	}
	
	public static void addObject(Object obj){
		String key = String.valueOf(it.incrementAndGet());
		map.put("http://localhost/" + key, obj);
	}
}
