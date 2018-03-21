import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class KNN {
    public ArrayList testList = new ArrayList<Flower>();
    public ArrayList trainList = new ArrayList<Flower>();

    public void Knn() {
        File testFile = new File("iris-test.txt");
        File trainingFile = new File("iris-training.txt");
        Scanner scTest = null;
        Scanner scTrain = null;
        try {
            scTest = new Scanner(testFile);
            scTrain = new Scanner(trainingFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (scTest != null && scTest.hasNext())
            testList.add(NewFlower(scTest.nextDouble(), scTest.nextDouble(), scTest.nextDouble(), scTest.nextDouble(), scTest.next()));

        while (scTrain != null && scTrain.hasNext())
            trainList.add(NewFlower(scTest.nextDouble(), scTest.nextDouble(), scTest.nextDouble(), scTest.nextDouble(), scTest.next()));

    }

    public double EuclideanDistance(double[] first, double[] second, int length) {
        double distance = 0;
        for (int i = 0; i < length; i++) {
            distance += Math.pow((first[i] - second[i]), 2);
        }
        return Math.sqrt(distance);
    }

    public Flower[] getNeighbours(int k) {
        double[] distance;
        double dist = 0;
        int length = testList.size() - 1;
        for (int i = 0; i < trainList.size(); i++){

        }
        return new Flower[0];
    }

    private Flower NewFlower(double slength, double swidth, double plength, double pwidth, String name) {
        Flower newFlower = new Flower();
        newFlower.slength = slength;
        newFlower.swidth = swidth;
        newFlower.plength = plength;
        newFlower.pwidth = pwidth;
        newFlower.name = name;
        return newFlower;
    }

    class Flower {
        double slength;
        double swidth;
        double plength;
        double pwidth;
        String name;
    }

    public static void main(String[] args) {
        KNN start = new KNN();
        start.Knn();
    }

}
