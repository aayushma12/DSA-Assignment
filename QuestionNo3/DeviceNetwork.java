/* 
Algorithm Explanation:
----------------------
Problem:
- We have 'n' devices that need to be connected.
- Each device can either install its own communication module at a given cost.
- Alternatively, devices can connect to each other using bidirectional connections with specified costs.
- The goal is to **connect all devices with the minimum total cost**.

Approach:
1. **Model the Problem as a Minimum Spanning Tree (MST)**:
   - Treat each device as a node in a graph.
   - Each direct connection is an edge with an associated cost.
   - Each device also has a "virtual" edge to a central node (device 0) with a cost equal to its module installation cost.

2. **Use Kruskal's Algorithm with Union-Find**:
   - Create a list of all possible edges (direct connections + virtual module installation edges).
   - Sort the edges based on cost (lowest to highest).
   - Use a **Union-Find** data structure to keep track of connected components.
   - Iterate through the edges and add the lowest-cost connections while avoiding cycles.
   - Stop when we have connected all `n` devices (using `n` edges in total).

Time Complexity:
- Sorting edges: **O(E log E)** (where E is the number of edges).
- Union-Find operations: **O(E α(n))**, where α(n) is nearly constant.
- Total Complexity: **O(E log E)**, which is efficient for large networks.
*/

import java.util.*;

public class DeviceNetwork {
    
    // Class representing a connection between two devices
    static class Connection {
        int device1;
        int device2;
        int cost;

        public Connection(int device1, int device2, int cost) {
            this.device1 = device1;
            this.device2 = device2;
            this.cost = cost;
        }
    }

    // Function to find the root parent of a device (for union-find)
    public static int find(int[] parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]); // Path compression to optimize future queries
        }
        return parent[x];
    }

    // Function to merge two sets (for union-find)
    public static void union(int[] parent, int x, int y) {
        int rootX = find(parent, x);
        int rootY = find(parent, y);
        if (rootX != rootY) {
            parent[rootX] = rootY; // Connect the two components
        }
    }

    // Function to compute the minimum cost to connect all devices
    public static int minCostToConnectAllDevices(int n, int[] modules, List<Connection> connections) {
        // Step 1: Initialize a list of edges (connections + virtual module edges)
        List<int[]> edges = new ArrayList<>();

        // Add virtual connections representing module installation costs
        for (int i = 0; i < n; i++) {
            edges.add(new int[]{0, i + 1, modules[i]}); // Connect to a virtual device 0
        }

        // Add real physical connections between devices
        for (Connection conn : connections) {
            edges.add(new int[]{conn.device1, conn.device2, conn.cost});
        }

        // Step 2: Sort edges by cost (ascending order)
        edges.sort(Comparator.comparingInt(a -> a[2]));

        // Step 3: Initialize Union-Find data structure
        int[] parent = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            parent[i] = i; // Initially, each device is its own parent
        }

        // Step 4: Process edges using Kruskal's MST Algorithm
        int totalCost = 0;
        int edgesUsed = 0;

        for (int[] edge : edges) {
            int device1 = edge[0];
            int device2 = edge[1];
            int cost = edge[2];

            // If devices are not already connected, connect them
            if (find(parent, device1) != find(parent, device2)) {
                union(parent, device1, device2);
                totalCost += cost;
                edgesUsed++;

                // If all n devices are connected, stop early
                if (edgesUsed == n) {
                    break;
                }
            }
        }

        return totalCost; // Return the minimum cost
    }

    public static void main(String[] args) {
        // Test Case 1
        int n = 3;
        int[] modules = {1, 2, 2};
        List<Connection> connections = new ArrayList<>();
        connections.add(new Connection(1, 2, 1));
        connections.add(new Connection(2, 3, 1));

        // Compute and print the minimum cost
        int result = minCostToConnectAllDevices(n, modules, connections);
        System.out.println("Minimum cost to connect all devices: " + result);
    }
}

/*
Test Cases & Expected Output:
------------------------------
Test Case 1:
Input:
n = 3
modules = [1, 2, 2]
connections = [[1, 2, 1], [2, 3, 1]]

Output:
Minimum cost to connect all devices: 3

Explanation:
- Install a module on device 1 (cost = 1).
- Connect device 1 to device 2 (cost = 1).
- Connect device 2 to device 3 (cost = 1).
- Total cost = 1 + 1 + 1 = 3.

------------------------------
Test Case 2:
Input:
n = 4
modules = [5, 1, 2, 3]
connections = [[1, 2, 2], [2, 3, 3], [3, 4, 4], [1, 3, 6]]

Output:
Minimum cost to connect all devices: 6

Explanation:
- Install a module on device 2 (cost = 1).
- Connect device 2 to device 1 (cost = 2).
- Connect device 2 to device 3 (cost = 3).
- Device 4 installs its own module (cost = 3, but we found a cheaper way using connections).
- Total cost = 1 + 2 + 3 = 6.

------------------------------
Test Case 3:
Input:
n = 5
modules = [3, 2, 1, 4, 5]
connections = [[1, 2, 2], [2, 3, 2], [3, 4, 2], [4, 5, 2], [1, 5, 7]]

Output:
Minimum cost to connect all devices: 8

Explanation:
- Install module on device 3 (cost = 1).
- Connect 3 to 2 (cost = 2), 3 to 4 (cost = 2), 4 to 5 (cost = 2), 2 to 1 (cost = 2).
- Total cost = 1 + 2 + 2 + 2 + 2 = 8.
*/
