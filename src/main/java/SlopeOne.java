import java.util.*;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * Created by Robin on 04/04/16.
 */
public class SlopeOne {
    Table<Integer, Integer, Deviation> deviationTable = HashBasedTable.create();

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
}