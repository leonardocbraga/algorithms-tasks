package collinear_points;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class FastCollinearPoints {
	private final List<LineSegment> lines;

	public FastCollinearPoints(Point[] pointsParam){
		if(pointsParam == null){
			throw new IllegalArgumentException("Argument is null");
		}
		
		Point[] points = new Point[pointsParam.length];
		for(int i = 0; i < pointsParam.length; i++){
			if(pointsParam[i] == null){
				throw new IllegalArgumentException("An element in array argument is null");
			}
			
			points[i] = pointsParam[i];
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
		Point current = null;
		
		for(int i = 0; i < points.length; i++){
			current = points[i];
			subarray = copyRemaining(points, i);
			
			Arrays.sort(subarray, current.slopeOrder());
			
			double[] slopes = new double[subarray.length];
			for (int j = 0; j < slopes.length; j++) {
				slopes[j] = current.slopeTo(subarray[j]);
			}
			
			int j = 0;
			while(j < subarray.length){
				
				int c = 0;
				while(j < subarray.length - 1 && current.slopeTo(subarray[j]) == current.slopeTo(subarray[j + 1])){
					j++;
					c++;
				}
				
				Point nextPoint = subarray[j - c];
				Point lastPoint = subarray[j];
				
				if(c >= 2 && current.compareTo(nextPoint) < 0){
					lines.add(new LineSegment(current, lastPoint));
				}
				
				j++;
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
	    LineSegment[] segments = collinear.segments();
	    System.out.println(segments.length);
	    
	    for (LineSegment segment : segments) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    
	    StdDraw.show();
	}
}
