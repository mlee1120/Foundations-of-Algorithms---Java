/*
 * This file illustrates AddOne.java from hw5.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * This program first receives a number (n) of vertices and adjacency lists for all vertices.
 * It then has to determine whether it is possible to add just a single edge to the graph
 * such that the graph becomes strongly connected.
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class AddOne {
    /** the number of vertices */
    private int n;
    /** if there is a single edge (one way) from current SCC to previous SCC */
    private boolean edgeBetweenSCCs;
    /** a list of vertices representing the graph */
    private final List<Vertex> graph;
    /** the finishing order of vertices from first DFS */
    private final List<Vertex> finishingOrder;
    private final Scanner in;

    /**
     * The constructor initializes some important fields.
     */
    public AddOne() {
        edgeBetweenSCCs = false;
        graph = new ArrayList<>();
        finishingOrder = new ArrayList<>();
        in = new Scanner(System.in);
    }

    /**
     * This method reads the input from the standard input and stores data into corresponding data structures.
     */
    public void input() {
        n = in.nextInt();
        in.nextLine();
        for (int i = 0; i < n; i++) {
            graph.add(new Vertex(i));
        }
        String[] aStringArray;
        for (int j = 0; j < n; j++) {
            aStringArray = in.nextLine().split(" ");
            for (int k = 0; k < aStringArray.length - 1; k++) {
                graph.get(j).neighbor.add(graph.get(Integer.parseInt(aStringArray[k]) - 1));
            }
            // sort neighbors by their numerical (indices) order for first DFS
            Collections.sort(graph.get(j).neighbor);
        }
    }

    /**
     * This method utilizes the algorithm of finding strongly connected components.
     * During the algorithm, it checks if all strongly connected components have an
     * edge toward the previous found SCC (in the reversed graph). If yes, the result
     * would be true; else, false.
     */
    public void SCC() {
        // first DFS
        for (int i = 0; i < n; i++) {
            if (graph.get(i).visited == 0) DFSOne(graph.get(i));
        }

        // reverse the graph
        for (int j = 0; j < n; j++) {
            for (int k = 0; k < finishingOrder.get(j).neighbor.size(); k++) {
                finishingOrder.get(j).neighbor.get(k).neighborReverse.add(finishingOrder.get(j));
            }
        }

        // setting all vertices' order in reversed finishing order and reset visited or not
        for (int x = 0; x < n; x++) {
            finishingOrder.get(x).order = n - 1 - x;
            finishingOrder.get(x).visited = 0;
        }

        // sort all vertices' neighbors in the reversed finishing order
        for (int y = 0; y < n; y++) {
            Collections.sort(finishingOrder.get(y).neighborReverse);
        }


        int indexOfCurrentSCC = 0;
        for (int z = n - 1; z >= 0; z--) {
            if (finishingOrder.get(z).visited == 0) {
                indexOfCurrentSCC++;
                edgeBetweenSCCs = false;
                DFSTwo(finishingOrder.get(z), indexOfCurrentSCC);
                if (!edgeBetweenSCCs) break;
            }
        }
    }

    /**
     * This method performs first DFS recursively.
     *
     * @param currentVertex the currently visited vertex
     */
    public void DFSOne(Vertex currentVertex) {
        currentVertex.visited = 1;
        for (int i = 0; i < currentVertex.neighbor.size(); i++) {
            if (currentVertex.neighbor.get(i).visited == 0) DFSOne(currentVertex.neighbor.get(i));
        }
        finishingOrder.add(currentVertex);
    }

    /**
     * This method performs second DFS recursively tp find all SCCs and check if all SCCs is single-way connected.
     *
     * @param currentVertex the currently visited vertex
     * @param currentSCC index of the current SCC for labeling vertices
     */
    public void DFSTwo(Vertex currentVertex, int currentSCC) {
        // there is no previous SCC for the first SCC
        if (currentSCC == 1) edgeBetweenSCCs = true;
        // setting 'visited' to current SCC's index so that we can tell every vertex belongs to which SCC
        currentVertex.visited = currentSCC;
        for (int i = 0; i < currentVertex.neighborReverse.size(); i++) {
            if (currentVertex.neighborReverse.get(i).visited == 0) {
                DFSTwo(currentVertex.neighborReverse.get(i), currentSCC);
            } else {
                // checks if there is at least one edge from current SCC to previous SCC
                if (currentSCC == currentVertex.neighborReverse.get(i).visited + 1) edgeBetweenSCCs = true;
            }
        }
    }

    /**
     * This method prints out the result.
     */
    public void output() {
        if (edgeBetweenSCCs) System.out.println("YES");
        else System.out.println("NO");
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        AddOne hw5 = new AddOne();
        hw5.input();
        hw5.SCC();
        hw5.output();
    }
}

/**
 * This is an auxiliary class use to represent vertices.
 */
class Vertex implements Comparable<Vertex> {
    /** is this vertex visited or not */
    protected int visited;
    /** this vertex's order (for DFS) */
    protected int order;
    /** this vertex's neighbors in the original graph */
    protected List<Vertex> neighbor;
    /** this vertex's neighbors in the reversed graph */
    protected List<Vertex> neighborReverse;

    /**
     * The constructor initializes some important fields.
     *
     * @param order this vertex's order (for DFS)
     */
    public Vertex(int order) {
        visited = 0;
        this.order = order;
        neighbor = new ArrayList<>();
        neighborReverse = new ArrayList<>();
    }

    /**
     * This method helps determining the natural order of Vertex.
     *
     * @param v a Vertex to be compared with this Vertex
     * @return two Vertices' order
     */
    @Override
    public int compareTo(Vertex v) {
        return this.order - v.order;
    }
}