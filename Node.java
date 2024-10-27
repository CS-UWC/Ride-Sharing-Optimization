
// This class represents a point in the graph, either a pick-up or drop-off point
public class Node {
    private String id;        // Uniquely identifies the node.
    private Node parentNode;  // Reference to the parent node
    private double latitude;  // Latitude of the node
    private double longitude; // Longitude of the node

    // Constructor with latitude and longitude
    public Node(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getter for id
    public String getId() {
        return id;
    }

    // Setter for id
    public void setId(String id) {
        this.id = id;
    }

    // Getter for parentNode
    public Node getParentNode() {
        return parentNode;
    }

    // Setter for parentNode
    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    // Getter for latitude
    public double getLatitude() {
        return latitude;
    }

    // Setter for latitude
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    // Getter for longitude
    public double getLongitude() {
        return longitude;
    }

    // Setter for longitude
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // Override equals and hashCode to ensure proper behavior in collections
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node node = (Node) obj;
        return id.equals(node.id);
    }

}
