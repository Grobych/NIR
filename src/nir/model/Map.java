package nir.model;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static nir.model.util.MapUtil.loadPixelsFromImage;

public class Map {
    private static final int xSize = 1200;
    private static final int ySize = 800;

    private static int objectMap[][] = new int[xSize][ySize];
    private static int[][] levelMap = new int[xSize][ySize];

    public static int getxSize() {
        return xSize;
    }

    public static int getySize() {
        return ySize;
    }

    public static void showLevels() {
        for (int i = 0; i < xSize; i++) {
            for (int j = 0; j < ySize; j++) {
                System.out.print(levelMap[i][j] +" ");
            }
            System.out.println();
        }
    }

    public static int[][] getLevelMap() {
        return levelMap;
    }

    public int[][] getObjectMap() {
        return objectMap;
    }

    public void fillMap(int[][] object, int[][] level){
        this.levelMap = level;
        this.objectMap = object;
    }

    public static void loadLevelMap(){
        try {
            BufferedImage image = ImageIO.read(new File("levelmap2.jpg"));
            Color[][] colors = loadPixelsFromImage(image);
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    Color pix = colors[i][j];
                    float pixel[] = Color.RGBtoHSB(pix.getRed(),pix.getGreen(),pix.getBlue(),null);
//                    System.out.print(pixel[0] + " " +pixel[1]+" "+pixel[2] + "; ");
                    float level = (240f - (pixel[0] * 240f));
                    levelMap[i][j] = (int) level;
                }
//                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getLevelUping(int x1, int y1, int x2, int y2){
        try{
            int v1 = levelMap[x1][y1];
            int v2 = levelMap[x2][y2];
            if (v2 > v1) return v2 - v1;
            else return 0;
        } catch (ArrayIndexOutOfBoundsException e){
            return 40;
        }

    }
}
