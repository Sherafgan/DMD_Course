package HW9;

import java.io.IOException;

/**
 * @author Sherafgan Kandov
 *         21.10.2015
 */
public interface IDBStorage {

    public void insert(int tableDeterminant, String id, String name, String emailOrDesignation, String address) throws IOException;

    public void delete(int tableDeterminant, String id) throws IOException;

    public void update(int tableDeterminant, String id, String name, String emailOrDesignation, String address) throws IOException;

    public String search(int tableDeterminant, String id) throws IOException;
}
