package nir.view;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Rotate;
import nir.model.*;
import nir.model.route.Route;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.locationtech.jts.geom.Coordinate;

import java.util.List;


public class RenderThread extends Thread {

    @FXML
    Canvas mapCanvas;
    @FXML
    Canvas robotCanvas;
    @FXML
    Canvas utilCanvas;

    int x = 50;
    int y = 100;
    GraphicsContext mapGC, robotGC, utilGC;
    private boolean isRun = false;
    Image robotImage = new Image("file:robot.jpg");
    Image goalImage = new Image("file:res\\star.png");

    public void setMapCanvas(Canvas map, Canvas robot, Canvas util){
        this.mapCanvas = map;
        this.robotCanvas = robot;
        this.utilCanvas = util;
        mapGC = mapCanvas.getGraphicsContext2D();
        robotGC = robotCanvas.getGraphicsContext2D();
        utilGC = utilCanvas.getGraphicsContext2D();
    }

    private void clear(GraphicsContext gc){
        gc.clearRect(0,0,gc.getCanvas().getWidth(),gc.getCanvas().getHeight());
    }
    public void reset(){
        clear(utilGC);
        clear(robotGC);
        clear(mapGC);
        stopThread();
    }
    private void drawRobots(){
        for (Robot robot : RobotList.getRobotList()) {
            drawRotatedImage(robotGC,robotImage,robot.getRotation(),robot.getxPosition() - robotImage.getWidth()/2,robot.getyPosition() - robotImage.getHeight()/2);
            //drawRoute(robot);
            robotGC.save();
        }
    }
    private void drawObstacles(){
        for (Obstacle obstacle : ObstacleList.obstacles) {
            Coordinate coordinates[] = obstacle.coords.getCoordinates();
            int size = obstacle.coords.getCoordinates().length;
            double x[] = new double[size];
            double y[] = new double[size];
            for (int i = 0; i < coordinates.length; i++) {
                x[i] = coordinates[i].x;
                y[i] = coordinates[i].y;
            }
            mapGC.fillPolygon(x,y,size);
        }
        mapGC.save();
    }

    public void drawRoute(Robot robot){
        if (robot.getRoute() != null){
            drawRoute(robot.getRoute());
        }
    }
    public void drawRoute(Route route){
        Coordinate startPoint = route.list.get(0);
        utilGC.moveTo(startPoint.getX(),startPoint.getY());
        utilGC.setStroke(Paint.valueOf("#FF0000"));
        for (Coordinate o : route.list) {
            double nextX = o.x;
            double nextY = o.y;
            utilGC.lineTo(nextX,nextY);
            utilGC.stroke();
        }
        utilGC.setStroke(Paint.valueOf("#000000"));
    }
    public void drawGoals(){
        for (Goal goal : GoalList.goalList) {
            drawGoal(goal);
        }
    }

    public void drawCoordinates(List<Coordinate> coordinates){
        for (Coordinate coordinate : coordinates) {
            drawCoordinate(coordinate);
        }
    }
    public void drawGoal(Goal goal){
        utilGC.drawImage(goalImage,goal.getX()-15,goal.getY()-15,30,30);
    }
    public void stopThread(){
        isRun = false;
        //System.out.println(isRun);
    }

    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
    private void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy) {
        gc.save(); // saves the current state on stack, including the current transform
        rotate(gc, angle, tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2);
        gc.drawImage(image, tlpx, tlpy);
        gc.restore(); // back to original state (before rotation)
    }

    private void drawCoordinate(Coordinate coordinate){
        double x = coordinate.x;
        double y = coordinate.y;
        utilGC.setFill(Paint.valueOf("#00FF00"));
        utilGC.fillOval(x-5,y-5,10,10);
    }


    public void drawGraph(Graph<Coordinate, DefaultWeightedEdge> graph){
        for (Coordinate coordinate : graph.vertexSet()) {
            drawCoordinate(coordinate);
        }
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            Coordinate s = graph.getEdgeSource(edge);
            Coordinate t = graph.getEdgeTarget(edge);
            utilGC.strokeLine(s.x,s.y,t.x,t.y);
        }
    }

    @Override
    public void run(){
        isRun = true;
        drawObstacles();
        while (isRun){
            clear(robotGC);
            drawRobots();
            drawGoals();
            //System.out.println("draw");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
