import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rikvanderwerf on 9-2-16.
 */
public class Main {

    static UserPreferences<Integer, HashMap<Integer, Preference>> userPreferences = new UserPreferences<Integer, HashMap<Integer, Preference>>(
            new HashMap<Integer, Preference>()
    );

    public static void main(String[] args) {
        try {
            openFile("./data/userItem.data");
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println(userPreferences);

        RecommendationClient pearsonClient = new RecommendationClient(new Pearson());
        RecommendationClient cosineClient = new RecommendationClient(new Cosine());
        RecommendationClient EuclidedeanClient = new RecommendationClient(new Euclidedean());

        // e.1
        ArrayList neighbours = pearsonClient.calculateSimilarity(7, userPreferences, 3, 0);
        System.out.println(pearsonClient.calculateRating(neighbours, 101));
        System.out.println(pearsonClient.calculateRating(neighbours, 103));
        //System.out.println(cosineClient.calculateSimilarity(7, userPreferences, 3, 0.35));
        //System.out.println(EuclidedeanClient.calculateSimilarity(2, userPreferences, 3, 0.25));
    }

    private static void openFile(String path) throws IOException {
        FileReader fr = new FileReader(path);
        BufferedReader textReader = new BufferedReader(fr);

        String line;
        while ((line = textReader.readLine()) != null) {
            String[] data = line.split(",");
            buildDict(new User(Integer.parseInt(data[0])),
                      new Preference(Double.parseDouble(data[2]),
                                     Integer.parseInt(data[1]))
            );
        }
        textReader.close();
    }

    private static void buildDict(User user, Preference preference) {
        userPreferences.putMap(user.id, preference);
    }
}
