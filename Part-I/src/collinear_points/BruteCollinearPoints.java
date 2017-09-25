package collinear_points;

import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class BruteCollinearPoints {
	private List<LineSegment> lines;
	
	public BruteCollinearPoints(Point[] points){
		if(points == null){
			throw new NullPointerException("Argument is null");
		}
		
		for(int i = 0; i < points.length; i++){
			if(points[i] == null){
				throw new NullPointerException("An element in array argument is null");
			}
		}
		
		for(int i = 0; i < points.length; i++){
			for(int j = 0; j < points.length; j++){
				if(i != j && points[i].compareTo(points[j]) == 0){
					throw new IllegalArgumentException("There is a repeated point in the array");
				}
			}
		}
		
		lines = new ArrayList<LineSegment>();
		
		for(int i = 0; i < points.length; i++){
			for(int j = i + 1; j < points.length; j++){				
				for(int k = j + 1; k < points.length; k++){					
					for(int l = k + 1; l < points.length; l++){
						if(collinear(points[i], points[j], points[k], points[l])){
							lines.add(new LineSegment(points[i], points[l]));
						}
					}
				}
			}
		}
	}
	
	private boolean collinear(Point p, Point q, Point r, Point s){
		double slopeQ = p.slopeTo(q);
		double slopeR = p.slopeTo(r);
		double slopeS = p.slopeTo(s);
		
		return slopeQ == slopeR && slopeQ == slopeS;
	}
	   
	public int numberOfSegments(){
		return lines.size();
	}
	   
	public LineSegment[] segments(){
		return lines.toArray(new LineSegment[lines.size()]);
	}
	
	public static void main(String[] args) {

	    // read the N points from a file
	    In in = new In(args[0]);
	    int N = in.readInt();
	    Point[] points = new Point[N];
	    for (int i = 0; i < N; i++) {
	        int x = in.readInt();
	        int y = in.readInt();
	        points[i] = new Point(x, y);
	    }

	    // draw the points
	    StdDraw.show(0);
	    StdDraw.setXscale(0, 32768);
	    StdDraw.setYscale(0, 32768);
	    for (Point p : points) {
	        p.draw();
	    }
	    StdDraw.show();

	    // print and draw the line segments
	    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	}
}
