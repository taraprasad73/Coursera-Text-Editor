package textgen;

import java.util.AbstractList;


/** A class that implements a doubly linked list
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 * @param <E> The type of the elements stored in the list
 */
public class MyLinkedList<E> extends AbstractList<E> {
	LLNode<E> head;
	LLNode<E> tail;
	int size;

	/** Create a new empty LinkedList */
	public MyLinkedList() {
		// TODO: Implement this method
		head = new LLNode<E>(null);
		tail = new LLNode<E>(head, null);
		size = 0;
	}

	/**
	 * Appends an element to the end of the list
	 * @param element The element to add
	 */
	public boolean add(E element ) 
	{
		// TODO: Implement this method
		add(size, element);
		return true;
	}

	/** Get the element at position index 
	 * @throws IndexOutOfBoundsException if the index is out of bounds. */
	public E get(int index) 
	{
		// TODO: Implement this method.
		if(index > (size - 1) || index < 0)
			throw new IndexOutOfBoundsException();
		LLNode curr = head;
		for (int i = 0; i <= index; i++) {
			curr = curr.next;
		}
		return (E) curr.data;
	}

	/**
	 * Add an element to the list at the specified index
	 * @param The index where the element should be added
	 * @param element The element to add
	 */
	public void add(int index, E element ) 
	{
		// TODO: Implement this method
		if(element == null)
			throw new NullPointerException();
		if(index > size || index < 0)
			throw new IndexOutOfBoundsException();
		LLNode curr = head;
		LLNode prev;
		LLNode next;
		for (int i = 0; i < index; i++) {
			curr = curr.next;
		}
		prev = curr;
		next = curr.next;
		curr = new LLNode(prev, element, next);
		size++;
	}


	/** Return the size of the list */
	public int size() 
	{
		// TODO: Implement this method
		return size;
	}

	/** Remove a node at the specified index and return its data element.
	 * @param index The index of the element to remove
	 * @return The data element removed
	 * @throws IndexOutOfBoundsException If index is outside the bounds of the list
	 * 
	 */
	public E remove(int index) 
	{
		// TODO: Implement this method
		if(index > (size - 1) || index < 0)
			throw new IndexOutOfBoundsException();
		LLNode curr = head;
		LLNode prev;
		LLNode next;
		for (int i = 0; i <= index; i++) {
			curr = curr.next;
		}
		prev = curr.prev;
		next = curr.next;
		prev.next = next;
		next.prev = prev;
		size--;
		return (E) curr.data;
	}

	/**
	 * Set an index position in the list to a new element
	 * @param index The index of the element to change
	 * @param element The new element
	 * @return The element that was replaced
	 * @throws IndexOutOfBoundsException if the index is out of bounds.
	 */
	public E set(int index, E element) 
	{
		// TODO: Implement this method
		if(index > (size - 1) || index < 0)
			throw new IndexOutOfBoundsException();
		if(element == null)
			throw new NullPointerException();
		LLNode curr = head;
		LLNode prev;
		LLNode next;
		for (int i = 0; i <= index; i++) {
			curr = curr.next;
		}
		E prevData = (E) curr.data;
		curr.data = element;
		return (E) prevData;
	}   
}

class LLNode<E> 
{
	LLNode<E> prev;
	LLNode<E> next;
	E data;

	// TODO: Add any other methods you think are useful here
	// E.g. you might want to add another constructor

	public LLNode(E e) 
	{
		this.data = e;
		this.prev = null;
		this.next = null;
	}
	
	public LLNode(LLNode prev, E e) {
		this(e);
		this.next = prev.next;
		prev.next = this;
		this.prev = prev;
	}
	
	public LLNode(LLNode prev, E e, LLNode next) {
		this(e);
		prev.next = this;
		this.prev = prev;
		next.prev = this;
		this.next = next;
	}
	
}
