package percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	
	private double meanValue;
	private double stddevValue;
	private double confidenceLow;
	private double confidenceHigh;
	
	public PercolationStats(int N, int T) {
		if(N <= 0 || T <= 0) {
			throw new IllegalArgumentException("N or T should be greater than 0");
		}
		
		double[] fractionArray = new double[T];
		
		Percolation p;
		int i, j;
		int openSites = 0;
		boolean percolates = false;
		
		for(int idx = 0; idx < T; idx++) {
			p = new Percolation(N);
			openSites = 0;
			percolates = false;
			
			while(!percolates) {
				i = StdRandom.uniform(N) + 1;
				j = StdRandom.uniform(N) + 1;
				
				if(!p.isOpen(i, j)) {
					p.open(i, j);
					openSites++;
					
					percolates = p.percolates();
				}
			}
			
			fractionArray[idx] = (double)openSites / (double)(N*N);
		}
		
		meanValue = StdStats.mean(fractionArray);
		stddevValue = StdStats.stddev(fractionArray);
		confidenceLow = meanValue - ((1.96 * stddevValue) / Math.sqrt(T));
		confidenceHigh = meanValue + ((1.96 * stddevValue) / Math.sqrt(T));
	}

	public double mean() {
		return meanValue;
	}

	public double stddev() {
		return stddevValue;
	}

	public double confidenceLo() {
		return confidenceLow;
	}

	public double confidenceHi() {
		return confidenceHigh;
	}

	public static void main(String[] args) {
		int N = Integer.parseInt(args[0]);
		int T = Integer.parseInt(args[1]);
		
		PercolationStats p1 = new PercolationStats(N, T);
		
		System.out.println("mean                    = " + p1.mean());
		System.out.println("stddev                  = " + p1.stddev());
		System.out.println("95% confidence interval = " + p1.confidenceLo() + ", " + p1.confidenceHi());
	}
}
