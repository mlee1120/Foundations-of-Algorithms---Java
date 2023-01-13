/*
 * This file illustrates Daycare.java from hw6.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This program first receives a list of numbers, including the number of children (n), the number
 * of hats (a), the number of mittens (b), and the number of jackets (c). And then, it will receive
 * preference lists of children (hat -> mittens -> jacket for every child; total 3n lines).
 * After that, it has to determine whether whether it is possible to get every child dressed with
 * a hat, a pair of mittens, and a winter jacket that the child finds acceptable on O(n^2*max{a, b, c}).
 *
 * @author Michael Lee, ml3406@rit.edu
 */
public class Daycare {
    /** the number of children */
    private int n;
    /** is there an update in a single DFS */
    private boolean update;
    /** a list of hats */
    private final List<Vertex> hats;
    /** a list of mittens */
    private final List<Vertex> mittens;
    /** a list of jackets */
    private final List<Vertex> jackets;
    /** the graph for solving bipartite matching of hats */
    private final Graph graphHat;
    /** the graph for solving bipartite matching of mittens */
    private final Graph graphMitten;
    /** the graph for solving bipartite matching of jackets */
    private final Graph graphJacket;
    private final Scanner in;

    /**
     * The constructor initializes some important fields.
     */
    public Daycare() {
        hats = new ArrayList<>();
        mittens = new ArrayList<>();
        jackets = new ArrayList<>();
        graphHat = new Graph();
        graphMitten = new Graph();
        graphJacket = new Graph();
        in = new Scanner(System.in);
    }

    /**
     * This method reads the input from the standard input and stores data into corresponding data structures.
     */
    public void input() {
        String[] aStringArray = in.nextLine().split(" ");
        n = Integer.parseInt(aStringArray[0]);
        int a = Integer.parseInt(aStringArray[1]);
        int b = Integer.parseInt(aStringArray[2]);
        int c = Integer.parseInt(aStringArray[0]);

        // building the graphs (creating children and connecting them to the source node)
        for (int i = 0; i < n; i++) {
            graphHat.s.edges.add(new Vertex("child"));
            graphHat.s.edges.get(i).index = i;
            graphHat.s.edgesCapacity.add(1);

            graphMitten.s.edges.add(new Vertex("child"));
            graphMitten.s.edges.get(i).index = i;
            graphMitten.s.edgesCapacity.add(1);

            graphJacket.s.edges.add(new Vertex("child"));
            graphJacket.s.edges.get(i).index = i;
            graphJacket.s.edgesCapacity.add(1);
        }

        // building the graph for hats (creating hats and connecting them to the sink node)
        for (int i = 0; i < a; i++) {
            hats.add(new Vertex("hat"));
            hats.get(i).index = i;
            hats.get(i).edges.add(graphHat.t);
            hats.get(i).edgesCapacity.add(1);
            // building non-existing backward edges to all children (for convenience)
            for (int j = 0; j < n; j++) {
                hats.get(i).backwardEdges.add(graphHat.s.edges.get(j));
                hats.get(i).backwardEdgesCapacity.add(0);
            }
        }

        // building the graph for mittens (creating mittens and connecting them to the sink node)
        for (int i = 0; i < b; i++) {
            mittens.add(new Vertex("mitten"));
            mittens.get(i).index = i;
            mittens.get(i).edges.add(graphMitten.t);
            mittens.get(i).edgesCapacity.add(1);
            // building non-existing backward edges to all children (for convenience)
            for (int j = 0; j < n; j++) {
                mittens.get(i).backwardEdges.add(graphMitten.s.edges.get(j));
                mittens.get(i).backwardEdgesCapacity.add(0);
            }
        }

        // building the graph for jackets (creating jackets and connecting them to the sink node)
        for (int i = 0; i < c; i++) {
            jackets.add(new Vertex("jacket"));
            jackets.get(i).index = i;
            jackets.get(i).edges.add(graphJacket.t);
            jackets.get(i).edgesCapacity.add(1);
            // building non-existing backward edges to all children (for convenience)
            for (int j = 0; j < n; j++) {
                jackets.get(i).backwardEdges.add(graphJacket.s.edges.get(j));
                jackets.get(i).backwardEdgesCapacity.add(0);
            }
        }

        // building the graphs (connecting children to preference items)
        for (int i = 0; i < n; i++) {
            // hats
            for (int j = 0; j < a; j++) {
                graphHat.s.edges.get(i).edges.add(null);
                graphHat.s.edges.get(i).edgesCapacity.add(-1);
            }
            aStringArray = in.nextLine().split(" ");
            int index;
            for (int j = 0; j < aStringArray.length - 1; j++) {
                index = Integer.parseInt(aStringArray[j]) - 1;
                graphHat.s.edges.get(i).edges.set(index, hats.get(index));
                graphHat.s.edges.get(i).edgesCapacity.set(index, 1);
            }

            // mittens
            for (int j = 0; j < b; j++) {
                graphMitten.s.edges.get(i).edges.add(null);
                graphMitten.s.edges.get(i).edgesCapacity.add(-1);
            }
            aStringArray = in.nextLine().split(" ");
            for (int j = 0; j < aStringArray.length - 1; j++) {
                index = Integer.parseInt(aStringArray[j]) - 1;
                graphMitten.s.edges.get(i).edges.set(index, mittens.get(index));
                graphMitten.s.edges.get(i).edgesCapacity.set(index, 1);
            }

            // jackets
            for (int j = 0; j < c; j++) {
                graphJacket.s.edges.get(i).edges.add(null);
                graphJacket.s.edges.get(i).edgesCapacity.add(-1);
            }
            aStringArray = in.nextLine().split(" ");
            for (int j = 0; j < aStringArray.length - 1; j++) {
                index = Integer.parseInt(aStringArray[j]) - 1;
                graphJacket.s.edges.get(i).edges.set(index, jackets.get(index));
                graphJacket.s.edges.get(i).edgesCapacity.set(index, 1);
            }
        }
        in.close();
    }

    /**
     * This method solves bipartite matching via max flow solution.
     */
    public void bipartiteMatching() {
        // hats O(n^2*a)
        do {
            update = false;
            maxFlow(null, graphHat.s);
        } while (update);

        // mittens O(n^2*b)
        do {
            update = false;
            maxFlow(null, graphMitten.s);
        } while (update);

        // jackets O(n^2*c)
        do {
            update = false;
            maxFlow(null, graphJacket.s);
        } while (update);
    }

    /**
     * This method performs DFS for finding the max flow.
     *
     * @param last the last visited vertex
     * @param current the currently visited vertex
     */
    public void maxFlow(Vertex last, Vertex current) {
        // the beginning (current: source node)
        if (current.type.equals("s")) {
            for (int i = 0; i < current.edges.size(); i++) {
                if (current.edgesCapacity.get(i) == 1) {
                    maxFlow(current, current.edges.get(i));
                    if (update) {
                        current.edgesCapacity.set(i, 0);
                        break;
                    }
                }
            }
        }

        // last: source node; current: a child
        else if (last.type.equals("s")) {
            for (int i = 0; i < current.edges.size(); i++) {
                if (current.edgesCapacity.get(i) == 1) {
                    maxFlow(current, current.edges.get(i));
                    if (update) {
                        current.edgesCapacity.set(i, 0);
                        current.item = current.edges.get(i).index + 1;
                        break;
                    }
                }
            }
        }

        // last: a child; current: a hat, a pair of mittens, or a jacket
        else if (last.type.equals("child")) {
            // the item hasn't been assigned to a child
            if (current.edgesCapacity.get(0) == 1) {
                update = true;
                current.edges.get(0).backwardEdges.add(current);
                current.edgesCapacity.set(0, 0);
                current.backwardEdgesCapacity.set(last.index, 1);
            }
            // checks backward edge
            else {
                for (int i = 0; i < current.backwardEdges.size(); i++) {
                    if (current.backwardEdgesCapacity.get(i) == 1) {
                        maxFlow(current, current.backwardEdges.get(i));
                        if (update) {
                            current.backwardEdgesCapacity.set(last.index, 1);
                            current.backwardEdgesCapacity.set(i, 0);
                            break;
                        }
                    }
                }
            }
        }

        // last: a hat, a pair of mittens, or a jacket; current: a child (coming from backward edges)
        else {
            for (int i = 0; i < current.edges.size(); i++) {
                if (current.edgesCapacity.get(i) == 1) {
                    maxFlow(current, current.edges.get(i));
                    if (update) {
                        current.edgesCapacity.set(i, 0);
                        current.item = current.edges.get(i).index + 1;
                        break;
                    }
                }
            }
        }
    }

    /**
     * This method prints out whether it is possible to get every child dressed with
     * a hat, a pair of mittens, and a winter jacket that the child finds acceptable.
     * If yes, also prints out the hat, mittens, and jacket every child gets.
     */
    public void output() {
        int x = graphHat.t.backwardEdges.size();
        int y = graphMitten.t.backwardEdges.size();
        int z = graphJacket.t.backwardEdges.size();
        int q, w, e;
        if (x == n && y == n && z == n) {
            System.out.println("YES");
            for (int i = 0; i < n; i++) {
                q = graphHat.s.edges.get(i).item;
                w = graphMitten.s.edges.get(i).item;
                e = graphJacket.s.edges.get(i).item;
                System.out.println(q + " " + w + " " + e);
            }
        } else System.out.println("NO");
    }

    /**
     * Main method.
     *
     * @param args command line arguments -- unused
     */
    public static void main(String[] args) {
        Daycare hw6 = new Daycare();
        hw6.input();
        hw6.bipartiteMatching();
        hw6.output();
    }
}

/**
 * This is an auxiliary class use to represent vertices.
 */
class Vertex {
    /** the type if this vertex (source node, sink node, child, hat, mittens, or jacket) */
    protected String type;
    /** what the child gets (-1 if the vertex is not a child or the child gets nothing) */
    protected int item;
    /** index of this vertex (for accessing this vertex on O(1)) */
    protected int index;
    /** a list of neighbors of this vertex */
    protected List<Vertex> edges;
    /** a list of the capacities of all neighbors */
    protected List<Integer> edgesCapacity;
    /** a list of backward neighbors */
    protected List<Vertex> backwardEdges;
    /** a list of the capacities of all backward neighbors */
    protected List<Integer> backwardEdgesCapacity;

    /**
     * The constructor initializes some important fields.
     *
     * @param type the type of this vertex
     */
    public Vertex(String type) {
        this.type = type;
        item = -1;
        edges = new ArrayList<>();
        edgesCapacity = new ArrayList<>();
        backwardEdges = new ArrayList<>();
        backwardEdgesCapacity = new ArrayList<>();
    }
}

/**
 * This is an auxiliary class use to represent graphs.
 */
class Graph {
    /** the source node */
    protected Vertex s;
    /** the sink node */
    protected Vertex t;

    /**
     * The constructor initializes some important fields.
     */
    public Graph() {
        s = new Vertex("s");
        t = new Vertex("t");
    }
}