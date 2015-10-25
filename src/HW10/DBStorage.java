package HW10;

import HW10.BPlusTree.BTree;

import java.io.*;

/**
 * @author Sherafgan Kandov
 *         21.10.2015
 */
public class DBStorage implements IDBStorage {
    private static final int ID_LENGTH = 5;
    private static final int NAME_LENGTH = 20;
    private static final int EMAIL_LENGTH = 40;
    private static final int DESIGNATION_LENGTH = 15;
    private static final int ADDRESS_LENGTH = 40;

    private static final int TABLE_1_TUPLE_LENGTH = ID_LENGTH + NAME_LENGTH + EMAIL_LENGTH + ADDRESS_LENGTH; // 105 + 1(TAB) = 106
    private static final int TABLE_2_TUPLE_LENGTH = ID_LENGTH + NAME_LENGTH + DESIGNATION_LENGTH + ADDRESS_LENGTH; // 80 + 1(TAB) = 81

    private static int[] sizesOfRecords = new int[]{TABLE_1_TUPLE_LENGTH, TABLE_2_TUPLE_LENGTH};

    private static final int DATA_FILE_LENGTH = 1000000;

    private static final String TAB = "\t";
    private static final String SPACE = " ";
    private static final String EMPTY = "";

    private static final String[] NAMES_OF_TABLES = new String[]{"Students.txt", "Employees.txt"};

    private static final String INDEX_FILE_NAME = "Index.txt";
    private static final String DFN_ABC = "DFN_ABC.txt";
    private static final String DFN_DEF = "DEF.txt";
    private static final String DFN_GHI = "GHI.txt";
    private static final String DFN_JKL = "JKL.txt";
    private static final String DFN_MNO = "MNO.txt";
    private static final String DFN_PQR = "PQR.txt";
    private static final String DFN_STU = "STU.txt";
    private static final String DFN_VWX = "VWX.txt";
    private static final String DFN_YZ = "YZ.txt";

    private static final String[] DATA_FILE_NAMES = {DFN_ABC, DFN_DEF, DFN_GHI, DFN_JKL, DFN_MNO, DFN_PQR, DFN_STU, DFN_VWX, DFN_YZ};

    private static String emptyData = getEmptyData();

    private static String getEmptyData() {
        String emptyData = EMPTY;
        for (int i = 0; i < DATA_FILE_LENGTH; i++) {
            emptyData = emptyData + SPACE;
        }
        return emptyData;
    }

    public DBStorage() throws IOException {
        //creating B+ tree
        BTree<String, Integer> bTree = new BTree<>();
        FileOutputStream fos = new FileOutputStream(INDEX_FILE_NAME);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(bTree);
        oos.close();

        for (int i = 0; i < DATA_FILE_NAMES.length; i++) {
            RandomAccessFile randomAccessFile = new RandomAccessFile(DATA_FILE_NAMES[i], "rw");
            randomAccessFile.write(emptyData.getBytes());
            randomAccessFile.close();
            System.out.println("FILE " + i + " WRITTEN!");
        }
    }


    @Override
    public void insert(int tableDeterminant, String id, String name, String emailOrDesignation, String address) throws IOException {
        //TODO
    }

    @Override
    public void delete(int tableDeterminant, String id, String name, String emailOrDesignation, String address) throws IOException {
        //TODO
    }

    @Override
    public void update(int tableDeterminant, String id, String name, String emailOrDesignation, String address) throws IOException {
        //TODO
    }

    @Override
    public String search(int tableDeterminant, String id) throws IOException {
        //TODO
        return null;
    }

    private String makeRecord(int tableDeterminant, String id, String name, String emailOrDesignation, String address) {
        String record;

        String finalID = EMPTY;
        for (int i = 0; i < ID_LENGTH - id.length(); i++) {
            finalID = finalID + SPACE;
        }
        finalID = finalID + id;

        String finalName = EMPTY;
        for (int i = 0; i < NAME_LENGTH - name.length(); i++) {
            finalName = finalName + SPACE;
        }
        finalName = finalName + name;

        String finalEmailOrDesignation = EMPTY;
        if (tableDeterminant == 1) {
            for (int i = 0; i < EMAIL_LENGTH - emailOrDesignation.length(); i++) {
                finalEmailOrDesignation = finalEmailOrDesignation + SPACE;
            }
        } else {
            for (int i = 0; i < DESIGNATION_LENGTH - emailOrDesignation.length(); i++) {
                finalEmailOrDesignation = finalEmailOrDesignation + SPACE;
            }
        }
        finalEmailOrDesignation = finalEmailOrDesignation + emailOrDesignation;

        String finalAddress = EMPTY;
        for (int i = 0; i < ADDRESS_LENGTH - address.length(); i++) {
            finalAddress = finalAddress + SPACE;
        }
        finalAddress = finalAddress + address;

        record = finalID + finalName + finalEmailOrDesignation + finalAddress;

        return record;
    }
}