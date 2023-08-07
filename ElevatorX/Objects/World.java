package Objects;
public class World {
    
    private Building _building;

    public World(int nbmOfFloors, int nmbOfElevators) {

        _building = new Building(nbmOfFloors, nmbOfElevators);
        _building.Build();
    }
}