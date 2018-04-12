import java.io.*;
import java.util.*;

public class Perceptron {
    Perceptron(String fname){
        loadData(fname);
    }

    static void main(String args[]){
        new Perceptron(args[0]);
    }

    void loadData(String fname){
        File file = new File(fname);
        try {
            Scanner scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    class Feature{
        int[]row;
        int[]col;

    }
}