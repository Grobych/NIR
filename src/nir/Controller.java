package nir;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import nir.logic.GraphRouting;
import nir.logic.Routing;
import nir.model.*;
import nir.model.route.Route;
import nir.view.RenderThread;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    private RenderThread renderThread;
    private GraphRouting graphRouting;
    private Routing routing;
    private Thread routingThread;

    @FXML Canvas mapCanvas, robotCanvas, utilCanvas, pheromoneCanvas;
    @FXML Button runButton, initButton, createRouteButton, clearButton, createVerticesButton;
    @FXML TextField agentsTextField, verticesTextField, antsTextField, iterationsTextField, pheromoneTextField, evaporationTextField, goalCTextField, pheromoneCTextField, randomCTextField;

    public void init(){
        RobotList.add(new Robot(30,50));
        createObstacles();
        Map.loadLevelMap();
//        MapUtil.saveImage(ArrayUtil.getOneDimArrFromTwoDimArray(Map.getLevelMap()));
//        Map.showLevels();

        GoalList.goalList.add(new Goal(800,200));
        routing = new Routing(
                new Coordinate(100,700),
                GoalList.goalList.get(0).getCoordinate(),
                20,
                Integer.parseInt(agentsTextField.getText()),
                Double.parseDouble(pheromoneTextField.getText()));
        routing.setParams(Double.parseDouble(goalCTextField.getText()),
                Double.parseDouble(pheromoneCTextField.getText()),
                Double.parseDouble(randomCTextField.getText()));
        if (renderThread != null) renderThread.reset();
        renderThread = new RenderThread();
        renderThread.setMapCanvas(mapCanvas,robotCanvas,utilCanvas, pheromoneCanvas);
        renderThread.start();

        runButton.setDisable(false);
    }
    public void runButtonClick(){
        initButton.setDisable(true);
//        RobotMoveThread robotMoveThread = new RobotMoveThread();
//        robotMoveThread.setRobot(RobotList.get(0));
//        robotMoveThread.start();
        routingThread = new Thread(routing);
        routingThread.start();
    }

    public void clearButtonClick(){
        ObstacleList.clear();
        renderThread.reset();
        RobotList.clear();
        routingThread.stop();
        routing.stop();
        initButton.setDisable(false);
    }
    private void createObstacles() {
        ObstacleList.clear();
        int o1[][] = {{200,500},{270,500},{270,550},{260,500},{220,515}};
        int o2[][] = {{200,700},{220,700},{220,730},{200,730}};
        int o3[][] = {{260,670},{270,670},{270,690},{260,690}};
        int o4[][] = {{50,600},{120,600},{120,620},{50,620}};
        createObstacle(o1);
        createObstacle(o2);
        createObstacle(o3);
        createObstacle(o4);
    }

    public void createObstacle(int[][] c){
        List<Coordinate> obs = new ArrayList<>();
        for (int i = 0; i < c.length; i++) {
            obs.add(new Coordinate(c[i][0],c[i][1]));
        }
        obs.add(new Coordinate(c[0][0],c[0][1]));
        ObstacleList.obstacles.add(new Obstacle(obs.toArray(new Coordinate[obs.size()])));
    }

    public void createVerticesButtonClick(){
        int n = Integer.parseInt(verticesTextField.getText());
        graphRouting = new GraphRouting(routing.getStart(),routing.getGoal(),n);
        graphRouting.generateCoordinates(Routing.pheromoneMap.keySet().toArray(new Coordinate[Routing.pheromoneMap.keySet().size()]), routing.getStart(), routing.getGoal());
    }

    public void createRoute(){
        if (!Routing.isGoalTaken()) return;

        graphRouting = new GraphRouting(routing.getStart(),routing.getGoal());
        graphRouting.generateCoordinates(Routing.pheromoneMap.keySet().toArray(new Coordinate[Routing.pheromoneMap.keySet().size()]), routing.getStart(), routing.getGoal());

        Integer ants = Integer.parseInt(antsTextField.getText());
        Integer iteration = Integer.parseInt(iterationsTextField.getText());
        Double pheromone = Double.parseDouble(pheromoneTextField.getText());
        Double evaporation = 0.8;
        graphRouting.setParams(ants,iteration,pheromone,evaporation);
        graphRouting.generateGraph();
        //renderThread.drawGraph(graphRouting.graph);
        Route res = graphRouting.generateRoute();
//        renderThread.drawGraph(graphRouting.graph);
        renderThread.drawRoute(res);
    }



    @FXML
    public void exitApplication(ActionEvent event) {
        stop();
        Platform.exit();
    }

    public void stop(){
        if (renderThread != null) renderThread.stopThread();
        if (routingThread != null) routingThread.stop();
    }
}
