package elevator;

import api.AbstractElevator;

public class Passenger implements Runnable {

	private int id;
	private int destinationFloor;
	private int fromFloor;
	private Building building;

	public Passenger(int id, int from, int dest) {
		this.id = id;
		destinationFloor = dest;
		fromFloor = from;
	}
	
	public Passenger(Building b, int id, int from, int dest) {
	    this(id, from, dest);
            setBuilding(b);
    }

	public void setBuilding(Building b) {
		building = b;
	}

	@Override
	public void run() {
	    AbstractElevator elevator = null;
	    if (fromFloor < destinationFloor) {
	        //In case no match is found
	        while(elevator == null) {
	            elevator = building.CallUp(fromFloor);
	        }
	    } //TODO implement someone that comes and leaves from same floor
	    else if (fromFloor > destinationFloor){
	        while(elevator == null) {
	            elevator = building.CallDown(fromFloor);
	        }
	    }
	    
	    RideElevator(elevator);

	}
	
	private synchronized void RideElevator(AbstractElevator elevator) {

	    //Whats the difference between synchronizing the method or the elevator?
	    while (elevator.getCurrentFloor() != fromFloor) {
	        try {
	            elevator.wait();
	        }
	        catch (InterruptedException e) {
	            // TODO Log error
	            e.printStackTrace();
	        }
	    }
	    elevator.Enter();
	    //TODO Log passenger entering
	    elevator.RequestFloor(destinationFloor);
	    //TODO Log floor request
	    while (elevator.getCurrentFloor() != destinationFloor) {
	        try {
	            elevator.wait();
	        } catch (InterruptedException e) {
	            // TODO Write error on log
	            e.printStackTrace();
	        }
	    }
	    elevator.Exit();
	    //TODO Log passenger exiting

	}

}
