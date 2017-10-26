package kd_tree;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        if (root == null) {
            return 0;
        }

        return root.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        if (root == null) {
            root = new Node(p, true, new RectHV(0, 0, 1, 1));
        } else {
            root.insert(p);
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        if (root == null) {
            return false;
        }

        return root.contains(p);
    }

    public void draw() {
        drawNode(root);
    }

    private void drawNode(Node node) {
        if (node == null) {
            return;
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        node.key.draw();

        if (node.vertical) {
            StdDraw.setPenColor(StdDraw.BOOK_RED);
            StdDraw.line(node.key.x(), node.rect.ymin(), node.key.x(),
                    node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BOOK_BLUE);
            StdDraw.line(node.rect.xmin(), node.key.y(), node.rect.xmax(),
                    node.key.y());
        }

        drawNode(node.left);
        drawNode(node.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        List<Point2D> matches = new ArrayList<Point2D>();

        if (!isEmpty()) {
            root.rangeSearch(rect, matches);
        }

        return matches;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        if (isEmpty()) {
            return null;
        }

        return root.nearestSearch(null, p, false).key;
    }

    private class Node {
        private final Point2D key;
        private final boolean vertical;
        private final RectHV rect;

        private Node left;
        private Node right;
        private int size;

        Node(Point2D point, boolean vertical, RectHV rect) {
            this.key = point;
            this.vertical = vertical;
            this.rect = rect;
            this.size = 1;
        }

        boolean insert(Point2D point) {
            if (point.equals(key)) {
                return false;
            }

            boolean inserted = true;

            if (isLeanLeft(point)) {
                if (left == null) {
                    left = new Node(point, !vertical, getLeftRect());
                } else {
                    inserted = left.insert(point);
                }
            } else {
                if (right == null) {
                    right = new Node(point, !vertical, getRightRect());
                } else {
                    inserted = right.insert(point);
                }
            }

            if (inserted) {
                this.size++;
            }

            return inserted;
        }

        private boolean isLeanLeft(Point2D point) {
            double keyValue = 0d;
            double pointValue = 0d;

            if (vertical) {
                pointValue = point.x();
                keyValue = key.x();
            } else {
                pointValue = point.y();
                keyValue = key.y();
            }

            return pointValue < keyValue;
        }

        private RectHV getLeftRect() {
            if (vertical) {
                return new RectHV(rect.xmin(), rect.ymin(), key.x(),
                        rect.ymax());
            } else {
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(),
                        key.y());
            }
        }

        private RectHV getRightRect() {
            if (vertical) {
                return new RectHV(key.x(), rect.ymin(), rect.xmax(),
                        rect.ymax());
            } else {
                return new RectHV(rect.xmin(), key.y(), rect.xmax(),
                        rect.ymax());
            }
        }

        boolean contains(Point2D point) {
            if (key.equals(point)) {
                return true;
            }

            if (isLeanLeft(point)) {
                boolean leftContains = left != null && left.contains(point);

                return leftContains;
            } else {
                boolean rightContains = right != null && right.contains(point);

                return rightContains;
            }
        }

        Node nearestSearch(Node nearestParam, Point2D point, boolean prune) {
            Node nearest = null;
            if (nearestParam == null) {
                nearest = this;
            } else {

                double nearestDist = nearestParam.key.distanceSquaredTo(point);
                if (prune) {
                    double rectDist = this.rect.distanceSquaredTo(point);

                    if (nearestDist <= rectDist) {
                        return nearestParam;
                    }
                }

                double nodeDist = this.key.distanceSquaredTo(point);
                if (nodeDist < nearestDist) {
                    nearest = this;
                } else {
                    nearest = nearestParam;
                }
            }

            if (left == null && right == null) {
                return nearest;
            } else if (left == null) {
                return right.nearestSearch(nearest, point, false);
            } else if (right == null) {
                return left.nearestSearch(nearest, point, false);
            } else {
                if (isLeanLeft(point)) {
                    nearest = left.nearestSearch(nearest, point, false);
                    nearest = right.nearestSearch(nearest, point, true);
                } else {
                    nearest = right.nearestSearch(nearest, point, false);
                    nearest = left.nearestSearch(nearest, point, true);
                }

                return nearest;
            }
        }

        void rangeSearch(RectHV rectParam, List<Point2D> matches) {
            if (!rectParam.intersects(this.rect)) {
                return;
            }

            if (rectParam.contains(this.key)) {
                matches.add(this.key);
            }

            if (left != null) {
                left.rangeSearch(rectParam, matches);
            }

            if (right != null) {
                right.rangeSearch(rectParam, matches);
            }
        }

        int size() {
            return size;
        }

    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));

        System.out.println(tree.nearest(new Point2D(0.89, 0.2)));
        // for (Point2D range : tree.range(new RectHV(0, 0, .6, .8))) {
        // System.out.println(range);
        // }

        // System.out.println(tree.contains(new Point2D(0.9, 0.6)));
        // tree.draw();

        // System.out.println((new RectHV(0.436, 0.582, 0.006, 0.871))
        // .contains(new Point2D(0.522, 0.852)));
    }
}
