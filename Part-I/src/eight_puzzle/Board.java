package eight_puzzle;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private int[][] blocks;

    private int hamming;
    private int manhattan;
    private final int n;
    private int blankPosition;

    public Board(int[][] blocksParam) {
        this.n = blocksParam.length;

        int zeroPos = -1;

        this.blocks = new int[n][n];
        for (int i = 0; i < blocksParam.length; i++) {
            for (int j = 0; j < blocksParam.length; j++) {
                this.blocks[i][j] = blocksParam[i][j];

                if (this.blocks[i][j] == 0) {
                    zeroPos = i * n + j;
                }
            }
        }

        blankPosition = zeroPos;
        hamming = 0;
        manhattan = 0;

        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks.length; j++) {
                hamming += calculateHamming(i, j);
                manhattan += calculateManhattan(i, j);
            }
        }
    }

    private int calculateHamming(int i, int j) {
        if (this.blocks[i][j] != 0 && this.blocks[i][j] != ((i * n) + j + 1)) {
            return 1;
        }

        return 0;
    }

    private int calculateManhattan(int i, int j) {
        if (this.blocks[i][j] != 0) {
            int linha = (int) Math.ceil((double) this.blocks[i][j] / n);
            int coluna = (this.blocks[i][j] % n == 0 ? n
                    : (this.blocks[i][j] % n));

            return Math.abs(linha - i - 1) + Math.abs(coluna - j - 1);
        }

        return 0;
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    private Board twinRandom() {
        Board blocksTwin = new Board(blocks);

        boolean found = false;

        while (!found) {
            int i1 = StdRandom.uniform(n);
            int j1 = StdRandom.uniform(n);

            int i2 = StdRandom.uniform(n);
            int j2 = StdRandom.uniform(n);

            if ((i1 != i2 || j1 != j2) && blocksTwin.blocks[i1][j1] != 0
                    && blocksTwin.blocks[i2][j2] != 0) {
                blocksTwin.exchange(i1, j1, i2, j2);
                found = true;
            }
        }

        return blocksTwin;
    }

    public Board twin() {
        int position1 = 0;
        int position2 = 0;

        for (int i1 = 0; i1 < blocks.length; i1++) {
            for (int j1 = 0; j1 < blocks.length; j1++) {
                for (int i2 = 0; i2 < blocks.length; i2++) {
                    for (int j2 = 0; j2 < blocks.length; j2++) {
                        if ((i1 != i2 || j1 != j2) && blocks[i1][j1] != 0
                                && blocks[i2][j2] != 0) {
                            position1 = i1 * n + j1;
                            position2 = i2 * n + j2;

                            if ((position1 < position2 && blocks[i1][j1] > blocks[i2][j2])
                                    || (position1 > position2 && blocks[i1][j1] < blocks[i2][j2])) {
                                Board blocksTwin = new Board(blocks);
                                blocksTwin.exchange(i1, j1, i2, j2);
                                return blocksTwin;
                            }
                        }
                    }
                }

            }
        }

        return twinRandom();
    }

    private void exchange(int i1, int j1, int i2, int j2) {
        this.hamming -= (calculateHamming(i1, j1) + calculateHamming(i2, j2));
        this.manhattan -= (calculateManhattan(i1, j1) + calculateManhattan(i2,
                j2));

        int aux = this.blocks[i1][j1];
        this.blocks[i1][j1] = this.blocks[i2][j2];
        this.blocks[i2][j2] = aux;

        if (this.blocks[i1][j1] == 0) {
            blankPosition = i1 * n + j1;
        } else if (this.blocks[i2][j2] == 0) {
            blankPosition = i2 * n + j2;
        }

        this.hamming += (calculateHamming(i1, j1) + calculateHamming(i2, j2));
        this.manhattan += (calculateManhattan(i1, j1) + calculateManhattan(i2,
                j2));
    }

    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }

        if (y.getClass() != this.getClass()) {
            return false;
        }

        Board other = (Board) y;

        if (n != other.blocks.length) {
            return false;
        }

        for (int i = 0; i < blocks.length; i++) {
            if (n != other.blocks[i].length) {
                return false;
            }

            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] != other.blocks[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public Iterable<Board> neighbors() {
        int i = (int) Math.floor((double) blankPosition / n);
        int j = (blankPosition % n);

        List<Board> neighbors = new ArrayList<Board>();

        if (i > 0) {
            Board newboard = new Board(blocks);
            newboard.exchange(i, j, i - 1, j);
            neighbors.add(newboard);
        }

        if (i < n - 1) {
            Board newboard = new Board(blocks);
            newboard.exchange(i, j, i + 1, j);
            neighbors.add(newboard);
        }

        if (j > 0) {
            Board newboard = new Board(blocks);
            newboard.exchange(i, j, i, j - 1);
            neighbors.add(newboard);
        }

        if (j < n - 1) {
            Board newboard = new Board(blocks);
            newboard.exchange(i, j, i, j + 1);
            neighbors.add(newboard);
        }

        return neighbors;
    }

    public String toString() {
        StringBuilder result = new StringBuilder(n + "");
        for (int i = 0; i < blocks.length; i++) {
            result.append("\n");
            for (int j = 0; j < blocks.length; j++) {
                result.append(" " + (this.blocks[i][j]));
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        int[][] b = { { 8, 1, 3 }, { 4, 2, 0 }, { 7, 6, 5 } };
        // int[][] b = {{1, 2, 0}, {4, 5, 6}, {7, 8, 3}};

        // System.out.println((new Board(b)).manhattan());
        // System.out.println((new Board(b)).hamming());

        for (Board board : (new Board(b)).neighbors()) {
            System.out.println(board);
        }
    }
}