import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by rikvanderwerf on 9-2-16.
 */
public class Main {

    public static void main(String[] args) {
        try {
            openFile("./data/userItem.data");
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private static void openFile(String path) throws IOException {
        FileReader fr = new FileReader(path);
        BufferedReader textReader = new BufferedReader(fr);

        String line;
        while ((line = textReader.readLine()) != null) {
            String[] data = line.split(",");
            buildDict(new User(Integer.parseInt(data[0])),
                      new Preference(Float.parseFloat(data[2]),
                                     Integer.parseInt(data[1]))
            );
        }
        textReader.close();
    }

    private static void buildDict(User user, Preference preference) {


    }
}
