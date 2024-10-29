package com.example.ElevatorSystem.service;

import com.example.ElevatorSystem.model.Direction;
import com.example.ElevatorSystem.model.Elevator;
import com.example.ElevatorSystem.model.ElevatorStatus;

import java.util.List;

public class ElevatorService {
    private final SchedulerService schedulerService;
    Elevator bestElevator;

    public ElevatorService(SchedulerService schedulerService, List<Elevator> elevatorList){
        this.schedulerService = SchedulerService.getInstance(elevatorList);
        this.bestElevator = null;
    }

    public void handleExternalButtonRequest(int floor, Direction direction){ // with external request it will only assign elevator
        bestElevator = schedulerService.assignElevator(floor, direction);
    }
    public void handleInternalButtonRequest(int destinationFloor){ //with internal request, it will start moving to that direction
         if(validateFloorNumber(bestElevator,destinationFloor)){
            schedulerService.assignDestinationFloor(bestElevator, destinationFloor);
             moveElevator(bestElevator, destinationFloor);
         }

    }
    public void moveElevator(Elevator elevator, int nextFloor){
        if(elevator.getCurrentFloor()>nextFloor){
            elevator.setDirection(Direction.DOWN);
            elevator.setCurrentFloor(nextFloor);
        }
        else{
            elevator.setDirection(Direction.UP);
            elevator.setCurrentFloor(nextFloor);
        }
        List<Integer> floorAlreadyAssigned = elevator.getFloorsAssigned();
        floorAlreadyAssigned.remove(Integer.valueOf(nextFloor));
        elevator.setFloorsAssigned(floorAlreadyAssigned);
        System.out.println("The elevator is now moved to "+elevator.getCurrentFloor());
        if(elevator.getFloorsAssigned().isEmpty()){ //If there is no assigned FLOOR NOW, THEN status is idle
            elevator.setStatus(ElevatorStatus.IDLE);
        }
    }

  //Example if list is at 5th floor and going UP and user press 4th floor it should not be able to press now
    public Boolean validateFloorNumber(Elevator bestElevator, int destinationFloor){
        Direction currentDir= bestElevator.getDirection();
        if(currentDir == Direction.UP && bestElevator.getCurrentFloor()<=destinationFloor){return true;}
        else if (currentDir == Direction.DOWN && bestElevator.getCurrentFloor()>=destinationFloor){return true;}
        else{return false;}
    }
}
//addElevator
//removeElevator