package nir.model;

import org.locationtech.jts.geom.Coordinate;

public class Goal {
    private Coordinate coordinate;
    private int R;

    public Goal(Coordinate c){
        this.coordinate = c;
        this.R = 3;
    }
    public Goal(int x, int y){
        this.coordinate = new Coordinate(x,y);
        this.R = 3;
    }

    public double getY() {
        return coordinate.y;
    }

    public double getX() {
        return coordinate.x;
    }

    public int getR() {
        return R;
    }
}
