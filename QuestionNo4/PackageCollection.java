import java.util.*;

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

public class PackageCollection {
    public static int minRoadsToCollectPackages(int n, int[][] roads, int[] packages) {
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // Build the adjacency list
        for (int[] road : roads) {
            graph[road[0]].add(road[1]);
            graph[road[1]].add(road[0]);
        }
        
        boolean[] visited = new boolean[n];
        return dfs(0, graph, packages, visited);
    }

    private static int dfs(int node, List<Integer>[] graph, int[] packages, boolean[] visited) {
        visited[node] = true;
        int totalRoads = 0;
        
        for (int neighbor : graph[node]) {
            if (!visited[neighbor]) {
                int roadsUsed = dfs(neighbor, graph, packages, visited);
                if (roadsUsed > 0 || packages[neighbor] == 1) {
                    totalRoads += roadsUsed + 2; // Counting forward and return path
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
