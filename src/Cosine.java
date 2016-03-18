import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Cosine implements ISimilarityInterface{
    public double calculate(HashMap<Integer, Preference> targetUser, HashMap<Integer, Preference> compareUser){
        Iterator targetPreferences = targetUser.entrySet().iterator();
        double similarity;

        double sumMultiplicationRatings = 0;
        double targetPreferencePowSum = 0;
        double comparePreferencePowSum = 0;

        while (targetPreferences.hasNext()){
            Map.Entry keyValue = (Map.Entry)targetPreferences.next();

            Preference targetPreference = (Preference) keyValue.getValue();
            Preference comparePreference = compareUser.get(targetPreference.subject);

            // if we can not find the target's preference in the preference of the user we're comparing to
            // we create a new preference with the rating of 0 to fill in the blank
            if (comparePreference == null){
                comparePreference = new Preference(0, targetPreference.subject);
            }

            sumMultiplicationRatings += (targetPreference.rating * comparePreference.rating);
            targetPreferencePowSum += Math.pow(targetPreference.rating, 2);
            comparePreferencePowSum += Math.pow(comparePreference.rating, 2);
        }

        similarity = sumMultiplicationRatings / (Math.sqrt(targetPreferencePowSum) * Math.sqrt(comparePreferencePowSum));
        return similarity;
    }
}
