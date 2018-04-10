import java.io.*;
import java.util.*;


public class DecisionTree {


    private List<String> categoryNames;
    private List<List<Instance>> split;

    private String probableClass;

    public static void main(String args[]) {
        new DecisionTree().run("Data/ass1-data/part2/hepatitis.dat");
    }

    public void run(String fname) {

    }

    private List<List<Instance>> getSplit(Node node, int maxDepth, int minSize, int depth) {
        Node True = node.True;
        Node False = node.False;

        node.delete();


        return null;
    }

    public Node buildTree(Set<Instance> instances, List<Boolean> attributes) {
        if (instances.isEmpty()) {
            //return baseline predictor
        }

        for (Instance x : instances) {
            for (Instance i : instances) {
                if (!(x.name.equals(i.name))) {
                    break;
                } else {
                    Leaf returnable = new Leaf(1f);
                    returnable.className = i.name;
                    return returnable;
                }
            }

        }

        if (attributes.isEmpty()) {
            //return leaf containing name/probability of majority class
        } else {
            Float purity = 0f;
            int attribute = 0;
            Set<Instance> instanceTrue = new HashSet<>();
            Set<Instance> instanceFalse = new HashSet<>();

            for (int i = 0; i < attributes.size(); i++) {
                Set<Instance> tempTrue = new HashSet<>();
                Set<Instance> tempFalse = new HashSet<>();

                for (Instance y : instances) {
                    if (y.vals.get(i) == true) {
                        tempTrue.add(y);
                    } else tempFalse.add(y);
                }
                Float averagePurity = (calculatePurity(tempTrue) + calculatePurity(tempFalse)) / 2;
                if (averagePurity > purity) {
                    attribute = i;
                    instanceTrue = tempTrue;
                    instanceFalse = tempFalse;
                }
            }
            attributes.remove(attribute);
            Node left = buildTree(instanceTrue, attributes);
            Node right = buildTree(instanceFalse, attributes);
            Node returnable = new Node(attribute, left, right);

            returnable.True = left;
            returnable.False = right;

//            returnable.attName = attribute;
        }
        return null;
    }

    private void readDataFile(String fname) {
        /* format of names file:
         * names of categories, separated by spaces
         * names of attributes
         * category followed by true's and false's for each instance
         */
        System.out.println("Reading data from file " + fname);
        try {
            Scanner din = new Scanner(new File(fname));

            categoryNames = new ArrayList<String>();
            for (Scanner s = new Scanner(din.nextLine()); s.hasNext(); ) categoryNames.add(s.next());
            int numCategories = categoryNames.size();
            System.out.println(numCategories + " categories");

            List<String> attNames = new ArrayList<String>();
            for (Scanner s = new Scanner(din.nextLine()); s.hasNext(); ) attNames.add(s.next());
            int numAtts = attNames.size();
            System.out.println(numAtts + " attributes");

            List<Instance> allInstances = readInstances(din);
            din.close();
        } catch (IOException e) {
            throw new RuntimeException("Data File caused IO exception");
        }
    }

    private List<Instance> readInstances(Scanner din) {
        /* instance = classname and space separated attribute values */
        List<Instance> instances = new ArrayList<Instance>();
        String ln;
        while (din.hasNext()) {
            Scanner line = new Scanner(din.nextLine());
            String name = line.next();
            instances.add(new Instance(name, categoryNames.indexOf(line.next()), line));
        }
        System.out.println("Read " + instances.size() + " instances");
        return instances;
    }

    private Float calculatePurity(Set<Instance> Instances) {

        return 0f;
    }

    private class Node {
        private Node True;
        private Node False;

        private int att;



        public Node(int att, Node t, Node f) {
            this.True = t;
            this.False = f;
        }

        public Node(Float prob) {
        }

        public void delete() {
            this.False = null;
            this.True = null;
        }

        public Node getTrue() {
            return True;
        }

        public Node getFalse() {
            return False;
        }


//        public String getAttName() {
//            return attName;
//        }
//
//        public void report(String indent) {
//            System.out.format("%s%s = True:\n",
//                    indent, attName);
//            False.report(indent + "    ");
//            System.out.format("%s%s = False:\n",
//                    indent, attName);
//            True.report(indent + "    ");
//        }
    }

    private class Leaf extends Node {
        private String className;
        private int count;
        private Float prob;

        public Leaf(Float prob) {
            super(prob);
        }

        public void report(String indent) {
            if (count == 0)
                System.out.format("%sUnknown\n", indent);
            else
                System.out.format("%sClass %s, prob=$4.2f\n",
                        indent, className, prob);
        }
    }

    private class Instance {

        private String name;
        private int category;
        private List<Boolean> vals;

        public Instance(String n, int cat, Scanner s) {
            name = n;
            category = cat;
            vals = new ArrayList<Boolean>();
            while (s.hasNextBoolean()) vals.add(s.nextBoolean());
        }

        public boolean getAtt(int index) {
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
}
