/*
 * This file illustrates HowManyMST.java from hw5.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This program first receives:
 * 1. a single value n, representing the number of vertices in the graph
 * 2. a single value m, representing the number of edges in the graph
 * 3. All edges' information (vertices of both ends and cost)
 * It then has to determine how many minimum spanning trees the graph has.
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class HowManyMST {
    /** the number of vertices */
    private int n;
    /** the number of edges */
    private int m;
    /** the times of two choices encountered during Kruskal's Algorithm */
    private int numberOfDoubleChoices;
    /** a list of all edges */
    private List<int[]> edges;
    /** the BOSS array for Kruskal's Algorithm */
    private final List<Integer> BOSS;
    /** the BOSS array for Kruskal's Algorithm */
    private final List<Integer> SIZE;
    /** the BOSS array for Kruskal's Algorithm */
    private final List<MyLinkedList> SET;
    private final Scanner in;

    /**
     * The constructor initializes some important fields.
     */
    public HowManyMST() {
        numberOfDoubleChoices = 0;
        edges = new ArrayList<>();
        BOSS = new ArrayList<>();
        SIZE = new ArrayList<>();
        SET = new ArrayList<>();
        in = new Scanner(System.in);
    }

    /**
     * This method reads the input from the standard input and stores data into corresponding data structures.
     */
    public void input() {
        n = in.nextInt();
        m = in.nextInt();
        in.nextLine();
        String[] aStringArray;
        int v1, v2, cost;
        for (int i = 0; i < m; i++) {
            aStringArray = in.nextLine().split(" ");
            v1 = Integer.parseInt(aStringArray[0]);
            v2 = Integer.parseInt(aStringArray[1]);
            cost = Integer.parseInt(aStringArray[2]);
            edges.add(new int[]{v1, v2, cost});
        }
        in.close();
    }

    /**
     * This method performs Kruskal's Algorithm to find MSTs.
     */
    public void findMST() {
        // sorting the edges with mergesort
        edges = mergeSort(0, m - 1);
        // initializes Union-Find Data Structure.
        initializeUF();
        // includes or excludes edges to MST
        int vertex1, vertex2, vertex3, vertex4, cost1, cost2, treeSize = 0;
        LinkedNode updateBoss;
        for (int i = 0; i < m; i++) {
            if (treeSize == n) break;
            vertex1 = edges.get(i)[0];
            vertex2 = edges.get(i)[1];
            // checks if there are multiple valid choices
            if (i < m - 1) {
                vertex3 = edges.get(i + 1)[0];
                vertex4 = edges.get(i + 1)[1];
                cost1 = edges.get(i)[2];
                cost2 = edges.get(i + 1)[2];
                if (cost1 == cost2) {
                    if (vertex1 == vertex3) {
                        if (BOSS.get(vertex2).equals(BOSS.get(vertex4))) numberOfDoubleChoices++;
                    } else if (vertex1 == vertex4) {
                        if (BOSS.get(vertex2).equals(BOSS.get(vertex3))) numberOfDoubleChoices++;
                    } else if (vertex2 == vertex3) {
                        if (BOSS.get(vertex1).equals(BOSS.get(vertex4))) numberOfDoubleChoices++;
                    } else if (vertex2 == vertex4) {
                        if (BOSS.get(vertex1).equals(BOSS.get(vertex3))) numberOfDoubleChoices++;
                    }
                }
            }

            // if won't form a cycle
            if (!BOSS.get(vertex1).equals(BOSS.get(vertex2))) {
                // Union operations (update the component with smaller size)
                if (SIZE.get(vertex1) >= SIZE.get(vertex2)) {
                    updateBoss = SET.get(vertex2).front;
                    // update BOSS (dominated term)
                    do {
                        BOSS.set(updateBoss.name, BOSS.get(vertex1));
                        updateBoss = updateBoss.next;
                    } while (updateBoss != null);
                    treeSize = SIZE.get(vertex1) + SIZE.get(vertex2);
                    SIZE.set(vertex1, treeSize);
                    SET.get(vertex1).add(SET.get(vertex2));
                } else {
                    updateBoss = SET.get(vertex1).front;
                    do {
                        BOSS.set(updateBoss.name, BOSS.get(vertex2));
                        updateBoss = updateBoss.next;
                    } while (updateBoss != null);
                    treeSize = SIZE.get(vertex1) + SIZE.get(vertex2);
                    SIZE.set(vertex2, treeSize);
                    SET.get(vertex2).add(SET.get(vertex1));
                }
            }
        }

    }

    /**
     * This method performs mergesort for sorting all edges.
     *
     * @param start the index of the first element
     * @param end   the index of the last element
     * @return a list of sorted edges
     */
    public List<int[]> mergeSort(int start, int end) {
        List<int[]> temp = new ArrayList<>();
        if (start != end) {
            int middle = (start + end) / 2;
            List<int[]> left = mergeSort(start, middle);
            List<int[]> right = mergeSort(middle + 1, end);
            int leftIndex = 0, rightIndex = 0;
            do {
                if (left.get(leftIndex)[2] <= right.get(rightIndex)[2]) {
                    temp.add(left.get(leftIndex));
                    leftIndex++;
                } else {
                    temp.add(right.get(rightIndex));
                    rightIndex++;
                }
            } while (leftIndex < left.size() && rightIndex < right.size());

            while (leftIndex < left.size()) {
                temp.add(left.get(leftIndex));
                leftIndex++;
            }
            while (rightIndex < right.size()) {
                temp.add(right.get(rightIndex));
                rightIndex++;
            }
        } else temp.add(edges.get(start));
        return temp;
    }

    /**
     * This method initializes the Union-Find Data Structure.
     */
    public void initializeUF() {
        for (int i = 0; i < n; i++) {
            BOSS.add(i);
            SIZE.add(1);
            SET.add(new MyLinkedList(i));
        }
    }

    /**
     * This method prints out the result.
     */
    public void output() {
        System.out.println((int) Math.pow(2, numberOfDoubleChoices));
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        HowManyMST hw5 = new HowManyMST();
        hw5.input();
        hw5.findMST();
        hw5.output();
    }

}

/**
 * An auxiliary class representing a LinkedNode for LinkedList.
 */
class LinkedNode {
    /** this LinkedNode's name (index) */
    protected int name;
    /** a pointer to the next LinkedNode in a MyLinkedList */
    protected LinkedNode next;

    /**
     * The constructor initializes some important fields.
     *
     * @param name the LinkedNode's name (index)
     */
    public LinkedNode(int name) {
        this.name = name;
        next = null;
    }
}

/**
 * An auxiliary class representing a MyLinkedList.
 */
class MyLinkedList {
    /** a pointer to the first LinkedNode */
    protected LinkedNode front;
    /** a pointer to the last LinkedNode */
    protected LinkedNode back;

    /**
     * The constructor initializes some important fields.
     *
     * @param name the first LinkedNode's name (index)
     */
    public MyLinkedList(int name) {
        LinkedNode aLinkedNode = new LinkedNode(name);
        front = aLinkedNode;
        back = aLinkedNode;
    }

    /**
     * This method attaches a MyLinkedList to the end of this MyLinkedList on O(1).
     *
     * @param aLinkedList a MyLinkedList to be attached to this MyLinkedList
     */
    public void add(MyLinkedList aLinkedList) {
        back.next = aLinkedList.front;
        back = aLinkedList.back;
    }
}