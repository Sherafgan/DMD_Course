package HW9;

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

    private static final int DATA_LENGTH = 1000000;
    private static final int TABLE_1_METADATA_LENGTH = 56000; // 9433 + 1(TAB) + 18 + 270 + 3600 + 42170 = 55492 ~ 56000
    private static final int TABLE_2_METADATA_LENGTH = 76000; // 12 345 + 1(TAB) + 18 + 270 + 3600 + 45000 + 14076 = 75310 ~ 76000

    private static final int TABLE_1_FILE_LENGTH = DATA_LENGTH + TABLE_1_METADATA_LENGTH; // 1 000 000 + 56 000 = 1 056 000
    private static final int TABLE_2_FILE_LENGTH = DATA_LENGTH + TABLE_2_METADATA_LENGTH; // 1 000 000 + 76 000 = 1 076 000

    private static final String TAB = "\t";
    private static final String SPACE = " ";
    private static final String EMPTY = "";

    private static final String[] NAMES_OF_TABLES = new String[]{"Students.txt", "Employees.txt"};

    private static final long METADATA_BEGINNING_POSITION = 1000001;

    private long cursorInFile;

    public DBStorage(int cursorInFile) {
        this.cursorInFile = cursorInFile;
    }

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
        table.seek(METADATA_BEGINNING_POSITION);

        String metadataLine = table.readLine();
        String[] metadata = metadataLine.split("\\t");

        boolean isSpaceBetweenRecords = false;
        int placeToInsert = -1;

        for (int i = 0; i < metadata[0].length(); i++) {
            if (metadata[0].charAt(i) == '0') {
                if (!isSpaceBetweenRecords) {
                    isSpaceBetweenRecords = true;
                    placeToInsert = i;
                }
            }
        }

        String newMetadata;
        if (isSpaceBetweenRecords) {
            table.seek(placeToInsert * sizesOfRecords[tableDeterminant - 1]);
            table.write(newRecord.getBytes());

            char[] part1Chars = metadata[0].toCharArray();
            String newPart1 = EMPTY;
            for (int i = 0; i < part1Chars.length; i++) {
                if (placeToInsert == i) {
                    newPart1 = newPart1 + '1';
                } else {
                    newPart1 = newPart1 + part1Chars[i];
                }
            }

            String[] part2 = metadata[1].split("\\s");
            String newPart2 = EMPTY;
            for (int i = 0; i < part2.length; i++) {
                if (placeToInsert == i) {
                    newPart2 = newPart2 + SPACE + id + SPACE + part2[i];
                } else {
                    newPart2 = newPart2 + SPACE + part2[i];
                }
            }

            newMetadata = newPart1 + TAB + newPart2;
        } else {
            table.seek(cursorInFile);
            table.write(newRecord.getBytes());
            cursorInFile = cursorInFile + newRecord.length();

            metadata[0] = metadata[0] + 1;
            metadata[0] = metadata[0].trim();
            metadata[1] = metadata[1] + SPACE + id;
            metadata[1] = metadata[1].trim();

            newMetadata = metadata[0] + TAB + metadata[1];
        }
        metadata[2] = metadata[2].trim();
        long amountOfRecords = Long.parseLong(metadata[2]);
        amountOfRecords++;
        newMetadata = newMetadata + TAB + amountOfRecords;

        table.seek(METADATA_BEGINNING_POSITION);
        table.write(newMetadata.getBytes());

        table.close();
    }

    @Override
    public void delete(int tableDeterminant, String id) throws IOException {
        int position = searchPos(tableDeterminant, id);
        if (position == -1) {
            System.out.println("RECORD NOT FOUND!");
        } else {
            RandomAccessFile table = new RandomAccessFile(NAMES_OF_TABLES[tableDeterminant - 1], "rws");
            String toWrite = EMPTY;
            for (int i = 0; i < sizesOfRecords[tableDeterminant - 1]; i++) {
                toWrite = toWrite + SPACE;
            }
            table.seek(position);
            table.write(toWrite.getBytes());
            table.seek(METADATA_BEGINNING_POSITION);

            String metadataLine = table.readLine().trim();
            String[] metadataParts = metadataLine.split("\\t");

            char[] part1Metadata = metadataParts[0].toCharArray();
            String[] part2Metadata = metadataParts[1].split("\\s");

            int changer = -1;
            for (int i = 0; i < part2Metadata.length; i++) {
                if (part2Metadata[i].equals(id)) {
                    part2Metadata[i] = EMPTY;
                    changer = i;
                }
            }

            char[] part1MetadataFinal = new char[part1Metadata.length];
            for (int i = 0; i < part1MetadataFinal.length; i++) {
                if (changer != i) {
                    part1MetadataFinal[i] = part1Metadata[i];
                }
            }
            part1MetadataFinal[changer] = '0';

            String newMetadata1 = EMPTY;
            for (int i = 0; i < part1MetadataFinal.length; i++) {
                newMetadata1 = newMetadata1 + part1MetadataFinal[i];
            }

            String newMetadata2 = EMPTY;
            for (int i = 0; i < part2Metadata.length; i++) {
                newMetadata2 = newMetadata2 + SPACE + part2Metadata[i];
                newMetadata2 = newMetadata2.trim();
            }

            metadataParts[2] = metadataParts[2].trim();
            int metadataPart3 = Integer.parseInt(metadataParts[2]);
            metadataPart3--;

            String newMetadata = newMetadata1 + TAB + newMetadata2 + TAB + metadataPart3;

            for (int i = 0; i < (metadataLine.length() - newMetadata.length()) * 6; i++) {
                newMetadata = newMetadata + SPACE;
            }

            table.seek(METADATA_BEGINNING_POSITION);
            table.write(newMetadata.getBytes());

            table.close();
        }
    }

    @Override
    public void update(int tableDeterminant, String id, String name, String emailOrDesignation, String address) throws IOException {
        RandomAccessFile table = new RandomAccessFile(NAMES_OF_TABLES[tableDeterminant - 1], "rws");
        int position = searchPos(tableDeterminant, id);
        String record = makeRecord(tableDeterminant, id, name, emailOrDesignation, address);
        table.seek(position);
        table.write(record.getBytes());
        table.close();
    }

    private static int searchPos(int tableDeterminant, String id) throws IOException {
        int finalPosition;
        RandomAccessFile table = new RandomAccessFile(NAMES_OF_TABLES[tableDeterminant - 1], "rws");
        table.seek(METADATA_BEGINNING_POSITION);
        String metadataLine = table.readLine();
        table.close();
        String[] metadata = metadataLine.trim().split("\\t");
        String[] ids = metadata[1].trim().split("\\s");
        for (int i = 0; i < ids.length; i++) {
            if (ids[i].equals(id)) {
                finalPosition = i * sizesOfRecords[tableDeterminant - 1];
                return finalPosition;
            }
        }
        return -1;
    }

    @Override
    public String search(int tableDeterminant, String id) throws IOException {
        int position = searchPos(tableDeterminant, id);
        if (position == -1) {
            return "RECORD NOT FOUND!";
        } else {
            String record = EMPTY;

            RandomAccessFile table = new RandomAccessFile(NAMES_OF_TABLES[tableDeterminant - 1], "rws");
            table.seek(position);
            byte[] recordInBytes = new byte[sizesOfRecords[tableDeterminant - 1]];
            table.read(recordInBytes);
            for (int i = 0; i < recordInBytes.length; i++) {
                record = record + (char) recordInBytes[i];
            }
            return record;
        }
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

    public long getCursorInFile() {
        return cursorInFile;
    }
}