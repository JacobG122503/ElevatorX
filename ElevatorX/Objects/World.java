package Objects;

import java.util.Scanner;

public class World {
    
    private Building _building;
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

        while (!result.equals("s")) { //TODO Add demolish building option and animation
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
            if (_building._intervals >= 1) {
                _building.CheckForIdleElevators(); //TODO BREAKPOINT HERE
            }
        }
    }

    
    private void TickToNextInterval() {
        for (int i = 0; i < _ticks; i++) {
            try {
                Thread.sleep(333);
            } 
            catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            Clear();
            _building.Tick();
            _building.Build();
        }
        _building._intervals += 1;
    }

    private static void Clear() {
        System.out.print("\033\143");
    }
}