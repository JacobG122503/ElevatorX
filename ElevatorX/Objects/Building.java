package Objects;

import java.util.ArrayList;
import java.util.Scanner;

public class Building {
    private int _columns;
    private int _rows;
    private Scanner _scnr;
    private ArrayList<Elevator> _elevators;
    private String[][] _building;
    private String Green = "\u001B[32m";
    private String White = "\u001B[0m";
    private String Blue = "\u001B[34m";
    private int _ticks;
    private ArrayList<String> _hasArrivedStrings;
    private ArrayList<Elevator> _hasArrivedElevators;

    public Building(int floors, int nmbOfElevators, Scanner scnr) {
        _rows = floors;
        _columns = (nmbOfElevators * 2) + 1;
        _scnr = scnr;
        _hasArrivedStrings = new ArrayList<>();
        _hasArrivedElevators = new ArrayList<>();

        _building = new String[_rows][_columns];

        CreateElevators(nmbOfElevators);
        _ticks = 0;
    }

    public void Build() {
        //Create Building
        for (int r = 0; r < _rows; r++) {
            for (int c = 0; c < _columns; c++) {
                Elevator selected = GetElevator(r, c);
                if (selected != null) {
                    _building[r][c] = Green + selected.GetElevatorView();
                    continue;
                }
                _building[r][c] = White + " O ";
            }
        }

        //Print Building
        for (int r = _rows - 1; r >= 0; r--) {
            //Print floor number and spaces to the left
            String floorNmb = (""+ (r+1));
            System.out.print(Blue + floorNmb);
            for (int i = 3 - floorNmb.length(); i > 0; i--) {
                System.out.print(" ");
            }
            //Print building + elevators
            System.out.print(White);
            for (int c = 0; c < _columns; c++) {
                System.out.print(_building[r][c]);
            }
            System.out.println();
        }
        System.out.println("Ticks elapsed: " + _ticks);
    }

    private void CreateElevators(int nmbOfElevators) {
        _elevators = new ArrayList<Elevator>();
        int columnPlacement = 1;

        //Initialize 
        for (int i = 0; i < nmbOfElevators; i++, columnPlacement += 2) {
            _elevators.add(new Elevator());
            _elevators.get(i)._column = columnPlacement;
            _elevators.get(i).SetElevatorId(i);
            // //TEST
            // if (_elevators.get(i)._elevatorIdChar == 'D') {
            //     _elevators.get(i)._currentRow = 6;
            //     _elevators.get(i)._isGoingUp= true;
            //     _elevators.get(i)._isResetting = true;
            // }
        }
    }

    private Elevator GetElevator(int row, int column) {
        Elevator selected = null;
        
        for (Elevator elevator : _elevators) {
            if (elevator._currentRow == row && elevator._column == column) {
                selected = elevator;
            }
        }
        return selected;
    }

    //Happens before tick
    public void CallElevator() {
        boolean correctlyTyped = false;
        int calledRow = -1;
        boolean wantToGoUp = false;
        boolean wantToGoDown = false;
        while (!correctlyTyped) {
            System.out.println("What floor would you like to call the elevator to?");
            calledRow = _scnr.nextInt();
            //Account for the + 1 in the display
            calledRow -= 1;
            if (calledRow > _rows || calledRow < 0) {
                continue;
            }
            System.out.println("Would you like to send the elevator 'up' or 'down' ");
            String UpOrDown = _scnr.next();
            if (UpOrDown.equals("up")) {
                wantToGoUp= true;
                wantToGoDown = false;
            } else if (UpOrDown.equals("down")) {
                wantToGoUp = false;
                wantToGoDown = true;
            } else {
                continue;
            }
            correctlyTyped = true;
        }
        Elevator elevator = FindNearestElevator(calledRow, wantToGoUp, wantToGoDown);
        if (elevator == null) {
            System.out.println("All elevators are unavailable. Please try again later. Hit enter to continue. ");
            _scnr.nextLine();
            return;
        }
        elevator.CallElevatorTo(calledRow);
        Clear();
        Build();
    }
    
    private Elevator FindNearestElevator(int floor, boolean wantToGoUp, boolean wantToGoDown) {
        int[] distances = new int[_elevators.size()];

        //Get array of all elevators distances that are going in wanted direction
        for (int i = 0; i < _elevators.size(); i++) {
            Elevator elevator = _elevators.get(i);
            distances[i] = elevator._currentRow - floor;
        }

        //Out of all distances, find all nearest elevator candidates 
        ArrayList<Integer> distanceCandidates = new ArrayList<>();
        ArrayList<Elevator> closestElevatorCandidates = new ArrayList<>();
        
        for (int i = 0; i < _elevators.size(); i++) {
            Elevator currentElevator = _elevators.get(i);
            int currentDistance = distances[i];
            if (currentElevator._isResetting) {
                continue;
            }
            //When elevator is already going up
            if (currentElevator._isGoingUp) {
                if (currentElevator._isGoingUp && currentDistance < 0 ) {
                    distanceCandidates.add(Math.abs(currentDistance));
                    closestElevatorCandidates.add(currentElevator);
                } 
            }
            //When elevator is already going down
            if (currentElevator._isGoingDown) {
                if (currentElevator._isGoingDown && currentDistance > 0) {
                    distanceCandidates.add(currentDistance);
                    closestElevatorCandidates.add(currentElevator);
                } 
            }
            //If elevator is at ground level and idle
            if (currentElevator.AtGroundLevel() && currentElevator.IsIdle()) {
                distanceCandidates.add(Math.abs(currentDistance));
                closestElevatorCandidates.add(currentElevator);
            }
            //If elevator is on floor already, return.
            if (currentDistance == 0) {
                return currentElevator;
            }
        }
        //Find smallest distance from candidate list. 
        if (distanceCandidates.isEmpty()) return null;
        Elevator closestElevator = closestElevatorCandidates.get(0);
        int closestDistance = distanceCandidates.get(0);

        for (int i = 0; i < distanceCandidates.size(); i++) {
            if (distanceCandidates.get(i) < closestDistance) {
                closestDistance = distanceCandidates.get(i);
                closestElevator = closestElevatorCandidates.get(i);
            }
        }
        // closestElevator._isGoingDown = wantToGoDown;
        // closestElevator._isGoingUp = wantToGoUp;
        return closestElevator;
    }

    public void Tick() {
        for (Elevator elevator : _elevators) {
            String result = elevator.Tick();
            if (!result.equals("")) {
                _hasArrivedStrings.add(result);
                _hasArrivedElevators.add(elevator);
            }
        }
        _ticks += 1;
    }

    public void CheckForIdleElevators() {
        for (Elevator elevator : _elevators) {
            elevator.CheckIfNeedsResetting();
        }
    }

    public void CheckAndPrintArriveStrings() {
        if (!_hasArrivedStrings.isEmpty()) {
            for (String str : _hasArrivedStrings) {

            }
        }
    }
    private static void Clear() {
        System.out.print("\033\143");
    }
}
