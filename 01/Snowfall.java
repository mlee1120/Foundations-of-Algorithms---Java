/*
 * This file illustrates Snowfall.java from hw1.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This program determines if there exists a three-day interval that
 * produced more than half of the total snowfall across the n days. o(n)
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class Snowfall {
    /**
     * store cumulative snowfall totals for n consecutive days
     * using a index base data structure ArrayList inorder to perform data traverse more easily
     */
    private final List<Integer> aList;
    /** size of the data (n) */
    private int size;
    /** half of the total snowfall across the n days */
    float half;
    /** determine to print YES or NO */
    Boolean result;

    /**
     * The constructor initializes the fields.
     */
    public Snowfall() {
        aList = new ArrayList<>();
        size = 0;
        half = 0;
        result = false;
    }

    /**
     * This method reads the input from the standard input and stores data into corresponding data structures.
     */
    public void input() {
        Scanner in = new Scanner(System.in);
        size = Integer.parseInt(in.next());
        for (int i = 0; i < size; i++) {
            aList.add(Integer.parseInt(in.next()));
        }
        in.close();
    }

    /**
     * This method traverse the data to see if there exists a three-day interval
     * that produced more than half of the total snowfall across the n days.
     */
    public void process() {
        // calculate half of the total snowfall across the n days
        half = aList.get(size - 1) / 2.0f;
        // check snowfall of every three day
        for (int j = 0; j < size - 2; j++) {
            if (j == 0) {
                if (aList.get(j + 2) > half) {
                    result = true;
                    break;
                }
            } else {
                if (aList.get(j + 2) - aList.get(j - 1) > half) {
                    result = true;
                    break;
                }
            }
        }
    }

    /**
     * This method prints out if there  exists a three-day interval that
     * produced more than half of the total snowfall across the n days.
     */
    public void output() {
        System.out.println(result ? "YES" : "NO");
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        Snowfall hw1 = new Snowfall();
        hw1.input();
        hw1.process();
        hw1.output();
    }
}
