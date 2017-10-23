package kd_tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private SET<Point2D> tree;

    public PointSET() {
        tree = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return tree.isEmpty();
    }

    public int size() {
        return tree.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Argument cannot be null");
        }

        tree.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Argument cannot be null");
        }

        return tree.contains(p);
    }

    public void draw() {
        for (Point2D point : new PointIterable()) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new NullPointerException("Argument cannot be null");
        }

        return new RangeIterable(rect);
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Argument cannot be null");
        }

        if (isEmpty()) {
            return null;
        }

        double minDist = Double.MAX_VALUE;
        double dist = 0;
        Point2D nearest = null;
        for (Point2D point : new PointIterable()) {
            dist = point.distanceTo(p);

            if (dist < minDist) {
                minDist = dist;
                nearest = point;
            }
        }

        return nearest;
    }

    public static void main(String[] args) {
        StdDraw.square(.2, .8, .1);
        StdDraw.filledSquare(.8, .8, .2);
        StdDraw.circle(.8, .2, .2);

        StdDraw.setPenColor(StdDraw.BOOK_RED);
        StdDraw.setPenRadius(.02);
        StdDraw.arc(.8, .2, .1, 200, 45);

        // draw a blue diamond
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        double[] x = { .1, .2, .3, .2 };
        double[] y = { .2, .3, .2, .1 };
        StdDraw.filledPolygon(x, y);

        // text
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.text(0.2, 0.5, "black text");
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(0.8, 0.8, "white text");
    }

    private class RangeIterable implements Iterable<Point2D> {
        private List<Point2D> points;

        public RangeIterable(RectHV rect) {
            points = new ArrayList<Point2D>();

            for (Point2D point : new PointIterable()) {
                if (rect.contains(point)) {
                    points.add(point);
                }
            }
        }

        @Override
        public Iterator<Point2D> iterator() {
            return points.iterator();
        }

    }

    private class PointIterable implements Iterable<Point2D> {
        @Override
        public Iterator<Point2D> iterator() {
            return tree.iterator();
        }

    }
}