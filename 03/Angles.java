/*
 * This file illustrates Angles.java from hw3.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This program first receives a single value n, representing the number
 * of points in a 2-d space, and also the coordinates of those n points.
 * It then has to calculate the number of right triangles that can formed
 * by choosing sets of three points of the n points.
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class Angles {
    /** the number of points */
    private int n;
    /** how many right triangles can be formed */
    private int result;
    /** a list storing n points' coordinates */
    private List<int[]> aList;
    private final Scanner in;

    /**
     * The constructor initiates some important fields.
     */
    public Angles() {
        result = 0;
        aList = new ArrayList<>();
        in = new Scanner(System.in);
    }

    /**
     * This method reads the input from the standard input and stores data into corresponding data structures.
     */
    public void input() {
        n = in.nextInt();
        in.nextLine();
        String[] aStringArray;
        for (int i = 0; i < n; i++) {
            aStringArray = in.nextLine().split(" ");
            aList.add(new int[]{Integer.parseInt(aStringArray[0]), Integer.parseInt(aStringArray[1])});
        }
        in.close();
    }

    /**
     * This method calculates how many right triangles can be formed. It first sorts n points by their x coordinates.
     * After that, starting from the bottom-left point, it generates vectors with the remaining points (only toward
     * Quadrant 1 and 4). Dot product two vector  and get a value. If that value is 0, implying a right triangle is
     * found, there is no need to check the other two angles; If that value > 0, implying the angle is less than 90
     * degrees, check the other two angles; If that value is < 0, meaning the angle is over 90 degrees, there is no
     * need check the other two angles because if an angle of a triangle is over 90 degrees, the other two angles
     * can't be 90 degrees (angles sum of a triangle = 180 degrees).
     */
    public void findRightTriangle() {
        // sort n points by x coordinates
        aList = mergesortX(0, n - 1);

        for (int i = 0; i < n - 2; i++) {
            // generate vectors from one point with other points starting from the bottom-left point
            List<int[]> vectors = new ArrayList<>();
            for (int j = i + 1; j < n; j++) {
                if (j != i) {
                    vectors.add(new int[]{aList.get(j)[0] - aList.get(i)[0], aList.get(j)[1] - aList.get(i)[1]});
                }
            }

            // sort vectors by their slopes
            vectors = mergesortV(vectors, 0, vectors.size() - 1);

            // dot-product two vectors to find right triangles
            for (int k = 0; k < vectors.size() - 1; k++) {
                for (int l = k + 1; l < vectors.size(); l++) {
                    int x1 = vectors.get(k)[0], y1 = vectors.get(k)[1];
                    int x2 = vectors.get(l)[0], y2 = vectors.get(l)[1];
                    int dotProduct = x1 * x2 + y1 * y2;

                    // no further check is required if dotProduct < 0 (angle > 90 degrees)
                    if (dotProduct == 0) result++;
                    else if (dotProduct > 0) {
                        int x3 = x1 - x2, y3 = y1 - y2;
                        if (x1 * x3 + y1 * y3 == 0) result++;
                        else if (x2 * x3 + y2 * y3 == 0) result++;
                    }
                }
            }
        }
    }

    /**
     * This method performs merge-sorting n points by their x coordinates
     *
     * @param start the first index of the list containing points' coordinates
     * @param end   the last index of the list containing points' coordinates
     * @return a list of sorted points
     */
    public List<int[]> mergesortX(int start, int end) {
        List<int[]> temp = new ArrayList<>();
        if (start != end) {
            int midIndex = (start + end) / 2;
            List<int[]> left = mergesortX(start, midIndex);
            List<int[]> right = mergesortX(midIndex + 1, end);
            int leftIndex = 0, rightIndex = 0;
            do {
                if (left.get(leftIndex)[0] < right.get(rightIndex)[0]) {
                    temp.add(left.get(leftIndex));
                    leftIndex++;
                } else if (left.get(leftIndex)[0] > right.get(rightIndex)[0]) {
                    temp.add(right.get(rightIndex));
                    rightIndex++;
                } else {
                    if (left.get(leftIndex)[1] < right.get(rightIndex)[1]) {
                        temp.add(left.get(leftIndex));
                        leftIndex++;
                    } else {
                        temp.add(right.get(rightIndex));
                        rightIndex++;
                    }
                }
            } while (leftIndex < left.size() && rightIndex < right.size());
            if (leftIndex < left.size()) {
                do {
                    temp.add(left.get(leftIndex));
                    leftIndex++;
                } while (leftIndex < left.size());
            } else {
                do {
                    temp.add(right.get(rightIndex));
                    rightIndex++;
                } while (rightIndex < right.size());
            }
        } else temp.add(aList.get(start));
        return temp;
    }

    /**
     * This method performs merge-sorting vectors by their slopes.
     *
     * @param vectors a list of vectors to be sorted
     * @param start   the first index of the list containing vectors
     * @param end     the last index of the list containing vectors
     * @return a list of sorted vectors
     */
    public List<int[]> mergesortV(List<int[]> vectors, int start, int end) {
        List<int[]> temp = new ArrayList<>();
        if (start != end) {
            int midIndex = (start + end) / 2;
            List<int[]> left = mergesortV(vectors, start, midIndex);
            List<int[]> right = mergesortV(vectors, midIndex + 1, end);
            int leftIndex = 0, rightIndex = 0;
            do {
                if (left.get(leftIndex)[0] == 0) {
                    temp.add(left.get(leftIndex));
                    leftIndex++;
                } else if (right.get(rightIndex)[0] == 0) {
                    temp.add(right.get(rightIndex));
                    rightIndex++;
                } else {
                    float slope1 = (float) left.get(leftIndex)[1] / left.get(leftIndex)[0];
                    float slope2 = (float) right.get(rightIndex)[1] / right.get(rightIndex)[0];
                    if (slope1 >= slope2) {
                        temp.add(left.get(leftIndex));
                        leftIndex++;
                    } else {
                        temp.add(right.get(rightIndex));
                        rightIndex++;
                    }
                }
            } while (leftIndex < left.size() && rightIndex < right.size());
            if (leftIndex < left.size()) {
                do {
                    temp.add(left.get(leftIndex));
                    leftIndex++;
                } while (leftIndex < left.size());
            } else {
                do {
                    temp.add(right.get(rightIndex));
                    rightIndex++;
                } while (rightIndex < right.size());
            }
        } else temp.add(vectors.get(start));
        return temp;
    }

    /**
     * This method prints out how many right triangles can be formed.
     */
    public void output() {
        System.out.println(result);
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        Angles hw3 = new Angles();
        hw3.input();
        hw3.findRightTriangle();
        hw3.output();
    }
}
