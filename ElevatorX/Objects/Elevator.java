package Objects;

import java.util.ArrayList;

public class Elevator {
    
    public int _currentRow;
    public int _column;
    public char _elevatorIdChar;
    public ArrayList<Integer> _elevatorCalls;
    public boolean _isGoingUp;
    public boolean _isGoingDown;
    public boolean _wantsToGoUp;
    public boolean _wantsToGoDown;
    public boolean _isResetting;
    private int _idleForNIntervals;
    public boolean _doneForInterval;

    public Elevator() {
        _currentRow = 0;
        _elevatorCalls = new ArrayList<Integer>();
        _isResetting = false;
        _isGoingUp = false;
        _isGoingDown = false;
        _idleForNIntervals = 0;
        _doneForInterval = false;
    }

    public String GetElevatorView() {
        return "[" + _elevatorIdChar + "]";
    }

    public int CurrentlyGoingTo() {
        return !_elevatorCalls.isEmpty() ? _elevatorCalls.get(0) : -11;
    }

    public void SetElevatorId(int id) {
        _elevatorIdChar =  (char) ((id + 1) + 64);
    }

    public void CallElevatorTo(int floor) {
        _idleForNIntervals = 0;
        //_doneForInterval = false;
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
        if (!AtGroundLevel() && _elevatorCalls.isEmpty()) {
            _idleForNIntervals += 1;
            if (_idleForNIntervals >= 2) {
                _isResetting = true;
                _isGoingDown = true;
                _isGoingUp = false;
                CallElevatorTo(0);
                _idleForNIntervals = 0;
            }
        }
    }

    private void MoveElevator() {
        if (_doneForInterval) {
            return;
        }
        // If elevator is at ground level and has a call
        if (AtGroundLevel() && !_elevatorCalls.isEmpty()) {
            if (_wantsToGoUp) _isGoingUp = true;
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
                if (_currentRow == 0) {
                    _isGoingDown = false;
                    _isGoingUp = false;
                }
                _doneForInterval = true;
                _isResetting = false;
                _elevatorCalls.remove(i);
            }
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
        if (_currentRow == CurrentlyGoingTo()) return;

        if (_currentRow < CurrentlyGoingTo()) {
            _isGoingUp = true;
            _isGoingDown = false;
        } else if (_currentRow > CurrentlyGoingTo()) {
            _isGoingDown = true;
            _isGoingUp = false;
        }
    }
}