package eight_puzzle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
	private MinPQ<Node> queue;
	
	private int moves;
	private Node gameRoot;
	private boolean solvable;
	
    public Solver(Board initial){
    	if(initial == null){
    		throw new NullPointerException("Argument cannot be null");
    	}
    	
    	moves = 0;
    	
    	gameRoot = new Node(initial, null);
    	
    	queue = new MinPQ<Node>(new BoardComparator());
    	queue.insert(gameRoot);
    	
    	this.solvable = hasSolution();
    	
    	if(isSolvable()){
    		solve();
    	};
    }
    
    private void solve(){
    	Node min = queue.delMin();
    	
    	while(min != null && !min.value.isGoal()){
	    	moves++;
	    	
	    	for(Board neighbor : min.value.neighbors()){
	    		if((min.parent == null || !neighbor.equals(min.parent.value))){
	    			Node nodeChild = new Node(neighbor, min);
	    			min.children.add(nodeChild);
		    		queue.insert(nodeChild);
	    		}
	    	}
	    	
	    	min = queue.delMin();
    	}
    	
		Node child = min;
		
		while(child != null){
			child.solution = true;
			child = child.parent;
		}
    }
    
    private boolean hasSolution(){
    	Board initial = gameRoot.value;
    	Board twin = initial.twin();
    	
    	Node rootOrig = new Node(initial, null);
    	Node rootTwin = new Node(twin, null);
    	
    	MinPQ<Node> queueOrig = new MinPQ<Node>(new BoardComparator());
    	queueOrig.insert(rootOrig);
    	
    	MinPQ<Node> queueTwin = new MinPQ<Node>(new BoardComparator());
    	queueTwin.insert(rootTwin);
    	
    	Node minOrig = queueOrig.delMin();
    	Node minTwin = queueTwin.delMin();
    	
    	List<Board> listOrig = new ArrayList<Board>();
    	List<Board> listTwin = new ArrayList<Board>();
    	
    	listOrig.add(initial);
    	listTwin.add(twin);
    	
    	while((minOrig != null && !minOrig.value.isGoal()) && (minTwin != null && !minTwin.value.isGoal())){
	    	for(Board neighbor : minOrig.value.neighbors()){
	    		if((minOrig.parent == null || !neighbor.equals(minOrig.parent.value)) && rootOrig.search(neighbor) == null){
	    			Node nodeChild = new Node(neighbor, minOrig);
	    			minOrig.children.add(nodeChild);
		    		queueOrig.insert(nodeChild);
		    		
		    		listOrig.add(neighbor);
	    		}
	    	}
	    	
	    	minOrig = queueOrig.delMin();
	    	
	    	for(Board neighbor : minTwin.value.neighbors()){
	    		if((minTwin.parent == null || !neighbor.equals(minTwin.parent.value)) && rootTwin.search(neighbor) == null){
	    			Node nodeChild = new Node(neighbor, minTwin);
	    			minTwin.children.add(nodeChild);
		    		queueTwin.insert(nodeChild);
		    		
		    		listTwin.add(neighbor);
	    		}
	    	}
	    	
	    	minTwin = queueTwin.delMin();
    	}
    	
    	if((minTwin != null && minTwin.value.isGoal())){
    		return false;
    	}
    	
    	if(minOrig == null){
    		return false;
    	}
    	
    	return true;
    }
    
    public boolean isSolvable(){
    	return solvable;
    }
    
    public int moves(){
    	return moves;
    }
    
    public Iterable<Board> solution(){
    	return new Iterable<Board>(){

			@Override
			public Iterator<Board> iterator() {
				return new SolverIterator();
			}
    		
    	};
    }
    
    public static void main(String[] args){
    	 // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    
    class SolverIterator implements Iterator<Board>{
		private int currentIndex = 0;
		private List<Board> boards;
		
		SolverIterator(){
			boards = new ArrayList<Board>();
			
			Node solRoot = gameRoot;
			
			while(solRoot != null && solRoot.solution){
				boards.add(solRoot.value);
				
				boolean sol = false;
				for(Node child : solRoot.children){
					if(child.solution){
						solRoot = child;
						sol = true;
						break;
					}
				}
				
				if(!sol)
					solRoot = null;
			}
		}
		
		
		@Override
		public boolean hasNext() {
			if(currentIndex < boards.size()){
				return true;
			}
			return false;
		}

		@Override
		public Board next() {
			return boards.get(currentIndex++);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
    
    class Node{
    	private Board value;
    	private int level;
    	private Node parent;
    	private List<Node> children;
    	private boolean solution;
    	
    	public Node(Board value, Node parent) {
			this.value = value;
			this.children = new ArrayList<Node>();
			this.parent = parent;
			
			if(this.parent != null){
				level = this.parent.level + 1;
			}
		}
    	
    	public Node search(Board value){
    		if(this.value.equals(value)){
    			return this;
    		}
    		
    		for(Node child : children){
    			Node result = child.search(value);
    			
    			if(result != null)
    				return result;
    		}
    		
    		return null;
    	}
    	
    	@Override
    	public String toString() {
    		return value.toString();
    	}
    }
    
    class BoardComparator implements Comparator<Node>{
		@Override
		public int compare(Node b1, Node b2) {
			Integer h1 = b1.value.hamming() + b1.level;
			Integer h2 = b2.value.hamming() + b2.level;
			
			return h1.compareTo(h2);
		}
    	
    }
}