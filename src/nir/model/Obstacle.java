package nir.model;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

public class Obstacle extends AbstractObject {

    public LinearRing coords;
    public Polygon polygon;

    public Obstacle(Coordinate[] coords){
        this.coords = new GeometryFactory().createLinearRing(coords);
        polygon = new GeometryFactory().createPolygon(this.coords);
    }
}
