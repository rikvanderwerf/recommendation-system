import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by rikvanderwerf on 9-2-16.
 */
public class Main {

    static UserPreferences<Integer, HashMap<Integer, Preference>> userPreferences = new UserPreferences<Integer, HashMap<Integer, Preference>>(
            new HashMap<Integer, Preference>()
    );
    static ArrayList<Integer> subjectList = new ArrayList<Integer>();

    public static void main(String[] args) {
        try {
            openFile("./data/u.data");
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println(userPreferences);

        RecommendationClient pearsonClient = new RecommendationClient(new Pearson());
        //RecommendationClient cosineClient = new RecommendationClient(new Cosine());
        //RecommendationClient EuclidedeanClient = new RecommendationClient(new Euclidedean());

        // e.1
        ArrayList neighbours = pearsonClient.calculateSimilarity(186, userPreferences, 25, 0.35);
        System.out.println(pearsonClient.calculateRatings(neighbours, subjectList, 8, 3));
        //System.out.println(cosineClient.calculateSimilarity(7, userPreferences, 3, 0.35));
        //System.out.println(EuclidedeanClient.calculateSimilarity(2, userPreferences, 3, 0.25));
    }

    private static void openFile(String path) throws IOException {
        FileReader fr = new FileReader(path);
        BufferedReader textReader = new BufferedReader(fr);

        String line;
        while ((line = textReader.readLine()) != null) {
            String[] data = line.split("\\s+");
            Integer subjectID = Integer.parseInt(data[1]);
            buildDict(new User(Integer.parseInt(data[0])),
                    new Preference(Double.parseDouble(data[2]),
                            subjectID)
            );
            subjectList.add(subjectID);
        }
        // Make sure each subject exist only once
        subjectList = new ArrayList<Integer>(new LinkedHashSet<Integer>(subjectList));
        textReader.close();
    }

    private static void buildDict(User user, Preference preference) {
        userPreferences.putMap(user.id, preference);
    }
}
