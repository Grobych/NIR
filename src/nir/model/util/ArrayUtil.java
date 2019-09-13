package nir.model.util;

public class ArrayUtil {

        private static void print2DimArray(int[][] arr) {
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length; j++) {
                    System.out.print(j + 1 < arr[i].length ? "" + arr[i][j] + ", " : "" + arr[i][j] + '\n');
                }
            }
            System.out.println("______________");
        }

        public static byte[] getOneDimArrFromTwoDimArray(byte [][] matrix) {
            int countElem = 0;
            for (byte [] tmpArr : matrix)
                countElem += tmpArr.length;
            byte [] ret = new byte[countElem];
            int indRet = 0;
            for (byte [] tmpArr : matrix)
                for (byte elemTmpArr : tmpArr)
                    ret[indRet++] = elemTmpArr;
            return ret;
        }
}
