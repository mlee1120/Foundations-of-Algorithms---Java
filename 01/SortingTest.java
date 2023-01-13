/*
 * This file illustrates SortingTest.java from hw1.
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This program is able to perform MergeSort, InsertionSort, BucketSort
 * and record the running time of these methods mean while.
 * I followed the homework instructions to make this program implement
 * those sorting methods with 100, 1000, 10000, and 100000 uniform and
 * Gaussian distribution data. And then I recorded every sorting time.
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class SortingTest {
    /**
     * number of data (random floating point numbers)
     */
    private final int n;
    /**
     * a list storing all the data (would be initialized as an index-based ArrayList)
     */
    private final List<Float> aList;
    /**
     * a list storing all sorted data
     */
    private List<Float> sortedList;
    /**
     * a boolean variable as a decider whether to generate uniform or Gaussian distribution data
     */
    private final boolean uniformOrNot;

    /**
     * The constructor initializes some important variable and generates all unsorted data.
     *
     * @param n the number of data
     * @param b a decider whether to generate uniform or Gaussian distribution data
     */
    public SortingTest(int n, boolean b) {
        this.n = n;
        Random random = new Random();
        // ArrayList is easier to traverse and access a single data with index on O(1)
        aList = new ArrayList<>();
        uniformOrNot = b;
        if (b) {
            for (int i = 0; i < n; i++) {
                // .nextFloat() generates random float number between 0 to 1
                aList.add(random.nextFloat());
            }
        } else {
            for (int j = 0; j < n; j++) {
                // mu = 0.5 and sigma = 1/10000
                aList.add((float) (0.5 + 0.0001 * random.nextGaussian()));
            }
        }
    }

    /**
     * This method performs MergeSort by calling a helper function "divide" first for dividing.
     */
    public void mergeSort() {
        sortedList = new ArrayList<>();
        if (uniformOrNot) System.out.println("\nStart implementing MergeSort with " + n + " uniform distributed data.");
        else System.out.println("\nStart implementing MergeSort with " + n + " Gaussian distributed data.");
        // record the start time of MergeSort
        long startTime = System.nanoTime();
        // call helper function
        sortedList = divide(aList);
        // record the end time of MergeSort
        long endTime = System.nanoTime();
        // calculate and print out the total running time of MergeSort
        System.out.println("Time spent: " + (endTime - startTime) / 1000000000.0 + " seconds.");
    }

    /**
     * This method performs the dividing stage of MergeSort recursively until data are separate to single
     * unit. And then, it calls second helper function "merge" for merging the the data in order.
     *
     * @param divide divided data sort in an Arraylist
     * @return a list of sorted data
     */
    public List<Float> divide(List<Float> divide) {
        if (divide.size() == 1) return divide;
        else {
            int middle = Math.round(divide.size() / 2.0f);
            List<Float> A = divide.subList(0, middle);
            List<Float> B = divide.subList(middle, divide.size());
            List<Float> As = divide(A);
            List<Float> Bs = divide(B);
            return merge(As, Bs);
        }
    }

    /**
     * This method performs the merging stage of MergeSort and return the sorted data.
     *
     * @param As a list of data to be merged
     * @param Bs the other list of data to be merged
     * @return a list of sorted data from both As and Bs
     */
    public List<Float> merge(List<Float> As, List<Float> Bs) {
        // a list to store merged data
        List<Float> finish = new ArrayList<>();
        int indexA = 0, indexB = 0;
        // start merging
        while (indexA < As.size() && indexB < Bs.size()) {
            if (As.get(indexA) <= Bs.get(indexB)) {
                finish.add(As.get(indexA));
                indexA++;
            } else {
                finish.add(Bs.get(indexB));
                indexB++;
            }
        }
        if (indexA < As.size()) {
            for (; indexA < As.size(); indexA++) {
                finish.add(As.get(indexA));
            }
        } else {
            for (; indexB < Bs.size(); indexB++) {
                finish.add(Bs.get(indexB));
            }
        }
        return finish;
    }

    /**
     * This method performs InsertionSort.
     */
    public void insertionSort() {
        sortedList = new ArrayList<>();
        // copy all data from aList first and sort this list
        sortedList.addAll(aList);
        if (uniformOrNot)
            System.out.println("\nStart implementing InsertionSort with " + n + " uniform distributed data.");
        else System.out.println("\nStart implementing InsertionSort with " + n + " Gaussian distributed data.");
        long startTime = System.nanoTime();
        /* InsertionSort: traverse data one by one. if there is a single datum not in
           the right position, swap back one by one until it is at the right position */
        for (int i = 1; i < n; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (sortedList.get(j + 1) < sortedList.get(j)) {
                    float temp = sortedList.get(j + 1);
                    sortedList.set(j + 1, sortedList.get(j));
                    sortedList.set(j, temp);
                }
            }
        }
        long endTime = System.nanoTime();
        System.out.println("Time spent: " + (endTime - startTime) / 1000000000.0 + " seconds.");
    }

    /**
     * This method performs BucketSort.
     */
    public void bucketSort() {
        sortedList = new ArrayList<>();
        if (uniformOrNot)
            System.out.println("\nStart implementing BucketSort with " + n + " uniform distributed data.");
        else System.out.println("\nStart implementing BucketSort with " + n + " Gaussian distributed data.");
        long startTime = System.nanoTime();
        // create the buckets with n LinkedLists
        List<LinkedList<Float>> buckets = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            buckets.add(new LinkedList<>());
        }
        // assign all data to the corresponding buckets
        for (int j = 0; j < n; j++) {
            buckets.get((int) (aList.get(j) / (1.0 / n))).add(aList.get(j));
        }
        // sort data in the same bucket using MergeSort
        for (int k = 0; k < n; k++) {
            if (buckets.get(k).size() > 1) {
                List<Float> temp = new ArrayList<>(buckets.get(k));
                temp = this.mergeSort(temp);
                buckets.get(k).clear();
                buckets.get(k).addAll(temp);
            }
        }
        // add all data in all buckets in order to get a sorted list of data
        for (int l = 0; l < n; l++) {
            sortedList.addAll(buckets.get(l));
        }
        long endTime = System.nanoTime();
        System.out.println("Time spent: " + (endTime - startTime) / 1000000000.0 + " seconds.");
    }

    /**
     * This method is a overloaded function used to implement MergeSort for BucketSort.
     *
     * @param temp a list of data to be sorted from a bucket
     * @return a list of sorted data
     */
    public List<Float> mergeSort(List<Float> temp) {
        return divide(temp);
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        SortingTest hw1Uniform100 = new SortingTest(100, true);
        SortingTest hw1Uniform1000 = new SortingTest(1000, true);
        SortingTest hw1Uniform10000 = new SortingTest(10000, true);
        SortingTest hw1Uniform100000 = new SortingTest(100000, true);
        SortingTest hw1Gaussion100 = new SortingTest(100, false);
        SortingTest hw1Gaussion1000 = new SortingTest(1000, false);
        SortingTest hw1Gaussion10000 = new SortingTest(10000, false);
        SortingTest hw1Gaussion100000 = new SortingTest(100000, false);
        hw1Uniform100.mergeSort();
        hw1Uniform100.insertionSort();
        hw1Uniform100.bucketSort();
        hw1Uniform1000.mergeSort();
        hw1Uniform1000.insertionSort();
        hw1Uniform1000.bucketSort();
        hw1Uniform10000.mergeSort();
        hw1Uniform10000.insertionSort();
        hw1Uniform10000.bucketSort();
        hw1Uniform100000.mergeSort();
        hw1Uniform100000.insertionSort();
        hw1Uniform100000.bucketSort();
        hw1Gaussion100.mergeSort();
        hw1Gaussion100.insertionSort();
        hw1Gaussion100.bucketSort();
        hw1Gaussion1000.mergeSort();
        hw1Gaussion1000.insertionSort();
        hw1Gaussion1000.bucketSort();
        hw1Gaussion10000.mergeSort();
        hw1Gaussion10000.insertionSort();
        hw1Gaussion10000.bucketSort();
        hw1Gaussion100000.mergeSort();
        hw1Gaussion100000.insertionSort();
        hw1Gaussion100000.bucketSort();
    }
}
