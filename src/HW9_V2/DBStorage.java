package HW9_V2;

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

    private long[] sizesOfTuples = new long[]{TABLE_1_TUPLE_LENGTH, TABLE_2_TUPLE_LENGTH};

    private static final int DATA_LENGTH = 1000000;
    private static final int TABLE_1_METADATA_LENGTH = 56000; // 9433 + 1(TAB) + 18 + 270 + 3600 + 42170 = 55492 ~ 56000
    private static final int TABLE_2_METADATA_LENGTH = 76000; // 12 345 + 1(TAB) + 18 + 270 + 3600 + 45000 + 14076 = 75310 ~ 76000

    private static final int TABLE_1_FILE_LENGTH = DATA_LENGTH + TABLE_1_METADATA_LENGTH; // 1 000 000 + 56 000 = 1 056 000
    private static final int TABLE_2_FILE_LENGTH = DATA_LENGTH + TABLE_2_METADATA_LENGTH; // 1 000 000 + 76 000 = 1 076 000

    private static final String TAB = "\t";
    private static final String SPACE = " ";
    private static final String EMPTY = "";

    private static final String[] NAMES_OF_TABLES = new String[]{"Students2.txt", "Employees2.txt"};

    private static final long METADATA_BEGINNING_POSITION = 1000001;

    private long cursorInFile;

    public DBStorage() throws IOException {
        PrintWriter studentTable = new PrintWriter(new BufferedWriter(new FileWriter(NAMES_OF_TABLES[0])));
        PrintWriter employeeTable = new PrintWriter(new BufferedWriter(new FileWriter(NAMES_OF_TABLES[1])));

        for (int i = 0; i < TABLE_1_FILE_LENGTH; i++) {
            studentTable.write(SPACE);
        }
        for (int i = 0; i < TABLE_2_FILE_LENGTH; i++) {
            employeeTable.write(SPACE);
        }

        studentTable.close();
        employeeTable.close();

        String initialMetadata = SPACE + TAB + SPACE + TAB + 0;

        RandomAccessFile studentTableRAF = new RandomAccessFile(NAMES_OF_TABLES[0], "rws");
        RandomAccessFile employeeTableRAF = new RandomAccessFile(NAMES_OF_TABLES[1], "rws");

        studentTableRAF.seek(METADATA_BEGINNING_POSITION);
        employeeTableRAF.seek(METADATA_BEGINNING_POSITION);

        studentTableRAF.write(initialMetadata.getBytes());
        employeeTableRAF.write(initialMetadata.getBytes());

        studentTable.close();
        employeeTable.close();

        cursorInFile = 0;
    }


    @Override
    public void insert(int tableDeterminant, String id, String name, String emailOrDesignation, String address) throws IOException {
        String newRecord = makeRecord(tableDeterminant, id, name, emailOrDesignation, address);
        RandomAccessFile table = new RandomAccessFile(NAMES_OF_TABLES[tableDeterminant - 1], "rws");
        table.seek(cursorInFile);
        table.write(newRecord.getBytes());

        table.seek(METADATA_BEGINNING_POSITION);

        String metadataLine = table.readLine();
        String[] metadata = metadataLine.split("\\t");

        if (metadata[0].equals(SPACE)) {
            metadata[0] = "1";
        } else {
            metadata[0] = metadata[0] + 1;
        }
        if (metadata[1].equals(SPACE)) {
            metadata[1] = id;
        } else {
            metadata[1] = metadata[1] + SPACE + id;
        }
        metadata[2] = metadata[2].trim();
        int numberOfRecords = Integer.parseInt(metadata[2]);
        numberOfRecords++;
        metadata[2] = "" + numberOfRecords;
        String newMetadata = metadata[0] + TAB + metadata[1] + TAB + metadata[2];

        table.seek(METADATA_BEGINNING_POSITION);
        table.write(newMetadata.getBytes());

        table.close();
        cursorInFile = cursorInFile + sizesOfTuples[tableDeterminant - 1];
    }

    @Override
    public void delete(int tableDeterminant, String id, String name, String emailOrDesignation, String address) throws IOException {

    }

    @Override
    public void update(int tableDeterminant, String oldId, String oldName, String oldEmailOrDesignation, String oldAddress, String id, String name, String emailOrDesignation, String address) throws IOException {

    }

    @Override
    public long search(int tableDeterminant, String id, String name, String emailOrDesignation, String address) throws IOException {
        return 0;
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