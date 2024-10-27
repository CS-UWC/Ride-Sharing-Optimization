// This class represents a connection between two nodes and its associated travel cost.
public class Edge {
	
	 private Node edgeStart; //Starting node of the edge.
	 private Node edgeEnd;   //Ending node of the edge.
	 private double weight; //Travel distance or cost associated with the edge.

	 public Edge(Node edgeStart, Node edgeEnd, double weight) {
		 //Constructor
		 this.edgeStart = edgeStart;
	     this.edgeEnd = edgeEnd;
	     this.weight = weight;
	 }

	public Node getEdgeStart() {
		return edgeStart;
	}

	public void setEdgeStart(Node edgeStart) {
		this.edgeStart = edgeStart;
	}

	public Node getEdgeEnd() {
		return edgeEnd;
	}

	public void setEdgeEnd(Node edgeEnd) {
		this.edgeEnd = edgeEnd;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public void printEdge(Edge edge) {
		System.out.println("Start Node " + getEdgeStart().getId() + " End Node " + edgeEnd.getId());
	}
	

}
