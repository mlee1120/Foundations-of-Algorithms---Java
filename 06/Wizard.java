/*
 * This file illustrates Wizard.java from hw6.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This program first receives a series of numbers, including the number of points of interest
 * in the maze (n; containing the entry point, the exit point, and all barricade points), the
 * number of magic vials (k), the unit of time to escape the maze (t), and the number of paths
 * among all points of interest (m). And then, it will receive m lines of information about all
 * paths (connections).
 * After that, it has to determine whether there is at least a way out of the maze before the
 * magic vials and time are used up on O(mn).
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class Wizard {
    /** the number of points of interest */
    private int n;
    /** the number of magic vials */
    private int k;
    /** the number of time */
    private int t;
    /** the number of paths */
    private int m;
    /** is there a way out of the maze before the magic vials and time are used up */
    private boolean result;
    /** a list storing all paths' information */
    private final List<int[]> edges;
    /** a 2D list storing all possible results determined by Bellman-Ford algorithm */
    private final List<List<Integer>> arrivalInfo;
    private final Scanner in;

    /**
     * The constructor initializes some important fields.
     */
    public Wizard() {
        result = false;
        edges = new ArrayList<>();
        arrivalInfo = new ArrayList<>();
        in = new Scanner(System.in);
    }

    /**
     * This method reads the input from the standard input and stores data into corresponding data structures.
     */
    public void input() {
        n = in.nextInt();
        k = in.nextInt();
        t = in.nextInt();
        m = in.nextInt();
        in.nextLine();
        String[] aStringArray;
        int firstEndPoint, secondEndPoint, time;
        for (int i = 0; i < m; i++) {
            aStringArray = in.nextLine().split(" ");
            firstEndPoint = Integer.parseInt(aStringArray[0]);
            secondEndPoint = Integer.parseInt(aStringArray[1]);
            time = Integer.parseInt(aStringArray[2]);
            edges.add(new int[]{firstEndPoint, secondEndPoint, time});
        }
        for (int i = 0; i < n; i++) {
            arrivalInfo.add(new ArrayList<>());
            for (int j = 0; j <= k; j++) {
                // use a number greater than 1000 to represent infinity
                if (i != 0 || j != 0) arrivalInfo.get(i).add(1111);
                else arrivalInfo.get(i).add(0);
            }
        }
        in.close();
    }

    /**
     * The method determines all possible results to arrive at every point before using up all magic vials.
     */
    public void bellmanFord() {
        // is there an update in a single iteration
        boolean update;
        int[] anEdge;
        // keeps on updating until there is no update in a single iteration
        do {
            update = false;
            for (int i = 0; i < m; i++) {
                anEdge = edges.get(i);
                // path between two barricade points
                if (anEdge[0] != 0 && anEdge[0] != n - 1 && anEdge[1] != 0 && anEdge[1] != n - 1) {
                    for (int j = 0; j < k; j++) {
                        if (arrivalInfo.get(anEdge[1]).get(j + 1) > arrivalInfo.get(anEdge[0]).get(j) + anEdge[2]) {
                            arrivalInfo.get(anEdge[1]).set(j + 1, arrivalInfo.get(anEdge[0]).get(j) + anEdge[2]);
                            update = true;
                        }
                        if (arrivalInfo.get(anEdge[0]).get(j + 1) > arrivalInfo.get(anEdge[1]).get(j) + anEdge[2]) {
                            arrivalInfo.get(anEdge[0]).set(j + 1, arrivalInfo.get(anEdge[1]).get(j) + anEdge[2]);
                            update = true;
                        }
                    }
                }
                // path whose first endpoint is the entry point and second endpoint is a barricade point
                else if (anEdge[0] == 0 && anEdge[1] != n - 1) {
                    for (int j = 0; j < k; j++) {
                        if (arrivalInfo.get(anEdge[1]).get(j + 1) > arrivalInfo.get(anEdge[0]).get(j) + anEdge[2]) {
                            arrivalInfo.get(anEdge[1]).set(j + 1, arrivalInfo.get(anEdge[0]).get(j) + anEdge[2]);
                            update = true;
                        }
                        if (arrivalInfo.get(anEdge[0]).get(j) > arrivalInfo.get(anEdge[1]).get(j) + anEdge[2]) {
                            arrivalInfo.get(anEdge[0]).set(j, arrivalInfo.get(anEdge[1]).get(j) + anEdge[2]);
                            update = true;
                        }
                    }
                }
                // path whose first endpoint is a barricade point and second endpoint is the entry point
                else if (anEdge[0] != n - 1 && anEdge[1] == 0) {
                    for (int j = 0; j < k; j++) {
                        if (arrivalInfo.get(anEdge[1]).get(j) > arrivalInfo.get(anEdge[0]).get(j) + anEdge[2]) {
                            arrivalInfo.get(anEdge[1]).set(j, arrivalInfo.get(anEdge[0]).get(j) + anEdge[2]);
                            update = true;
                        }
                        if (arrivalInfo.get(anEdge[0]).get(j + 1) > arrivalInfo.get(anEdge[1]).get(j) + anEdge[2]) {
                            arrivalInfo.get(anEdge[0]).set(j + 1, arrivalInfo.get(anEdge[1]).get(j) + anEdge[2]);
                            update = true;
                        }
                    }
                }
                // path whose first endpoint is a barricade point and second endpoint is the exit point
                else if (anEdge[0] != 0 && anEdge[1] == n - 1) {
                    for (int j = 0; j <= k; j++) {
                        if (arrivalInfo.get(anEdge[1]).get(j) > arrivalInfo.get(anEdge[0]).get(j) + anEdge[2]) {
                            arrivalInfo.get(anEdge[1]).set(j, arrivalInfo.get(anEdge[0]).get(j) + anEdge[2]);
                            update = true;
                        }
                    }
                }
                // path whose first endpoint is the exit point and second endpoint is a barricade point
                else if (anEdge[0] == n - 1 && anEdge[1] != 0) {
                    for (int j = 0; j <= k; j++) {
                        if (arrivalInfo.get(anEdge[0]).get(j) > arrivalInfo.get(anEdge[1]).get(j) + anEdge[2]) {
                            arrivalInfo.get(anEdge[0]).set(j, arrivalInfo.get(anEdge[1]).get(j) + anEdge[2]);
                            update = true;
                        }
                    }
                }
                // path whose first endpoint is the entry point and second endpoint is the exit point
                else if (anEdge[0] == 0 && anEdge[1] == n - 1) {
                    for (int j = 0; j <= k; j++) {
                        if (arrivalInfo.get(anEdge[1]).get(j) > arrivalInfo.get(anEdge[0]).get(j) + anEdge[2]) {
                            arrivalInfo.get(anEdge[1]).set(j, arrivalInfo.get(anEdge[0]).get(j) + anEdge[2]);
                            update = true;
                        }
                    }
                }
                // path whose first endpoint is the exit point and second endpoint is the entry point
                else if (anEdge[0] == n - 1 && anEdge[1] == 0) {
                    for (int j = 0; j <= k; j++) {
                        if (arrivalInfo.get(anEdge[0]).get(j) > arrivalInfo.get(anEdge[1]).get(j) + anEdge[2]) {
                            arrivalInfo.get(anEdge[0]).set(j, arrivalInfo.get(anEdge[1]).get(j) + anEdge[2]);
                            update = true;
                        }
                    }
                }
            }
        } while (update);
    }

    /**
     * This method prints out if there at least a way out of
     * the maze before the magic vials and time are used up.
     */
    public void output() {
        for (int i = 0; i <= k; i++) {
            if (arrivalInfo.get(n - 1).get(i) <= t) {
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
        Wizard hw6 = new Wizard();
        hw6.input();
        hw6.bellmanFord();
        hw6.output();
    }
}
