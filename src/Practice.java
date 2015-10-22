import java.io.IOException;
import java.io.RandomAccessFile;

public class Practice {
    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile("outPractice.txt", "rws");



        file.close();
    }
}
