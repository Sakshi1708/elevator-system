package com.example.ElevatorSystem.service;

import com.example.ElevatorSystem.model.Direction;
import com.example.ElevatorSystem.model.Elevator;
import com.example.ElevatorSystem.model.ElevatorStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SchedulerService {
    private  static SchedulerService instance = null;
    List<Elevator> elevatorList;

    public SchedulerService(List<Elevator> elevatorList){
        this.elevatorList = elevatorList;
    }
    public static SchedulerService getInstance(List<Elevator> elevatorList){
        if(instance==null){
            synchronized ((SchedulerService.class)){  //ensure thread safety
                if(instance == null){
                    instance = new SchedulerService(elevatorList);
                }
            }
        }
        return instance;
    }

    //create a singleton class. So that only one thread can be working at a time, others have to wait as there is only one instance. Reason: So that only 1 instance of schedule can assign the elevator.
    public Elevator assignElevator(int floor, Direction direction){
        Elevator bestElevator = findBestElevator( floor, direction);
        if(bestElevator!=null){
            System.out.println("Assigning elevator at floor " + bestElevator.getCurrentFloor() + " serve the request");
            bestElevator.setDirection(direction);
            bestElevator.setStatus(ElevatorStatus.MOVING);
            return bestElevator;
        }
       return null;
    }

    //for now, we are considering that elevator and requested direction should be same but there can be a chance that we have to assign from opposite side when we don't have any other available lift. How to do:
    public Elevator findBestElevator(int floor, Direction direction){
        for(Elevator elevator: elevatorList){
            if((elevator.getStatus() == ElevatorStatus.IDLE) || (elevator.getDirection() == direction && (elevator.getDirection() == Direction.UP && elevator.getCurrentFloor()<=floor) || (elevator.getDirection() == Direction.DOWN && elevator.getCurrentFloor()>=floor))){
                return elevator;
            }
        }
        return null; //Technically I should assign manual any specific elevator
    }

    public void assignDestinationFloor(Elevator bestElevator, int destinationFloor){
        List<Integer> floorsAlreadyAssigned = bestElevator.getFloorsAssigned();//6,7
        if(floorsAlreadyAssigned == null){
            floorsAlreadyAssigned = new ArrayList<>();
            bestElevator.setFloorsAssigned(floorsAlreadyAssigned);
        }
        floorsAlreadyAssigned.add(destinationFloor); //new floor added //6,7,4

        if(bestElevator.getDirection().equals(Direction.UP)){
            floorsAlreadyAssigned.sort(Comparator.naturalOrder()); //4,6,7
        }
        else if(bestElevator.getDirection().equals(Direction.DOWN)){
            floorsAlreadyAssigned.sort(Comparator.reverseOrder());
        }

        bestElevator.setFloorsAssigned(floorsAlreadyAssigned);
        System.out.println("Updated Stops for elevator: "+ floorsAlreadyAssigned);
    }
}

//5 elevator -> 4,6,7
