package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {
	
	private int[][] grid;
	private int N;
	private WeightedQuickUnionUF weightedQuickUnionUF;
	
	public Percolation(int N) {
		if(N <= 0) {
			throw new IllegalArgumentException("N must be greater than 0");
		}
		
		grid = new int[N][N];
		this.N = N;
		
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				grid[i][j] = 0;
			}
		}
		
		weightedQuickUnionUF = new WeightedQuickUnionUF(N*N + 2);
	}
	
	public boolean isOpen(int i, int j) {
		validateArgs(i, j);
		
		if(grid[i - 1][j - 1] == 1) {
			return true;
		}
		
		return false;
	}
	
	public void open(int i, int j) {
		validateArgs(i, j);
		
		if(isOpen(i, j)) {
			return;
		}
		
		int x = i - 1;
		int y = j - 1;
		int p = x * N + y + 1;
		
		grid[x][y] = 1;
		
		//very top
		if(x == 0) {
			weightedQuickUnionUF.union(0, p);
		}
		
		//very bottom
		if(x == (N - 1)) {
			weightedQuickUnionUF.union(p, N * N + 1);
		}
		
		//left
		if(j > 1 && isOpen(i, j - 1)) {
			weightedQuickUnionUF.union(p, p - 1);
		}
		
		//right
		if(j < N && isOpen(i, j + 1)) {
			weightedQuickUnionUF.union(p, p + 1);
		}
		
		//top
		if(i > 1 && isOpen(i - 1, j)) {
			weightedQuickUnionUF.union(p, p - N);
		}
		
		//bottom
		if(i < N && isOpen(i + 1, j)) {
			weightedQuickUnionUF.union(p, p + N);
		}
	}
	  
	public boolean isFull(int i, int j) {
		validateArgs(i, j);
		
		if(!isOpen(i, j)) {
			return false;
		}
		
		int x = i - 1;
		int y = j - 1;
		int p = x * N + y + 1;
		
		return weightedQuickUnionUF.connected(0, p);
	}
	   
	public boolean percolates() {
		//return isFull(N, N + 1);
		int p = N*N + 1;
		
		return weightedQuickUnionUF.connected(0, p);
	}
	
	public static void main(String[] args) {
		Percolation p = new Percolation(4);
		p.open(1, 1);
		p.open(2, 2);
		p.open(2, 3);
		p.open(2, 4);
		p.open(3, 4);
		p.open(4, 4);
		p.open(4, 3);
		
		//p.print();
		
		System.out.println(p.percolates());
	}
	
	private void validateArgs(int i, int j) {
		if(i <= 0 || i > N) {
			throw new IndexOutOfBoundsException("i argument must be between 1 and N");
		}
		
		if(j <= 0 || j > N) {
			throw new IndexOutOfBoundsException("i argument must be between 1 and N");
		}
	}
}
