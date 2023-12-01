import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ElevatorGUI extends JFrame {

    public ElevatorGUI() {
        // Set up the frame properties
        setTitle("Elevator Simulation");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold your components
        JPanel mainPanel = new JPanel();

        // Create a button
        JButton elevatorButton = new JButton("Press me");
        
        // Add the button to the panel
        mainPanel.add(elevatorButton);

        // Add the panel to the frame
        add(mainPanel);
    }
}
