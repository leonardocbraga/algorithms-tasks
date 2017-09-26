package collinear_points;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class FastCollinearPoints {
	private final List<LineSegment> lines;
	
	public FastCollinearPoints(Point[] points){
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
		
		Arrays.sort(points);
		
		Point[] subarray = null;

		for(int i = 0; i < points.length; i++){
			subarray = copyRemaining(points, i);
			
			Arrays.sort(subarray, points[i].slopeOrder());
			
			int number = 0;
			int minor = 0;
			boolean found = false;
			for(int j = 0; j < subarray.length - 1; j++){
				if(points[i].slopeTo(subarray[j]) == points[i].slopeTo(subarray[j + 1])){
					found = true;
					number++;
					
					if(subarray[j].compareTo(points[i]) > 0) minor++;
				}else if(found){
					if(number >= 2 && number == minor){
						lines.add(new LineSegment(points[i], subarray[j]));
					}
					
					found = false;
					number = 0;
					minor = 0;
				}
				
			}
		}
	}
	
	private Point[] copyRemaining(Point[] array, int index){
		Point[] subarray = new Point[array.length - 1];
		int n = 0;
		
		for(int i = 0; i < array.length; i++){
			if(i != index){
				subarray[n++] = array[i];
			}
		}
		
		return subarray;
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
	    FastCollinearPoints collinear = new FastCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	}
}
