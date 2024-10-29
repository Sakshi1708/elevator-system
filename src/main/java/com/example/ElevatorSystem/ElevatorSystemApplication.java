package com.example.ElevatorSystem;

import com.example.ElevatorSystem.controller.ElevatorController;
import com.example.ElevatorSystem.model.Elevator;
import com.example.ElevatorSystem.model.ElevatorStatus;
import com.example.ElevatorSystem.model.InternalButton;
import com.example.ElevatorSystem.service.ElevatorService;
import com.example.ElevatorSystem.service.SchedulerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

import static com.example.ElevatorSystem.model.Direction.UP;

@SpringBootApplication
public class ElevatorSystemApplication {

	public static void main(String[] args) {
		List<Elevator> elevatorList = new ArrayList<>();
		Elevator elevator1 = new Elevator(1, 2, UP, ElevatorStatus.STOP, null);
		Elevator elevator2 = new Elevator(2, 0, UP, ElevatorStatus.MOVING, null);
		elevatorList.add(elevator1);
		elevatorList.add(elevator2);
		SchedulerService schedulerService = SchedulerService.getInstance(elevatorList);
		ElevatorService elevatorService = new ElevatorService(schedulerService, elevatorList);

		ElevatorController elevatorController = new ElevatorController(elevatorService);
		elevatorService.handleExternalButtonRequest(7,UP);
		elevatorService.handleInternalButtonRequest(9);


	}

}
//Now i am left with using 2 differents queues for UP and DOWN Requests and then assign elevator when that request is getting processed.

//1.