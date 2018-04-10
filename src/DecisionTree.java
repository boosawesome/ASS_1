import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;


public class DecisionTree {


    private List<String> categoryNames;
    private List<List<Instance>> split;

    private int k = 2;
    private int treeSize = 112;

    public File convertTxt(String fname) throws IOException {
        String arffName = fname.substring(0, fname.length() - 3) + ".arff";

        Scanner stringScan = new Scanner(new File(fname));
        String classes = stringScan.nextLine();
        StringTokenizer classTokens = new StringTokenizer(classes);
        String attributes = stringScan.nextLine();
        StringTokenizer attTokens = new StringTokenizer(attributes);

        File returnable = new File(arffName);

        PrintWriter printWriter = new PrintWriter(arffName);

        printWriter.println("@RELATION " + arffName);
        printWriter.println();

        while(attTokens.hasMoreTokens()) {
            printWriter.println("@ATTRIBUTE " + attTokens.nextToken() + "  STRING");
        }

        printWriter.print("@ATTRIBUTE class {" );
        while(classTokens.hasMoreTokens()){
            printWriter.print(classTokens.nextToken());
            if (classTokens.hasMoreTokens()) printWriter.print(",");
        }

        printWriter.println("}");

        printWriter.println("@DATA");

        while(stringScan.hasNextLine()){
            String dataLine = stringScan.nextLine();
            StringTokenizer dataTokens = new StringTokenizer(dataLine);
            String instanceClass = dataTokens.nextToken();

            while(dataTokens.hasMoreTokens()){
                printWriter.print(dataTokens.nextToken() + ",");
            }
            printWriter.println(instanceClass);
        }

        return returnable;
    }

    public void run(String fname) {

    }

    private List<List<Instance>> getSplit(Node node, int maxDepth, int minSize, int depth) {
        Node True = node.True;
        Node False = node.False;

        node.delete();


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
            instances.add(new Instance(categoryNames.indexOf(line.next()), line));
        }
        System.out.println("Read " + instances.size() + " instances");
        return instances;
    }

    private class Node {
        private Node True;
        private Node False;

        private String attName;

        private Float prob;


        public Node(Float prob) {
            this.prob = prob;
            this.True = null;
            this.False = null;
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

        public Float getProb() {
            return prob;
        }

        public String getAttName() {
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

    private class Leaf {
        private String className;
        private int count;
        private Float prob;

        public void report(String indent) {
            if (count == 0)
                System.out.format("%sUnknown\n", indent);
            else
                System.out.format("%sClass %s, prob=$4.2f\n",
                        indent, className, prob);
        }
    }


    private class Instance {

        private int category;
        private List<Boolean> vals;

        public Instance(int cat, Scanner s) {
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

    public static void main(String args[]) {
        new DecisionTree().run("Data/ass1-data/part2/hepatitis.dat");
    }
}
