package com.example.ElevatorSystem;

import com.example.ElevatorSystem.model.Direction;
import com.example.ElevatorSystem.model.Elevator;
import com.example.ElevatorSystem.model.ElevatorStatus;
import com.example.ElevatorSystem.service.ElevatorService;
import com.example.ElevatorSystem.service.SchedulerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ElevatorSystemApplicationTests {
	List<Elevator> elevatorList;
	SchedulerService schedulerService;
	ElevatorService elevatorService;

	@BeforeEach
	void setUp(){
		elevatorList = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			elevatorList.add(new Elevator(i, 0, Direction.UP, ElevatorStatus.IDLE, new ArrayList<>()));
		}
		schedulerService = new SchedulerService(elevatorList);
		elevatorService = new ElevatorService(schedulerService,elevatorList);

	}

	@Test
	void handleExternalButtonRequest_assignElevator() {
		int floor = 3;
		Direction direction = Direction.UP;

		Elevator bestElevator = schedulerService.assignElevator(floor,direction);
		Assertions.assertNotNull(bestElevator);
		Assertions.assertEquals(ElevatorStatus.MOVING, bestElevator.getStatus());
		Assertions.assertEquals(direction,bestElevator.getDirection());
	}

	@Test
	void handleInternalButtonRequest_addDestination(){
		int destinationFloor= 5;

		Elevator bestElevator = schedulerService.assignElevator(2,Direction.UP);
		schedulerService.assignDestinationFloor(bestElevator,destinationFloor);
		elevatorService.moveElevator(bestElevator,destinationFloor);
		Assertions.assertNotNull(bestElevator);
		Assertions.assertEquals(destinationFloor,bestElevator.getCurrentFloor());
	}

	@Test
	void moveElevatorUpwards(){
		Elevator elevator = elevatorList.get(1);
		elevator.setCurrentFloor(3);

		elevatorService.moveElevator(elevator,5);
		Assertions.assertEquals(5,elevator.getCurrentFloor());
		Assertions.assertEquals(Direction.UP, elevator.getDirection());
		Assertions.assertTrue(elevator.getFloorsAssigned().isEmpty());
		Assertions.assertEquals(ElevatorStatus.IDLE, elevator.getStatus());
	}

	@Test
	void moveElevatorDownwards(){
		Elevator elevator = elevatorList.get(1);
		elevator.setCurrentFloor(5);

		elevatorService.moveElevator(elevator,1);
		Assertions.assertEquals(1,elevator.getCurrentFloor());
		Assertions.assertEquals(Direction.DOWN, elevator.getDirection());
		Assertions.assertTrue(elevator.getFloorsAssigned().isEmpty());
		Assertions.assertEquals(ElevatorStatus.IDLE, elevator.getStatus());
	}

	@Test
	public void testValidateFloorNumber_valid() {
		Elevator elevator = elevatorList.get(0);
		elevator.setCurrentFloor(3);
		elevator.setDirection(Direction.UP);

		boolean isValid = elevatorService.validateFloorNumber(elevator, 5);

		Assertions.assertTrue(isValid);
	}

	@Test
	public void testValidateFloorNumber_inValid() {
		Elevator elevator = elevatorList.get(0);
		elevator.setCurrentFloor(5);
		elevator.setDirection(Direction.DOWN);

		boolean isValid = elevatorService.validateFloorNumber(elevator, 7);

		Assertions.assertFalse(isValid);
	}

}
