import java.util.Random;

/**
 * @author Danya Polishchuk
 * @information Student IV-83, number 18
 * Task 318:
 */

public class MOPE_lab2 {

    static Random random = new Random();
    static int myNumber = 18;
    static int m = 5;
    static int y_max = (30 - myNumber) * 10;
    static int y_min = (20 - myNumber) * 10;
    static float[][] matrix_X = new float[][]{{-1, -1}, {1, -1}, {-1, 1}};
    static float[][] matrix_Y = new float[3][m];
    static float[] matrix_AverageOfY = new float[3];
    static float[] dispersia = new float[3];
    static float sigma = (float) Math.sqrt((float) (2 * (2 * m - 2)) / (float) (m * (m - 4)));
    static float[] F_uv = new float[3];
    static float[] O_uv = new float[3];
    static float[] R_uv = new float[3];
    static float[] mx = new float[2];
    static float my = 0;
    static float a1 = 0;
    static float a2 = 0;
    static float a3 = 0;
    static float a11 = 0;
    static float a22 = 0;
    static float[][] matrix_b;
    static float[][] matrix_b1;
    static float[][] matrix_b2;
    static float[][] matrix_b3;
    static float b0 = 0;
    static float b1 = 0;
    static float b2 = 0;
    static float x1min = 20;
    static float x1max = 70;
    static float x2min = -15;
    static float x2max = 45;
    static float[][] matrixForNaturalization = new float[][]{{x1min, x1max}, {x2min, x2max}};
    static float delta_x1 = Math.abs(x1max - x1min) / 2;
    static float delta_x2 = Math.abs(x2max - x2min) / 2;
    static float x10 = (x1max + x1min) / 2;
    static float x20 = (x2max + x2min) / 2;
    static float a_0 = 0;
    static float a_1 = 0;
    static float a_2 = 0;

    public static void main(String[] args) {

        showY();
        fillMatrixOfY();
        averageOfY();
        fillDispersia();
        criterionRomanovskiy();

    }

    private static void showY() {
        System.out.println("Мій номер в списку: " + myNumber + "\n\ny_max = (30 - 18)*10 = " + y_max
                + "\ny_min = (20 - 18)*10 = " + y_min + "\n");
    }

    private static void fillMatrixOfY() {
        for (int i = 0; i < matrix_Y.length; i++) {
            for (int j = 0; j < matrix_Y[i].length; j++) {
                matrix_Y[i][j] = (float) (random.nextInt(y_max + 1));
                if (matrix_Y[i][j] < (float) y_min) {
                    matrix_Y[i][j] += (float) y_min;
                }
            }
        }
        iteration(matrix_Y);
    }

    private static void iteration(float[][] array) {
        if (array == matrix_Y) {
            System.out.println("Значення функції відгуку в інтервалі від [" + y_min + "-" + y_max + "]:");
        }
        for (float[] i : array) {
            for (float j : i) {
                System.out.print(j + "    ");
            }
            System.out.println();
        }
    }

    private static void iteration(float[] array) {
        if (array == matrix_AverageOfY) {
            System.out.println("\nСереднє значення функції відгуку в кожній точці плану:");
        } else if (array == dispersia) {
            System.out.println("\nДисперсії для кожної точки планування:");
        }
        for (float i : array) {
            System.out.print(i + "\n");
        }
    }

    private static void averageOfY() {
        for (int i = 0; i < matrix_Y.length; i++) {
            matrix_AverageOfY[i] = averageValueOfY(matrix_Y[i]);
        }
        iteration(matrix_AverageOfY);
    }

    private static float averageValueOfY(float[] array) {
        float sum = 0;
        for (float i : array) {
            sum += i;
        }
        return sum / array.length;
    }

    private static void fillDispersia() {
        for (int i = 0; i < matrix_Y.length; i++) {
            float sum = 0;
            for (int j = 0; j < matrix_Y[i].length; j++) {
                sum += Math.pow((matrix_Y[i][j] - matrix_AverageOfY[i]), 2);
            }
            dispersia[i] = sum / matrix_Y[i].length;
        }
        iteration(dispersia);
        System.out.println("\nОсновне відхилення:\nsigma = " + sigma);
    }

    private static void criterionRomanovskiy() {
        F_uv[0] = calculateF(dispersia[0], dispersia[1]);
        F_uv[1] = calculateF(dispersia[0], dispersia[2]);
        F_uv[2] = calculateF(dispersia[1], dispersia[2]);
        for (int i = 0; i < O_uv.length; i++) {
            O_uv[i] = ((float) (m - 2) / (float) m) * F_uv[i];
            R_uv[i] = Math.abs(O_uv[i] - 1) / sigma;
        }
        boolean bool = false;
        System.out.println("\nЗа критерієм Романовського:");
        for (int i = 0; i < R_uv.length; i++) {
            System.out.print("R_uv" + i + " = " + R_uv[i]);
            if (R_uv[i] < 2) {
                System.out.print(" < " + " R_кр = 2");
            } else {
                System.out.print(" > " + " R_кр = 2");
                bool = true;
            }
            System.out.println();
        }
        if (bool) {
            System.out.println("\nОтже, дисперсія не однорідна...");
        } else {
            System.out.println("\nОтже, дисперсія однорідна.");
            calculateNormCoefficients();
            naturalizationCoefficients();
        }
    }

    private static float calculateF(float dispersia1, float dispersia2) {
        if (dispersia1 >= dispersia2) {
            return dispersia1 / dispersia2;
        } else {
            return dispersia2 / dispersia1;
        }
    }

    private static void calculateNormCoefficients() {
        for (int i = 0; i < matrix_X[0].length; i++) {
            mx[i] = calculateAverage(matrix_X[0][i], matrix_X[1][i], matrix_X[2][i]);
        }
        my = calculateAverage(matrix_AverageOfY[0], matrix_AverageOfY[1], matrix_AverageOfY[2]);
        a1 = calculateAverage((float) Math.pow(matrix_X[0][0], 2), (float) Math.pow(matrix_X[1][0], 2), (float) Math.pow(matrix_X[2][0], 2));
        a2 = calculateAverage(matrix_X[0][0] * matrix_X[0][1], matrix_X[1][0] * matrix_X[1][1], matrix_X[2][0] * matrix_X[2][1]);
        a3 = calculateAverage((float) Math.pow(matrix_X[0][1], 2), (float) Math.pow(matrix_X[1][1], 2), (float) Math.pow(matrix_X[2][1], 2));
        a11 = calculateAverage(matrix_X[0][0] * matrix_AverageOfY[0], matrix_X[1][0] * matrix_AverageOfY[1], matrix_X[2][0] * matrix_AverageOfY[2]);
        a22 = calculateAverage(matrix_X[0][1] * matrix_AverageOfY[0], matrix_X[1][1] * matrix_AverageOfY[1], matrix_X[2][1] * matrix_AverageOfY[2]);
        matrix_b = new float[][]{{1, mx[0], mx[1]}, {mx[0], a1, a2}, {mx[1], a2, a3}};
        matrix_b1 = new float[][]{{my, mx[0], mx[1]}, {a11, a1, a2}, {a22, a2, a3}};
        matrix_b2 = new float[][]{{1, my, mx[1]}, {mx[0], a11, a2}, {mx[1], a22, a3}};
        matrix_b3 = new float[][]{{1, mx[0], my}, {mx[0], a1, a11}, {mx[1], a2, a22}};
        b0 = discriminant(matrix_b1) / discriminant(matrix_b);
        b1 = discriminant(matrix_b2) / discriminant(matrix_b);
        b2 = discriminant(matrix_b3) / discriminant(matrix_b);
        System.out.println();
        System.out.println("Розрахунок нормованих коефіцієнтів рівняння регресії:");
        for (int i = 0; i < matrix_X.length; i++) {
            for (int j = 0; j < matrix_X[i].length - 1; j++) {
                System.out.println("y = b0 + b1*x1 + b2*x2 = " + b0 + " + " + b1 + "*" + matrix_X[i][j] + " + " +
                        b2 + "*" + matrix_X[i][j + 1] + " = " + (b0 + b1 * matrix_X[i][j] + b2 * matrix_X[i][j + 1]));
            }
        }
    }

    private static void naturalizationCoefficients() {
        a_0 = b0 - b1 * (x10 / delta_x1) - b2 * (x20 / delta_x2);
        a_1 = b1 / delta_x1;
        a_2 = b2 / delta_x2;
        System.out.println("\nЗапишемо натуралізоване рівняння регресії:");
        System.out.println("y = a0 + a1*x1 + a2*x2 = " + a_0 + " + " + a_1 + "*" + x1min + " + " +
                a_2 + "*" + x2min + " = " + (a_0 + a_1 * x1min + a_2 * x2min));
        System.out.println("y = a0 + a1*x1 + a2*x2 = " + a_0 + " + " + a_1 + "*" + x1max + " + " +
                a_2 + "*" + x2min + " = " + (a_0 + a_1 * x1max + a_2 * x2min));
        System.out.println("y = a0 + a1*x1 + a2*x2 = " + a_0 + " + " + a_1 + "*" + x1min + " + " +
                a_2 + "*" + x2max + " = " + (a_0 + a_1 * x1min + a_2 * x2max));

    }

    private static float calculateAverage(float... arg) {
        float sum = 0;
        for (float i : arg) {
            sum += i;
        }
        return sum / arg.length;
    }

    private static float discriminant(float[][] matrix) {
        return (matrix[0][0] * matrix[1][1] * matrix[2][2]) + (matrix[1][0] * matrix[2][1] * matrix[0][2])
                + (matrix[0][1] * matrix[1][2] * matrix[2][0]) - ((matrix[2][0] * matrix[1][1] * matrix[0][2])
                + (matrix[0][0] * matrix[2][1] * matrix[1][2]) + (matrix[1][0] * matrix[0][1] * matrix[2][2]));
    }
}