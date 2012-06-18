/* DListNode.java */

package util.list;

/**
 *  A DListNode is a mutable node in a DList (doubly-linked list).
 **/

public class DLinkedListNode<N> extends ListNode<N>{

    /**
     *  (inherited)  item references the item stored in the current node.
     *  (inherited)  myList references the List that contains this node.
     *  prev references the previous node in the DList.
     *  next references the next node in the DList.
     **/

    protected DLinkedListNode<N> prev;
    protected DLinkedListNode<N> next;

    /**
     *  DListNode() constructor.
     *  @param i the item to store in the node.
     *  @param l the list this node is in.
     *  @param p the node previous to this node.
     *  @param n the node following this node.
     */
    DLinkedListNode(N i, DLinkedList l, DLinkedListNode p, DLinkedListNode n) {
	item = i;
	myList = l;
	prev = p;
	next = n;
    }

    /**
     *  isValidNode returns true if this node is valid; false otherwise.
     *  An invalid node is represented by a `myList' field with the value null.
     *  Sentinel nodes are invalid, and nodes that don't belong to a list are
     *  also invalid.
     *
     *  @return true if this node is valid; false otherwise.
     *
     *  Performance:  runs in O(1) time.
     */
    @Override
    public boolean isValidNode() {
	return myList != null;
    }

    /**
     *  next() returns the node following this node.  If this node is invalid,
     *  throws an exception.
     *
     *  @return the node following this node.
     *  @exception InvalidNodeException if this node is not valid.
     *
     *  Performance:  runs in O(1) time.
     */
    @Override
    public ListNode<N> next() throws InvalidNodeException {
	if (!isValidNode()) {
	    throw new InvalidNodeException("next() called on invalid node");
	}
	return next;
    }

    /**
     *  prev() returns the node preceding this node.  If this node is invalid,
     *  throws an exception.
     *
     *  @param node the node whose predecessor is sought.
     *  @return the node preceding this node.
     *  @exception InvalidNodeException if this node is not valid.
     *
     *  Performance:  runs in O(1) time.
     */
    @Override
    public ListNode<N> prev() throws InvalidNodeException {
	if (!isValidNode()) {
	    throw new InvalidNodeException("prev() called on invalid node");
	}
	return prev;
    }

    /**
     *  insertAfter() inserts an item immediately following this node.  If this
     *  node is invalid, throws an exception.
     *
     *  @param item the item to be inserted.
     *  @exception InvalidNodeException if this node is not valid.
     *
     *  Performance:  runs in O(1) time.
     */
    @Override
    public void insertAfter(N item) throws InvalidNodeException {
	if (!isValidNode()) {
	    throw new InvalidNodeException("insertAfter() called on invalid node");
	}
	DLinkedListNode<N> newNode = ((DLinkedList<N>)myList).newNode(item,(DLinkedList<N>)myList,this,this.next);
	this.next = newNode;
	newNode.next.prev = newNode;
	myList.size++;
    }

    /**
     *  insertBefore() inserts an item immediately preceding this node.  If this
     *  node is invalid, throws an exception.
     *
     *  @param item the item to be inserted.
     *  @exception InvalidNodeException if this node is not valid.
     *
     *  Performance:  runs in O(1) time.
     */
    @Override
    public void insertBefore(N item) throws InvalidNodeException {
	if (!isValidNode()) {
	    throw new InvalidNodeException("insertBefore() called on invalid node");
	}
	DLinkedListNode<N> newNode = ((DLinkedList<N>)myList).newNode(item,(DLinkedList<N>)myList,this.prev,this);
	this.prev = newNode;
	newNode.prev.next = newNode;
	myList.size++;
    }

    /**
     *  remove() removes this node from its DList.  If this node is invalid,
     *  throws an exception.
     *
     *  @exception InvalidNodeException if this node is not valid.
     *
     *  Performance:  runs in O(1) time.
     */
    @Override
    public void remove() throws InvalidNodeException {
	if (!isValidNode()) {
	    throw new InvalidNodeException("remove() called on invalid node");
	}
	this.prev.next = this.next;
	this.next.prev = this.prev;
	myList.size--;

	myList = null;
	next = null;
	prev = null;
    }

}
