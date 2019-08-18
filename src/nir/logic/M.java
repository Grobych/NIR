package nir.logic;

import nir.model.Goal;
import nir.model.route.*;
import org.locationtech.jts.geom.Coordinate;

public class M {
    public static boolean isTakingGoal(Route route, Goal goal){
        Coordinate prevStep, step;
        if (route.list.size() > 0) {
            prevStep = route.list.get(0);
            if (route.list.size() > 1){
                for (int i = 1; i < route.list.size(); i++) {
                    step = route.list.get(i);
                    if (isTakingGoal(prevStep,step,goal)) return true;
                    else {
                        prevStep = step;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isTakingGoal(Coordinate s1, Coordinate s2, Goal goal){
        return commonSectionCircle(s1.x,s1.y,s2.x,s2.y,goal.getX(),goal.getY(),goal.getR());
    }

    public static boolean commonSectionCircle(double x1, double y1, double x2, double y2, double xC, double yC, double R)
    {   x1 -= xC;
        y1 -= yC;
        x2 -= xC;
        y2 -= yC;

        double dx = x2 - x1;
        double dy = y2 - y1;

        //составляем коэффициенты квадратного уравнения на пересечение прямой и окружности.
        //если на отрезке [0..1] есть отрицательные значения, значит отрезок пересекает окружность
        double a = dx*dx + dy*dy;
        double b = 2.*(x1*dx + y1*dy);
        double c = x1*x1 + y1*y1 - R*R;

        //а теперь проверяем, есть ли на отрезке [0..1] решения
        if (-b < 0)
            return (c < 0);
        if (-b < (2.*a))
            return ((4.*a*c - b*b) < 0);

        return (a+b+c < 0);
    }


}
