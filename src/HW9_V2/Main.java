package HW9_V2;

import java.io.IOException;

/**
 * @author Sherafgan Kandov
 *         22.10.2015
 */
public class Main {
    public static void main(String[] args) throws IOException {
        DBStorage dbStorage = new DBStorage();

        dbStorage.insert(1, "1", "John", "john@gmail.com", "NY");
        dbStorage.insert(1, "2", "Thomas", "thomas@gmail.com", "LA");
        dbStorage.insert(1, "3", "Boris", "boris@gmail.com", "CO");
        dbStorage.insert(1, "4", "Bruce", "bruce@gmail.com", "OR");
        dbStorage.insert(1, "5", "Mark", "mark@gmail.com", "IW");
    }
}