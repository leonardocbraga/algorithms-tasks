package kd_tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;

    public KdTree() {

    }

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
            throw new NullPointerException("Argument cannot be null");
        }

        if (root == null) {
            root = new Node(p, true, new RectHV(0, 0, 1, 1));
        } else {
            root.insert(p);
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Argument cannot be null");
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
            throw new NullPointerException("Argument cannot be null");
        }

        List<Point2D> matches = new ArrayList<Point2D>();
        rangeSearch(root, rect, matches);

        return new PointIterable(matches);
    }

    private void rangeSearch(Node node, RectHV rect, List<Point2D> matches) {
        if (node == null) {
            return;
        }

        if (!rect.intersects(node.rect)) {
            return;
        }

        if (rect.contains(node.key)) {
            matches.add(node.key);
        }

        rangeSearch(node.left, rect, matches);
        rangeSearch(node.right, rect, matches);
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new NullPointerException("Argument cannot be null");
        }

        if (isEmpty()) {
            return null;
        }

        return nearestSearch(root, null, p).key;
    }

    private Node nearestSearch(Node node, Node nearestParam, Point2D point) {
        Node nearest = null;
        if (nearestParam == null) {
            nearest = node;
        } else {
            double nearestDist = nearestParam.key.distanceTo(point);
            double rectDist = node.rect.distanceTo(point);

            if (nearestDist < rectDist) {
                return nearestParam;
            }

            double nodeDist = node.key.distanceTo(point);
            if (nodeDist < nearestDist) {
                nearest = node;
            } else {
                nearest = nearestParam;
            }

        }

        if (node.left == null && node.right == null) {
            return nearest;
        } else if (node.left == null) {
            return nearestSearch(node.right, nearest, point);
        } else if (node.right == null) {
            return nearestSearch(node.left, nearest, point);
        } else {
            if (node.vertical) {
                if (point.x() > node.key.x()) {
                    nearest = nearestSearch(node.right, nearest, point);
                    nearest = nearestSearch(node.left, nearest, point);
                } else {
                    nearest = nearestSearch(node.left, nearest, point);
                    nearest = nearestSearch(node.right, nearest, point);
                }
            } else {
                if (point.y() > node.key.y()) {
                    nearest = nearestSearch(node.right, nearest, point);
                    nearest = nearestSearch(node.left, nearest, point);
                } else {
                    nearest = nearestSearch(node.left, nearest, point);
                    nearest = nearestSearch(node.right, nearest, point);
                }
            }

            return nearest;
        }
    }

    private class Node {
        private Point2D key;
        private Node left;
        private Node right;
        private boolean vertical;
        private RectHV rect;
        private int size;

        Node(Point2D point, boolean vertical, RectHV rect) {
            this.key = point;
            this.vertical = vertical;
            this.rect = rect;
            this.size = 1;
        }

        void insert(Point2D point) {
            if (point.equals(key)) {
                return;
            }

            this.size++;

            double keyValue = 0d;
            double pointValue = 0d;
            RectHV rectChild = null;

            if (vertical) {
                pointValue = point.x();
                keyValue = key.x();
            } else {
                pointValue = point.y();
                keyValue = key.y();
            }

            if (pointValue < keyValue) {
                if (left == null) {
                    if (vertical) {
                        rectChild = new RectHV(rect.xmin(), rect.ymin(),
                                key.x(), rect.ymax());
                    } else {
                        rectChild = new RectHV(rect.xmin(), rect.ymin(),
                                rect.xmax(), key.y());
                    }

                    left = new Node(point, !vertical, rectChild);
                } else {
                    left.insert(point);
                }
            } else {
                if (right == null) {
                    if (vertical) {
                        rectChild = new RectHV(key.x(), rect.ymin(),
                                rect.xmax(), rect.ymax());
                    } else {
                        rectChild = new RectHV(rect.xmin(), key.y(),
                                rect.xmax(), rect.ymax());
                    }

                    right = new Node(point, !vertical, rectChild);
                } else {
                    right.insert(point);
                }
            }
        }

        boolean contains(Point2D point) {
            if (key.equals(point)) {
                return true;
            }

            boolean rightContains = false;
            boolean leftContains = false;

            if (vertical) {
                if (point.x() > key.x()) {
                    if (right != null) {
                        rightContains = right.contains(point);
                    }

                    if (rightContains) {
                        return true;
                    } else if (left != null) {
                        return left.contains(point);
                    } else {
                        return false;
                    }
                } else {
                    if (left != null) {
                        leftContains = left.contains(point);
                    }

                    if (leftContains) {
                        return true;
                    } else if (right != null) {
                        return right.contains(point);
                    } else {
                        return false;
                    }
                }
            } else {
                if (point.y() > key.y()) {
                    if (right != null) {
                        rightContains = right.contains(point);
                    }

                    if (rightContains) {
                        return true;
                    } else if (left != null) {
                        return left.contains(point);
                    } else {
                        return false;
                    }
                } else {
                    if (left != null) {
                        leftContains = left.contains(point);
                    }

                    if (leftContains) {
                        return true;
                    } else if (right != null) {
                        return right.contains(point);
                    } else {
                        return false;
                    }
                }
            }
        }

        int size() {
            return size;
        }

    }

    private class PointIterable implements Iterable<Point2D> {
        private List<Point2D> points;

        PointIterable(List<Point2D> points) {
            this.points = points;
        }

        @Override
        public Iterator<Point2D> iterator() {
            return this.points.iterator();
        }

    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));

        for (Point2D range : tree.range(new RectHV(0, 0, .6, .8))) {
            // System.out.println(range);
        }

        // System.out.println(tree.contains(new Point2D(0.9, 0.6)));
        // tree.draw();

        System.out.println((new RectHV(0.436, 0.582, 0.006, 0.871))
                .contains(new Point2D(0.522, 0.852)));
    }
}
