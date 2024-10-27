//This class represents a vehicle, including its capacity, assigned ride requests and route. 

import java.util.ArrayList;
import java.util.List;

public class Vehicle {
	
	private String vehicleID;
	private Node currentNode;
	private List<RideRequest> rideRequests; //List of ride requests assigned to the vehicle.
	private List<RideRequest> pickedUpRequests; // List of ride requests that have been picked up.
    private int seatCapacity; // Maximum number of passengers the vehicle can have.
    private Route route; // Route of the Vehicle.
    private boolean hasPassenger; // To track whether the vehicle has picked up a passenger 
    private int totalActiveTime;
    private int totalOccupancy;
    
    public Vehicle(String vechileID, Node currentNode, int seatCapacity){
    	this.vehicleID = vechileID;
    	this.currentNode = currentNode;
    	this.seatCapacity = seatCapacity;
    	this.rideRequests = new ArrayList<>();
    	this.pickedUpRequests = new ArrayList<>(); 
    	this.hasPassenger = false;
    	this.totalActiveTime = 0;
    	this.totalOccupancy = 0;
    }
    
    public Vehicle(int seatCapacity) {
        this.seatCapacity = seatCapacity;
    }
    
    public double getTotalPathCost() {
    //Calculates and returns the total cost of the vehicle's current route.
        return route.getRouteCost();
    }

	public List<RideRequest> getRideRequests() {
		return rideRequests;
	}
	
	public boolean addRideRequest(RideRequest request) {
	   	
		// Check if there is enough room for more ride requests or picked-up requests
	    if (rideRequests.size() + pickedUpRequests.size() < seatCapacity) {
	        rideRequests.add(request);
	        return true;
	    }
	    return false;
	}
	
	public boolean isEmpty() {
        return rideRequests.isEmpty()&& pickedUpRequests.isEmpty();
    }
	
	public void removeCompletedRequest(RideRequest request) {
        rideRequests.remove(request);
    }

	public void setRideRequests(List<RideRequest> rideRequests) {
		//Is a list for multiple for multiple riderequest for one vechile
		this.rideRequests = rideRequests; 
	}

	public String getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(String vehicleID) {
		this.vehicleID = vehicleID;
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}

	public int getSeatCapacity() {
		return seatCapacity;
	}

	public void setSeatCapacity(int seatCapacity) {
		this.seatCapacity = seatCapacity;
	}
	
	// Getter and setter for hasPassenger
    public boolean hasPassenger() {
        return hasPassenger;
    }

    public void setHasPassenger(boolean hasPassenger) {
        this.hasPassenger = hasPassenger;
    }

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		//Current node 1st node in list
		this.route = route;
	}

	public List<RideRequest> getPickedUpRequests() {
		return pickedUpRequests;
	}

	public void setPickedUpRequests(List<RideRequest> pickedUpRequests) {
		this.pickedUpRequests = pickedUpRequests;
	}
	
	// Method to add a ride request to the list of picked-up requests
	public void addPickedUpRequest(RideRequest request) {
		pickedUpRequests.add(request);
	}
		
	// Method to remove a ride request from the list of picked-up requests after drop-off
	public void removePickedUpRequest(RideRequest request) {
		pickedUpRequests.remove(request);
	}

	public int getTotalActiveTime() {
		return totalActiveTime;
	}

	public void setTotalActiveTime(int totalActiveTime) {
		this.totalActiveTime = totalActiveTime;
	}

	public int getTotalOccupancy() {
		return totalOccupancy;
	}

	public int getCurrentOccupancy() {
		return pickedUpRequests.size();
	}
	public void setTotalOccupancy(int totalOccupancy) {
		this.totalOccupancy = totalOccupancy;
	}
	
}
