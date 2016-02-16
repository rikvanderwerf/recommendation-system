import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Pearson implements ISimilarityInterface{
    public double calculate(HashMap<Integer, Preference> targetUser, HashMap<Integer, Preference> compareUser){
        Iterator targetPreferences = targetUser.entrySet().iterator();
        double similarity = 0;
        double targetRatingSum = 0;
        double compareRatingSum = 0;
        double iterationSum = 0;
        double targetPowSum = 0;
        double comparePowSum = 0;
        double totalSum = 0;

        while (targetPreferences.hasNext()){
            Map.Entry keyValue = (Map.Entry)targetPreferences.next();

            Preference targetPreference = (Preference) keyValue.getValue();
            Preference comparePreference = (Preference) compareUser.get(targetPreference.subject);

            targetRatingSum += targetPreference.rating;
            compareRatingSum += comparePreference.rating;

            targetPowSum += Math.pow(targetPreference.rating, 2);
            comparePowSum += Math.pow(comparePreference.rating, 2);

            iterationSum += (targetPreference.rating * comparePreference.rating);
        }

        totalSum = (targetRatingSum + compareRatingSum) / targetUser.size();
        double upperEquation = iterationSum - totalSum;



        return similarity;
    }
}
