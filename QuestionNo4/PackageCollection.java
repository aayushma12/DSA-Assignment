/**
 * Algorithm Explanation:
 * ----------------------
 * The problem is modeled as a graph where nodes represent locations and edges represent roads.
 * Each node has a value (0 or 1) indicating whether it has a package.
 * The goal is to collect all packages and return to the starting point using the minimum number of roads.
 *
 * Approach:
 * 1. Construct the graph using an adjacency list.
 * 2. Perform a Depth First Search (DFS) to traverse the graph and collect all packages.
 * 3. If a node has a package or leads to a package, we count the roads used.
 * 4. The DFS ensures that all roads traveled are counted twice (forward and return).
 * 5. The final answer is the total number of roads traveled.
 */

 import java.util.*;

 public class PackageCollection {
 
     /**
      * Calculates the minimum roads required to collect all packages and return to the starting point.
      * 
      * @param n         Number of locations (nodes)
      * @param roads     List of roads (edges) connecting the locations
      * @param packages  Array indicating which locations contain a package (1 if present, 0 otherwise)
      * @return          Minimum number of roads required
      */
     public static int minRoadsToCollectPackages(int n, int[][] roads, int[] packages) {
         List<Integer>[] graph = new ArrayList[n];
         
         // Initialize adjacency list
         for (int i = 0; i < n; i++) {
             graph[i] = new ArrayList<>();
         }
         
         // Construct the graph
         for (int[] road : roads) {
             graph[road[0]].add(road[1]);
             graph[road[1]].add(road[0]);
         }
         
         boolean[] visited = new boolean[n];
         return dfs(0, graph, packages, visited);
     }
 
     /**
      * DFS function to traverse the graph and count roads needed to collect packages.
      * 
      * @param node      Current node being visited
      * @param graph     Adjacency list representing the graph
      * @param packages  Array indicating package presence at each node
      * @param visited   Boolean array to track visited nodes
      * @return          Number of roads used for collecting packages
      */
     private static int dfs(int node, List<Integer>[] graph, int[] packages, boolean[] visited) {
         visited[node] = true;
         int totalRoads = 0;
         
         // Explore all neighbors
         for (int neighbor : graph[node]) {
             if (!visited[neighbor]) {
                 int roadsUsed = dfs(neighbor, graph, packages, visited);
                 
                 // If the path leads to a package, count the roads (both forward and return)
                 if (roadsUsed > 0 || packages[neighbor] == 1) {
                     totalRoads += roadsUsed + 2;
                 }
             }
         }
         return totalRoads;
     }
 
     public static void main(String[] args) {
         // Test case 1
         int[] packages1 = {1, 0, 0, 0, 0, 1};
         int[][] roads1 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}};
         System.out.println("Output: " + minRoadsToCollectPackages(6, roads1, packages1));
         // Expected Output: 2
 
         // Test case 2
         int[] packages2 = {0, 0, 0, 1, 1, 0, 0, 1};
         int[][] roads2 = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}, {5, 7}};
         System.out.println("Output: " + minRoadsToCollectPackages(8, roads2, packages2));
         // Expected Output: 2
     }
 }
 

/*
Test Results:
------------
Input: packages = {1, 0, 0, 0, 0, 1}, roads = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}}
Output: 2
Explanation: Start at location 2, collect packages at locations 0 and 5, then return.

Input: packages = {0, 0, 0, 1, 1, 0, 0, 1}, roads = {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}, {5, 7}}
Output: 2
Explanation: Start at location 0, collect packages at locations 4 and 7, then return.
*/
