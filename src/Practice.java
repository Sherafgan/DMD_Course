import java.io.IOException;
import java.io.RandomAccessFile;

public class Practice {
    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("outPractice.txt", "rws");

        file.write("1 John john@gmail.com NY".getBytes());
        file.write("\t".getBytes());
        file.write("2 Thomas thomas@gmail.com LA".getBytes());
        file.write("\t".getBytes());
        file.write("3 Boris boris@gmail.com CO".getBytes());
        file.write("\t".getBytes());
        file.write("4 Bruce bruce@gmail.com OR".getBytes());


        String[] tuples = new String[4];
        tuples = file.readUTF().trim().split("\t");

        int pos = tuples[0].getBytes().length + "\t".getBytes().length + tuples[1].getBytes().length + "\t".getBytes().length;
        file.seek(pos);
        file.write("3 mafak mafak@gmail.com CO".getBytes());

        String tmp = file.readLine();
        String tmp2 = "";

        while (tmp != null) {
            
        }
    }
}
