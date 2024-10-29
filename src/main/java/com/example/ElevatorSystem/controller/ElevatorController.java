package com.example.ElevatorSystem.controller;

import com.example.ElevatorSystem.model.Direction;
import com.example.ElevatorSystem.model.InternalButton;
import com.example.ElevatorSystem.service.ElevatorService;

public class ElevatorController {
    ElevatorService elevatorService;

    public ElevatorController(ElevatorService elevatorService){
        this.elevatorService = elevatorService;
    }

    public void handleInternalButtonRequest(InternalButton internalButton){
        elevatorService.handleInternalButtonRequest(internalButton.getDestinationFloor());
    }

    public void handleExternalButtonRequest(int floor, Direction direction){
        elevatorService.handleExternalButtonRequest(floor,direction);
    }
}
