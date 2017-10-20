package eight_puzzle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private int moves;
    private final boolean solvable;

    private final List<Board> boards;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        moves = -1;

        this.boards = getSolution(initial);
        this.solvable = this.boards != null;
    }

    private List<Board> getSolution(Board initial) {
        Board twin = initial.twin();

        Node minOrig = new Node(initial, null);
        Node minTwin = new Node(twin, null);

        MinPQ<Node> queueOrig = new MinPQ<Node>(new BoardComparator());
        MinPQ<Node> queueTwin = new MinPQ<Node>(new BoardComparator());

        while ((minOrig != null && !minOrig.value.isGoal())
                && (minTwin != null && !minTwin.value.isGoal())) {
            for (Board neighbor : minOrig.value.neighbors()) {
                if ((minOrig.parent == null || !neighbor
                        .equals(minOrig.parent.value))) {
                    Node nodeChild = new Node(neighbor, minOrig);

                    queueOrig.insert(nodeChild);
                }
            }

            minOrig = queueOrig.delMin();

            for (Board neighbor : minTwin.value.neighbors()) {
                if ((minTwin.parent == null || !neighbor
                        .equals(minTwin.parent.value))) {
                    Node nodeChild = new Node(neighbor, minTwin);

                    queueTwin.insert(nodeChild);
                }
            }

            minTwin = queueTwin.delMin();
        }

        if ((minTwin != null && minTwin.value.isGoal())) {
            return null;
        }

        if (minOrig == null) {
            return null;
        }

        Node child = minOrig;

        List<Board> solution = new ArrayList<Board>(child.level + 1);

        for (int i = 0; i < child.level + 1; i++) {
            solution.add(null);
        }

        while (child != null) {
            solution.set(child.level, child.value);

            moves++;

            child = child.parent;
        }

        return solution;
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        return boards;
    }

    public static void main(String[] args) {
        // create initial board from file
        // In in = new In(args[0]);
        // int N = in.readInt();
        // int[][] blocks = new int[N][N];
        // for (int i = 0; i < N; i++)
        // for (int j = 0; j < N; j++)
        // blocks[i][j] = in.readInt();
        // Board initial = new Board(blocks);

        // solve the puzzle
        // long time = System.currentTimeMillis();
        // Solver solver = new Solver(initial);
        // StdOut.println(System.currentTimeMillis() - time);

        // print solution to standard output
        // if (!solver.isSolvable())
        // StdOut.println("No solution possible");
        // else {
        // StdOut.println("Minimum number of moves = " + solver.moves());
        // for (Board board : solver.solution())
        // StdOut.println(board);
        // }
    }

    private class Node {
        private final Board value;
        private int level;
        private final Node parent;

        public Node(Board value, Node parent) {
            this.value = value;
            this.parent = parent;

            if (this.parent != null) {
                level = this.parent.level + 1;
            }
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    private class BoardComparator implements Comparator<Node> {

        @Override
        public int compare(Node b1, Node b2) {
            int h1 = b1.value.manhattan() + b1.level;
            int h2 = b2.value.manhattan() + b2.level;

            return (h1 < h2) ? -1 : ((h1 == h2) ? 0 : 1);
        }

    }
}