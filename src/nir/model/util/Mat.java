package nir.model.util;

public class Mat {
    public static double getRad(double x1, double y1, double x2, double y2){
        double difX = x2 - x1; double difY = y2 - y1;
        return Math.atan2(difY,difX);
    }
}
