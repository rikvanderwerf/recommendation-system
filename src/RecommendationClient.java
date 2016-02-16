import java.util.*;

/**
 * Created by rikvanderwerf on 16-2-16.
 */
public class RecommendationClient {

    ISimilarityInterface similarityAlgoritm;
    List<List<Double>> similarityList;

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

                // to do : check if compared user actually has a new preference compared to the target user.

                if (similarityList.size() < maximumNeighbourListSize) {
                    similarityList.add(Arrays.asList((Double) keyValue.getKey(), similarity));

                } else if(similarityList.get(0).get(1) < similarity) {
                    similarityList.set(0, Arrays.asList((Double) keyValue.getKey(), similarity));

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

            userPreferencerIterator.remove();

        }
    }


}
