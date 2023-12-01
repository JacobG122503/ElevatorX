import java.util.Scanner;

import Objects.World;

/**
 * Author: Jacob Garcia
 * Version Number: 1.0.1
 * Last Updated: 8/15/23
 */
public class Startup {
    
    private static int _nmbOfFloors;
    private static int _nmbOfElevators;
    private static int _ticks;
    private static final String CYAN = "\u001B[36m";
    private static final String WHITE = "\u001B[0m";

    public static void main(String args[]) throws InterruptedException {

        ElevatorGUI gui = new ElevatorGUI();
        gui.setVisible(true);

        boolean confirmed = false;
        Scanner scnr = new Scanner(System.in);

        Clear();
        System.out.println("Welcome To");
        Thread.sleep(300);
        System.out.println("ElevatorX");
        Thread.sleep(300);

        System.out.println("\nPress Enter to start");
        scnr.nextLine();
        
        while (!confirmed) {
            Clear();
            System.out.println("How many floors would you like your building to have?");
            _nmbOfFloors = scnr.nextInt();

            System.out.println("How many elevators would you like your building to have?");
            _nmbOfElevators = scnr.nextInt();

            System.out.println("How many ticks would you like to have per interval? (1 tick = 1/4 second)");
            _ticks = scnr.nextInt();
            System.out.println(CYAN + "#########################################################################" + WHITE);
            System.out.println("Is this correct? Floors: " + _nmbOfFloors + ", Elevators: " + _nmbOfElevators);
            System.out.println("Ticks per interval: " + _ticks);
            System.out.println("y or n");

            String result = scnr.next();

            if (result.equals("y")) {
                confirmed = true;
            }
        }

        Start(scnr);
        scnr.close();
    }

    private static void Start(Scanner scnr) throws InterruptedException {
        System.out.print("Starting program");
        for (int i = 0; i < 5; i++) {
            Thread.sleep(100);
            System.out.print(".");
        }
        Clear();

        World world = new World(_nmbOfFloors, _nmbOfElevators, _ticks, scnr);
        world.Commence();
    }

    private static void Clear() {
        System.out.print("\033\143");
    }
}