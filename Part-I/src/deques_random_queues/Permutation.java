package deques_random_queues;

import java.util.Iterator;

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
	
	public static void main(String[] args) {
		Integer k = Integer.parseInt(args[0]);
		
		RandomizedQueue<String> queue = new RandomizedQueue<String>();
		
		while (!StdIn.isEmpty()) {
			String s = StdIn.readString();
			
			queue.enqueue(s);
		}
		
		Iterator<String> iterator = queue.iterator();
		
		int i = 0;
		while(iterator.hasNext() && i < k) {
			System.out.println(iterator.next());
			
			i++;
		}
	}
}
