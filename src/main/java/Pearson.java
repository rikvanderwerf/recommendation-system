import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Pearson implements ISimilarityInterface{
    public double calculate(HashMap<Integer, Preference> targetUser, HashMap<Integer, Preference> compareUser){
        Iterator targetPreferences = targetUser.entrySet().iterator();
        double similarity;
        double totalSum;
        double targetRatingSum = 0;
        double compareRatingSum = 0;
        double iterationSum = 0;
        double targetPowSum = 0;
        double comparePowSum = 0;

        Integer targetPreferencesSize = targetUser.size();

        // iterate of the target preferences
        while (targetPreferences.hasNext()){
            Map.Entry keyValue = (Map.Entry)targetPreferences.next();

            Preference targetPreference = (Preference) keyValue.getValue();
            Preference comparePreference = compareUser.get(targetPreference.subject);

            if (comparePreference == null){
                targetPreferencesSize -= 1;
                continue;
            }

            targetRatingSum += targetPreference.rating;
            compareRatingSum += comparePreference.rating;

            targetPowSum += Math.pow(targetPreference.rating, 2);
            comparePowSum += Math.pow(comparePreference.rating, 2);

            iterationSum += (targetPreference.rating * comparePreference.rating);
        }
        totalSum = (targetRatingSum * compareRatingSum) / targetPreferencesSize;
        double upperEquation = iterationSum - totalSum;
        double leftLowerEquation = Math.sqrt(targetPowSum - (Math.pow(targetRatingSum, 2) / targetPreferencesSize));
        double rightLowerEquation = Math.sqrt(comparePowSum - (Math.pow(compareRatingSum, 2) / targetPreferencesSize));
        similarity = upperEquation / (leftLowerEquation * rightLowerEquation);
        return similarity;
    }
}
