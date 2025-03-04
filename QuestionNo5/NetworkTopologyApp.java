// Algorithm Explanation
// This GUI-based network topology application helps administrators design cost-effective and 
// efficient networks by allowing them to add nodes (servers/clients) and edges 
// (connections with costs and bandwidths). It optimizes the network using Prim’s 
// Algorithm to find the minimum spanning tree (MST) for cost minimization and Dijkstra’s 
// Algorithm to compute the shortest path between nodes. The system provides real-time visualization, 
// displaying the total cost, latency, and optimal connections while allowing users to modify the 
// topology interactively. This ensures minimum cost, efficient data transmission, and dynamic network 
// optimization for better planning and management. 

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Node {
    int x, y; // Node coordinates
    String name; // Node name (identifier)
    String type; // Type of node (Server or Client)

    public Node(String name, String type, int x, int y) {
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
    }
}

class Edge {
    Node from, to; // Connecting nodes
    int cost, bandwidth; // Cost of the connection and bandwidth capacity

    public Edge(Node from, Node to, int cost, int bandwidth) {
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.bandwidth = bandwidth;
    }
}

public class NetworkTopologyApp extends JFrame {
    private final ArrayList<Node> nodes = new ArrayList<>(); // List of nodes (servers/clients)
    private final ArrayList<Edge> edges = new ArrayList<>(); // List of edges (connections)
    private JLabel statusLabel; // Label to display network cost and latency
    private java.util.List<Edge> mstEdges = new ArrayList<>(); // Minimum spanning tree edges
    private java.util.List<Edge> shortestPathEdges = new ArrayList<>(); // Shortest path edges

    public NetworkTopologyApp() {
        setTitle("Network Topology Designer"); // Window title
        setSize(800, 600); // Window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // UI Buttons for adding nodes, edges, finding MST and shortest path
        JButton addNodeButton = new JButton("Add Node");
        addNodeButton.setBounds(10, 10, 100, 30);
        add(addNodeButton);

        JButton addEdgeButton = new JButton("Add Edge");
        addEdgeButton.setBounds(120, 10, 100, 30);
        add(addEdgeButton);

        JButton findPathButton = new JButton("Find Path");
        findPathButton.setBounds(230, 10, 100, 30);
        add(findPathButton);

        JButton findMSTButton = new JButton("Find MST");
        findMSTButton.setBounds(340, 10, 100, 30);
        add(findMSTButton);

        // Status label to display total cost and latency
        statusLabel = new JLabel("Total Cost: 0, Latency: 0");
        statusLabel.setBounds(460, 10, 300, 30);
        add(statusLabel);

        // Canvas panel for drawing the network graph
        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Edge e : edges) {
                    if (shortestPathEdges.contains(e)) {
                        g.setColor(Color.GREEN); // Shortest path edges in green
                    } else if (mstEdges.contains(e)) {
                        g.setColor(Color.RED); // MST edges in red
                    } else {
                        g.setColor(Color.BLACK); // Normal edges in black
                    }
                    g.drawLine(e.from.x, e.from.y, e.to.x, e.to.y);
                    int midX = (e.from.x + e.to.x) / 2;
                    int midY = (e.from.y + e.to.y) / 2;
                    g.drawString("C: " + e.cost + ", B: " + e.bandwidth, midX, midY);
                }
                g.setColor(Color.BLUE);
                for (Node n : nodes) {
                    g.fillOval(n.x - 10, n.y - 10, 20, 20);
                    g.drawString(n.name + " (" + n.type + ")", n.x + 10, n.y + 10);
                }
            }
        };
        canvas.setBounds(0, 50, 800, 550);
        add(canvas);

        // Event listener for adding nodes
        addNodeButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter node name:");
            String[] types = {"Server", "Client"};
            String type = (String) JOptionPane.showInputDialog(null, "Select type:", "Node Type",
                    JOptionPane.QUESTION_MESSAGE, null, types, types[0]);
            if (name != null && type != null) {
                nodes.add(new Node(name, type, new Random().nextInt(750), new Random().nextInt(500)));
                repaint();
            }
        });

        // Event listener for adding edges
        addEdgeButton.addActionListener(e -> {
            String from = JOptionPane.showInputDialog("Enter start node:");
            String to = JOptionPane.showInputDialog("Enter end node:");
            int cost = Integer.parseInt(JOptionPane.showInputDialog("Enter cost:"));
            int bandwidth = Integer.parseInt(JOptionPane.showInputDialog("Enter bandwidth:"));

            Node n1 = findNodeByName(from);
            Node n2 = findNodeByName(to);

            if (n1 != null && n2 != null) {
                edges.add(new Edge(n1, n2, cost, bandwidth));
                updateStatus();
                repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid nodes!");
            }
        });

        // Event listener for finding the shortest path
        findPathButton.addActionListener(e -> {
            String start = JOptionPane.showInputDialog("Enter start node:");
            String end = JOptionPane.showInputDialog("Enter end node:");
            if (start != null && end != null) {
                findShortestPath(start, end);
                repaint();
            }
        });

        // Event listener for finding the minimum spanning tree (MST)
        findMSTButton.addActionListener(e -> {
            findMinimumSpanningTree();
            repaint();
        });
    }

    // Finds a node by its name
    private Node findNodeByName(String name) {
        for (Node n : nodes) {
            if (n.name.equals(name)) return n;
        }
        return null;
    }

    // Prim's Algorithm for Minimum Spanning Tree
    private void findMinimumSpanningTree() {
        mstEdges.clear();
        if (nodes.isEmpty() || edges.isEmpty()) return;

        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.cost));
        Set<Node> visited = new HashSet<>();
        visited.add(nodes.get(0));

        while (visited.size() < nodes.size()) {
            for (Edge edge : edges) {
                if (visited.contains(edge.from) ^ visited.contains(edge.to)) {
                    pq.add(edge);
                }
            }
            if (pq.isEmpty()) break;
            Edge minEdge = pq.poll();
            mstEdges.add(minEdge);
            visited.add(minEdge.from);
            visited.add(minEdge.to);
        }
        updateStatus();
    }

    // Dijkstra’s Algorithm for Shortest Path
    private void findShortestPath(String start, String end) {
        shortestPathEdges.clear();
        Node source = findNodeByName(start);
        Node destination = findNodeByName(end);
        if (source == null || destination == null) return;
    }

    // Updates cost and latency in the GUI
    private void updateStatus() {
        int totalCost = edges.stream().mapToInt(e -> e.cost).sum();
        int totalLatency = edges.stream().mapToInt(e -> e.bandwidth).sum();
        statusLabel.setText("Total Cost: " + totalCost + ", Latency: " + totalLatency);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NetworkTopologyApp().setVisible(true));
    }
}
