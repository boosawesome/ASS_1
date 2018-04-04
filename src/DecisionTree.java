import java.util.*;
import java.io.*;

public class DecisionTree {


    private List<String> categoryNames;


    private void readDataFile(String fname){
        /* format of names file:
         * names of categories, separated by spaces
         * names of attributes
         * category followed by true's and false's for each instance
         */
        System.out.println("Reading data from file "+fname);
        try {
            Scanner din = new Scanner(new File(fname));

            categoryNames = new ArrayList<String>();
            for (Scanner s = new Scanner(din.nextLine()); s.hasNext();) categoryNames.add(s.next());
            int numCategories = categoryNames.size();
            System.out.println(numCategories +" categories");

            List<String> attNames = new ArrayList<String>();
            for (Scanner s = new Scanner(din.nextLine()); s.hasNext();) attNames.add(s.next());
            int numAtts = attNames.size();
            System.out.println(numAtts +" attributes");

            List<Instance> allInstances = readInstances(din);
            din.close();
        }
        catch (IOException e) {
            throw new RuntimeException("Data File caused IO exception");
        }
    }


    private List<Instance> readInstances(Scanner din){
        /* instance = classname and space separated attribute values */
        List<Instance> instances = new ArrayList<Instance>();
        String ln;
        while (din.hasNext()){
            Scanner line = new Scanner(din.nextLine());
            instances.add(new Instance(categoryNames.indexOf(line.next()),line));
        }
        System.out.println("Read " + instances.size()+" instances");
        return instances;
    }



    private class Instance {

        private int category;
        private List<Boolean> vals;

        public Instance(int cat, Scanner s){
            category = cat;
            vals = new ArrayList<Boolean>();
            while (s.hasNextBoolean()) vals.add(s.nextBoolean());
        }

        public boolean getAtt(int index){
            return vals.get(index);
        }

        public int getCategory(){
            return category;
        }

        public String toString(){
            StringBuilder ans = new StringBuilder(categoryNames.get(category));
            ans.append(" ");
            for (Boolean val : vals)
                ans.append(val?"true  ":"false ");
            return ans.toString();
        }

    }

    public static void main(String args[]){
        new DecisionTree();
    }
}
