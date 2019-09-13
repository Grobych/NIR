package nir.logic;

import nir.model.Robot;
import nir.model.route.Route;
import org.locationtech.jts.geom.Coordinate;

public class RobotMoveThread extends Thread {
    private Robot robot;
    private Coordinate coord;
    private boolean onRoute = false;
    public void setRoute(Route route) {
        robot.setRoute(route);
        onRoute = true;
    }

    public void setCoord(Coordinate coord) {
        this.coord = coord;
        onRoute = false;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
        if (robot.getRoute()!=null) onRoute = true;
    }

    @Override
    public void run(){
        if (onRoute) robot.moveRoute();
        else robot.move(coord);
    }
}
