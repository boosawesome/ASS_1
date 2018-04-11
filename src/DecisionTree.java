import java.io.*;
import java.util.*;


public class DecisionTree {


    private List<String> categoryNames;

    private String probableClass;
    private Float probableProb;

    private DecisionTree(String training, String test) {
        List<Instance> allInstances = readDataFile(training);
        List<Instance> testInstances = readDataFile(test);
        Set<Instance> instances = new HashSet<>(allInstances);

        String a = allInstances.get(0).name;
        int a1 = 0;
        String b = "";
        int b1 = 0;
        for (int i = 1; i < allInstances.size(); i++) {
            if (!allInstances.get(i).equals(a)) {
                b = allInstances.get(i).name;
            }
        }
        for (int x = 0; x < allInstances.size(); x++) {
            if (allInstances.get(x).name.equals(a)) {
                a1++;
            } else if (allInstances.get(x).name.equals(b)) {
                b1++;
            }
        }

        if (a1 >= b1) {
            probableClass = a;
            probableProb = (float) (a1 / allInstances.size());
        } else {
            probableClass = b;
            probableProb = (float) (b1 / allInstances.size());
        }
        Node root = buildTree(instances, categoryNames);

        printTree(root);

        List<String> actual = null;
        for (Instance i : testInstances) {
            actual.add(classify(root, i));
        }
    }

    public static void main(String args[]) {
        new DecisionTree(args[0], args[1]);
    }

    private Node buildTree(Set<Instance> instances, List<String> attributes) {
        if (instances.isEmpty()) {
            //return leaf baseline predictor
            Leaf returnLeaf = new Leaf(probableProb);
            returnLeaf.className = probableClass;
            return returnLeaf;
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
            String a = attributes.get(0);
            int a1 = 0;
            String b = "";
            int b1 = 0;

            for (int i = 1; i < attributes.size(); i++) {
                if (!attributes.get(i).equals(a)) {
                    b = attributes.get(i);
                }
            }

            for (String s : attributes) {
                if (s.equals(a)) {
                    a1++;
                } else if (s.equals(b)) {
                    b1++;
                }
            }

            if (a1 >= b1) {
                Leaf returnLeaf = new Leaf((float) a1 / attributes.size());
                returnLeaf.className = a;
                return returnLeaf;
            } else if (b1 > a1) {
                Leaf returnLeaf = new Leaf((float) b1 / attributes.size());
                returnLeaf.className = b;
                return returnLeaf;
            }
        } else {
            Float purity = 0f;
            int attribute = 0;
            Set<Instance> instanceTrue = new HashSet<>();
            Set<Instance> instanceFalse = new HashSet<>();

            for (int i = 0; i < attributes.size(); i++) {
                Set<Instance> tempTrue = new HashSet<>();
                Set<Instance> tempFalse = new HashSet<>();

                for (Instance y : instances) {
                    if (y.vals.get(i)) {
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

            return returnable;
        }
        return null;
    }

    private List<Instance> readDataFile(String fname) {
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

            return readInstances(din);
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

    private Float calculatePurity(Set<Instance> instances) {
        int difference = 0;
        String s = instances.iterator().next().name;
        for (Instance i : instances) {
            if (!i.name.equals(s)) {
                difference++;
            }
        }

        return (float) 1 - (difference / instances.size());
    }

    private void printTree(Node root) {
        while (root.True != null || root.False != null) {
            if (root.True != null) {
                root.True.report(" | ");
                if (root.True != null) {
                    printTree(root.True);
                }
                if (root.False != null) {
                    printTree(root.False);
                }
            }
            if (root.False != null) {
                root.False.report(" | ");
                if (root.False != null) {
                    printTree(root.False);
                }
                if (root.True != null) {
                    printTree(root.True);
                }
            }
        }
    }

    private String classify(Node root, Instance test) {
        while (!root.isLeaf()) {
            int i;
            for (i = 0; i < categoryNames.size(); i++) {
                if (categoryNames.get(i).equals(root.attName)) {
                    break;
                }
            }

            if (test.getAtt(i)) {
                root = root.True;
            } else if (!test.getAtt(i)) {
                root = root.getFalse();
            }
        }

        return root.getAttName();
    }


    private class Node {
        private Node True;
        private Node False;

        private int att;
        private String attName = categoryNames.get(att);


        Node(int att, Node t, Node f) {
            this.True = t;
            this.False = f;
            this.att = att;
        }

        Node(Float prob) {
        }

        public Node getTrue() {
            return True;
        }

        public Node getFalse() {
            return False;
        }

        public boolean isLeaf() {
            return false;
        }


        String getAttName() {
            return attName;
        }

        public void report(String indent) {
            System.out.format("%s%s = True:\n",
                    indent, attName);
            False.report(indent + "    ");
            System.out.format("%s%s = False:\n",
                    indent, attName);
            True.report(indent + "    ");
        }
    }

    private class Leaf extends Node {
        private String className;
        private int count;
        private Float prob;

        Leaf(Float prob) {
            super(prob);
            this.prob = prob;
        }

        public boolean isLeaf() {
            return true;
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

        Instance(String n, int cat, Scanner s) {
            name = n;
            category = cat;
            vals = new ArrayList<Boolean>();
            while (s.hasNextBoolean()) vals.add(s.nextBoolean());
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
}
