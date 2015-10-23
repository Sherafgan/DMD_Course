package HW9_OLD;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        DBStorage dbStorage = new DBStorage();

        dbStorage.insert(1, "1", "John", "john@gmail.com", "NY");
        dbStorage.insert(1, "2", "Thomas", "thomas@gmail.com", "LA");
        dbStorage.insert(1, "3", "Boris", "boris@gmail.com", "CO");
        dbStorage.insert(1, "4", "Bruce", "bruce@gmail.com", "OR");
        dbStorage.insert(1, "5", "Mark", "mark@gmail.com", "IW");

        dbStorage.delete(1, "3", "Boris", "boris@gmail.com", "CO");

        dbStorage.update(1, "5", "Mark", "mark@gmail.com", "IW", "5", "Joe", "mark@gmail.com", "IW");

        dbStorage.insert(2, "1", "John", "Programmer", "NY");
        dbStorage.insert(2, "2", "Thomas", "Teacher", "LA");
        dbStorage.insert(2, "3", "Boris", "Barber", "CO");
    }
}
