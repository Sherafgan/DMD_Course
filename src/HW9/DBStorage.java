package HW9;

import java.io.*;
import java.util.Scanner;

/**
 * @author Sherafgan Kandov
 */
public class DBStorage implements IDBStorage {
    private static final String TAB = "\t";
    private static final String EMPTY = "";
    private static final String SPACE = " ";

    private static final int NOT_FOUND = -1;

    private static final String TABLE_1 = "Students.txt";
    private static final String TABLE_2 = "Employees.txt";

    private static final int ZERO_CHAR = 79; //ASCII

    private static final int CAPACITY = 10000;

    private String[] data;

    private int[] sizesOfTables = new int[2];

    private static final int TABLE_1_INDEX = 0;
    private static final int TABLE_2_INDEX = 1;

    public DBStorage() throws IOException {
        sizesOfTables[TABLE_1_INDEX] = 0;
        sizesOfTables[TABLE_2_INDEX] = 0;

        data = new String[CAPACITY];
        PrintWriter studentTable = new PrintWriter(new BufferedWriter(new FileWriter(TABLE_1)));
        PrintWriter employeeTable = new PrintWriter(new BufferedWriter(new FileWriter(TABLE_2)));

        for (int i = 0; i < data.length; i++) {
            data[i] = EMPTY;
        }

        String metadata = "0\t0";

        data[data.length - 1] = metadata;

        writeDataToArray(studentTable, data);
        studentTable.close();

        writeDataToArray(employeeTable, data);
        employeeTable.close();
    }

    private void writeDataToArray(PrintWriter printWriter, String[] data) {
        for (int i = 0; i < data.length; i++) {
            printWriter.write(data[i]);
            if (i != data.length - 1) {
                printWriter.println();
            }
        }
    }


    @Override
    public void insert(int tableDeterminant, String id, String name, String emailOrDesignation, String address) throws IOException {
        readDataToArray(tableDeterminant);
        String[] metadata = data[data.length - 1].trim().split("\\t");
        String newTuple = id + TAB + name + TAB + emailOrDesignation + TAB + address;
        int indexToInsertTo = Integer.parseInt(metadata[1]);
        data[indexToInsertTo] = newTuple;
        sizesOfTables[tableDeterminant - 1]++;
        String metadataToWrite = metadataGen(Integer.parseInt(metadata[0]), tableDeterminant);
        data[data.length - 1] = metadataToWrite;
        writeDataToArray(tableDeterminant);
    }

    private String metadataGen(int i, int tableDeterminant) {
        return i + "1" + TAB + sizesOfTables[tableDeterminant - 1];
    }

    @Override
    public void delete(int tableDeterminant, String id, String name, String emailOrDesignation, String address) throws IOException {
        readDataToArray(tableDeterminant);
        String[] metadata = data[data.length - 1].trim().split("\\t");
        char[] metadata1 = metadata[0].toCharArray();
        int metadata2 = Integer.parseInt(metadata[1]);
        String tupleToDelete = id + TAB + name + TAB + emailOrDesignation + TAB + address;
        for (int i = 0; i < sizesOfTables[tableDeterminant - 1]; i++) {
            if (tupleToDelete.equals(data[i])) {
                data[i] = EMPTY;
                metadata1[i] = ZERO_CHAR;
                metadata2--;
            }
        }
        sizesOfTables[tableDeterminant - 1] = metadata2;
        data[data.length - 1] = newMetadata(metadata1, metadata2);
        compactData(tableDeterminant, data);
        writeDataToArray(tableDeterminant);
    }

    private void compactData(int tableDeterminant, String[] data) {
        for (int i = 0; i < sizesOfTables[tableDeterminant - 1]; i++) {
            if (data[i].equals(EMPTY)) {
                for (int j = i; j < sizesOfTables[tableDeterminant - 1]; j++) {
                    data[j] = data[j + 1];
                }
            }
        }
        data[sizesOfTables[tableDeterminant - 1]] = EMPTY;
    }

    private String newMetadata(char[] metadata1, int metadata2) {
        String newMetadata1 = EMPTY;
        String newMetadata2 = "" + metadata2;
        for (char c : metadata1) {
            if (c != ZERO_CHAR) {
                newMetadata1 = newMetadata1 + c;
            }
        }
        return newMetadata1 + TAB + newMetadata2;
    }

    @Override
    public void update(int tableDeterminant, String oldId, String oldName, String oldEmailOrDesignation, String oldAddress, String id, String name, String emailOrDesignation, String address) throws IOException {
        int indexToUpdate = search(tableDeterminant, oldId, oldName, oldEmailOrDesignation, oldAddress);
        String newTuple = id + TAB + name + TAB + emailOrDesignation + TAB + address;
        if (indexToUpdate > -1 && indexToUpdate < sizesOfTables[tableDeterminant - 1]) {
            data[indexToUpdate] = newTuple;
        } else {
            throw new FileNotFoundException("NO TUPLE FOUND TO UPDATE!");
        }
        writeDataToArray(tableDeterminant);
    }

    @Override
    public int search(int tableDeterminant, String id, String name, String emailOrDesignation, String address) throws IOException {
        readDataToArray(tableDeterminant);
        String tupleToFind = id + TAB + name + TAB + emailOrDesignation + TAB + address;
        for (int i = 0; i < sizesOfTables[tableDeterminant - 1]; i++) {
            if (data[i].equals(tupleToFind)) {
                return i;
            }
        }

        return NOT_FOUND;
    }

    private void writeDataToArray(int tableDeterminant) throws IOException {
        PrintWriter printWriter;
        if (tableDeterminant == 1) {
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(TABLE_1)));
        } else {
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(TABLE_2)));
        }

        for (int i = 0; i < data.length; i++) {
            printWriter.write(data[i]);
            if (i != data.length - 1) {
                printWriter.println();
            }
        }

        printWriter.close();
    }

    private void readDataToArray(int tableDeterminant) throws FileNotFoundException {
        Scanner scanner;
        if (tableDeterminant == 1) {
            scanner = new Scanner(new File(TABLE_1));
        } else {
            scanner = new Scanner(new File(TABLE_2));
        }

        int i = 0;
        while (scanner.hasNextLine()) {
            data[i] = scanner.nextLine().trim();
            i++;
        }

        scanner.close();
    }
}
