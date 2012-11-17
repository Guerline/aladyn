package aladyn.parser;

import java.util.HashMap;
import java.util.Map;

final class MyEntry<K, V> implements Map.Entry<K, V> {
    private final K k;
    private V v;
    
    
    public MyEntry(K key, V value) {
        k = key;
        v = value;
    }

    @Override
    public K getKey() {
        return(k);
    }

    @Override
    public V getValue() {
        return(v);
    }

    @Override
    public V setValue(V value) {
        V old = v;
        v = value;
        return old;
    }
    
}
