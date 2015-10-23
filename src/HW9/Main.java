package HW9;

import java.io.IOException;

/**
 * @author Sherafgan Kandov
 *         22.10.2015
 */
public class Main {
    private static final int STUDENTS = 1;
    private static final int EMPLOYEES = 2;

    public static void main(String[] args) throws IOException {
        DBStorage dbStorage = new DBStorage();

        dbStorage.insert(STUDENTS, "1", "John", "john@gmail.com", "NY");
        dbStorage.insert(STUDENTS, "2", "Thomas", "thomas@gmail.com", "LA");
        dbStorage.insert(STUDENTS, "3", "Boris", "boris@gmail.com", "CO");
        dbStorage.insert(STUDENTS, "4", "Bruce", "bruce@gmail.com", "OR");
        dbStorage.insert(STUDENTS, "5", "Mark", "mark@gmail.com", "IW");

        dbStorage.delete(STUDENTS, "2", "Thomas", "thomas@gmail.com", "LA");

        dbStorage.insert(STUDENTS, "6", "Peter", "peter@gmail.com", "SL");

        System.out.println(dbStorage.search(STUDENTS, "6"));
        System.out.println(dbStorage.search(STUDENTS, "22"));

        dbStorage.update(1, "6", "Robert", "robert@gmail.com", "NY");

        System.out.println(dbStorage.search(STUDENTS, "6"));
    }
}