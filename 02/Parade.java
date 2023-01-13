/*
 * This file illustrates Parade.java from hw2.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This program first receives a sequence of data, representing participants lining up for the parade in an
 * unsorted order. After that, it sorts the line by swapping adjacent participants, whose patience will drop
 * by 1 when being swapped. In the end, this program will print out how many participants hav lost their
 * patience during the swapping (sorting).
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class Parade {
    /** the number of data (n) */
    private int n;
    /** a list of participants (every element is a int array containing the participant's desired order and patience) */
    private final List<int[]> participants;
    /** the sorted list of participants */
    private List<int[]> sortedParticipants;
    private final Scanner in;

    /**
     * The constructor initiates some important fields.
     */
    public Parade() {
        participants = new ArrayList<>();
        in = new Scanner(System.in);
    }

    /**
     * This method reads the input from the standard input and stores data into corresponding data structures.
     */
    public void input() {
        n = Integer.parseInt(in.next());
        in.nextLine();
        String[] aStringArray;
        for (int i = 0; i < n; i++) {
            aStringArray = in.nextLine().split(" ");
            participants.add(new int[]{Integer.parseInt(aStringArray[0]), Integer.parseInt(aStringArray[1])});
        }
        in.close();
    }

    /**
     * This method starts sorting the list by calling a helper function (for recurrence)
     */
    public void process() {
        sortedParticipants = di(0, n - 1);
    }

    /**
     * This method sorts the list by swapping adjacent elements (participants) and
     * also calculates their patience while swapping.
     * The algorithm is the enhanced version of merge sort (divide and conquer). It
     * works like inversion counting => the number of swaps to finish sorting equals
     * the number of inversions in the list.
     *
     * @param start the index of the first element of the list to be sorted
     * @param end the index of the last element of the list to be sorted
     * @return a list of sorted data
     */
    public List<int[]> di(int start, int end) {
        // only one element left after divided
        if (start == end) {
            List<int[]> temp = new ArrayList<>();
            temp.add(participants.get(start));
            return temp;
        } else {
            // dividing
            int middle = (start + end) / 2;
            List<int[]> tempLeft = di(start, middle);
            List<int[]> tempRight = di(middle + 1, end);
            int indexLeft = 0, indexRight = 0;
            List<int[]> temp = new ArrayList<>();
            // merging
            while (indexLeft < tempLeft.size() && indexRight < tempRight.size()) {
                if (tempLeft.get(indexLeft)[0] < tempRight.get(indexRight)[0]) {
                    temp.add(tempLeft.get(indexLeft));
                    // difference in index = change of patience (number of swaps)
                    temp.get(temp.size() - 1)[1] -= Math.abs(indexLeft - temp.size() + 1);
                    indexLeft++;
                } else {
                    temp.add(tempRight.get(indexRight));
                    temp.get(temp.size() - 1)[1] -= Math.abs(indexRight + tempLeft.size() - temp.size() + 1);
                    indexRight++;
                }
            }
            if (indexLeft < tempLeft.size()) {
                while (indexLeft < tempLeft.size()) {
                    temp.add(tempLeft.get(indexLeft));
                    temp.get(temp.size() - 1)[1] -= Math.abs(indexLeft - temp.size() + 1);
                    indexLeft++;
                }
            } else {
                while (indexRight < tempRight.size()) {
                    temp.add(tempRight.get(indexRight));
                    temp.get(temp.size() - 1)[1] -= Math.abs(indexRight + tempLeft.size() - temp.size() + 1);
                    indexRight++;
                }
            }
            return temp;
        }
    }

    /**
     * This method prints out how many participants have lost their patience during sorting.
     */
    public void output() {
        int total = 0;
        for (int i = 0; i < n; i++) {
            if (sortedParticipants.get(i)[1] < 0) total++;
        }
        System.out.println(total);
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        Parade hw2 = new Parade();
        hw2.input();
        hw2.process();
        hw2.output();
    }
}
