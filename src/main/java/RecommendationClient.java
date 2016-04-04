import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by rikvanderwerf on 16-2-16.
 */
public class RecommendationClient {

    ISimilarityInterface similarityAlgoritm;
    ArrayList<ArrayList<Double>> similarityList = new ArrayList<ArrayList<Double>>();
    HashMap<Integer, HashMap<Integer,Preference>> userPreferences;


    public RecommendationClient(ISimilarityInterface similarityAlgoritm) {
        this.similarityAlgoritm = similarityAlgoritm;
    }

    public ArrayList<ArrayList<Double>> calculateSimilarity(int targetUserId,
                                      HashMap<Integer, HashMap<Integer,Preference>> userPreferences,
                                      int maximumNeighbourListSize,
                                      double minimumSimilarity) {
        this.userPreferences = userPreferences;
        HashMap<Integer, Preference> targetUserPreferences = userPreferences.get(targetUserId);

        // remove targer user from scope to prevent being compared with itself
        userPreferences.remove(targetUserId);

        //create iterator to iterate over userPreferences
        Iterator userPreferenceIterator = userPreferences.entrySet().iterator();
        while (userPreferenceIterator.hasNext()) {
            boolean usefulUser = false;
            Map.Entry keyValue = (Map.Entry) userPreferenceIterator.next();
            HashMap<Integer, Preference> userPreference = (HashMap<Integer, Preference>) keyValue.getValue();

            // determine if the user has a new rating to offer
            for (Preference preference : userPreference.values()) {
                if (targetUserPreferences.get(preference.subject) == null) {
                    usefulUser = true; //User has a new preference to offer and is therefore relevant.
                }
            }

            // if the user has no new preferences compared to the target user it's not useful to do any calculations
            if (!usefulUser){
                continue;
            }

            // calculate the similarity using the chosen algorithm
            double similarity = similarityAlgoritm.calculate(targetUserPreferences, userPreference);

            // check if the similarity is above the threshold
            if (similarity >= minimumSimilarity) {
                //create a mapping of users and their simimarity to the target user
                ArrayList<Double> userSimilarityList = new ArrayList<Double>();
                userSimilarityList.add(Double.parseDouble(keyValue.getKey().toString()));
                userSimilarityList.add(similarity);

                // if the similarity list smaller than the maximum size always append
                if (similarityList.size() < maximumNeighbourListSize) {
                    similarityList.add(userSimilarityList);

                // if the list is full determine if the current similarity is higher than the lowest one in the list
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
        // add the target user to the scope again
        userPreferences.put(targetUserId, targetUserPreferences);
        return similarityList;
    }

    public Double calculateRating(ArrayList nearestNeighbours, int subjectID, int minOccurences) {
        Double totalSimilarity = 0.0;
        Double totalWeight = 0.0;
        Integer occurenceCounter = 0;
        Iterator<ArrayList<Double>> neighbourIterator = nearestNeighbours.iterator();


        while (neighbourIterator.hasNext()) {
            ArrayList<Double> neighbourMapping = neighbourIterator.next();

            Double neighbourSimilarity = neighbourMapping.get(1);
            try {
                Double neighbourRating = getNeighbourRating(userPreferences.get(neighbourMapping.get(0).intValue()),
                        subjectID);
                totalSimilarity += neighbourSimilarity;
                totalWeight += neighbourSimilarity * neighbourRating;
                occurenceCounter++;
            } catch(NullPointerException e){
                // if the subject isn't found, skip
                continue;
            }
        }

        if(occurenceCounter < minOccurences){
            return null;
        }
        return totalWeight / totalSimilarity;
    }

    public ArrayList<ArrayList<Double>> calculateRatings(ArrayList nearestNeighbours, ArrayList subjects, Integer amount,
                                                         Integer minOccurences) {
        ArrayList<ArrayList<Double>> subjectRatings = new ArrayList<ArrayList<Double>>();
        Iterator<Integer> subjectIterator = subjects.iterator();

        while (subjectIterator.hasNext()) {
            Integer subjectID = subjectIterator.next();
            Double  rating = calculateRating(nearestNeighbours, subjectID, minOccurences);
            if(rating == null){
                // No rating found, skip subject
                continue;
            }

            ArrayList<Double> subjectRating = new ArrayList<Double>();
            subjectRating.add(Double.parseDouble(subjectID.toString()));
            subjectRating.add(rating);

            // if the list if not full yet, always insert
            if(subjectRatings.size() < amount){
                subjectRatings.add(subjectRating);
                // if the current rating is bigger than the smallest rating on the list, replace
            } else if(subjectRatings.get(0).get(1) < rating){
                subjectRatings.set(0, subjectRating);
                // we're not inserting so no need to sort the list
            } else {
                continue;
            }

            // sort the subject rating list based on rating value. Only if mutated! #zombie
            Collections.sort(subjectRatings, new Comparator<List<Double>>() {
                public int compare(List<Double> o1, List<Double> o2)
                {
                    return o1.get(1).compareTo(o2.get(1));
                }
            });
        }

        return subjectRatings;
    }

    private double getNeighbourRating(HashMap<Integer, Preference> neighbourPreferences, Integer subjectID){
        return neighbourPreferences.get(subjectID).rating;
    }
}
