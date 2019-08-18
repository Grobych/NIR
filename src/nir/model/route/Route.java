package nir.model.route;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

import java.util.ArrayList;
import java.util.List;

public class Route {
    public List<Coordinate> list = new ArrayList<>();

    public Route(List coords){
        this.list = coords;
    }

    public LineString getLineString(){
        return new GeometryFactory().createLineString(list.toArray(new Coordinate[list.size()]));
    }
}
