package nir.model;

import nir.model.route.*;
import nir.model.util.Mat;
import org.locationtech.jts.geom.Coordinate;

public class Robot extends AbstractObject {

    private int speed = 10;
    private double rotation;

    private Route route;

    public Robot(int x, int y){
        this.xPosition = x;
        this.yPosition = y;
    }

    public Coordinate getPosition(){
        return new Coordinate(xPosition,yPosition);
    }

    public double getRotation() {
        //System.out.println(rotation);
        return rotation;
    }

    public void move(Coord coord){
        move(coord.getX(),coord.getY());
    }

    public void move(double destX, double destY){
        while (xPosition != destX || yPosition != destY){
            if ((Math.abs(destX-xPosition) < speed) && (Math.abs(destY-yPosition) < speed)) {
                xPosition = destX;
                yPosition = destY;
                return;
            }
            double rad = Mat.getRad(xPosition,yPosition,destX,destY);
            rotation = Math.toDegrees(rad) +90;
            move(rad,speed);
            //System.out.println(rad + "  "+ Math.toDegrees(rad) +"   "+xPosition +"   "+yPosition);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void moveRoute(Route route){
        setRoute(route);
        moveRoute();
    }
    public void moveRoute(){
        for (Coordinate point : route.list) {
            move(point.x,point.y);
        }
    }

    public void move(double rad, int speed){
        xPosition = (int) (xPosition + speed * Math.cos(rad));
        yPosition = (int) (yPosition + speed * Math.sin(rad));
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
