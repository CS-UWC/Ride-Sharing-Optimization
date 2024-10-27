import java.util.*;

public class Graph {
    private Map<Node, List<Edge>> adjList; // Adjacency list representation of the graph

    public Graph() {
        adjList = new HashMap<>(); // Hashmap to store the edges in the graph
    }

    // Method to add a node to the graph
    public void addNode(Node node) {
        adjList.putIfAbsent(node, new ArrayList<>()); // Add a node to the adjacency list
    }

    // Method to add an edge between two nodes
    public void addEdge(Node from, Node to, double weight) {
        adjList.get(from).add(new Edge(from, to, weight)); // Add an edge from "from" to "to"
        
        //Remove to make graph directed.
        adjList.get(to).add(new Edge(to, from, weight)); // Assuming an undirected graph, add an edge from "to" to "from"
    }
    
    // Method to get the weight of the edge between two nodes
    public double getEdgeWeight(Node currentNode, Node nextNode) {
        List<Edge> edges = adjList.get(currentNode); // Get edges of the current node
        if (edges != null) {
            for (Edge edge : edges) {
                if (edge.getEdgeEnd().equals(nextNode)) {
                    return edge.getWeight(); // Return the weight of the edge
                }
            }
        }
        return Double.MAX_VALUE; // Return a large value if no edge is found (infinite cost)
    }

    // Method to get the weight of the edge between two nodes in an unweighted graph
    public double getEdgeWeightUnweighted(Node currentNode, Node nextNode) {
        List<Edge> edges = adjList.get(currentNode); // Get edges of the current node
        if (edges != null) {
            for (Edge edge : edges) {
                if (edge.getEdgeEnd().equals(nextNode)) {
                    return 1.0; // Return a constant value for an unweighted graph
                }
            }
        }
        return Double.MAX_VALUE; // Return a large value if no edge is found (infinite cost)
    }


    // Method to get edges connected to a node
    public List<Edge> getEdges(Node node) {
        return adjList.get(node); // Return the list of edges for the given node
    }

    // Method to get the count of nodes in the graph
    public void getNodeCount() {
        System.out.println("The graph has " + adjList.keySet().size() + " nodes.");
    }

    // Method to get the count of edges in the graph
    public void getEdgeCount() {
        int count = 0;
        for (Node node : adjList.keySet()) {
            count += adjList.get(node).size(); // Count all edges
        }
        count /= 2; // Since the graph is undirected, divide by 2 to avoid double counting
        System.out.println("The graph has " + count + " edges.");
    }

    // Method to check if a node is present in the graph
    public void hasNode(Node node) {
        if (adjList.containsKey(node)) {
            System.out.println("The graph contains " + node.getId() + " as a node.");
        } else {
            System.out.println("The graph does not contain " + node.getId() + " as a node.");
        }
    }

    // Method to check if an edge exists between two nodes
    public void hasEdge(Node from, Node to) {
        boolean hasEdge = false;
        if (adjList.containsKey(from)) {
            for (Edge edge : adjList.get(from)) {
                if (edge.getEdgeEnd().equals(to)) {
                    hasEdge = true;
                    break;
                }
            }
        }
        if (hasEdge) {
            System.out.println("The graph has an edge between " + from.getId() + " and " + to.getId() + ".");
        } else {
            System.out.println("The graph has no edge between " + from.getId() + " and " + to.getId() + ".");
        }
    }

    // Method to print the neighbors of a node
    public void neighbors(Node node) {
        if (!adjList.containsKey(node)) return;
        System.out.print("The neighbors of " + node.getId() + " are: ");
        for (Edge edge : adjList.get(node)) {
            System.out.print(edge.getEdgeEnd().getId() + ", ");
        }
        System.out.println();
    }

    // Overriding the toString() method to print the adjacency list of the graph
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Node node : adjList.keySet()) {
            builder.append(node.getId()).append(": ");
            for (Edge edge : adjList.get(node)) {
                builder.append(edge.getEdgeEnd().getId()).append(" (").append(edge.getWeight()).append("), ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
    
    public static void main(String[] args) {
    	
    	//Create nodes
        Node nodeA = new Node("A", -33.9258, 18.4232); // Cape Town, South Africa
        Node nodeB = new Node("B", -33.9280, 18.4245); // Slightly north of Node A
        Node nodeC = new Node("C", -33.9267, 18.4225); // Slightly west of Node A
        Node nodeD = new Node("D", -33.9295, 18.4218); // Slightly south-west of Node A
        // Create a graph
        Graph graph = new Graph();

        // Add nodes to the graph
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC);
        graph.addNode(nodeD);

        // Add edges to the graph
        graph.addEdge(nodeA, nodeB, 1.0); //For weight will use km values to so it has equivalent metrics for hcost.
        graph.addEdge(nodeA, nodeC, 3.0);
        graph.addEdge(nodeB, nodeC, 1.5);
        graph.addEdge(nodeC, nodeD, 2.5);

        // Print the graph's adjacency list
        System.out.println("Graph adjacency list:");
        System.out.println(graph);

        // Print the a nodes edges
        System.out.println("Node A has an edges:");
        for (Edge edge: graph.getEdges(nodeA)) {
        	edge.printEdge(edge);
        }
        System.out.println("");
        
        // Get node and edge counts
        graph.getNodeCount(); // Expected: 4 nodes
        graph.getEdgeCount(); // Expected: 4 edges (since it's undirected)

        // Check if specific nodes exist
        graph.hasNode(nodeA); // Expected: The graph contains A as a node.
        graph.hasNode(new Node("E", -33.9295, 18.4218)); // Expected: The graph does not contain E as a node.

        // Check if specific edges exist
        graph.hasEdge(nodeA, nodeB); // Expected: The graph has an edge between A and B.
        graph.hasEdge(nodeA, nodeD); // Expected: The graph has no edge between A and D.

        // Get neighbors of a node
        graph.neighbors(nodeA); // Expected: The neighbors of A are: B, C,
        graph.neighbors(nodeD); // Expected: The neighbors of D are: C,
    	
    }
    
}

