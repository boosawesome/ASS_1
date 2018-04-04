import java.io.IOException;
import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

/*
"Data/ass1-data/part1/iris-test.txt"
"Data/ass1-data/part1/iris-training.txt"
 */
public class KNN {
    public ArrayList testList = new ArrayList<Flower>();
    public ArrayList trainList = new ArrayList<Flower>();

    public ArrayList predictions = new ArrayList<String>();
    public final int k = 3;

    public void Knn(String training, String test) {
        File testFile = new File(test);
        File trainingFile = new File(training);
        Scanner scTest = null;
        Scanner scTrain = null;

        try {
            scTest = new Scanner(testFile);
            scTrain = new Scanner(trainingFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (scTest != null && scTest.hasNext())
            testList.add(new Flower(scTest.nextDouble(), scTest.nextDouble(), scTest.nextDouble(), scTest.nextDouble(), scTest.next()));

        while (scTrain != null && scTrain.hasNext())
            trainList.add(new Flower(scTrain.nextDouble(), scTrain.nextDouble(), scTrain.nextDouble(), scTrain.nextDouble(), scTrain.next()));


        for (int i = 0; i < testList.size() - 1; i++) {
            Flower[] neighbours = getNeighbours((Flower) testList.get(i), k);
            String result = getResponses(neighbours);
            predictions.add(result);
            System.out.println("predicted: " + result + " | actual: " + ((Flower) testList.get(i)).name);
        }
        Float accuracy = getAccuracy(testList, predictions);
        System.out.print("Accuracy: " + accuracy + "%");
    }


    public double EuclideanDistance(Flower first, Flower second, int length) {
        double distance = 0;

        for (int i = 0; i < length; i++) {
            distance += Math.pow((first.measure[i] - second.measure[i]), 2);
        }
        return Math.sqrt(distance);
    }

    public Flower[] getNeighbours(Flower testInstance, int k) {
        List<FlowerDouble> distances = new ArrayList<>();
        double dist;
        int length = testInstance.measure.length - 1;

        for (int i = 0; i < trainList.size() - 1; i++) {
            dist = EuclideanDistance(testInstance, (Flower) trainList.get(i), length);
            distances.add(new FlowerDouble((Flower) trainList.get(i), dist));
        }
        Collections.sort(distances);

        Flower[] neighbours = new Flower[k];

        for (int i = 0; i < k; i++) {
            neighbours[i] = distances.get(i).flower;
        }

        return neighbours;
    }

    public String getResponses(Flower[] neighbours) {
        int setosa = 0;
        int versicolor = 0;
        int virginica = 0;

        for (int i = 0; i < neighbours.length; i++) {
         if (neighbours[i].name.equals("Iris-setosa")) setosa++;
         if (neighbours[i].name.equals("Iris-virginica")) virginica++;
         if (neighbours[i].name.equals("Iris-versicolor")) versicolor++;
        }

        if(setosa > versicolor && setosa > virginica){
            System.out.println(setosa + " " + virginica + " " + versicolor);

            return "Iris-setosa";
        }
        else if (versicolor > setosa && versicolor > virginica){
            System.out.println(setosa + " " + virginica + " " + versicolor);

            return "Iris-versicolor";
        }
        System.out.println(setosa + " " + virginica + " " + versicolor);
        return "Iris-virginica";
    }

    public float getAccuracy(ArrayList testList, ArrayList predictions) {
        float correct = 0;
        for (int i = 0; i < testList.size() - 1; i++) {
            Flower test = (Flower) testList.get(i);
            String testName = test.name;
            String predict = (String) predictions.get(i);
            if (testName.equals(predict)) {
                correct += 1;
            }
        }
        return (correct / (float) testList.size()) * (float) 100.0;
    }

    class Flower {
        double[] measure;

        {
            measure = new double[4];
        }

        String name;

        public Flower(double slength, double swidth, double plength, double pwidth, String name) {
            this.measure[0] = slength;
            this.measure[1] = swidth;
            this.measure[2] = plength;
            this.measure[3] = pwidth;
            this.name = name;
        }
    }

    class FlowerDouble implements Comparable<FlowerDouble> {
        Flower flower;
        double num;

        public FlowerDouble(Flower flower, double num) {
            this.flower = flower;
            this.num = num;
        }

        @Override
        public int compareTo(FlowerDouble o) {
            if (num <= o.num) return -1;
            else return 1;
        }

    }


    public static void main(String[] args) {
        KNN start = new KNN();
        start.Knn(args[0], args[1]);
    }

}
