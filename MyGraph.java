package com.leerdev.graphs.graphVis;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Class describing a graph (set of vertices and connections between them)
 * @author Artem Leer
 */
public class MyGraph {
    private HashMap<Integer, HashSet<Integer>> vertices;    
    //constructor
    public MyGraph(File f) {
    	vertices = new HashMap<>();
    	loadGraph(f);
    }
    // return true if n2 is a neighbor of n1
    public boolean isNeighbor (GNode n1, GNode n2) {
		if (n1==null||n2==null) return false;
    	if (vertices.get(n1.getName()).contains(n2.getName())) return true;
		return false;
    }
    
    // getter for vertices
    public Set<Integer> getVertices() {return vertices.keySet();}
    
    // getter for vertex amount
    public int getSize() { return vertices.size();}
    
    // getter for 'neighbors'
    public Set<Integer> getNeighbors(int name) {return vertices.get(name);}
   
    //loading data function
    private void loadGraph (File f) {
        Scanner sc;
        try {
            sc = new Scanner(f);
        } catch (Exception e) {
            System.out.println ("File opening error.");
            return;
        }
        // Iterate over the lines in the file, adding new
        // vertices as they are found and adding neighbors
        while (sc.hasNextInt()) {
            int v1 = sc.nextInt(); int v2 = sc.nextInt();
            if (!vertices.containsKey(v1)) {
            	vertices.put(v1,new HashSet<Integer>());
            }
            if (!vertices.containsKey(v2)) {
                vertices.put(v2, new HashSet<Integer>());
            }
            	vertices.get(v1).add(v2);
        }
        sc.close();
    }
}


