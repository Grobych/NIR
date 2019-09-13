package nir.model;

import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class RobotAgent extends Robot {
    public RobotAgent(int x, int y) {
        super(x, y);
        this.speed = 2;
        movedRoute.add(new Coordinate(x,y));
    }

    private int up = 0;

    public void incUp(int val){
        this.up += val;
    }
    public int getUp(){
        return up;
    }
    private List<Coordinate> movedRoute = new ArrayList<>();
    public void clearMovedRoute(){
        movedRoute.clear();
    }
    public void redirect(Coordinate coordinate){
        this.xPosition = coordinate.x;
        this.yPosition = coordinate.y;
    }
    public int getMoveSize(){
        return movedRoute.size();
    }
    public List<Coordinate> getMovedRoute(){
        return this.movedRoute;
    }
}
