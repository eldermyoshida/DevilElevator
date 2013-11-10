package elevator;

import java.util.ArrayList;
import java.util.Collections;
import elevator.Elevator;
import api.AbstractBuilding;
import api.AbstractElevator;

public class Building extends AbstractBuilding {
	private ArrayList<Elevator> elevators;
	private int maxOccupancy;

	public Building(int numFloors, int numElevators) {
		super(numFloors, numElevators);
		maxOccupancy = 15;
		elevators = new ArrayList<Elevator>();

		for (int i = 0; i < numElevators; i++) {
			Elevator elevatorService = new Elevator(numFloors, i, maxOccupancy);
			elevators.add(elevatorService);
			Thread t = new Thread(elevatorService, "Elevator " + i);
			t.start();
		}
	}

	// int 1 = up, int -1 = down
	private AbstractElevator callCorrectElevator(Elevator elevator, int floorFrom, int upOrDown) {

		if (elevator.isIdle()) {
			elevator.startElevator(floorFrom);
			return elevator;
		}

		if (upOrDown < 0) {
			if ((elevator.isAscending() && elevator.getCurrentFloor() <= floorFrom)) {

				elevator.callToFloor(floorFrom);

				return elevator;
			}
		} else {
			if ((elevator.isAscending() && elevator.getCurrentFloor() >= floorFrom)) {

				elevator.callToFloor(floorFrom);

				return elevator;
			}
		}
		return null;
	}

	@Override
	public AbstractElevator CallUp(int fromFloor) {
		while (true) {
			shuffleElevators();
			for (Elevator elevator : elevators) {
				synchronized (elevator) {
					if (elevator.getNumberOfPassengers() == maxOccupancy)
						continue;
					return callCorrectElevator(elevator, fromFloor, -1);
				}
			}
		}
	}

	@Override
	public AbstractElevator CallDown(int fromFloor) {
		while (true) {
			shuffleElevators();
			for (Elevator elevator : elevators) {
				synchronized (elevator) {
					if (elevator.getNumberOfPassengers() == maxOccupancy)
						continue;
					return callCorrectElevator(elevator, fromFloor, 1);
				}
			}
		}
	}

	/**
	 * Adds randomness to the list being iterated, otherwise the first elevator
	 * will always be returned if available somehow
	 */
	private synchronized void shuffleElevators() {
		Collections.shuffle(elevators);
	}
}
