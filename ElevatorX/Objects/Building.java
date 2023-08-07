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

    public Building(int floors, int nmbOfElevators, Scanner scnr) {
        _rows = floors;
        _columns = (nmbOfElevators * 2) + 1;
        _scnr = scnr;

        _building = new String[_rows][_columns];

        CreateElevators(nmbOfElevators);
    }

    public void Build() {
        //Create Building
        for (int r = 0; r < _rows; r++) {
            for (int c = 0; c < _columns; c++) {
                Elevator selected = GetElevator(r, c);
                if (selected != null) {
                    _building[r][c] = Green + "["+ selected._elevatorId +"]";
                    continue;
                }
                _building[r][c] = White + " O ";
            }
        }

        //Print Building
        for (int r = _rows - 1; r >= 0; r--) {
            for (int c = 0; c < _columns; c++) {
                System.out.print(_building[r][c]);
            }
            System.out.println();
        }
    }

    private void CreateElevators(int nmbOfElevators) {
        _elevators = new ArrayList<Elevator>();
        int columnPlacement = 1;

        //Initialize 
        for (int i = 0; i < nmbOfElevators; i++, columnPlacement += 2) {
            _elevators.add(new Elevator());
            _elevators.get(i)._column = columnPlacement;
            _elevators.get(i)._elevatorId = (char) ((i + 1) + '0');
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

    public void CallElevator() {
        System.out.println("What floor would you like to call the Elevator to?");
        int calledRow = _scnr.nextInt();
        Elevator elevator = FindNearestElevator(calledRow);

    }
    
    private Elevator FindNearestElevator(int floor) {
        int[] distances = new int[_elevators.size()];

        for (int i = 0; i < _elevators.size(); i++) {
            Elevator elevator = _elevators.get(i);
            distances[i] = Math.abs(elevator._currentRow - floor);
        }
        return null;
    }

    public void Tick() {

    }
}
