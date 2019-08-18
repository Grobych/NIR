package nir.logic;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class Ant {
    private List<DefaultWeightedEdge> edges;
    private Coordinate currentCoordinate;

    public Ant(Coordinate coordinate){
        this.currentCoordinate = coordinate;
        this.edges = new ArrayList<>();
    }

    public Coordinate getCurrentCoordinate() {
        return currentCoordinate;
    }

    public List<DefaultWeightedEdge> getEdges() {
        return edges;
    }

    public boolean isMoved(DefaultWeightedEdge edge){
        return edges.contains(edge);
    }

    public void move(DefaultWeightedEdge edge, Coordinate coordinate){
        if (isMoved(edge)) {
            return;
        } else {
            edges.add(edge);
            currentCoordinate = coordinate;
        }
    }
}
