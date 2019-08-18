package nir.model;

public class Map {
    private static final int xSize = 1200;
    private static final int ySize = 800;

    private int objectMap[][] = new int[xSize][ySize];
    private int levelMap[][] = new int[xSize][ySize];

    public static int getxSize() {
        return xSize;
    }

    public static int getySize() {
        return ySize;
    }

    public int[][] getLevelMap() {
        return levelMap;
    }

    public int[][] getObjectMap() {
        return objectMap;
    }

    public void fillMap(int[][] object, int[][] level){
        this.levelMap = level;
        this.objectMap = object;
    }
}
