/*
 * This file illustrates Evensum.java from hw0.
 */

import java.util.Scanner;

/**
 * This program reads in a series of non-negative integers and prints out the sum of all inputs that are even.
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class Evensum {
    /** stores the number of integers */
    private int number;
    /** stores the sum of all even inputs */
    private int sum;

    /**
     * This method first acquires a number of integers and checks if it is a valid number.
     * After that, it reads a series of non-negative integers.
     */
    public void read() {
        Scanner in = new Scanner(System.in);
        String aString = in.next();
        try {
            number = Integer.parseInt(aString);
            if (number <= 0) throw new Exception();
        } catch (Exception e1) {
            System.out.println("The input should be a number of integers (at least one).");
            System.exit(-1);
        }

        int a = 0;

        // reads in a series of non-negative integers
        for (int i = 0; i < number; i++) {
            aString = in.next();
            try {
                a = Integer.parseInt(aString);
                if (a < 0) throw new Exception();
            } catch (Exception e2) {
                System.out.println("The inputs should be non-negative integers.");
                System.exit(-1);
            }
            if (a % 2 == 0) sum += a;
        }
        // closes the Scanner
        in.close();
    }

    /**
     * This method prints out the sum of all inputs that are even.
     */
    public void print() {
        System.out.println(sum);
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        Evensum hw0 = new Evensum();
        hw0.read();
        hw0.print();
    }
}
