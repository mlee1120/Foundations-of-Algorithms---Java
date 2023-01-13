/*
 * This file illustrates PickHalf.java from hw5.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * This program first receives an even number (n) of cards and all numbers ranged
 * from [1, n] (every number appears exactly twice) written on the cards.
 * It then has to determine whether it is possible to select n/2 cards such that
 * each number appears exactly once on the selected cards on O(n).
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class PickHalf {
    /** the number of cards */
    private int n;
    /** the result */
    private boolean result;
    /** a list storing all Numbers */
    private final List<Number> numbers;
    private final Scanner in;

    /**
     * The constructor initializes some important fields.
     */
    public PickHalf() {
        result = true;
        numbers = new ArrayList<>();
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
            numbers.add(new Number(i));
        }
        for (int j = 0; j < n; j++) {
            aStringArray = in.nextLine().split(" ");
            int first = Integer.parseInt(aStringArray[0]) - 1;
            int second = Integer.parseInt(aStringArray[1]) - 1;
            numbers.get(first).neighbor.add(numbers.get(second));
            numbers.get(second).neighbor.add(numbers.get(first));
        }
        // sorts all neighbors in numerical order (DFS order)
        for (int k = 0; k < n; k++) {
            Collections.sort(numbers.get(k).neighbor);
        }
        in.close();
    }

    /**
     * This method determines whether it is possible to select n/2 cards such
     * that each number appears exactly once on the selected cards via DFS.
     * If every connected component has even number of vertices, Yes; Else, No.
     */
    public void check() {
        // an auxiliary variable used to count the number of vertices in every connected component
        int numberOfVisitedVertices;
        for (int i = 0; i < n; i++) {
            numberOfVisitedVertices = 0;
            if (!numbers.get(i).visited) {
                numberOfVisitedVertices = DFS(numbers.get(i), numberOfVisitedVertices + 1);
                if (numberOfVisitedVertices % 2 == 1) {
                    result = false;
                    break;
                }
            }
        }
    }

    /**
     * This method perform DFS on the graph recursively.
     *
     * @param currentNumber             currently visited card (Vertex)
     * @param numberOfVisitedVertices how many cards (vertices) visited now in current connected component
     * @return the number of cards visited in this recursive step
     */
    public int DFS(Number currentNumber, int numberOfVisitedVertices) {
        currentNumber.visited = true;
        if (!currentNumber.neighbor.get(0).visited) {
            numberOfVisitedVertices = DFS(currentNumber.neighbor.get(0), numberOfVisitedVertices + 1);
        }
        if (!currentNumber.neighbor.get(1).visited) {
            numberOfVisitedVertices = DFS(currentNumber.neighbor.get(1), numberOfVisitedVertices + 1);
        }
        return numberOfVisitedVertices;
    }

    /**
     * This method prints out the result.
     */
    public void output() {
        if (result) System.out.println("YES");
        else System.out.println("NO");
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        PickHalf hw5 = new PickHalf();
        hw5.input();
        hw5.check();
        hw5.output();
    }
}

/**
 * This is an auxiliary class use to represent Numbers (vertices).
 * Every Number has two neighbors. (might be duplicated)
 */
class Number implements Comparable<Number> {
    /** is this Number visited or not */
    protected boolean visited;
    /** this Number's order (for DFS) */
    protected int order;
    /** a list of neighbors */
    protected List<Number> neighbor;

    /**
     * The constructor initializes some important fields.
     *
     * @param order this Number's order (for DFS)
     */
    public Number(int order) {
        visited = false;
        this.order = order;
        neighbor = new ArrayList<>();
    }

    /**
     * This method helps determining the natural order of Number.
     *
     * @param c a Number to be compared with this Number
     * @return two Numbers' order
     */
    @Override
    public int compareTo(Number c) {
        return this.order - c.order;
    }
}