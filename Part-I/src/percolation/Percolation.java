package percolation;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private final boolean[][] grid;
	private final int N;
	
	private WeightedQuickUnionUF weightedQuickUnionUF;
	private int numberOfOpenSites;
	
	public Percolation(int N) {
        if (N <= 0) {
			throw new IllegalArgumentException("N must be greater than 0");
		}

		grid = new boolean[N][N];
		this.N = N;

		this.numberOfOpenSites = 0;
		weightedQuickUnionUF = new WeightedQuickUnionUF(N * N + 2);
	}

	public boolean isOpen(int i, int j) {
		validateArgs(i, j);

		if (grid[i - 1][j - 1]) {
			return true;
		}

		return false;
	}

	public void open(int i, int j) {
		validateArgs(i, j);

		if (isOpen(i, j)) {
			return;
		}

		int x = i - 1;
		int y = j - 1;
		int p = x * N + y + 1;

		numberOfOpenSites++;
		grid[x][y] = true;

		// very top
		if (x == 0) {
			weightedQuickUnionUF.union(0, p);
		}

		// very bottom
		if (x == (N - 1)) {
			weightedQuickUnionUF.union(p, N * N + 1);
		}

		// left
		if (j > 1 && isOpen(i, j - 1)) {
			weightedQuickUnionUF.union(p, p - 1);
		}

		// right
		if (j < N && isOpen(i, j + 1)) {
			weightedQuickUnionUF.union(p, p + 1);
		}

		// top
		if (i > 1 && isOpen(i - 1, j)) {
			weightedQuickUnionUF.union(p, p - N);
		}

		// bottom
		if (i < N && isOpen(i + 1, j)) {
			weightedQuickUnionUF.union(p, p + N);
		}
	}

	public boolean isFull(int i, int j) {
		validateArgs(i, j);

		if (!isOpen(i, j)) {
			return false;
		}

		int x = i - 1;
		int y = j - 1;
		int p = x * N + y + 1;

		return weightedQuickUnionUF.connected(0, p);
	}

	public boolean percolates() {
		// return isFull(N, N + 1);
		int p = N * N + 1;

		return weightedQuickUnionUF.connected(0, p);
	}

	public int numberOfOpenSites() {
		return numberOfOpenSites;
	}

	public static void main(String[] args) {
		In in = new In(args[0]);      // input file
        int n = in.readInt();         // n-by-n percolation system

        Percolation perc = new Percolation(n);

        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
            
            System.out.println("Is full: " + perc.isFull(i, j));
        }
        
		System.out.println(perc.percolates());
	}

	private void validateArgs(int i, int j) {
		if (i <= 0 || i > N) {
			throw new IndexOutOfBoundsException(
					"i argument must be between 1 and N");
		}

		if (j <= 0 || j > N) {
			throw new IndexOutOfBoundsException(
					"i argument must be between 1 and N");
		}
	}
}
