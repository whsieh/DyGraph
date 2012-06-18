/* HashTableChained.java */

package util.dict;
import util.list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 **/

public class HashTable <K,V> implements Dictionary <K,V>{

    protected static int PRIME = 32771;
    protected int size, a, b, numBuckets;
    protected DLinkedList<Entry>[] buckets;


    /** 
     *  Construct a new empty hash table intended to hold roughly sizeEstimate
     *  entries.  (The precise number of buckets is up to you, but we recommend
     *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
     **/

    public HashTable(int sizeEstimate) {
	this();
	int N = (int)(sizeEstimate * 1.426);
	N = HashTable.firstPrimeLarger(N);
	if (sizeEstimate == 0) { N = 0; }
	this.buckets = new DLinkedList[N];
	this.numBuckets = buckets.length;
    }
  
    /** 
     *  Construct a new empty hash table with a default size.  Say, a prime in
     *  the neighborhood of 100.
     **/

    public HashTable() {
	java.util.Random rand = new java.util.Random();
	a = rand.nextInt(HashTable.PRIME - 1) + 1;
	b = rand.nextInt(HashTable.PRIME);
	this.size = 0;
	this.buckets = new DLinkedList[131];
	this.numBuckets = buckets.length;
    }
  
    /**
     * Find the first prime greater than or equal to x.
     * @param x a positive integer
     * @return smallest prime larger than or equal to x.
     */
    private static int firstPrimeLarger(int x) {
	while (true) {
	    if (isPrime(x)) {
		return x;
	    } else {
		x++;
	    }
	}
    }
  
    /**
     * Determine if x is a prime number or not.
     * @param x a positive integer
     * @return true if x is a prime, false otherwise
     */
    private static boolean isPrime(int x) {
	if (x <= 1 || (x != 2 && x % 2 == 0)) { return false; }
	boolean prime = true;
	int a = 3;
	while (prime && a*a <= x) {
	    if (x % a == 0) {
		prime = false;
	    } else {
		a++;
	    }
	}
	return prime;
    }


    /**
     *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
     *  to a value in the range 0...(size of hash table) - 1.
     *
     *  This function should have package protection (so we can test it), and
     *  should be used by insert, find, and remove.
     **/

    private int compFunction(int code) {
	int n = ((a*code + b) % HashTable.PRIME) % numBuckets;
	n = n < 0 ? -n : n;
	return n;
    }

    /** 
     *  Returns the number of entries stored in the dictionary.  Entries with
     *  the same key (or even the same key and value) each still count as
     *  a separate entry.
     *  @return number of entries in the dictionary.
     **/

    public int size() {
	return this.size;
    }

    /** 
     *  Tests if the dictionary is empty.
     *
     *  @return true if the dictionary has no entries; false otherwise.
     **/

    public boolean isEmpty() {
	return this.size == 0;
    }

    /**
     *  Create a new Entry object referencing the input key and associated value,
     *  and insert the entry into the dictionary.  Return a reference to the new
     *  entry.  Multiple entries with the same key (or even the same key and
     *  value) can coexist in the dictionary.
     *
     *  This method should run in O(1) time if the number of collisions is small.
     *
     *  @param key the key by which the entry can be retrieved.
     *  @param value an arbitrary object.
     *  @return an entry containing the key and value.
     **/

    public Entry<K,V> insert(K key, V value) {
	Entry entry = new Entry(key,value);
	int index = compFunction(key.hashCode());
	if (buckets[index] == null) {
	    buckets[index] = new DLinkedList();
	}
	this.buckets[index].insertBack(entry);
	this.size++;
	if (((double)(size) / buckets.length) >= 0.75) {
	    resize(2);
	}
	return entry;
    }

    /** 
     *  Search for an entry with the specified key.  If such an entry is found,
     *  return it; otherwise return null.  If several entries have the specified
     *  key, choose one arbitrarily and return it.
     *
     *  This method should run in O(1) time if the number of collisions is small.
     *
     *  @param key the search key.
     *  @return an entry containing the key and an associated value, or null if
     *          no entry contains the specified key.
     **/

    public V find(K key) {
	int index = compFunction(key.hashCode());
	if (buckets[index] == null || buckets[index].isEmpty()) {
	    return null;
	} else {
	    DLinkedListNode current = (DLinkedListNode)(buckets[index].front());
	    while (current.isValidNode()) {
		try {
		    if (((Entry)(current.item())).key().equals(key)) {
			return (V)((Entry)(current.item())).value;
		    }
		    current = (DLinkedListNode)(current.next());
		}
		catch (InvalidNodeException ex) {
		    return null;
		}
	    }
	    return null;
	}
    }

    /** 
     *  Remove an entry with the specified key.  If such an entry is found,
     *  remove it from the table and return it; otherwise return null.
     *  If several entries have the specified key, choose one arbitrarily, then
     *  remove and return it.
     *
     *  This method should run in O(1) time if the number of collisions is small.
     *
     *  @param key the search key.
     *  @return an entry containing the key and an associated value, or null if
     *          no entry contains the specified key.
     */

    public V remove(K key) {
	int index = compFunction(key.hashCode());
	if (buckets[index] == null || buckets[index].isEmpty()) {
	    return null;
	} else {
	    DLinkedListNode current = (DLinkedListNode)(buckets[index].front());
	    while (current.isValidNode()) {
		try {
		    if (((Entry)(current.item())).key().equals(key)) {
			Entry entry = (Entry)(current.item());
			current.remove();
			this.size--;
			if (((double)(size) / buckets.length) <= 0.25) {
			    resize(0.5);
			}
			return (V)(entry.value);
		    }
		    current = (DLinkedListNode)(current.next());
		}
		catch (InvalidNodeException ex) {
		    return null;
		}
	    }
	    return null;
	}
    }
  
    /**
     * Resize the hashtable to a factor of the original size.
     * @param factor
     */
    private void resize(double factor) {
	if (buckets.length * factor < size) {
	    return;
	}
	DLinkedList[] temp = new DLinkedList[(int)(buckets.length * factor)];
	this.numBuckets = temp.length;
	int s = this.size, index;
	DLinkedListNode node;
	Entry entry;
      
	for (int i = 0; i < buckets.length; i++) {
	    if (buckets[i] != null && !(buckets[i].isEmpty())) {
		node = (DLinkedListNode)(buckets[i].front());
		try {
		    while (node.isValidNode()) {
			entry = (Entry)(node.item());
			index = compFunction(entry.key.hashCode());
			if (temp[index] == null) {
			    temp[index] = new DLinkedList();
			}
			temp[index].insertBack(entry);
			s--;
			node = (DLinkedListNode)(node.next());
		    }
		}
		catch(Exception ex) {
		    System.out.println(ex);
		}
	    }
	    if (s == 0) { break; }
	}
	buckets = temp;
    }

}
