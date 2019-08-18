package nir.model.route;

public class Step {
    double rad;
    int lenght;

    public Step(double rad, int len){
        this.lenght = len;
        this.rad = rad;
    }

    public int getLenght() {
        return lenght;
    }

    public double getRad() {
        return rad;
    }
}
