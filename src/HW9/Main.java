package HW9;

import java.io.IOException;

/**
 * @author Sherafgan Kandov
 *         22.10.2015
 */
public class Main {
    private static final int STUDENTS_TABLE = 1;
    private static final int EMPLOYEES_TABLE = 2;

    public static void main(String[] args) throws IOException {
        DBStorage dbStorage = new DBStorage();

        dbStorage.insert(STUDENTS_TABLE, "1", "Alexander", "Alexander@gmail.com", "New York, Marks Avenue, 103");
        dbStorage.insert(STUDENTS_TABLE, "2", "Boris", "Boris@gmail.com", "Los Angeles, Cicero Av. 30");
//        System.out.println(dbStorage.search(STUDENTS_TABLE, "1"));
//        System.out.println(dbStorage.search(STUDENTS_TABLE, "2"));
//
//        dbStorage.update(STUDENTS_TABLE, "2", "John", "John@gmail.com", "Colorado, Doe Av., 98/1");
//        System.out.println(dbStorage.search(STUDENTS_TABLE, "2"));
//
//        dbStorage.delete(STUDENTS_TABLE, "1");
//        System.out.println(dbStorage.search(STUDENTS_TABLE, "1"));
    }
}