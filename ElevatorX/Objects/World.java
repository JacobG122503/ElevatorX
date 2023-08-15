package Objects;

import java.util.Scanner;

public class World {
    
    public Building _building;
    private int _ticks;
    private Scanner _scnr;

    public World(int nbmOfFloors, int nmbOfElevators, int ticks, Scanner scnr) {

        _building = new Building(nbmOfFloors, nmbOfElevators, scnr);
        _ticks = ticks;
        _scnr = scnr;
    }

    public void Commence() {
        String result = "";
        _building.Build();

        while (!result.equals("s")) {
            _building.CheckAndPrintArriveStrings();
            System.out.println("Press c to call an elevator or n to go to next interval. s to stop program");
            result = _scnr.next();

            if (result.equals("c")) {
                _building.CallElevator();
                continue;
            } 
            else if (result.equals("s")) {
                Clear();
                continue;
            }

            TickToNextInterval();
        }
    }

    
    public void TickToNextInterval() {
        for (int i = 0; i < _ticks; i++) {
            try {
                Thread.sleep(250);
            } 
            catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            Clear();
            _building.Tick();
            _building.Build();
        }
        _building.FinishUpInterval(); 
    }

    private static void Clear() {
        System.out.print("\033\143");
    }
}