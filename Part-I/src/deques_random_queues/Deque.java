package deques_random_queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
	
	private Node first = null;
	private Node last = null;
	private int length;
	
	private class Node{
		Item item;
		Node previous;
		Node next;
	}
	
	public Deque() {
	}

	public boolean isEmpty() {
		return first == null;
	}

	public int size() {
		return length;
	}

	public void addFirst(Item item) {
		if(item == null) {
			throw new IllegalArgumentException("Item cannot be null");
		}
		
		Node oldfirst = first;
		first = new Node();
		first.item = item;
		first.previous = null;
		first.next = oldfirst;
		
		if(oldfirst != null) {
			oldfirst.previous = first;
		}else {
			last = first;
		}
		
		length++;
	}

	public void addLast(Item item) {
		if(item == null) {
			throw new IllegalArgumentException("Item cannot be null");
		}
		
		Node oldlast = last;
		last = new Node();
		last.item = item;
		last.previous = oldlast;
		last.next = null;
		
		if(oldlast != null) {
			oldlast.next = last;
		}else {
			first = last;
		}
		
		length++;
	}

	public Item removeFirst() {
		if(isEmpty()) {
			throw new NoSuchElementException("It is not possible to remove an item from an empty Deque");
		}
		
		Item item = first.item;
		first = first.next;
		
		if(first != null) {
			first.previous = null;
		}else {
			last = first;
		}
		
		length--;
		
		return item;
	}

	public Item removeLast() {
		if(isEmpty()) {
			throw new NoSuchElementException("It is not possible to remove an item from an empty Deque");
		}
		
		Item item = last.item;
		last = last.previous;
		
		if(last != null) {
			last.next = null;
		}else {
			first = last;
		}
		
		length--;
		
		return item;
	}

	public Iterator<Item> iterator() {
		return new DequeIterator();
	}

	private class DequeIterator implements Iterator<Item>{
		private Node current = first;
		
		public boolean hasNext() { return current != null; }
		
		public void remove() { throw new UnsupportedOperationException("Operation cannot be called"); }
		
		public Item next() {
			if(!hasNext()) {
				throw new NoSuchElementException("There are no more items to return");
			}
			
			Item item = current.item;
			current = current.next;
			return item;
		}
	}
	
	public static void main(String[] args) {
		Deque<String> deque = new Deque<String>();
		deque.addFirst("1");
		deque.addLast("5");
		deque.addFirst("2");
		deque.removeLast();
		deque.addLast("3");
		deque.addFirst("6");
		deque.removeFirst();
		deque.addLast("7");
		
		Iterator<String> iterator = deque.iterator();
		
		while(iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}
}
