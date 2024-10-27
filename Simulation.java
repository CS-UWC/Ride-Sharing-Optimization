//Simulation with heuristic 1
//This heuristic assigns ride request to vehicles based on which one is closest to the pick up point of
//the ride request using the h(n) value to determine this

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Simulation {
	
	private List<Vehicle> vehicles; // List of vehicles in the simulation
    private Queue<RideRequest> rideRequests; // Queue to store incoming ride requests
    private int timeInterval; // Current time interval in the simulation
    private Graph graph; // The graph representing the road network
    
    // Constructor to initialize the simulation
    public Simulation(Graph graph, List<Vehicle> vehicles, Queue<RideRequest> rideRequests, int timeInterval) {
        this.graph = graph;
        this.vehicles = vehicles;
        this.rideRequests = rideRequests;
        this.timeInterval = timeInterval;
    }
	
    public void runSimulation() {
    	//Not necessary to have time passed in as a parameter anymore, can remove it later
    	int count = 1;
        while (count <= timeInterval) {
            System.out.println("Time Interval: " + count);
            allocateRideRequests(count); // Allocate incoming ride requests to vehicles
            moveVehicles(); // Move vehicles to their next node based on A* algorithm
            count++; // Increment the time interval
            
            //Add the logic to stop the simulation if all the riderequest have been fulfilled here.
            if (rideRequests.isEmpty() && vehicles.stream().allMatch(Vehicle::isEmpty)) {
                System.out.println("All ride requests have been fulfilled. Stopping the simulation.");
                break; // Stop the simulation once all ride requests are fulfilled
            }
        }
    }
	
	// Method to allocate ride requests to the most suitable vehicle
    private void allocateRideRequests(int currentTime) {
    	
    	// Add ride requests for the current time interval
    	Queue<RideRequest> newRequests = new LinkedList<>();
    	
    	for (RideRequest request : rideRequests) {
    		if (request.getTimeInterval() == currentTime) {
                newRequests.add(request); // Add to active ride requests
                System.out.println("RideRequest " + request.getRideRequestID() + " entered the simulation at time " + currentTime);
            }
        }
    	
    	rideRequests.removeAll(newRequests);
    	
    	//Allocates ride request to vehicles based off which vehicle is closest to the pickup point of the ride request.
    	while (!newRequests.isEmpty()) {
            RideRequest request = newRequests.poll(); // Get the next ride request
            Vehicle assignedVehicle = null;
            double minDistance = Double.MAX_VALUE; // Initialize minimum distance as maximum possible

            // Find the closest vehicle to the ride request's pick-up point
            for (Vehicle vehicle : vehicles) {
                // Skip vehicles that already have a ride request assigned
                if (!vehicle.isEmpty()) {
                    continue;
                }

                Node vehicleCurrentNode = vehicle.getCurrentNode(); // Use the vehicle's current node
                double distance = calculateHeuristic(vehicleCurrentNode, request.getPickUpPoint());

                // Assign the ride request to the vehicle with the minimum distance
                if (distance < minDistance) {
                    minDistance = distance;
                    assignedVehicle = vehicle;
                }
            }

            // If a suitable vehicle is found, add the ride request to it
            if (assignedVehicle != null && assignedVehicle.addRideRequest(request)) {
                System.out.println("Assigned " + request.getRideRequestID() + " to " + assignedVehicle.getVehicleID());
            } else {
                System.out.println("No available vehicle for Ride Request " + request.getRideRequestID() + ".");
                request.setTimeInterval(request.getTimeInterval()+1);
                rideRequests.add(request); // Requeue the request for future time intervals
            }
        }
    }
    
    // Method to move each vehicle based on the A* algorithm
    private void moveVehicles() {
    	for (Vehicle vehicle : vehicles) {
            if (!vehicle.isEmpty()) { // Only move vehicles with active ride requests
                RideRequest currentRide = vehicle.getRideRequests().get(0); // Get the first ride request
                Node currentNode = vehicle.getCurrentNode(); // Vehicle's current position

                Node destination;
                if (!vehicle.hasPassenger()) {
                    // If the vehicle hasn't picked up the passenger, move towards the pickup point
                    destination = currentRide.getPickUpPoint();
                    System.out.println(vehicle.getVehicleID() + " is heading to pickup point " + destination.getId());
                } else {
                    // If the vehicle has picked up the passenger, move towards the drop-off point
                    destination = currentRide.getDropOffPoint();
                    System.out.println(vehicle.getVehicleID() + " is heading to drop-off point " + destination.getId());
                }

                Node nextNode = aStarNextMove(currentNode, destination); // Determine the next node using A*

                if (nextNode != null) {
                    System.out.println(vehicle.getVehicleID() + " moving from " + currentNode.getId() + " to " + nextNode.getId());
                    vehicle.setCurrentNode(nextNode); // Update vehicle's current node
                } 

                // Check if the vehicle has reached its destination
                if (currentNode.equals(destination)) {
                    if (!vehicle.hasPassenger()) {
                        // If it has reached the pickup point, set hasPassenger to true
                        vehicle.setHasPassenger(true);
                        System.out.println(vehicle.getVehicleID() + " has picked up a passenger.");
                    } else {
                        // If it has reached the drop-off point, complete the ride
                        vehicle.setHasPassenger(false);
                        vehicle.getRideRequests().remove(0); // Remove the completed ride request
                        System.out.println(vehicle.getVehicleID() + " has dropped off the passenger.");
                    }
                }
            }
        }
    }

 // A* algorithm to determine the next node for the vehicle to move to
    private Node aStarNextMove(Node startNode, Node stopNode) {
        //attributes are in a local scope because they need to be recalculated after every interval based off new information. So no need to store it.
    	Set<Node> openSet = new HashSet<>(); // Set of nodes to be evaluated
        Set<Node> closedSet = new HashSet<>(); // Set of nodes already evaluated
        Map<Node, Double> gScore = new HashMap<>(); // Map to store the cost of the cheapest path from startNode to each node
        Map<Node, Node> cameFrom = new HashMap<>(); // Map to store the best parent node for each node

        openSet.add(startNode);
        gScore.put(startNode, 0.0); // The cost to reach the start node is 0

        while (!openSet.isEmpty()) {
            Node current = null;

            // Find the node in openSet with the lowest fScore (gScore + heuristic)
            for (Node node : openSet) {
                if (current == null || gScore.get(node) + calculateHeuristic(node, stopNode) < gScore.get(current) + calculateHeuristic(current, stopNode)) {
                    current = node;
                }
            }

            // If the stopNode is reached, return the node leading to it
            if (current.equals(stopNode)) {
                return reconstructPath(cameFrom, current);
            }

            openSet.remove(current);
            closedSet.add(current);

            // Evaluate neighbors of the current node
            for (Edge edge : graph.getEdges(current)) {
                Node neighbor = edge.getEdgeEnd();
                if (closedSet.contains(neighbor)) continue; // Ignore already evaluated nodes

                double tentativeGScore = gScore.get(current) + edge.getWeight(); // gScore for the neighbor

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor); // Discover a new node
                } else if (tentativeGScore >= gScore.get(neighbor)) {
                    continue; // This path is not better
                }

                // This path is the best so far, record it
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentativeGScore);
                
            }
        }

        return null; // No path found
    }

    // Reconstruct the path from start to stop node by following the cameFrom map
    private Node reconstructPath(Map<Node, Node> cameFrom, Node current) {
        List<Node> path = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
        	
            path.add(current);
            current = cameFrom.get(current);
            
         
        }
        Collections.reverse(path);
       
        
     // Check if the path is empty before accessing it
        if (!path.isEmpty()) {
            return path.get(0); // Return the first node in the path (next move)
        } else {
            return null; // Return null or handle it in the calling method
        }
    }
    
    
 // Heuristic function using the Haversine formula for great-circle distance
    private double calculateHeuristic(Node currentNode, Node goalNode) {
        double R = 6371.0; // Earth's radius in kilometers

        // Get latitude and longitude in radians
        double lat1 = Math.toRadians(currentNode.getLatitude());
        double lon1 = Math.toRadians(currentNode.getLongitude());
        double lat2 = Math.toRadians(goalNode.getLatitude());
        double lon2 = Math.toRadians(goalNode.getLongitude());

        // Haversine formula
        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;
        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dlon / 2) * Math.sin(dlon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Return the distance between the two nodes
        return R * c; // Distance in kilometers
    }
    
    public static void main(String[] args) {
    	//Will read in values from the datasets I attached in the pictures 
    	//So the graph, list of vechiles, list of ride request and time intervals will all be setup 
    	//Before I call the simulation to start running.
    	//Then the simulation will run and using all that predefined information and will just use the a star algorithm
    	//And move the vechiles one step per time interval
    	//I was thinking of reading each step into a csv file so we can see exactly where each vechile is moving per step.
    	
    	// Create the Graph dataset
        Graph graph = new Graph();
        
        // Adding Nodes to the graph (10 nodes with latitude and longitude values)
        Node node1 = new Node("Node1", -33.9249, 18.4241);  // Cape Town City Center
        Node node2 = new Node("Node2", -33.9180, 18.4233);  // Bo-Kaap
        Node node3 = new Node("Node3", -33.9300, 18.4232);  // Gardens
        Node node4 = new Node("Node4", -33.9106, 18.3945);  // Sea Point
        Node node5 = new Node("Node5", -33.9398, 18.4662);  // Observatory
        Node node6 = new Node("Node6", -33.9590, 18.4705);  // Rondebosch
        Node node7 = new Node("Node7", -33.9165, 18.4160);  // Green Point
        Node node8 = new Node("Node8", -33.8675, 18.5061);  // Bloubergstrand
        Node node9 = new Node("Node9", -34.0409, 18.6197);  // Muizenberg
        Node node10 = new Node("Node10", -33.9849, 18.4695); // Claremont
        
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        graph.addNode(node4);
        graph.addNode(node5);
        graph.addNode(node6);
        graph.addNode(node7);
        graph.addNode(node8);
        graph.addNode(node9);
        graph.addNode(node10);
        
        // Adding Edges to the graph
        graph.addEdge(node1, node2, 10);  
        graph.addEdge(node2, node3, 15);
        graph.addEdge(node3, node4, 20);   
        graph.addEdge(node4, node5, 25); 
        graph.addEdge(node5, node6, 30);
        graph.addEdge(node6, node7, 35);
        graph.addEdge(node7, node8, 40); 
        graph.addEdge(node8, node9, 45); 
        graph.addEdge(node9, node10, 50);

        // Adding more connections between nodes to create intricate pathways
        graph.addEdge(node1, node3, 12);
        graph.addEdge(node2, node4, 18);
        graph.addEdge(node3, node5, 22);
        graph.addEdge(node4, node6, 28);
        graph.addEdge(node5, node7, 32);
        graph.addEdge(node6, node8, 38);
        graph.addEdge(node7, node9, 42);
        graph.addEdge(node8, node10, 48);

        graph.addEdge(node1, node4, 25);
        graph.addEdge(node2, node5, 30);
        graph.addEdge(node3, node6, 35);
        graph.addEdge(node4, node7, 40);
        graph.addEdge(node5, node8, 45);
        graph.addEdge(node6, node9, 50);
        graph.addEdge(node7, node10, 55);

        
        //Create a list of Vehicles (3 vehicles)
        List<Vehicle> vehicles = new ArrayList<>();
        
        // Add vehicle objects to the list
        Vehicle vehicle1 = new Vehicle("Vehicle1", node1, 3);  // vehicle with ID 1 starting at node1, seat capacity 4
        Vehicle vehicle2 = new Vehicle("Vehicle2", node4, 4);  // vehicle with ID 2 starting at node2, seat capacity 6
        Vehicle vehicle3 = new Vehicle("Vehicle3", node6, 6);  // vehicle with ID 3 starting at node3, seat capacity 3
        
        vehicles.add(vehicle1);
        vehicles.add(vehicle2);
        vehicles.add(vehicle3);
        
        //Create a queue of RideRequests
        //Consider changing to a normal list
        Queue<RideRequest> rideRequests = new LinkedList<>();
        
        // Add ride request objects to the queue (requests between different nodes)
        RideRequest request1 = new RideRequest("RideRequest1", node2, node5, 1); // request from node1 to node5
        RideRequest request5 = new RideRequest("RideRequest2", node3, node10, 1); // request from node1 to node5
        RideRequest request6 = new RideRequest("RideRequest3", node7, node1, 1); // request from node1 to node5
        RideRequest request2 = new RideRequest("RideRequest4",node5, node4, 4); // request from node2 to node4
        RideRequest request3 = new RideRequest("RideRequest5",node3, node7, 6); // request from node3 to node1
        RideRequest request4 = new RideRequest("RideRequest6",node3, node10, 6); // request from node3 to node1
        
        
        rideRequests.add(request1);
        rideRequests.add(request2);
        rideRequests.add(request3);
        rideRequests.add(request4);
        rideRequests.add(request5);
        rideRequests.add(request6);
        
        //Set the timeInterval
        int timeInterval = 20; // 10-time intervals for the simulation
        
        //Pass datasets to SimulationRun constructor
        Simulation simulation = new Simulation(graph, vehicles, rideRequests, timeInterval);
        
        // Simulation is ready to run
        simulation.runSimulation(); // Assuming there's a run method in SimulationRun
    	
    	
    }
}
