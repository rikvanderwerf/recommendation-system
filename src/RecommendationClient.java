import java.util.*;

/**
 * Created by rikvanderwerf on 16-2-16.
 */
public class RecommendationClient {

    ISimilarityInterface similarityAlgoritm;
    SortedMap<Integer, Double> simularityMapping;

    public RecommendationClient(ISimilarityInterface similarityAlgoritm) {
        this.similarityAlgoritm = similarityAlgoritm;
    }

    public double calculateSimularity(int targetUserId,
                                      HashMap<Integer, HashMap<Integer,Preference>> userPreferences,
                                      int maximumNeighbourListSize,
                                      double minimumSimularity) {

        HashMap<Integer, Preference> targetUserPreferences = userPreferences.get(targetUserId);
        userPreferences.remove(targetUserId);
        Iterator userPreferencerIterator = userPreferences.entrySet().iterator();

        while (userPreferencerIterator.hasNext()) {
            Map.Entry keyValue = (Map.Entry) userPreferencerIterator.next();

            HashMap<Integer, Preference> userPreference = (HashMap<Integer, Preference>) keyValue.getValue();

            double similarity = similarityAlgoritm.calculate(targetUserPreferences, userPreference);

            if (similarity >= minimumSimularity) {
                if (simularityMapping.size() < maximumNeighbourListSize) {
                    simularityMapping.put((Integer) keyValue.getKey(), similarity);
                } else {
                    

                }
            }
            simularityMapping.put()

            userPreferencerIterator.remove();

        }
    }


}
