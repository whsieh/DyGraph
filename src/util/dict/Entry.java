/* Entry.java */

package util.dict;

/**
 *  A class for dictionary entries.
 **/

public class Entry<K,V> {

    protected K key;
    protected V value;

    public Entry(K key,V value) {
        this.key = key;
        this.value = value;
    }
    
    public K key() {
	return key;
    }

    public V value() {
	return value;
    }

}
