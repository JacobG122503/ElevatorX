package ElevatorXTests;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.ArrayList;
import org.junit.Test;

import Objects.Building;
import Objects.Elevator;
import Objects.World;

public class ElevatorXTests {
    
    @Test
    public void CreateElevators_IsSuccess() {
        World world = new World(15, 5, 0, null);
        //Building building = new Building(15, 5, null);
        ArrayList<Elevator> list = world._building.GetElevatorList();
        for (Elevator elevator : list) {
            assertNotNull(elevator, 
            "Elevator in list is null");
        }
    }
    
    @Test 
    public void CallFirstElevator_IsA() {
        World world = new World(15, 5, 15, null);
        Elevator elevator = world._building.FindNearestElevator(5, true, false);
        elevator.CallElevatorTo(5);
        assertEquals(5, world._building.GetElevatorList().get(0)._elevatorCalls.get(0), 
        "Elevator A was not the first elevator called. ");
    }

    @Test
    public void MovementTestOne() {
        World world = new World(15, 5, 15, null);
        Building building = world._building;

        Elevator elevator = building.FindNearestElevator(14, false, true);
        elevator.CallElevatorTo(14);

        world.TickToNextInterval();
        assertEquals(14, elevator._currentRow,
        "Elevator call to 15 >> 15 Ticks >> Elevator not at floor 15.");
    }

    @Test
    public void MovementTestTwo() {
        World world = new World(15, 5, 15, null);
        Building building = world._building;

        building.FindNearestElevator(9, false, true).CallElevatorTo(9);
        world.TickToNextInterval();

        Elevator elevator = building.FindNearestElevator(1, true, false);
        elevator.CallElevatorTo(1);
        world.TickToNextInterval();

        assertEquals(1, elevator._currentRow,
        "Call A to floor 10 >> 15 Ticks >> Call B to floor 2 >> B is not at floor 2");
    }

    @Test
    public void MovementTestThree() {
        World world = new World(15, 5, 15, null);
        Building building = world._building;

        //Call A, 8, down | Call B, 3, up | Call C, 15, down 
        Elevator elevatorA = building.FindNearestElevator(7, false, true);
        elevatorA.CallElevatorTo(7);
        Elevator elevatorB = building.FindNearestElevator(2, true, false);
        elevatorB.CallElevatorTo(2);
        Elevator elevatorC = building.FindNearestElevator(14, false, true);
        elevatorC.CallElevatorTo(14);
        world.TickToNextInterval();

        //B to 12, A to 1, C to 4
        elevatorB.CallElevatorTo(11);
        elevatorA.CallElevatorTo(0);
        elevatorC.CallElevatorTo(3);
        world.TickToNextInterval();

        assertEquals(0, elevatorA._currentRow, "Elevator A is not on floor 1");
        assertEquals(11, elevatorB._currentRow, "Elevator B is not on floor 12");
        assertEquals(3, elevatorC._currentRow, "Elevator C is not on floor 4");
    }

    @Test
    public void ElevatorResets_IsSuccess() {
        World world = new World(15, 5, 15, null);
        Building building = world._building;

        Elevator elevator = building.FindNearestElevator(8, true, false);
        elevator.CallElevatorTo(8);
        world.TickToNextInterval();
        world.TickToNextInterval();
        world.TickToNextInterval();

        assertEquals(0, elevator._currentRow,
        "Elevator did not reset correctly");
    }
}
