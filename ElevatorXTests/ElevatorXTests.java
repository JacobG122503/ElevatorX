package ElevatorXTests;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import Objects.Building;
import Objects.Elevator;

public class ElevatorXTests {
    
    @Test
    public void CreateElevatorsReturnsNotNull() {
        Building building = new Building(15, 5, null);
        ArrayList<Elevator> list = building.GetElevatorList();
        for (Elevator elevator : list) {
            assertNotNull(elevator, "Elevator in list is null");
        }
    }
}
