package HW10;

import HW10.BPlusTree.BTree;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Sherafgan Kandov
 *         25.10.2015
 */
public class Practice {
    private static final int ROOT_SIZE = 100000; //100 000
    private static final int LEFT_NODE_SIZE = 1000000;  //1 000 000
    private static final int RIGHT_NODE_SIZE = 1000000; //1 000 000

    private static int rootPointer = 0;
    private static int leftNodePointer = 100001; //100 001
    private static int rightNodePointer = 1100001; //1 100 001

    public static void main(String[] args) throws IOException {
        BTree<Integer, String> bTree = new BTree<>();
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        for (int i = 0; i < 1000000000; i++) {
            String tmp = "" + alphabet[(int) (Math.random() * 26)] + alphabet[(int) (Math.random() * 26)] + alphabet[(int) (Math.random() * 26)] +
                    alphabet[(int) (Math.random() * 26)] + alphabet[(int) (Math.random() * 26)];

            bTree.put(i, tmp);
        }

        System.out.println(bTree.size());
        System.out.println(bTree.get(10));
        System.out.println(bTree.get(555));
        System.out.println(bTree.get(999));

        writeToFile(bTree);
        System.out.println(bTree.getOrder());
    }

    private static void writeToFile(BTree<Integer, String> bTree) throws IOException {
        PrintWriter outFile = new PrintWriter(new FileWriter("Index.txt"));

        writeNode(bTree, outFile, rootPointer, leftNodePointer, rightNodePointer);
    }

    private static void writeNode(BTree<Integer, String> bTree, PrintWriter outFile, int rootPointer, int leftNodePointer, int rightNodePointer) {
        int order = bTree.getOrder();
    }
}
