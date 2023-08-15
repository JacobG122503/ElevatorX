package Objects;

import java.util.ArrayList;
import java.util.Scanner;

public class Building {
    private int _columns;
    private int _rows;
    private Scanner _scnr;
    private ArrayList<Elevator> _elevators;
    private String[][] _building;
    private final String GREEN = "\u001B[32m";
    private final String WHITE = "\u001B[0m";
    private final String BLUE = "\u001B[34m";
    private final String RED = "\u001B[31m";
    private int _ticks;
    private ArrayList<Elevator> _hasArrivedElevators;

    public Building(int floors, int nmbOfElevators, Scanner scnr) {
        _rows = floors;
        _columns = (nmbOfElevators * 2) + 1;
        _scnr = scnr;
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
                    _building[r][c] = GREEN + selected.GetElevatorView();
                    continue;
                }
                _building[r][c] = WHITE + " O ";
            }
        }

        //Print Building
        for (int r = _rows - 1; r >= 0; r--) {
            //Print floor number and spaces to the left
            String floorNmb = (""+ (r+1));
            System.out.print(BLUE + floorNmb);
            for (int i = 3 - floorNmb.length(); i > 0; i--) {
                System.out.print(" ");
            }
            //Print building + elevators
            System.out.print(WHITE);
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
            _elevators.add(new Elevator(_rows-1));
            _elevators.get(i)._column = columnPlacement;
            _elevators.get(i).SetElevatorId(i);
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
            System.out.print(BLUE);
            calledRow = _scnr.nextInt();
            System.out.print(WHITE);
            //Account for the + 1 in the display
            calledRow -= 1;
            if (calledRow >= _rows || calledRow < 0) {
                continue;
            }
            System.out.println("Would you like to send the elevator 'up' or 'down' ");
            System.out.print(BLUE);
            String UpOrDown = _scnr.next();
            System.out.print(WHITE);
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
            System.out.println(RED + "ERROR: Your call was not possible or all elevators are busy. Try again. ");
            System.out.print(WHITE);
            _scnr.nextLine();
            return;
        }
        elevator.CallElevatorTo(calledRow);
        Clear();
        Build();
    }
    
    public Elevator FindNearestElevator(int floor, boolean wantToGoUp, boolean wantToGoDown) {
        int[] distances = new int[_elevators.size()];

        //Checking to make sure call is actually possible.
        if (floor == _rows-1 && wantToGoUp == true) return null;
        if (floor == 0 && wantToGoDown == true) return null;


        //Get array of all elevators distances 
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
            //If elevator is at ground level and idle
            if (currentElevator.AtGroundLevel() && currentElevator.IsIdle()) {
                distanceCandidates.add(Math.abs(currentDistance));
                closestElevatorCandidates.add(currentElevator);
                continue;
            }

            int currentlyGoingTo = currentElevator.CurrentlyGoingTo();
            if (currentlyGoingTo == -11) continue;

            //When elevator is already going up AND elevators target floor is above called floor AND elevator is below floor called.
            if (currentElevator._isGoingUp && floor < currentlyGoingTo && floor > currentElevator._currentRow && wantToGoUp && currentElevator._wantsToGoUp) {
                distanceCandidates.add(Math.abs(currentDistance));
                closestElevatorCandidates.add(currentElevator);
            }
            //When elevator is already going down AND elevators target is below called floor AND elevator is above the floor called 
            if (currentElevator._isGoingDown && floor > currentlyGoingTo && floor < currentElevator._currentRow && wantToGoDown && currentElevator._wantsToGoDown) {
                distanceCandidates.add(currentDistance);
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
        
        closestElevator._wantsToGoUp = wantToGoUp;
        closestElevator._wantsToGoDown = wantToGoDown;

        return closestElevator;
    }

    public void Tick() {
        for (Elevator elevator : _elevators) {
            Elevator result = elevator.Tick();
            if (result != null) {
                _hasArrivedElevators.add(elevator);
            }
        }
        _ticks += 1;
    }

    public void FinishUpInterval() {
        for (Elevator elevator : _elevators) {
            elevator.CheckIfNeedsResetting();
            elevator._doneForInterval = false;
        }
    }

    public void CheckAndPrintArriveStrings() {
        ArrayList<Elevator> toRemove = new ArrayList<>();
        if (!_hasArrivedElevators.isEmpty()) {
            for (Elevator elevator : _hasArrivedElevators) {
                var list = AvailableNextFloors(elevator);
                if (list.isEmpty()) continue;
                System.out.println(GREEN + "Elevator " + elevator.GetElevatorView() + " has arrived at floor " + BLUE + (elevator._currentRow + 1));
                System.out.print(WHITE);
                System.out.println("Where would you like to send it next? Type 0 for elevator to continue ");
                boolean correctlyTyped = false;
                while (!correctlyTyped) {
                    System.out.println("Available floors to go to: " + BLUE + list.toString());
                    int nextCall = _scnr.nextInt();
                    if (nextCall == 0) break;
                    if (list.indexOf(nextCall) == -1) continue;
                    System.out.print(WHITE);
                    nextCall -= 1;
                    if (nextCall == 0) {
                        elevator._isResetting = true;
                    }
                    if (nextCall >= _rows || nextCall < 0) {
                        continue;
                    }
                    correctlyTyped = true;
                    elevator.CallElevatorTo(nextCall);
                }
                toRemove.add(elevator);
            }
            _hasArrivedElevators.removeAll(toRemove);
            Clear();
            Build();
        }
    }

    private static void Clear() {
        System.out.print("\033\143");
    }


    private ArrayList<Integer> AvailableNextFloors(Elevator elevator) {
        ArrayList<Integer> floorList = new ArrayList<>();
        //If at top, send back down.
        if (elevator._currentRow == _rows - 1) {
            elevator._wantsToGoDown = true;
            elevator._wantsToGoUp = false;
        }

        if (elevator._wantsToGoUp) {
            for (int i = elevator._currentRow + 1; i < _rows; i++) {
                floorList.add(i+1);
            }
        }

        if (elevator._wantsToGoDown) {
            for (int i = elevator._currentRow + 1; i > 1; i--) {
                floorList.add(i-1);
            }
        }

        return floorList;
    }

    public ArrayList<Elevator> GetElevatorList() {
        return _elevators;
    }
}
