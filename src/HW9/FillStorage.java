package HW9;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Sherafgan Kandov
 *         24.10.2015
 */
public class FillStorage {
    private static final int ID_LENGTH = 5;
    private static final int NAME_LENGTH = 20;
    private static final int EMAIL_LENGTH = 40;
    private static final int DESIGNATION_LENGTH = 15;
    private static final int ADDRESS_LENGTH = 40;

    private static final int TABLE_1_TUPLE_LENGTH = ID_LENGTH + NAME_LENGTH + EMAIL_LENGTH + ADDRESS_LENGTH; // 105 + 1(TAB) = 106
    private static final int TABLE_2_TUPLE_LENGTH = ID_LENGTH + NAME_LENGTH + DESIGNATION_LENGTH + ADDRESS_LENGTH; // 80 + 1(TAB) = 81

    private static final int STUDENTS = 1;
    private static final int EMPLOYEES = 2;

    private static final int FILL_NUMBER = 1000;

    public static void main(String[] args) throws IOException {
        FillStorage fillStorage = new FillStorage();
        fillStorage.fill();
    }

    public void fill() throws IOException {
        DBStorage dbStorage = new DBStorage();

        ArrayList<String[]> studentsRecords = randomStudentsRecords(FILL_NUMBER);
        ArrayList<String[]> employeesRecords = randomEmployeesRecords(FILL_NUMBER);

        int numberOfStudentsRecords = 0;
        int numberOfEmployeesRecords = 0;

        for (int i = 0; i < FILL_NUMBER; i++) {
            String[] studentRecord = studentsRecords.get(i);
            String[] employeeRecord = employeesRecords.get(i);
            if (studentRecord[0].length() + studentRecord[1].length() + studentRecord[2].length() + studentRecord[3].length() <= TABLE_1_TUPLE_LENGTH) {
                dbStorage.insert(STUDENTS, studentRecord[0], studentRecord[1], studentRecord[2], studentRecord[3]);
                numberOfStudentsRecords++;
                System.out.println("STUDENTS: " + numberOfStudentsRecords);
            }
            if (employeeRecord[0].length() + employeeRecord[1].length() + employeeRecord[2].length() + employeeRecord[3].length() <= TABLE_2_TUPLE_LENGTH) {
                dbStorage.insert(EMPLOYEES, employeeRecord[0], employeeRecord[1], employeeRecord[2], employeeRecord[3]);
                numberOfEmployeesRecords++;
                System.out.println("EMPLOYEES: " + numberOfEmployeesRecords);
            }
        }

        System.out.println("======================================================================");
        System.out.println("TOTAL STUDENT RECORDS ADDED: " + numberOfStudentsRecords);
        System.out.println("TOTAL EMPLOYEES RECORDS ADDED: " + numberOfEmployeesRecords);
        System.out.println("======================================================================");
    }

    private static ArrayList<String[]> randomEmployeesRecords(int n) throws IOException {
        ArrayList<String[]> employeeRecords = new ArrayList<>();

        String[] names = getNames(n);
        String[] designations = getJobs();
        String[] addresses = getAddresses();

        for (int i = 0; i < n; i++) {
            String[] record = new String[4];
            record[0] = "" + (i + 1);
            record[1] = names[i];
            record[2] = designations[(int) (Math.random() * designations.length)];
            record[3] = addresses[i];
            employeeRecords.add(record);
        }

        return employeeRecords;
    }

    private static ArrayList<String[]> randomStudentsRecords(int n) throws IOException {
        ArrayList<String[]> studentsRecords = new ArrayList<>();

        String[] names = getNames(n);
        String[] emails = getEmails(names);
        String[] addresses = getAddresses();

        for (int i = 0; i < n; i++) {
            String[] record = new String[4];
            record[0] = "" + (i + 1);
            record[1] = names[i];
            record[2] = emails[i];
            record[3] = addresses[i];
            studentsRecords.add(record);
        }

        return studentsRecords;
    }

    private static String[] getJobs() throws IOException {
        String[] jobs = new String[130];
        BufferedReader jobsFile = new BufferedReader(new FileReader("Random_Data/jobs.txt"));
        for (int i = 0; i < jobs.length; i++) {
            String tmp = jobsFile.readLine().trim();
            if (tmp.length() < DESIGNATION_LENGTH) {
                jobs[i] = tmp;
            }
        }
        jobsFile.close();

        ArrayList<String> tmp = new ArrayList<>();
        for (int i = 0; i < jobs.length; i++) {
            if (jobs[i] != null && jobs[i].length() > 2) {
                tmp.add(jobs[i]);
            }
        }

        jobs = new String[tmp.size()];
        for (int i = 0; i < jobs.length; i++) {
            jobs[i] = tmp.get(i);
        }

        return jobs;
    }

    private static String[] getAddresses() throws IOException {
        int n = 3000;
        String[] addresses = new String[n];
        BufferedReader addressesFile = new BufferedReader(new FileReader("Random_Data/addresses.txt"));
        for (int i = 0; i < addresses.length; i++) {
            String tmp = addressesFile.readLine().trim() + ", " + addressesFile.readLine().trim();
            if (tmp.length() <= ADDRESS_LENGTH) {
                addresses[i] = tmp;
            }
        }
        addressesFile.close();

        ArrayList<String> tmp = new ArrayList<>();
        for (int i = 0; i < addresses.length; i++) {
            if (addresses[i] != null && addresses[i].length() > 2) {
                tmp.add(addresses[i]);
            }
        }

        addresses = new String[tmp.size()];
        for (int i = 0; i < FILL_NUMBER; i++) {
            addresses[i] = tmp.get(i);
        }

        return addresses;
    }

    private static String[] getEmails(String[] names) {
        String[] emails = new String[names.length];
        for (int i = 0; i < emails.length; i++) {
            emails[i] = names[i] + "@gmail.com";
        }

        return emails;
    }

    private static String[] getNames(int n) throws IOException {
        String[] names = new String[n];
        BufferedReader firstNamesFile = new BufferedReader(new FileReader("Random_Data/firstNames.txt"));
        for (int i = 0; i < names.length; i++) {
            String tmp = firstNamesFile.readLine().trim();
            if (tmp.length() <= NAME_LENGTH) {
                names[i] = tmp;
            }
        }
        firstNamesFile.close();

        return names;
    }
}
