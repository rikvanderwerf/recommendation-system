import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by robin on 16-2-16.
 */
public interface ISimilarityInterface {

    public double calculate(HashMap<Integer, Preference> targetUser, HashMap<Integer, Preference> compareUser);
}


