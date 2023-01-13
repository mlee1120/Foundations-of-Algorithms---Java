/*
 * This file illustrates Jobs.java from hw4.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This program first receives a single value n, representing the number
 * of intervals (jobs), and also the starting times, the finishing times,
 * and the employers of all intervals.
 * It then has to calculate and print out the maximum number of compatible
 * jobs that you can complete while keeping both employers satisfied
 * (alternate doing jobs for the employers) with time complexity O(nlogn)
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class Jobs {
    /** the number of intervals (jobs) */
    private int n;
    /** the maximum number of compatible jobs that you can complete while keeping both employers satisfied */
    private int result;
    /** a list storing all intervals' starting times, finishing times, and employers */
    private List<int[]> aList;
    private final Scanner in;

    /**
     * The constructor initializes some important fields.
     */
    public Jobs() {
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
            aList.add(new int[3]);
            aStringArray = in.nextLine().split(" ");
            aList.get(i)[0] = Integer.parseInt(aStringArray[0]);
            aList.get(i)[1] = Integer.parseInt(aStringArray[1]);
            aList.get(i)[2] = Integer.parseInt(aStringArray[2]);
        }
        in.close();
    }

    /**
     * This method schedules and determines the maximum number of compatible
     * jobs that you can complete while keeping both employers satisfied with
     * a greedy algorithm by choosing the interval with the earliest finishing
     * time, which requires sorting the intervals first.
     */
    public void schedule() {
        // sorts the intervals     mergesort => O(nlogn)
        aList = this.mergeSort(0, n - 1);
        int lastFinishingTime = 0;
        // sets it to random number rather than 0 and 1 because there is no last employer at first
        int lastEmployer = 2;
        // traverses all intervals for scheduling
        for (int i = 0; i < n; i++) {
            if (aList.get(i)[0] >= lastFinishingTime && aList.get(i)[2] != lastEmployer) {
                result++;
                lastFinishingTime = aList.get(i)[1];
                lastEmployer = aList.get(i)[2];
            }
        }
    }

    /**
     * This method sorts the intervals by mergesort.
     *
     * @param start the index of the first interval in the array
     * @param end   the index of the last interval in the array
     * @return a list of sorted intervals
     */
    public List<int[]> mergeSort(int start, int end) {
        List<int[]> temp = new ArrayList<>();
        if (start != end) {
            int middle = (start + end) / 2;
            List<int[]> left = this.mergeSort(start, middle);
            List<int[]> right = this.mergeSort(middle + 1, end);
            int leftIndex = 0, rightIndex = 0;
            while (leftIndex < left.size() && rightIndex < right.size()) {
                if (left.get(leftIndex)[1] < right.get(rightIndex)[1]) {
                    temp.add(left.get(leftIndex));
                    leftIndex++;
                } else if (left.get(leftIndex)[1] > right.get(rightIndex)[1]) {
                    temp.add(right.get(rightIndex));
                    rightIndex++;
                } else {
                    if (left.get(leftIndex)[0] >= right.get(rightIndex)[0]) {
                        temp.add(left.get(leftIndex));
                        leftIndex++;
                    } else if (left.get(leftIndex)[0] < right.get(rightIndex)[0]) {
                        temp.add(right.get(rightIndex));
                        rightIndex++;
                    }
                }
            }
            while (leftIndex < left.size()) {
                temp.add(left.get(leftIndex));
                leftIndex++;
            }
            while (rightIndex < right.size()) {
                temp.add(right.get(rightIndex));
                rightIndex++;
            }
        } else temp.add(aList.get(start));
        return temp;
    }

    /**
     * This method prints out the maximum number of compatible jobs
     * that you can complete while keeping both employers satisfied.
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
        Jobs hw4 = new Jobs();
        hw4.input();
        hw4.schedule();
        hw4.output();
    }
}
