package seam_carving;

import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private final Picture picture;
    private int[][] colors;
    private int width;
    private int height;

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new NullPointerException("Argument cannot be null");
        }

        this.picture = picture;
        this.width = this.picture.width();
        this.height = this.picture.height();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                colors[i][j] = this.picture.get(i, j).getRGB();
            }
        }
    }

    public Picture picture() {
        Picture newPic = new Picture(width, height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                newPic.set(i, j, new Color(colors[i][j]));
            }
        }

        return newPic;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1)
            throw new IndexOutOfBoundsException("X is outside the range");

        if (y < 0 || y > height() - 1)
            throw new IndexOutOfBoundsException("Y is outside the range");

        if (x == 0 || x == width() - 1)
            return 1000;

        if (y == 0 || y == height() - 1)
            return 1000;

        Color colorRight = new Color(colors[x + 1][y]);
        Color colorLeft = new Color(colors[x - 1][y]);
        Color colorTop = new Color(colors[x][y - 1]);
        Color colorBottom = new Color(colors[x][y + 1]);

        double deltaX = Math.pow(colorRight.getRed() - colorLeft.getRed(), 2)
                + Math.pow(colorRight.getGreen() - colorLeft.getGreen(), 2)
                + Math.pow(colorRight.getBlue() - colorLeft.getBlue(), 2);

        double deltaY = Math.pow(colorTop.getRed() - colorBottom.getRed(), 2)
                + Math.pow(colorTop.getGreen() - colorBottom.getGreen(), 2)
                + Math.pow(colorTop.getBlue() - colorBottom.getBlue(), 2);

        return Math.sqrt(deltaX + deltaY);
    }

    public int[] findHorizontalSeam() {
        double[][] weights = new double[width][height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                weights[x][y] = Double.POSITIVE_INFINITY;

                if (x == 0)
                    weights[x][y] = 0;
            }
        }

        double energyLeft = 0;
        double energyBottom = 0;
        double energyRight = 0;
        int[][] edge = new int[width][height];

        for (int x = 0; x < width - 1; x++) {
            for (int y = 0; y < height; y++) {
                if (y > 0) {
                    energyLeft = energy(x + 1, y - 1);
                    if (weights[x + 1][y - 1] > weights[x][y] + energyLeft) {
                        weights[x + 1][y - 1] = weights[x][y] + energyLeft;
                        edge[x + 1][y - 1] = y;
                    }
                }

                energyBottom = energy(x + 1, y);
                if (weights[x + 1][y] > weights[x][y] + energyBottom) {
                    weights[x + 1][y] = weights[x][y] + energyBottom;
                    edge[x + 1][y] = y;
                }

                if (y < height - 1) {
                    energyRight = energy(x + 1, y + 1);
                    if (weights[x + 1][y + 1] > weights[x][y] + energyRight) {
                        weights[x + 1][y + 1] = weights[x][y] + energyRight;
                        edge[x + 1][y + 1] = y;
                    }
                }
            }
        }

        double minWeight = Double.POSITIVE_INFINITY;
        int minPos = 0;

        for (int y = 0; y < height; y++) {
            if (minWeight > weights[width - 1][y]) {
                minWeight = weights[width - 1][y];
                minPos = y;
            }
        }

        int[] result = new int[width];
        result[width - 1] = minPos;

        for (int x = width - 2; x >= 0; x--) {
            result[x] = edge[x + 1][result[x + 1]];
        }

        return result;
    }

    public int[] findVerticalSeam() {
        double[][] weights = new double[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                weights[x][y] = Double.POSITIVE_INFINITY;

                if (y == 0)
                    weights[x][y] = 0;
            }
        }

        double energyLeft = 0;
        double energyBottom = 0;
        double energyRight = 0;
        int[][] edge = new int[width][height];

        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width; x++) {
                if (x > 0) {
                    energyLeft = energy(x - 1, y + 1);
                    if (weights[x - 1][y + 1] > weights[x][y] + energyLeft) {
                        weights[x - 1][y + 1] = weights[x][y] + energyLeft;
                        edge[x - 1][y + 1] = x;
                    }
                }

                energyBottom = energy(x, y + 1);
                if (weights[x][y + 1] > weights[x][y] + energyBottom) {
                    weights[x][y + 1] = weights[x][y] + energyBottom;
                    edge[x][y + 1] = x;
                }

                if (x < width - 1) {
                    energyRight = energy(x + 1, y + 1);
                    if (weights[x + 1][y + 1] > weights[x][y] + energyRight) {
                        weights[x + 1][y + 1] = weights[x][y] + energyRight;
                        edge[x + 1][y + 1] = x;
                    }
                }
            }
        }

        double minWeight = Double.POSITIVE_INFINITY;
        int minPos = 0;

        for (int x = 0; x < width; x++) {
            if (minWeight > weights[x][height - 1]) {
                minWeight = weights[x][height - 1];
                minPos = x;
            }
        }

        int[] result = new int[height];
        result[height - 1] = minPos;

        for (int y = height - 2; y >= 0; y--) {
            result[y] = edge[result[y + 1]][y + 1];
        }

        return result;
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new NullPointerException("Argument cannot be null");
        }

        if (height() <= 1) {
            throw new IllegalArgumentException("Height must be greater than 1");
        }

        if (seam.length != width()) {
            throw new IllegalArgumentException("Seam length is wrong");
        }

        validateSeam(seam, false);

        for (int i = 0; i < seam.length; i++) {
            for (int y = seam[i]; y < height - 1; y++) {
                colors[i][y] = colors[i][y + 1];
            }
        }

        height--;
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new NullPointerException("Argument cannot be null");
        }

        if (width() <= 1) {
            throw new IllegalArgumentException("Width must be greater than 1");
        }

        if (seam.length != height()) {
            throw new IllegalArgumentException("Seam length is wrong");
        }

        validateSeam(seam, true);

        for (int i = 0; i < seam.length; i++) {
            for (int x = seam[i]; x < width - 1; x++) {
                colors[x][i] = colors[x + 1][i];
            }
        }

        width--;
    }

    private void validateSeam(int[] seam, boolean vertical) {
        for (int i = 0; i < seam.length; i++) {
            if (vertical && (seam[i] < 0 || seam[i] > width() - 1))
                throw new IllegalArgumentException(
                        "Array argument is not a valid seam");

            if (!vertical && (seam[i] < 0 || seam[i] > height() - 1))
                throw new IllegalArgumentException(
                        "Array argument is not a valid seam");

            if (i > 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException(
                        "Array argument is not a valid seam");
            }
        }
    }

}