//This class represents a request for a ride with the pick-up and drop-off points.
public class RideRequest {
	
	private String rideRequestID;
	private Node pickUpPoint; //Node representing the pick-up location
    private Node dropOffPoint; // Node representing the drop-off location.
    private int timeInterval;
    private boolean isPickedUp;
    
    public RideRequest(String rideRequestID, Node pickUpPoint, Node dropOffPoint, int timeInterval) {
    	//Constructor
    	this.rideRequestID = rideRequestID;
        this.pickUpPoint = pickUpPoint;
        this.dropOffPoint = dropOffPoint;
        this.timeInterval = timeInterval;
        this.isPickedUp = false;
    }
    
	public String getRideRequestID() {
		return rideRequestID;
	}

	public void setRideRequestID(String rideRequestID) {
		this.rideRequestID = rideRequestID;
	}
	
	 public boolean isPickedUp() {
	    return isPickedUp;
	 }

	 // Method to mark the request as picked up
	 public void markAsPickedUp() {
	     this.isPickedUp = true;
	     System.out.println("RideRequest " + rideRequestID + " has been marked as picked up.");
	 }

	public Node getPickUpPoint() {
		return pickUpPoint;
	}

	public void setPickUpPoint(Node pickUpPoint) {
		this.pickUpPoint = pickUpPoint;
	}

	public Node getDropOffPoint() {
		return dropOffPoint;
	}

	public void setDropOffPoint(Node dropOffPoint) {
		this.dropOffPoint = dropOffPoint;
	}

	public int getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}
    
}
