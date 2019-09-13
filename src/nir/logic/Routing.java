package nir.logic;

import nir.model.Map;
import nir.model.RobotAgent;
import org.locationtech.jts.geom.Coordinate;

import java.util.*;

public class Routing implements Runnable {

    public static List<RobotAgent> agents;
    public static HashMap<Coordinate,Double> pheromoneMap = new HashMap();
    Coordinate start , goal;
    private int iterationNumber, currentIteration;
    private int agentsNumber;
    private double phInit = 1;
    private double phThreshold = 0.1;
    private double phEx = 1000;
    private double movingDist = 20;

    private double gC, phC, rC;

    private static boolean goalTaken = false;
    public static boolean isGoalTaken(){
        return goalTaken;
    }
    public Coordinate getStart(){
        return this.start;
    }

    public Coordinate getGoal() {
        return goal;
    }

    public Routing(Coordinate start, Coordinate goal, int iterations, int agentsNamber, double phInit){
        this.start = start;
        this.goal = goal;
        this.iterationNumber = iterations;
        this.agentsNumber = agentsNamber;
        this.phInit = phInit;
        createAgents();
    }

    public void setParams(double goal, double ph, double rand){
        this.gC = goal;
        this.phC = ph;
        this.rC = rand;
    }

    public void stop(){
        pheromoneMap.clear();
        agents.clear();
        createAgents();
    }

    @Override
    public void run() {
        for (int i = 0; i < iterationNumber; i++) {
            for (RobotAgent agent : agents) {
                do {
                    Coordinate point = getCoordinate(agent,goal);
                    move(agent,point);
                    putPheromone(agent);
//                    showPh();
                    if (goalTaken(agent)) {
                        goalTaken = true;
                        expandPheromone(agent);
                        redirect(agent,start);
                        break;
                    }
                    if (agentMoveTooLong(agent)){
                        redirect(agent,start);
                        break;
                    }
                } while (true);
            }
            unitePheromone();
            evaporatePheromone();
        }
        //        savePheromoneAsVerticles();

    }

    private void unitePheromone() {
        Coordinate keys[] = pheromoneMap.keySet().toArray(new Coordinate[pheromoneMap.keySet().size()]);
        for (int i = 0; i < keys.length -1; i++) {
            for (int j = i+1; j < keys.length; j++) {
                if (keys[i].distance(keys[j]) < 3 && pheromoneMap.get(keys[i])!= 0 && pheromoneMap.get(keys[j])!= 0){
                    Coordinate n = new Coordinate((keys[i].x+keys[j].x)/2,(keys[i].y+keys[j].y)/2);
                    Double v = pheromoneMap.get(keys[i]) + pheromoneMap.get(keys[j]);
                    pheromoneMap.put(n,v);
                    pheromoneMap.replace(keys[i],0d);
                    pheromoneMap.replace(keys[j],0d);
                }
            }
        }
        Iterator<Coordinate> iterator = pheromoneMap.keySet().iterator();
        while (iterator.hasNext()){
            Coordinate c = iterator.next();
            if (pheromoneMap.get(c) == 0) iterator.remove();
        }
    }

    private void showPh() {
        for (Coordinate c : pheromoneMap.keySet()) {
            System.out.print(c + " "+pheromoneMap.get(c) + ";  ");
        }
        System.out.println();
    }

    synchronized private void expandPheromone(RobotAgent agent) {
        Double routeLength = calculateRouteLenght(agent);
        for (Coordinate coordinate : agent.getMovedRoute()) {
            if (coordinate.distance(start) < 1) break;
            pheromoneMap.replace(coordinate,pheromoneMap.get(coordinate) + phEx / routeLength);
        }
    }

    private Double calculateRouteLenght(RobotAgent agent) {
        Double res = 0d;
        for (int i = 0; i < agent.getMovedRoute().size() - 1; i++) {
            res += agent.getMovedRoute().get(i).distance(agent.getMovedRoute().get(i+1));
        }
        return res;
    }

    private boolean agentMoveTooLong(RobotAgent agent) {
        if (agent.getMoveSize() > 150) return true;
        else return false;
    }

    private boolean goalTaken(RobotAgent agent) {
        if (goal.distance(agent.getPosition()) < 20) return true;
        else return false;
    }

    synchronized private void evaporatePheromone() {
        for (Coordinate coordinate : pheromoneMap.keySet()) {
            Double val = pheromoneMap.get(coordinate);
            if (val > phThreshold) pheromoneMap.replace(coordinate,pheromoneMap.get(coordinate) * 0.9);
            else pheromoneMap.remove(coordinate);
        }
    }

    private void redirect(RobotAgent agent, Coordinate start) {
        agent.clearMovedRoute();
        agent.redirect(start);
    }

    private Coordinate getCoordinate(RobotAgent agent, Coordinate goal) {
        double[] goalVector = getRotation(agent, goal);
        double[] pheromoneVector = getRotation(agent);
        double[] randomVector = getRotation();

        checkHight(agent,goalVector);
        checkHight(agent,pheromoneVector);
        checkHight(agent,randomVector);

//        System.out.println("goal: "+goalVector[0]+" "+goalVector[1]);
//        System.out.println("pher: "+pheromoneVector[0]+" "+pheromoneVector[1]);
//        System.out.println("rand: "+randomVector[0]+" "+randomVector[1]);

        Coordinate res = calculatePoint(goalVector, pheromoneVector, randomVector, agent);
        return res;

    }
    private Coordinate calculatePoint(double v1[], double v2[], double v3[], RobotAgent agent){
        double resV[] = new double[2];
        resV[0] = v1[0]*gC + v2[0] *phC + v3[0] * rC;
        resV[1] = v1[1]*gC + v2[1] *phC + v3[1] * rC;
        Coordinate result = new Coordinate(agent.getPosition().x + resV[0],agent.getPosition().y + resV[1]);
        return result;
    }
    private double[] checkHight(RobotAgent agent, double v[]){
        int x1 = (int) agent.getPosition().x;
        int y1 = (int) agent.getPosition().y;
        int x2 = x1 + (int) v[0];
        int y2 = y1 + (int) v[1];
        int delta = Map.getLevelUping(x1,y1,x2,y2);
        System.out.println("delta: "+delta);
//        System.out.println("before: "+v[0]+" "+v[1]);
        if (delta > 3) {
            v[0] = v[0] * (4d / delta);
            v[1] = v[1] * (4d / delta); //TODO: choose different coef
        }
//        System.out.println("after: "+v[0]+" "+v[1]);
        return v;
    }

    private double[] getRotation(RobotAgent agent, Coordinate goal) {
        Coordinate r = agent.getPosition();
        double vector[] = new double[2];
        vector[0] = (goal.x - r.x) / r.distance(goal) * movingDist * 0.8;
        vector[1] = (goal.y - r.y) / r.distance(goal) * movingDist * 0.8;
        return vector;
    }

    private double[] getRotation(RobotAgent agent){
        double vector[] = new double[2];
        double temp[] = new double[2];
        for (Coordinate coordinate : pheromoneMap.keySet()) {
            if (agent.getPosition().distance(coordinate) < 20){
                temp[0] = coordinate.x - agent.getPosition().x;
                temp[1] = coordinate.y - agent.getPosition().y;
                vector[0] += temp[0] * pheromoneMap.get(coordinate) / 2;
                vector[1] += temp[1] * pheromoneMap.get(coordinate) / 2;
            }
        }

        return vector;
    }

    private double[] getRotation(){
        double vector[] = new double[2];
        vector[0] = new Random().nextDouble()*40 -20;
        vector[1] = new Random().nextDouble()*40 -20;
        return vector;
    }

    private void createAgents() {
        agents = new ArrayList<>();
        for (int i = 0; i < agentsNumber; i++) {
            agents.add(new RobotAgent((int) start.x, (int) start.y));
        }
    }

    synchronized private void move(RobotAgent agent, Coordinate point){
        do {
            agent.incUp(Map.getLevelUping((int)agent.getPosition().x,(int)agent.getPosition().y,(int)point.x,(int)point.y));
            if (!agent.move(point)) break;
        } while (agent.getPosition().distance(point) > 3);
        agent.getMovedRoute().add(agent.getPosition());
    }



    synchronized private void putPheromone(RobotAgent agent){
        Coordinate coordinate = agent.getPosition();
        if (pheromoneMap.containsKey(coordinate)) {
            Double val = pheromoneMap.get(coordinate) + phInit;
            pheromoneMap.put(coordinate,val);
        } else {
            pheromoneMap.put(coordinate,phInit);
        }
    }
}
