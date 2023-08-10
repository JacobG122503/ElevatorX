package Objects;

import java.util.ArrayList;

public class Elevator {
    
    public int _currentRow;
    public int _column;
    public char _elevatorIdChar;
    private ArrayList<Integer> _elevatorCalls;
    public boolean _isGoingUp;
    public boolean _isGoingDown;
    public boolean _isResetting;

    public Elevator() {
        _currentRow = 0;
        _elevatorCalls = new ArrayList<Integer>();
        _isResetting = false;
        _isGoingUp = false;
        _isGoingDown = false;
    }

    public String GetElevatorView() {
        return "[" + _elevatorIdChar + "]";
    }

    public int CurrentlyGoingTo() {
        return !_elevatorCalls.isEmpty() ? _elevatorCalls.get(0) : null;
    }

    public void SetElevatorId(int id) {
        _elevatorIdChar =  (char) ((id + 1) + 64);
    }

    public void CallElevatorTo(int floor) {
        _elevatorCalls.add(floor);
        if (floor != _currentRow) {
            this.StartMovingTowards();
        }
    }

    public Elevator Tick() {
        MoveElevator();
        return CheckIfHasArrived();
    }

    public void CheckIfNeedsResetting() {
        //If elevator is idle and has no calls. Reset back to ground floor.
        if (_elevatorCalls.isEmpty() && !AtGroundLevel()) {
            _isResetting = true;
            _isGoingDown = true;
            CallElevatorTo(0);
        }
    }

    private void MoveElevator() {
        // If elevator is at ground level and has a call
        if (AtGroundLevel() && !_elevatorCalls.isEmpty()) {
            _isGoingUp = true;
        }
        //Move elevator
        if (_isGoingUp) {
            _currentRow += 1;
        }
        if (_isGoingDown) {
            _currentRow -= 1;
        }
    }

    private Elevator CheckIfHasArrived() {
        Elevator arrivedElevator = null;
        for (int i = 0; i < _elevatorCalls.size(); i++) {
            if (_currentRow == _elevatorCalls.get(i)) {
                if (!_isResetting) {
                    arrivedElevator = this;
                }
                _elevatorCalls.remove(i);
            }
            
        }
        if (_elevatorCalls.isEmpty()) {
            _isGoingDown = false;
            _isGoingUp = false;
            _isResetting = false;
        }
        return arrivedElevator;
    }

    public boolean AtGroundLevel() {
        if (_currentRow == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean IsIdle() {
        return !(_isGoingDown || _isGoingUp);
    }

    public void StartMovingTowards() {
        if (_currentRow < CurrentlyGoingTo()) {
            _isGoingUp = true;
            _isGoingDown = false;
        } else if (_currentRow > CurrentlyGoingTo()) {
            _isGoingDown = true;
            _isGoingUp = false;
        }
    }
}