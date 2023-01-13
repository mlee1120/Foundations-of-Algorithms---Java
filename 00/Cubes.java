/*
 * This file illustrates Cubes.java from hw0.
 */

import java.util.Scanner;

/**
 * This program takes a single non-negative integer from the standard input and prints out in increasing order, starting
 * from 0, all positive perfect cubes less than or equal to that value. The input value is at most 1,000,000.
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class Cubes {
    /** stores the input value */
    private int number;

    /**
     * This method acquires an input from the standard input, checks if its value is a
     * positive integer less than or equal to 1000000, and stores the value to 'number'.
     */
    public void ask() {
        Scanner in = new Scanner(System.in);
        String aString = in.next();
        try {
            number = Integer.parseInt(aString);
            if (number < 0 || number > 1000000) throw new Exception();
        } catch (Exception e) {
            System.out.println("The input should be a positive integer no greater than 1000000");
            System.exit(-1);
        }
        // closes the Scanner
        in.close();
    }

    /**
     * This method prints out all perfect cubes less than or equal to 'number'. (starting from 0)
     */
    public void print() {
        for (int i = 0; i * i * i <= number; i++) {
            System.out.println(i * i * i);
        }
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        Cubes hw0 = new Cubes();
        hw0.ask();
        hw0.print();
    }
}
