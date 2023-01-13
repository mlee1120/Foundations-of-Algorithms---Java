/*
 * This file illustrates Sphere.java from hw2.
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;

/**
 * This program first receives a sequence of data, representing n points on the 3-dimensional integer lattice
 * restricted to the range [-n, n]. And it performs an O(n) algorithm to determine if there exists any sphere
 * centered at the origin having two or more then points on its surface.
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class Sphere {
    /** the number of data (n) */
    private long n;
    /** the list storing the distances (square) of n points to the origin */
    private final List<Long> aList;
    /** the list of buckets used for radixsort */
    private final List<LinkedList<Long>> temp;
    /** the decider for the result (YES or NO) */
    private boolean result;
    private final Scanner in;

    /**
     * The constructor initiates some important fields.
     */
    public Sphere() {
        aList = new ArrayList<>();
        temp = new ArrayList<>();
        in = new Scanner(System.in);
        result = false;
    }

    /**
     * This method reads the input from the standard input and stores data into corresponding data structures.
     */
    public void input() {
        n = Long.parseLong(in.next());
        in.nextLine();
        // x, y, and z axes
        long x, y, z;
        String[] aStringArray;
        for (int i = 0; i < n; i++) {
            aStringArray = in.nextLine().split(" ");
            // distance to the origin = (x^2 + y^2 + z^2)^1/2  (I didn't take the square root for accuracy)
            x = Long.parseLong(aStringArray[0]);
            y = Long.parseLong(aStringArray[1]);
            z = Long.parseLong(aStringArray[2]);
            aList.add(x * x + y * y + z * z);
        }
        in.close();
    }

    /**
     * This method performs radix sort to sort the data in linear time O(n).
     */
    public void radixsort() {
        /*
           We have n inputs and number in range [0, 3n^2] (x^2 + y^2 + z^2 => max = 3n^2)
           take base n => need long_n(3n^2) = 2 + log_n(3) > 2 => therefore 3 digits
         */
        // digits
        int d = 3;
        // index of the list of buckets
        long index;
        for (int i = 1; i <= d; i++) {
            temp.clear();
            // creates empty buckets
            for (int j = 0; j < n; j++) {
                temp.add(new LinkedList<>());
            }
            // radix sort => theta(d(n+r)) = theta(3(n+n)) = theta(n)
            for (int k = 0; k < n; k++) {
                if (i == 1) index = aList.get(k) % (long) Math.pow(n, i);
                else index = aList.get(k) % (long) Math.pow(n, i) / (long) Math.pow(n, i - 1);
                temp.get((int) index).addLast(aList.get(k));
            }
            aList.clear();
            for (int l = 0; l < n; l++) {
                if (temp.get(l).size() != 0) aList.addAll(temp.get(l));
            }
        }
    }

    /**
     * Since the data is sorted, this method checks if there exists any sphere centered at the origin
     * having two or more then points on its surface (by checking if there are duplicates in the list)
     * in linear time O(n) and prints out the result.
     */
    public void output() {
        for (int i = 1; i < n; i++) {
            if (aList.get(i).equals(aList.get(i - 1))) {
                result = true;
                break;
            }
        }
        System.out.println(result ? "YES" : "NO");
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        Sphere hw2 = new Sphere();
        hw2.input();
        hw2.radixsort();
        hw2.output();
    }
}
