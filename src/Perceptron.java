import java.io.*;
import java.util.*;

class Perceptron {
    private Set<Image> images;
    private Set<Feature> randomFeatures;
    private int width;
    private int height;

    int MAX_ITER = 100;
    double LEARNING_RATE = 0.1;
    int NUM_INSTANCES = width * height;

    public static void main(String args[]) {
        new Perceptron(args[0]);
    }

    Perceptron(String fname) {
        loadData(fname);
        randomFeatures(width, height);

        int[] outputs = new int[NUM_INSTANCES];
        int[] w = new int[100];
        int[] x = new int[100];
        int[] y = new int[100];
        int[] z = new int[100];

        double[] weights = new double[5];
        weights[0] = randomNumber(0, 1);// w1
        weights[1] = randomNumber(0, 1);// w2
        weights[2] = randomNumber(0, 1);// w3
        weights[3] = randomNumber(0, 1);// w4
        weights[4] = randomNumber(0, 1);// this is the bias

        for(int i = 0; i < NUM_INSTANCES/2; i++){
            w[i] = randomNumber(3, 7);
            x[i] = randomNumber(5 , 10);
            y[i] = randomNumber(4 , 8);
            z[i] = randomNumber(2 , 9);
            outputs[i] = 1;
            System.out.println(x[i]+"\t"+y[i]+"\t"+z[i]+"\t"+outputs[i]);
        }

        double localError, globalError;
        int i, p, iteration, output;

        double[] compare = new double[4];

        iteration = 0;
        do {
            iteration++;
            globalError = 0;
            //loop through all instances (complete one epoch)
            for (p = 0; p < NUM_INSTANCES; p++) {
                // calculate predicted class
                output = calculateOutput(weights, compare);
                // difference between predicted and actual class values
                localError = outputs[p] - output;
                //update weights and bias
                weights[0] += LEARNING_RATE * localError * w[p];
                weights[1] += LEARNING_RATE * localError * x[p];
                weights[2] += LEARNING_RATE * localError * y[p];
                weights[3] += LEARNING_RATE * localError * z[p];
                weights[4] += LEARNING_RATE * localError;
                //summation of squared error (error value for all instances)
                globalError += (localError*localError);
            }
        } while (globalError != 0 && iteration<=MAX_ITER);
    }

    void loadData(String fname) {
        File file = new File(fname);
        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                scan.nextLine();
                String type = scan.nextLine();
                int col = scan.nextInt();
                int row = scan.nextInt();
                width = col;
                height = row;
                int[] numbers = new int[100];
                int i = 0;
                String line = scan.nextLine();
                for(int a = 0; a < 99; a++){
                    numbers[a] = Integer.decode(line.substring(a, a+1));
                }
                numbers[99] = Integer.decode(line.substring(99));

                System.out.println(col);
                System.out.println(row);
                for(int x = 0; x < 100; x++) {
                    System.out.println(numbers[x]);
                }
                    images.add(new Image(col, row, numbers));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    static int calculateOutput(double weights[], double[] compare) {
        double sum = compare[0] * weights[0] + compare[1] * weights[1] + compare[2] * weights[2] + weights[3] * compare[3] + weights[4];
        return (sum >= 3) ? 1 : 0;
    }

    private void randomFeatures(int width, int height) {
        for (int i = 0; i < 50; i++) {
            int col[] = {randomNumber(0, width), randomNumber(0, height), randomNumber(0, width), randomNumber(0, height)};
            int row[] = {randomNumber(0, width), randomNumber(0, height), randomNumber(0, width), randomNumber(0, height)};
            boolean sgn[] = {randomBool(), randomBool(), randomBool(), randomBool()};
            randomFeatures.add(new Feature(row, col, sgn));
        }
    }

    private static int randomNumber(int min, int max) {
        double d = min + Math.random() * (max - min);
        return (int) d;
    }

    private static boolean randomBool() {
        Double rand = Math.random();
        if (rand > 0.5) {
            return true;
        } else {
            return false;
        }
    }


class Feature {
    private int[] row;
    private int[] col;
    private boolean[] sgn;

    Feature(int[] row, int[] col, boolean[] sgn) {
        this.row = row;
        this.col = col;
        this.sgn = sgn;
    }
}

class Image {
    private int col;
    private int row;
    private int[] numbers;

    Image(int col, int row, int[] numbers) {
        this.col = col;
        this.row = row;
        this.numbers = numbers;
    }
}
}



