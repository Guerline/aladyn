package tools;

import java.util.HashMap;
import java.util.Map;

//Classe représentant le tableau de hachage pour stocker les objets
public class ObjectsMap {
	private final static Map<String, Object> map = new HashMap<String, Object>();  
	
	/**
	 * recupère l'objet dans la map
	 * @param key la clef de l'objet à recuperer
	 * @return l'objet correspondant à la clef
	 */
	public static Object getObject(String key) {
		return map.get(key);
	}
	
	/**
	 * ajout d'une clef, valeur dans la map
	 * @param key la clef de l'objet dans la map
	 * @param obj l'objet à ajouter dans la map
	 */
	public static void addObject(String key, Object obj) {
		map.put(key, obj);
	}
}
