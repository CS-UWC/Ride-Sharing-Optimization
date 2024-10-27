//This class represents the nodes and edges forming a path for a vehicle.
import java.util.List;

public class Route {
	
    private List<Edge> edges; //List of edges in the route.

    public Route(List<Edge> edges) {
    	//Constructor
        this.edges = edges;
    }

    public double getRouteCost() {
        return edges.stream().mapToDouble(Edge::getWeight).sum(); //Calculates and returns the total cost of the route by summing the weights of all edges.
    }

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}


}
