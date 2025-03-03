import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Node {
    int x, y;
    String name;

    public Node(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
}

class Edge {
    Node from, to;
    int cost, bandwidth;

    public Edge(Node from, Node to, int cost, int bandwidth) {
        this.from = from;
        this.to = to;
        this.cost = cost;
        this.bandwidth = bandwidth;
    }
}

public class NetworkTopologyApp extends JFrame {
    private final ArrayList<Node> nodes = new ArrayList<>();
    private final ArrayList<Edge> edges = new ArrayList<>();
    private Node selectedNode = null;

    public NetworkTopologyApp() {
        setTitle("Network Topology Designer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JButton addNodeButton = new JButton("Add Node");
        addNodeButton.setBounds(10, 10, 100, 30);
        add(addNodeButton);

        JButton addEdgeButton = new JButton("Add Edge");
        addEdgeButton.setBounds(120, 10, 100, 30);
        add(addEdgeButton);

        JButton findPathButton = new JButton("Find Path");
        findPathButton.setBounds(230, 10, 100, 30);
        add(findPathButton);

        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Edge e : edges) {
                    g.drawLine(e.from.x, e.from.y, e.to.x, e.to.y);
                    int midX = (e.from.x + e.to.x) / 2;
                    int midY = (e.from.y + e.to.y) / 2;
                    g.drawString("C: " + e.cost + ", B: " + e.bandwidth, midX, midY);
                }
                for (Node n : nodes) {
                    g.fillOval(n.x - 10, n.y - 10, 20, 20);
                    g.drawString(n.name, n.x + 10, n.y + 10);
                }
            }
        };
        canvas.setBounds(0, 50, 800, 550);
        add(canvas);

        addNodeButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter node name:");
            if (name != null) {
                nodes.add(new Node(name, new Random().nextInt(750), new Random().nextInt(500)));
                repaint();
            }
        });

        addEdgeButton.addActionListener(e -> {
            String from = JOptionPane.showInputDialog("Enter start node:");
            String to = JOptionPane.showInputDialog("Enter end node:");
            int cost = Integer.parseInt(JOptionPane.showInputDialog("Enter cost:"));
            int bandwidth = Integer.parseInt(JOptionPane.showInputDialog("Enter bandwidth:"));

            Node n1 = findNodeByName(from);
            Node n2 = findNodeByName(to);

            if (n1 != null && n2 != null) {
                edges.add(new Edge(n1, n2, cost, bandwidth));
                repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid nodes!");
            }
        });

        findPathButton.addActionListener(e -> {
            String start = JOptionPane.showInputDialog("Enter start node:");
            String end = JOptionPane.showInputDialog("Enter end node:");
            if (start != null && end != null) {
                findShortestPath(start, end);
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Node n : nodes) {
                    if (Math.abs(n.x - e.getX()) < 10 && Math.abs(n.y - e.getY()) < 10) {
                        selectedNode = n;
                        return;
                    }
                }
            }
        });
    }

    private Node findNodeByName(String name) {
        for (Node n : nodes) {
            if (n.name.equals(name)) return n;
        }
        return null;
    }

    private void findShortestPath(String start, String end) {
        HashMap<Node, Integer> distances = new HashMap<>();
        HashMap<Node, Node> previous = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        Node startNode = findNodeByName(start);
        Node endNode = findNodeByName(end);

        if (startNode == null || endNode == null) {
            JOptionPane.showMessageDialog(null, "Invalid nodes!");
            return;
        }

        for (Node node : nodes) distances.put(node, Integer.MAX_VALUE);
        distances.put(startNode, 0);
        pq.add(startNode);

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            for (Edge edge : edges) {
                if (edge.from.equals(current)) {
                    Node neighbor = edge.to;
                    int newDist = distances.get(current) + edge.cost;
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        previous.put(neighbor, current);
                        pq.add(neighbor);
                    }
                }
            }
        }

        ArrayList<Node> path = new ArrayList<>();
        for (Node at = endNode; at != null; at = previous.get(at)) path.add(at);
        Collections.reverse(path);

        JOptionPane.showMessageDialog(null, "Shortest Path: " + path);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NetworkTopologyApp().setVisible(true));
    }
}
