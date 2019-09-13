package nir.model;

import nir.logic.Intersection;
import nir.model.route.*;
import nir.model.util.Mat;
import org.locationtech.jts.geom.Coordinate;

import java.util.Random;

public class Robot extends AbstractObject {

    protected int speed = 10;
    protected double rotation;

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

    public boolean move(Coordinate coord){
        return move(coord.x,coord.y);
    }

    public boolean move(double destX, double destY){
        while (xPosition != destX || yPosition != destY){
            if ((Math.abs(destX-xPosition) < speed) && (Math.abs(destY-yPosition) < speed)) {
                xPosition = destX;
                yPosition = destY;
                return true;
            }
            double rad = Mat.getRad(xPosition,yPosition,destX,destY);
            rotation = Math.toDegrees(rad) +90;
            move(rad,speed);
            if (Intersection.isIntersect(this.getPosition(), ObstacleList.obstacles))
            {
//                System.out.println("Intersect " + this.getPosition());
                moveBack();
                return false;
            }
            //System.out.println(rad + "  "+ Math.toDegrees(rad) +"   "+xPosition +"   "+yPosition);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void moveBack() {
//        System.out.println("roration: "+ this.getRotation());
        Double rad = this.getRotation() - 180 + new Random().nextInt(180)-90;
        double newX = (this.getPosition().x + 15 * Math.cos(rad));
        double nexY = (this.getPosition().y + 15 * Math.sin(rad));
//        System.out.println("Current: " + getPosition() + "   New: "+ newX + " "+ nexY);
        this.move(newX,nexY);
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
//        this.rotation = rad;
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
