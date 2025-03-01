/*
     * Algorithm Explanation:
     * 
     * The goal is to find the minimum total cost to connect all devices in a network, where:
     * 1. Devices can either have their own communication module installed at a given cost.
     * 2. Devices can also be connected with each other via direct connections, which have specific costs.
     * 
     * The approach is to treat the problem like a Minimum Spanning Tree (MST) problem and use Kruskal's
     * algorithm to find the MST. The steps are as follows:
     * 
     * 1. **Virtual Connections**: We create virtual connections to simulate the installation of communication
     *    modules for each device. For each device, a connection is made to a virtual central device (device 0) with
     *    the cost being the module installation cost for that device.
     * 
     * 2. **Sort All Edges**: All the connections (both real and virtual) are combined into a list. We then sort this
     *    list of edges based on the connection cost. The lower the cost, the higher priority it will have during the MST
     *    creation.
     * 
     * 3. **Union-Find Data Structure**: We use a Union-Find (disjoint-set) data structure to manage the connected
     *    components. This allows us to efficiently check if two devices are already connected (same set) or not (different sets).
     * 
     * 4. **Kruskal's Algorithm**: We iterate through the sorted list of connections and keep adding the least cost
     *    edges to the MST until all devices are connected. Each time we add an edge, we perform a union operation to
     *    connect the two devices. If adding an edge would form a cycle, we skip it.
     * 
     * 5. **Return the Total Cost**: After connecting all devices (using `n-1` edges for `n` devices), we return the
     *    total cost accumulated for the MST, which is the minimum cost to connect all devices.
     * 
     * This approach ensures that we always select the least expensive way to connect devices, either through direct
     * connections or by installing communication modules.
     */



import java.util.*;

public class DeviceNetwork {

    
    // Define a class to represent a connection between two devices
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

    // Function to find the parent of a device (for union-find)
    public static int find(int[] parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]); // Path compression: flatten the tree
        }
        return parent[x];
    }

    // Function to union two devices (for union-find)
    public static void union(int[] parent, int x, int y) {
        int rootX = find(parent, x);
        int rootY = find(parent, y);
        if (rootX != rootY) {
            parent[rootX] = rootY; // Connect the two devices
        }
    }

    // Function to find the minimum total cost to connect all devices
    public static int minCostToConnectAllDevices(int n, int[] modules, List<Connection> connections) {
        // Step 1: Initialize a list of edges for the connection graph
        List<int[]> edges = new ArrayList<>();

        // Add the costs of installing modules to the connection edges (simulate "virtual" edges)
        for (int i = 0; i < n; i++) {
            edges.add(new int[]{0, i + 1, modules[i]}); // Virtual connection to device 0 with cost of modules[i]
        }

        // Add the direct connections between devices
        for (Connection conn : connections) {
            edges.add(new int[]{conn.device1, conn.device2, conn.cost});
        }

        // Step 2: Sort all edges based on the cost
        edges.sort((a, b) -> Integer.compare(a[2], b[2])); // Sort edges by the cost in ascending order

        // Step 3: Initialize the union-find data structure (disjoint-set) to keep track of connected devices
        int[] parent = new int[n + 1]; // Array to store the parent of each node
        for (int i = 0; i <= n; i++) {
            parent[i] = i; // Initially, each device is its own parent
        }

        // Step 4: Process all the edges to form the minimum spanning tree (MST)
        int totalCost = 0;
        int edgesUsed = 0;

        for (int[] edge : edges) {
            int device1 = edge[0];
            int device2 = edge[1];
            int cost = edge[2];

            // If the devices are not already connected (i.e., their roots are different)
            if (find(parent, device1) != find(parent, device2)) {
                // Union the devices and add the cost to the total cost
                union(parent, device1, device2);
                totalCost += cost;
                edgesUsed++;

                // If we have used n-1 edges, we have connected all devices
                if (edgesUsed == n - 1) {
                    break;
                }
            }
        }

        return totalCost; // Return the minimum cost
    }

    public static void main(String[] args) {
        // Test case 1
        int n = 3;
        int[] modules = {1, 2, 2};
        List<Connection> connections = new ArrayList<>();
        connections.add(new Connection(1, 2, 1));
        connections.add(new Connection(2, 3, 1));

        // Call the function and print the result
        int result = minCostToConnectAllDevices(n, modules, connections);
        System.out.println("Minimum cost to connect all devices: " + result); // Expected Output: 3
    }
}
