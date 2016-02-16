import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Euclidedean implements ISimilarityInterface{
    public double calculate(HashMap<Integer, Preference> targetUser, HashMap<Integer, Preference> compareUser){
        Iterator targetPreferences = targetUser.entrySet().iterator();
        float similarity = 0;

        while (targetPreferences.hasNext()){
            Map.Entry keyValue = (Map.Entry)targetPreferences.next();

            Preference targetPreference = (Preference) keyValue.getValue();
            Preference comparePreference = compareUser.get(targetPreference.subject);

            if (comparePreference == null) {
                continue;
            }
            
            similarity += Math.pow((targetPreference.rating - comparePreference.rating), 2);

            targetPreferences.remove();
        }

        return Math.sqrt(similarity);
    }
}
