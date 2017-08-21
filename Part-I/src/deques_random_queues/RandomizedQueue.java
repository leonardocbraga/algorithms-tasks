package deques_random_queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	
	private Item[] list = null;
	private int N = 0;
	
	public RandomizedQueue() {
		list = (Item[]) new Object[1];
	}

	public boolean isEmpty() {
		return N == 0;
	}

	public int size() {
		return N;
	}

	public void enqueue(Item item) {
		if(item == null) {
			throw new IllegalArgumentException("Item cannot be null");
		}
		
		if(N == list.length) {
			resize(2 * list.length);
		}
		
		list[N++] = item;
	}

	public Item dequeue() {
		if(isEmpty()) {
			throw new NoSuchElementException("It is not possible to remove an item from an empty Deque");
		}
		
		int randomIndex = StdRandom.uniform(N);
		
		Item aux = list[randomIndex];
		list[randomIndex] = list[N - 1];
		list[N - 1] = aux;
		
		Item item = list[--N];
		list[N] = null;
		
		if (N > 0 && N == list.length/4) {
			resize(list.length/2);
		}
		
		return item;
	}

	public Item sample() {
		if(isEmpty()) {
			throw new NoSuchElementException("It is not possible to remove an item from an empty Deque");
		}
		
		int randomIndex = StdRandom.uniform(N);
		
		return list[randomIndex];
	}

	public Iterator<Item> iterator() {
		return new RandomizedQueueIterator();
	}
	
	private class RandomizedQueueIterator implements Iterator<Item>{
		private Item[] listIterator;
		private int current = 0;
		
		public RandomizedQueueIterator() {
			listIterator = (Item[]) new Object[N];
			
			for(int i = 0; i < N; i++) {
				listIterator[i] = list[i];
			}
			
			StdRandom.shuffle(listIterator);
		}
		
		public boolean hasNext() { return current < N; }
		
		public void remove() { throw new UnsupportedOperationException("Operation cannot be called"); }
		
		public Item next() {
			if(!hasNext()) {
				throw new NoSuchElementException("There are no more items to return");
			}
			
			Item item = listIterator[current++];
			return item;
		}
	}
	
	private void resize(int capacity) {
		Item[] copy = (Item[]) new Object[capacity];
		for (int i = 0; i < N; i++) {
			copy[i] = list[i];
		}
		
		list = copy;
	}

	public static void main(String[] args) {
		RandomizedQueue<String> queue = new RandomizedQueue<String>();
		queue.enqueue("1");
		queue.enqueue("2");
		queue.enqueue("3");
		queue.enqueue("4");
		queue.enqueue("5");
		queue.enqueue("6");
		
		Iterator<String> it1 = queue.iterator();
		Iterator<String> it2 = queue.iterator();
		Iterator<String> it3 = queue.iterator();
		
		System.out.println("\nIterator 1: ");
		while(it1.hasNext()) {
			System.out.print(it1.next() + ", ");
		}
		
		System.out.println("\nIterator 2: ");
		while(it2.hasNext()) {
			System.out.print(it2.next() + ", ");
		}
		
		System.out.println("\nIterator 3: ");
		while(it3.hasNext()) {
			System.out.print(it3.next() + ", ");
		}
	}
}
