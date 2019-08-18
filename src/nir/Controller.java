package nir;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import nir.logic.Intersection;
import nir.logic.RobotMoveThread;
import nir.logic.Routing;
import nir.model.*;
import nir.model.route.Route;
import nir.view.RenderThread;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Controller {

    private RenderThread renderThread;
    private Routing routing;

    @FXML Canvas mapCanvas, robotCanvas, utilCanvas;
    @FXML Button runButton, initButton, createRouteButton, clearButton, createVerticesButton;
    @FXML TextField obstaclesTextField, verticesTextField;

    public void init(){
        RobotList.add(new Robot(30,50));
        createObstacles(Integer.parseInt(obstaclesTextField.getText()));
        renderThread = new RenderThread();
        renderThread.setMapCanvas(mapCanvas,robotCanvas,utilCanvas);
        renderThread.start();
    }

    public void clearButtonClick(){
        ObstacleList.clear();
        renderThread.reset();
        RobotList.clear();

    }
    private void createObstacles(int n) {
        ObstacleList.clear();
        int rx = Map.getxSize();
        int ry = Map.getySize();
        int o = 50;
        Random s = new Random();
        for (int i = 0; i < n; i++) {
            Coordinate c = new Coordinate(s.nextInt(rx)+50,s.nextInt(ry)+50);
            List<Coordinate> obs1 = new ArrayList<>();
            obs1.add(c);
            obs1.add(new Coordinate(c.x, c.y+s.nextInt(o)));
            obs1.add(new Coordinate(c.x+s.nextInt(o),c.y+s.nextInt(o)));
            obs1.add(new Coordinate(c.x+s.nextInt(o),c.y));
            obs1.add(c);
            ObstacleList.obstacles.add(new Obstacle(obs1.toArray(new Coordinate[obs1.size()])));
        }
    }

    public void createVerticesButtonClick(){
        Robot robot = RobotList.get(0);
        Coordinate goal;
        Random r = new Random();
        do {
            goal = new Coordinate(r.nextInt(200) + 800, r.nextInt(200) + 600);
            if(Intersection.isIntersect(goal,ObstacleList.obstacles)) goal = null;
        } while (goal == null);
        int n = Integer.parseInt(verticesTextField.getText());
        routing = new Routing(robot.getPosition(),goal,n);
        routing.generateCoordinates();
        renderThread.drawCoordinates(routing.coordinates);
        renderThread.drawGoal(new Goal(goal));
    }

    public void createRoute(){
        routing.generateGraph();
        //renderThread.drawGraph(routing.graph);
        Route res = routing.generateRoute();
//        renderThread.drawGraph(routing.graph);
        renderThread.drawRoute(res);
        RobotList.get(0).setRoute(res);
    }

    public void runButtonClick(){
        RobotMoveThread robotMoveThread = new RobotMoveThread();
        robotMoveThread.setRobot(RobotList.get(0));
        robotMoveThread.start();
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        stop();
        Platform.exit();
    }

    public void stop(){
        if (renderThread != null) renderThread.stopThread();
    }
}
