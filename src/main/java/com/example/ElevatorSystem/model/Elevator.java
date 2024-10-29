package com.example.ElevatorSystem.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@AllArgsConstructor
@Data
public class Elevator {
    int id;
    int currentFloor;
    Direction direction;
    ElevatorStatus status;
    List<Integer> floorsAssigned; //this is given by scheduler service
}
//up-> 3,5,1
//down->