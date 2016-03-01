import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by rikvanderwerf on 16-2-16.
 */
public class RecommendationClient {

    ISimilarityInterface similarityAlgoritm;
    ArrayList<ArrayList<Double>> similarityList = new ArrayList<ArrayList<Double>>();


    public RecommendationClient(ISimilarityInterface similarityAlgoritm) {
        this.similarityAlgoritm = similarityAlgoritm;
    }

    public ArrayList<ArrayList<Double>> calculateSimilarity(int targetUserId,
                                      HashMap<Integer, HashMap<Integer,Preference>> userPreferences,
                                      int maximumNeighbourListSize,
                                      double minimumSimilarity) {
        ArrayList<Integer> usefulUsers = new ArrayList<Integer>();
        HashMap<Integer, Preference> targetUserPreferences = userPreferences.get(targetUserId);
        userPreferences.remove(targetUserId);

        Iterator userPreferencerIterator = userPreferences.entrySet().iterator();

        while (userPreferencerIterator.hasNext()) {
            boolean usefulUser = false;
            Map.Entry keyValue = (Map.Entry) userPreferencerIterator.next();
            HashMap<Integer, Preference> userPreference = (HashMap<Integer, Preference>) keyValue.getValue();

            for (Preference preference : userPreference.values()) {
                if (targetUserPreferences.get(preference.subject) == null) {
                    usefulUser = true; //User has a new preference to offer and is therefore relevant.
                }
            }

            if (!usefulUser){
                continue;
            }

            System.out.println(keyValue.getKey());
            double similarity = similarityAlgoritm.calculate(targetUserPreferences, userPreference);

            if (similarity >= minimumSimilarity) {
                ArrayList<Double> userSimilarityList = new ArrayList<Double>();
                userSimilarityList.add(Double.parseDouble(keyValue.getKey().toString()));
                userSimilarityList.add(similarity);
                if (similarityList.size() < maximumNeighbourListSize) {
                    similarityList.add(userSimilarityList);

                } else if(similarityList.get(0).get(1) < similarity) {
                    similarityList.set(0, userSimilarityList);

                } else{
                    continue;
                }

                // sort the similarity list based on similarity value. Only if mutated!
                Collections.sort(similarityList, new Comparator<List<Double>>()
                {
                    public int compare(List<Double> o1, List<Double> o2)
                    {
                        return o1.get(1).compareTo(o2.get(1));
                    }
                });
            }
        }
        return similarityList;
    }


}
