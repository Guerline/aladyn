package tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

//Classe représentant le tableau de hachage pour stocker les objets
public class ObjectsMap {
	private final static Map<String, Object> map = new HashMap<String, Object>();  
	private static AtomicInteger it = new AtomicInteger(1);
	
	/**
	 * recupère l'objet dans la map
	 * @param key la clef de l'objet à recuperer
	 * @return l'objet correspondant à la clef
	 */
	public static Object getObject(String key) {
		return map.get(key);
	}
	
	
	
	public static String getKey ( Object objectSerializable) {
		for(Entry<String, Object> entryInMap : map.entrySet()){
			if( entryInMap.getValue().equals(objectSerializable))
				return entryInMap.getKey();
		}
		return null;
	}
	
	/**
	 * ajout d'une clef, valeur dans la map
	 * @param obj l'objet à ajouter dans la map
	 */
	public static void addObject( Object obj) {
		String key = String.valueOf(it.incrementAndGet());
		map.put("http://localhost/" + key, obj);
	}
}
