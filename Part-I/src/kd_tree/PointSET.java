package kd_tree;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private final SET<Point2D> tree;

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
            throw new IllegalArgumentException("Argument cannot be null");
        }

        tree.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        return tree.contains(p);
    }

    public void draw() {
        for (Point2D point : tree) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        List<Point2D> points = new ArrayList<Point2D>();

        for (Point2D point : tree) {
            if (rect.contains(point)) {
                points.add(point);
            }
        }

        return points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        if (isEmpty()) {
            return null;
        }

        double minDist = Double.MAX_VALUE;
        double dist = 0;
        Point2D nearest = null;
        for (Point2D point : tree) {
            dist = point.distanceSquaredTo(p);

            if (dist < minDist) {
                minDist = dist;
                nearest = point;
            }
        }

        return nearest;
    }

}