/*
 * This file illustrates Matches.java from hw1.
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


/**
 * This program determines if there exist two completely different stable matchings for the given data. O(n^2)
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class Matches {
    /** the number of elements in each group */
    private int n;
    /**
     * using two index-based data structure ArrayList to store members from both group A and group B
     * with index as the members and elements as their own preference lists in a integer array
     */
    private final List<int[]> A = new ArrayList<>(), B = new ArrayList<>();
    /**
     * using two index-based data structure ArrayList to store all members' inverse preference lists so that we
     * can use index to search one member's position on another member's preference list on the other team with
     * constant time O(1)
     */
    private final List<int[]> inverseA = new ArrayList<>(), inverseB = new ArrayList<>();
    /** Group A's partner lists while implementing group A-optimal stable matching */
    private final List<Integer> AoptimalA = new ArrayList<>();
    /** Group B's partner lists while implementing group A-optimal stable matching */
    private final List<Integer> AoptimalB = new ArrayList<>();
    /** Group A's partner lists while implementing group B-optimal stable matching */
    private final List<Integer> BoptimalA = new ArrayList<>();
    /** Group B's partner lists while implementing group B-optimal stable matching */
    private final List<Integer> BoptimalB = new ArrayList<>();


    /**
     * This method reads in and process the input from the standard input and stores data
     * into corresponding data structures created when a Matches object is generated.
     */
    public void input() {
        Scanner in = new Scanner(System.in);
        n = Integer.parseInt(in.next());
        in.nextLine();
        for (int i = 0; i < n; i++) {
            // create lists with appropriate sizes
            A.add(new int[n]);
            B.add(new int[n]);
            inverseA.add(new int[n]);
            inverseB.add(new int[n]);
            // set original value as -1, which represents the status of unmatched
            AoptimalA.add(-1);
            AoptimalB.add(-1);
            BoptimalA.add(-1);
            BoptimalB.add(-1);
        }

        String[] aStringArray;
        // create members of group A with their preference lists and also their inverse preference lists
        for (int j = 0; j < n; j++) {
            aStringArray = in.nextLine().split(" ");
            for (int k = 0; k < aStringArray.length; k++) {
                A.get(j)[k] = Integer.parseInt(aStringArray[k]);
                inverseA.get(j)[Integer.parseInt(aStringArray[k])] = k;
            }
        }

        // create members of group B with their preference lists and also their inverse preference lists
        for (int x = 0; x < n; x++) {
            aStringArray = in.nextLine().split(" ");
            for (int y = 0; y < aStringArray.length; y++) {
                B.get(x)[y] = Integer.parseInt(aStringArray[y]);
                inverseB.get(x)[Integer.parseInt(aStringArray[y])] = y;
            }
        }
        in.close();
    }

    /**
     * This method performs group A-optimal stable matching .
     */
    public void matchingA() {
        /* create a list of free group A members with a LinkedList for implementing
           adding or removing an element at the front */
        LinkedList<Integer> freeA = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            freeA.add(i);
        }

        // two helper variables for making the following codes clear
        int temp1, temp2;
        while (!freeA.isEmpty()) {
            // traverse preference list (asking)
            for (int j = 0; j < n; j++) {
                // first free member from group A
                temp1 = freeA.getFirst();
                // j-th member from group B on the current group A member's preference list
                temp2 = A.get(temp1)[j];
                // if the asked member from group B is not matched
                if (AoptimalB.get(temp2) == -1) {
                    AoptimalA.set(temp1, temp2);
                    AoptimalB.set(temp2, temp1);
                    freeA.removeFirst();
                    break;
                } else {
                    // if the asked member from group B is matched, but he/she prefers this asker
                    if (inverseB.get(temp2)[temp1] < inverseB.get(temp2)[AoptimalB.get(temp2)]) {
                        freeA.removeFirst();
                        AoptimalA.set(temp1, temp2);
                        freeA.addFirst(AoptimalB.get(temp2));
                        AoptimalB.set(temp2, temp1);
                        break;
                    }
                }
            }
        }
    }

    /**
     * This method performs group B-optimal stable matching.
     */
    public void matchingB() {
        /* create a list of free group B members with a LinkedList for implementing
           adding or removing an element at the front */
        LinkedList<Integer> freeB = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            freeB.add(i);
        }

        // two helper variables for making the following codes clear
        int temp1, temp2;
        while (!freeB.isEmpty()) {
            // traverse preference list (asking)
            for (int j = 0; j < n; j++) {
                temp1 = freeB.getFirst();
                temp2 = B.get(temp1)[j];
                if (BoptimalA.get(temp2) == -1) {
                    BoptimalB.set(temp1, temp2);
                    BoptimalA.set(temp2, temp1);
                    freeB.removeFirst();
                    break;
                } else {
                    if (inverseA.get(temp2)[temp1] < inverseA.get(temp2)[BoptimalA.get(temp2)]) {
                        freeB.removeFirst();
                        BoptimalB.set(temp1, temp2);
                        freeB.addFirst(BoptimalA.get(temp2));
                        BoptimalA.set(temp2, temp1);
                        break;
                    }
                }
            }
        }
    }

    /**
     * This method checks if there exist two completely different stable matchings
     * and prints out the corresponding result on the standard output.
     */
    public void output() {
        boolean exist = true;
        // check if all group A members' partners differ while implementing group A-optimal V.S. group B-optimal
        for (int i = 0; i < n; i++) {
            if (AoptimalA.get(i).equals(BoptimalA.get(i))) {
                exist = false;
                break;
            }
        }
        if (exist) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        Matches hw1 = new Matches();
        // read, process, and store input data
        hw1.input();
        // implement group A-optimal stable matching
        hw1.matchingA();
        // implement group B-optimal stable matching
        hw1.matchingB();
        // print the output
        hw1.output();
    }
}