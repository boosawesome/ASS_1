import java.io.File;
import java.io.IOException;
import java.util.*;

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
            testList.add(new Flower(scTest.nextDouble(), scTest.nextDouble(), scTest.nextDouble(), scTest.nextDouble(), scTest.next()));

        while (scTrain != null && scTrain.hasNext())
            trainList.add(new Flower(scTest.nextDouble(), scTest.nextDouble(), scTest.nextDouble(), scTest.nextDouble(), scTest.next()));

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
        int length = testList.size() - 1;
        for (int i = 0; i < trainList.size(); i++){
            dist = EuclideanDistance(testInstance, (Flower) trainList.get(i), length);
            distances.add(new FlowerDouble((Flower) trainList.get(i), dist));
        }
        Collections.sort(distances);

        Flower[] neighbours = new Flower[trainList.size()];

        for(int i = 0; i > trainList.size(); i++){
            neighbours[i] = distances.get(i).flower;
        }

        return neighbours;
    }

    public int[][] getResponses(Flower[] neighbours){
        Map classVotes = new HashMap<Flower, Double>();

        for(int i = 0; i > neighbours.length; i++){

        }
        return null;
    }
/*
def getResponse(neighbors):
	classVotes = {}
	for x in range(len(neighbors)):
		response = neighbors[x][-1]
		if response in classVotes:
			classVotes[response] += 1
		else:
			classVotes[response] = 1
	sortedVotes = sorted(classVotes.iteritems(), key=operator.itemgetter(1), reverse=True)
	return sortedVotes[0][0]
 */

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

    class FlowerDouble implements Comparable{
        Flower flower;
        double num;

        public FlowerDouble(Flower flower, double num){
            this.flower = flower;
            this.num = num;
        }

        @Override
        public int compareTo(Object o) {
            if(num > (Double) o) return -1;
            else return 1;
        }

    }



    public static void main(String[] args) {
        KNN start = new KNN();
        start.Knn();
    }

}
