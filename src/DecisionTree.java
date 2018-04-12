import java.util.*;
import java.io.*;


public class DecisionTree {
    private List<String> categoryNames;
    private List<String> attNames;
    private List<Instance> allInstances;
    private Node root;

    private DecisionTree(String s) {
        readDataFile(s);
        Set<Instance> so = new HashSet<>(allInstances);
        List<String> list = new ArrayList<>(attNames);
        root = constructTree(so, list);
        nonLeafReport("", root);
    }

    public static void main(String[] args) {
        new TestDTL(args[0], args[1]);
    }

    private void nonLeafReport(String indent, Node n) {
        if (n.getLeftNode() != null) {
            System.out.format("%s%s = True:\n", indent, n.getName());
            nonLeafReport(indent + " ", n.getLeftNode());
        }
        if (n.getRightNode() != null) {
            System.out.format("%s%s = False:\n", indent, n.getName());
            nonLeafReport(indent + " ", n.getRightNode());
        }
        if (n.getLeftNode() == null && n.getRightNode() == null) {
            leafReport(indent, n);
        }
    }

    private void leafReport(String indent, Node n) {
        System.out.format("%sClass %s, prob= " + n.getProbability() + "\n", indent, n.getName());
    }

    private Node constructTree(Set<Instance> instances, List<String> attributes) {

        String bestAtt;
        Set<Instance> bestTrueInstances = new HashSet<>();
        Set<Instance> bestFalseInstances = new HashSet<>();

        if (instances.isEmpty())
            return getBaseLine();

        int c = 0;
        int b = 0;

        for (Instance i : instances) {
            if (i.category == 0) b++;
            else c++;
        }
        if (c == 0 || b == 0)
            return leafNodeContainingNameOfClass(instances);
        else if (attributes.isEmpty())
            return leafNodeOfMajorityClass(instances);
        else {
            double best = 1000;
            bestAtt = "";
            for (String s : attributes) {
                Set<Instance> trueInstances = getTrueInstances(instances, s);
                Set<Instance> falseInstances = getFalseInstances(instances, s);
                double d = computePurityOfAttribute(s, trueInstances, falseInstances);

                if (d < best) {
                    bestAtt = s;
                    best = d;
                    bestTrueInstances = trueInstances;
                    bestFalseInstances = falseInstances;
                }
            }
        }
        attributes.remove(bestAtt);
        Node l = constructTree(bestTrueInstances, attributes);
        Node r = constructTree(bestFalseInstances, attributes);
        System.out.println(bestAtt);
        return new Node(bestAtt, l, r);
    }

    private float computePurityOfAttribute(String s, Set<Instance> trueInstances, Set<Instance> falseInstances) {
        System.out.println(trueInstances.isEmpty() + "" + falseInstances.isEmpty());
        float tru = 0;
        float fal = 0;
        float catOneOnFalse = 0;
        float catTwoOnFalse = 0;
        float catOneOnTrue = 0;
        float catTwoOnTrue = 0;
        float aImpurity = 0;
        float bImpurity = 0;
        int l;

        for (l = 0; l < attNames.size(); l++) {
            if (attNames.get(l).equals(s))
                break;
        }

        for (Instance i : trueInstances) {
            if (i.getAtt(l)) {
                tru++;
                if (i.getCategory() == 0)
                    catOneOnTrue++;
                else
                    catTwoOnTrue++;
            }
        }
        for (Instance i : falseInstances) {
            if (!i.getAtt(l)) {
                fal++;
                if (i.getCategory() == 0)
                    catOneOnFalse++;
                else
                    catTwoOnFalse++;
            }
        }

        float probA = tru / (tru + fal);
        float probB = fal / (tru + fal);
        if (catOneOnTrue + catTwoOnTrue != 0) {
            aImpurity = 2 * (catOneOnTrue / (catOneOnTrue + catTwoOnTrue)) * (catTwoOnTrue / (catOneOnTrue + catTwoOnTrue));
        }
        if (catTwoOnFalse + catOneOnFalse != 0)
            bImpurity = 2 * (catOneOnFalse / (catOneOnFalse + catTwoOnFalse)) * (catTwoOnFalse / (catOneOnFalse + catTwoOnFalse));

        float d = ((probA * aImpurity) + (bImpurity * probB));
        System.out.println(d);
        return d;
    }

    private Set<Instance> getFalseInstances(Set<Instance> instances, String s) {
        Set<Instance> falseSet = new HashSet<>();
        int l;
        for (l = 0; l < attNames.size(); l++) {
            if (attNames.get(l).equals(s))
                break;
        }
        for (Instance i : instances) {
            if (!i.getAtt(l)) {
                falseSet.add(i);
            }
        }
        return falseSet;
    }

    private Set<Instance> getTrueInstances(Set<Instance> instances, String s) {
        Set<Instance> trueSet = new HashSet<>();
        int l;
        for (l = 0; l < attNames.size(); l++) {
            if (attNames.get(l).equals(s)) {
                break;
            }
        }
        for (Instance i : instances) {
            if (i.getAtt(l)) {
                trueSet.add(i);
            }
        }
        return trueSet;
    }

    private Node leafNodeOfMajorityClass(Set<Instance> instances) {
        double catOne=0;
        double catTwo=0;
        for(Instance i:instances) {
            if(i.getCategory()==0)
                catOne++;
            else
                catTwo++;
        }
        if(catOne>catTwo) {
            Node n= new Node(categoryNames.get(0),null,null);
            n.setProbability(catOne/(catOne+catTwo));
            return n;
        }
        else {
            Node n= new Node(categoryNames.get(1),null,null);
            n.setProbability(catTwo/(catOne+catTwo));
            return n;
        }
    }

    private Node leafNodeContainingNameOfClass(Set<Instance> instances) {
        Instance inst=null;
        for(Instance i:instances)
            inst=i;
        assert inst != null;
        Node n= new Node(categoryNames.get(inst.getCategory()),null,null);
        n.setProbability(1);
        return n;
    }

    private Node getBaseLine() {
        double catOne=0;
        double catTwo=0;
        for(Instance i:allInstances) {
            if(i.getCategory()==0)
                catOne++;
            else
                catTwo++;
        }
        if(catOne>catTwo) {
            Node n= new Node(categoryNames.get(0),null,null);
            n.setProbability(catOne/(catOne+catTwo));
            return n;
        }
        else {
            Node n= new Node(categoryNames.get(1),null,null);
            n.setProbability(catTwo/(catOne+catTwo));
            return n;
        }
    }

    private String getPredictedCategory(Instance instance) {
        Node n = root;
        while (!(n.getLeftNode() == null & n.getRightNode() == null)) {
            String s = n.getName();
            int i;
            for (i = 0; i < allInstances.size(); i++) {
                if (attNames.get(i).equals(s))
                    break;
            }
            if (instance.getAtt(i))
                n = n.getLeftNode();
            else if (!instance.getAtt(i))
                n = n.getRightNode();
        }
        return n.getName();
    }

    private void readDataFile(String fname) {
        System.out.println("Reading data from file " + fname);
        try {
            Scanner din = new Scanner(new File(fname));
            categoryNames = new ArrayList<>();
            for (Scanner s = new Scanner(din.nextLine()); s.hasNext(); )
                categoryNames.add(s.next());
            int numCategories = categoryNames.size();
            System.out.println(numCategories + " categories");

            attNames = new ArrayList<>();
            for (Scanner s = new Scanner(din.nextLine()); s.hasNext(); )
                attNames.add(s.next());
            int numAtts = attNames.size();
            System.out.println(numAtts + " attributes");

            allInstances = readInstances(din);
            din.close();
        } catch (IOException e) {
            throw new RuntimeException("Data File caused IO exception");
        }
    }

    private List<Instance> readInstances(Scanner din) {
        List<Instance> instances = new ArrayList<>();
        while (din.hasNext()) {
            Scanner line = new Scanner(din.nextLine());
            instances.add(new Instance(categoryNames.indexOf(line.next()), line, this.categoryNames));
        }
        System.out.println("Read " + instances.size() + " instances");
        return instances;
    }

    private static class TestDTL {

        private int numCategories;
        private int numAtts;
        private List<String> categoryNames;
        private List<String> attNames;
        private List<Instance> allInstances;

        private TestDTL(String testing, String training) {
            readDataFile(testing);
            test(training);
        }

        private void test(String training) {
            DecisionTree d = new DecisionTree(training);
            double correct = 0;
            for (Instance allInstance : allInstances) {
                String s = d.getPredictedCategory(allInstance);
                System.out.println("\n" + "Predicted category:" + s + ". Actual category: " + categoryNames.get(allInstance.getCategory()));
                if (s.equals(categoryNames.get(allInstance.getCategory()))) {
                    correct++;
                }

            }
            System.out.println("\n" + "Success rate: " + 100 * (correct / allInstances.size()));
        }

        private void readDataFile(String fname) {
            System.out.println("Reading data from file " + fname);
            try {
                Scanner din = new Scanner(new File(fname));
                categoryNames = new ArrayList<>();
                for (Scanner s = new Scanner(din.nextLine()); s.hasNext(); )
                    categoryNames.add(s.next());
                numCategories = categoryNames.size();
                System.out.println(numCategories + " categories");

                attNames = new ArrayList<>();
                for (Scanner s = new Scanner(din.nextLine()); s.hasNext(); )
                    attNames.add(s.next());
                numAtts = attNames.size();
                System.out.println(numAtts + " attributes");

                allInstances = readInstances(din);
                din.close();
            } catch (IOException e) {
                throw new RuntimeException("Data File caused IO exception");
            }
        }

        private List<Instance> readInstances(Scanner din) {
            List<Instance> instances = new ArrayList<>();
            while (din.hasNext()) {
                Scanner line = new Scanner(din.nextLine());
                instances.add(new Instance(categoryNames.indexOf(line.next()), line, categoryNames));
            }
            System.out.println("Read " + instances.size() + " instances");
            return instances;
        }


    }

    public static class Instance {
        int category;
        List<Boolean> vals;
        private List<String> categoryNames;

        Instance(int cat, Scanner s, List<String> catNames) {
            this.categoryNames = catNames;
            category = cat;
            vals = new ArrayList<>();
            while (s.hasNextBoolean())
                vals.add(s.nextBoolean());
        }


        boolean getAtt(int index) {
            return vals.get(index);
        }

        public int getCategory() {
            return category;
        }

        public String toString() {
            StringBuilder ans = new StringBuilder(categoryNames.get(category));
            ans.append(" ");
            for (Boolean val : vals)
                ans.append(val ? "true  " : "false ");
            return ans.toString();
        }
    }

    private class Node implements Comparable<Node>{

        private String name;
        private Node left;
        private Node right;
        private double probability;


        Node(String bestAtt, Node l, Node r) {
            this.left=l;
            this.right=r;
            this.name=bestAtt;
        }

        Node getLeftNode() {return left;}

        Node getRightNode() {return right;}


        String getName() {
            return name;
        }

        double getProbability() {

            // TODO Auto-generated method stub
            return probability;
        }

        void setProbability(double d) {
            this.probability=d;
        }

        @Override
        public int compareTo(Node o) {
            // TODO Auto-generated method stub
            return 0;
        }

    }
}