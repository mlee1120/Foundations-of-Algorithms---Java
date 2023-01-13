/*
 * This file illustrates Food.java from hw2.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This program first receives a sequence of data, representing n donated perishable food items with their
 * donation days and expiration periods. After that it checks if all food items can be used up (one item
 * can be used per time unit) without being wasted.
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class Food {
    /** the number of data (n) */
    private int n;
    /** the list of foods with their donation days and expiration periods (sorted by donation days) */
    private final List<int[]> foods;
    /** the sorted food list by the expiration periods */
    private List<int[]> sortedFoods;
    /** a list of food after all food items are decided when to be used (sorted by the days of being used) */
    private List<int[]> cookedFoods;
    private final Scanner in;
    /** whether all food items are not wasted */
    private boolean result = true;

    /**
     * The constructor initiates some important fields.
     */
    public Food() {
        foods = new ArrayList<>();
        sortedFoods = new ArrayList<>();
        cookedFoods = new ArrayList<>();
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
            foods.add(new int[]{Integer.parseInt(aStringArray[0]), Integer.parseInt(aStringArray[1])});
        }
        in.close();
    }

    /**
     * This method starts merge-sorting by calling a helper function (sort by expiration periods)
     */
    public void mergeSortByLife() {
        sortedFoods = sortByLife(0, n - 1);
    }

    /**
     * This method merge-sorts the list by the order of expiration periods
     *
     * @param start the index of the first element of the list to be sorted
     * @param end   the index of the last element of the list to be sorted
     * @return a list of sorted data
     */
    public List<int[]> sortByLife(int start, int end) {
        if (start == end) {
            List<int[]> temp = new ArrayList<>();
            temp.add(foods.get(start));
            return temp;
        } else {
            int middle = (start + end) / 2;
            List<int[]> tempLeft = sortByLife(start, middle);
            List<int[]> tempRight = sortByLife(middle + 1, end);
            int indexLeft = 0, indexRight = 0;
            List<int[]> temp = new ArrayList<>();
            while (indexLeft < tempLeft.size() && indexRight < tempRight.size() && result) {
                if (tempLeft.get(indexLeft)[1] <= tempRight.get(indexRight)[1]) {
                    temp.add(tempLeft.get(indexLeft));
                    indexLeft++;
                } else {
                    temp.add(tempRight.get(indexRight));
                    indexRight++;
                }
            }
            if (indexLeft < tempLeft.size()) {
                while (indexLeft < tempLeft.size()) {
                    temp.add(tempLeft.get(indexLeft));
                    indexLeft++;
                }
            } else {
                while (indexRight < tempRight.size()) {
                    temp.add(tempRight.get(indexRight));
                    indexRight++;
                }
            }
            return temp;
        }
    }

    /**
     * This method starts merge-sorting by calling a helper function (sort by the days to be used)
     */
    public void cookDay() {
        cookedFoods = cookDayDecide(0, n - 1);
    }

    /**
     * This method decides every food item's day to be used if possible and
     * merge-sorts the list by the order of days to be used.
     *
     * @param start the index of the first element of the list to be sorted
     * @param end   the index of the last element of the list to be sorted
     * @return a list of sorted data
     */
    public List<int[]> cookDayDecide(int start, int end) {
        if (start == end) {
            List<int[]> temp = new ArrayList<>();
            temp.add(foods.get(start));
            return temp;
        } else {
            int middle = (start + end) / 2;
            List<int[]> tempLeft = sortByLife(start, middle);
            List<int[]> tempRight = sortByLife(middle + 1, end);
            int indexLeft = 0, indexRight = 0;
            List<int[]> temp = new ArrayList<>();
            /*
               Food item with shortest expiration period is going to be used first.
               Since only one item can be used per time unit (day), a variable day
               (day) is declared to record the days being occupied.
             */
            int day = -1;
            while (indexLeft < tempLeft.size() && indexRight < tempRight.size()) {
                if (tempLeft.get(indexLeft)[0] < tempRight.get(indexRight)[0]) {
                    // the day hasn't been occupied
                    if (tempLeft.get(indexLeft)[0] > day) {
                        day = tempLeft.get(indexLeft)[0];
                        temp.add(tempLeft.get(indexLeft));
                        indexLeft++;
                    } else {
                        // if the day is already occupied, modify the data accordingly
                        tempLeft.get(indexLeft)[0]++;
                        tempLeft.get(indexLeft)[1]--;
                        // food expires
                        if (tempLeft.get(indexLeft)[1] == 0) {
                            result = false;
                            break;
                        }
                    }
                } else if (tempLeft.get(indexLeft)[0] > tempRight.get(indexRight)[0]) {
                    if (tempRight.get(indexRight)[0] > day) {
                        day = tempRight.get(indexRight)[0];
                        temp.add(tempRight.get(indexRight));
                        indexRight++;
                    } else {
                        tempRight.get(indexRight)[0]++;
                        tempRight.get(indexRight)[1]--;
                        if (tempRight.get(indexRight)[1] == 0) {
                            result = false;
                            break;
                        }
                    }
                } else {
                    if (tempRight.get(indexRight)[0] > day) {
                        if (tempLeft.get(indexLeft)[1] <= tempRight.get(indexRight)[1]) {
                            day = tempLeft.get(indexLeft)[0];
                            temp.add(tempLeft.get(indexLeft));
                            indexLeft++;
                        } else {
                            day = tempRight.get(indexRight)[0];
                            temp.add(tempRight.get(indexRight));
                            indexRight++;
                        }
                    } else {
                        tempLeft.get(indexLeft)[0]++;
                        tempLeft.get(indexLeft)[1]--;
                        tempRight.get(indexRight)[0]++;
                        tempRight.get(indexRight)[1]--;
                        if (tempRight.get(indexRight)[1] == 0 || tempLeft.get(indexLeft)[1] == 0) {
                            result = false;
                            break;
                        }
                    }
                }
            }
            if (result) {
                if (indexLeft < tempLeft.size()) {
                    while (indexLeft < tempLeft.size()) {
                        if (tempLeft.get(indexLeft)[0] > day) {
                            day = tempLeft.get(indexLeft)[0];
                            temp.add(tempLeft.get(indexLeft));
                            indexLeft++;
                        } else {
                            tempLeft.get(indexLeft)[0]++;
                            tempLeft.get(indexLeft)[1]--;
                            if (tempLeft.get(indexLeft)[1] == 0) {
                                result = false;
                                break;
                            }
                        }
                    }
                } else {
                    while (indexRight < tempRight.size()) {
                        if (tempRight.get(indexRight)[0] > day) {
                            day = tempLeft.get(indexRight)[0];
                            temp.add(tempRight.get(indexRight));
                            indexRight++;
                        } else {
                            tempRight.get(indexRight)[0]++;
                            tempRight.get(indexRight)[1]--;
                            if (tempRight.get(indexRight)[1] == 0) {
                                result = false;
                                break;
                            }
                        }
                    }
                }
            }
            return temp;
        }
    }

    /**
     * This method prints out if all food can be used without being wasted.
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
        Food hw2 = new Food();
        hw2.input();
        hw2.mergeSortByLife();
        hw2.cookDay();
        hw2.output();
    }
}
