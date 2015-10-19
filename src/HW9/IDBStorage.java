package HW9;

import java.io.IOException;

/**
 * @author Sherafgan Kandov
 */
public interface IDBStorage {

    public void insert(int tableDeterminant, String id, String name, String emailOrDesignation, String address) throws IOException;

    public void delete(int tableDeterminant, String id, String name, String emailOrDesignation, String address) throws IOException;

    public void update(int tableDeterminant, String oldId, String oldName, String oldEmailOrDesignation, String oldAddress, String id, String name, String emailOrDesignation, String address) throws IOException;

    public int search(int tableDeterminant, String id, String name, String emailOrDesignation, String address) throws IOException;
}
