/* DList.java */

package util.list;

import java.util.Iterator;

/**
 *  A DList is a mutable doubly-linked list ADT.  Its implementation is
 *  circularly-linked and employs a sentinel node at the head of the list.
 **/

public final class DLinkedList<N> extends List<N> implements Iterable<N> {

    /**
     *  (inherited)  size is the number of items in the list.
     *  head references the sentinel node.
     *  Note that the sentinel node does not store an item, and is not included
     *  in the count stored by the "size" field.
     **/

    protected DLinkedListNode<N> head;

    /* DList invariants:
     *  1)  head != null.
     *  2)  For every DListNode x in a DList, x.next != null.
     *  3)  For every DListNode x in a DList, x.prev != null.
     *  4)  For every DListNode x in a DList, if x.next == y, then y.prev == x.
     *  5)  For every DListNode x in a DList, if x.prev == y, then y.next == x.
     *  6)  For every DList l, l.head.myList = null.  (Note that l.head is the
     *      sentinel.)
     *  7)  For every DListNode x in a DList l EXCEPT l.head (the sentinel),
     *      x.myList = l.
     *  8)  size is the number of DListNodes, NOT COUNTING the sentinel,
     *      that can be accessed from the sentinel (head) by a sequence of
     *      "next" references.
     **/

    /**
     *  newNode() calls the DListNode constructor.  Use this method to allocate
     *  new DListNodes rather than calling the DListNode constructor directly.
     *  That way, only this method need be overridden if a subclass of DList
     *  wants to use a different kind of node.
     *
     *  @param item the item to store in the node.
     *  @param list the list that owns this node.  (null for sentinels.)
     *  @param prev the node previous to this node.
     *  @param next the node following this node.
     **/
    protected DLinkedListNode<N> newNode(N item, DLinkedList<N> list,
				DLinkedListNode<N> prev, DLinkedListNode<N> next) {
	return new DLinkedListNode(item, list, prev, next);
    }

    /**
     *  DList() constructs for an empty DList.
     **/
    public DLinkedList() {
	head = newNode(null,null,null,null);
	head.next = head;
	head.prev = head;
	size = 0;
    }

    /**
     *  insertFront() inserts an item at the front of this DList.
     *  @param item is the item to be inserted.
     *
     *  Performance:  runs in O(1) time.
     **/
    @Override
    public void insertFront(N item) {      
	DLinkedListNode newNode = newNode(item,this,head,head.next);
	head.next.prev = newNode;
	head.next = newNode;
	size++;
    }

    /**
     *  insertBack() inserts an item at the back of this DList.
     *  @param item is the item to be inserted.
     *
     *  Performance:  runs in O(1) time.
     **/
    @Override
    public void insertBack(N item) {
	DLinkedListNode newNode = newNode(item,this,head.prev,head);
	head.prev.next = newNode;
	head.prev = newNode;
	size++;
    }

    /**
     *  front() returns the node at the front of this DList.  If the DList is
     *  empty, return an "invalid" node--a node with the property that any
     *  attempt to use it will cause an exception.  (The sentinel is "invalid".)
     *  @return a ListNode at the front of this DList.
     *
     *  Performance:  runs in O(1) time.
     */
    @Override
    public ListNode<N> front() {
	return head.next;
    }

    /**
     *  back() returns the node at the back of this DList.  If the DList is
     *  empty, return an "invalid" node--a node with the property that any
     *  attempt to use it will cause an exception.  (The sentinel is "invalid".)
     *  @return a ListNode at the back of this DList.
     *
     *  Performance:  runs in O(1) time.
     */
    @Override
    public ListNode<N> back() {
	return head.prev;
    }
    
    public N find(N n) {
        for(N elem : this) {
            if (elem.equals(n)) {
                return elem;
            }
        }
        return null;
    }

    /**
     *  toString() returns a String representation of this DList.
     *  @return a String representation of this DList.
     *
     *  Performance:  runs in O(n) time, where n is the length of the list.
     */
    @Override
    public String toString() {
	String result = "[ ";
	DLinkedListNode<N> current = head.next;
	while (current != head) {
	    result = result + current.item + ", ";
	    current = current.next;
	}
	result = result.substring(0,result.length()-2);
	return result + " ]";
    }

    @Override
    public Iterator<N> iterator() {
        return new java.util.Iterator<N>() {

            ListNode<N> cur = front();
            
            @Override
            public boolean hasNext() {
                return cur != null && cur.isValidNode();
            }

            @Override
            public N next() {
                try {
                    N result = cur.item();
                    cur = cur.next();
                    return result;
                }
		catch (Exception e) {}
                return null;
            }

            @Override
            public void remove() {
                return;
            }
            
        };
    }
}