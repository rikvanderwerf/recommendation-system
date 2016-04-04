import java.util.*;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * Created by Robin on 04/04/16.
 */
public class SlopeOne {
    UserPreferences<Integer, HashMap<Integer, Preference>> userPreferences;
    Table<Integer, Integer, Deviation> deviationTable = HashBasedTable.create();

    public SlopeOne(UserPreferences<Integer, HashMap<Integer, Preference>> userPreferences) {
        this.userPreferences = userPreferences;
    }

    public void computeDeviation(Map.Entry<Integer, Map<Integer, Preference>> subjectUserEntry1,
                                 Map.Entry<Integer, Map<Integer, Preference>> subjectUserEntry2){
        Double deviationValue = 0.0;
        Integer totalUsersRated = 0;
        Deviation deviation = new Deviation();
        Map<Integer, Preference> userMapping1 = subjectUserEntry1.getValue();
        Map<Integer, Preference> userMapping2 = subjectUserEntry2.getValue();
        Integer subjectID1 = subjectUserEntry1.getKey();
        Integer subjectID2 = subjectUserEntry2.getKey();
        Iterator users = userMapping1.entrySet().iterator();


        while (users.hasNext()){
            Map.Entry keyValue = (Map.Entry)users.next();
            Preference preferenceUser1 = (Preference) keyValue.getValue();
            Preference preferenceUser2 = userMapping2.get(keyValue.getKey());

            if (preferenceUser2 == null){
                continue;
            }

            deviationValue += preferenceUser1.rating - preferenceUser2.rating;
            totalUsersRated++;
        }

        if(totalUsersRated == 0){
            return;
        }

        deviation.setDeviationValue(deviationValue / totalUsersRated);
        deviation.setTotalUsersRated(totalUsersRated);
        deviationTable.put(subjectID1, subjectID2, deviation);
    }

    public void computeDeviations(UserPreferences<Integer, HashMap<Integer,Preference>> itemUsersMapping){
        Iterator items = itemUsersMapping.entrySet().iterator();

        while(items.hasNext()){
            Map.Entry itemEntry = (Map.Entry)items.next();
            Iterator comparableItems = itemUsersMapping.entrySet().iterator();
            while (comparableItems.hasNext()){
                Map.Entry compareItemEntry = (Map.Entry)comparableItems.next();
                computeDeviation(itemEntry, compareItemEntry);
            }

        }
    }

    public Double predictRating(HashMap<Integer, Preference> targetUser, Integer subjectID){
        Integer totalUserSum = 0;
        Double computationResult = 0.0;
        Iterator targetUserPreferences = targetUser.entrySet().iterator();

        while(targetUserPreferences.hasNext()){
            Map.Entry keyValue = (Map.Entry)targetUserPreferences.next();
            Preference userPreference = (Preference) keyValue.getValue();
            Deviation deviation = deviationTable.get(subjectID, userPreference.subject);
            computationResult += (userPreference.rating + deviation.getDeviationValue()) * deviation.getTotalUsersRated();
            totalUserSum += deviation.getTotalUsersRated();
        }
        return computationResult / totalUserSum;
    }

    public ArrayList<ArrayList<Double>> computeRecomendations(HashMap<Integer, Preference> targetUser, ArrayList subjects,
                                                              Integer amount){
        Iterator<Integer> subjectIterator = subjects.iterator();
        ArrayList<ArrayList<Double>> subjectRatings = new ArrayList<ArrayList<Double>>();

        while(subjectIterator.hasNext()){
            Integer subjectID = subjectIterator.next();

            Double rating = predictRating(targetUser, subjectID);

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

    public void updateDeviation(Integer userID, Integer subjectID, Double rating){
        Iterator targerUserPreferences = userPreferences.get(userID).entrySet().iterator();

        while(targerUserPreferences.hasNext()){
            Map.Entry keyValue = (Map.Entry)targerUserPreferences.next();
            Preference targetUserPreference = (Preference) keyValue.getValue();
            Deviation currentDeviation = deviationTable.get(subjectID, targetUserPreference.subject);
            Double numerator = (currentDeviation.getDeviationValue() * currentDeviation.getTotalUsersRated()) + (rating - targetUserPreference.rating);

            currentDeviation.setTotalUsersRated(currentDeviation.getTotalUsersRated() + 1);
            currentDeviation.setDeviationValue(numerator / currentDeviation.getTotalUsersRated());

            deviationTable.put(subjectID, targetUserPreference.subject, currentDeviation);
            currentDeviation.setDeviationValue(currentDeviation.getDeviationValue() * -1);
            deviationTable.put(targetUserPreference.subject, subjectID, currentDeviation);
        }
    }
}