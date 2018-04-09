import java.io.File;
import java.io.IOException;
import java.util.*;

/*
"Data/ass1-data/part1/iris-test.txt"
"Data/ass1-data/part1/iris-training.txt"
 */
public class KNN {
    public ArrayList testList = new ArrayList<Flower>();
    public ArrayList trainList = new ArrayList<Flower>();

    public ArrayList predictions = new ArrayList<Response>();

    public static void main(String[] args) {
        KNN start = new KNN();
        start.Knn(/*args[0], args[1]*/);
    }


    public double EuclideanDistance(Flower first, Flower second, int length) {
        double distance = 0;
        for (int i = 0; i < length - 1; i++) {
            distance += Math.pow((first.measure[i] - second.measure[i]), 2);
        }
        return Math.sqrt(distance);
    }

    public Flower[] getNeighbours(Flower testInstance, int k) {
        List<FlowerDouble> distances = new ArrayList<>();
        double dist;

        for (int i = 0; i < trainList.size() - 1; i++){
            dist = EuclideanDistance(testInstance, (Flower) trainList.get(i), 3);
            distances.add(new FlowerDouble((Flower) trainList.get(i), dist));
        }
        Collections.sort(distances);

        Flower[] neighbours = new Flower[k];

        for(int i = 0; i < k; i++){
            neighbours[i] = distances.get(i).flower;
        }

        return neighbours;
    }

    public Response getResponses(Flower[] neighbours){
        Set classVotes = new HashSet<Response>();
        Response[] responses = new Response[neighbours.length + 1];
        for(int i = 0; i < neighbours.length - 1; i++){
            responses[i] = new Response(neighbours[i], -1);
            if(classVotes.contains(responses[i])){
                Response temp = responses[i];
                temp.num += 1;
                classVotes.remove(responses[i]);
                classVotes.add(temp);
            }
            else {
                Response temp = responses[i];
                temp.num = 1;
                classVotes.add(responses[i]);
            }
        }
        List list = new ArrayList(classVotes);
        Collections.sort(list);
        Collections.reverse(list);
        return (Response) list.get(0);
    }

    public float getAccuracy(ArrayList testList, ArrayList predictions){
        float correct = 0;
        for(int i = 0; i < testList.size() - 1; i++){
            Flower test = (Flower) testList.get(i);
            String testName = test.name;
            Response predict = (Response) predictions.get(i);
            String predictName = predict.flower.name;
            if (testName.equals(predictName)){
                correct += 1;
            }
        }
        return (correct/(float)testList.size()) * (float) 100.0;
    }

    class Flower {
        double[] measure;

        {
            measure = new double[4];
        }

        String name;

        public Flower(double slength, double swidth, double plength, double pwidth, String name){
            this.measure[0] = slength;
            this.measure[1] = swidth;
            this.measure[2] = plength;
            this.measure[3] = pwidth;
            this.name = name;
        }
    }

    class FlowerDouble implements Comparable<FlowerDouble>{
        Flower flower;
        double num;

        public FlowerDouble(Flower flower, double num){
            this.flower = flower;
            this.num = num;
        }

        @Override
        public int compareTo(FlowerDouble o) {
            if(num <= o.num) return -1;
            else return 1;
        }

    }

    class Response implements Comparable<Response>{
        Flower flower;
        int num;

        public Response(Flower flower, int num){
            this.flower = flower;
            this.num = num;
        }

        @Override
        public int compareTo(Response o) {
            return Integer.compare(num, o.num);
        }
    }

    public void Knn(/*String test, String training*/) {
        File testFile = new File("Data/ass1-data/part1/iris-test.txt");
        File trainingFile = new File("Data/ass1-data/part1/iris-training.txt");
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

        int k = 3;

        for (int i = 0; i < testList.size(); i++){
         Flower[] neighbours = getNeighbours((Flower) testList.get(i), k);
         Response result = getResponses(neighbours);
         predictions.add(result);
         System.out.println("predicted: " + result.flower.name + " | actual: " + ((Flower) testList.get(i)).name);
        }
        Float accuracy = getAccuracy(testList, predictions);
        System.out.print("Accuracy: " + accuracy + "%");
    }

}
